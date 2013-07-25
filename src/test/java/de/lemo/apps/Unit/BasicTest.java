/**
 * File ./src/test/java/de/lemo/apps/Unit/BasicTest.java
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

/**
 * File BasicTest.java
 * Date May 31, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.Unit;


import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.test.PageTester;
import org.testng.Assert;
import org.testng.annotations.Test;



/**
 * @author Andreas Pursian
 *
 */
public class BasicTest extends Assert{
	@Test
    public void test1()
    {
        String appPackage = "de.lemo.apps";
        String appName = "lemo";
        PageTester tester = new PageTester(appPackage, appName, "src/main/webapp");
        Document doc = tester.renderPage("Start");
        assertTrue(doc.toString().contains("LeMo"));
    }

}
