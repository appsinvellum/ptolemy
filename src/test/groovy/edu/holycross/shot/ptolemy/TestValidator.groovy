package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class TestValidator extends GroovyTestCase {

  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")

  
  void testCoordValidate() {
    GeoParser gp = new GeoParser(xmlSource)
    def sites = gp.indexListItems()
    assert gp.validateCoords(sites)
  }
  
}
