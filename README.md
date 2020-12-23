# US Zip Code Range Consolidator
This tool takes inclusive ranges of US 5-digit zip codes as input and presents them as the smallest set of ranges representing the same number space.

## Build
This is a Maven project building an executable jar with dependencies.

```
mvn clean package
```

## Usage
Arguments to the program are zip code ranges in standard interval notation. Each argument must be a whitespace delimited list of inclusive intervals of 5-digit codes and/or a file containing such intervals.

E.g. zip code range for Albany, NY:

```
[12201,12288]
```

Run in console (see [examples](#examples) below):

```
$ java -jar ziprange.jar [args...]
```

## Examples
With range argument:

```
$ java -jar ziprange-0.1-jar-with-dependencies.jar "[12345,23556] [23456,45677]"
[12345,45677]
```

With file argument:

```
$ cat input.txt 
[92345,93556] [93456,99677]
[00000,00005]
[00000,00010]
$ java -jar ziprange-0.1-jar-with-dependencies.jar input.txt 
[00000,00010] [92345,99677]
```

With both range and file argument:

```
$ cat input.txt 
[92345,93556] [93456,99677]
[00000,00005]
[00000,00010]
$ java -jar ziprange-0.1-jar-with-dependencies.jar "[12345,23556] [23456,45677]" input.txt
[00000,00010] [12345,45677] [92345,99677]
```