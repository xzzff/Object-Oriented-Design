# Cpp
In Cpp, strings are compared lexographically by default by using `std::basic_string's operator==`. Note that since Cpp allows operator overloading, one could define string's `operator==` however they want.

Note that `std::string::compare()` method returns
* 0 if `s` and `t` are equal
* less than 0 if `s` is less than `t`
* greater than zero if `s` is greater than `t`

When using `operator==` for `std::basic_string` it is implemented by just checking `std::string::compare() == 0`. Most people use `operator==` which is fine as the Assembly emitted by the most modern compilers is the same as that of just invoking `compare` due to an optimization.

An example:
```cpp
    auto s = "hello"s;
    auto t = "HELLO"s;
    if (std::to_lower(t) == s)
    {
        // Strings s and t matched
    }
```

# Java
To compare strings in Java, we have to use the `String.equals()` method. Note that we cannot user `operator==` as that will just compare addresses of the two objects.

An example:
```java
public boolean isEqual(final String s, final String t)
{
    String s = "hello";
    String t = "he";
    t += "llo";
    return s.equals(t); // returns true since both s and t match
}
```

Note that similar to Cpp, Java offers a `compareTo` method for lexicographically comparing two strings and is similar to `std::string::compare()`.
