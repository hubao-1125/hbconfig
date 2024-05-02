package io.github.hubao.hbconfig.client.config;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/2 20:44
 */
public interface HBConfigService {

    String[] getPropertyNames();

    String getProperty(String name);
}
