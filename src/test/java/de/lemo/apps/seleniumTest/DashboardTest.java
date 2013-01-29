/**
 * 
 */
package de.lemo.apps.seleniumTest;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

/**
 * @author johndoe
 */
public class DashboardTest extends SeleniumTestCase
{

	@Test
	public void dashboard()
	{
		this.open("start");
		this.type("username", "johndoe");
		this.type("password", "john");
		this.clickAndWait("Sign in");

		this.assertTitle("LeMo | Initialization");

		this.waitForCondition(
				"var title = selenium.browserbot.getCurrentWindow().document.title; title.match('Lemo | Dashboard')",
				"15000");

		this.assertTitle("LeMo | Dashboard");

		this.assertTextPresent("My courses");
		this.assertTextPresent("Logout");

		this.captureScreenshot("/Users/johndoe/git/apps/apps/target/surefire-reports/Selenium Tests Suite/DashboardPage.png");

		this.clickAndWait("My courses");
		this.assertTitle("LeMo | Explorer");
		this.assertTextPresent("Summary");
	}
}