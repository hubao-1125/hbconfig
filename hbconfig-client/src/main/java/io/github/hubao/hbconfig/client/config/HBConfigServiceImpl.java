package io.github.hubao.hbconfig.client.config;

import java.util.Map;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/2 20:48
 */
public class HBConfigServiceImpl implements HBConfigService {

    Map<String, String> config;

    public HBConfigServiceImpl(Map<String, String> config) {
        this.config = config;
    }

    @Override
    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return this.config.get(name);
    }
}
