# Cpp
In Cpp, as of Cpp-14 (and what compiler vendors have supported for the Cpp-17 standard thus far), there is no reflection support. There are proposals for *static* (compile-time) reflection for Cpp-17, but it may not make it in time for the release. The proposal(s) in question may be found [here](http://www.open-std.org/jtc1/sc22/wg21/docs/papers/2015/n4452.pdf) and [also here](https://isocpp.org/files/papers/n3996.pdf).

Note that static reflection is not exactly the same thing as what Java has. It is possible to build dynamic reflection on top of static reflection, but static reflection in its own right is difficult. 

### Hurdles of Reflection
* Pay for what you use
  * A common driving philosophy of Cpp is you do not pay for what you do not use. So why should my code carry around metadata if I may never use or need it? Further, the addition of metadata may inhibit the compiler from optimizating some parts of code. Why should I pay that cost if my code may never need such metadata?
* Template Metaprogramming and Type Traits
  * Simply, reflection is not as vital as it is in C# or Java. Template metaprogramming is how Cpp wins out. When one would normally look to use reflection, nearly always we can write a metaprogram which does the same thing at compile-time. Checking `type_traits` of a type `T`  is easy.


# Java
Some reflection abilities that are supported include:
* Examine object's class at runtime
* Construct object for class at runtime
* Examine a class's field and methods at runtime
* Invoke any method of an object at runtime

Below is an example to invoke a method on an unknown object.
```java

public class Reflection
{
    public static void main(String[] args)
    {
        Foo f = new Foo();
        Method m;
        try
        {
            m = f.getClass().getMethod("print", new Class<?>[0]);
            m.invoke(f);
        }
        catch (Exception e)
        {
            // Do something intelligent
        }
    }
}

class Foo
{
    public void print()
    {
        System.out.println("Hello from Foo!");
    }
}
```
The output of the example would print "Hello from Foo!".