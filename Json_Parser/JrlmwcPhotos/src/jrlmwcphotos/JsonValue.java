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

/**
 * Just a wrapper class around converting primitive Java types
 * to our JsonValueType.
 * @author joe
 */
public final class JsonValue
{
    // Better for people not to instantiate this beast.
    private JsonValue() {}
   
    // Code-gen is nifty, I guess. :)
    public static <T extends Number> JsonValueType valueOf(final T value)
    {
       return new JsonNumber(value.toString());
    }
    
    public static JsonValueType valueOf(final String value)
    {
        return new JsonString(value);
    }
}
