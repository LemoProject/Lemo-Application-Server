/**
 * 
 */
package de.lemo.apps.seleniumTest;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

/**
 * @author johndoe
 *
 */
public class DashboardTest extends SeleniumTestCase
{

    @Test
    public void dashboard()
    {
    	open("start");
    	type("username","johndoe");
        type("password","john");
        clickAndWait("Sign in");
       
        assertTitle("LeMo | Initialization");
        
        waitForCondition("var title = selenium.browserbot.getCurrentWindow().document.title; title.match('Lemo | Dashboard')", "15000");  

        assertTitle("LeMo | Dashboard");
    	
   
        assertTextPresent("My courses");
        assertTextPresent("Logout");

        captureScreenshot("/Users/johndoe/git/apps/apps/target/surefire-reports/Selenium Tests Suite/DashboardPage.png");
        
        clickAndWait("My courses");
        assertTitle("LeMo | Explorer");
        assertTextPresent("Summary");
    }
}