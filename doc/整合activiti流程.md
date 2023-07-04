# 目标
集成 `activiti 5.22.0` 到 `Jeecg Boot 2.4.2`.

只集成了 mysql 5.7. 其他数据库, 请参考下文中手动集成部分.

activiti 来源:
https://github.com/happy-panda/jeecg-boot-activiti

# 安装和配置
配置过程请参考jeecgboot, 并导入 `jeecg-boot-and-activiti-mysql5.7.sql` 到数据库.

修改 `application-dev.yml`

集成成功!!!

# 效果演示
![](static/jeecg-activiti.gif)
# 适用范围
jeecg-boot: 2.4.2 (https://gitee.com/jeecg/jeecg-boot/tree/v2.4.2/)

activiti: 5.22.0 ( https://www.activiti.org/get-started )

# 集成过程
如果您不想用我集成好的成品, 那您可以手动集成.请按照下面步骤集成.

完整版请查看项目根目录下的文件: jeecgboot集成jeecg-boot-activiti.pdf

# 前端教程
将目录 `activiti` 拷贝到 `jeecg-boot-2.4.2/ant-design-vue-jeecg/src/views/` 下

在前端 `main.js` 文件中, 添加如下代码
```python
import { getAction,postFormAction ,postAction} from "@/api/manage"
Vue.prototype.postFormAction = postFormAction;
Vue.prototype.postDataAction = postAction;
Vue.prototype.getAction = getAction; 
``` 

# 后端集成
## 复制目录文件
将目录 `jeecg-boot-module-activiti` 拷贝后端根目录下

## 根目录 pom.xml 添加模块依赖
在 `modules` 下添加 `<module>jeecg-boot-module-activiti</module>`

## system 添加依赖
在 `jeecg-boot-module-system/pom.xml` 中添加

```python
<dependency>
	<groupId>org.jeecgframework.boot</groupId>
	<artifactId>jeecg-boot-module-activiti</artifactId>
	<version>2.4.0</version>
</dependency> 
```

## 启动配置
```python
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
,org.activiti.spring.boot.SecurityAutoConfiguration.class
}) 
```

## 绕过 Shiro
在 `jeecg-boot-base/jeecg-boot-base-core/src/main/java/org/jeecg/config/shiro/ShiroConfig.java` 中条件如下代码
```python
//activiti
filterChainDefinitionMap.put("/activiti/**", "anon");
filterChainDefinitionMap.put("/diagram-viewer/**", "anon");
filterChainDefinitionMap.put("/editor-app/**", "anon");
filterChainDefinitionMap.put("/jeecg-boot/activiti/**", "anon");
filterChainDefinitionMap.put("/jeecg-boot/diagram-viewer/**", "anon");
filterChainDefinitionMap.put("/jeecg-boot/editor-app/**", "anon"); 

```

## 添加 activiti 数据库
Navicat 执行 `activi.sql` , 成功后如下

## 添加菜单
```python
/*菜单  添加完菜单后需配置菜单权限*/
INSERT INTO sys_permission (id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('1238331160370012162', null, '工作流', '/activiti', 'layouts/RouteView', null, null, 0, null, '1', 1.1, 0, 'cluster', 1, 0, 0, 0, null, 'admin', '2020-03-13 13:08:50', null, null, 0, 0, '1', 0);
INSERT INTO sys_permission (id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
 VALUES ('1238331682929958913', '1238331160370012162', '流程模型', '/activiti/ModelList', 'activiti/ModelList', null, null, 1, null, '1', 2, 0, 'bars', 1, 1, 0, 0, null, 'admin', '2020-03-13 13:10:55', null, null, 0, 0, '1', 0);
INSERT INTO sys_permission (id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
 VALUES ('1238331682929958914', '1238331160370012162', '已发布模型', '/activiti/ProcessModelList', 'activiti/ProcessModelList', null, null, 1, null, '1', 3, 0, 'bars', 1, 1, 0, 0, null, 'admin', '2020-03-13 13:10:55', null, null, 0, 0, '1', 0);
INSERT INTO sys_permission (id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
 VALUES ('1238331682929958915', '1238331160370012162', '我的申请', '/activiti/applyList', 'activiti/applyList', null, null, 1, null, '1', 4, 0, 'bars', 1, 1, 0, 0, null, 'admin', '2020-03-13 13:10:55', null, null, 0, 0, '1', 0);
INSERT INTO sys_permission (id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
 VALUES ('1238331682929958916', '1238331160370012162', '我的待办', '/activiti/todoManage', 'activiti/todoManage', null, null, 1, null, '1', 5, 0, 'bars', 1, 1, 0, 0, null, 'admin', '2020-03-13 13:10:55', null, null, 0, 0, '1', 0);
INSERT INTO sys_permission (id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
 VALUES ('1238331682929958917', '1238331160370012162', '我的已办', '/activiti/doneManage', 'activiti/doneManage', null, null, 1, null, '1', 6, 0, 'bars', 1, 1, 0, 0, null, 'admin', '2020-03-13 13:10:55', null, null, 0, 0, '1', 0);
INSERT INTO sys_permission (id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
 VALUES ('1238331682929958918', '1238331160370012162', '进行中的流程', '/activiti/processInsManage', 'activiti/processInsManage', null, null, 1, null, '1', 7, 0, 'bars', 1, 1, 0, 0, null, 'admin', '2020-03-13 13:10:55', null, null, 0, 0, '1', 0);
INSERT INTO sys_permission (id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
 VALUES ('1238331682929958919', '1238331160370012162', '已结束流程', '/activiti/processFinishManage', 'activiti/processFinishManage', null, null, 1, null, '1', 8, 0, 'bars', 1, 1, 0, 0, null, 'admin', '2020-03-13 13:10:55', null, null, 0, 0, '1', 0);
INSERT INTO sys_permission (id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('1238331682929958920', '1238331160370012162', '所有流程', '/activiti/applyHome', 'activiti/applyHome', null, null, 1, null, '1', 1, 0, 'bars', 1, 1, 0, 0, null, 'admin', '2020-03-13 13:10:55', null, null, 0, 0, '1', 0);
 
```

## 菜单授权


## activiti 配置
app-cfg.js
```python
ACTIVITI.CONFIG = {
	// 'contextRoot' : '/activiti-explorer/service',
	'contextRoot' : 'http://localhost:8080/jeecg-boot/activiti/activitiService',
};
``` 


# 报错
## Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set
不知道修改了哪里, 导致 hibernate 报错
[[Hibernate问题排查]]
```python
spring:
  jpa:
    open-in-view: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect  
```

## 工作流编辑界面异常
![[Pasted image 20220330152956.png]]
app-cfg.js
```python
 ACTIVITI.CONFIG = {
	// 'contextRoot' : '/activiti-explorer/service',
	'contextRoot' : 'http://localhost:8080/jeecg-boot/activiti/activitiService',
};
```

## token为空
权限问题
ShiroConfig.java
```python
filterChainDefinitionMap.put("/activiti/**", "anon");
filterChainDefinitionMap.put("/diagram-viewer/**", "anon");
filterChainDefinitionMap.put("/editor-app/**", "anon");
filterChainDefinitionMap.put("/jeecg-boot/activiti/**", "anon");
filterChainDefinitionMap.put("/jeecg-boot/diagram-viewer/**", "anon");
filterChainDefinitionMap.put("/jeecg-boot/editor-app/**", "anon"); 
```
# 成功演示
![](static/jeecg-activiti.gif)



## 数据库

执行db目录下脚本：

db/create/activiti.mysql.create.engine.sql
db/create/activiti.mysql.create.history.sql
db/create/activiti.mysql.create.identity.sql

db/update.sql


## 后台

详细见提交记录