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

import java.io.IOException;
import java.net.URL;

/**
 * The entry point into the application. This will create a JSON Parser and
 * parse the contents into our JSON types. Lastly, it prints them for "testing"
 * to see if our parser works correctly.
 * @author joe
 */
public class JrlmwcPhotos
{
    public static void main(String[] args)
    {
        final String jsonUrlStr = "http://dalemusser.net/data/nocaltrip/photos.json";
        URL jsonUrl = null;
        try
        {
            jsonUrl = new URL(jsonUrlStr);
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        
        final JsonParser parser = new JsonParser(jsonUrl);
        JsonValueType result = null;
        try
        {
            result = parser.parse();
        } catch (IOException e)
        {
            System.err.println("Error in parsing the JSON.");
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        
        final JsonObject jsonObject = result.toObject();
        final String status = jsonObject.get("status").toString();
        final String photosPath = jsonObject.get("photosPath").toString();
        
        // Printing the final objects, woot woot!
        System.out.println("status: " + status);
        System.out.println("photosPath: " + photosPath);
        System.out.println();
        
        JsonArray photoArray = jsonObject.get("photos").toArray();
        
        /**
         * Example usage of iteration over JsonArray: in our case, all the
         * photos and their metadata (image, title, description, latitude,
         * longitude, and date).
         * 
         * I should stress the point that these fields in the objects were
         * created dynamically and hence we are not tied to a specific type
         * necessarily. I do a pretty printing of the keys and values which is
         * generic as long as it can be recognized by the output stream (i.e. be
         * represented as a string).
         */
        for (final JsonValueType value : photoArray)
        {
            /**
             * Transform the JsonValueType into a different type: either a
             * subtype of JsonValueType or a string. In our example, we just
             * have objects.
             */
            if (value.isObject())
            {
                final JsonObject obj = value.toObject();
                obj.prettyPrintKeyValues();
            }
            /*
            else if (value.isArray())
            {
                JsonArray arr = value.toArray();
                // Have your way with arr here...
            }
            else if (value.isString())
            {
                String str = value.toString();
                // Have your way with str here...
            }
            else if (value.isNumber())
            {
                // Have your way with numbers too!
            }
            */
        }
    }   
}
