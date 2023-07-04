package org.jeecg.modules.activiti.support;

import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.dto.message.BusMessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MessageSupport {

  @Autowired ISysBaseAPI sysBaseAPI;

  /**
   * 发消息
   *
   * @param actBusId 流程业务id
   * @param fromUser 发送人
   * @param toUser 接收人
   * @param title 标题
   * @param msgText 信息内容
   * @param sendMessage 系统消息
   * @param sendSms 短信
   * @param sendEmail 邮件
   */
  public void sendMessage(
      String actBusId,
      LoginUser fromUser,
      LoginUser toUser,
      String title,
      String msgText,
      Boolean sendMessage,
      Boolean sendSms,
      Boolean sendEmail) {

    if (sendMessage != null && sendMessage) {
      BusMessageDTO messageDTO =
          new BusMessageDTO(
              fromUser.getUsername(), toUser.getUsername(), title, msgText, "2", "bpm", actBusId);
      sysBaseAPI.sendBusAnnouncement(messageDTO);
    }
    // todo 以下需要购买阿里短信服务；设定邮件服务账号
    if (sendSms != null && sendSms && StrUtil.isNotBlank(toUser.getPhone())) {
      // DySmsHelper.sendSms(toUser.getPhone(), obj, DySmsEnum.REGISTER_TEMPLATE_CODE)
    }
    if (sendEmail != null && sendEmail && StrUtil.isNotBlank(toUser.getEmail())) {
      JavaMailSender mailSender = (JavaMailSender) SpringContextUtils.getBean("mailSender");
      SimpleMailMessage message = new SimpleMailMessage();
      // 设置发送方邮箱地址
      //            message.setFrom(emailFrom);
      message.setTo(toUser.getEmail());
      // message.setSubject(es_title);
      message.setText(msgText);
      mailSender.send(message);
    }
  }
}
