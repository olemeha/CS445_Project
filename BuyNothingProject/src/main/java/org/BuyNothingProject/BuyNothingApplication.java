package org.BuyNothingProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EntityScan(basePackageClasses = { BuyNothingApplication.class, Jsr310JpaConverters.class }, basePackages = {
		"org.BuyNothingProject.domain.*", })
@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = "org.BuyNothingProject.controller")
public class BuyNothingApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(BuyNothingApplication.class, args);
	}
}
