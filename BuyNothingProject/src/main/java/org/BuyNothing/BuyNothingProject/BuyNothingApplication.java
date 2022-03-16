package org.BuyNothing.BuyNothingProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
	basePackageClasses = { BuyNothingApplication.class, Jsr310JpaConverters.class }
)
@SpringBootApplication
public class BuyNothingApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(BuyNothingApplication.class, args);
    }
}
