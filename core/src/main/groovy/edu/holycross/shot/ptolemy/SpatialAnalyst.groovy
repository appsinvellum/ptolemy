package edu.holycross.shot.ptolemy


/**
 * A class analyzing spatial density and implied precision of
 * Ptolemy's data by list, as well as checking correlations of 
 * all properties of Ptolemaic points.
 * It would be cool to do this with geotools, but I can't get them
 * working. Perhaps work with data generated from R.
 */
class SpatialAnalyst {

  /*
- for each point in a set: find its twelfth value.
- find most precise fraction in the set
- test distribution of twelfths against expected distributtion for implied
precision.
   */


  // list of  ModernSites
  ArrayList siteList
  
  // constructor with list of ModernSite objects
  SpatialAnalyst(ArrayList modernSites){
    siteList = modernSites
  }


  static HashMap lonTwelfthsMap(ArrayList sites) {
    def twelfths = [:]
    sites.each { s ->
      if (s.rawLonFract != null) {
	twelfths[s.ptolemySite.urnString] = keyForFract(s.rawLonFract)
      } else {
	System.err.println "NULL val for rawlonfract on ${s} (${s.ptolemySite.urnString})"
      }
    }
    return twelfths
  }

  static HashMap latTwelfthsMap(ArrayList sites) {
    def twelfths = [:]
    sites.each { s ->
      if (s.rawLatFract != null) {
	twelfths[s.ptolemySite.urnString] = keyForFract(s.rawLatFract)
      } else {
	System.err.println "NULL val for rawlatfract on ${s} (${s.ptolemySite.urnString})"
      }
    }
    return twelfths
  }


  
  HashMap lonTwelfthsMap() {
    return lonTwelfthsMap(siteList)
  }


  HashMap latTwelfthsMap() {
    return latTwelfthsMap(siteList)
  }


  
  String keyForFract(BigDecimal decimal)
  throws Exception {
    String twelfth 
    switch(decimal) {
    case 0:
    twelfth = "00"
    break
    case 0.083:
        twelfth = "01"
    break

    case 0.167:
        twelfth = "02"
    break

    case 0.25:
        twelfth = "03"
    break
    case 0.333:
        twelfth = "04"
    break
    case 0.416:
        twelfth = "05"
    break
    case   0.5:
    twelfth = "06"
    break

    case 0.583:
        twelfth = "07"
    break
    case 0.667:
        twelfth = "08"
    break
    case 0.75:
        twelfth = "09"
    break
    case 0.833:
        twelfth = "10"
    break
    case 0.916:
        twelfth = "11"
    break
    default:
    throw new Exception("SpatialAnalyst: unrecognized fraction for twelfth ${decimal}")
    break
    }
    return twelfth
  }


  HashMap initTwelfths() {
    return ["00": 0,
	    "01": 0,
	    "02": 0,
	    "03": 0,
	    "04": 0,
	    "05": 0,
	    "06": 0,
	    "07": 0,
	    "08": 0,
	    "09": 0,
	    "10": 0,
	    "11": 0
	   ]
  }
  
  // maps a histogram of fractional values
  HashMap lonTwelfthsHist() {
    HashMap histo = initTwelfths()
    siteList.each { s ->
      PtolemySite pt =  s.ptolemySite
      BigDecimal decimal = 0
      if (pt.lonFraction != null) {
	decimal = pt.lonFraction.getFractionValue()
      }
      String twelfths = keyForFract(decimal)
      histo[twelfths] = histo[twelfths] + 1
    }
    return histo
  }
  
}
