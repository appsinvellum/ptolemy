## Content to add to ghpages ##

- digital editing and the Ptolemy Machine:  rationale
- reconstruction of Ptolemy's derivation of spherical coords

## Content to add to master branch ##

- √ compute coordinate values using greeklang lib
- √ shrink coords by 250/180 ratio
- √ compute offset of latitude from Rhodes
- use a data set of equivalents to find offset of longitude from 0


## Analyses to develop as ORCA Collections ##

Contained in the text citation hierarchy:

- √ continents
- √ provinces

Cutting across the citation hierarchy:

- √ geographic type (originally, in XML on `div@n` )
- ethnic/cultural group (originally, in XML on `div@ana` )
- physical geographic region or feature (originally in XML on `list@ana`)


Set up work by generating KML for sites that do/do not have prior assignment of geographic type:

- √ ethnic/cultural group (originally, in XML on `div@ana` )
- √ physical geographic region or feature (originally in XML on `list@ana`)


Other Ptolemaic taxonomy to extract:

- Extract sections that list "demes/tribes" without coordinates, map named geographic entities
- Identify provicnes with no ethnic subdivisions (e.g., British Isles)



## Verification of the edition ##

- check spatial consistency of provinces by reviewing convex hulls


## Analysis ##

- for each of 1264 lists, assemble data for all 7 Ptolemaic properties from external tables mapping URNs together.  Continents and provinces will be mapped to text URNs *containing* the list's text URN: continents are mapped to whole books, provinces to chapters, and lists to sections within chapters. Other properties will be mapped directly to the same section as the list.

- constructed ordered coasts
- identify degree of precision for points selected by Ptolemaic property
- compute density for points selected by Ptolemaic property:
    - create convex hull for each province in bonne equal area projection, and measure # sites for area of the polygon
    - create boundaries polygon as defined by Ptolemy using geotype.1

## Develop secondary data sets ##

- composite geo objects (especially rivers).  Pull in earlier work.

## Visualization ##

- Output as:
    - √ kml set of points
    - √ geoson set of points
    - leaflet maps
- implementations of both global projections
- regional maps?
- symbology for all of Ptolemy's properties, perhaps
    - convex hull for satrapy
    - icon for geotype
    - phys ?
    - ethnic ?

## Coordination with MSS? ##

How best coordinate machine data with MSS?


## Alignment with external data sets ##

- Pelagios. How can we best associate this project's identifiers with Pelagios identifiers?
- Recent Bern edition and associated database.  As each section of the text is validated, each geographic entity could be checked for alignment with the Bern database; conversely, each Bern entry in the corresponding section of the text can be checked against this project's inventory.  Discrepancies could be due to (at least) the following possibilities:
    - discrepancy is due to an error in one of the tables:  for Bern, I can check the table entry against the printed text;  for my edition, I need to go back to see if the digital edition reflects the digitized print source.  For books 2-3, that's Müller:  for books 4-7 it's Nobbe, so I want to handle them separately.
    - discrepancy in the tables accurately reflects a difference in the editions

