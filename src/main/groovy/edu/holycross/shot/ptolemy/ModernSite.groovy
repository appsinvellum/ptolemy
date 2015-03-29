package edu.holycross.shot.ptolemy



class ModernSite {

  
  PtolemySite ptolemySite
  BigDecimal rawLon
  BigDecimal rawLat

  BigDecimal rawLonFract
  BigDecimal rawLatFract

  BigDecimal projectedLon
  BigDecimal projectedLat

  ModernSite(PtolemySite theSite) {
    ptolemySite = theSite
  }
  

  
}
