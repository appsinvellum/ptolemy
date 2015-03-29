
package edu.holycross.shot.ptolemy

/** A class implementing GeoJSON's Point data model in a structure
 * that can be directly passed to a groovy JsonBuilder.
 */

class GeoJsonSite {

  /** Map of property names to values. */
  def properties

  /** Lon / lon coordinate pair for a GeoJson Point object.*/
  def geometry

  /** GeoJson type, for Ptolemaic points, always "Feature". */
  def type


  /** Overrides default toString() method
   * @returns Human-readable string with name and coordinates of site.
   */
  String toString() {
    return """${properties.siteName} with coords ${geometry.coordinates}"""
  }
}