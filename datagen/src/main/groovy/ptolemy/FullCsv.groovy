package ptolemy

import edu.holycross.shot.ptolemy.*

class FullCsv {


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
    def compoundSites = csvm.joinSitesToLists(siteList, wEthnics)
    def modernList = ModernSite.ptolemyToModernSites(compoundSites)


    // 3. Write out .csv representation
    File lists = new File(outputDir, "ptolemy.csv")
    lists.setText(csvm.compoundSitesToCsv(modernList), "UTF-8")
  }
}
