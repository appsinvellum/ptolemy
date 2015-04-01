package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class Test_GenModernSites extends GroovyTestCase {

  String outputDir = "r/csv"

  // XML source for PtolemySites
  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")

  // Analyses of PtolemyLists as .csv 
  File csvSource = new File("collections/data/lists.csv")
  File provinces = new File("collections/vocab/provinces.csv")
  File provinceCsv = new File("collections/analyses/orca_provinces.csv")
  
  
  void testModernSite() {
    if (! outputDir.exists()) {
      outputDir.mkdir()
    }
    GeoParser gp = new GeoParser(xmlSource)
    def siteList = gp.indexPtolemySites()

    CsvManager csvm = new CsvManager()
    def ptLists = csvm.listsFromCsv(csvSource)
    def wProvinces = csvm.addProvinces(ptLists, provinceCsv)
    def compoundSites = csvm.joinSitesToLists(siteList, wProvinces)
    def modernList = ModernSite.ptolemyToModernSites(compoundSites)
    File modernFile = new File("${outputDir}/modern.csv")
    modernFile.setText(csvm.modernSitesToCsv(modernList), "UTF-8")
		       
    // convert provinces to csv
    provinces.eachLine { l ->
      def cols = l.split(/,/)
      String urn  = cols[0]
      String label = cols[1].replaceAll(" ",".")
      def sites = modernList.findAll {it.ptolemySite.ptolemyList.provinceUrn == urn}

      if (sites.size() > 0) {
	String csv  = csvm.modernSitesToCsv(sites)
	File csvFile = new File("${outputDir}/${label}.csv")
	csvFile.setText(csv, "UTF-8")
      } else {
	System.err.println "0 sites found for ${urn} (${label})"
      }
    }
  }

}
