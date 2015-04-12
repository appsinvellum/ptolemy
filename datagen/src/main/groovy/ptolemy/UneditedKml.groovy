package ptolemy

import edu.holycross.shot.ptolemy.*

class UneditedKml {





  /*
     arg[0] = XML source file
     arg[1] = repository's collection directory
     arg[2] = output directory



   */
  static void main(args) {
    File xml = new File(args[0])
    File collections = new File(args[1])
    File outputDir = new File(args[2])


    // Three steps:
    // 1. Collect ptolemaic data from XML source
    GreekGeoParser gp  = new GreekGeoParser(new File(args[0]))
    def siteList = gp.indexPtolemySites()


    // 2a. Assemble analyses of PtolemyLists from the
    // following .csv files...
    File geoLists = new File(collections, "/data/lists.csv")
    File provinces = new File(collections, "/vocab/provinces.csv")
    File provinceAnalyses = new File(collections, "/analyses/orca_provinces.csv")
    File geoTypeAnalyses = new File(collections, "/analyses/orca_geoTypes.csv")
    File ethnicAnalyses = new File(collections, "/analyses/orca_ethnics.csv")

    CsvManager csvm = new CsvManager()
    def ptolLists = csvm.listsFromCsv(geoLists)
    def wProvinces = csvm.addProvinces(ptolLists, provinceAnalyses)
    def wGeoTypes = csvm.addGeoTypes(wProvinces,geoTypeAnalyses)
    def wEthnics = csvm.addEthnics(wProvinces,ethnicAnalyses)

    // 2b. and select unedited entries
    def noAnalysis = wEthnics.findAll {it.ethnicUrn == null }
    System.err.println "Number of lists wout ethnic analysis: " + noAnalysis.size()
    def compoundSites = csvm.joinSitesToLists(siteList, wEthnics)

    

    // 3. Write KML representation of missing sites
    KmlManager km = new KmlManager()
    def provLabels = csvm.labelMap(provinces)
    provLabels.keySet().each { provUrn ->
      String label = provLabels[provUrn]
      System.err.println "Get sites for " + label + " (${provUrn})"
      def provincial = compoundSites.findAll { it.ptolemyList.provinceUrn == provUrn && (it.ptolemyList.ethnicUrn == null || it.ptolemyList.geoTypeUrn == null) }
      if (provincial.size() > 0) {
	File lists = new File(outputDir, "${label.replaceAll(/ /,'_')}.kml")
	lists.setText(km.colorByList(provincial, provLabels, "${label}: sites needing analysis"), "UTF-8")
      }
    }
    
  }


  /*
  void testUneditedKml() {

    
    /// Join sites and lists for KML colored by analyzed properties:

    csvm.debug = 0
    def compoundSites = csvm.joinSitesToLists(siteList, wEthnics)

    println "Compound sites : " + compoundSites.size()
    def missing =  compoundSites.findAll{it.ptolemyList == null}
    println "Sites without list: " + missing.size()
    missing.each {
      println it
    }
    
    //def unedited = compoundSites.findAll {it.ptolemyList.ethnicUrn == null}



  }
  */
  }
