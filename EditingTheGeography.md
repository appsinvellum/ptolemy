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

### Aligning the editions with other work on Ptolemy ###

- Pelagios.  Collaborate in order share the joy and labor of identifying Ptolemy's taxonomy.  How can we best associate this project's identifiers with Pelagios identifiers?
- Recent Bern edition and associated database.  As each section of the text is validated, each geographic entity will be checked for alignment with the Bern database; conversely, each Bern entry in the corresponding section of the text will be checked against this project's inventory.


### Manual indexing ###

Need to define in conjunction with Pelagios group a standard vocabulary for Ptolemy's taxonomy of:
    - physical geographic type
    - physical geographic area
    - ethnic/cultural group
