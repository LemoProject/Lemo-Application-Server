package de.lemo.apps.application.config;

import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.lemo.apps.entities.User;

/**
 * @author Leonard Kappe
 */
public enum ServerConfiguration {

	INSTANCE;

	{
		// TODO the DMS initializes the logger here - is there a better way in tapestry?
	}

	private static String USER_FILE = "users.xml";

	private final Logger logger = Logger.getLogger(this.getClass());
	private String serverName;
	private Map<String, String> dbConfig;
	private String dmsUrl;

	private List<User> userImports;

	public static ServerConfiguration getInstance() {
		return INSTANCE;
	}

	/**
	 * The context path is used to determine the configuration file's name. The first char (a slash by convention) will
	 * be
	 * ignored. Some context paths aren't valid file names (like the root context or those with sub-paths), so tomcat's
	 * naming conventions for .war files will be used.
	 * Examples:
	 * 
	 * <pre>
	 * context   -> file name
	 * -------------------------
	 * /         -> ROOT.xml
	 * /lemo     -> lemo.xml
	 * /lemo/foo -> lemo#foo.xml
	 * </pre>
	 * 
	 * @param contextPath
	 */
	public void loadConfig(final String contextPath) {
		final LemoConfig lemoConfig = this.readConfigFiles(contextPath);
		// TODO set log level from config

		this.serverName = lemoConfig.applicationServer.name;
		this.dmsUrl = lemoConfig.applicationServer.dataManagementServerURL;

		this.dbConfig = Maps.newHashMap();
		for (final PropertyConfig property : lemoConfig.applicationServer.appDbConfig) {
			this.dbConfig.put(property.key, property.value);
		}
		final LemoUserConfig userConfigurations = this.readUserFile();
		if (userConfigurations != null) {
			this.userImports = this.createUsers(userConfigurations);
		} else {
			this.userImports = Lists.newArrayList();
		}
	}

	private List<User> createUsers(final LemoUserConfig userConfigurations) {
		final List<User> users = Lists.newArrayList();
		if ((userConfigurations.users == null) || userConfigurations.users.isEmpty()) {
			this.logger.warn(ServerConfiguration.USER_FILE + " loaded but no users elements were found.");
		} else {
			for (final UserConfig userConfig : userConfigurations.users) {
				final User user = new User(userConfig.fullName, userConfig.username, userConfig.email, userConfig.password);
				//TODO FIX Course import to match new database schema
				//user.setMyCourses(userConfig.courses);
				users.add(user);
			}
		}
		return users;
	}

	private LemoUserConfig readUserFile() {
		try {
			final Unmarshaller jaxbUnmarshaller = JAXBContext.newInstance(LemoUserConfig.class).createUnmarshaller();
			final InputStream in = this.getClass().getResourceAsStream("/" + ServerConfiguration.USER_FILE);
			if (in == null) {
				return null;
			}
			this.logger.info("Loading user file: " + ServerConfiguration.USER_FILE);
			return (LemoUserConfig) jaxbUnmarshaller.unmarshal(in);
		} catch (final JAXBException e) {
			// no way to recover, re-throw at runtime
			throw new RuntimeException(e);
		}
	}

	private LemoConfig readConfigFiles(String contextPath) {
		if (contextPath.isEmpty()) {
			// empty means root context, Tomcat convention for root context war files is 'ROOT.war'
			contextPath = "/ROOT";
		}

		// remove leading slash, replace any slashes with hashes like Tomcat does with the war files
		final String warName = contextPath.substring(1).replace('/', '#');

		final Set<String> fileNames = new LinkedHashSet<String>();
		fileNames.add(warName + ".xml"); // default, based on war name

		int lastHash;
		String warPath = warName;
		while ((lastHash = warPath.lastIndexOf('#')) > 0) {
			// try to read more generic name by removing sub-paths, i.e. /lemo/dms -> /lemo
			// (useful to read the appserver's config file, so only one file is needed)
			warPath = warPath.substring(0, lastHash);
			fileNames.add(warPath + ".xml");
		}

		fileNames.add("lemo.xml"); // eventually try generic lemo.xml for use in local development

		LemoConfig lemoConfig = null;
		try {
			final Unmarshaller jaxbUnmarshaller = JAXBContext.newInstance(LemoConfig.class).createUnmarshaller();
			for (final String fileName : fileNames) {
				final InputStream in = this.getClass().getResourceAsStream("/" + fileName);
				if (in != null) {
					this.logger.info("Using config file: " + fileName);
					lemoConfig = (LemoConfig) jaxbUnmarshaller.unmarshal(in);
				}
			}
		} catch (final JAXBException e) {
			// no way to recover, re-throw at runtime
			throw new RuntimeException(e);
		}
		if (lemoConfig == null) {
			final String files = fileNames.toString();
			throw new RuntimeException(
					"No config file found in the classpath. Files follow tomcat's naming convention for .war files, " +
							"in following order until a valid one is found: "
							+ files.substring(1, files.length() - 1));
		}

		this.logger.info("Config loaded for '" + lemoConfig.applicationServer.name + "'");
		return lemoConfig;
	}

	public String getName() {
		return this.serverName;
	}

	public String getDMSBaseUrl() {
		return this.dmsUrl;
	}

	public Map<String, String> getDbConfig() {
		return this.dbConfig;
	}

	public List<User> getUserImports() {
		return this.userImports;
	}

}
