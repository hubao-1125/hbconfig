package io.github.hubao.hbconfig.server;

import io.github.hubao.hbconfig.server.mapper.ConfigsMapper;
import io.github.hubao.hbconfig.server.model.Configs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/4/27 20:18
 */
@RestController
@Slf4j
public class HbConfigController {

    @Autowired
    DistributedLocks distributedLocks;

    @Autowired
    ConfigsMapper configsMapper;
    Map<String, Long> VERSIONS = new HashMap<>();

    @GetMapping(value = "/list")
    public List<Configs> list(String app, String env, String ns) {
        return configsMapper.list(app, env, ns);
    }

    @PostMapping(value = "/update")
    public List<Configs> update(@RequestParam("app") String app,
                                @RequestParam("env") String env,
                                @RequestParam("ns") String ns,
                                @RequestBody Map<String, String> params) {
        log.info("params: {}", params);
        params.forEach((k, v) -> {
            insertOrUpdate(new Configs(app, env, ns, k, v));
        });
        VERSIONS.put(app + "-" + env + "-" + ns, System.currentTimeMillis());
        return configsMapper.list(app, env, ns);
    }

    private void insertOrUpdate(Configs configs) {
        Configs conf = configsMapper.select(configs.getApp(), configs.getEnv(), configs.getNs(), configs.getPkey());
        if (Objects.isNull(conf)) {
            configsMapper.insert(configs);
        } else {
            configsMapper.update(configs);
        }
    }

    @GetMapping("/version")
    public long version(String app, String env, String ns) {
        return VERSIONS.getOrDefault(app + "-" + env + "-" + ns, -1L);
    }

    @GetMapping("/status")
    public boolean status() {
        return distributedLocks.getLocked().get();
    }
}
