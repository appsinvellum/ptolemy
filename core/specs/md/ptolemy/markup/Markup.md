# Markup conventions for TEI edition of the *Geography* #

The central data structure in Ptolemy's *Geography* is made up of a series of lists in books 2-7.  These are identified as `@type='simple'`.  Each item in the list contains both a `name` and `measure` element of type `llpair`.  The `measure element` contains an ordered set of `num` elements alternating between `@type='cardinal'` and `@type='fraction`, to represent first longitude degrees and fraction of a degree, then latitude degrees and fraction of a degree.

