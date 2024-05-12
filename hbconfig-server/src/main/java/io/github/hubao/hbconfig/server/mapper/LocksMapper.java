package io.github.hubao.hbconfig.server.mapper;

import org.apache.ibatis.annotations.Select;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/12 20:57
 */
public interface LocksMapper {

    @Select("select app from locks where 1=1 and id=1 for update")
    String selectForUpdate();
}
