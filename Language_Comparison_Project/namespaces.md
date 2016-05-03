# Cpp
In Cpp, namespaces are essentially the equivalent of packages in Java -- you use them to group together related classes. Namespaces are used as an important qualifier when resolving collisions in name lookups for the compiler (such as for invoking methods). If the compiler is unsure what method you are trying to call (i.e. from which namespace), an error will be generated.

An example:
```cpp
#include <vector> // from STL

// Imagine I am trying to create my fast vector (say, stack allocated)
// and I want to encapsulate it into its own namespace
namespace vec
{
    template<typename T>
    class vector
    {
        //...
    }
} // end namespace vec

int main()
{
    std::vector<int> v1; // standard vector
    vec::vector<int> v2; // my vec
}
```

We can use `using` directives to specify which namespace we want to use. We can also do many other fun things such as inline namespaces, unnamed namespaces (will generated unique reprentation when nested in a named outer namespace), and others. For now, we just discussed the basic usage of them.

Nested namespace example:
```cpp
namespace ns_1
{
    void func() {}
    namespace ns_2
    {
        void func() {}
    }
}

// To access members of ns_2
using namespace ns_1::ns_2;
// Now we do not need to preface the func method in namespace 2 with ns_1::ns_2::func()
// Instead we can just use func().
```

# Java
In Java, a `package` is a grouping of related types which provides access protection and namespace management. Note that packages in Java are just a folder in a directory for your project. It helps prevent class name collisions similar to that in Cpp. 

The convention for package naming is all lowercase letters. The first non-comment line of a Java source file should be the package. You can import other packages within a source file using the `import` keyword. 

For example,
```java
package com.apple.concurrent.double-check-lock

// Import other packages here

class DoubleCheckLock
{
    //...
}
```
