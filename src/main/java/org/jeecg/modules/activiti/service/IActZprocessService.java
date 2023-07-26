package org.jeecg.modules.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.activiti.entity.ActZprocess;

public interface IActZprocessService extends IService<ActZprocess> {

  List<ActZprocess> queryNewestProcess(String processKey);

  String startProcessAndUpdateActBusiness(ActBusiness actBusiness);
}
