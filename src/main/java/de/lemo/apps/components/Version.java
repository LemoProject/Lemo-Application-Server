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
