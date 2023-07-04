package org.jeecg.modules.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.jeecg.modules.activiti.entity.ActZprocess;

/** @Description: 流程定义扩展表 @Author: pmc @Date: 2020-03-22 @Version: V1.0 */
public interface IActZprocessService extends IService<ActZprocess> {

  List<ActZprocess> queryNewestProcess(String processKey);
}
