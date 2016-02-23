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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author joe
 */
public class JsonArray extends JsonValueType implements Iterable<JsonValueType>
{
    private final List<JsonValueType> values;
    
    public JsonArray()
    {
        values = new ArrayList<>();
    }
    
    // All for code-generation and one for all!
    public <T extends Number> void add(final T val)
    {
        values.add(JsonValue.valueOf(val));
    }
    
    public void add(final String v)
    {
        values.add(JsonValue.valueOf(v));
    }
    
    public void add(final JsonValueType v)
    {
        if (v == null)
        {
            throw new NullPointerException("Cannot add a null value!");
        }
        values.add(v);
    }
    
    /**
     * I'll support some of the typical List API only for my own convenience.
     * This is not meant to be real library code.
     */
    public int size()
    {
        return values.size();
    }
    
    public boolean isEmpty()
    {
        return values.isEmpty();
    }
    
    public JsonValueType get(final int index)
    {
        return values.get(index);
    }
    
    /**
     * Returns an iterator for the values of the array. Read-only.
     */
    public Iterator<JsonValueType> iterator()
    {
        final Iterator<JsonValueType> itr = values.iterator();
        
        return new Iterator<JsonValueType>()
        {
            public boolean hasNext() { return itr.hasNext(); }
            public JsonValueType next() { return itr.next(); }
            public void remove() { throw new UnsupportedOperationException(); }
        };
    }
    
    @Override
    public boolean isArray()
    {
        return true;
    }
    
    @Override
    public JsonArray toArray()
    {
        return this;
    }
}
