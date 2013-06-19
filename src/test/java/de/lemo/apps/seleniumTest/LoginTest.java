/**
 * 
 */
package de.lemo.apps.seleniumTest;

/**
 * @author johndoe
 */
import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class LoginTest extends SeleniumTestCase {

	@Test
	public void loginUser() {
		
		this.open("start");
		this.waitForPageToLoad("15");
		// waitForCondition(getTitle(), "15");
		this.assertTextPresent("LeMo - Monitoring of Learning processes");

		this.captureScreenshot("/Users/johndoe/git/apps/apps/target/surefire-reports/Selenium Tests Suite/StartPage.png");

		this.clickAndWait("signin");
		this.assertTextPresent("Login or password not correct");

		this.type("username", "user");
		this.type("password", "lemolemo");
		this.clickAndWait("signin");

		this.assertTitle("LeMo | Initialization");

		this.waitForCondition(
				"var title = selenium.browserbot.getCurrentWindow().document.title; title.match('Lemo | Dashboard')",
				"15000");
		this.assertTitle("LeMo | Dashboard");

		// waitForPageToLoad("15");

	}
}