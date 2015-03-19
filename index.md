---
layout: page
title: The Ptolemy Machine
---


![Ptolemy's second projection][p2]

[p2]: http://neelsmith.github.io/ptolemy/imgs/projection2.png

## What is the Ptolemy Machine? ##

TBA

## Current state ##

The library specified here currently extracts from an XML edition of the *Geography* Ptolemy's two main data structures:  the lists that organize the contents into groups by five classifying properties, and the individual sites that are named and located in longitude and latitude.

It verifies that all geographic coordinates are formatted correctly as longitude-latitude pairs, that each element of the pair consists of an integer value for degrees, and a fractional value for fraction of a degree up to a maxiumum precision of 1/12, that the strings for each coordinate value are in syntactically valid Milesian notation, and that latitude may optionally flag values as negative (= "south of the equator",  not applicable to longitude in Ptolemy's system which places the origin of longitude in the Atlantic and runs only in the positive direction.)


## Material on this web site ##


Specifications for The Machine are being written using [concordion](http://concordion.org) to specify tests.  The output of testing these specifications is included [here](specs/ptolemy/Ptolemy.html).

API documentation is available [here](api).
