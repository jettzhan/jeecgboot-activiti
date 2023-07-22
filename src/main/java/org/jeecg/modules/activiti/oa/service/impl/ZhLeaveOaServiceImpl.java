package org.jeecg.modules.activiti.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.activiti.oa.entity.ZhLeaveOa;
import org.jeecg.modules.activiti.oa.mapper.ZhLeaveOaMapper;
import org.jeecg.modules.activiti.oa.service.IZhLeaveOaService;
import org.springframework.stereotype.Service;

@Service
public class ZhLeaveOaServiceImpl extends ServiceImpl<ZhLeaveOaMapper, ZhLeaveOa>
    implements IZhLeaveOaService {}
