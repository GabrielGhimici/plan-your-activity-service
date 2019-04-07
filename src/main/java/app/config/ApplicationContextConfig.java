package app.config;

import java.net.*;
import java.util.Properties;

import javax.sql.DataSource;

import app.dao.EventsDao;
import app.dao.EventsDaoI;
import app.dao.LoginDao;
import app.dao.LoginDaoI;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@ComponentScan("app")
@EnableTransactionManagement
public class ApplicationContextConfig {

    @Bean(name = "dSource")
    public BasicDataSource dataSource() throws URISyntaxException {

        //URI dbUri = new URI(System.getenv("DATABASE_URL"));
        URI dbUri = new URI("postgres://skpwaubfsmqeuv:79beb15610377684dfeac94235ed52ea7e3bdd6050f9568a6badd16c2374835f@ec2-54-247-92-185.eu-west-1.compute.amazonaws.com:5432/dfklrr4ilueqsl");

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        //String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        return properties;
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dSource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dSource);
        sessionBuilder.addProperties(getHibernateProperties());
        sessionBuilder.scanPackages("app.model");
        return sessionBuilder.buildSessionFactory();
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(
            SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(
                sessionFactory);

        return transactionManager;
    }

    @Autowired
    @Bean(name = "loginDAO")
    public LoginDao getPassLoginDao(SessionFactory sessionFactory) {

        return new LoginDaoI(sessionFactory);
    }

    @Autowired
    @Bean(name = "EventsDAO")
    public EventsDao getPassEventsDao(SessionFactory sessionFactory) {
        return new EventsDaoI(sessionFactory);
    }

}