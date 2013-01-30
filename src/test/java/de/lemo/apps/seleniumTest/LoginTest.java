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
		this.assertTextPresent("LeMo - Learnprocess Monitoring");

		this.captureScreenshot("/Users/johndoe/git/apps/apps/target/surefire-reports/Selenium Tests Suite/StartPage.png");

		this.clickAndWait("Sign in");
		this.assertTextPresent("Login or password not correct");

		this.type("username", "johndoe");
		this.type("password", "john");
		this.clickAndWait("Sign in");

		this.assertTitle("LeMo | Initialization");

		this.waitForCondition(
				"var title = selenium.browserbot.getCurrentWindow().document.title; title.match('Lemo | Dashboard')",
				"15000");
		this.assertTitle("LeMo | Dashboard");

		// waitForPageToLoad("15");

	}
}