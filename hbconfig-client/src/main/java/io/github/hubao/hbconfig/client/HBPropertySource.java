package io.github.hubao.hbconfig.client;

import io.github.hubao.hbconfig.client.config.HBConfigService;
import org.springframework.core.env.EnumerablePropertySource;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/2 20:36
 */
public class HBPropertySource extends EnumerablePropertySource<HBConfigService> {


    public HBPropertySource(String name, HBConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
