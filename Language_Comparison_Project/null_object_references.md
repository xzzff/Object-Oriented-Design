# Cpp
In Cpp, the keyword `nullptr` is what is used to refer to a pointer that is a null object reference. Note that for backwards compatability, when `nullptr` was introduced in Cpp-11, most vendors just do

```cpp
#define NULL 0
// and since Cpp-11
#define NULL nullptr
```

What this shows you is that while all `0`, `NULL`, and `nullptr` are equivalent, it is best practice to use `nullptr` as technically `NULL` is implementation-defined. This was needed in order for backwards compatibility with older code as well as C code.

An example:
```cpp
    struct A
    {
        int8_t header;
    };

    A* a = nullptr;
    // Do stuff with a here after allocating memory rather than setting to nullptr.
```

# Java
In Java, the keyword for a null object reference is `null`.