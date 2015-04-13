package edu.holycross.shot.ptolemy


import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString


/** A class implementing one of Ptolemy's two fundamental data 
 * structures, the site.  The site stands in a many-to-one relation
 * to the other fundamental structure, the List (implemented as
 * a PtolemyList).
 */
class PtolemySite {


  /** URN for the site, as a String. */
  String urnString

  /** Sequence within the entire set of analyzed sites.*/
  Integer textSequence

  /** URN, as a String, identifying the PtolemyList this
   * site belongs to. */
  String listUrn

  /** Sequence within the owning list. */
  Integer listIndex

  
  //GreekString greekName
  /** Name of the site in Ptolemy's text. */
  String greekName

  /** True if latitude value is south of the equator. */
  boolean southLatitude  = false

  /** Integer degrees latitude. */
  MilesianInteger latDegree

  /** Additional fraction of a degree, if any, of latitude. */
  MilesianFraction latFraction

  /** Integer degrees longitude. */
  MilesianInteger lonDegree

  /** Additional fraction of a degree, if any, of longitude. */
  MilesianFraction lonFraction


  /** Containing list as a PtolemyList object. 
   * If this value is assigned, the effect is of joining
   * the site and list properties in a single object.
   */
  PtolemyList ptolemyList


  /**
   * Gets longitude / latitude coordinate pair.
   * @returns Longitude / latidue pair (in that order) as
   * BigDecimals.
   */
  ArrayList getLL() {
    return [this.getLongitude(), this.getLatitude()]
  }


  /** Converts the PtolemySite to a KmlPoint.
   * @returns A KmlPoint object.
   */
  KmlPoint asKmlPoint() {
    return( new KmlPoint([description : this.toString(), coords : this.getLL()]))
  }



  /** Gets the decimal value of Ptolemy's record
   * for integer and fractional latitude value.
   * @returns A BigDecimal rounded to .001.
   */
  BigDecimal getLatitude() {

    BigDecimal lat
    if (latDegree == null)  {
      lat = 0
    } else {
      lat = latDegree.toInteger() as BigDecimal
    }
    if (latFraction != null) {
      lat += latFraction.getFractionValue()
    }
    //ok:
    //System.err.println "PtolemySite: looking at lat, with latfract " + latFraction + " == " + latFraction.getFractionValue() + ", so returning " + lat
    return lat
  }



  /** Gets the decimal value of Ptolemy's record
   * for integer and fractional longitude value.
   * @returns A BigDecimal rounded to .001.
   */
  BigDecimal getLongitude() {
    BigDecimal lon
    try {
      lon = lonDegree.toInteger() as BigDecimal
    } catch (Exception e) {
      System.println "\n\nERROR: could not convert lonDegree ${lonDegree} to integer in ${textSequence}. ${this.toString()}"
    }
    if (lonFraction != null) {
      lon += lonFraction.getFractionValue()
    }
    return lon
  }


  /** Overrides default method.*/
  String toString() {
    return """Site ${urnString} = ${greekName}"""
  }

  String showSettings() {
    return """
urnString ${urnString}
textSequence ${textSequence}
listUrn ${listUrn}
listIndex ${listIndex}
greekName ${greekName}
southLatitude ${southLatitude}
latDegree ${latDegree}
latFraction ${latFraction}
lonDegree ${lonDegree}
lonFraction ${lonFraction}
ptolemyList ${ptolemyList}
"""
  }
  
}