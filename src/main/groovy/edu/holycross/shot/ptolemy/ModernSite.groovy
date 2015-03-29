package edu.holycross.shot.ptolemy



class ModernSite {

  
  PtolemySite ptolemySite
  BigDecimal rawLon
  BigDecimal rawLat

  BigDecimal rawLonFract
  BigDecimal rawLatFract

  ArrayList projectedCoords


  // create list of modern sites from list of ptolemy sites
  static ArrayList ptolemyToModernSites(ArrayList ptolemySites) {
    ArrayList moderns = []
    ptolemySites.each {
      moderns.add(new ModernSite(it))
    }
    return moderns
  }
  
  ModernSite(PtolemySite theSite) {
    ptolemySite = theSite
    rawLon = ptolemySite.lonDegree.toInteger() as Integer
    
    if (ptolemySite.lonFraction != null) {
      rawLon = ptolemySite.lonFraction.getFractionValue()
    }

    if (ptolemySite.latDegree != null) {
      rawLat = ptolemySite.latDegree.toInteger() as Integer
    }
    if (ptolemySite.latFraction != null) {
      rawLat = ptolemySite.latFraction.getFractionValue()
    }
    
    PtolemyProjector projector = new PtolemyProjector(ptolemySite)
    projectedCoords = projector.project()
    
  }
  

  
}
