package edu.holycross.shot.ptolemy

import groovy.json.JsonBuilder
import groovy.xml.MarkupBuilder

import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString

/** A class projecting Ptolemaic data to modern formats.
 * All work with coordinate pairs is in the order longitude-latitude.
 */
class PtolemyProjector {

  /** Latitude Ptolemy used as baseline for computing spherical 
   * coordinates from ground distances. */
  static BigDecimal baseLatitude = 36.0 // "parallel through Rhodes"


  /** Ratio of Ptolemy's circumference (180,000 stades) to
   * that of Eratosthenes (250,000 stades). */
  static BigDecimal circumfScale = 180 / 250 // Ptolemy / Eratosthenes


  /** Default distance in modern longitude coordinates that Ptolemy's
   * origin of longitude was west of Greenwich. */
  static BigDecimal lonOff = 14.0

  /** Maximum precision for decimal conversions is 3 decimal places. */
  static Integer maxDigits = 10**3
  

  /** Alternative, dynamically configurable value for location of
   * Ptolemy's origin of longitude. */
  BigDecimal altLonOffset

  /**  Site to project.
   */
  PtolemySite pSite

  /** Constructor for projecting an individual site.
   * @param site The site to manipulate.
   */
  PtolemyProjector(PtolemySite site) {
    pSite = site
  }

  // never return more than 3 decimal places
  static BigDecimal round(BigDecimal dec)
  throws Exception  {
    // check for sign:
    BigDecimal testVal = dec.abs()
    if (testVal > 1000) {
      throw new Exception("PtolemyProjector: cannot round value > 1000 or < -1000")
    }
    return Math.round(dec * maxDigits) / maxDigits
  }


  /** Projects a single site into modern lon-lat coordinates
   * using the default, static value for the offset of Ptolemy's
   * origin of longitude.
   * @param set The site to project.
   * @returns A longitude / latitude pair (in that order),
   * as BigDecimals.
   */
  static ArrayList project(PtolemySite site) {
    return project(site, PtolemyProjector.lonOff)
  }

  /** Projects a single site into modern lon-lat coordinates
   * using a given value for the offset of Ptolemy's
   * origin of longitude.
   * @param site The site to project.
   * @param lonOffset  Number of degrees west of Greenwich to
   * use for Ptolemy's origin of longitude.
   * @returns A longitude / latitude pair (in that order),
   * as BigDecimals.
   */
  static ArrayList project(PtolemySite site, BigDecimal lonOffset) {
    def shrunk = shrink(site)
    BigDecimal lonAdjusted = shrunk[0] - lonOffset

    BigDecimal scaledBase = PtolemyProjector.round(baseLatitude * circumfScale)
    BigDecimal latOff = baseLatitude - scaledBase
    BigDecimal latAdjusted = shrunk[1] + latOff
    
    return ([lonAdjusted,latAdjusted])
  }


  /** Rescales Ptolemy's raw coordinates for a site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @param set The site to rescale.
   * @returns A longitude / latitude pair (in that order),
   * as BigDecimals.
   */
  static ArrayList shrink(PtolemySite site) {
    return [shrinkLon(site), shrinkLat(site)]
  }



  /** Rescales Ptolemy's raw latitude coordinate for a site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @param set The site to rescale.
   * @returns The rescaled latitude value as a BigDecimal.
   */
  static BigDecimal shrinkLat (PtolemySite site) {
    return
    PtolemyProjector.round(site.getLatitude() * circumfScale )
  }


  /** Rescales Ptolemy's raw longitude coordinate for a site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @param set The site to rescale.
   * @returns The rescaled longitude value as a BigDecimal.
   */
  static BigDecimal shrinkLon (PtolemySite site) {
    return PtolemyProjector.round(site.getLongitude() * circumfScale)
  }


  /** Projects the site into modern lon-lat coordinates
   * using the default value for the offset of Ptolemy's
   * origin of longitude.
   * @param lonOffset  Number of degrees west of Greenwich to
   * use for Ptolemy's origin of longitude.
   * @returns A longitude / latitude pair (n that order),
   * as BigDecimals.
   */  
  ArrayList project() {
    return project(lonOff)
  }


  /** Projects the site into modern lon-lat coordinates
   * using the provided value for the offset of Ptolemy's
   * origin of longitude.
   * @param lonOffset  Number of degrees west of Greenwich to
   * use for Ptolemy's origin of longitude.
   * @returns A longitude / latitude pair (n that order),
   * as BigDecimals.
   */  
  ArrayList project(BigDecimal lonOffset) {
    return PtolemyProjector.project(pSite, lonOffset)
  }

  
  /** Rescales Ptolemy's raw coordinates for the site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @returns A longitude / latitude pair (in that order),
   * as BigDecimals.
   */
  ArrayList shrink() {
    def shrunken = [shrinkLon(), shrinkLat()]
    return shrunken
  }


  /** Rescales Ptolemy's raw latitude coordinate for the site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @returns The rescaled latitude value as a BigDecimal.
   */
  BigDecimal shrinkLat () {
    return PtolemyProjector.round(pSite.getLatitude() * circumfScale)
  }
  
  /** Rescales Ptolemy's raw longitude coordinate for the site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @returns The rescaled longitude value as a BigDecimal.
   */
  BigDecimal shrinkLon () {
    return PtolemyProjector.round(pSite.getLongitude() * circumfScale)
  }


  /** Overrides default toString() method.
   * @returns The PtolemySite, as a String.
   */
  String toString() {
    return pSite.toString()
  }
  
}

