package org.jeecg.modules.activiti.handler;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.activiti.mapper.ActBusinessMapper;
import org.springframework.stereotype.Service;

@Service
public class SimpleActBusinessService extends ServiceImpl<ActBusinessMapper, ActBusiness> {

  /** 修改业务表的流程字段 */
  public void updateBusinessStatus(String tableName, String tableId, String actStatus) {
    try {
      baseMapper.updateBusinessStatus(tableName, tableId, actStatus);
    } catch (Exception e) {
      // 业务表需要有 act_status字段，没有会报错，不管他
      // e.printStackTrace();
      log.warn(e.getMessage());
    }
  }
}
