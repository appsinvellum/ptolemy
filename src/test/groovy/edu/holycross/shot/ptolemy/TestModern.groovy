package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class TestModern extends GroovyTestCase {


  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")
  File csvSource = new File("collections/geoLists.csv")
  File provinceCsv = new File("collections/orca_provinces.csv")
  
  String delphiUrn = "urn:cite:ptolemy:lonlat.pt_ll_2194"
      
  void testModernSite() {
    CsvManager csvm = new CsvManager()
    def ptLists = csvm.listsFromCsv(csvSource)
    assert ptLists.size() == 1264
    def wProvinces = csvm.addProvinces(ptLists, provinceCsv)
    assert wProvinces.size() == 1264

    GeoParser gp = new GeoParser(xmlSource)
    def siteList = gp.indexPtolemySites()
    def compoundSites = csvm.joinSitesToLists(siteList, wProvinces)

    PtolemySite delphi = compoundSites.find {it.urnString == delphiUrn }
    ModernSite modernDelphi = new ModernSite(delphi)
    assert modernDelphi.rawLon == 50
    assert modernDelphi.projectedCoords == [22.0, 37.20]

  }



}
