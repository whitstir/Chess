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

## Chess Server Design (Phase 2)
We're going to have a server application with a WebAPI, read Phase 3 specs for more details. 
- Single Resposibility Principle
- Avoid Code Duplication
- Encapsultion/Information Hiding (make class members private when possible)

Look at Phase 3 Specs for helpful tips and stuff. 

Data Access Object --> Model

### Data Access Object
- Think of CRUD

## HTTP Overview
Distributed System: The system that is running on multiple machines

Client connects to Server
- Client establishes a network connection with the server
- A connection allows the client to send bytes to the server and vice versa
- IP addresses are hard to work with and remember so we specify the IP address with a domain name
- The client uses the domain name service (DNS) to convert the server's domain name to an IP address
- The server will probably be running multiple programs (which are probably using the internet), meaning that the server's IP address is not sufficient for the client to connect to the server program
- Each server program communicates on a particular port number (an unsigned integer in the range 1-65535)
- The client needs to know both the server program's IP address and port number in order to connect to it

URL: Uniform Resource Locator

Most of the chess project is going to be done with POST requests

#### HTTPS Methods
GET, POST, PUT, DELETE, etc

#### cURL
Client-URL
- Easy experimentation with HTTP endpoints
- Available everywhere
- Great for sharing with others, debugging endpoints, etc

Example request --> curl byu.edu

Look at slides for parameters to use with curl

## Javalin Overview
- An open source framework for building small Java applications and web APIs
- Create handler methods for handling HTTP requests and returning HTTP responses
- Serve website files to web browers

### Handlers
### Endpoint
Basically just a route with a url bound to it. 

Before and After Handlers
- Provides a way to execute common code for multiple routes without code duplication
- Throwing a response exception prevents further processing by other handlers
- Responses can be thrown from before or after endpoint handlers
- Before and after routes take an option pattern to restrict the routes to which they are applied
- beforeMatched(...) only applies to routes that have a matching endpoint handler
- You can  have multiple before/after filters

### Useful Request and Response Methods
See slides 

## Error Handling
- Handling exceptions --> call .exception
- Handling status codes --> call .error



