package org.jeecg.modules.activiti.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.activiti.oa.entity.ZhLeaveOa;
import org.jeecg.modules.activiti.oa.service.IZhLeaveOaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/** @Description: jeecg-boot @Author: noodzhan@163.com @Date: 2023-07-22 @Version: V1.0 */
@Api(tags = "")
@RestController
@RequestMapping("/leave/zhLeaveOa")
@Slf4j
public class ZhLeaveOaController extends JeecgController<ZhLeaveOa, IZhLeaveOaService> {
  @Autowired private IZhLeaveOaService zhLeaveOaService;

  /**
   * 分页列表查询
   *
   * @param zhLeaveOa
   * @param pageNo
   * @param pageSize
   * @param req
   * @return
   */
  // @AutoLog(value = "zh_leave_oa-分页列表查询")
  @ApiOperation(value = "zh_leave_oa-分页列表查询", notes = "zh_leave_oa-分页列表查询")
  @GetMapping(value = "/list")
  public Result<IPage<ZhLeaveOa>> queryPageList(
      ZhLeaveOa zhLeaveOa,
      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
      HttpServletRequest req) {
    QueryWrapper<ZhLeaveOa> queryWrapper =
        QueryGenerator.initQueryWrapper(zhLeaveOa, req.getParameterMap());
    Page<ZhLeaveOa> page = new Page<ZhLeaveOa>(pageNo, pageSize);
    IPage<ZhLeaveOa> pageList = zhLeaveOaService.page(page, queryWrapper);
    return Result.OK(pageList);
  }

  /**
   * 添加
   *
   * @param zhLeaveOa
   * @return
   */
  @AutoLog(value = "zh_leave_oa-添加")
  @ApiOperation(value = "zh_leave_oa-添加", notes = "zh_leave_oa-添加")
  // @RequiresPermissions("biz:zhLeaveOa:add")
  @PostMapping(value = "/add")
  public Result<String> add(@RequestBody ZhLeaveOa zhLeaveOa) {
    zhLeaveOaService.save(zhLeaveOa);
    return Result.OK("添加成功！");
  }

  /**
   * 编辑
   *
   * @param zhLeaveOa
   * @return
   */
  @AutoLog(value = "zh_leave_oa-编辑")
  @ApiOperation(value = "zh_leave_oa-编辑", notes = "zh_leave_oa-编辑")
  // @RequiresPermissions("biz:zhLeaveOa:edit")
  @RequestMapping(
      value = "/edit",
      method = {RequestMethod.PUT, RequestMethod.POST})
  public Result<String> edit(@RequestBody ZhLeaveOa zhLeaveOa) {
    zhLeaveOaService.updateById(zhLeaveOa);
    return Result.OK("编辑成功!");
  }

  /**
   * 通过id删除
   *
   * @param id
   * @return
   */
  @AutoLog(value = "zh_leave_oa-通过id删除")
  @ApiOperation(value = "zh_leave_oa-通过id删除", notes = "zh_leave_oa-通过id删除")
  // @RequiresPermissions("biz:zhLeaveOa:delete")
  @DeleteMapping(value = "/delete")
  public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
    zhLeaveOaService.removeById(id);
    return Result.OK("删除成功!");
  }

  /**
   * 批量删除
   *
   * @param ids
   * @return
   */
  @AutoLog(value = "zh_leave_oa-批量删除")
  @ApiOperation(value = "zh_leave_oa-批量删除", notes = "zh_leave_oa-批量删除")
  // @RequiresPermissions("biz:zhLeaveOa:deleteBatch")
  @DeleteMapping(value = "/deleteBatch")
  public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
    this.zhLeaveOaService.removeByIds(Arrays.asList(ids.split(",")));
    return Result.OK("批量删除成功!");
  }

  /**
   * 通过id查询
   *
   * @param id
   * @return
   */
  // @AutoLog(value = "zh_leave_oa-通过id查询")
  @ApiOperation(value = "zh_leave_oa-通过id查询", notes = "zh_leave_oa-通过id查询")
  @GetMapping(value = "/queryById")
  public Result<ZhLeaveOa> queryById(@RequestParam(name = "id", required = true) String id) {
    ZhLeaveOa zhLeaveOa = zhLeaveOaService.getById(id);
    if (zhLeaveOa == null) {
      return Result.error("未找到对应数据");
    }
    return Result.OK(zhLeaveOa);
  }

  /**
   * 导出excel
   *
   * @param request
   * @param zhLeaveOa
   */
  // @RequiresPermissions("biz:zhLeaveOa:exportXls")
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZhLeaveOa zhLeaveOa) {
    return super.exportXls(request, zhLeaveOa, ZhLeaveOa.class, "zh_leave_oa");
  }

  /**
   * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  // @RequiresPermissions("biz:zhLeaveOa:importExcel")
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
    return super.importExcel(request, response, ZhLeaveOa.class);
  }
}
