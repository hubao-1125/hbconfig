package io.github.hubao.hbconfig.client;

import io.github.hubao.hbconfig.client.config.HBConfigService;
import io.github.hubao.hbconfig.client.config.HBConfigServiceImpl;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, PriorityOrdered, EnvironmentAware {

    private final static String HB_PROPERTY_SOURCES = "HBPropertySources";
    private final static String HB_PROPERTY_SOURCE = "HBPropertySource";

    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        boolean contains = env.getPropertySources().contains(HB_PROPERTY_SOURCES);
        if (contains) {
            return;
        }

        // 去配置中心获取配置 TODO
        Map<String, String> config = new HashMap<>();
        config.put("hb.a", "dev500");
        config.put("hb.b", "dev600");
        config.put("hb.c", "dev700");

        HBConfigService configService = new HBConfigServiceImpl(config);
        HBPropertySource propertySource = new HBPropertySource(HB_PROPERTY_SOURCE, configService);
        CompositePropertySource compositePropertySource = new CompositePropertySource(HB_PROPERTY_SOURCE);
        compositePropertySource.addPropertySource(propertySource);
        env.getPropertySources().addFirst(compositePropertySource);

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
