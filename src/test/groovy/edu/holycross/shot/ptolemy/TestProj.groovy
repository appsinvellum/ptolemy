package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class TestProj extends GroovyTestCase {

  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")

  /*
  void testShrink() {
    GeoParser gp = new GeoParser(xmlSource)
    def siteList = gp.indexPtolemySites()
    PtolemySite site2 = siteList[1]
    PtolemyProjector proj = new PtolemyProjector(site2)
    
    println "CF " + site2.getLL() + " and " + proj.shrink()

    CsvManager csvm = new CsvManager()


    File shr = new File("shrunk.geojson")
    shr.setText(csvm.shrinkPtolemy(siteList), "UTF-8")
    
  }
*/


    void testProj() {
      GeoParser gp = new GeoParser(xmlSource)
      def siteList = gp.indexPtolemySites()

      CsvManager csvm = new CsvManager()


      String delphiUrn = "urn:cite:ptolemy:lonlat.pt_ll_2194"
      PtolemySite delphi = siteList.find {it.urnString == delphiUrn }
      assert delphi.getLL() == [50, 37.667]
      PtolemyProjector projDelphi = new PtolemyProjector(delphi)
      assert projDelphi.project() ==   [22.00, 37.20024]



      String pergeUrn = "urn:cite:ptolemy:lonlat.pt_ll_3394"
      PtolemySite perge = siteList.find {it.urnString == pergeUrn}
      assert perge.getLL() == [62.25, 37.916]
      PtolemyProjector projPerge = new PtolemyProjector(perge)
      assert projPerge.project() == [30.8200, 37.37952]

    
      /*
      GeoJsonManager gjsm = new GeoJsonManager()
      String json = gjsm.projectGeoJson(siteList)
      // parse and test this string

      
      File shr = new File("projected.geojson")
      shr.setText(json, "UTF-8")
      */
    }

}
