package edu.holycross.shot.ptolemy;

/**
 * A class for parsing the geographic database in Ptolemy's Geography.  
 * Works from a TEI source file following a specified set
 * of markup conventions.
 */
class GeoParser {

  /** TEI namespace as a groovy Namespace object. */
  static groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

  /** Root of parsed document. */
  groovy.util.Node root


  /** Constructor from a File object.
   * @param xmlFile TEI source for edition.
   */
  GeoParser(File xmlFile) {
    root = new XmlParser().parse(xmlFile)
  }

  /** Maps indvidual list items with lon-lat data
   * onto the containing list structure.
   * @returns Map of CITE URNs for items to CITE URNs
   * for lists.
   * @throws Exception if not four coordinates for a site.
   */
  java.util.HashMap indexListItems ()
  throws Exception{
    def siteMap = [:]
    Integer count = 0
    Integer siteTotal = 0
    root[tei.text][tei.body][tei.div][tei.div][tei.div].each { divNode ->
      divNode[tei.list].each { l -> 
	if (l.'@type' == "simple") {
	  count++;
	  String listId = formListUrn(count)

	  Integer itemIdx = 0
	  l[tei.item].each {  i ->
	    // get name and measure from each item:
	    siteTotal++;
	    String siteName =  i[tei.name].text().replaceAll(/[\n\r]/,'')
	    siteName = siteName.replaceAll(/[ \t]+/,' ')
	    String siteUrn =  i[tei.name][0].'@key'

	    def coords = []
	    i[tei.measure][tei.num].each { digit ->
	      coords.add(digit.text())
	    }

	    if (coords.size() != 4) {
	      throw new Exception("GeoParser: too few coordinates for site ${siteUrn} in list ${listId}")
	    }
	    def dataRecord = [listId, itemIdx, siteName, coords[0], coords[1], coords[2], coords[3]]

	    siteMap[siteUrn] = dataRecord
	    itemIdx++

	  }
	}
      }
      divNode[tei.p][tei.list].each { l ->
	if (l.'@type' == "simple") {
	  count++;
	  String listId = formListUrn(count)
	  Integer itemIdx = 0
	  l[tei.item].each {  i ->
	    // get name and measure from each item:
	    siteTotal++;
	    String siteName =  i[tei.name].text().replaceAll(/[\n\r]/,'')
	    siteName = siteName.replaceAll(/[ \t]+/,' ')
	    String siteUrn =  i[tei.name][0].'@key'

	    def coords = []
	    i[tei.measure][tei.num].each { digit ->
	      coords.add(digit.text())
	    }

	    if (coords.size() != 4) {
	      throw new Exception("GeoParser: too few coordinates for site ${siteUrn} in list ${listId}")
	    }
	    def dataRecord = [listId, itemIdx, siteName, coords[0], coords[1], coords[2], coords[3]]

	    siteMap[siteUrn] = dataRecord
	    itemIdx++
	  }
	}
      }
    }
    return siteMap
  }
  

  /** Maps the basic list structure of the Geography
   * to CTS URNs where the list occurs.
   * @returns Map of CITE URNs for lists to CTS URNs
   * for passages.
   */
  java.util.HashMap indexLists () {
    def listMap = [:]
    Integer count = 0
    String urnBase = "urn:cts:greekLit:tlg0363.tlg009.hc01:"
    String textUrn
    String book
    String chapter
    String section
    
    root[tei.text][tei.body][tei.div].each { bookNode ->
      book = bookNode.'@n'
      bookNode[tei.div].each { chapNode ->
	chapter = chapNode.'@n'
	chapNode[tei.div].each { sectNode ->
	  section = sectNode.'@n'

	  textUrn = "${urnBase}${book}.${chapter}.${section}"
	  // first get lists

	  sectNode[tei.list].each { l ->
	    if (l.'@type' == "simple") {
	      count++;
	      String listId = formListUrn(count)
	      listMap[listId] = textUrn
	    }
	  }

	  sectNode[tei.p][tei.list].each { l ->
	    if (l.'@type' == "simple") {
	      count++;
	      String listId = formListUrn(count)
	      listMap[listId] = textUrn
	    }
	  }
	  
	}
      }
    }
    return listMap
  }


  
  /** Forms URN for ptolemy list collection
   * with strings formatted for convenient sorting.
   * @param i Unique identifier for list.
   */
  static String formListUrn(Integer i) {
    String baseUrn = "urn:cite:ptolemy:list"
    if (i > 999) {
      return "${baseUrn}.list$i"
    } else if (i > 99) {
      return "${baseUrn}.list0$i"
    } else if (i > 9) {
      return "${baseUrn}.list00$i"
    } else if (i > 0) {
      return "${baseUrn}.list000$i"
    }
  }

  
  
}