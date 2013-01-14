package de.lemo.apps.services.internal;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tapestry5.services.ApplicationInitializer;
import org.apache.tapestry5.services.ApplicationInitializerFilter;
import org.apache.tapestry5.services.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.lemo.apps.application.config.ServerConfiguration;
import de.lemo.apps.entities.User;

public class AppsInitializerImpl implements ApplicationInitializerFilter {

    private Session session;
    private Logger logger = Logger.getLogger(getClass());
    
    private AppsInitializerImpl(Session session) {
        this.session = session;
    }

    public void initializeApplication(Context context, ApplicationInitializer applicationInitializer) {
        logger.info("initializing application");
        importUsers();
    }

    private void importUsers() {
        List<User> userImports = ServerConfiguration.getInstance().getUserImports();
        if(!userImports.isEmpty()) {
            Transaction t = session.getTransaction();
            t.begin();
            for(User user : userImports) {
                if(session.get(User.class, user.getUsername()) == null) {
                    // do not update existing users
                    session.save(user);
                }
            }
            t.commit();
        }
    }

}
