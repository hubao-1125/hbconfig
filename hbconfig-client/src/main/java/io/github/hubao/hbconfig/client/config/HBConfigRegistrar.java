package io.github.hubao.hbconfig.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/2 21:11
 */
@Slf4j
public class HBConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        System.out.println("register PropertySourcesProcessor");
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames()).filter(i -> i.equals(PropertySourcesProcessor.class.getName())).findFirst();
        if (first.isPresent()) {
            System.out.println("register PropertySourcesProcessor is already register.");
            return;
        }
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(PropertySourcesProcessor.class).getBeanDefinition();
        registry.registerBeanDefinition(PropertySourcesProcessor.class.getName(), beanDefinition);
    }
}
