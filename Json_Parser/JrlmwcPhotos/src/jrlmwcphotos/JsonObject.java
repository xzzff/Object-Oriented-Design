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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.List;

/**
 *
 * @author joe
 */
public class JsonObject extends JsonValueType
{
    /**
     * This uses a lot more memory than I need. I could maintain
     * two lists (one for keys, one for values), but that is a 
     * bit of effort. Meh.
     */
    private Map<String, JsonValueType> jsonObjectsMap;
    
    public JsonObject()
    {
        // We need to maintain the same ordering for the metadata, so let's
        // opt for a LinkedHashMap as a TreeMap or HashMap will not suffice.
        jsonObjectsMap = new LinkedHashMap<>();
    }
    
    public <T extends Number> JsonObject add(final String key, final T value)
    {
        add(key, JsonValue.valueOf(value));
        return this;
    }
    
    public JsonObject add(final String key, final JsonValueType value)
    {
        jsonObjectsMap.put(key, value);
        return this;
    }
    
    public JsonValueType get(final String key)
    {
        return jsonObjectsMap.get(key);
    }
    
    public int size()
    {
        return jsonObjectsMap.size();
    }
    
    public Set<String> keys()
    {
        return jsonObjectsMap.keySet();
    }
    
    public Collection<JsonValueType> values()
    {
        return jsonObjectsMap.values();
    }
    
    public void prettyPrintKeyValues()
    {
        final List<String> keys = new ArrayList<>(keys());
        final List<JsonValueType> values = new ArrayList<>(values());
        
        for (int i = 0; i < keys.size(); ++i)
        {
            System.out.println(keys.get(i) + ": " + values.get(i).toString());
        }
        System.out.println();
    }
    
    @Override
    public boolean isObject()
    {
        return true;
    }
    
    @Override
    public JsonObject toObject()
    {
        return this;
    }
}
