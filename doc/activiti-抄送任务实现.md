---
title: activiti 抄送任务的实现
---
# activiti 抄送任务的实现

第 1 种方式：
根据业务逻辑实现就行了
创建流程时根据业务id创建抄送信息，单独存表，
业务id，抄送人id，是否已读

第 2 种方式：
审批流不存在子任务时（也就是没用到 act_run_task ===> parent_task_id 这个字段时 就可以用这个字段保存业务id，当做此业务抄送）

```java
	//1 启动流程实例时调用此方法
	createSonTask("bussiness2");
	
 	/**
     * 创建子任务
     */
    public void createSonTask(String bussinessId){
        String users = "曹操,典韦,刘备";
        List<String> split = Arrays.asList(users.split(","));
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        split.forEach(e->{
            Task newtask = taskService.newTask();
            newtask.setAssignee(e);
            newtask.setName("抄送任务");
            newtask.setParentTaskId(bussinessId);//业务id
            taskService.saveTask(newtask);
        });
    }

	/**
	  * 查询当前用户全部抄送任务
	  */
     @Test
     public void findSonTask(){
         String user = "曹操";
         ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
         TaskService taskService = processEngine.getTaskService();
         List<Task> taskList = taskService.createTaskQuery().taskAssignee(user).list()
                 .stream().filter(e->StringUtils.isNotBlank(e.getParentTaskId())).collect(Collectors.toList());
         System.out.println(taskList);
     }


    /**
     * 用户已读抄送任务
     * businessId 不为空 即【已读此条消息】，
     * businessId 为空   即【全部已读】
     */
    @Test
    public void readSonTask(){
        String user = "刘备";
        String businessId = "";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list =  taskService.createTaskQuery().taskAssignee(user).list();
        for(Task task:list){
            if(!StringUtils.isBlank(businessId) && StringUtils.equals(businessId,task.getParentTaskId())){
                //已读当前任务
                taskService.complete(task.getId());
                break;
            }else {
                taskService.complete(task.getId());
            }
        }
    }
```