# Specifications for the Ptolemy Machine, version @version@ #

For an introduction to the Ptolemy Machine — what it is, and why — see the [project web site](http://neelsmith.github.io/ptolemy/).  This document specifies the computational system introduced there.  In this version, the focus of work is Ptolemy's *Geography*.

## Data structures ##

The Ptolemy Machine works directly from a digital edition of Ptolemy's *Geography*.   Its archival form is a TEI-compliant XML document, following markup conventions specified [here](markup/Markup.html).

The first task to specify is extracting Ptolemy's data set from the archival TEI XML edition of the text.

From an XML source text, we need to extract `list` items that contain paired `name` and `measure` elements.

Citation hierarchy is book/chapter/section in `div/div/div`.  Books ~ continents, chapters ~ provinces, sections ~ geotype.

Rather than adding ever more markup to the XML, we will annotate these analytically.

We will also align this edition with other analyses of Ptolemy.


## Information about code dependencies ##

[dependencies](dependencies/Dependencies.html)
