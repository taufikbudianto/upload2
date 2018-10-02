/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author Randy
 */
@SpringBootApplication(
        scanBasePackages = { "prosia.app", "prosia.app.service", "prosia.app.web.controller", 
            "prosia.basarnas.service", "prosia.basarnas.web.controller", "prosia.basarnas.web.rest"})
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
