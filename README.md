Foxy Doxy
=========

This is a library for generating documentation from the code base.

How does this differ from JavaDoc ?

JavaDoc was designed to document Java APIs, and is good at that. But in order for the user to find the documentation,
you need to know what class to look for.

Sometimes you need an other kind of documentation. Let's say you would describe the flow of the application, the bootstrapping process or some other kind
that spans multiple classes and concepts.

But at the same time you need it close to the source code, in order for it to be kept up to date.

__Then Foxy Doxy is the tool for you.__

Check out the generated result [here](http://sorenmat.github.io/FoxyDoxy/doc/template.html)



## How it works
The specified source directory is scanned for comments including a @documentation annotation. If one is found the comment is
included in the documentation.
In order to structure the documentation, there is an option to specify a @section tag. If multiple @documentation annotations include
the same section tag, the are sorted by the @priority tag.

## Tags
__@documentaion__ The main tag, should be included in comment in order for Foxy Doxy to process it.

__@section__ What section does this piece of documentation belongs to.

__@tags__ A comma separated list containg some keywords that describes the section.

## How to use it.

Download the current binary from here http://dl.bintray.com/content/sorenmat/FoxyDoxy/foxydoxy-assembly-0.1.jar?direct

Default values for source directory is src, and output directory is doc.


```
java -jar foxydoxy-assembly-0.1.jar --help
```
```
Foxy Doxy version 0.1
    -s, --source DIRECTORY           Source directory to scan for documentation
    -t, --template FILE              Choose non default template
    -o, --output DIRECTORY           Directory where output files will be placed
    -h, --help                       Show this message
```

## Example usage
```java
package com.scalaprog;
/**
 * @documentation
 *
 * # This is simple piece of documentation
 *
 * This is so more cool than normal java doc.
 *
 * @section Configuration
 * @tags test, demo, simple
 */
public class JavaTestFile {

}
```

