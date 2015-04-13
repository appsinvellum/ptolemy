package ptolemy

import edu.holycross.shot.ptolemy.*

class Composer {


  /** Creates composite object joining analyses with structures
   * extracted directly from a text of Ptolemy.
   * @param xml An XML edition of the Geography.
   * @param collections Directory with analyses in .csv files.
   * @returns A list of PtolemySite objects with analytical properties
   * incorporated.
   */
  static ArrayList ptolemyComposite(File xml, File collections) {
    //
    // 1. Parse XML source
    // 
    // Collect ptolemaic data from XML source following project
    // conventions:
    GreekGeoParser gp  = new GreekGeoParser(xml)
    def siteList = gp.indexPtolemySites()
    

    // 2. Assemble analyses of PtolemyLists from .csv files.
    //
    // Files should be organized following project conventions...
    File geoLists = new File(collections, "/data/lists.csv")
    File provinces = new File(collections, "/vocab/provinces.csv")
    File provinceAnalyses = new File(collections, "/analyses/orca_provinces.csv")
    File geoTypeAnalyses = new File(collections, "/analyses/orca_geoTypes.csv")
    File ethnicAnalyses = new File(collections, "/analyses/orca_ethnics.csv")

    // then use CsvManager to join analyses to ptolemaic data
    CsvManager csvm = new CsvManager()
    def ptolLists = csvm.listsFromCsv(geoLists)
    def wProvinces = csvm.addProvinces(ptolLists, provinceAnalyses)
    def wGeoTypes = csvm.addGeoTypes(wProvinces,geoTypeAnalyses)
    def wEthnics = csvm.addEthnics(wProvinces,ethnicAnalyses)
    def compoundSites = csvm.joinSitesToLists(siteList, wEthnics)


    // 3. Done.
    return(compoundSites)
  }





  /** Creates composite object joining analyses and modern coordinate
   * projects with structures extracted directly from a text of Ptolemy.
   * @param xml An XML edition of the Geography.
   * @param collections Directory with analyses in .csv files.
   * @returns A list of PtolemySite objects with analytical properties
   * incorporated.
   */
  static ArrayList modernComposite(File xml, File collections) {
    def compoundSites = Composer.ptolemyComposite(xml,collections)
    return (ModernSite.ptolemyToModernSites(compoundSites))
  }

  
}
