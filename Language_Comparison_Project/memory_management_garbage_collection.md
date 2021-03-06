# Cpp
In Cpp, memory management *can* be done completely by the programmer by using the `new` and `delete` keywords to request and delete memory for objects, respectively. Since those are very similar to `malloc` and `free` in many ways, we do not discuss them as many readers are familiar with the details.

Overall, memory management in Cpp is done through constructors, destructors, and smart pointers.

Instead, we just focus on how memory management is easier in modern cpp thanks to things like `std::unique_ptr` and `std::shared_ptr`. These constructs known as "smart pointers" make managing dynamically created objects very simply with easy ownership semantics.

### RAII
Cpp relies heavily on RAII, also known as *Resource Acquistion Is Initialization*. RAII is a technique which binds the life cycle of a resource to the lifetime of an object. It can be summarized as follows as stated here at [CppReference](http://en.cppreference.com/w/cpp/language/raii).
* Encapsulate each resource into a class where
  * Constructor acquired resources, establishes class invariants, or throws an exception if such things cannot be done.
  * Destructor releases the resource and never throws exceptions
* Always use the resource via an instance of an RAII-class that itself either
  * Has temporary lifetme itself or automatic storage duration or 
  * Has lifetime bounded by lifetime of an automatic or temporary object.

Below is an example using smart pointers.
```cpp
#include <memory> // for std::unique_ptr

class SomeBigObject
{
    void someMethod();
}

int main()
{
    std::unique_ptr<SomeBigObject> p = std::make_unique<SomeBigObject>();
}
```

# Java
Java approaches the memory management problem by having automatic garbage collection. This it the process of looking at heap memory, identifying which objects are in use and not in use, and deleting the unused objects. We think of in-use objects as those that still have a reference count and the others are unreferenced (have a reference count of 0), ie it is not referenced in any part of our program anymore. Hence the memory can be reclaimed for the unreferenced objects.

There is not much to say about garbage collection as it is apparent and straightforward for the most part. Some criteria for when an object becomes eligible for garbage collection include
* All references to said object are set to null.
* The object is created inside a block scope and the reference goes out of scope once the control flow exists that block.
* If an object has only weak references. For example, see WeakHashMap. The discussion between WeakReferences, SoftReferences, PhantomReferences, and StrongReferences is a bit more involved and out of the scope for this class, so we omit the details.

# Other remarks
* Cpp can allocate arbitrary sized blocks of memory, whereas Java can only allocate memory through object instantiation. Otherwise, in Java, arbitrary blocks may be allocated in Java as an array of bytes.
* Cpp has destructors, while Java has finalizers. Both are invoked before an object's deallocation but they differ significantly.
  * An object's destructor in Cpp must be invoked implicitly for variables declared on the task and explicitly for deallocating an object.
* In Java, the object's finalizer is invoked asynchronously after some time has passed since the object has been accessed for the last time and before it is deallocated.
  * Note that only a few objects need finalizers. In particularly, one is needed only for objects that *must* guarantee some cleanup of object state before deallocating.


