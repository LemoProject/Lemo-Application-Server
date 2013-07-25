package de.lemo.apps.pages.data;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;

@RequiresAuthentication
public class Help {
	
	@Inject
	private Logger logger;
	
	@Inject
	@Path("../../pdf/LeMo_HB_final_a.pdf")
	@Property
	private Asset helpPDF;
	
	
}

