# Specifications for the Ptolemy Machine, version @version@ #

For an introduction to the Ptolemy Machine — what it is, and why — see the [project web site](http://neelsmith.github.io/ptolemy/).  This document specifies the computational system introduced there.  In this version, the focus of work is Ptolemy's *Geography*.

## Data structures ##

The Ptolemy Machine works directly from a digital edition of Ptolemy's *Geography*.   Its archival form is a TEI-compliant XML document, following markup conventions specified [here](markup/Markup.html).

The central data structure is comprised of the 1264 lists containing site names with coordinate locations in longitude and latitude.  The text's hierarchy of book and chapter defines the situation of each list in the proper continent and politcial unit (province of the Roman Empire, or "satrapy" of a foreign state).  Within that hierarchy, each list contains data for a single combination of ethnic group, physical region, and geographic type.  

The Ptolemy Machine extracts from  the archival TEI XML edition of the text the two essential related data sets:  the 1264 lists, and the 6341 items with corrdinates contained in the lists.




## Alignment with other projects ##

The CITE Collection of sites could be aligned with the Bern database of Ptolemaic sites, and with identifiers in the Pleiades Gazetteer.





## Information about code dependencies ##

[dependencies](dependencies/Dependencies.html)
