package ptolemy

import edu.holycross.shot.ptolemy.*

class GeoJson  {



    /*
     arg[0] = XML source file
     arg[1] = repository's collection directory
     arg[2] = output directory
   */
  static void main(args) {
    File xml = new File(args[0])
    File collections = new File(args[1])
    File outputDir = new File(args[2])

    // 1. Collect ptolemaic data from XML source
    GreekGeoParser gp  = new GreekGeoParser(new File(args[0]))
    def siteList = gp.indexPtolemySites()


    // 2. Write GeoJson representations of different projections
    GeoJsonManager gjsm = new GeoJsonManager()

    String rawjson = gjsm.toGeoJson(siteList)
    File raw = new File(outputDir, "/raw.geojson")
    raw.setText(rawjson, "UTF-8")



    String shrunkjson = gjsm.shrink(siteList)
    File shrunk = new File(outputDir, "/shrunk.geojson")
    shrunk.setText(shrunkjson, "UTF-8")

    String projectedjson = gjsm.project(siteList)
    File projected = new File(outputDir, "/projected.geojson")
    projected.setText(projectedjson, "UTF-8")
    
  }

}
