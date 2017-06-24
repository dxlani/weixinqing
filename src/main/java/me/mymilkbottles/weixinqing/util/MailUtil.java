package me.mymilkbottles.weixinqing.util;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.aspectj.weaver.bcel.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2017/06/24 11:22.
 */
@Component
public class MailUtil implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);

    private JavaMailSenderImpl mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public void sendMail(String to, String subject,
                         MailTemplateType mailTemplateType,
                         Map<String, Object> exts) {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            String nick = MimeUtility.encodeText("微心情官方通知");
            InternetAddress from = new InternetAddress(nick + "<weixinqing_admin@163.com>");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mails/" + mailTemplateType.toString());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, exts);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败" + e.getMessage());
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        Properties properties = new Properties();

        InputStream inputStream = ClassUtils.getDefaultClassLoader().getResourceAsStream("mail.properties");

        properties.load(inputStream);

        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(properties.getProperty("mail.username"));
        mailSender.setPassword(properties.getProperty("mail.password"));
        mailSender.setHost(properties.getProperty("mail.password"));
        mailSender.setProtocol(properties.getProperty("mail.protocol"));
        mailSender.setDefaultEncoding(properties.getProperty("mail.host"));

        Properties javaMailProperties = new Properties();
//        javaMailProperties.put("mails.smtp.ssl.enable", properties.getProperty("mails.smtp.ssl.enable"));
        javaMailProperties.put("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));
        javaMailProperties.put("mail.smtp.starttls.enable", properties.getProperty("mails.smtp.ssl.enable"));
        mailSender.setJavaMailProperties(javaMailProperties);
    }

}
