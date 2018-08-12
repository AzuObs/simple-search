## Run the dev app

```bash
sbt "run ."
```

## Test the app

```bash
sbt test
```

## Run the compiled app

```bash
java -jar dist/SimpleSearch.jar .
```

## Assemble the app

```bash
sbt assembly
cp target/scala-2.12/SimpleSearch.jar dist/SimpleSearch.jar
```
