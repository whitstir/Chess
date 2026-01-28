# Here are my notes

## Java Fundamentals

### Wrapper Types

Integer Wrapper Class --> int Integer.parseInt(String value)

### Objects

All objects live in the heap.  
The only way to create an object is to call "new".  
"New" returns a reference (or pointer) to the object.  
There is no way to create objects in the runtime stack.  
The stack contains object references, but not actual objects (they are in the heap).  

### Working with Strings

String Declaration --> use double quotes to assign  
String Concatenation --> Strings are immutable so concatenation always creates a new String  
String Formatting --> String.format(stuff in here)  

String concatenation is inefficient because it creates a new String for every concatenation operation. If you're doing a lot of String concatenation, just use StringBuilder to avoid creating temporary Strings!  

### Important String Methods

- int length()
- char charAt()
- String trim()
- boolean startsWith(String)
- int indexOf(int)
- int indexOf(String)
- String substring(int)
- String substring(int, int)

You can also put in special characters!!! 

### Phase 0

Have ChessPiece class by itself, include color and type. Have completely separate classes for King, Queen, etc. Then call pieceMoves method on each different class?

### Arrays

### Classes and Objects

- static things are not attached to an object
- classes always extend from the Object class (a Java default thing)

### Copying Objects
#### Shallow Copy
Takes the original object and makes a new object of the same class. It's almost never what you want. 
#### Deep Copy
Copies the object and all objects it references, recursively
- Immutable objects do not need to be copied and can be safely shared (strings, integers, booleans, doubles, etc)

### Handle/Declare Rule
- Applies to Checked Exceptions (Exceptions that aren't RuntimeExceptions)
- Does not apply to Errors
- Does not apply to RuntimeExceptions
- Call printStackTrace();

### Collections
- When regular arrays do not suffice
- Can only store objects, not primitives

#### List
- A sequence of elements accessed by the index
- ArrayList or LinkedList
- Supports a ListIterator
#### Sets
- Unordered collections of unique values
- Contains no duplicates
- HashSet, TreeSet, LinkedHashSet
#### Queue
- A collection designed for holding elements prior to processing
#### Stack
- Just used a double ended queue, stacks are not it. 
#### Maps
- Key value pairs
- A collection that maps keys to values where the keys are unique
#### Equality Checking
