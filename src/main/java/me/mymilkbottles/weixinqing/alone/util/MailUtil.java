package me.mymilkbottles.weixinqing.alone.util;

import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;


/**
 * Created by Administrator on 2017/06/24 15:10.
 */
@Component
public class MailUtil {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MailUtil.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public boolean sendMail(String to, String subject, MailTemplateType type, Map<String, Object> exts) {

        log.info(javaMailSender);

        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String nick = MimeUtility.encodeText("微心情");
            InternetAddress from = new InternetAddress(nick + "<weixinqing_admin@163.com>");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            // 通过指定模板名获取FreeMarker模板实例
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mails/" + type.getType());

            // 解析模板并替换动态数据，最终content将替换模板文件中的${content}标签。
            String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, exts);

            helper.setText(htmlText, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("邮件发送失败" + e.getMessage());
            return false;
        }

        log.info("javaMailSender send a mail");
        return true;
    }


}
