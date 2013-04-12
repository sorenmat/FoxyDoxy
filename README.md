Foxy Doxy
=========

This is a library for generating documentation from the code base.


## How it works
The specified source directory is scanned for comments including a @documentation annotation. If one is found the comment is
included in the documentation.
In order to structure the documentation, there is an option to specify a @section tag. If multiple @documentation annotations include
the same section tag, the are sorted after the @priority tag.

# Tags
@documentaion The main tag, should be included in comment in order for Foxy Doxy to process it.

@section What section does this piece of documentation belongs to.

@tags A comma separated list containg some keywords that describes the section.
