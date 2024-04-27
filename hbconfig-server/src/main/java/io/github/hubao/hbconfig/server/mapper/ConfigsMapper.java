package io.github.hubao.hbconfig.server.mapper;

import io.github.hubao.hbconfig.server.model.Configs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/4/27 20:21
 */
@Repository
@Mapper
public interface ConfigsMapper {

    @Select("select * from configs where app = #{app} and env = #{env} and ns = #{ns}")
    List<Configs> list(String app, String env, String ns);

    @Select("select * from configs where app = #{app} and env = #{env} and ns = #{ns} and pkey = #{pkey}")
    Configs select(String app, String env, String ns, String pkey);

    @Insert("insert into configs(app, env, ns, pkey, pvalue) values(#{app}, #{env}, #{ns}, #{pkey}, #{pValue})")
    void insert(Configs configs);

    @Update("update configs set pvalue = #{pValue} where app = #{app} and env = #{env} and ns = #{ns} and pkey = #{pkey}")
    void update(Configs configs);
}
