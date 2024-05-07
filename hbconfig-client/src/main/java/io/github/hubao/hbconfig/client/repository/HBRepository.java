package io.github.hubao.hbconfig.client.repository;

import io.github.hubao.hbconfig.client.config.ConfigMeta;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public interface HBRepository {

    static HBRepository getDefault(ApplicationContext applicationContext, ConfigMeta meta) {
        return new HBRepositoryImpl(applicationContext, meta);
    }

    Map<String, String> getConfig();
    void addListener(HBRepositoryChangeListener listener);

}
