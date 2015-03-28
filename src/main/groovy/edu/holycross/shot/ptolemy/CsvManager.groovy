package edu.holycross.shot.ptolemy

import groovy.json.JsonBuilder
import groovy.xml.MarkupBuilder

import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString

import edu.unc.epidoc.transcoder.TransCoder

/**
 * A class for managing tabular data in .csv or .tsv format.
 */
class CsvManager {


  // Strings allowed in edition for 0 of latitude value.
  /** Nominative case for 0 of latitude value. */
  static String equator = "ἰσημερινός"
  /** Accusative case for 0 of latitude value. */
  static String equator_acc = 'ἰσημερινόν'
  /** Abbreviated form 0 of latitude value. */
  static String equator_abbr = 'ἰσημεριν.'

  /** Creates a map of URNs to human-readable labels
   * from a csv file with URNs in the first column and
   * labelling strings in the second column.
   * @param csv File with the data, in csv format.
   * @returns Hash keyed by URN with labelling strings as values.
   */
  HashMap labelMap(File csv) {
    def urnMap = [:]
    Integer count  = 0
    csv.eachLine { l ->
      def cols = l.split(/,/)
      String urn = cols[0]
      
      urnMap[urn] = cols[1]
      count++
    }
    return urnMap
  }
  

  /** Attaches PtolemyList objects to PtolemySite objects.
   *  Sites are in a many-to-one relation to Lists. Sites should
   *  always have a URN value identifying the List they belong to.
   *  This method checks that value against the URNs in ptolemyLists,
   * and assigns any matching List object to the Site.
   * @param sites List of PtolemySite objects, presumed to have a
   * listUrn value.
   * @param ptolemyLists List of PtolemyLists to check.
   * @returns ArrayList of PtolemySite objects, with ptolemyList properties
   * for all sites where matching list was found.
   */
  ArrayList joinSitesToLists(ArrayList sites, ArrayList ptolemyLists) {
    ArrayList returnList = []
    sites.each { s ->
      PtolemyList pl = ptolemyLists.find {it.listUrn == s.listUrn}
      //System.err.println "For ${s.listUrn}
      s.ptolemyList = pl
      //System.err.println "${s} : added list " + s.ptolemyList
      returnList.add(s)
    }
    return returnList
  }
  

  /** Creates a list of PtolemyList objects from
   * a minimal CSV source with list URN in the first column,
   * sequence in the text in the second column, and CTS URN
   * value for the passage where it occurs in the third column.
   * @param csv Source file.
   * @returns ArrayList of PtolemyList objects.
   */
  ArrayList listsFromCsv(File csv) {
    def ptolLists = []
    Integer count = 0
    csv.eachLine { l ->
      if (count > 0) {
	def cols = l.split(/,/)
	if (cols.size() < 3) {
	  System.err.println "Did not find 3 columns in ${cols}"
	} else {
	  Integer seq = cols[1].toInteger()
	  PtolemyList ptolemyList = new PtolemyList(
	    listUrn: cols[0],textSequence: seq,passageUrn: cols[2]
	  )
	  ptolLists.add(ptolemyList)
	}
      }
      count++
    }
    return ptolLists
  }

  /** Adds data from CSV source to a list of PtolemyList objects.
   * @param ptolLists A list of PtolemyList objects.
   * @param csv CSV file with data for provinces.
   */
  ArrayList addProvinces(ArrayList ptolLists, File csv) {
    ArrayList resultList = ptolLists
    Integer count = 0
    csv.eachLine { l ->
      if (count > 0) {
	def cols = l.split(/,/)
	if (cols.size() != 3) {
	  System.err.println "Did not find 3 columns in ${cols}"
	} else {
	  def includedUrn = ~/${cols[1]}\..*/
	  def includedLists = ptolLists.findAll {it.passageUrn ==~ includedUrn}
	  if (includedLists == null) {
	    System.err.println "ERROR: no matching passage for " + cols[1] + ", failed for " + cols + " using pattern " + includedUrn
	  }
	  includedLists.each { pList ->
	    pList.provinceUrn = cols[2]
	    // Now replace in original list!
	    resultList = resultList.findAll { it.listUrn != pList.listUrn}
	    resultList.add(pList)
	  }
	}
      }
      count++
    }
    return resultList
  }

  

  
  /** Constructor.
   */
  CsvManager() {
  }



  /** Checks if a string is a valid Ptolemaic value for
   * the equautor (latitude 0).
   * @param latString The string to check.
   * @returns True if latString is a valid value for the equator.
   */
  static boolean isEquator(String latString) {
    return ((latString == equator) || (latString == equator_acc) || (latString == equator_abbr))
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

      if (isEquator(latDeg)) {
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



  // make minimal csv file for stet of points
  String minimalCsv (ArrayList ptolemyPoints, HashMap provinceLabels ) {
    String csv = "SiteId,Label,Pleiades\n"
    ptolemyPoints.each { ptol  ->
      def coords = PtolemyProjector.project(ptol)
      
      TransCoder xcoder = new TransCoder()
      xcoder.setParser("Unicode")
      xcoder.setConverter("GreekXLit")
      String xcoded = xcoder.getString(ptol.greekName)
      
      String description = "${xcoded} in ${provinceLabels[ptol.ptolemyList.provinceUrn]}"

      csv += "${ptol.urnString},${description},\n"
    }
    return csv
  }











  
  

  /**

  String toGeoJson(HashMap coordMap) {
    def featureList = []
    
    coordMap.keySet().each { site ->
      def coords  = coordMap[site]
      
      GeoJsonSite gjSite = new GeoJsonSite(geometry: [:], properties: [:], type: 'Feature')
      gjSite.properties = ['urn': site ]
      gjSite.geometry = ['coordinates': [coords[0], coords[1]], 'type': 'Point']
      featureList.add(gjSite)
    }
    return (featuresToGeoJson(featureList))
  }

   */  

  /** Takes a map of site URNs to Ptolemy data arrays,
   * and generates a map of site URNs to lon-lat pairs.
   * @param mapSource Source data map.
   * @returns Map of lon-lat pairs.
   */

  /*
  HashMap convertCoords(HashMap mapSource) {

    HashMap decimalCoords = [:]
    mapSource.keySet().each { site ->
      boolean ok = true
      
      BigDecimal lon = 0
      BigDecimal lat = 0
    
      def siteData = mapSource[site]
      String lonDeg = siteData[4]
      String lonFract = siteData[5]
      String latDeg = siteData[6]
      String latFract = siteData[7]
      
      String demarcatedLon = lonFract + '"'
      String demarcatedLat = latFract + '"'


      MilesianInteger milLon
      MilesianInteger milLat

      MilesianFraction milLonFract
      MilesianFraction milLatFract

      
      try {
	milLon = new MilesianInteger(lonDeg)
      } catch (Exception e) {
	System.err.println "Could not parse lon.deg. ${lonDeg} in row ${siteData}"
	ok = false
	errCount++
      }
      
      if (lonFract.size() > 0) {
	try {
	  milLonFract = new MilesianFraction(demarcatedLon)
	  
	} catch (Exception e) {
	  System.err.println "Could not parse lon.fraction ${demarcatedLon} in row ${siteData}"
	  System.err.println "\tlen was ${lonFract.length()} with ${lonFract.codePointCount(0,lonFract.length())} code points, first one at ${lonFract.codePointAt(0)}.\n\n"
	  ok = false
	  errCount++;
	}
      }

      if (isEquator(latDeg)) {
	// valid value == 0
      } else if (latDeg.size() == 0) {
	//  empty string == valid value of 0
      } else {

	try {
	  milLat = new MilesianInteger(latDeg)
	} catch (Exception e) {
	  System.err.println "Could not parse lat.deg. ${latDeg} in row ${siteData}"
	  ok = false
	  errCount++;
	}
      }
      

      if (latFract.size() > 0) {
	try {
	  milLatFract = new MilesianFraction(demarcatedLat)
	  
	} catch (Exception e) {
	  System.err.println "Could not parse lon.fraction ${demarcatedLat} in row ${siteData}"
	  System.err.println "\tlen was ${lonFract.length()} with ${latFract.codePointCount(0,latFract.length())} code points, first one at ${latFract.codePointAt(0)}.\n\n"
	  ok = false
	  errCount++;
	}
      }
      
      if (ok) {
	lon = 0
	lat = 0
	if (milLon != null) {
	  lon = milLon.toInteger() as BigDecimal
	}
	if (milLonFract != null) {
	  lon += milLonFract.getFractionValue()
	}

	if (milLat != null) {
	  lat = milLat.toInteger() as BigDecimal
	}
	if (milLatFract != null) {
	  lat += milLatFract.getFractionValue()
	}

	// ADD CHECK FOR NEGATIVE LAT!

	ArrayList coords = [lon, lat]
	decimalCoords[site] = coords
      }
      
      
    }
    return decimalCoords
  }
*/

  /*
  HashMap sitesForList(HashMap siteHash, String listId) {
  }
  HashMap sitesForPassage(HashMap listHash, HashMap siteHash, String urnString) {
    HashMap filtered = [:]
    return filtered
    }
*/
  
}


