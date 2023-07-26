package org.jeecg.modules.activiti.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.activiti.entity.ActZprocess;
import org.jeecg.modules.activiti.entity.ActivitiConstant;
import org.jeecg.modules.activiti.entity.CommonApplyDTO;
import org.jeecg.modules.activiti.service.IActBusinessService;
import org.jeecg.modules.activiti.service.IActZprocessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actCommon")
@Slf4j
@Transactional
@Api(tags = "流程")
public class ActCommonController {

  @Autowired IActZprocessService iActZprocessService;

  @Autowired IActBusinessService actBusinessService;

  // 通用审批接口
  @AutoLog(value = "流程-提交申请 启动流程")
  @ApiOperation(value = "流程-提交申请 启动流程", notes = "提交申请 启动流程。")
  @RequestMapping(value = "/commonApply", method = RequestMethod.POST)
  public Result apply(CommonApplyDTO commonApplyDTO) {
    // 根据当前表名，获取是否绑定到审批流
    List<ActZprocess> list =
        iActZprocessService
            .lambdaQuery()
            .eq(ActZprocess::getBusinessTable, commonApplyDTO.getTableName())
            .orderBy(true, false, ActZprocess::getVersion)
            .list();
    if (CollectionUtils.isEmpty(list)) {
      return Result.error("当前表没有绑定审批流，请联系工作人员");
    }
    ActZprocess actZprocess = list.get(0);
    ActBusiness actBusiness = new ActBusiness();
    actBusiness.setTitle(actZprocess.getDescription());
    actBusiness.setProcDefId(actZprocess.getId());
    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    String username = sysUser.getUsername();
    actBusiness.setUserId(username);
    actBusiness.setTableName(commonApplyDTO.getTableName());
    actBusiness.setTableId(commonApplyDTO.getTableId());
    actBusiness.setResult(ActivitiConstant.STATUS_TO_APPLY);
    actBusinessService.save(actBusiness);
    iActZprocessService.startProcessAndUpdateActBusiness(actBusiness);
    return Result.ok("已提交审批");
  }
}
