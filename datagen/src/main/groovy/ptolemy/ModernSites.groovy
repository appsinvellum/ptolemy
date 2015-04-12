package ptolemy

import edu.holycross.shot.ptolemy.*


class ModernSites  {


  /*
     arg[0] = XML source file
     arg[1] = repository's collection directory
     arg[2] = output directory
  */
  static void main(args) {
    File xml = new File(args[0])
    File collections = new File(args[1])
    File outputDir = new File(args[2])
    if (! outputDir.exists()) {
      outputDir.mkdir()
    }

    // 1. Collect ptolemaic data from XML source
    GreekGeoParser gp  = new GreekGeoParser(new File(args[0]))
    def siteList = gp.indexPtolemySites()
    

    // 2. Assemble analyses of PtolemyLists from the
    // following .csv files...
    File csvSource = new File(collections, "/data/lists.csv")
    File provinces = new File(collections, "/vocab/provinces.csv")
    File provinceCsv = new File(collections, "/analyses/orca_provinces.csv")
  
    CsvManager csvm = new CsvManager()
    def ptLists = csvm.listsFromCsv(csvSource)
    def wProvinces = csvm.addProvinces(ptLists, provinceCsv)
    def compoundSites = csvm.joinSitesToLists(siteList, wProvinces)
    def modernList = ModernSite.ptolemyToModernSites(compoundSites)

    // 3. Write csv files, first composite of all sites
    File modernFile = new File("${outputDir}/modern.csv") 
    modernFile.setText(csvm.modernSitesToCsv(modernList), "UTF-8")
    Integer count = 0
    // then, province by province
    provinces.eachLine { l ->
      if (count > 0) {
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
	count++;
      }
    }
  }
    

}
