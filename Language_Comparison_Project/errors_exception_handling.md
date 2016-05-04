# Overview
Exceptions are events that occur during an execution of a program when there is "exceptional behavior" that disrupts the flow of the program. Some examples include dividing by 0, accessing an array out of bounds, etc. 

Exceptions enable you to write the main flow of your code and to deal with the exceptional cases somewhere else in the code. Further, it allows you to group and differentiate error types.

# Java
In Java, all exceptions are of type Throwable whose direct sublcasses are Error and Exception. That is, the Throwable class is the superclass of all errors and exceptions.

### Checked Exceptions
Checked exceptions are the ones that are checked at compile time. The class Exception and any of its subclasses that are not also a subclass of RuntimeException are denoted as being in the group of checked exceptions. Checked Exceptions must be specified using the `throws` keyword.

### Unchecked Exceptions
Contrarily, unchecked exceptions are not checked at compile time. With Java, all exceptions that are subclasses of Error or RuntimeException are unchecked. 

An example where an `ArithmeticException` would be thrown, which is a type of unchecked exception:
```java
class Main
{
    public static void main(String[] args)
    {
        int x = 5;
        int y = 0;
        int z = x/y; // blows up since dividing by 0
    }
}
```

### Try-Catch-Finally Blocks
An example usage followed by explanation
```java
try
{
    // Some code here which may cause an exception to be thrown
}
catch (SomeExceptionType e)
{
    // Do something with the exception
}
catch (AnotherExceptionType e)
{
    // Do something with this type of exception
}
// more exception catching
finally
{
    // Always gets executed
}
```

If you are writing code which may throw exceptions, you should place it within a `try-catch` block. The `finally` block is something that always gets executed unless the application exits.

Each separate catch block is an exception handler to handle the type of exception specified. Note that we *can* catch more than one exception type with one handler. For example:
```java
try
{
    // ...
}
catch (IOException | SQLException e) // catches either of these exceptions
{

}
```

When an exception is thrown within the body of a try statement, the catch clauses of the try statement are examined in the order in which they appear. The first catch clause that is capable of handling the exception is executed; the remaining ones are simply ignored. How Java analyzes the catch clauses to handle an exception is based on type matching: the exception type is matched to the Exception class listed in the catch statement. So if the type matches either because it is of the exact same type or if it is one of the exception's inherited types, it is used.

### Errors
An Error is a subclass of Throwable which is used for *serious* problems that most applications should not catch at all. As such, these are *abnormal* conditions. With Errors, a method is not required to declare them in their `throws` clause. Thus Errors fall in the same group of unchecked exceptions as far as compile-time exception checking goes. Note that an Error is much more fatal than Exception.

### General Guidelines
If we expect that a client can recover from an exception, we should make it a checked exception. Otherwise, if the client cannot do much to recover from the exception, we should make it an unchecked exception.

# Cpp
In Cpp, exception handling was an after-thought after introducing the language initially. That is, earlier versions of Cpp did not support exception handling whatsoever. This is unlike Java where exceptions were included from the beginning and all of its standard libraries communicate problems by means of using exceptions.

Both languages use the keywords `try`, `catch`, and `throw` for exception handling and they mean the same thing. Below we discuss some differences in Cpp in particular.

In Cpp, all exceptions are unchecked. Thus the compiler is not forced to handle or specify the exception whatsoever. It is entirely on the programmer to specify and/or catch the exception(s).

### Types thrown for Exceptions
All types -- primitive and pointer can thrown as exceptions. In Java, only Throwable objects can be thrown as an exception. For example, the following code segment works in Cpp but not in Java.
```cpp
#include <iostream>
int main()
{
    int x = -1;
    try
    {
        // Do some stuff
        if (x < 0) throw x;
    }
    catch(int x)
    {
        std::cout << "Exception occurred with value: " << x << '\n';
    }
    return 0;
}
```

### Catch all clause
In Cpp, there is a "catch all" kind of exceptions clause. 
```cpp
try
{
    // Do some stuff
}
catch (...) // Catch all kinds of exeptions
{
}
```
This is similar to in nature of just catching type `Exception` in Java within a `catch` statement.

Some other remarks:
* Cpp does not have a `finally` block for try-catch-finally.
* In Java, the `throws` keyword is used to list exceptions that can be thrown by a function, whereas Cpp uses the `throw` keyword (not `throws`).
