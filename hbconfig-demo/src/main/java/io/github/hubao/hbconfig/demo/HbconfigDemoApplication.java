package io.github.hubao.hbconfig.demo;

import io.github.hubao.hbconfig.client.annotation.EnableHBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigurationProperties(HbDemoConfig.class)
@EnableHBConfig
public class HbconfigDemoApplication {

    @Value("${hb.a}")
    private String a;

    @Autowired
    private HbDemoConfig hbDemoConfig;

    @Autowired
    Environment environment;


    public static void main(String[] args) {
        SpringApplication.run(HbconfigDemoApplication.class, args);
    }

    @Bean
    ApplicationRunner runner() {
        System.out.println(environment.getProperty("hb.a"));
        return args -> {
            System.out.println(a);
            System.out.println(hbDemoConfig.getA());
        };
    }
}
