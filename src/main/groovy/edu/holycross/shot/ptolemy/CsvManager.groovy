package edu.holycross.shot.ptolemy

import groovy.json.JsonBuilder

import edu.holycross.shot.greekutils.MilesianInteger
import edu.holycross.shot.greekutils.MilesianFraction
import edu.holycross.shot.greekutils.MilesianString

import edu.unc.epidoc.transcoder.TransCoder

/**
 * A class for managing input and output of tabular data in .csv or .tsv format.
 */
class CsvManager {

  /** Epidoc transcoder for transliterating labels. */
  TransCoder xcoder
  
  /** Creates a map of URNs to human-readable labels
   * from a csv file with URNs in the first column and
   * labelling strings in the second column.
   * @param csv File with the data, in csv format.
   * @returns Hash keyed by URN with labelling strings as values.
   */
  HashMap labelMap(File csv) {
    def urnMap = [:]
    Integer count  = 0
    csv.eachLine { l ->
      def cols = l.split(/,/)
      String urn = cols[0]
      
      urnMap[urn] = cols[1]
      count++
    }
    return urnMap
  }
  

  /** Attaches PtolemyList objects to PtolemySite objects.
   *  Sites are in a many-to-one relation to Lists. Sites should
   *  always have a URN value identifying the List they belong to.
   *  This method checks that value against the URNs in ptolemyLists,
   * and assigns any matching List object to the Site.
   * @param sites List of PtolemySite objects, presumed to have a
   * listUrn value.
   * @param ptolemyLists List of PtolemyLists to check.
   * @returns ArrayList of PtolemySite objects, with ptolemyList properties
   * for all sites where matching list was found.
   */
  ArrayList joinSitesToLists(ArrayList sites, ArrayList ptolemyLists) {
    ArrayList returnList = []
    sites.each { s ->
      PtolemyList pl = ptolemyLists.find {it.listUrn == s.listUrn}
      //System.err.println "For ${s.listUrn}
      s.ptolemyList = pl
      //System.err.println "${s} : added list " + s.ptolemyList
      returnList.add(s)
    }
    return returnList
  }
  

  /** Creates a list of PtolemyList objects from
   * a minimal CSV source with list URN in the first column,
   * sequence in the text in the second column, and CTS URN
   * value for the passage where it occurs in the third column.
   * @param csv Source file.
   * @returns ArrayList of PtolemyList objects.
   */
  ArrayList listsFromCsv(File csv) {
    def ptolLists = []
    Integer count = 0
    csv.eachLine { l ->
      if (count > 0) {
	def cols = l.split(/,/)
	if (cols.size() < 3) {
	  System.err.println "Did not find 3 columns in ${cols}"
	} else {
	  Integer seq = cols[1].toInteger()
	  PtolemyList ptolemyList = new PtolemyList(
	    listUrn: cols[0],textSequence: seq,passageUrn: cols[2]
	  )
	  ptolLists.add(ptolemyList)
	}
      }
      count++
    }
    return ptolLists
  }


  ArrayList addEthnics(ArrayList ptolLists, File csv) {
    ArrayList resultList = ptolLists
    Integer count = 0
    csv.eachLine { l ->
      if (count > 0) {
	def cols = l.split(/,/)
	String urnStr = cols[1]
	if (cols.size() != 3) {
	  System.err.println "Did not find 3 columns in ${cols}"
	} else {
	  def includedLists = ptolLists.findAll {it.passageUrn ==~ urnStr}
	  if (includedLists == null) {
	    System.err.println "ERROR: no matching passage for " + urnStr + ", failed for " + cols
	  }
	  includedLists.each { pList ->
	    pList.ethnicUrn = cols[2]
	    // Now replace in original list!
	    resultList = resultList.findAll { it.listUrn != pList.listUrn}
	    resultList.add(pList)
	  }
	}
      }
      count++
    }
    return resultList
  }

  ArrayList addGeoTypes(ArrayList ptolLists, File csv) {
    ArrayList resultList = ptolLists
    Integer count = 0
    csv.eachLine { l ->
      if (count > 0) {
	def cols = l.split(/,/)
	String urnStr = cols[1]
	if (cols.size() != 3) {
	  System.err.println "Did not find 3 columns in ${cols}"
	} else {
	  def includedLists = ptolLists.findAll {it.passageUrn ==~ urnStr}
	  if (includedLists == null) {
	    System.err.println "ERROR: no matching passage for " + urnStr + ", failed for " + cols
	  }
	  includedLists.each { pList ->
	    pList.geoTypeUrn = cols[2]
	    // Now replace in original list!
	    resultList = resultList.findAll { it.listUrn != pList.listUrn}
	    resultList.add(pList)
	  }
	}
      }
      count++
    }
    return resultList
  }
  
  /** Adds data from CSV source to a list of PtolemyList objects.
   * @param ptolLists A list of PtolemyList objects.
   * @param csv CSV file with data for provinces.
   */
  ArrayList addProvinces(ArrayList ptolLists, File csv) {
    ArrayList resultList = ptolLists
    Integer count = 0
    csv.eachLine { l ->
      if (count > 0) {
	def cols = l.split(/,/)
	if (cols.size() != 3) {
	  System.err.println "Did not find 3 columns in ${cols}"
	} else {
	  def includedUrn = ~/${cols[1]}\..*/
	  def includedLists = ptolLists.findAll {it.passageUrn ==~ includedUrn}
	  if (includedLists == null) {
	    System.err.println "ERROR: no matching passage for " + cols[1] + ", failed for " + cols + " using pattern " + includedUrn
	  }
	  includedLists.each { pList ->
	    pList.provinceUrn = cols[2]
	    // Now replace in original list!
	    resultList = resultList.findAll { it.listUrn != pList.listUrn}
	    resultList.add(pList)
	  }
	}
      }
      count++
    }
    return resultList
  }

  

  
  /** Constructor configures transcoder.
   */
  CsvManager() {
    xcoder = new TransCoder()
    xcoder.setParser("Unicode")
    xcoder.setConverter("GreekXLit")
  }





  // make minimal csv file for stet of points
  /**
   */
  String minimalCsvForSites (ArrayList ptolemyPoints, HashMap provinceLabels ) {
    String csv = "SiteId,Label,Pleiades\n"
    ptolemyPoints.each { ptol  ->
      def coords = PtolemyProjector.project(ptol)
      String xcoded = xcoder.getString(ptol.greekName)
      String description = "${xcoded} in ${provinceLabels[ptol.ptolemyList.provinceUrn]}"
      csv += "${ptol.urnString},${description},\n"
    }
    return csv
  }

  
  String modernSitesToCsv(ArrayList modernSiteList) {
    String csv = "SiteId,Label,Lon,Lat\n"
    modernSiteList.each { s ->
      String xcoded = xcoder.getString(s.ptolemySite.greekName)
      String newLine = "${s.ptolemySite.urnString},'" + xcoded + "',${s.projectedCoords[0]},${s.projectedCoords[1]}\n"
      csv += newLine
    }
    return csv
  }


  // full join of site and list properties
  String compoundSitesToCsv(ArrayList modernSiteList) {
    String csv = "site,label,lon,lat,textSequence,textUrn,contintent,province,geoType,ethnic,physicalRegion\n"
    modernSiteList.each { s ->
      String xcoded = xcoder.getString(s.ptolemySite.greekName)
      String newLine = "${s.ptolemySite.urnString},'" + xcoded + "',${s.projectedCoords[0]},${s.projectedCoords[1]},${s.ptolemySite.textSequence},${s.ptolemySite.ptolemyList.passageUrn},${s.ptolemySite.ptolemyList.continentUrn},${s.ptolemySite.ptolemyList.provinceUrn},${s.ptolemySite.ptolemyList.geoTypeUrn},${s.ptolemySite.ptolemyList.ethnicUrn},${s.ptolemySite.ptolemyList.physicalRegionUrn}"
      csv += newLine
    }
    return csv
  }

  
}

