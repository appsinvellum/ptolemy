package ptolemy

import edu.holycross.shot.ptolemy.*

class Composer {

  
  static ArrayList modernComposite(File xml, File collections) {
    // Collect ptolemaic data from XML source
    GreekGeoParser gp  = new GreekGeoParser(xml)
    def siteList = gp.indexPtolemySites()

    // Assemble analyses of PtolemyLists from the
    // following .csv files...
    File geoLists = new File(collections, "/data/lists.csv")
    File provinces = new File(collections, "/vocab/provinces.csv")
    File provinceAnalyses = new File(collections, "/analyses/orca_provinces.csv")
    File geoTypeAnalyses = new File(collections, "/analyses/orca_geoTypes.csv")
    File ethnicAnalyses = new File(collections, "/analyses/orca_ethnics.csv")

    // Use CsvManager to join analyses to ptolemaic data
    CsvManager csvm = new CsvManager()
    def ptolLists = csvm.listsFromCsv(geoLists)
    def wProvinces = csvm.addProvinces(ptolLists, provinceAnalyses)
    def wGeoTypes = csvm.addGeoTypes(wProvinces,geoTypeAnalyses)
    def wEthnics = csvm.addEthnics(wProvinces,ethnicAnalyses)
    def compoundSites = csvm.joinSitesToLists(siteList, wEthnics)

    // Add modern representation
    def modernList = ModernSite.ptolemyToModernSites(compoundSites)

    // done:
    return(modernList)
  }

  
}
