package org.jeecg.config;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.parse.BpmnParseHandler;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.jeecg.modules.activiti.handler.JeecgProcessEndListener;
import org.jeecg.modules.activiti.handler.JeecgUserTaskParseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ActivitiDataSourceConfig extends AbstractProcessEngineAutoConfiguration {

  @Resource private ActivitiDataSourceProperties activitiDataSourceProperties;
  /** 直接取AutoConfig生成的数据源 */
  @Resource private DataSource dataSource;

  @Autowired private JeecgUserTaskParseHandler jeecgUserTaskParseHandler;

  @Autowired private JeecgProcessEndListener jeecgProcessEndListener;

  /*@Bean
  public DataSource activitiDataSource() {
      DruidDataSource DruiddataSource = new DruidDataSource();
      DruiddataSource.setUrl(activitiDataSourceProperties.getUrl());
      DruiddataSource.setDriverClassName(activitiDataSourceProperties.getDriverClassName());
      DruiddataSource.setPassword(activitiDataSourceProperties.getPassword());
      DruiddataSource.setUsername(activitiDataSourceProperties.getUsername());
      return DruiddataSource;
  }*/

  @Bean
  public PlatformTransactionManager transactionManager() {
    // return new DataSourceTransactionManager(activitiDataSource());
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
    SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
    // configuration.setDataSource(activitiDataSource());
    configuration.setDataSource(dataSource);
    configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    configuration.setJobExecutorActivate(true);
    configuration.setTransactionManager(transactionManager());
    configuration.setActivityFontName("宋体");
    configuration.setLabelFontName("宋体");
    configuration.setAnnotationFontName("宋体");
    // 添加自定义解析
    List<BpmnParseHandler> bpmHandler = new ArrayList<>();
    bpmHandler.add(jeecgUserTaskParseHandler);
    configuration.setCustomDefaultBpmnParseHandlers(bpmHandler);

    //    configuration.setEventListeners(Lists.newArrayList(jeecgProcessEndListener));

    Map<String, List<ActivitiEventListener>> typeEventListeners = new HashMap<>();
    typeEventListeners.put(
        ActivitiEventType.PROCESS_COMPLETED.name(), Lists.newArrayList(jeecgProcessEndListener));

    typeEventListeners.put(
        ActivitiEventType.PROCESS_STARTED.name(), Lists.newArrayList(jeecgProcessEndListener));

    configuration.setTypedEventListeners(typeEventListeners);

    // id生成器
    // configuration.setIdGenerator(new MyUUIDgenerator());
    return configuration;
  }
}
