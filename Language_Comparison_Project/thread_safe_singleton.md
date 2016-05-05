# Cpp
Before Cpp-11, we would have had to use (buggy!) double-checked locking to implement a Singleton. However, we can just use a static initializer.

```cpp
    class Singleton
    {
        public:
            Singleton& getInstance()
            {
                static Singleton instance;
                return instance;
            }
    };
```
Per the Cpp-11 Standard: 
    "If control enters the declaration concurrently while the variable is being initialized, the concurrent execution shall wait for completion of the initialization."

This puts the work on each compiler vendor to fill in the implementation details, though using the double checked locking pattern (DCLP) is the obvious choice. There is no guarantee that the compiler will use DCLP, but most Cpp-11 compilers do.

# Java
As per the Lecture notes link [here](https://sourcemaking.com/design_patterns/singleton/java/1), below is an example of a thread-safe Singleton using the Initialization on Demand Holder idiom.

```java
    public class Singleton
    {
        // Private c'tor prevents other classes instantiating this
        private Singleton()
        {
        }

        // SingletonHolder loaded on first exec of Singleton.getInstance() or 
        // first access to SingletonHolder.INSTANCE -- NOT before.
        private static class SingletonHolder
        {
            private static final Singleton INSTANCE = new Singleton();
        }

        public static Singleton getInstance()
        {
            return SingletonHolder.INSTANCE;
        }
    }
```

