---
title: activiti 加签和转办的实现
---
# activiti 加签和转办的实现


原始流程：  开始—》经理—》财务—–》老板—》结束

## 加签

加签：
流程经理审批完开始财务审批，但是我想在财务审批前，加一个人审批，这个人审批结束，财务在审批，然后是老板，最后结束

原理： 就是**设置任务的委托人**

```java
   @Test
    public void addOneTask() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey("bussiness2")
                .singleResult();
        taskService.delegateTask(task.getId(), "加签人1");
    }
```

加签审批通过：  
加签人完成审批,若是拒绝应如何拒绝，那就是加签人审批通过，由创建加签人这个人去拒绝，也就是加签人只能同意

```java
    @Test
    public void jiaCompleteProcess(){
        String user = "加签人1";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task taskSingle = taskService.createTaskQuery()
                .processInstanceBusinessKey("bussiness2")
                .taskAssignee(user)
                .singleResult();
        if(Objects.isNull(taskSingle)){
            System.out.println("此加签人当前节点没有审批任务");
            return;
        }
        taskService.resolveTask(taskSingle.getId());
        System.out.println("任务: bussiness2 加签人 ==> "+user+"审批完成");
    }
```

## 转办

转签：转签就是该财务审批了，但是财务不审批了，我把这个机会交给其他人去审批，其他人审批完成，直接向下走，不用回来了

和加签的区别是：加签人审批完成会回到创建加签人这，但是转办就是转办人替我审批，不用回来了

原理： **就是修改任务节点的处理人**

```java

    @Test
    public void trunTask() {
        //转签
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey("bussiness2")
                .singleResult();
        taskService.setAssignee(task.getId(), "转签人1");
    }

```

转签完成：

```java
   @Test
    public void completeProcess(){
        String user = "转签人1";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task taskSingle = taskService.createTaskQuery()
                .processInstanceBusinessKey("bussiness2")
                .taskAssignee(user)
                .singleResult();
        if(Objects.isNull(taskSingle)){
            System.out.println("此人当前节点没有审批任务");
            return;
        }
        taskService.complete(taskSingle.getId());
        System.out.println("任务: bussiness1 ==> "+user+"审批完成");
    }

```