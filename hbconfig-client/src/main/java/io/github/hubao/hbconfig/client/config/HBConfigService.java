package io.github.hubao.hbconfig.client.config;

import io.github.hubao.hbconfig.client.repository.HBRepository;
import io.github.hubao.hbconfig.client.repository.HBRepositoryChangeListener;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/2 20:44
 */
public interface HBConfigService extends HBRepositoryChangeListener {

    static HBConfigService getDefault(ApplicationContext applicationContext, ConfigMeta meta) {
        HBRepository repository = HBRepository.getDefault(applicationContext, meta);
        Map<String, String> config = repository.getConfig();
        HBConfigService configService = new HBConfigServiceImpl(applicationContext, config);
        repository.addListener(configService);
        return configService;
    }

    String[] getPropertyNames();

    String getProperty(String name);
}
