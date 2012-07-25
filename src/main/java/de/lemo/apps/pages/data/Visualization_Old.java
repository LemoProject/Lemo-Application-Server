package de.lemo.apps.pages.data;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;

@RequiresAuthentication
@BreadCrumb(titleKey="visualizationTitle")
public class Visualization_Old {
	
	@Property
	private BreadCrumbInfo breadCrumb;
	
	@Inject
	private JavaScriptSupport javascript;

	public JSONObject getOptions(){
	JSONObject opt = new JSONObject();
	opt.put("text", "Source: WorldClimate.com");
	opt.put("x", -20);

	JSONObject high = new JSONObject();
	high.put("subtitle", opt);

	return high;
	}

	@AfterRender
	public void afterRender(){
	javascript.addInitializerCall(InitializationPriority.EARLY, "visualization", new JSONObject());
	javascript.addInitializerCall("documentation", new JSONObject());
	}

}
