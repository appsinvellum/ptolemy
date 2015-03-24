package edu.holycross.shot.ptolemy


import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString


/** A class implementing Ptolemy's model of geographic data.
 */
class PtolemySite {

  String urnString
  Integer textSequence
  String listUrn
  Integer listIndex
  //GreekString greekName
  String greekName
  
  boolean southLatitude  = false

  MilesianInteger latDegree
  MilesianFraction latFraction
  MilesianInteger lonDegree
  MilesianFraction lonFraction


  ArrayList getLL() {
    return [this.getLatitude(), this.getLongitude()]
  }
  

  BigDecimal getLatitude() {
    BigDecimal lat  = latDegree.toInteger() as BigDecimal
    if (latFraction != null) {
      lat += latFraction.getFractionValue()
    }
    return lat
  }



  
  BigDecimal getLongitude() {
    BigDecimal lon
    if (lonDegree == null) {
       lon = 0
    } else {
      try {
	lon = lonDegree.toInteger() as BigDecimal
      } catch (Exception e) {
	System.println "\n\nERROR: could not convert lonDegree ${lonDegree} to integer in ${textSequence}. ${this.toString()}"
      }
    }
    if (lonFraction != null) {
      lon += lonFraction.getFractionValue()
    }
    return lon
  }
  
  String toString() {
    return """Site ${urnString} = ${greekName}"""
  }
}