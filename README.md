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
