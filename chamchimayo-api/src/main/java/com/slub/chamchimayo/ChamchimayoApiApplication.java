package com.slub.chamchimayo;

import com.slub.chamchimayo.config.AppProperties;
import com.slub.chamchimayo.config.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
	CorsProperties.class,
	AppProperties.class
})
public class ChamchimayoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChamchimayoApiApplication.class, args);
	}

}
