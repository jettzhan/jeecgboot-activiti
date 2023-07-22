package org.jeecg.modules.activiti.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/** @Description: zh_leave_oa @Author: noodzhan@163.com @Date: 2023-07-22 @Version: V1.0 */
@Data
@TableName("zh_leave_oa")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "", description = "")
public class ZhLeaveOa implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 主键 */
  @TableId(type = IdType.ASSIGN_ID)
  @Excel(name = "主键", width = 15)
  @ApiModelProperty(value = "主键")
  private String id;

  /** 创建人登录名称 */
  @Excel(name = "创建人登录名称", width = 15)
  @ApiModelProperty(value = "创建人登录名称")
  private String createBy;

  /** 创建日期 */
  @Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @ApiModelProperty(value = "创建日期")
  private Date createTime;

  /** 更新人登录名称 */
  @Excel(name = "更新人登录名称", width = 15)
  @ApiModelProperty(value = "更新人登录名称")
  private String updateBy;

  /** 更新日期 */
  @Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @ApiModelProperty(value = "更新日期")
  private Date updateTime;

  /** 标题 */
  @Excel(name = "标题", width = 15)
  @ApiModelProperty(value = "标题")
  private String name;

  /** 请假天数 */
  @Excel(name = "请假天数", width = 15)
  @ApiModelProperty(value = "请假天数")
  private Integer leaveDay;

  /** 请假理由 */
  @Excel(name = "请假理由", width = 15)
  @ApiModelProperty(value = "请假理由")
  private String reason;

  /** 审批流状态 */
  @Excel(name = "审批流状态", width = 15)
  @ApiModelProperty(value = "审批流状态")
  private String actStatus;

  /** 所属部门 */
  @Excel(name = "所属部门", width = 15)
  @ApiModelProperty(value = "所属部门")
  private String sysOrgCode;
}
