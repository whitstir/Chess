# Here are my notes

## Java Fundamentals

### Wrapper Types

Integer Wrapper Class --> int Integer.parseInt(String value)

### Objects

All objects live in the heap
The only way to create an object is to call "new"
"New" returns a reference (or pointer) to the object
There is no way to create objects in the runtime stack
The stack contains object references, but not actual objects (they are in the heap)

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