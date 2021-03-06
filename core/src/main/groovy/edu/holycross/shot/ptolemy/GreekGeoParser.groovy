package edu.holycross.shot.ptolemy;

import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.GreekString


/**
 * A class for parsing the geographic database in Ptolemy's Geography.  
 * Working from a TEI source file following a specified set
 * of markup conventions, GreekGeoParser objects can create ordered lists of
 * Ptolemy's two fundamental data structures, the list (PtolemyList class)
 * and the individual site (PtolemySite).
 */
class GreekGeoParser {


  Integer debug = 0

  
  /** TEI namespace as a groovy Namespace object. */
  static groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")


  /* *********************************************************************************** */
  /* *** Strings allowed in edition for 0 of latitude value. *************************** */
  /* *********************************************************************************** */
  
  /** Nominative case for 0 of latitude value. */
  static String equator = "ἰσημερινός"
  
  /** Accusative case for 0 of latitude value. */
  static String equator_acc = 'ἰσημερινόν'
  
  /** Abbreviated form 0 of latitude value. */
  static String equator_abbr = 'ἰσημεριν.'

  /** Checks if a string is a valid Ptolemaic value for
   * the equautor (latitude 0).
   * @param latString The string to check.
   * @returns True if latString is a valid value for the equator.
   */
  static boolean isEquator(String latString) {
    return ((latString == equator) || (latString == equator_acc) || (latString == equator_abbr))
  }
  /* *********************************************************************************** */
  /* End Strings for 0 of latitude. */
  /* *********************************************************************************** */
  
  /** Root of parsed document. */
  groovy.util.Node root

  /** Constructor from a File object.
   * @param xmlFile TEI source for edition.
   */
  GreekGeoParser(File xmlFile) {
     root = new XmlParser().parse(xmlFile)
  }



  /** Checks all coordinates in a map of site name to
   * data collection.
   * @param mapSource A map keyed by URNs for sites,
   * containing arrays composed of the following properties:
   * sequence within the work, URN for the list it bleongs to,
   * sequence withint the list, name of the site, lon. degree,
   * lon. fraction, lat. degree, lat. fraction and a boolean 
   * value indicating wheter or not latitude is south of the equator.
   * @returns True if all coordinates are valid.
   */
  boolean validateCoords(HashMap mapSource) {
    boolean ok = true
    Integer errCount = 0
    
    mapSource.keySet().each { site ->
      def siteData = mapSource[site]

      String lonDeg = siteData[4]
      String lonFract = siteData[5]
      String latDeg = siteData[6]
      String latFract = siteData[7]
      
      String demarcatedLon = lonFract + '"'
      String demarcatedLat = latFract + '"'

      try {
	MilesianInteger mi = new MilesianInteger(lonDeg)
      } catch (Exception e) {
	System.err.println "Could not parse lon.deg. ${lonDeg} in row ${siteData}"
	ok = false
	errCount++
      }
      
      if (lonFract.size() > 0) {
	try {
	  MilesianFraction mf = new MilesianFraction(demarcatedLon)
	  
	} catch (Exception e) {
	  System.err.println "Could not parse lon.fraction ${demarcatedLon} in row ${siteData}"
	  System.err.println "\tlen was ${lonFract.length()} with ${lonFract.codePointCount(0,lonFract.length())} code points, first one at ${lonFract.codePointAt(0)}.\n\n"
	  ok = false
	  errCount++;
	}
      }

      if (GreekGeoParser.isEquator(latDeg)) {
	// valid value == 0
      } else if (latDeg.size() == 0) {
	//  empty string == valid value of 0
      } else {

	try {
	  MilesianInteger mi = new MilesianInteger(latDeg)
	} catch (Exception e) {
	  System.err.println "Could not parse lat.deg. ${latDeg} in row ${siteData}"
	  ok = false
	  errCount++
	    }
      }
      

      if (latFract.size() > 0) {
	try {
	  MilesianFraction mf = new MilesianFraction(demarcatedLat)
	  
	} catch (Exception e) {
	  System.err.println "Could not parse lon.fraction ${demarcatedLat} in row ${siteData}"
	  System.err.println "\tlen was ${lonFract.length()} with ${latFract.codePointCount(0,latFract.length())} code points, first one at ${latFract.codePointAt(0)}.\n\n"
	  ok = false
	  errCount++;
	}
      }
      
    }
    System.err.println "Totals: ${errCount} errors found"
    return ok
  }




  
  /** Maps indvidual list items with lon-lat data
   * onto the containing list structure. Useful in conjunction with
   * CsvManager for validating contents of entries and verifying
   * quality of XML source.
   * @returns Map of CITE URNs for items to CITE URNs for lists.
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
	    boolean negativeLat = false
	    // get name and measure from each item:
	    siteTotal++;
	    String siteName =  i[tei.name].text().replaceAll(/[\n\r]/,'')
	    siteName = siteName.replaceAll(/[ \t]+/,' ')
	    String siteUrn =  i[tei.name][0].'@key'

	    def coords = []
	    i[tei.measure][tei.num].each { digit ->
	      coords.add(digit.text())
	      if (digit.'@n' == "NO/T") {
		negativeLat = true
	      }
	    }

	    if (coords.size() != 4) {
	      throw new Exception("GreekGeoParser: too few coordinates for site ${siteUrn} in list ${listId}")
	    }
	    def dataRecord = [siteTotal, listId, itemIdx, siteName, coords[0], coords[1], coords[2], coords[3], negativeLat]

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
	    boolean negativeLat = false
	    // get name and measure from each item:
	    siteTotal++;
	    String siteName =  i[tei.name].text().replaceAll(/[\n\r]/,'')
	    siteName = siteName.replaceAll(/[ \t]+/,' ')
	    String siteUrn =  i[tei.name][0].'@key'

	    def coords = []
	    i[tei.measure][tei.num].each { digit ->
	      coords.add(digit.text())
	      if (digit.'@n' == "NO/T") {
		negativeLat = true
	      }
	    }

	    if (coords.size() != 4) {
	      throw new Exception("GreekGeoParser: too few coordinates for site ${siteUrn} in list ${listId}")
	    }



	    // Replace this with a PtolemySite object
	    
	    def dataRecord = [siteTotal, listId, itemIdx, siteName, coords[0], coords[1], coords[2], coords[3], negativeLat]
	    siteMap[siteUrn] = dataRecord
	    itemIdx++
	  }
	}
      }
    }
    return siteMap
  }

  /** Creates an ordered list of PtolemySite objects.
   * @returns List of PtolemySites.
   * @throws Exception if not four coordinates for a site.
   */
  ArrayList indexPtolemySites ()
  throws Exception{
    def siteList = []
    Integer count = 0
    Integer siteTotal = 0
    root[tei.text][tei.body][tei.div][tei.div][tei.div].each { divNode ->
      divNode[tei.list].each { l -> 
	if (l.'@type' == "simple") {
	  count++;
	  String listId = formListUrn(count)

	  Integer itemIdx = 0
	  l[tei.item].each {  i ->
	    boolean negativeLat = false
	    // get name and measure from each item:
	    siteTotal++;
	    String siteName =  i[tei.name].text().replaceAll(/[\n\r]/,'')
	    siteName = siteName.replaceAll(/[ \t]+/,' ')
	    String siteUrn =  i[tei.name][0].'@key'

	    def coords = []
	    i[tei.measure][tei.num].each { digit ->
	      coords.add(digit.text())
	      if (digit.'@n' == "NO/T") {
		negativeLat = true
	      }
	    }

	    if (coords.size() != 4) {
	      throw new Exception("GreekGeoParser: too few coordinates for site ${siteUrn} in list ${listId}")
	    }

	    
	    MilesianInteger lat1
	    if ((! GreekGeoParser.isEquator(coords[2])) && (coords[2].size() > 0)) {
	      lat1  = new MilesianInteger(coords[2])
	    }
	    MilesianInteger lon1 = new MilesianInteger(coords[0])

	    MilesianFraction lat2
	    MilesianFraction lon2

	    if (coords[1].size() > 0) {
	      lon2 = new MilesianFraction(coords[1] + '"')
	    }
	    if (coords[3].size() > 0) {
	      lat2 = new MilesianFraction(coords[3] + '"')
	    }

	    if (debug > 2) {
	      System.err.println("Looking at coords " + coords)
	      System.err.println ("Fracts in ${coords[1]} and ${coords[3]}")
	      System.err.println "Yields " + lon2 + ":" + lat2
	    }
	    def dataRecord = [urnString: siteUrn, textSequence : siteTotal, listUrn: listId, listIndex: itemIdx, greekName: siteName, southLatitude: negativeLat, lonDegree:  lon1]
	    
	    if (lat1 != null) {
	      dataRecord["latDegree"] = lat1
	    }
	    if (lat2 != null) {
	      dataRecord["latFraction"] = lat2
	    }
	    if (lon2 != null) {
	      dataRecord["lonFraction"] = lon2
	    }
	    siteList.add(new PtolemySite(dataRecord))
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
	    boolean negativeLat = false
	    // get name and measure from each item:
	    siteTotal++;
	    String siteName =  i[tei.name].text().replaceAll(/[\n\r]/,'')
	    siteName = siteName.replaceAll(/[ \t]+/,' ')
	    String siteUrn =  i[tei.name][0].'@key'

	    def coords = []
	    i[tei.measure][tei.num].each { digit ->
	      coords.add(digit.text())
	      if (digit.'@n' == "NO/T") {
		negativeLat = true
	      }
	    }

	    if (coords.size() != 4) {
	      throw new Exception("GreekGeoParser: too few coordinates for site ${siteUrn} in list ${listId}")
	    }


	    MilesianInteger lat1
	    if ((! GreekGeoParser.isEquator(coords[2])) && (coords[2].size() > 0)) {
	      lat1 = new MilesianInteger(coords[2])
	    }
	    MilesianInteger lon1 = new MilesianInteger(coords[0])


	    MilesianFraction lat2 = null
	    MilesianFraction lon2 = null
	    if (coords[3].size() > 0) {
	      lat2 = new MilesianFraction(coords[3] + '"')
	    }
	    if (coords[1].size() > 0) {
	      lon2 = new MilesianFraction(coords[1] + '"')
	    }
	    
	    def dataRecord = [urnString: siteUrn, textSequence : siteTotal, listUrn: listId, listIndex: itemIdx, greekName: siteName, southLatitude: negativeLat, lonDegree:  lon1]
	    if (lon1 != null) {
	      dataRecord["latDegree"] = lat1
	    }
	    if (lat2 != null) {
	      dataRecord["latFraction"] = lat2
	    }
	    if (lon2 != null) {
	      dataRecord["lonFraction"] = lon2
	    }
	    siteList.add(new PtolemySite(dataRecord))
	    itemIdx++
	  }
	}
      }
    }
    return siteList
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