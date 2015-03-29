
package edu.holycross.shot.ptolemy

/**  A class modelling the data used by the KmlManager to
 * represent a KML Point object.
 */
class KmlPoint {


  /** Labelling string for the point */
  String description
  
  /** List of pairs of lon / lat values */
  ArrayList coords

  // tba
  String styleClass
  

  /** Overrides default toString() method
   * @returns Human-readable string with name and coordinates of site.
   */
  String toString() {
    return description
  }
}