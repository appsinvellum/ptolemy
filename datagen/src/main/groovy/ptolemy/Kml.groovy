package ptolemy


import edu.holycross.shot.ptolemy.*


class Kml {
  
  /*
     arg[0] = XML source file
     arg[1] = repository's collection directory
     arg[2] = output directory
   */
  static void main(args) {
    File xml = new File(args[0])
    File collections = new File(args[1])

    File provinces = new File(collections, "/vocab/provinces.csv")
    CsvManager csvm = new CsvManager()
    def provLabels = csvm.labelMap(provinces)

    File outputDir = new File(args[2])
    // 1. Collect ptolemaic data from XML source
    GreekGeoParser gp  = new GreekGeoParser(new File(args[0]))
    def siteList = gp.indexPtolemySites()

    // 2. Write KML for data in 3 stages of projection
    KmlManager km = new KmlManager()

    // KML for Ptolemy in 3 stages of projection:
    File kmlraw = new File(outputDir, "/raw.kml")
    kmlraw.setText(km.toKml(siteList, "Ptolemy (raw coordinate values)" ), "UTF-8")

    File kmlshrunk = new File(outputDir, "/shrunk.kml")
    kmlshrunk.setText(km.shrink(siteList, "Ptolemy (coordinates scaled down)" ), "UTF-8")

    File kmlprojected = new File(outputDir, "/projected.kml")
    kmlprojected.setText(km.project(siteList, "Ptolemy (coordinates projected to modern LL)" ), "UTF-8")


    // 3. Add csv analyses by province and by list,
    // and add KML colored by  each

    def compoundSites = Composer.ptolemyComposite(xml,collections)

    File geoLists = new File(collections, "/data/lists.csv")
    File provinceAnalyses = new File(collections, "/analyses/orca_provinces.csv")

    // KML projected and colored by Ptolemaic classifications:    
    File satrapies = new File(outputDir, "/satrapal.kml")
    satrapies.setText(km.colorBySatrapy(compoundSites, provLabels, "Ptolemy in modern coordinates (colored by province)"), "UTF-8")

    File lists = new File(outputDir, "/lists.kml")
    lists.setText(km.colorByList(compoundSites, provLabels, "Ptolemy in modern coordinates (colored by list)"), "UTF-8")

  }
}
