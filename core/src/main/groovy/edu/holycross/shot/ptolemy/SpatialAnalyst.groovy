package edu.holycross.shot.ptolemy


/**
 * A class analyzing spatial density and implied precision of
 * Ptolemy's data by list, as well as checking correlations of 
 * all properties of Ptolemaic points.
 * It would be cool to do this with geotools, but I can't get them
 * working. Perhaps work with data generated from R.
 */
class SpatialAnalyst {

  Integer debug =  0
  
  /*
- for each point in a set: find its twelfth value.
- find most precise fraction in the set
- test distribution of twelfths against expected distributtion for implied
precision.
   */


  

  /** List of  ModernSites objects */
  ArrayList siteList
  
  /** Constructor with list of ModernSite objects. 
   * @param modernSites Sites to analyze.
   */
  SpatialAnalyst(ArrayList modernSites){
    siteList = modernSites
    if (debug > 0) {
      System.err.println "SpatialAnalyst: initialized with " + siteList.size() + " sites"
    }
  }


  /** Creates map of strings indicating fractional value, in twelfths, for
   * each site in sites.
   * 
   */
  static HashMap lonTwelfthsMap(ArrayList sites) {
    def twelfths = [:]
    sites.each { s ->
      if (s.rawLonFract != null) {
	twelfths[s.ptolemySite.urnString] = keyForFract(s.rawLonFract)
      } else {
	twelfths[s.ptolemySite.urnString] = "00"
      }
    }
    return twelfths
  }



  static HashMap lonPrecisionMap(ArrayList sites) {
    def precision = [:]
    sites.each { s ->
      if (s.rawLonFract != null) {
	precision[s.ptolemySite.urnString] = precisionForFract(s.rawLonFract)
      } else {
	precision[s.ptolemySite.urnString] = 0
      }
    }
    return precision
  }




  
  static HashMap latTwelfthsMap(ArrayList sites) {
    def twelfths = [:]
    sites.each { s ->
      if (s.rawLatFract != null) {
	twelfths[s.ptolemySite.urnString] = keyForFract(s.rawLatFract)
      } else {
	twelfths[s.ptolemySite.urnString] = "00"
      }
    }
    return twelfths
  }


  static HashMap latPrecisionMap(ArrayList sites) {
    def precision = [:]
    sites.each { s ->
      if (s.rawLatFract != null) {
	precision[s.ptolemySite.urnString] = precisionForFract(s.rawLatFract)
      } else {
	precision[s.ptolemySite.urnString] = 0
      }
    }
    return precision
  }


  
  
  HashMap lonTwelfthsMap() {
    return lonTwelfthsMap(siteList)
  }


  HashMap latTwelfthsMap() {
    return latTwelfthsMap(siteList)
  }



  HashMap lonPrecisionMap() {
    return lonPrecisionMap(siteList)
  }


  HashMap latPrecisionMap() {
    return latPrecisionMap(siteList)
  }


  // minimum precision, in 12ths of a degree, implied
  // by a decimal fraction
  static Integer precisionForFract(BigDecimal decimal) {
    Integer twelfth
    switch(decimal) {
    case 0:
    twelfth = 0
    break
    
    case 0.083:
    twelfth = 1
    break

    case 0.167:
    twelfth = 2
    break

    case 0.25:
        twelfth = 3
    break
    case 0.333:
        twelfth = 4
    break
    case 0.416:
        twelfth = 1
    break
    case   0.5:
    twelfth = 2
    break

    case 0.583:
        twelfth = 1
    break
    case 0.667:
        twelfth = 4
    break
    case 0.75:
        twelfth = 3
    break
    case 0.833:
        twelfth = 2
    break
    case 0.916:
        twelfth = 1
    break
    default:
    throw new Exception("SpatialAnalyst: unrecognized fraction for twelfth ${decimal}")
    break
    }
    return twelfth

  }

  
  static String keyForFract(BigDecimal decimal)
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






    // maps a histogram of fractional values
  HashMap latTwelfthsHist() {
    HashMap histo = initTwelfths()
    siteList.each { s ->
      PtolemySite pt =  s.ptolemySite
      BigDecimal decimal = 0
      if (pt.latFraction != null) {
	decimal = pt.latFraction.getFractionValue()
      }
      String twelfths = keyForFract(decimal)
      histo[twelfths] = histo[twelfths] + 1
    }
    return histo
  }

}
