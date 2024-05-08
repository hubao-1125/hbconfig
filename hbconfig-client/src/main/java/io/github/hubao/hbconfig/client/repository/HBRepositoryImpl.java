package io.github.hubao.hbconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import io.github.hubao.hbconfig.client.config.ConfigMeta;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Slf4j
public class HBRepositoryImpl implements HBRepository {

    ConfigMeta meta;
    Map<String, Long> versionMap = new HashMap<>();
    Map<String, Map<String, String>> configMap = new HashMap<>();
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    List<HBRepositoryChangeListener> listeners = new ArrayList<>();

    public HBRepositoryImpl(ApplicationContext applicationContext, ConfigMeta meta) {
        this.meta = meta;
        executor.scheduleWithFixedDelay(this::heartbeat, 1, 5, TimeUnit.SECONDS);
    }

    public void addListener(HBRepositoryChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public Map<String, String> getConfig() {
        String key = meta.genKey();
        if(configMap.containsKey(key)) {
            return configMap.get(key);
        }
        return findAll();
    }

    private @NotNull Map<String, String> findAll() {
        String listPath = meta.listPath();
        log.info("[HBCONFIG] list all configs from HB config server.");
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>(){});
        Map<String,String> resultMap = new HashMap<>();
        configs.forEach(c -> resultMap.put(c.getPkey(), c.getPval()));
        return resultMap;
    }

    private void heartbeat() {
        String versionPath = meta.versionPath();
        Long version = HttpUtils.httpGet(versionPath, new TypeReference<Long>() {});
        String key = meta.genKey();;
        Long oldVersion = versionMap.getOrDefault(key, -1L);
        if(version > oldVersion) { // 发生了变化了
            log.info("[HBCONFIG] current=" +version+ ", old=" + oldVersion);
            log.info("[HBCONFIG] need update new configs.");
            versionMap.put(key, version);
            Map<String, String> newConfigs = findAll();
            configMap.put(key, newConfigs);
            listeners.forEach(l -> l.onChange(new HBRepositoryChangeListener.ChangeEvent(meta, newConfigs)));
        }

    }
}
