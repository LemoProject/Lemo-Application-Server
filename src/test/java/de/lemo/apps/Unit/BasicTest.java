/**
 * File BasicTest.java
 * Date May 31, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.Unit;


import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.test.PageTester;
import org.testng.Assert;
import org.testng.annotations.Test;



/**
 * @author Andreas Pursian
 *
 */
public class BasicTest extends Assert{
	@Test
    public void test1()
    {
        String appPackage = "de.le,o.apps";
        String appName = "lemo";
        PageTester tester = new PageTester(appPackage, appName, "src/main/webapp");
        Document doc = tester.renderPage("Start");
        assertTrue(doc.toString().contains("LeMo"));
    }

}
