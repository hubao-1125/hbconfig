package io.github.hubao.hbconfig.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/2 20:48
 */
@Slf4j
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
        Set<String> keys = calcChangedKeys(this.config, event.config());
        if (keys.isEmpty()) {
            log.info("[HBCONFIG] no changed keys");
            return;
        }

        this.config = event.config();
        if(!config.isEmpty()) {
            log.info("[HBCONFIG] fire an EnvironmentChangeEvent with keys:{}", config.keySet());
            applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
        }
    }

    private Set<String> calcChangedKeys(Map<String, String> oldConfigs, Map<String, String> newConfigs) {

        if (oldConfigs.isEmpty()) {
            return newConfigs.keySet();
        }
        if (newConfigs.isEmpty()) {
            return oldConfigs.keySet();
        }

        Set<String> news = newConfigs.keySet().stream().filter(
                key -> !newConfigs.get(key).equals(oldConfigs.get(key))
                ).collect(Collectors.toSet());
        log.info("news :{}", news);

        oldConfigs.keySet().stream().filter(key -> !newConfigs.containsKey(key)).forEach(news::add);
        return news;
    }
}
