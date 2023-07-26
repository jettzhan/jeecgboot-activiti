# 与jeecg-boot整合方法


## 1、引入依赖

在jeecg-system-start的pom.xml引入依赖

```xml
<dependency>
  <groupId>com.noodb.jeecg</groupId>
  <artifactId>jeecg-boot-activiti</artifactId>
  <version>3.4.4</version>
</dependency>
```

## 2、配置启动类

在jeecg-system-start的启动类JeecgSystemApplication，添加下面配置

```text
@EnableAutoConfiguration(
exclude = {
org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
org.springframework.boot.actuate.autoconfigure.security.servlet
.ManagementWebSecurityAutoConfiguration.class,
org.activiti.spring.boot.SecurityAutoConfiguration.class
})
```

## 3、添加访问权限

在这个类的方法ShiroConfig#shiroFilter()，添加下面配置
```text
        //activiti
        filterChainDefinitionMap.put("/activiti/**", "anon");
        filterChainDefinitionMap.put("/bpm/**/*", "anon");
```


## 代码生成的form表单，页面接口，怎么结合审批流

1、生成代码后，在src/views/activiti/mixins/activitiMixin.js 添加表单

````text
 allFormComponent:function(){
      return [
          {
            text:'示例表单',
            routeName:'@/views/activiti/form/demoForm',
            component:() => import(`@/views/activiti/form/demoForm`),
            businessTable:'test_demo'
          },
          {
            text:'请假oa表单',
            routeName:'@/views/activiti/form/leaveOaForm',
            component:() => import(`@/views/activiti/form/leaveOaForm`),
            businessTable:'zh_leave_oa'
          },
         {
          text:'请假代码生成表单',
          routeName:'@/views/leave/modules/ZhLeaveOaForm',
          component:() => import(`@/views/leave/modules/ZhLeaveOaForm`),
          businessTable:'zh_leave_oa'
        }
      ]
````
2、 将审批流和表单业务进行绑定。

