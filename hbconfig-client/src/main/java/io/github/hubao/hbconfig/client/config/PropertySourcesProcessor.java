package io.github.hubao.hbconfig.client.config;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/2 20:52
 */
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, ApplicationContextAware, EnvironmentAware, PriorityOrdered {

    private final static String HB_PROPERTY_SOURCES = "HBPropertySources";
    private final static String HB_PROPERTY_SOURCE  = "HBPropertySource";
    Environment environment;
    ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        if(ENV.getPropertySources().contains(HB_PROPERTY_SOURCES)) {
            return;
        }

        String app = ENV.getProperty("hbconfig.app", "app1");
        String env = ENV.getProperty("hbconfig.env", "dev");
        String ns = ENV.getProperty("hbconfig.ns", "public");
        String configServer = ENV.getProperty("hbconfig.configServer", "http://localhost:9129");

        ConfigMeta configMeta = new ConfigMeta(app, env, ns, configServer);

        HBConfigService configService = HBConfigService.getDefault(applicationContext, configMeta);
        HBPropertySource propertySource = new HBPropertySource(HB_PROPERTY_SOURCE, configService);
        CompositePropertySource composite = new CompositePropertySource(HB_PROPERTY_SOURCES);
        composite.addPropertySource(propertySource);
        ENV.getPropertySources().addFirst(composite);

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
