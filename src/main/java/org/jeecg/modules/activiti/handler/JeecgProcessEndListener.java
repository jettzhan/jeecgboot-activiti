package org.jeecg.modules.activiti.handler;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.activiti.entity.ActivitiConstant;
import org.jeecg.modules.activiti.support.MessageSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JeecgProcessEndListener implements ActivitiEventListener {

  private final Logger logger = LoggerFactory.getLogger(JeecgProcessEndListener.class);

  @Autowired private SimpleActBusinessService actBusinessService;

  @Autowired ISysBaseAPI sysBaseAPI;

  @Autowired private MessageSupport messageSupport;

  @Override
  public void onEvent(ActivitiEvent event) {
    if (event.getType().equals(ActivitiEventType.PROCESS_STARTED)) {
      logger.info("process start:{}", event.getProcessInstanceId());
    } else if (event.getType().equals(ActivitiEventType.PROCESS_COMPLETED)) {

      logger.info("process end:{}", event.getProcessInstanceId());
      ActBusiness actBusiness =
          actBusinessService
              .lambdaQuery()
              .eq(ActBusiness::getProcInstId, event.getProcessInstanceId())
              .one();
      // 结束流程来实现通知
      actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
      actBusiness.setResult(ActivitiConstant.RESULT_PASS);
      actBusinessService.updateById(actBusiness);
      // 异步发消息
      LoginUser user = sysBaseAPI.getUserByName(actBusiness.getUserId());
      LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
      messageSupport.sendMessage(
          actBusiness.getId(),
          loginUser,
          user,
          ActivitiConstant.MESSAGE_PASS_CONTENT,
          String.format("您的 【%s】 申请已通过！", actBusiness.getTitle()),
          true,
          false,
          false);
      // 修改业务表的流程字段
      actBusinessService.updateBusinessStatus(
          actBusiness.getTableName(), actBusiness.getTableId(), "审批通过");
    }
  }

  @Override
  public boolean isFailOnException() {
    return false;
  }
}
