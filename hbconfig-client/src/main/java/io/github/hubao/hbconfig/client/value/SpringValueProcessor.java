package io.github.hubao.hbconfig.client.value;

import cn.kimmking.utils.FieldUtils;
import io.github.hubao.hbconfig.client.util.PlaceHolderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/8 20:11
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

    static final PlaceHolderHelper helper = PlaceHolderHelper.getInstance();
    static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        FieldUtils.findAnnotatedField(bean.getClass(), Value.class).forEach(
                field -> {
                    log.info("[HBCONFIG] ==> find spring value:{}", field);
                    Value value = field.getAnnotation(Value.class);
                    helper.extractPlaceholderKeys(value.value()).forEach(
                            key -> {
                                SpringValue springValue = new SpringValue(bean, beanName, key, value.value(), field);
                                VALUE_HOLDER.add(key, springValue);
                            });
                }
        );
        return bean;
    }

//    @EventListener
//    public void onChange(EnvironmentChangeEvent event) {
//
//    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        event.getKeys().forEach(
                key -> {
                    log.info("[HBCONFIG] ==> on change:{}", key);
                    List<SpringValue> springValues = VALUE_HOLDER.get(key);
                    if (Objects.isNull(springValues) || springValues.isEmpty()) {
                        return;
                    }
                    springValues.forEach(
                            springValue -> {
                                try {
                                    log.info("[HBCONFIG] ==> resolve springValue:{}", springValue);
                                    Object value = helper.resolvePropertyValue((ConfigurableBeanFactory) beanFactory,
                                            springValue.getBeanName(), springValue.getPlaceholder());
                                    springValue.getField().setAccessible(true);
                                    log.info("[HBCONFIG] ==> resolve value:{}", value);
                                    springValue.getField().set(springValue.getBean(), value);

                                } catch (IllegalAccessException e) {
                                    log.error("[HBCONFIG] ==> resolve spring value error", e);
                                }
                            }
                    );
                }
        );
    }


}
