
package edu.holycross.shot.ptolemy

/** A class modelling GeoJSON's geographic data model.
 */

class GeoJsonSite {
  def properties
  def geometry
  def type

  String toString() {
    return """${properties.siteName} with coords ${geometry.coordinates}"""
  }
}