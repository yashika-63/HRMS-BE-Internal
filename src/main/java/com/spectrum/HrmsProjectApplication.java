package com.spectrum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @CrossOrigin("http://localhost:3000")
@SpringBootApplication
@EnableScheduling

	public class HrmsProjectApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(HrmsProjectApplication.class, args);
		
		System.out.println("Welcome to HRMS");
	}
		 
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// registry.addMapping("/**").allowedOrigins("http://15.207.163.30:3002" )
			 registry.addMapping("/**").allowedOrigins("http://localhost:5173")
			//  registry.addMapping("/**").allowedOrigins("http://172.16.20.131:3008")

				// Replace with your frontend URL
				.allowedMethods("GET", "POST", "PUT", "DELETE").allowCredentials(true);
		System.out.println("heyy your application run properly");
	}
}
