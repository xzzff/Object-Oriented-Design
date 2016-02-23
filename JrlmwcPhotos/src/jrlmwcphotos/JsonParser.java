/*
 * The MIT License
 *
 * Copyright 2016 joe.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jrlmwcphotos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

/**
 *
 * @author joe
 */
public class JsonParser
{
    private String jsonText; // the JSON captured string from the URL
    
    /* String stream reader and buffer for reading character-by-character */
    private final Reader reader;
    private final char[] buffer;
    
    // Nesting level is defined as the level of the heirarchy of objects.
    private static final int MAX_NESTING_LEVEL = 300;
    private int nestingLevel;
    
    private int bufferIndex;
    private int charsRead; // just to keep track of state of stream of chars
    
    /* This has to be an int since stupid Java uses UTF-16 and read()
     * cannot return a byte or a char, so it has to use an int. What a waste
     * of space. Who doesn't love Java?
     */
    private int currentReadVal; // the ASCII value of character read in
    
    private int captureStartIndex;
    private StringBuilder captureBuffer;
    
    /*
     * Given a URL, parse the contents from the web page into text. In our case,
     * we are expecting a web page containing only JSON and we parse the JSON 
     * into a valid string.
     * @param urlStr 
     */
    public JsonParser(final URL jsonUrl)
    {
        URLConnection conn = null;
        try
        {
            conn = jsonUrl.openConnection();
        }
        catch (IOException e)
        {
            System.err.println("Error when opening the JSON");
            System.exit(1);
        }
       
        // Parse the Json from BR using our cool Java 8 fancy ways.
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "UTF-8")))
        {
            jsonText = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException _)
        {
            throw new ParsingException("Error when reading the JSON into a string.");
        }
        
        this.reader = new StringReader(jsonText);
        
        final int bufferSize = Math.min(4096, jsonText.length());
        this.buffer = new char[bufferSize];
        this.captureStartIndex = -1;
    }
    
    JsonValueType parse() throws IOException
    {
        // Don't know why our StreamReader wouldn't be ready, but better to be
        // safe than sorry.
        if (!reader.ready())
        {
            throw new ParsingException("StringReader is not ready to read the"
                + "JSON string stream for some reason.");
        }
       
        read();
        skipWhiteSpace();
        final JsonValueType result = readValueType();
        skipWhiteSpace();
        if (!doneReading())
        {
            throw new ParsingException("Unexpected character.");
        }
        
        // Every good man closes his streams. Why can't Java make these
        // autocloseable again?
        reader.close();
        return result;
    }
    
    /**
     * The magical read method handles reading from the StringReader and
     * adjusting the buffers accordingly.
     * @throws IOException 
     */
    private void read() throws IOException
    {
        if (bufferIndex == charsRead)
        {
            if (captureStartIndex != -1)
            {
                captureBuffer.append(buffer, captureStartIndex, charsRead - captureStartIndex);
                // Reset
                captureStartIndex = 0;
            }
            charsRead = reader.read(buffer, 0, buffer.length);
            bufferIndex = 0;
            // End of stream
            if (charsRead == -1)
            {
                currentReadVal = -1;
                return;
            }
        }
        currentReadVal = buffer[bufferIndex++];
    }
    
    /**
     * Read over whitespace characters. Eat them up!
     */
    private void skipWhiteSpace() throws IOException
    {
        while (Character.isWhitespace(currentReadVal))
        {
            read();
        }
    }
    
    /**
     * StringReader is done with its text streaming if read returns -1, i.e.
     * currentRead (which is the value captured from read) is -1.
     */
    private boolean doneReading()
    {
        return currentReadVal == -1;
    }
    
    /**
     * By looking at the current character, determines how to parse
     * the following characters. The rules are simple:
     * 1) '"' indicates to read a string
     * 2) '[' indicates to read an array
     * 3) '{' indicates to read an object
     * @throws IOException 
     */
    private JsonValueType readValueType() throws IOException
    {
        // Hacky way to get around couple of edge cases with skipping whitespaces
        // everywhere else since everything recurses through this method.
        if (Character.isWhitespace(currentReadVal))
        {
            skipWhiteSpace();
        }
        switch (currentReadVal)
        {
            case '{':
                return readObject();
            case '"':
                return readString();
            case '[':
                return readArray();
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': 
                return readNumber();
            default:
                throw new ParsingException("Unexpected character.");
        }
    }
    
    private JsonString readString() throws IOException
    {
        return new JsonString(readStringContents());
    }
    
    private JsonObject readObject() throws IOException
    {
        // Keep on reading!
        read();
        if (nestingLevel++ >= MAX_NESTING_LEVEL)
        {
            throw new ParsingException("Nesting too deep in object heirarchy!");
        }
        // Create our object now I suppose
        JsonObject obj = new JsonObject();
        skipWhiteSpace(); 
        
        // End of our object must be matched with }
        if (readChar('}'))
        {
            --nestingLevel;
            return obj;
        }
        
        do
        {
            skipWhiteSpace();
            final String key = readKey();
            skipWhiteSpace();
            if (!readChar(':'))
            {
                throw new ParsingException("Expected ':' after reading the key.");
            }
            obj.add(key, readValueType());
            skipWhiteSpace();            
        } while (readChar(','));
        
        if (!readChar('}'))
        {
            // Expected more data to follow or end of object signal
            throw new ParsingException("Expected ',' or '}'");
        }
        
        --nestingLevel;
        return obj;
    }
    
    private JsonValueType readArray() throws IOException
    {
        read();
        if (nestingLevel++ >= MAX_NESTING_LEVEL)
        {
            throw new ParsingException("Nesting too deep in object heirarchy!");
        }
        JsonArray arr = new JsonArray();
        skipWhiteSpace();
        
        // Arrays end with ']'
        if (readChar(']'))
        {
            --nestingLevel;
            return arr;
        }
        
        do
        {
            skipWhiteSpace();
            arr.add(readValueType());
            skipWhiteSpace();
        } while (readChar(','));
        
        if (!readChar(']'))
        {
            throw new ParsingException("Expecting ']' to end the array.");
        }
        
        --nestingLevel;
        return arr;
    }
    
    private JsonValueType readNumber() throws IOException
    {
        startCapturingString();
        readChar('-');
        // Now, currentRead is the first digit read thus far.
        if (!readDigit())
        {
            throw new ParsingException("Error in reading digit.");
        }
        
        if (currentReadVal != '0')
        {
            // More digits to follow
            while (readDigit())
            {
            }
        }
        
        readDecimal();
        return new JsonNumber(endCapturingString());
    }
    
    // Read the characters past the decimal point
    private void readDecimal() throws IOException
    {
        if (!readChar('.'))
        {
            throw new ParsingException("Error in reading the decimal.");
        }
        if (!readDigit())
        {
            throw new ParsingException("Error in reading the digit when "
                + "trying to read the decimal.");
        }
        // Read while you can
        while (readDigit())
        {
        }
    }
    
    private boolean readDigit() throws IOException
    {
        if (!Character.isDigit(currentReadVal))
        {
            return false;
        }
        read();
        return true;
    }
    
    private String readKey() throws IOException
    {
        if (currentReadVal != '"')
        {
            throw new ParsingException("Expected to be reading a key.");
        }
        // Otherwise it was a beginning of a string clearly, so let's
        // read the contents inside the "".
        return readStringContents();
    }
    
    /**
     * Read the characters inside "" and form a string out of it.
     * Example: "hello" returns hello
     */
    private String readStringContents() throws IOException
    {
        read();
        startCapturingString();
        
        while (currentReadVal != '"')
        {
            read();
        }
        
        final String contents = endCapturingString();
        read();
        
        return contents;
    }
    
    /**
     * Create a buffer for capturing the contents of the string inside ""
     */
    private void startCapturingString()
    {
        if (captureBuffer == null)
        {
            captureBuffer = new StringBuilder();
        }
        captureStartIndex = bufferIndex - 1;
    }
    
    /**
     * Capture the current string in the buffer of characters read in.
     * Reset the buffer when done.
     */
    private String endCapturingString()
    {
        final int end = currentReadVal == -1 ? bufferIndex : bufferIndex - 1;
        String contents;
        if (captureBuffer.length() > 0)
        {
          captureBuffer.append(buffer, captureStartIndex, end - captureStartIndex);
          contents = captureBuffer.toString();
          captureBuffer.setLength(0);
        }
        else
        {
          contents = new String(buffer, captureStartIndex, end - captureStartIndex);
        }
        captureStartIndex = -1;
        return contents;
    }
    
    private boolean readChar(final char c) throws IOException
    {
        if (currentReadVal != c)
        {
            return false;
        }
        read();
        return true;
    }
}
