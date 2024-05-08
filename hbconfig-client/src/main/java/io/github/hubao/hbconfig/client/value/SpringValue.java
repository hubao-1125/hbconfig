package io.github.hubao.hbconfig.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/8 20:37
 */
@Data
@AllArgsConstructor
public class SpringValue {

    private Object bean;
    private String beanName;
    private String key;
    private String placeholder;
    private Field field;
}
