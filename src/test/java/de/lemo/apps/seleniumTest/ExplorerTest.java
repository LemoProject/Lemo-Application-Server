package de.lemo.apps.seleniumTest;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class ExplorerTest extends SeleniumTestCase
{

	@Test
	public void explorer()
	{

		this.open("start");
		this.type("username", "johndoe");
		this.type("password", "john");
		this.clickAndWait("Sign in");

		this.assertTitle("LeMo | Initialization");

		this.waitForCondition(
				"var title = selenium.browserbot.getCurrentWindow().document.title; title.match('Lemo | Dashboard')",
				"15000");

		this.open("data/explorer");

		this.assertTextPresent("My courses");
		this.assertTextPresent("Logout");

		this.captureScreenshot("/Users/johndoe/git/apps/apps/target/surefire-reports/Selenium Tests Suite/ExplorerPage.png");

		// clickAndWait("View details");
		// assertTitle("LeMo | Visualization");

	}
}