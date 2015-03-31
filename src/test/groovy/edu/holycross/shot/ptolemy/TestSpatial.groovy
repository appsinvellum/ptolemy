package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class TestSpatial extends GroovyTestCase {


  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")
  File csvSource = new File("collections/geoLists.csv")
  File provinceCsv = new File("collections/analyses/orca_provinces.csv")
  
  String delphiUrn = "urn:cite:ptolemy:lonlat.pt_ll_2194"

  
  void testSpatialAnalysis() {
    CsvManager csvm = new CsvManager()
    def ptLists = csvm.listsFromCsv(csvSource)
    assert ptLists.size() == 1264
    def wProvinces = csvm.addProvinces(ptLists, provinceCsv)
    assert wProvinces.size() == 1264

    GeoParser gp = new GeoParser(xmlSource)
    def siteList = gp.indexPtolemySites()
    def compoundSites = csvm.joinSitesToLists(siteList, wProvinces)
    def modernList = ModernSite.ptolemyToModernSites(compoundSites)
    assert modernList.size() == compoundSites.size()

    

    String italyUrn = "urn:cite:ptolemy:province.17"
    def italy = modernList.findAll {it.ptolemySite.ptolemyList.provinceUrn == italyUrn}
    
    SpatialAnalyst sa = new SpatialAnalyst(italy)
    def histogram = sa.lonTwelfths()
    assert histogram.keySet().size() == 12
    assert histogram["00"] == 78
  }

}
