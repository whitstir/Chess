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

### Java I/O Streams (overview)
- Writes/reads data from files or other data sources
- Two Choices: Binary-Formatted or Text-Formatted Data
- InputStream and Output Stream
- Reader and Writer

#### Reading/Writing Bytes
The InputStream interface is used to read bytes sequentially from a data source.
There are mnay features you may want to enable when consuming data from an InputStream.
- Decompress data as it comes out of the stream
- Decrypt data as it comes out of the stream
- Compute a "digest" of the stream (a fixed length value that summarizes that data in the stream)
- Byte counting
- Line counting
- Buffering

The OutputStream interface is used to write bytes sequentially to a data destination
- Compress data as it goes into the stream
- Encrypt data as it goes into the stream
- Compute a "digest" of the stream (a fixed length value that summarizes that data in the stream)
- Byte counting

#### Reading/Writing Binary-Formatted Data
- Reading/writing bytes is useful, but usually we want to read/write larger data values like float, int, boolean, etc
- The DataOutputStream class lets you write binary-formatted data values
- he DataOutputStream constructor wraps a DataOutputStream around any OutputStream

#### Readers and Writers 
- Reader Interface: used to read characters sequetially from a data source
- Writer Interface: used to write characters sequentially from a data source

#### Reading/Writing Text-Formatted Data
- The PrintWriter class lets you write text-formatted data values (String, int, float, boolean, etc)
- The Scanner class lets you read text-formatted data values









