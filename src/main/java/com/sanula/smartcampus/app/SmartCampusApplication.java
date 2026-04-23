package com.sanula.smartcampus.app;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends ResourceConfig {
    public SmartCampusApplication() {
        // This replaces the config logic that used to be in Main.java
        packages("com.sanula.smartcampus");
    }
}