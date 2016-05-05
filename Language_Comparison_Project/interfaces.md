# Overview
Some uses of interfaces include
* defining a contract of methods
* to link unrelated classes with "has-a" capabilities
  * For example, classes implementing Serializable interface may or may not have any relation between them exception being serializable.
* to provide an interchangeable implementation

# Cpp
In Cpp, a class containing only pure virtual methods denotes an interface. Note that in Cpp, an *interface* is a class with only *pure virtual* methods (without any code). On the other hand, an *abstract class* is a class with virtual methods that can be overriddden and contains some code -- but, notably, has *at least one pure virtual method* which makes the class not instantiable.

Also note that since Cpp allows multiple inheritance, one can use many different interfaces and extend from them.

For example, consider the following:
```cpp
class Serializable
{  
    public:
        // Virtual destructor is required if the object may be 
        // deleted through a pointer to Serializable
        virtual ~Serializable();

        virtual std::string serialize() const = 0;
};

class SomeBaseClass
{
    public:
        virtual ~SomeBaseClass();
};

// Implements the Serializable interface
class MyClass : public SomeBaseClass, public virtual Serializable
{
    virtual std::string serialize() const
    {
        // Implementation here
    }
};
```

# Java
In Java, an interface looks like:
```java
public interface IBox
{
    // Some public methods declared here, but not implemented.
    // For example:
    public void setSize(int size);
    public int getSize();
    public int getArea();
}
```

A class may implement an interface by using the `implements` keyword. Note that a class can implement more than one interface at a time and an interface itself can extend one or more other interfaces.

# Key Differences
There are not many differences other than some extra work being required as far as Cpp goes since you need to declare pure virtual methods, ensure you have proper virtual destructors, and so on. Since both languages allow for multiple interface usage (via multiple inheritance in Cpp and just by nature of implementing interfaces in Java), they are on level playing ground as far as that goes.