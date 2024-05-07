package io.github.hubao.hbconfig.client.annotation;

import io.github.hubao.hbconfig.client.config.HBConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import(HBConfigRegistrar.class)
public @interface EnableHBConfig {
}
