package io.github.hubao.hbconfig.client.config;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/2 20:48
 */
public class HBConfigServiceImpl implements HBConfigService {

    Map<String, String> config;
    ApplicationContext applicationContext;

    public HBConfigServiceImpl(ApplicationContext applicationContext, Map<String, String> config) {
        this.applicationContext = applicationContext;
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

    @Override
    public void onChange(ChangeEvent event) {
        this.config = event.config();
        if(!config.isEmpty()) {
            System.out.println("[HBCONFIG] fire an EnvironmentChangeEvent with keys:" + config.keySet());
            applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
        }
    }
}
