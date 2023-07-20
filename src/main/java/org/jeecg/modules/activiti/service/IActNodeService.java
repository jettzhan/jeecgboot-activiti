package org.jeecg.modules.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.entity.ActNode;

public interface IActNodeService extends IService<ActNode> {

  // 根据流程定义id和节点id，查出配置 直接人员，角色，部门，部门负责人 所对应的用户
  List<LoginUser> findUserByNodeIdAndPdefId(String nodeId, String procDefId);
}
