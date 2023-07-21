package org.jeecg.modules.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.activiti.entity.ActZprocess;

public interface ActZprocessMapper extends BaseMapper<ActZprocess> {

  List<ActZprocess> selectNewestProcess(@Param("processKey") String processKey);
}
