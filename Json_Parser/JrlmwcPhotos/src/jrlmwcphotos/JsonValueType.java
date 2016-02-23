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
 * The base JSON type representations are defined here. Our specific subclasses
 * will extend this.
 * 
 * To check whether a specific instance is of a particular type, we provide 
 * wrapper methods for checking, such as isString(), isObject(), etc. These
 * are implemented as true in the respective subclasses.
 *
 * @author joe
 */
public abstract class JsonValueType
{
    JsonValueType() {}
    
    /**
     * Detects whether this JsonValueType represents a JsonObject. If so, this
     * value is an instance of JsonObject.
     */
    public boolean isObject()
    {
        return false;
    }
    
    /**
     * Detects whether a JsonValueType represents a JsonArray. If it is,
     * it must be an instance of our subclass, JsonArray.
     */
    public boolean isArray()
    {
        return false;
    }
    
    /**
     * Detects whether this JsonValueType represents a JsonNumber.
     */
    public boolean isNumber()
    {
        return false;
    }
    
    /**
     * Detects whether this JsonValueType represents a JsonString.
     */
    public boolean isString()
    {
        return false;
    }
    
    /**
     * Returns this JsonValueType as a JsonObject if it is a valid JsonObject.
     * Otherwise, an exception is thrown.
     */
    public JsonObject toObject()
    {
        throw new UnsupportedOperationException("JsonValueType not an object: " 
            + toString());
    }
    
    /**
     * Returns this JsonValueType as a JsonArray if it is a valid JsonArray.
     * Otherwise, an exception is thrown.
     */
    public JsonArray toArray()
    {
        throw new UnsupportedOperationException("JsonValueType not an array: " 
            + toString());
    }
    
     /**
     * Returns the JsonValueType as a String if it represents a JSON string.
     * Otherwise, an exception is thrown.
     */
    @Override
    public String toString()
    {
        throw new UnsupportedOperationException("JsonValueType not an array: " 
            + toString());
    }  
    
    public <T extends Number> T valueOf()
    {
        throw new UnsupportedOperationException("JsonValueType not an number: " 
            + toString());
    }
}
