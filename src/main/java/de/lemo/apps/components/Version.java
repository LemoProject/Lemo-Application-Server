/**
 * File ./src/main/java/de/lemo/apps/components/Version.java
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

package de.lemo.apps.components;

import java.io.File;
import java.io.FileReader;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

/**
 * read the current versionumber from the pom.xml
 * 
 * @author Boris Wenzlaff
 */
public class Version {

	private final String pom = "pom.xml";

	/**
	 * read the version number from the pom.xml
	 * 
	 * @return version number from app-server
	 */
	public String getServerVersion() {
		String version = "unknown";
		final File pomfile = new File(this.pom);
		Model model = null;
		FileReader reader = null;
		final MavenXpp3Reader mavenreader = new MavenXpp3Reader();
		try {
			reader = new FileReader(pomfile);
			model = mavenreader.read(reader);
			model.setPomFile(pomfile);
			version = model.getVersion();
		} catch (final Exception ex) {

		}
		return version;
	}
}
