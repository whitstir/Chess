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

#### Convert from Streams to Readers and Writers

### JSON

## Software Design

### Goals of Software Design
Create systems that
- Work and satisfy customer requirements
- Are easy as possible to understand, debug, and maintain
- Hold up well under changes

#### Principle 1 - Design is Inherently Iterative
- Design, implement, test, design, implement, test, etc...
- Feedback loop from implementation back into design provides valuable knowledge
- Designing everything before beginning implementation doesn't work
- Beginning implementation without doing any design also doesn't work
- The appropriate balance is acheived by interleaving design and implementation activites in short iterations

#### Principle 2 - Abstraction
- One of the software designer's primary tools for coping with COMPLEXITY
- In OOP, abstractions are represented by classes
- Programming languages provide classes that model low-level concepts such as strings and file I/O
- Programs written solely in terms of these low-level classes are very difficult to understand
- Software designers must create higher-level, domain-specific classes, and write their software in terms of those (high level classes are implemented in terms of low level classes)
- Classes often model complex, real-world objects
- You generally can't represent the full thing you are abstracting, so you need to make domain appropriate decisions about what to represent in methods and variables

#### Principle 3 - Good Naming
- Make people be impressed with the name hehe

#### Principle 4 - Single-Responsibility Principle
- Every class and method has one responsibility
- Each class should represent one, well-defined concept, and each method should perform one, well-defined task
- Methods that need to do multiple things should delegate tasks to sub-methods that each perform a single task
- Cohesive classes and methods are easy to name

#### Principle 5 - Decomposition
- A fundamental technique for managing COMPLEXITY
- Large problems subdivided into smaller sub-problems
- subdivison continues until leaf-level problems are simple enough to solve directly
- solutions to sub problems are recombined into solutions to larger problems
- Levels of decomposition: system --> subsystem --> packages --> classes --> methods

#### Principle 5 - Algorithm & Data Structure Selection
- No amount of decomposition or abstraction willhide a fundamentally flawed selection of algorithm or data structure

#### Principle 6 - Coupling
Low Coupling
- Coupling between classes should be minimized
- The less classes know about each other the better
- Minimize the number of other classes a class interacts with or knows about
- Low coupling reduces ripple effects when a class changes

Encapsultion/Information Hiding
- A class should hide its internal implementation that are not essential for its users to know about
- Encapsultation is central to achieving low coupling between classes
- Many languages provide "public", "private", and "protected" access levels
- All internal implementation should be "private" unless there's a good reason to make it "protected" or "public"
- Don't let internal details "leak out" of a class (ex. ClassRoll instead of StudentLinkedList)

Seperation of Interface and Implementation
- Maintain a strict separation between a class' interface and its implementation
- This allows internal details to change without affecting clients
- Program to interaces instead of concrete classes

#### Principle 8 - Avoid Code Duplication
- The DRY Principle: don't repeat yourself
- Code duplication should be avoided (identical or similar sections of code)
- Disadvantages of duplication: N copies to maintain, bugs are duplicated N times, makes programs longer, decreases maintainability
- Solutions: factor common code into a seperate method or class, shared code might be placed in a common superclass





