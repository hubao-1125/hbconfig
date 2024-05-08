package io.github.hubao.hbconfig.client.config;

import io.github.hubao.hbconfig.client.value.SpringValueProcessor;
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
        registerClass(registry, PropertySourcesProcessor.class);
        registerClass(registry, SpringValueProcessor.class);
    }

    private static void registerClass(BeanDefinitionRegistry registry, Class<?> aClass) {
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames()).filter(i -> i.equals(aClass.getName())).findFirst();
        if (first.isPresent()) {
            log.info("register PropertySourcesProcessor is already register.");
            return;
        }
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(aClass).getBeanDefinition();
        registry.registerBeanDefinition(aClass.getName(), beanDefinition);
    }
}
