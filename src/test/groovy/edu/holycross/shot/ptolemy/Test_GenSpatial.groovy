package edu.holycross.shot.ptolemy

import static org.junit.Assert.*
import org.junit.Test

class Test_GenSpatial extends GroovyTestCase {


  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")

  // Analyses of PtolemyLists are in .csv files
  File provinces = new File("collections/provinces.csv")
  File geoLists = new File("collections/geoLists.csv")
  File provinceAnalyses = new File("collections/analyses/orca_provinces.csv")


  
  void testSpatialAnalysis() {
    GeoParser gp = new GeoParser(xmlSource)
    CsvManager csvm = new CsvManager()
    // Basic PtolemySites from the XML:
    def siteList = gp.indexPtolemySites()

    /// Join sites and lists, and create ModernSites 
    def ptolLists = csvm.listsFromCsv(geoLists)
    def wProvinces = csvm.addProvinces(ptolLists, provinceAnalyses)
    def compoundSites = csvm.joinSitesToLists(siteList, wProvinces)
    def modernList = ModernSite.ptolemyToModernSites(compoundSites)
    
    def provLabels = csvm.labelMap(provinces)

    String csv = "Province,Label,0,1,2,3,4,5,6,7,8,9,10,11\n"
    provLabels.keySet().each { urn ->
      String label = provLabels[urn]
      def sites = modernList.findAll {it.ptolemySite.ptolemyList.provinceUrn == urn}
      if (sites.size() > 0) {
	csv +=  "${urn},'" + label + "'"
	SpatialAnalyst sa = new SpatialAnalyst(sites)
	def histogram = sa.lonTwelfths()
	histogram.keySet().sort().each { twelfth ->
	  csv += ",${histogram[twelfth]}"
	}
	csv += "\n"
      }
    }
    File histoFile = new File("provincesPrecision.csv")
    histoFile.setText( csv, "UTF-8")
  }
  
}