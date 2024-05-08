package io.github.hubao.hbconfig.demo;

import io.github.hubao.hbconfig.client.annotation.EnableHBConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigurationProperties(HbDemoConfig.class)
@EnableHBConfig
@RestController
@Slf4j
public class HbconfigDemoApplication {

    @Value("${hb.a}")
    private String a;

    @Value("${hb.b}")
    private String b;

    @Autowired
    private HbDemoConfig hbDemoConfig;

    @Autowired
    Environment environment;


    public static void main(String[] args) {
        SpringApplication.run(HbconfigDemoApplication.class, args);
    }

    @GetMapping("/demo")
    public String demo() {
        return "hb.a = " + a + "\n"
                + "kk.b = " + b + "\n"
                + "demo.a = " + hbDemoConfig.getA() + "\n"
                + "demo.b = " + hbDemoConfig.getB() + "\n";
    }

    @Bean
    ApplicationRunner runner() {
        log.info(environment.getProperty("hb.a"));
        return args -> {
            log.info(a);
            log.info(hbDemoConfig.getA());
        };
    }
}
