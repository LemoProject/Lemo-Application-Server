/**
 * File ./src/test/java/de/lemo/apps/seleniumTest/LoginTest.java
 * Lemo-Application-Server for learning analytics.
 * Copyright (C) 2015
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

/**
 * 
 */
package de.lemo.apps.seleniumTest;

/**
 * @author johndoe
 */
import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class PredictionTest extends SeleniumTestCase {

	@Test
	public void loadPrdictionTool() {
		
		this.open("/start");
		this.waitForPageToLoad("150");
		this.assertTextPresent("LeMo - Monitoring of Learning processes");


		this.type("username", "user");
		this.type("password", "lemolemo");
		this.clickAndWait("signin");

		this.assertTitle("LeMo | Initialization");

		this.waitForCondition(
				"var title = selenium.browserbot.getCurrentWindow().document.title; title.match('LeMo | Visualization')",
				"15000");

		waitForPageToLoad("15000");
		this.captureScreenshot("StartPage.png");
		
	}
}