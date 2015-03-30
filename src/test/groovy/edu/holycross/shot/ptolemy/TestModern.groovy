package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class TestModern extends GroovyTestCase {


  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")
  File csvSource = new File("collections/geoLists.csv")
  File provinceCsv = new File("collections/analyses/orca_provinces.csv")
  
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

    // work with individual sites:
    PtolemySite delphi = compoundSites.find {it.urnString == delphiUrn }
    ModernSite modernDelphi = new ModernSite(delphi)
    assert modernDelphi.rawLon == 50
    assert modernDelphi.projectedCoords == [22.0, 37.20]

    def modernList = ModernSite.ptolemyToModernSites(compoundSites)
    assert modernList.size() == compoundSites.size()
    ModernSite anotherDelphi = modernList.find {it.ptolemySite.urnString == delphiUrn}
    assert anotherDelphi.projectedCoords == [22.0, 37.20]


    
    // convert provinces to csv
    File provinces = new File("collections/provinces.csv")
    provinces.eachLine { l ->
      def cols = l.split(/,/)
      String urn  = cols[0]
      String label = cols[1].replaceAll(" ",".")
      def sites = modernList.findAll {it.ptolemySite.ptolemyList.provinceUrn == urn}
      String csv  = csvm.modernSitesToCsv(sites)
      File csvFile = new File("r/csv/${label}.csv")
      csvFile.setText(csv, "UTF-8")
    }
    
    /*
    String italyUrn = "urn:cite:ptolemy:province.17"
    def italySites = modernList.findAll {it.ptolemySite.ptolemyList.provinceUrn == italyUrn}
    String csv  = csvm.modernSitesToCsv(italySites)
    File csvFile = new File("italy.csv")
    csvFile.setText(csv, "UTF-8")


    String sicilyUrn = "urn:cite:ptolemy:province.21"
    def sicilySites = modernList.findAll {it.ptolemySite.ptolemyList.provinceUrn == sicilyUrn}
    String csv2  = csvm.modernSitesToCsv(sicilySites)
    File csv2File = new File("sicily.csv")
    csv2File.setText(csv2, "UTF-8")

    */
  }

}
