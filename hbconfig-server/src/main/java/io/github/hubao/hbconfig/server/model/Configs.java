package io.github.hubao.hbconfig.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/4/27 20:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configs {

    private String app;
    private String env;
    private String ns;
    private String pkey;
    private String pValue;
}
