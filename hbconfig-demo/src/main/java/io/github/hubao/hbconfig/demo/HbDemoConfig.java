package io.github.hubao.hbconfig.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/2 20:31
 */
@Data
@ConfigurationProperties(prefix = "hb")
public class HbDemoConfig {

    String a;
    String b;
}
