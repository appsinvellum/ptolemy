package edu.holycross.shot.ptolemy

// This should be a gradle subproject, not a test!

import static org.junit.Assert.*
import org.junit.Test

class Test_GenKml extends GroovyTestCase {

  String outputDir = "kml"

  File xmlSource = new File("specs/resources/texts/editions/ptolemy-geo-hc.xml")

  // Analyses of PtolemyLists are in .csv files
  File provinces = new File("collections/provinces.csv")
  File geoLists = new File("collections/geoLists.csv")
  File provinceAnalyses = new File("collections/analyses/orca_provinces.csv")

  
  void testAllKml() {
    GeoParser gp = new GeoParser(xmlSource)
    KmlManager km = new KmlManager()
    CsvManager csvm = new CsvManager()

    // PtolemySites from the XML:
    def siteList = gp.indexPtolemySites()
    
    // KML for Ptolemy in 3 stages of projection:
    File kmlraw = new File("${outputDir}/raw.kml")
    kmlraw.setText(km.toKml(siteList, "Ptolemy (raw coordinate values)" ), "UTF-8")

    File kmlshrunk = new File("${outputDir}/shrunk.kml")
    kmlshrunk.setText(km.shrink(siteList, "Ptolemy (coordinates scaled down)" ), "UTF-8")

    File kmlprojected = new File("${outputDir}/projected.kml")
    kmlprojected.setText(km.project(siteList, "Ptolemy (coordinates projected to modern LL)" ), "UTF-8")

    /// Join sites and lists for KML colored by analyzed properties:
    def ptolLists = csvm.listsFromCsv(geoLists)
    def wProvinces = csvm.addProvinces(ptolLists, provinceAnalyses)
    def compoundSites = csvm.joinSitesToLists(siteList, wProvinces)
    def provLabels = csvm.labelMap(provinces)
    
    // KML projected and colored by Ptolemaic classifications:    
    File satrapies = new File("${outputDir}/satrapal.kml")
    satrapies.setText(km.colorBySatrapy(compoundSites, provLabels, "Ptolemy in modern coordinates (colored by province)"), "UTF-8")

    File lists = new File("${outputDir}/lists.kml")
    lists.setText(km.colorByList(compoundSites, provLabels, "Ptolemy in modern coordinates (colored by list)"), "UTF-8")
  }

}
