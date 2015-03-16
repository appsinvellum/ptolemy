package edu.holycross.shot.ptolemy;

/**
 * A class for parsing the geographic database in Ptolemy's Geography.  
 * Works from a TEI source file following a specified set
 * of markup conventions.
 */
class GeoParser {

  /** TEI namespace as a groovy Namespace object. */
  static groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")



  /** Root of parsed document. */
  groovy.util.Node root


  /** Constructor from a File object.
   * @param xmlFile TEI source for edition.
   */
  GeoParser(File xmlFile) {
    root = new XmlParser().parse(xmlFile)
  }

  /** Forms URN for ptolemy list collection
   * with strings formatted for convenient sorting.
   * @param i Unique identifier for list.
   */
  static String formListUrn(Integer i) {
    String baseUrn = "urn:cite:ptolemy:list"
    if (i > 999) {
      return "${baseUrn}.list$i"
    } else if (i > 99) {
      return "${baseUrn}.list0$i"
    } else if (i > 9) {
      return "${baseUrn}.list00$i"
    } else if (i > 0) {
      return "${baseUrn}.list000$i"
    }
  }

  
  
}