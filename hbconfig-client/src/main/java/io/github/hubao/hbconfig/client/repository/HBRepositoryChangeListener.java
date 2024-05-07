package io.github.hubao.hbconfig.client.repository;


import io.github.hubao.hbconfig.client.config.ConfigMeta;

import java.util.Map;

public interface HBRepositoryChangeListener {

    void onChange(ChangeEvent event);

    record ChangeEvent(ConfigMeta meta, Map<String, String> config) {}

}
