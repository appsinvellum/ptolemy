package edu.holycross.shot.ptolemy

import groovy.json.JsonBuilder
import groovy.xml.MarkupBuilder

import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString

import edu.unc.epidoc.transcoder.TransCoder

/**
 * A class for managing GeoJson output.
 */
class GeoJsonManager {

  /** Epidoc transcoder for transliterating labels in KML and GeoJson output. */
  TransCoder  xcoder

  /** Constructor.
   */
  GeoJsonManager() {
    xcoder = new TransCoder()
    xcoder.setParser("Unicode")
    xcoder.setConverter("GreekXLit")
  }


  /**
   * Creates a GeoJson string for an array of GeoJsonSite objects.
   * @param featureList List of GeoJsonSite objects.
   * @returns A JSON string.
   */
  String featuresToGeoJson(ArrayList featureList) {
    JsonBuilder bldr = new groovy.json.JsonBuilder()
    bldr(type : 'FeatureCollection', features : featureList)
    return bldr.toPrettyString()
  }

  
  /** Creates a GeoJson representation of a list of PtolemySite 
   * using the default value for the offset of Ptolemy's
   * origin of longitude.
   * @param siteList The list of sites to project.
   * @returns A geojson String.
   */
  String project(ArrayList siteList) {
    return project(siteList, PtolemyProjector.lonOff)
  }

  /** Projects coordinates for a list of GeoJsonSite objects
   * using a given value for the offset of Ptolemy's
   * origin of longitude.
   * @param siteList The list of sites to project.
   * @param lonOffset  Number of degrees west of Greenwich to
   * use for Ptolemy's origin of longitude.
   * @returns A geojson String.
   */
  String project(ArrayList siteList, BigDecimal lonOffset) {
    def featureList = []
    siteList.each { pSite ->
      GeoJsonSite gjSite = new GeoJsonSite(geometry: [:], properties: [:], type: 'Feature')
      gjSite.properties = ['urn': pSite.urnString, 'greek': pSite.greekName, 'site': xcoder.getString(pSite.greekName)]
      gjSite.geometry = ['coordinates': PtolemyProjector.project(pSite, lonOffset), 'type': 'Point']
      featureList.add(gjSite)
    }
    return (featuresToGeoJson(featureList))
  }

  /**
   * Creates a GeoJson representation of a list of GeoJsonSite objects with 
   * coordinates rescaled to dimensions appopriate for Eratosthenes' value 
   * for the circumference of the earth.
   * @param siteList A list of PtolemySite objects.
   * @returns A list of GeoJsonSite objects.
   */
  String shrink(ArrayList siteList) {
    def featureList = []

    siteList.each { pSite ->
      GeoJsonSite gjSite = new GeoJsonSite(geometry: [:], properties: [:], type: 'Feature')
      TransCoder xcoder = new TransCoder()
      xcoder.setParser("Unicode")
      xcoder.setConverter("GreekXLit")

      
      gjSite.properties = ['urn': pSite.urnString, 'greek': pSite.greekName, 'site': xcoder.getString(pSite.greekName)]

      
      gjSite.geometry = ['coordinates': PtolemyProjector.shrink(pSite), 'type': 'Point']
      featureList.add(gjSite)
    }
    return (featuresToGeoJson(featureList))
  }

  /** Formats a list of PtolemySite objects as geojson.
   */
  String toGeoJson(ArrayList siteList) {
    def featureList = []
    siteList.each { pSite ->
      GeoJsonSite gjSite = new GeoJsonSite(geometry: [:], properties: [:], type: 'Feature')

      TransCoder xcoder = new TransCoder()
      xcoder.setParser("Unicode")
      xcoder.setConverter("GreekXLit")

      
      gjSite.properties = ['urn': pSite.urnString, 'greek': pSite.greekName, 'site': xcoder.getString(pSite.greekName)]
      gjSite.geometry = ['coordinates': pSite.getLL(), 'type': 'Point']
      featureList.add(gjSite)
    }

    return (featuresToGeoJson(featureList))
  }
  
}


