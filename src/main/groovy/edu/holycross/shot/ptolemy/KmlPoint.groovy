
package edu.holycross.shot.ptolemy

/** 
 */

class KmlPoint {
  String description
  // lon, lat
  ArrayList coords
  String styleClass
  

  /** Overrides default toString() method
   * @returns Human-readable string with name and coordinates of site.
   */
  String toString() {
    return description
  }
}