package edu.holycross.shot.ptolemy

// This should be a gradle subproject, not a test!

import static org.junit.Assert.*
import org.junit.Test

class Test_GenUneditedKml extends GroovyTestCase {

  File outputDir = new File("editing")

  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")

  // Analyses of PtolemyLists are in .csv files
  File geoLists = new File("collections/data/lists.csv")
  File provinces = new File("collections/vocab/provinces.csv")
  File provinceAnalyses = new File("collections/analyses/orca_provinces.csv")
  File geoTypeAnalyses = new File("collections/analyses/orca_geoTypes.csv")
  File ethnicAnalyses = new File("collections/analyses/orca_ethnics.csv")
  
  void testUneditedKml() {
    GeoParser gp = new GeoParser(xmlSource)
    KmlManager km = new KmlManager()
    CsvManager csvm = new CsvManager()

    // PtolemySites from the XML:
    def siteList = gp.indexPtolemySites()
    
    /// Join sites and lists for KML colored by analyzed properties:
    def ptolLists = csvm.listsFromCsv(geoLists)
    def wProvinces = csvm.addProvinces(ptolLists, provinceAnalyses)
    def wGeoTypes = csvm.addGeoTypes(wProvinces,geoTypeAnalyses)
    def wEthnics = csvm.addEthnics(wProvinces,ethnicAnalyses)


    def noAnalysis = wEthnics.findAll {it.ethnicUrn == null }
    System.err.println "Number of lists wout ethnic analysis: " + noAnalysis.size()

    
    def compoundSites = csvm.joinSitesToLists(siteList, wEthnics)
    //def unedited = compoundSites.findAll {it.ptolemyList.ethnicUrn == null}


    def provLabels = csvm.labelMap(provinces)
    provLabels.keySet().each { provUrn ->
      String label = provLabels[provUrn]
    
      def provincial = compoundSites.findAll {it.ptolemyList.ethnicUrn == null && it.ptolemyList.provinceUrn == provUrn}
      File lists = new File(outputDir, "${label.replaceAll(/ /,'_')}.kml")
      lists.setText(km.colorByList(provincial, provLabels, "${label}: sites needing analysis"), "UTF-8")
    }

  }

}
