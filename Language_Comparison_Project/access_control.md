Here, we outline the differences in Cpp vs Java with regards to access control and access specifiers.

# Cpp

When discussing cpp, we will try to keep things away from low-level details of the language such as argument-dependent-lookup, named lookup, resolution of access, etc.

**Access specifiers**

In Cpp, there are 3 access specifiers for a class, struct, or union: namely, public, protected, and private. These define how members of said class, struct, or union can be accessed. As expected, any member of a class may be accessed inside any member function of the same class.

Without loss of generality, for the three access specifiers below, these apply to all classes, structs, or unions. We will just use the word 'class' below however.

* Public - Members declared as public are accessible from outside the class via an object of the class.
* Protected - Members declared as protected are accessible from outside the class, _but only_ in a Derived class.
* Private - Members declared as private are accessible only from within the class itself; no access from other classes is allowed.

The syntax for declaring members of a class is shown below. By default, class members are private if unspecified. Note that in a struct, every member is public.
```cpp
  class A
  {
    public:
      int x;
    protected:
      std::string s;
    private:
      std::vector<int> v;
  }
```

**Inheritance and Access Specifiers**

There are 3 types of inheritance: public, protected, and private. The most important rule as discussed above: private members of a class are never accessible from anywhere else except the members of the same class. By default, the inheritance type for a class is private. We now discuss the different types of inheritance in a bit more detail.

* Public Inheritance - All public members of the Base class simply are made public members of the Derived class. Similarly, protected members of the bass class become protected members in the Derived class.
* Protected Inheritance - All public and protected members of the Base class become protected members of the Derived class.
* Private Inheritance - All public and protected members of the Base class become private members in the Derived class.

The syntax for each of these is below:
``` cpp
  class Base : public Derived
  class Base2 : protected Derived2
  class Base3 : private Derived3
```

**Some important remarks**

* The same access rules apply to classes and members down an inheritance hierarchy.
* Access specification is per-class (similarly, per-struct, per-union), _not_ per-object. An example of this would be in a class copy constructor or copy assignment. Here, all of the members of the passed object can be accessed.
* Cpp, unlike Java, supports multiple inheritance. The same rules as described above apply.

Lastly, we discuss friend classes and friend functions. 

**Friend**

We can declare a class or function as a friend of another class. By doing so, the access specification rules described above will not apply to the this class or function that is being friended. Instead, the class or function can access _all_ of the members of that particular class.

As far as Objected Oriented principles go, friend functions and classes enhance encapsulation greatly. They are used to denote a strong coupling between two entities. An example is when one entity needs to access the other entity's private and/or protected members but we do not want everyone else to have access by just making them public instead to allow access.

# Java

**Access Modifiers**

In Java, there are four types of access modifiers for variables: public, protected, default, and private. They are succinctly described below. By default, variables are package private (default) if unspecified. Note that the access modifiers have a total order to them. That is, public > protected > default > private. This means that public provides the most access whereas private provides the least. It also tells us that any reference on a private member is also valid for a default member, and likewise for the other specifiers.

* Public - accessible from everywhere
* Protected - accessible by classes in the _same_ package and the subclasses in _any_ package
* Default (package private) - accessible by classes of the _same_ package only
* Private - accessible by the same class only

**Access Control and Inheritance**

Note that in Java, there is only a notion of _public_ inheritance, i.e. there is no protected or private inheritance like there is in Cpp.

Below the rules are succinctly described:
* Members declared public in a Base class must also be public in all Derived classes.
* Members declared protected in a Base class must either be protected or public in all Derived classes. Note that the members _cannot_ be private.
* Members declared private are not inherited whatsoever, so there is no specific rule for private members.

An example is shown below.
```java
  class Base
  {
    Member declarations here...
  }
  class Derived extends Base
  {
    ...
  }
```

