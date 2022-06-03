package pl.edu.umg.linkumg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
@EnableJdbcHttpSession
public class LinkumgApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkumgApplication.class, args);
	}

}
