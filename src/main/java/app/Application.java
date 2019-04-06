package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(exclude= HibernateJpaAutoConfiguration.class)
// We need the following in order to detect the filter
// from the security package
@ServletComponentScan(value="app.services")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}