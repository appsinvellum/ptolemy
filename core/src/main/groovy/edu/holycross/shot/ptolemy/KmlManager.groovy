package edu.holycross.shot.ptolemy

import groovy.json.JsonBuilder
import groovy.xml.MarkupBuilder

import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString

import edu.unc.epidoc.transcoder.TransCoder

/**
 * A class for managing KML output.
 */
class KmlManager {

  /** Epidoc transcoder for transliterating labels. */
  TransCoder  xcoder

  /** Constructor.
   */
  KmlManager() {
    xcoder = new TransCoder()
    xcoder.setParser("Unicode")
    xcoder.setConverter("GreekXLit")
  }



  /** Creates a KML map of a list of PtolemyPoints projected to modern coordinates
   * using the default value for offset of Ptolemy's longitude. Labelling strings
   * identify sites by transliterated name and province, and colors sites by the
   * province or "satrapy" they belong to.
   * @param ptolemyPoints List of PtolemyPoints to map.
   * @param lonOff  Number of degrees west of Greenwich to
   * use for Ptolemy's origin of longitude.
   * @param provinceLabels Map of province URNs to labelling strings.
   * @param label Title for the resulting KML map.
   * @returns A string of valid KML.
   */
  String colorBySatrapy(ArrayList ptolemyPoints, HashMap provinceLabels, String label) {
    return colorBySatrapy(ptolemyPoints, PtolemyProjector.lonOff, provinceLabels, label)
  }
  
  /** Creates a KML map of a list of PtolemyPoints projected to modern coordinates
   * using the default value for offset of Ptolemy's longitude. Labelling strings
   * identify sites by transliterated name and province, and colors sites by the
   * province or "satrapy" they belong to.
   * @param ptolemyPoints List of PtolemyPoints to map.  UPGRADE TO MODERN POINTS
   * @param lonOff  Number of degrees west of Greenwich to
   * use for Ptolemy's origin of longitude.
   * @param provinceLabels Map of province URNs to labelling strings.
   * @param label Title for the resulting KML map.
   * @returns A string of valid KML.
   */
  String colorBySatrapy(ArrayList ptolemyPoints, BigDecimal lonOff, HashMap provinceLabels, String label) {
    BigDecimal labelScale = 0.5
    
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.mkp.xmlDeclaration (version: '1.0', encoding: 'UTF-8')
    xml.kml(xmlns: 'http://www.opengis.net/kml/2.2') {
      Document {
        name(label)
        Style(id : "color0") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("6414F0FF")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }
        Style(id : "color1") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("701400D2")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }
        Style(id : "color2") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("88FF78B4")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }
        Style(id : "color3") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("7878DC78")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }
        Style(id : "color4") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("781478FF")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }

	Integer satrapyCount = 0
	String currentSatrapy = ""
	ptolemyPoints.each { ptol  ->
	  if (ptol.ptolemyList.provinceUrn != currentSatrapy) {
	    currentSatrapy = ptol.ptolemyList.provinceUrn
	    satrapyCount++
	  }
	  Integer color = satrapyCount.mod(5)
	  Placemark {
	    styleUrl {
	      mkp.yield("#color${color}")
	    }
	    
	    def coords = PtolemyProjector.project(ptol)

	    TransCoder xcoder = new TransCoder()
	    xcoder.setParser("Unicode")
	    xcoder.setConverter("GreekXLit")
	    String xcoded = xcoder.getString(ptol.greekName)
      
	    description {
	      mkp.yield("${xcoded} (${ptol.greekName}) in ${provinceLabels[ptol.ptolemyList.provinceUrn]} (${ptol.ptolemyList.passageUrn})")
	    }
	    Point {
	      coordinates("${coords[0]},${coords[1]},0")
	    }
	  }
	}
      }
    }
    return writer.toString()
  }


  /** Creates a KML map of a list of PtolemyPoints projected to modern coordinates
   * using the default value for offset of Ptolemy's longitude. Labelling strings
   * identify sites by transliterated name and province, and colors sites by the
   * PtolemyList they belong to.
   * @param ptolemyPoints List of PtolemyPoints to map.
   * @param provinceLabels Map of province URNs to labelling strings.
   * @param label Title for the resulting KML map.
   * @returns A string of valid KML.
   */
  String colorByList(ArrayList ptolemyPoints, HashMap provinceLabels, String label) {
    return colorByList(ptolemyPoints, PtolemyProjector.lonOff, provinceLabels, label)
  }


  /** Creates a KML map of a list of PtolemyPoints projected to modern coordinates
   * using the default value for offset of Ptolemy's longitude. Labelling strings
   * identify sites by transliterated name and province, and colors sites by the
   * PtolemyList they belong to.
   * @param ptolemyPoints List of PtolemyPoints to map.
   * @param lonOff  Number of degrees west of Greenwich to
   * use for Ptolemy's origin of longitude.
   * @param provinceLabels Map of province URNs to labelling strings.
   * @param label Title for the resulting KML map.
   * @returns A string of valid KML.
   */
  String colorByList(ArrayList ptolemyPoints, BigDecimal lonOff, HashMap provinceLabels, String label) {
    BigDecimal labelScale = 0.5
    
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.mkp.xmlDeclaration (version: '1.0', encoding: 'UTF-8')
    xml.kml(xmlns: 'http://www.opengis.net/kml/2.2') {
      Document {
        name(label)
        Style(id : "color0") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("6414F0FF")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }
        Style(id : "color1") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("701400D2")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }
        Style(id : "color2") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("88FF78B4")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }
        Style(id : "color3") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("7878DC78")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }
        Style(id : "color4") {
	  IconStyle {
	    Icon {
	      href("http://maps.google.com/mapfiles/kml/pal4/icon24.png")
	    }
	    color("781478FF")
	    colorMode("normal")
	  }
	  LabelStyle {
	    scale("${labelScale}")
	  }
        }

	Integer listCount = 0
	String currentList = ""
	ptolemyPoints.each { ptol  ->
	  if (ptol.listUrn != currentList) {
	    currentList = ptol.listUrn
	    listCount++
	  }
	  Integer color = listCount.mod(5)
	  Placemark {
	    styleUrl {
	      mkp.yield("#color${color}")
	    }
	    
	    def coords = PtolemyProjector.project(ptol)

	    TransCoder xcoder = new TransCoder()
	    xcoder.setParser("Unicode")
	    xcoder.setConverter("GreekXLit")
	    String xcoded = xcoder.getString(ptol.greekName)
      
	    description {
	      mkp.yield("${xcoded} (${ptol.greekName}) in ${provinceLabels[ptol.ptolemyList.provinceUrn]} (${ptol.ptolemyList.passageUrn})")
	    }
	    Point {
	      coordinates("${coords[0]},${coords[1]},0")
	    }
	  }
	}
      }
    }
    return writer.toString()
  }


  /**
   * Composes a KML representation of a set of PtolemyPoints.
   * @param ptolemyPoints List of points to represent as KML.
   * @param label Title for the resulting KML map.
   * @returns A String of valid KML.
   */
  String toKml(ArrayList ptolemyPoints, String label) {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.mkp.xmlDeclaration (version: '1.0', encoding: 'UTF-8')
    xml.kml(xmlns: 'http://www.opengis.net/kml/2.2') {
      Document {
        name(label)
	ptolemyPoints.each { ptol  ->
	  KmlPoint pt = ptol.asKmlPoint()
	  Placemark {
	    description {
	      mkp.yield(pt.description)
	    }
	    Point {
	      coordinates("${pt.coords[0]},${pt.coords[1]},0")
	    }
	  }
	}
      }
    }
    return writer.toString()
  }


  
  /**
   * Creates a KML representation of a list of PtolemySite objects with 
   * coordinates rescaled to dimensions appopriate for Eratosthenes' value 
   * for the circumference of the earth.
   * @param siteList A list of PtolemySite objects.
   * @returns A valid KML string.
   */
  String shrink(ArrayList siteList, String label) {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.mkp.xmlDeclaration (version: '1.0', encoding: 'UTF-8')
    xml.kml(xmlns: 'http://www.opengis.net/kml/2.2') {
      Document {
        name(label)
	siteList.each { ptol  ->
	  def coords = PtolemyProjector.shrink(ptol)

	  TransCoder xcoder = new TransCoder()
	  xcoder.setParser("Unicode")
	  xcoder.setConverter("GreekXLit")
	  String xcoded = xcoder.getString(ptol.greekName)
      

	  Placemark {
	    description {
	      mkp.yield("${xcoded} (ptol.urnString)")
	    }
	    Point {
	      coordinates("${coords[0]},${coords[1]},0")
	    }
	  }
	}
      }
    }
    return writer.toString()

  }


  /** Creates a KML representation of a list of PtolemySite  objects
   * using the default value for the offset of Ptolemy's
   * origin of longitude.
   * @param siteList The list of sites to project.
   * @param label Title for the resulting KML map.
   * @returns A valid KML String.
   */  
  String project(ArrayList siteList, String label) {
    return project(siteList, PtolemyProjector.lonOff, label)
  }



  /** Creates a KML representation of a list of PtolemySite  objects
   * using a given value for the offset of Ptolemy's
   * origin of longitude.
   * @param siteList The list of sites to project.
   * @param lonOff  Number of degrees west of Greenwich to
   * use for Ptolemy's origin of longitude.
   * @param siteList The list of sites to project.
   * @param label Title for the resulting KML map.
   * @returns A valid KML String.
   */
  String project(ArrayList siteList, BigDecimal lonOff, String label) {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.mkp.xmlDeclaration (version: '1.0', encoding: 'UTF-8')
    xml.kml(xmlns: 'http://www.opengis.net/kml/2.2') {
      Document {
        name(label)
	siteList.each { ptol  ->
	  def coords = PtolemyProjector.project(ptol)

	  TransCoder xcoder = new TransCoder()
	  xcoder.setParser("Unicode")
	  xcoder.setConverter("GreekXLit")
	  String xcoded = xcoder.getString(ptol.greekName)
      

	  Placemark {
	    description {
	      mkp.yield("${xcoded} (ptol.urnString)")
	    }
	    Point {
	      coordinates("${coords[0]},${coords[1]},0")
	    }
	  }
	}
      }
    }
    return writer.toString()

  }


  
}


