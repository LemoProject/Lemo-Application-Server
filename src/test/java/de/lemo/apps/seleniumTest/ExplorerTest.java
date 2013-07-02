/**
 * File ./src/test/java/de/lemo/apps/seleniumTest/ExplorerTest.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2013
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package de.lemo.apps.seleniumTest;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class ExplorerTest extends SeleniumTestCase {

	@Test
	public void explorer() {

		this.open("start");
		this.type("username", "user");
		this.type("password", "lemolemo");
		this.clickAndWait("signin");

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