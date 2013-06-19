/**
 * 
 */
package de.lemo.apps.seleniumTest;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

/**
 * @author johndoe
 */
public class DashboardTest extends SeleniumTestCase {

	@Test
	public void dashboard() {
		this.open("start");
		this.type("username", "user");
		this.type("password", "lemolemo");
		this.clickAndWait("signin");

		this.assertTitle("LeMo | Initialization");

		this.waitForCondition(
				"var title = selenium.browserbot.getCurrentWindow().document.title; title.match('Lemo | Dashboard')",
				"15000");

		this.assertTitle("LeMo | Dashboard");

		this.assertTextPresent("My courses");
		this.assertTextPresent("Logout");

		this.captureScreenshot("/Users/johndoe/git/apps/apps/target/surefire-reports/Selenium Tests Suite/DashboardPage.png");

		//goto explorer page and check for content
		this.clickAndWait("mycourses");
		this.assertTitle("LeMo | Explorer");
		this.assertTextPresent("Overview");
		this.assertTextPresent("Last month");
		this.assertTextPresent("Overall");
		this.assertTextPresent("Settings");
		this.assertTextPresent("Please choose your analysis:");
		
		//logging out 
		this.clickAndWait("logout");
		this.assertTextPresent("LeMo - Monitoring of Learning processes");
	}
}