
package edu.holycross.shot.ptolemy

/** A class modelling GeoJSON's geographic data model.
 */

class GeoJsonSite {
  def properties
  def geometry
  def type


  /** Overrides default toString() method
   * @returns Human-readable string with name and coordinates of site.
   */
  String toString() {
    return """${properties.siteName} with coords ${geometry.coordinates}"""
  }
}