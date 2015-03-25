package edu.holycross.shot.ptolemy



/** A class implementing Ptolemy's model of geographic data lists.
 * Lists are an analysis specific to a particular version of the text (edition or
 * translation).
 */
class PtolemyList {

  /** The list */
  String listUrn
  /** Sequence in this version of the Geography. */
  Integer textSequence
  /** Version-level URN where the list is found. */
  String passageUrn


  // Urns for properties associated with list
  String continentUrn
  String provinceUrn
  String geoTypeUrn
  String ethnicUrn
  String physicalRegionUrn
  
   
  String toString() {
    return """List ${listUrn} = ${passageUrn}"""
  }
}