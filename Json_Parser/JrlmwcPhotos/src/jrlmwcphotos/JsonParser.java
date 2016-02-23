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
    /* String stream reader and buffer for reading character-by-character */
    private final Reader reader;
    private final char[] buffer;
    private int bufferIndex;
    
    /*
     * Nesting level is defined as the level of the heirarchy of objects.
     * We do not want to bottom out and get a stack overflow now do we?
     */
    private static final int MAX_NESTING_LEVEL = 300;
    private int nestingLevel;
    
    private int tempReadValue; // just to keep track of state of stream of chars
    
    /* This has to be an int since stupid Java uses UTF-16 and read()
     * cannot return a byte or a char, so it has to use an int. What a waste
     * of space. Who doesn't love Java?
     * 
     * So, this represents the ASCII value of the character read in from read()
     * This will be -1 if the end of the stream has been reached.
     */
    private int currentCharValue;
    
    /**
     * Useful for building up strings from our char buffer.
     */
    private int stringContentBuilderStartIndex;
    private StringBuilder stringContentBuilder;
    
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
        String jsonText = "";
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "UTF-8")))
        {
            jsonText = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException _)
        {
            throw new ParsingException("Error when reading the JSON into a string.");
        }
        
        this.reader = new StringReader(jsonText);
        
        // Trying to get fancy with buffering on a page size with my char buffer
        // This would help a lot in the case where we don't want to flood our
        // char buffer with a huge jsonText.
        final int bufferSize = Math.min(4096, jsonText.length());
        this.buffer = new char[bufferSize];
        this.stringContentBuilderStartIndex = Integer.MIN_VALUE;
        stringContentBuilder = new StringBuilder();
    }
    
    /**
     * The magic method that parses the JSON.
     */
    JsonValueType parse() throws IOException
    {
        // Don't know why our StreamReader wouldn't be ready, but better to be
        // safe than sorry.
        if (!reader.ready())
        {
            throw new ParsingException("StringReader is not ready to read the"
                + "JSON string stream for some reason.");
        }
       
        // Place fat chunk of characters into our buffer and skip the initial
        // whitespace (i.e. read past) if there is any.
        read();
        skipWhitespace();
        
        // Now we get to the juicy part of our buffer, let's read it.
        final JsonValueType result = readValueType();
        
        // May be extra whitespace after } in our case, so let's go ahead
        // and skip that just in case.
        skipWhitespace();
        
        // Better to be safe than sorry. Perhaps unrecognized character,
        // escape sequence, foreign character, who knows what else?
        if (!doneReading())
        {
            throw new ParsingException("Unexpected character when parsing"
                + "the JSON.");
        }
        
        // Every good man closes his streams.
        reader.close();
        return result;
    }
    
    /**
     * The magical read method handles reading from the StringReader and
     * adjusting the buffers accordingly. Inspired from a post on StackOverflow
     * that I cannot seem to find anymore (had used something similar to this in
     * a project a year ago or so).
     * @throws IOException 
     */
    private void read() throws IOException
    {
        if (bufferIndex == tempReadValue)
        {
            if (safeToBuildStringFromBuffer())
            {
                stringContentBuilder = stringContentBuilder.append(
                    buffer, // char buffer
                    stringContentBuilderStartIndex, // offset
                    tempReadValue - stringContentBuilderStartIndex); // # chars
                // Reset index for the next buffer batch
                // i.e. min{4096, jsonText.length()} characters to be buffered in.
                stringContentBuilderStartIndex = 0;
            }
            
            /**
             * Go read a bunch of characters and place them in our buffer.
             * Bulk reading into our buffer will be higher performance than just
             * reading in one character at a time.
             */
            tempReadValue = reader.read(buffer, 0, buffer.length);
            bufferIndex = 0;
            
            // End of stream
            if (tempReadValue == -1)
            {
                currentCharValue = -1;
                return;
            }
        }
        
        currentCharValue = buffer[bufferIndex++];
    }
    
    /* 
     * Clean Code book taught me to act like I am telling a story with my code
     * and make very short methods. Am I doing it right?
     */
    
    // I am just using INT_MIN to represent a sentinel for knowing when
    // my buffer is good or not.
    private boolean safeToBuildStringFromBuffer()
    {
        return stringContentBuilderStartIndex != Integer.MIN_VALUE;
    }
    
    /**
     * Read over whitespace characters. Eat them up!
     */
    private void skipWhitespace() throws IOException
    {
        while (isWhitespace())
        {
            read();
        }
    }
    
    private boolean isWhitespace()
    {
        return Character.isWhitespace(currentCharValue);
    }
    
    private boolean doneReading()
    {
        return currentCharValue == -1;
    }
    
    /**
     * By looking at the current character, determine how to parse
     * the following characters to come. The rules are simple:
     * 1) '"' indicates to read a string
     * 2) '[' indicates to read an array
     * 3) '{' indicates to read an object
     * We know to handle the matching of the respective symbols accordingly
     * in readObject(), readString(), and readyArray().
     * @throws IOException 
     */
    private JsonValueType readValueType() throws IOException
    {
        // Hacky way to get around couple of edge cases with skipping whitespaces
        // everywhere else since everything recurses through this method.
        if (isWhitespace())
        {
            skipWhitespace();
        }
        switch (currentCharValue)
        {
            case '{':
                return readObject();
            case '"':
                return readString();
            case '[':
                return readArray();
            // Treat all of the cases of '-', 0, 1, ..., 9 as reading numbers.
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
        // If I want to populate my object, it would certainly help to make sure
        // I have all of the characters first in case I don't already.
        read();
        if (nestingLevel++ >= MAX_NESTING_LEVEL)
        {
            throw new ParsingException("Nesting too deep in object heirarchy!");
        }
        // Create our object now I suppose
        JsonObject obj = new JsonObject();
        //skipWhitespace(); 
        
        // End of our object must be matched with }
        if (readChar('}'))
        {
            --nestingLevel;
            return obj;
        }
        
        do
        {
            // Leading spaces (indentation and such) before the key, we need to skip it
            // Example:         "status": "ok", 
            skipWhitespace();
            final String key = readKey(); // "status" is the key for above ex
            // There may be junk white space after reading our key, code 
            // defensively.
            skipWhitespace();
            
            // We expect key: value, so we better have read a colon or else.
            if (!readChar(':'))
            {
                throw new ParsingException("Expected ':' after reading the key.");
            }
            obj.add(key, readValueType());
            skipWhitespace();            
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
        // Guess I want the contents in the array
        read();
        if (nestingLevel++ >= MAX_NESTING_LEVEL)
        {
            throw new ParsingException("Nesting too deep in object heirarchy!");
        }
        JsonArray arr = new JsonArray();
        skipWhitespace();
        
        // Arrays end with ']'
        if (readChar(']'))
        {
            --nestingLevel;
            return arr;
        }
        
        do
        {
            skipWhitespace();
            arr.add(readValueType());
            skipWhitespace();
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
        stringContentBuilderStartIndex = bufferIndex - 1;
        readChar('-');
        // Now, currentRead is the first digit read thus far.
        if (!readDigit())
        {
            throw new ParsingException("Error in reading digit.");
        }
        
        if (currentCharValue != '0')
        {
            // More digits to follow
            while (readDigit())
            {
            }
        }
        
        readDecimal();
        return new JsonNumber(readStringFromBuffer());
    }
    
    // Read the characters past the decimal point
    private void readDecimal() throws IOException
    {
        // We better have just read a decimal otherwise we messed up big time
        // in our parsing.
        if (!readChar('.'))
        {
            throw new ParsingException("Error in reading the decimal.");
        }
        if (!readDigit())
        {
            throw new ParsingException("Error in reading the digit when "
                + "trying to read the decimal.");
        }
        // Read digits while you can
        while (readDigit())
        {
        }
    }
    
    private boolean readDigit() throws IOException
    {
        if (!Character.isDigit(currentCharValue))
        {
            return false;
        }
        read();
        return true;
    }
    
    private String readKey() throws IOException
    {
        if (currentCharValue != '"')
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
        stringContentBuilderStartIndex = bufferIndex - 1;
        
        while (currentCharValue != '"')
        {
            read();
        }
        
        final String contents = readStringFromBuffer();
        read();
        
        return contents;
    }
    
    /**
     * Read the current string in the buffer of characters read in.
     * Reset the buffer when done.
     */
    private String readStringFromBuffer()
    {
        final int stringContentBuilderEndIndex = currentCharValue == -1 ? 
            bufferIndex : bufferIndex - 1;
        String contents;
        if (stringContentBuilder.length() > 0)
        {
          stringContentBuilder.append(buffer, 
              stringContentBuilderStartIndex,
              stringContentBuilderEndIndex - stringContentBuilderStartIndex);
          contents = stringContentBuilder.toString();
          // Now that we've captured our contents and stored it, reset the SB.
          stringContentBuilder.setLength(0);
        }
        else
        {
          contents = new String(
              buffer, 
              stringContentBuilderStartIndex,
              stringContentBuilderEndIndex - stringContentBuilderStartIndex);
        }
        stringContentBuilderStartIndex = Integer.MIN_VALUE;
        return contents;
    }
    
    private boolean readChar(final char c) throws IOException
    {
        if (currentCharValue != c)
        {
            return false;
        }
        read();
        return true;
    }
}
