package edu.holycross.shot.ptolemy;

import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString


/**
 * A class for managing tabular data in .csv or .tsv format.
 */
class TableManager {

  static String equator = "ἰσημερινός"

  /** Constructor.
   */
  TableManager() {
  }


  boolean validateCoords(HashMap mapSource) {
    boolean ok = true
    Integer errCount = 0
    mapSource.keySet().each { site ->
      def siteData = mapSource[site]
      String lonDeg = siteData[4]
      String latDeg = siteData[6]
      try {
	MilesianInteger mi = new MilesianInteger(lonDeg)
      } catch (Exception e) {
	System.err.println "Could not parse lon.deg. ${lonDeg} in row ${siteData}"
	ok = false
	errCount++
      }

      

      String lonFract = siteData[5]
      String latFract = siteData[7]
      String demarcatedLon = lonFract + '"'
      String demarcatedLat = latFract + '"'
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


      try {
	MilesianInteger mi = new MilesianInteger(latDeg)
      } catch (Exception e) {
	System.err.println "Could not parse lat.deg. ${latDeg} in row ${siteData}"
	ok = false
	errCount++
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
      

  // cols 5-8 are the coords
  boolean validateCoords(File tsvSource) {
    Integer count = 0
    boolean ok = true
    tsvSource.eachLine { l ->
      if (count > 0) {
	def cols = l.split(/\t/)

	if (cols.size() < 9) {
	  System.err.println "Only ${cols.size()} columns in ${l}"
	} else {

	
	  String lonDeg = cols[5]
	  try {
	    MilesianInteger mi = new MilesianInteger(lonDeg)
	  } catch (Exception e) {
	    System.err.println "Could not parse ${lonDeg} in row ${l}"
	    ok = false
	  }
	
	  String lonFract = cols[6]
	  String latDeg = cols[7]
	  String latFract = cols[8]
	}
	
	
      }
      count++
    }
    return ok
  }
  
  
}