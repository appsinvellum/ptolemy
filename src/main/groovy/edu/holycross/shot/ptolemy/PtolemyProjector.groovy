package edu.holycross.shot.ptolemy


import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString


/** A class projecting Ptolemaic data to modern formats.
 * All work with coordinate pairs is in the order longitude-latitude.
 */
class PtolemyProjector {


  static BigDecimal baseLatitude = 36.0 // "parallel through Rhodes"

  static BigDecimal circumfScale = 180 / 250 // Ptolemy / Eratosthenes
  
  static BigDecimal lonOff = 14.0
  
  BigDecimal altLonOffset
  

  
  PtolemySite pSite

  
  PtolemyProjector(PtolemySite site) {
    pSite = site
  }


  static ArrayList project(PtolemySite site) {
    def shrunk = shrink(site)


    BigDecimal lonAdjusted = shrunk[0] - lonOff

    BigDecimal latOff = baseLatitude - (baseLatitude * circumfScale)
    BigDecimal latAdjusted = shrunk[1] + latOff
    
    return ([lonAdjusted,latAdjusted])
  }


  
  static ArrayList shrink(PtolemySite site) {
    return [shrinkLon(site), shrinkLat(site)]
  }
  
  static BigDecimal shrinkLat (PtolemySite site) {
    return site.getLatitude() * circumfScale
  }


  static BigDecimal shrinkLon (PtolemySite site) {
    return site.getLongitude() * circumfScale
  }

  ArrayList project() {
    def shrunk = shrink()
    println "PtolemyProjector: Site ll is " + pSite.getLL()
    println "Projector: shrunk is "  + shrunk
    
    BigDecimal lonOff = 22.0
    BigDecimal lonAdjusted = shrunk[0] - lonOff

    BigDecimal latOff = baseLatitude - (baseLatitude * circumfScale)


    
    BigDecimal latAdjusted = shrunk[1] + latOff
    println "lon and lat offsets are " + lonOff + " / " + latOff
    println "${shrunk[1]} + ${latOff} == ${latAdjusted}"
    def adjusted = [lonAdjusted,latAdjusted]
    println "Adjusted: " + adjusted
    return (adjusted)
  }

  
  ArrayList shrink() {

    def shrunken = [shrinkLon(), shrinkLat()]
    println "Shrinking to " + shrunken + " because ..."
    println "shrinkLong " + shrinkLon() + " from " + pSite.getLongitude() + " and "
    println "shrinkLat " + shrinkLat() + " from " + pSite.getLatitude()
    return shrunken
  }
  
  BigDecimal shrinkLat () {
    return pSite.getLatitude() * circumfScale
  }


  BigDecimal shrinkLon () {
    println "\tshrinkLon: from " + pSite.getLongitude()
    return pSite.getLongitude() * circumfScale
  }
  
  String toString() {
    return pSite.toString()
  }
}