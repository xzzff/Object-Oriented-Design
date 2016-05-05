# Cpp
Functions *can* be created outside of classes. Member functions are defined within the class definition itself or separately using the scope resolution operator `::`.

For example, to define the `volume()` method inline:
```cpp
class Box
{
    public:
        double volume() const
        {
            return length * width * height;
        }
    private:
        double length;
        double width;
        double height;
};
```
Alternatively, we could define the same function outside of the class using `::` as follows:
```cpp
    const double Box::volume()
    {
        return length * width * length;
    }
```
The only important point here is how we used the class name before the `::` operator.

# Java
Note that while Java does support Procedural Programming, functions *cannot* be created outside of classes. That is, all functions must be methods of a class. Recall that a Java file represents a class. If we had a procedure outside of the class, what would its scope be? Would it be global, or perhaps belong to the class that Java file represents?