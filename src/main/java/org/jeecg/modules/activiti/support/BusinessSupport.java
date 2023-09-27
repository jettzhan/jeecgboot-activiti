package org.jeecg.modules.activiti.support;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;

/**
 * 业务数据支持，主要查询业务表
 * @author <a href="noodzhan@163.com">noodzhan</a>
 * @since 2023/9/27 10:53
 */
@Component
public class BusinessSupport {
  @Autowired
  JdbcTemplate jdbcTemplate;

  public Map<String, Object> getBusinessRecord(String tableName, String id) {
    String sql = String.format("select * from %s where id = %s limit 1", tableName, id);
    return jdbcTemplate.queryForMap(sql);
  }


  public String getBusinessTitle(Map<String, Object> record) {
    if (record.keySet().contains("name")) {
      return (String) record.get("name");
    }
    return "暂无标题数据";
  }


  public String getBusinessTitle(String tableName,String id) {
    Map<String, Object> record = getBusinessRecord(tableName, id);
    if (record.keySet().contains("name")) {
      return (String) record.get("name");
    }
    return "暂无标题数据";
  }


}
