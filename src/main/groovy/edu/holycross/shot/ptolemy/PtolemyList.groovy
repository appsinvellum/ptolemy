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


  // Urns for properties associated with each list
  /** Continent ~ book */
  String continentUrn

  /** Province or "satrapy" ~ chapter */
  String provinceUrn


  // Properties cutting across citation hierarchy
  
  /** One of Ptolemy's twelve geographic types. */
  String geoTypeUrn

  /** Ethnic group. */
  String ethnicUrn

  /** Physical region. */
  String physicalRegionUrn
  

  /** Overrides toString() method. */
  String toString() {
    return """List ${listUrn} = ${passageUrn}"""
  }
}