package org.jeecg.modules.activiti.listener;

import java.util.List;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.activiti.entity.ActivitiConstant;
import org.jeecg.modules.activiti.handler.SimpleActBusinessService;
import org.jeecg.modules.activiti.mapper.ActNodeMapper;
import org.jeecg.modules.activiti.support.MessageSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CreateUserTaskListener implements TaskListener {

  final Logger logger = LoggerFactory.getLogger(CreateUserTaskListener.class);

  @Autowired private ActNodeMapper actNodeMapper;

  @Autowired private SimpleActBusinessService actBusinessService;

  @Autowired ISysBaseAPI sysBaseAPI;

  @Autowired private MessageSupport messageSupport;

  @Autowired @Lazy
  // 采用延时加载，待使用的时候注入进去
  private RuntimeService runtimeService;

  @Override
  public void notify(DelegateTask delegateTask) {
    // 可以从关联的业务表里面，拿到用户信息，然后设置候选人即可
    logger.info("process defineId:{}", delegateTask.getProcessDefinitionId());
    logger.info("process instanceId:{}", delegateTask.getProcessInstanceId());
    logger.info("process executionId:{}", delegateTask.getExecutionId());
    logger.info("process node DefineKey:{}", delegateTask.getTaskDefinitionKey());

    // 所有的节点
    List<LoginUser> userByNodeIdAndPdefId =
        actNodeMapper.findUserByNodeIdAndPdefId(
            delegateTask.getTaskDefinitionKey(), delegateTask.getProcessDefinitionId());

    // 业务数据
    ActBusiness actBusiness =
        actBusinessService
            .lambdaQuery()
            .eq(ActBusiness::getId, delegateTask.getExecution().getProcessBusinessKey())
            .one();

    if (CollectionUtils.isEmpty(userByNodeIdAndPdefId)) {
      this.suspendProcess(delegateTask.getProcessInstanceId(), actBusiness, delegateTask);
    }

    // 当前登录人
    LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

    // 设置候选人
    userByNodeIdAndPdefId.stream()
        .forEach(
            toUser -> {
              delegateTask.addCandidateUser(toUser.getUsername());
              messageSupport.sendMessage(
                  actBusiness.getId(),
                  loginUser,
                  toUser,
                  ActivitiConstant.MESSAGE_TODO_CONTENT,
                  "您有一个任务待审批，请尽快处理！",
                  true,
                  false,
                  false);
            });
  }

  private void suspendProcess(String procInstId, ActBusiness actBusiness, DelegateTask task) {
    // 当前节点没有候选人，流程终止
    runtimeService.deleteProcessInstance(procInstId, "canceled-审批节点未分配审批人，流程自动中断取消");
    actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
    actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
    actBusinessService.updateById(actBusiness);
    // 修改业务表的流程字段
    logger.error("流程未分配，该节点未分配，受理人");
    actBusinessService.updateBusinessStatus(
        actBusiness.getTableName(),
        actBusiness.getTableId(),
        "审批异常-" + task.getTaskDefinitionKey() + "-" + task.getName());
  }
}
