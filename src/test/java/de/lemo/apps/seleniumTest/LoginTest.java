/**
 * 
 */
package de.lemo.apps.seleniumTest;

/**
 * @author johndoe
 *
 */
import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class LoginTest extends SeleniumTestCase
{

    @Test
    public void loginUser()
    {
        open("start");
        waitForPageToLoad("15");
        //waitForCondition(getTitle(), "15");
        assertTextPresent("LeMo - Learnprocess Monitoring");

        captureScreenshot("/Users/johndoe/git/apps/apps/target/surefire-reports/Selenium Tests Suite/StartPage.png");
        
        clickAndWait("Sign in");
        assertTextPresent("Login or password not correct");
        
        type("username","johndoe");
        type("password","john");
        clickAndWait("Sign in");
       
        assertTitle("LeMo | Initialization");
        
        waitForCondition("var title = selenium.browserbot.getCurrentWindow().document.title; title.match('Lemo | Dashboard')", "15000");  
        assertTitle("LeMo | Dashboard");
        
        //waitForPageToLoad("15");
        
        
    }
}