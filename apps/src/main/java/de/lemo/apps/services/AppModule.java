package de.lemo.apps.services;

import java.io.IOException;
import java.util.Date;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.*;
import org.apache.tapestry5.hibernate.HibernateTransactionDecorator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.got5.tapestry5.jquery.JQuerySymbolConstants;
import org.slf4j.Logger;
import org.tynamo.security.SecuritySymbols;

import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.AnalysisWorkerImpl;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.DateWorkerImpl;
import de.lemo.apps.application.StatisticWorker;
import de.lemo.apps.application.StatisticWorkerImpl;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.application.UserWorkerImpl;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.CourseDAOImpl;
import de.lemo.apps.integration.QuestionDAO;
import de.lemo.apps.integration.QuestionDAOImpl;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.integration.UserDAOImpl;
import de.lemo.apps.restws.client.Information;
import de.lemo.apps.restws.client.InformationImpl;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.client.InitialisationImpl;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.AnalysisImpl;
import de.lemo.apps.services.internal.jqplot.js.JqPlotJavaScriptStack;
import de.lemo.apps.services.security.BasicSecurityRealm;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class AppModule
{
    public static void bind(ServiceBinder binder)
    {
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);
    	
    	// Service for basic user authentification 
    	binder.bind(AuthorizingRealm.class, BasicSecurityRealm.class);
    	binder.bind(UserDAO.class, UserDAOImpl.class);
    	binder.bind(CourseDAO.class, CourseDAOImpl.class);
    	binder.bind(QuestionDAO.class, QuestionDAOImpl.class);
    	//binder.bind(LemoServiceResource.class, LemoServiceResourceImpl.class);
    	
    	//Facade Worker
    	binder.bind(UserWorker.class, UserWorkerImpl.class);
    	binder.bind(DateWorker.class, DateWorkerImpl.class);
    	binder.bind(StatisticWorker.class, StatisticWorkerImpl.class);
    	binder.bind(AnalysisWorker.class, AnalysisWorkerImpl.class);
    	
    	
    	// Rest Services
    	binder.bind(Initialisation.class, InitialisationImpl.class);
    	binder.bind(Information.class, InformationImpl.class);
    	binder.bind(Analysis.class, AnalysisImpl.class);
    	
    	

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overriden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.APPLICATION_VERSION, "0.0.1-SNAPSHOT");
    }
    
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration)
    {
     configuration.addInstance(JqPlotJavaScriptStack.STACK_ID, JqPlotJavaScriptStack.class);
    }
    
//    /**
//	 * Contributions to the RESTeasy main Application.
//	 */
//	public static void contributeApplication(Configuration<Object> singletons, ObjectLocator locator)
//	{
//		singletons.add(locator.autobuild(LemoServiceResourceImpl.class));
//	}

    
//    @Contribute(javax.ws.rs.core.Application.class)
//    public static void configureRestResources(Configuration<Object> singletons, Initialisation init)
//    {
//    	singletons.add(init);
//    }
    
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,de");
        
        // Disable Prototype Support in Tap5
        configuration.add(JQuerySymbolConstants.SUPPRESS_PROTOTYPE, "true");
        
        // Tynamo's tapestry-security module configuration
    	configuration.add(SecuritySymbols.LOGIN_URL, "/start");
    	configuration.add(SecuritySymbols.UNAUTHORIZED_URL, "/start");
    	configuration.add(SecuritySymbols.SUCCESS_URL, "/data/initialize");
    }
    
//    @Match("*DAO")
//    public static void adviseTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver)
//    {
//        advisor.addTransactionCommitAdvice(receiver);
//    }
    
    @Match("*DAO")
    public static <T> T decorateTransactionally(HibernateTransactionDecorator decorator, Class<T> serviceInterface,
                                                T delegate,
                                                String serviceId)
    {
        System.out.println("AppModule: Generating Decorator for DAO Interface");
    	return decorator.build(serviceInterface, delegate, serviceId);
    }
    
    
    
    public static void contributeWebSecurityManager(Configuration<Realm> configuration,
            @Inject AuthorizingRealm realm)
    {
        configuration.add(realm);
    }
    
    public static void contributeSeedEntity(OrderedConfiguration<Object> configuration)
    {
    	
    	//Example Courses
    	configuration.add("course1", new Course("Mathematik 2", "Dieser Kurs richtet sich an Studierende, die den Kurs Mathematik 1 erfolgreich bestanden haben ....",
                new Date(), new Date(), 25L, 22L));
    	configuration.add("course2", new Course("Mathematik 3", "Dieser Kurs richtet sich an Studierende, die den Kurs Mathematik 2 erfolgreich bestanden haben ....",
                new Date(), new Date(), 25L, 12L));
    	configuration.add("course3", new Course("BWL für Mathematiker", "Dieser Kurs richtet sich an Studierende, der Mathematik ....",
                new Date(), new Date(), 15L, 15L));
    	configuration.add("course4", new Course("Informatik für Mathematiker", "Dieser Kurs richtet sich an Studierende, dwe Mathematik  ....",
                new Date(), new Date(), 20L, 21L));
    	
    	//Example User
        configuration.add("user1", new User("John Doe", "johndoe",
                "johndoe@example.com", "john"));
        configuration.add("user2", new User("Peter Smith", "petersmith", "petersmith@example.com",
                "peter"));
    }
    
    
    /**
     * 
     * @param configuration
     * Contribution for hibernate domain package
     */
    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
    {
        configuration.add("de.lemo.apps.entities");
    }

    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     */
    public RequestFilter buildTimingFilter(final Logger log)
    {
        return new RequestFilter()
        {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException
            {
                long startTime = System.currentTimeMillis();

                try
                {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally
                {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @Local
                                         RequestFilter filter)
    {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Timing", filter);
    }
}
