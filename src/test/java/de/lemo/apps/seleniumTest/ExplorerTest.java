package de.lemo.apps.seleniumTest;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class ExplorerTest extends SeleniumTestCase
{

    @Test
    public void explorer()
    {
    	
    	open("start");
    	type("username","johndoe");
        type("password","john");
        clickAndWait("Sign in");
       
        assertTitle("LeMo | Initialization");
        
        
        waitForCondition("var title = selenium.browserbot.getCurrentWindow().document.title; title.match('Lemo | Dashboard')", "15000");  
        
        
        open("data/explorer");
   
        
        assertTextPresent("My courses");
        assertTextPresent("Logout");

        captureScreenshot("/Users/johndoe/git/apps/apps/target/surefire-reports/Selenium Tests Suite/ExplorerPage.png");
        
        //clickAndWait("View details");
        //assertTitle("LeMo | Visualization");
        
    }
}