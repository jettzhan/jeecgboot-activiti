---
title: activiti 并行会签的实现方式
---
# activiti 并行会签的实现方式

业务背景： 主要是用于多个人同时参与审批，也可以类似于投票的机制。  

## 主要涉及到的原理

1、 用户任务节点并行多实例的配置  

mutiInstanceLoopCharacteristics

```xml
    <bpmn:userTask id="Activity_0i3ty5m" name="军师会签" activiti:assignee="${guide}">
      <bpmn:extensionElements>
        <activiti:formData>
          <activiti:formField id="comment" label="评论" type="string" />
          <activiti:formField id="imageUrl" label="图片" type="string" />
        </activiti:formData>
      </bpmn:extensionElements>
      <bpmn:incoming>Flow_08fv1t0</bpmn:incoming>
      <bpmn:outgoing>Flow_1s1312l</bpmn:outgoing>
      <bpmn:multiInstanceLoopCharacteristics activiti:collection="${guideList}" activiti:elementVariable="guide" >
        <bpmn:completionCondition xsi:type="bpmn:tFormalExpression">${nrOfActiveInstances &gt; 0}</bpmn:completionCondition>
      </bpmn:multiInstanceLoopCharacteristics>
    </bpmn:userTask>
```

2、 流程变量

${nrOfInstance} : 并行任务实例数量

${nrOfCompletedInstance} ： 已经完成数量

只有有一个能通过，那么就可以通过该节点，一票通过权。

```xml
      <bpmn:multiInstanceLoopCharacteristics activiti:collection="${guideList}" activiti:elementVariable="guide" >
        <bpmn:completionCondition xsi:type="bpmn:tFormalExpression">${nrOfActiveInstances &gt; 0}</bpmn:completionCondition>
      </bpmn:multiInstanceLoopCharacteristics>
```


## 界面配置

![img_2.png](../assets/img_2_121313134.png)


## 代码逻辑

pom依赖

```xml
    <dependencies>
        <!-- https://mvnrepository.com/artifact/org.activiti/activiti-engine -->
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-engine</artifactId>
            <version>7.0.0.RC2</version>
        </dependency>
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-image-generator</artifactId>
            <version>7.0.0.RC2</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>
    </dependencies>
```

```java
package com.github.noodzhan.activiti.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

/**
 * 并行会签
 * 主要是在bpmn2.0 模型中，用|||表示，然后 <bpmn:multiInstanceLoopCharacteristics activiti:collection="${guideList}" activiti:elementVariable="guide" />
 * 并行会签，是用户任务多实例执行，同时产生任务。
 * 只要有一个人审核就行
 * nrOfInstances 总实例数量
 * nrOfCompletedInstances 已经完成数量
 * 根据这两个流程实例就可以实现，投票实现策略。
 * 1.会签一人完成即会签完成
 *
 * ${nrOfCompletedInstances>=1}
 *
 * 2.会签完成一半及以上，流转到下一节点
 *
 * ${nrOfCompletedInstances/nrOfInstance>=0.5}
 *
 * 3.全部完成，会签完成
 *
 * ${nrOfCompletedInstances==nrOfInstance}
 *
 */
public class ParallelUserTaskDemo {


  public static class ActivitiConfig extends StandaloneProcessEngineConfiguration {

    public ActivitiConfig() {
      this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_TRUE;
      this.jdbcUrl = "jdbc:mysql://localhost:3306/practitioner?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true";
      this.jdbcDriver = "com.mysql.cj.jdbc.Driver";
      this.jdbcUsername = "root";
      this.jdbcPassword = "123456";
    }
  }

  static ProcessEngine acquireProcessEngine() {
    GetDefaultEngine.ActivitiConfig activitiConfig = new GetDefaultEngine.ActivitiConfig();
    ProcessEngine processEngine = activitiConfig.buildProcessEngine();
    return processEngine;
  }

  /**
   * 部署bpm文件 只会操作 ACT_GE_BYTEARRAY、ACT_RE_DEPLOYMENT ACT_GE_BYTEARRAY 表是存储bpmn的字节，通用的流程定义和流程资源
   * ACT_RE_DEPLOYMENT 表是部署单元信息。
   *
   * @throws FileNotFoundException
   */
  static Deployment deploy() throws FileNotFoundException {
    ProcessEngine processEngine = acquireProcessEngine();
    RepositoryService repositoryService = processEngine.getRepositoryService();
    DeploymentBuilder deployment = repositoryService.createDeployment();
    Deployment leaveKey = deployment.addInputStream("Process_parallel_signature_nr.bpmn",
            new FileInputStream("activiti/src/main/resources/process/Process_parallel_signature_nr.bpmn"))
        .key("serial_co_nr")
        .deploy();
    return leaveKey;
  }

  static Deployment deployQuery(ProcessEngine processEngine) {
    Deployment deployment = processEngine.getRepositoryService().createDeploymentQuery()
        .singleResult();
    return deployment;
  }

  static ProcessDefinition processDefinitionQuery(String deploymentId,
      ProcessEngine processEngine) {
    ProcessDefinition processDefinition = processEngine.getRepositoryService()
        .createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
    return processDefinition;
  }

  static ProcessInstance startLeaveInstance(ProcessEngine processEngine, String processDefineId) {
    Map<String, Object> variable = new HashMap<>(10);
    List<String> guideList = new ArrayList<>();
    guideList.add("junshi1");
    guideList.add("junshi2");
    variable.put("guideList", guideList);
    variable.put("leader", "zhugong");
    variable.put("general", "zhangsan");
    ProcessInstance processInstance = processEngine.getRuntimeService()
        .startProcessInstanceById(processDefineId, variable);
    return processInstance;
  }

  //获取实时运行的图片
  public static void acquireImage(ProcessEngine processEngine, String instanceId)
      throws IOException {
    ProcessInstance processInstance = processEngine.getRuntimeService()
        .createProcessInstanceQuery().processInstanceId(instanceId).singleResult();

    BpmnModel bpmnModel = processEngine.getRepositoryService()
        .getBpmnModel(processInstance.getProcessDefinitionId());

    List<String> activeActivityIds = processEngine.getRuntimeService()
        .getActiveActivityIds(processInstance.getId());

    DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator();

    InputStream inputStream = defaultProcessDiagramGenerator.generateDiagram(bpmnModel,
        activeActivityIds, new ArrayList<>(), "宋体", "宋体", "宋体", true);

    saveToFile(inputStream, "a.svg");
  }

  public static void saveToFile(InputStream inputStream, String filePath) throws IOException {
    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
  }


  public static void main(String[] args) throws IOException {
    ProcessEngine processEngine = acquireProcessEngine();
//    Deployment deploy = deploy();
//    ProcessDefinition processDefinition = processDefinitionQuery(deploy.getId(), processEngine);
//    System.out.println("流程定义：" + processDefinition.getId());
//
//    //开始实例
//    ProcessInstance processInstance = startLeaveInstance(processEngine,
//        processDefinition.getId());
//
//    System.out.println("流程实例：" + processInstance.getId());
//
//    String processDefinitionId = processDefinition.getId();
//    String instanceId = processInstance.getId();

    String processDefinitionId = "Process_parallel_signature_nr:2:60003";
    String instanceId = "62501";


    acquireImage(processEngine, instanceId);

    processEngine.getTaskService().createTaskQuery().processDefinitionId(processDefinitionId)
        .taskCandidateOrAssigned("zhangsan").list().forEach(task -> {

          System.out.println(task.toString());
          processEngine.getTaskService().complete(task.getId());
        });

    System.out.println("-------军师1----------");

    printCurrentNodeVariables(processEngine, instanceId);

    processEngine.getTaskService().createTaskQuery().processDefinitionId(processDefinitionId)
        .taskCandidateOrAssigned("junshi1").list().forEach(task -> {
          System.out.println(task.toString());

          processEngine.getTaskService().complete(task.getId());
        });

    System.out.println("-------军师2----------");

    printCurrentNodeVariables(processEngine, instanceId);


    processEngine.getTaskService().createTaskQuery().processDefinitionId(processDefinitionId)
        .taskCandidateOrAssigned("junshi2").list().forEach(task -> {
          System.out.println(task.toString());
//          processEngine.getTaskService().complete(task.getId());
        });

    System.out.println("-------主公----------");

    printCurrentNodeVariables(processEngine, instanceId);


    processEngine.getTaskService().createTaskQuery().processDefinitionId(processDefinitionId)
        .taskCandidateOrAssigned("zhugong").list().forEach(task -> {
          System.out.println(task.toString());
//          processEngine.getTaskService().complete(task.getId());
        });


  }

  private static void printCurrentNodeVariables(ProcessEngine processEngine, String instanceId) {

    Map<String, Object> variables = processEngine.getRuntimeService()
        .getVariables(instanceId);

    System.out.println(variables);
  }


}

```

## 完整的bpmn2.0

```xml
<?xml version="1.0" encoding="UTF-8"?>
<bpmn:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:activiti="http://activiti.org/bpmn" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" id="Definitions_1" targetNamespace="http://bpmn.io/schema/bpmn">
  <bpmn:process id="Process_parallel_signature" name="并行会签" isExecutable="true">
    <bpmn:startEvent id="StartEvent_1">
      <bpmn:outgoing>Flow_03ekrgt</bpmn:outgoing>
    </bpmn:startEvent>
    <bpmn:sequenceFlow id="Flow_03ekrgt" sourceRef="StartEvent_1" targetRef="Activity_0kw63a3" />
    <bpmn:userTask id="Activity_0kw63a3" name="提交申请" activiti:assignee="${general}">
      <bpmn:extensionElements>
        <activiti:formData>
          <activiti:formField id="textValue" label="文本" type="string" />
          <activiti:formField id="numValue" label="数字" type="long" />
        </activiti:formData>
      </bpmn:extensionElements>
      <bpmn:incoming>Flow_03ekrgt</bpmn:incoming>
      <bpmn:outgoing>Flow_08fv1t0</bpmn:outgoing>
    </bpmn:userTask>
    <bpmn:sequenceFlow id="Flow_08fv1t0" sourceRef="Activity_0kw63a3" targetRef="Activity_0i3ty5m" />
    <bpmn:userTask id="Activity_0i3ty5m" name="军师会签" activiti:assignee="${guide}">
      <bpmn:extensionElements>
        <activiti:formData>
          <activiti:formField id="comment" label="评论" type="string" />
          <activiti:formField id="imageUrl" label="图片" type="string" />
        </activiti:formData>
      </bpmn:extensionElements>
      <bpmn:incoming>Flow_08fv1t0</bpmn:incoming>
      <bpmn:outgoing>Flow_1s1312l</bpmn:outgoing>
      <bpmn:multiInstanceLoopCharacteristics activiti:collection="${guideList}" activiti:elementVariable="guide" />
    </bpmn:userTask>
    <bpmn:sequenceFlow id="Flow_1s1312l" sourceRef="Activity_0i3ty5m" targetRef="Activity_0bqfyz9" />
    <bpmn:userTask id="Activity_0bqfyz9" name="主公审批" activiti:assignee="${leader}">
      <bpmn:extensionElements>
        <activiti:formData>
          <activiti:formField id="comment" label="评论" type="string" />
          <activiti:formField id="imageUrl" label="图片" type="string" />
        </activiti:formData>
      </bpmn:extensionElements>
      <bpmn:incoming>Flow_1s1312l</bpmn:incoming>
      <bpmn:outgoing>Flow_1be8hpj</bpmn:outgoing>
    </bpmn:userTask>
    <bpmn:endEvent id="Event_1ktotjy">
      <bpmn:incoming>Flow_1be8hpj</bpmn:incoming>
    </bpmn:endEvent>
    <bpmn:sequenceFlow id="Flow_1be8hpj" sourceRef="Activity_0bqfyz9" targetRef="Event_1ktotjy" />
  </bpmn:process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_parallel_signature">
      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">
        <dc:Bounds x="173" y="102" width="36" height="36" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Activity_0fr6gx9_di" bpmnElement="Activity_0kw63a3">
        <dc:Bounds x="260" y="80" width="100" height="80" />
        <bpmndi:BPMNLabel />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Activity_1vjnyn0_di" bpmnElement="Activity_0i3ty5m">
        <dc:Bounds x="420" y="80" width="100" height="80" />
        <bpmndi:BPMNLabel />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Activity_1wiqx2g_di" bpmnElement="Activity_0bqfyz9">
        <dc:Bounds x="580" y="80" width="100" height="80" />
        <bpmndi:BPMNLabel />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="Event_1ktotjy_di" bpmnElement="Event_1ktotjy">
        <dc:Bounds x="742" y="102" width="36" height="36" />
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="Flow_03ekrgt_di" bpmnElement="Flow_03ekrgt">
        <di:waypoint x="209" y="120" />
        <di:waypoint x="260" y="120" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_08fv1t0_di" bpmnElement="Flow_08fv1t0">
        <di:waypoint x="360" y="120" />
        <di:waypoint x="420" y="120" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_1s1312l_di" bpmnElement="Flow_1s1312l">
        <di:waypoint x="520" y="120" />
        <di:waypoint x="580" y="120" />
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="Flow_1be8hpj_di" bpmnElement="Flow_1be8hpj">
        <di:waypoint x="680" y="120" />
        <di:waypoint x="742" y="120" />
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</bpmn:definitions>

```