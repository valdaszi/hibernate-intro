package lt.bit.java2.utils;

import lt.bit.java2.entities.Employee;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HibernateUtil {

    private static EntityManagerFactory entityManagerFactory;

    static {
        StandardServiceRegistry registry = null;
        try {
            Properties appProperties = new Properties();
            InputStream is = HibernateUtil.class.getClassLoader().getResourceAsStream("app.properties");
            appProperties.load(is);

            // Hibernate settings
            Map<String, Object> settings = new HashMap<>();
            settings.put(Environment.DRIVER, appProperties.getProperty("jdbc.driver"));
            settings.put(Environment.URL, appProperties.getProperty("jdbc.url"));
            settings.put(Environment.USER, appProperties.getProperty("jdbc.user"));
            settings.put(Environment.PASS, appProperties.getProperty("jdbc.password"));
            settings.put(Environment.HBM2DDL_AUTO, appProperties.getProperty("jdbc.hibernate.hbm2dll_auto"));
            settings.put(Environment.SHOW_SQL, Boolean.valueOf(appProperties.getProperty("jdbc.hibernate.show_sql")));
            settings.put(Environment.FORMAT_SQL, Boolean.valueOf(appProperties.getProperty("jdbc.hibernate.format_sql")));

            // HikariCP settings
            // Maximum waiting time for a connection from the pool
            settings.put("hibernate.hikari.connectionTimeout", "20000");
            // Minimum number of idle connections in the pool
            settings.put("hibernate.hikari.minimumIdle", "10");
            // Maximum number of actual connection in the pool
            settings.put("hibernate.hikari.maximumPoolSize", "20");
            // Maximum time that a connection is allowed to sit ideal in the pool
            settings.put("hibernate.hikari.idleTimeout", "300000");

            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
            registryBuilder.applySettings(settings);

            registry = registryBuilder.build();

            // Registruojame entity klases
            MetadataSources sources = new MetadataSources(registry);
            sources.addAnnotatedClass(Employee.class);

            // Sukuriame EntityManagerFactory objekta is kurio veliau gausime EntityManager objektus (getEntityManager() metodas)
            Metadata metadata = sources.getMetadataBuilder().build();
            entityManagerFactory = metadata.getSessionFactoryBuilder().build();

        } catch (Exception e) {
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            e.printStackTrace();
        }
    }

    static public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

}
