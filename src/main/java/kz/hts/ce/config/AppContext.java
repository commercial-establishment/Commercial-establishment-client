package kz.hts.ce.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppContext {

    public AppContext() {
    }

    private static class AppContextHolder {
        private static final ApplicationContext instance = new AnnotationConfigApplicationContext(PersistenceConfig.class);
    }

    public static ApplicationContext getInstance(){
        return AppContextHolder.instance;
    }
}
