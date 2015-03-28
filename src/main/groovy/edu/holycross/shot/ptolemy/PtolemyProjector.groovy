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



  /** Projects a single site into modern lon-lat coordinates
   * using the default, static value for the offset of Ptolemy's
   * origin of longitude.
   * @param set The site to project.
   * @returns A longitude / latitude pair (in that order),
   * as BigDecimals.
   */
  static ArrayList project(PtolemySite site) {
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
    BigDecimal latOff = baseLatitude - (baseLatitude * circumfScale)
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
    return site.getLatitude() * circumfScale
  }


  /** Rescales Ptolemy's raw longitude coordinate for a site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @param set The site to rescale.
   * @returns The rescaled longitude value as a BigDecimal.
   */
  static BigDecimal shrinkLon (PtolemySite site) {
    return site.getLongitude() * circumfScale
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
    def shrunk = shrink()
    BigDecimal lonAdjusted = shrunk[0] - lonOffset
    BigDecimal latOff = baseLatitude - (baseLatitude * circumfScale)
    BigDecimal latAdjusted = shrunk[1] + latOff
    //println "lon and lat offsets are " + lonOffset + " / " + latOff
    //println "${shrunk[1]} + ${latOff} == ${latAdjusted}"
    def adjusted = [lonAdjusted,latAdjusted]
    //println "Adjusted: " + adjusted
    return (adjusted)
  }

  
   /** Rescales Ptolemy's raw coordinates for the site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @returns A longitude / latitude pair (in that order),
   * as BigDecimals.
   */
  ArrayList shrink() {
    def shrunken = [shrinkLon(), shrinkLat()]
    //println "Shrinking to " + shrunken + " because ..."
    //println "shrinkLong " + shrinkLon() + " from " + pSite.getLongitude() + " and "
    //println "shrinkLat " + shrinkLat() + " from " + pSite.getLatitude()
    return shrunken
  }


  /** Rescales Ptolemy's raw latitude coordinate for the site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @returns The rescaled latitude value as a BigDecimal.
   */
  BigDecimal shrinkLat () {
    return pSite.getLatitude() * circumfScale
  }
  
  /** Rescales Ptolemy's raw longitude coordinate for the site to dimensions
   * appopriate for Eratosthenes' value for circumference of the earth.
   * @returns The rescaled longitude value as a BigDecimal.
   */
  BigDecimal shrinkLon () {
    println "\tshrinkLon: from " + pSite.getLongitude()
    return pSite.getLongitude() * circumfScale
  }


  /** Overrides default toString() method.
   * @returns The PtolemySite, as a String.
   */
  String toString() {
    return pSite.toString()
  }
  
}


