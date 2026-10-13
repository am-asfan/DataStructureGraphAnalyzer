# University Student Management System

A console-based Java **University Student Management System** built to demonstrate core data structures
and algorithms. Each feature a user sees (Student Records, Recent Activities, Registration Requests,
Student Directory, Find a Student and the University Connection Network) is powered by one of the
assignment's data structures: an array, a stack, a circular queue, a singly linked list, linear and binary
search, and an undirected graph with Breadth-First Search (BFS) and Depth-First Search (DFS). Everything
is reached from one integrated menu, and a built-in performance report measures real operation counts and
execution times.

---

## Table of Contents

1. [Project Title](#university-student-management-system)
2. [Project Description](#project-description)
3. [Assignment Objectives](#assignment-objectives)
4. [Main Features](#main-features)
5. [Data Structures Implemented](#data-structures-implemented)
6. [Searching Algorithms](#searching-algorithms)
7. [Graph Representation and Traversal](#graph-representation-and-traversal)
8. [Performance and Complexity Analysis](#performance-and-complexity-analysis)
9. [Technologies Used](#technologies-used)
10. [Project Structure](#project-structure)
11. [Prerequisites](#prerequisites)
12. [Installation and Setup](#installation-and-setup)
13. [How to Compile](#how-to-compile)
14. [How to Run](#how-to-run)
15. [Usage Instructions](#usage-instructions)
16. [Example Output](#example-output)
17. [Testing](#testing)
18. [Time Complexity Reference](#time-complexity-reference)
19. [Group Member Details](#group-member-details)
20. [Individual Responsibilities](#individual-responsibilities)
21. [Individual Contributions](#individual-contributions)
22. [GitHub Collaboration](#github-collaboration)
23. [Limitations and Future Improvements](#limitations-and-future-improvements)

---

## Project Description

The **University Student Management System** is a university Data Structures and Algorithms assignment. Users manage student records, log recent activities,
process registration requests, maintain a student directory, find students, and explore a network of
connections between students, departments and clubs. They can also run a performance report whose values
are measured while the program runs. None of the results are hard-coded.

The menus use real-world university terminology. The technical concept stays visible: every submenu shows
a **Technical view** line naming the data structure or algorithm behind it, and the performance report
uses the technical names. The underlying implementation is exactly the same as it would be with generic
names such as "Array Operations".

The data structures are implemented by hand (no `java.util.Stack`, `Queue` or `LinkedList`) so that the
underlying logic (index shifting, the stack pointer, circular indexing and node links) is visible and
can be explained.

## Assignment Objectives

- Demonstrate practical understanding of arrays, stacks, queues and linked lists.
- Implement and compare linear search and binary search.
- Represent a graph with an adjacency list and implement BFS and DFS.
- Analyse algorithmic complexity both theoretically (Big-O) and empirically (operation counts and time).
- Apply object-oriented design: encapsulation, constructors, abstraction and separation of responsibilities.
- Validate all user input and handle errors without crashing.
- Collaborate as a team using Git and GitHub.

## Main Features

| Main-menu option | What it does | Technical concept |
| --- | --- | --- |
| 1. Student Records | Add a student record, add at a position, remove by position or student ID, look up, view all, view capacity | Fixed-capacity **array** |
| 2. Recent Activities | Log an activity, undo the latest activity, view the latest, view the history, empty and full checks | Array-based **stack** (LIFO) |
| 3. Registration Requests | Submit a request, process the next request, view the next request, view waiting requests (with the circular slots), empty and full checks | Circular-array **queue** (FIFO) |
| 4. Student Directory | Add a student to the start, end or a position; remove by ID or position; find; view; count | Singly **linked list** |
| 5. Find a Student | Search the records in stored order, search sorted student IDs, compare both methods | **Linear search** and **binary search** (on a sorted copy) |
| 6. University Connection Network | Add a member, connect two members, view, find a member, explore level by level or path by path, network summary, view all connected groups, load a sample network | Undirected **graph** (adjacency list), **BFS** and **DFS** |
| 7. System Performance Report | Search speed test, best and worst cases, network exploration tests, record operation costs, full report | **Performance analysis** |
| 8. System Overview | Current state of every feature plus the latest search, exploration and performance results | Shared application state |
| 9. Exit | Closes the application cleanly | |

### Terminology used in the interface

| Interface term | Technical meaning |
| --- | --- |
| Student ID | The integer value stored in a structure |
| Position | Array index, or linked-list node index |
| Slot | An index in the circular queue's array |
| Log / undo an activity | Stack push / pop |
| Submit / process a request | Queue enqueue / dequeue |
| Member / connection | Graph vertex / edge |
| Explore level by level / path by path | BFS / DFS |
| Connected group | Connected component |
| Connections checked | Adjacency-list entries examined |

Additional behaviour:

- Every input is validated: letters, empty input, negative positions and out-of-range choices are rejected
  with a friendly message, and the user is asked again.
- Results that were computed before the data changed are labelled **PREVIOUS RESULT** in the summary.
- If the input stream ends (for example Ctrl+Z / Ctrl+D), the program exits cleanly instead of looping.

## Data Structures Implemented

| Class | Structure | Used by feature | Key design points |
| --- | --- | --- | --- |
| `array.ArrayManager` | Fixed-capacity `int[]` | Student Records, Find a Student | Capacity fixed at construction (10 in the app). A full array rejects inserts. Insert and delete shift elements manually and count the shifts. |
| `stack.CustomStack<T>` | Array-based stack | Recent Activities | `top` index starts at -1. Overflow and underflow raise clear errors. The class is generic. |
| `queue.CustomQueue<T>` | Circular-array queue | Registration Requests; BFS in the network | `front`, `rear` and `size` are maintained with `(index + 1) % capacity`, so freed slots are reused. The class is generic and is reused by BFS. |
| `linkedlist.CustomLinkedList` | Singly linked list | Student Directory | Private nested `Node` class (value + next) and a `head` reference. There is no tail reference, so insert-at-end is O(n). |

Capacities used by the application (set in `model/ApplicationState.java`): Student Records (array) 10,
Recent Activities (stack) 5, Registration Requests (queue) 5.
They are small on purpose so that the full and overflow cases are quick to demonstrate.

## Searching Algorithms

Both algorithms are in `searching.SearchAlgorithms` and return a `SearchResult` with the index (or -1),
the number of comparisons and the elapsed time.

- **Linear search** examines elements from left to right. Best O(1), average O(n), worst O(n).
  It works on unsorted data.
- **Binary search** (iterative) examines the middle element and discards half of the range on each
  step. Best O(1), average and worst O(log n). It **requires sorted data**. If unsorted data is passed in,
  the method throws an error rather than returning a wrong answer.
- **Comparison definition:** one comparison is one dataset element examined against the target.
- **The original array is never reordered.** *Find a Student* runs binary search on a sorted *copy*
  (`createSortedCopy`) and shows both arrays. This is why the two algorithms can report different
  indexes: each index refers to its own dataset.

## Graph Representation and Traversal

`graph.Graph` powers the **University Connection Network**. In the interface, a vertex is a *member*
(a student, department or club), an edge is a *connection*, BFS is *Explore Network Level by Level*,
DFS is *Explore Network Path by Path*, and a connected component is a *connected group*.

It is an **undirected, unweighted graph** stored as an adjacency list
(`Map<String, List<String>>`, using a `LinkedHashMap` so the order is predictable).

Design decisions:

| Situation | Behaviour |
| --- | --- |
| Add an existing vertex | Rejected with a message (`addVertex` returns `false`) |
| Add an edge with a missing vertex | The missing vertex is **created automatically** and the user is told |
| Duplicate edge (A-B again, or B-A) | Ignored with a message (`addEdge` returns `false`) |
| Self-loop (A-A) | Rejected |
| Undirected storage | Each edge is added to **both** vertices' lists |
| Vertex names | 1-20 letters, digits, `_` or `-`; case sensitive |
| Invalid or missing start vertex | Clear error; no traversal is run |

**BFS** uses this project's own `CustomQueue<String>`. A vertex is marked visited when it is *enqueued*,
so it can never enter the queue twice.

**DFS** is recursive, so the method call stack acts as the stack.

Both traversals use a visited set, so cycles never cause a vertex to be visited twice. Both run in
**O(V + E)** time.

**Disconnected graphs:** BFS or DFS from a start vertex visits **only that vertex's connected component**.
The application says so when members were not reached. *View All Connected Groups* restarts the
traversal from every unvisited vertex so that the whole graph is covered.

**Sample network** (University Connection Network, option 9 *Load Sample Network*). It contains a cycle
and two components:

```text
A --- B          G --- H
|     |
C --- D --- E --- F
```

From A, BFS gives `A -> B -> C -> D -> E -> F` and DFS gives `A -> B -> D -> C -> E -> F`.

## Performance and Complexity Analysis

`performance.PerformanceAnalyzer` **runs the real code**:

| Comparison | Data used | Metrics recorded |
| --- | --- | --- |
| Linear vs Binary Search | The same generated sorted dataset `1..n` and the same target for both algorithms | Comparisons, average time, result |
| Search scenarios | First, middle, last and missing targets | Shows the best and worst cases |
| BFS vs DFS | The same graph and the same start vertex (the current graph or a seeded generated graph) | Vertices visited, adjacency checks, average time, traversal order |
| Data structure operations | Fresh structures of size n (the user's data is not touched) | Element shifts or node traversals, total time |

Metric definitions:

- **Comparisons:** dataset elements examined against the target.
- **Vertices visited:** vertices added to the traversal order.
- **Adjacency checks:** adjacency-list entries examined. Each undirected edge is stored twice, so a full
  traversal of a component examines 2 x (edges in that component) entries.
- **Element shifts:** array elements moved one place to open or close a gap.
- **Node traversals:** `current = current.next` steps in the linked list.

Timing method: `System.nanoTime()` is placed around the algorithm's main loop. Searches and traversals
first run 20 warm-up iterations, and then the average of 200 (search) or 50 (graph) timed runs is
reported. Data structure tests report the total time for a batch of n operations.

> **Important:** execution times depend on hardware, JVM warm-up (JIT compilation), garbage collection
> and other running programs. They vary between runs, and one measurement does not prove that one
> algorithm is always faster. Operation counts are deterministic and are the more reliable comparison.
> The app shows theoretical complexity separately from the measured values.

## Technologies Used

- **Java 17** language level (tested with JDK 24, compiled using `--release 17`)
- Standard JDK only: **no external libraries**, frameworks, databases or GUI
- **Git** and **GitHub** for version control
- Optional **Maven** build (`pom.xml`)

## Project Structure

```text
DataStructureGraphAnalyzer/
├── src/
│   ├── main/java/com/dsa/
│   │   ├── Main.java                      # Entry point
│   │   ├── menu/
│   │   │   ├── ConsoleMenu.java           # Integrated main menu
│   │   │   ├── BaseMenu.java              # Shared submenu loop + error handling (template method)
│   │   │   ├── ArrayMenu.java             # 1. Student Records
│   │   │   ├── StackMenu.java             # 2. Recent Activities
│   │   │   ├── QueueMenu.java             # 3. Registration Requests
│   │   │   ├── LinkedListMenu.java        # 4. Student Directory
│   │   │   ├── SearchMenu.java            # 5. Find a Student
│   │   │   ├── GraphMenu.java             # 6. University Connection Network
│   │   │   ├── PerformanceMenu.java       # 7. System Performance Report
│   │   │   └── ResultsSummary.java        # 8. System Overview
│   │   ├── array/ArrayManager.java
│   │   ├── stack/CustomStack.java
│   │   ├── queue/CustomQueue.java
│   │   ├── linkedlist/CustomLinkedList.java
│   │   ├── searching/SearchAlgorithms.java
│   │   ├── graph/Graph.java
│   │   ├── performance/PerformanceAnalyzer.java
│   │   ├── model/
│   │   │   ├── ApplicationState.java      # Shared structures + latest results
│   │   │   ├── SearchResult.java
│   │   │   ├── TraversalResult.java
│   │   │   ├── OperationResult.java       # One measured performance row
│   │   │   └── PerformanceReport.java
│   │   └── util/
│   │       ├── InputValidator.java        # Reusable validated input
│   │       ├── InputClosedException.java
│   │       ├── ConsoleFormatter.java      # Consistent headings and messages
│   │       └── SampleData.java            # Sample graph, benchmark graph, sorted datasets
│   └── test/java/com/dsa/tests/           # Automated tests (plain Java, no JUnit needed)
│       ├── AllTests.java                  # Test runner
│       ├── TestHelper.java                # Tiny assertion helper
│       ├── ArrayManagerTest.java ... IntegrationTest.java
├── docs/
│   ├── TEST_PLAN.md                       # Manual test plan
│   ├── GITHUB_COLLABORATION.md            # Branch / PR workflow and commands
│   ├── DEMO_VIDEO_PLAN.md                 # < 15 minute video plan
│   └── SUBMISSION_CHECKLIST.md
├── README.md
├── .gitignore
└── pom.xml
```

### Class responsibilities

| Class | Responsibility |
| --- | --- |
| `Main` | Creates the input helper and shared state, then starts the main menu |
| `ConsoleMenu` | Shows the main menu and dispatches to the submenus until Exit |
| `BaseMenu` | Abstract base: prints the options, reads a validated choice, catches data structure errors |
| `*Menu` classes | Read the user's values and call the matching data structure or algorithm |
| `ResultsSummary` | Prints the current state and the latest results, labelling outdated ones |
| `ArrayManager`, `CustomStack`, `CustomQueue`, `CustomLinkedList` | The manual data structures |
| `SearchAlgorithms` | Linear and binary search with comparison counting and timing |
| `Graph` | Adjacency list, vertex and edge operations, BFS, DFS, component traversal |
| `PerformanceAnalyzer` | Runs the measured comparisons and prints the result tables |
| `ApplicationState` | Holds the one shared instance of every structure and the latest results |
| `InputValidator` | All console input: integers, ranges, non-negative values, vertex names, yes/no |

## Prerequisites

- **JDK 17 or later** (`java -version` and `javac -version` should both work)
- Git (to clone the repository)
- Optional: Maven 3.6+ if you prefer Maven

## Installation and Setup

```bash
git clone <your-repository-url>
cd DataStructureGraphAnalyzer
```

## How to Compile

Run these from the `DataStructureGraphAnalyzer` folder.

**Windows (PowerShell):**

```powershell
javac -d out\main (Get-ChildItem -Recurse src\main\java -Filter *.java).FullName
```

**Windows (Command Prompt):**

```bat
dir /s /b src\main\java\*.java > sources.txt
javac -d out\main @sources.txt
```

**macOS / Linux / Git Bash:**

```bash
javac -d out/main $(find src/main/java -name "*.java")
```

## How to Run

```bash
java -cp out/main com.dsa.Main
```

(On Windows, `out\main` and `out/main` both work.)

**With Maven (optional):**

```bash
mvn compile
mvn exec:java
```

## Usage Instructions

1. Start the program. The main menu appears. Type a number from 1 to 9 and press Enter.
2. Each submenu lists its operations. The last option always returns to the main menu.
3. If you type something invalid (letters, nothing at all, a negative position or a number outside the
   menu range), you will see an `[ERROR]` message and be asked again.
4. Student IDs are whole numbers. Network member names use 1-20 letters, digits, `_` or `-` with no
   spaces (for example `Student01` or `IT_Dept`).

| Menu | How to use it |
| --- | --- |
| Student Records | *Add Student Record* appends a student ID. *Add Student Record at Position* accepts 0 to the current number of records. *Remove Student Record* asks whether to remove by position or by student ID. *Look Up* shows how many records were checked. |
| Recent Activities | *Log New Activity* until the log is full (5) to see a stack overflow. *Undo Latest Activity* or *View Latest Activity* on an empty log shows the underflow message. The history shows the latest activity first. |
| Registration Requests | Submit 5 requests, process 2, then submit 2 more. *View Waiting Requests* shows the queue's slots and the `rear index` wrapping round to slots 0 and 1 (circular reuse). |
| Student Directory | Add students to the start, the end or a position (0 to size). Remove by ID or by position. The directory is shown as `HEAD -> ... -> null` after each change. |
| Find a Student | Searches the Student Records, so add students there first. *Search Sorted Student IDs* shows the stored order and the sorted copy it searches. *Compare Both Search Methods* prints both results side by side with an explanation. |
| University Connection Network | Choose *Load Sample Network* (option 9) for a ready-made demo, or add members and connections yourself. The two *Explore* options ask for a starting member. *View All Connected Groups* covers members that are not linked to the rest. |
| System Performance Report | Option 6 runs everything with documented defaults (1000 student IDs, target 750). The other options let you choose sizes, targets and starting members. |
| System Overview | Shows every feature's current data and the latest results. Outdated results are labelled PREVIOUS RESULT. |

## Example Output

The following is real output from one run. **Your times will differ**, but the counts and orders will not.

```text
==================================================
       UNIVERSITY STUDENT MANAGEMENT SYSTEM
==================================================

1. Student Records
2. Recent Activities
3. Registration Requests
4. Student Directory
5. Find a Student
6. University Connection Network
7. System Performance Report
8. System Overview
9. Exit

==================================================
Enter your choice:
```

A submenu, showing its *Technical view* line:

```text
------------ REGISTRATION REQUESTS -------------
Technical view: circular-array queue (FIFO - first come, first served)

1. Submit Registration Request
2. Process Next Request
3. View Next Request
4. View Waiting Requests
5. Check if Any Requests Are Waiting
6. Check if Request Queue is Full
7. Return to Main Menu
```

Waiting requests after submitting 1, 2 and 3, processing 2 requests, then submitting 4, 5, 6 and 7
(6 and 7 reuse slots 0 and 1):

```text
Waiting registration requests (student IDs, next to be processed on the left):
Front -> 3, 4, 5, 6, 7 <- Rear
Array slots: [0]=6  [1]=7  [2]=3  [3]=4  [4]=5
front index = 2, rear index = 1, size = 5 / capacity = 5
```

Exploring the sample network level by level (BFS):

```text
[SUCCESS] Explored the network level by level (Breadth-First Search, using a queue) from A
Visit order         : A -> B -> C -> D -> E -> F
Members reached     : 6 of 8
Connections checked : 12
Time taken          : 77,000 ns
Complexity          : O(V + E)
[INFO] Some members are in a separate group that cannot be reached from A. Use 'View All Connected Groups' to see every member.
```

System Performance Report (extract):

```text
STUDENT SEARCH COMPARISON - Linear vs Binary Search (sorted student IDs 1..1000, target 750)
--------------------------------------------------------------------------------------------
Subject         Operation                         n        Count  Count metric                  Time  Result
Linear Search   search for 750                 1000          750  comparisons               5,542 ns  found at 749
Binary Search   search for 750                 1000            2  comparisons                 143 ns  found at 749
Time column: average per search over 200 runs (after warm-up)

RECORD OPERATION COSTS (n = 1000, fresh structures; your data is not changed)
-----------------------------------------------------------------------------
Subject         Operation                         n        Count  Count metric                  Time  Result
Array           insert at end x 1000           1000            0  element shifts          592,400 ns  size 1000
Array           insert at index 0 x 1000       1000      499,500  element shifts        2,432,200 ns  size 1000
Linked List     insert at beginning x 1000     1000            0  node traversals       2,438,700 ns  size 1000
Linked List     insert at end x 1000           1000      498,501  node traversals       2,801,600 ns  size 1000
Feature mapping: Student Records = Array, Recent Activities = Stack,
Registration Requests = Circular Queue, Student Directory = Linked List.
```

In the linked-list rows above, insert-at-beginning (0 traversals) took almost as long as insert-at-end
(498,501 traversals).
This shows why a single timing proves little: the first batch probably also absorbed JVM warm-up and
the cost of allocating new nodes. The operation counts show the real difference between the two operations.

## Testing

### Automated tests

The tests are plain Java (no JUnit download required). They live in `src/test/java/com/dsa/tests`, and
`AllTests` runs every one of them:

| Test class | What it covers |
| --- | --- |
| `ArrayManagerTest` | Insert into an empty array; insert at beginning, middle and end; full array; delete existing and missing values; invalid indexes; shifting counts; `toArray` copy |
| `CustomStackTest` | LIFO order, peek, overflow, pop or peek on empty, reuse |
| `CustomQueueTest` | FIFO order, overflow, underflow, **circular wrap-around of front and rear** |
| `CustomLinkedListTest` | Empty list; insert at beginning, end and index; delete the head and the last node; missing values; invalid indexes; `countNodes` |
| `SearchAlgorithmsTest` | First, last and missing targets; empty dataset; sorted copy leaves the original unchanged; binary search rejects unsorted data; comparison counts |
| `GraphTest` | Empty graph; duplicate vertices and edges; self-loop; missing start vertex; BFS and DFS orders; cycles (no repeat visits); disconnected components; adjacency checks = 2E |
| `PerformanceAnalyzerTest` | Measured counts match theory (for example n(n-1)/2 shifts); same dataset and same graph used for both algorithms |
| `InputValidatorTest` | Letters, empty input, negative and too-large numbers, ranges, vertex names, yes/no, end of input |
| `IntegrationTest` | Drives the real `ConsoleMenu` with scripted input through every submenu, invalid input, System Overview, the outdated-result label and Exit |

Compile and run the tests:

```powershell
# Windows PowerShell
javac -d out\main (Get-ChildItem -Recurse src\main\java -Filter *.java).FullName
javac -cp out\main -d out\test (Get-ChildItem -Recurse src\test\java -Filter *.java).FullName
java -cp "out\main;out\test" com.dsa.tests.AllTests
```

```bash
# macOS / Linux / Git Bash  (use ; instead of : in the classpath on Git Bash for Windows)
javac -d out/main $(find src/main/java -name "*.java")
javac -cp out/main -d out/test $(find src/test/java -name "*.java")
java -cp out/main:out/test com.dsa.tests.AllTests
```

With Maven: `mvn test-compile exec:java -Dexec.mainClass=com.dsa.tests.AllTests -Dexec.classpathScope=test`

The runner prints `PASS` or `FAIL` for each check and a final count, and exits with code 1 if any check
fails.

### Manual test plan

See [docs/TEST_PLAN.md](docs/TEST_PLAN.md) for step-by-step manual tests, with columns where you can
record your own results.

## Time Complexity Reference

These values match **this implementation** (n = elements, V = vertices, E = edges, d = degree of a vertex).

| Component | Operation | Time complexity | Notes |
| --- | --- | --- | --- |
| Array | Access by index (`get`) | O(1) | Direct indexing |
| Array | Insert at end (`insert`) | O(1) | No shifting |
| Array | Insert at index (`insertAt`) | O(n) | Shifts later elements right |
| Array | Delete at index (`deleteAt`) | O(n) | O(1) for the last element |
| Array | Delete by value (`deleteValue`) | O(n) | Search + shift |
| Array | Search | O(n) | Linear |
| Stack | Push / Pop / Peek | O(1) | Uses the `top` index |
| Stack | Display | O(n) | |
| Circular Queue | Enqueue / Dequeue / Peek | O(1) | Modulo arithmetic, no shifting |
| Circular Queue | Display | O(n) | |
| Singly Linked List | Insert at beginning | O(1) | Only `head` changes |
| Singly Linked List | Insert at end | O(n) | No tail reference is maintained |
| Singly Linked List | Insert at index | O(n) | Walk to the node before the index |
| Singly Linked List | Delete by value | O(n) | |
| Singly Linked List | Delete at index | O(n) | O(1) for index 0 |
| Singly Linked List | Search / Count nodes | O(n) | `size()` is O(1) through a counter |
| Linear Search | Search | O(1) best, O(n) average and worst | |
| Binary Search | Search | O(1) best, O(log n) average and worst | Needs sorted data. Making the sorted copy costs O(n log n) |
| Graph | Add vertex / Search vertex | O(1) average | Hash map lookup |
| Graph | Add edge | O(d) | Duplicate-edge check scans the neighbour list |
| Graph | BFS | O(V + E) | Uses `CustomQueue` |
| Graph | DFS | O(V + E) | Recursive |
| Graph | Traverse all components | O(V + E) | |

## Group Member Details

> Replace every placeholder with genuine information. Add or remove rows to match your team size.

| No. | Full Name | Student ID | Assigned Responsibility | Individual Contribution |
| --- | --- | --- | --- | --- |
| 1 | [Full Name] | [Student ID] | [Responsibility] | [Actual Contribution] |
| 2 | [Full Name] | [Student ID] | [Responsibility] | [Actual Contribution] |
| 3 | [Full Name] | [Student ID] | [Responsibility] | [Actual Contribution] |
| 4 | [Full Name] | [Student ID] | [Responsibility] | [Actual Contribution] |
| 5 | [Full Name] | [Student ID] | [Responsibility] | [Actual Contribution] |

## Individual Responsibilities

Suggested split. These are suggestions only: adapt them to your actual members, and combine
responsibilities if the group is smaller.

| Suggested responsibility | Files |
| --- | --- |
| Array and Searching | `array/ArrayManager.java`, `searching/SearchAlgorithms.java`, `menu/ArrayMenu.java`, `menu/SearchMenu.java`, `ArrayManagerTest`, `SearchAlgorithmsTest` |
| Stack and Queue | `stack/CustomStack.java`, `queue/CustomQueue.java`, `menu/StackMenu.java`, `menu/QueueMenu.java`, `CustomStackTest`, `CustomQueueTest` |
| Linked List | `linkedlist/CustomLinkedList.java`, `menu/LinkedListMenu.java`, `CustomLinkedListTest` |
| Graph and BFS/DFS | `graph/Graph.java`, `menu/GraphMenu.java`, `util/SampleData.java`, `model/TraversalResult.java`, `GraphTest` |
| Integration, Performance and Testing | `Main.java`, `menu/ConsoleMenu.java`, `menu/BaseMenu.java`, `menu/PerformanceMenu.java`, `menu/ResultsSummary.java`, `performance/`, `model/`, `util/InputValidator.java`, `IntegrationTest`, `AllTests` |

## Individual Contributions

Fill in this block for **every** member. Describe only work that the member actually did.

```text
Student Name:
Student ID:
Assigned Responsibility:
Individual Contribution:
```

```text
Student Name:
Student ID:
Assigned Responsibility:
Individual Contribution:
```

```text
Student Name:
Student ID:
Assigned Responsibility:
Individual Contribution:
```

```text
Student Name:
Student ID:
Assigned Responsibility:
Individual Contribution:
```

```text
Student Name:
Student ID:
Assigned Responsibility:
Individual Contribution:
```

## GitHub Collaboration

- Repository URL: `[add your GitHub repository URL]`
- The `main` branch must always compile and pass `AllTests`.
- Each component is developed on its own feature branch and merged through a pull request reviewed by
  another member.

Suggested branches (assign them to your actual members):

```text
main
feature/array-operations
feature/stack-queue
feature/linked-list
feature/graph-traversal
feature/searching-algorithms
feature/performance-analysis
feature/integration-testing
```

Full step-by-step commands are in [docs/GITHUB_COLLABORATION.md](docs/GITHUB_COLLABORATION.md).

> Record only branches, commits and pull requests that actually exist in your repository.

## Limitations and Future Improvements

**Current limitations**

- The array, stack and queue have fixed capacities (by design, to demonstrate the full condition).
- The data structures store integers only (the stack and queue classes are generic, but their menus use integers).
- The graph is unweighted and undirected. Vertices and edges cannot be removed.
- DFS is recursive, so very deep graphs (tens of thousands of vertices in a long chain) could exceed the
  Java call stack. The generated benchmark graph is therefore limited to 2,000 vertices.
- Timing uses `System.nanoTime()` with simple warm-up and averaging. It is not a rigorous benchmark
  harness like JMH, so times are indicative only.
- The data is not saved between runs.
- The tests use a small custom runner instead of JUnit, to avoid external dependencies.

**Possible improvements**

- A dynamic array that doubles its capacity, with amortised O(1) analysis.
- A tail reference in the linked list (O(1) insert at end), or a doubly linked list.
- Weighted and directed graphs, removing vertices and edges, shortest path (Dijkstra) and cycle detection.
- An iterative DFS using `CustomStack`, to avoid recursion-depth limits.
- Sorting algorithms (bubble, merge, quick) with operation counting.
- Saving and loading data from files, and migrating the tests to JUnit 5.
