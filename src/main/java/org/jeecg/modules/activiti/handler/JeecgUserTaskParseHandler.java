package org.jeecg.modules.activiti.handler;

import java.util.Map;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.task.TaskDefinition;
import org.jeecg.modules.activiti.listener.CreateUserTaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JeecgUserTaskParseHandler extends UserTaskParseHandler {

  @Autowired CreateUserTaskListener createUserTaskListener;

  @Override
  protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
    super.executeParse(bpmnParse, userTask);
    Map<String, Object> properties = bpmnParse.getCurrentActivity().getProperties();
    TaskDefinition taskDefinition = (TaskDefinition) properties.get("taskDefinition");
    taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, createUserTaskListener);
  }
}
