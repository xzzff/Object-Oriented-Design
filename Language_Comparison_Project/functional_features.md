# Cpp
### Lambda Expressions
Using lambdas allows you to define functions locally at the place of the call. They have the form
``` cpp
    [capture](params) -> return-type {body}
```
An example for adding two numbers inline:
```cpp
    auto sum = [](int64_t x, int64_t y) { return x + y; };
    // Example invocation
    std::cout << sum(5, 10); // prints 15
```
Now with support for auto `decltype` we could even do auto parameters in our capture list for the lambda.
```cpp
    auto genericLambda = [](auto a, auto&& b) { return a < b; };
    // Example invocation
    bool b = genericLambda(3, 3.14); // Different types, no problem
```

Overall, the notion of anonymous functions and their usages are similar in both languages. However, Cpp's capture-list semantics and power is much stronger than Java. Since it is quite complicated with parameter pack capturing (by-copy, by-ref, pack expansions, initializers, etc), we omit the details. See [here] (http://en.cppreference.com/w/cpp/language/lambda) for more information.

### Functions as First Class Objects
To declare functions as first class objects, we can use `std::function<>`. It is a general purpose polymorphic function wrapper. Instances of `std::function` can store, copy, and invoke any `Callable` -- functions, lambdas, bind expressions, other function objects, pointers to member functions, and pointers to data members for example. 

An example storing a free function:
```cpp
    void print_num(int64_t x)
    {
        std::cout << x << '\n';
    }

    // Example invocation
    int main()
    {
        std::function<void>(int64_t) f = print_num;
        f(5); // Calls print_num(5)
    }
```

### Remarks
Some notable lacking functional features include:
* Lazy evaluation
* Software transactional memory

# Java
Just recently in Java 8 did we see somre more functional features. We talk about them in more detail and give examples below.

### Lambda Expressions
A lambda expression can be thought of as a block of code you can pass around so it can be executed later.

For example, for event handling before lambda expression, we would have to do something like the following:
```java
    button.addActionListener(new ActionListener()
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Button clicked.");
        }
    )
```

Now, with lambda expression, we can make this succinct by:

```java
    button.addActionListener(e -> System.out.println("Button clicked in lambda."));
```

Another example:
```java
    List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
    nums.forEach(n -> System.out.println(n));
```

### Streams
Streams add lazy evaluation finally to Java and can help take full advantages of lambdas. Note that this looks similar to LINQ with .NET. 
```java
import java.util.Arrays;
class Main
{
    public static void main(String[] args)
    {
        String[] os = Arrays.asList(args)
            .stream()
            .filter(s -> s.toLowerCase().contains("e"))
            .toArray(String[]::new);
        System.out.println(Arrays.asList(os));
    }
}
```

Streams are a one-time-use Object and are useful for traversing and manipulating data easier and faster. Once the stream has been traversed, it cannot be traversed again. While traversing, streams have the ability to filter, map, and reduce. 

There are two modes for streams: parallel and sequential. Parallel streams use a fork/join parallelism for dividing the work among many processors.

Some examples of sequential and parallel streams:
```java
// Sequential
List <Person> ps = list.getStream.collect(Collectors.toList());

// Parallel
List <Person> ps2 = list.getStream.parallel().collect(Collectors.toList());
```

### Method References
Since a lambda expression can be thought of as an object-less method, it sure would be awesome if we could refer to existing methods instead of always using a lambda. This is where *method references* come in.

For example, imagine you need to filter a list of files based on file types. 
```java
public class FileFilters
{
    public static boolean isPdf(File f)
    {
        // Code here...
    }
    public static boolean isTxt(File f)
    {
        // Code here...
    }
    public static boolean isRtf(File f)
    {
        // Code here...
    }
}
```
Whenever we want to filter a list of files, we could use a method reference, assuming we defined a `getFiles()` method to return a `Stream`.

```java
Stream<File> pdfs = getFiles().filter(FileFilters:isPdf);
Stream<File> txts = getFiles().filter(FileFilters::isTxt);
Stream<File> rtfs = getFiles().filter(FileFilters::isRtf);
```

### Closures
Java 8 has limited support for closures. Lambda expressions cna capture variable from their enclosing scope as long as these are never modified (think of them as being final).

### Functional Interfaces
A functional interface is simply an interface with precisely one abstract method. 
```java
@FunctionalInterface
public interface IWorker
{
    public void doSomeWork();
}
```