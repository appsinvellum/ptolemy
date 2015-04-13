package ptolemy;


import org.concordion.integration.junit3.ConcordionTestCase;

import edu.holycross.shot.ptolemy.GreekGeoParser;
import java.io.File;
import java.util.HashMap;


public class PtolemyTest extends ConcordionTestCase {
    String xmlLocation = "/build/specs/texts/editions/ptolemy-geo-hc.xml";

    /** Hands back a String parameter so we can save links using concordion's
     * #Href variable for use in later computations. */
    public String setHref(String path) {
	return (path);
    }

    
    public Integer countLists()
    throws Exception {
	String xmlFullPath = new java.io.File( "." ).getCanonicalPath() + xmlLocation;
	File xmlFile = new File(xmlFullPath);
	GreekGeoParser geo = new GreekGeoParser(xmlFile);
	return geo.indexLists().keySet().size();
    }



    public Integer countSites()
    throws Exception {
	String xmlFullPath = new java.io.File( "." ).getCanonicalPath() + xmlLocation;
	File xmlFile = new File(xmlFullPath);
	GreekGeoParser geo = new GreekGeoParser(xmlFile);
	return geo.indexListItems().keySet().size();
    }
}
