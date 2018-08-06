## Run the dev app

```bash
sbt "run src/main/resources/sample-updates.txt 2 10.0"
```

## Run the compiled app

```bash
java -jar pre-assembled-prog.jar src/main/resources/sample-updates.txt 2 10.0
```

## Compile the app and run it

```bash
sbt assembly
java -jar target/scala-2.12/prog.jar src/main/resources/sample-updates.txt 2 10.0
```

## Architecture Decision Record
### Assumptions
- Although this is specified in the spec for Update events, Delete event will also not be given for a price level index which hasn't already been provided using a New event.
- Missing price level indexes, if they exist, will still be outputted as 0 bids, 0 asks.
- Delete instructions still contain ticks and quantity. And they are formatted the same way `U` and `N` instructions are. (eg `D A 1 0 0`)

### Data Structures used
We decided to use Array to store the market events, but List was considered too. Writing to an Array of which a
size is known ahead of time is O(1), and reading from an Array is also O(1). Writing to a List is O(1) if we keep
track of the last element of the list, and reading from a list sequentially is also O(1) for each element read.
There is however a slight optimisation advantage when using an Array because it is sequentially in memory, meaning
that the CPU can prefetch and cache the array. Where as a linked list will be stored all over memory and cannot be
pre-fetched.

We decided to use a HashMap to store the order book as it was being sourced from the market event. The 
HashMap uses the price level indexes as its keys. It is an efficient data structure because inserts are O(1),
and reads are also O(1). Reads were important because the order book needs to be read in order to be printed on
the screen. 

Overall the execution of the program is O(n), where n is the number of lines in the `updates.txt` file
