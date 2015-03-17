# Initial project: Ptolemy's *Geography* #



## Outline ##

- **Edit the text**.  Edition in TEI-conformant XML, with sections pushed to public repository in sections as they pass validation tests (see below)
- **Generate an inventory of points** identified by lon-lat value automatically from the XML edition.  This inventory will be managed as a CITE Collection, and will automatically  include classification of the top two levels of Ptolemy's taxonomy:  continents, "satrapies"/provinces.
- **Align** the current inventory with other projects (see below)
- **Manually index** text sections identified by URN to physical type, physical area and ethnic group.  Share this work with Pelagios participants.


### Editing and validating the edition ###


- every token output from a classified tokeinization tested by type:
    - lexical tokens are morphologically analyzed
    - identifiers of named geographic entities are validated against current inventory


### Generating an inventory of points ###

- implement a gradle build task generating the point from current edition and validating the inventory

