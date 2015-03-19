package edu.holycross.shot.ptolemy;

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
  boolean isEquator(String latString) {
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
      



  
  
}