package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class Test_GenGeoJson extends GroovyTestCase {

  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")

  String outputDir = "geojson"

  void testGeoJsonSites() {
    GeoParser gp = new GeoParser(xmlSource)
    def siteList = gp.indexPtolemySites()

    GeoJsonManager gjsm = new GeoJsonManager()

    String rawjson = gjsm.toGeoJson(siteList)
    File raw = new File("${outputDir}/raw.geojson")
    raw.setText(rawjson, "UTF-8")



    String shrunkjson = gjsm.shrink(siteList)
    File shrunk = new File("${outputDir}/shrunk.geojson")
    shrunk.setText(shrunkjson, "UTF-8")

    String projectedjson = gjsm.project(siteList)
    File projected = new File("${outputDir}/projected.geojson")
    projected.setText(projectedjson, "UTF-8")

    
  }

}
