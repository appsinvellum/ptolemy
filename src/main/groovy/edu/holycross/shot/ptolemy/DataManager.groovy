package edu.holycross.shot.ptolemy

import groovy.json.JsonBuilder

import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString


/**
 * A class for managing tabular data in .csv or .tsv format.
 */
class DataManager {

  static String equator = "ἰσημερινός"
  static String equator_acc = 'ἰσημερινόν'
  static String equator_abbr = 'ἰσημεριν.'

  
  
  /** Constructor.
   */
  DataManager() {
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
      

  String toGeoJson(HashMap coordMap) {
    def featureList = []
    
    coordMap.keySet().each { site ->
      def coords  = coordMap[site]
      
      GeoJsonSite gjSite = new GeoJsonSite(geometry: [:], properties: [:], type: 'Feature')
      gjSite.properties = ['urn': site]
      gjSite.geometry = ['coordinates': [coords[0], coords[1]], 'type': 'Point']
      featureList.add(gjSite)
    }

    JsonBuilder bldr = new groovy.json.JsonBuilder()
    bldr(type : 'FeatureCollection', features : featureList)
    return bldr.toPrettyString()
  }

  

  /** Takes a map of site URNs to Ptolemy data arrays,
   * and generates a map of site URNs to lon-lat pairs.
   * @param mapSource Source data map.
   * @returns Map of lon-lat pairs.
   */
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

  HashMap sitesForList(HashMap siteHash, String listId) {
  }
  
  HashMap sitesForPassage(HashMap listHash, HashMap siteHash, String urnString) {
    HashMap filtered = [:]
    
    
    return filtered
  }
  
}


