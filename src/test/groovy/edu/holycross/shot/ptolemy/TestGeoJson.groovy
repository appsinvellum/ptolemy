package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class TestGeoJson extends GroovyTestCase {

  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")


  void testGeoJsonSites() {
    GeoParser gp = new GeoParser(xmlSource)
    def siteList = gp.indexPtolemySites()

    GeoJsonManager gjsm = new GeoJsonManager()
    String json = gjsm.toGeoJson(siteList)
    // parse and test this string value...


    // temp kludge: inspect visually
    File raw = new File("rawSite.geojson")
    raw.setText(json, "UTF-8")
  }

}
