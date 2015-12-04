package kz.hts.ce.util.spring;

import kz.hts.ce.config.PersistenceConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppContextSingleton {

    public AppContextSingleton() {
    }

    private static class AppContextHolder {
        private static final ApplicationContext instance = new AnnotationConfigApplicationContext(PersistenceConfig.class);
    }

    public static ApplicationContext getInstance(){
        return AppContextHolder.instance;
    }
}
