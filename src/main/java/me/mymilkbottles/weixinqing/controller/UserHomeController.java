package me.mymilkbottles.weixinqing.controller;

import com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListFilter;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.model.ViewObject;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Administrator on 2017/06/22 16:10.
 */
@Controller
public class UserHomeController {

    private static final Logger log = Logger.getLogger(UserHomeController.class);

    private static final InputStream defaultHeadImg = ClassUtils.getDefaultClassLoader().getResourceAsStream("img/head-img/default.png");

    private static final int SIZE = 1024;

    private static final byte[] bytes = new byte[SIZE];


    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Value("${weixinqing.img.head-img-directory}")
    String headImgDirectory;

    @RequestMapping("/user/home/{id}")
    public String userHome(@PathVariable("id") String id, Model model) {
        int userId = WeixinqingUtil.parseUserId(id);
        if (userId == -1) {
            return "404";
        }

        User user = userService.getUserById(userId);

        ViewObject viewObject = new ViewObject();

        viewObject.add("userId", userId);
        viewObject.add("username", user.getUsername());

        model.addAttribute("vo", viewObject);
        return "user_home";
    }

    @RequestMapping("/headimg/{id}")
    public void userheadImg(@PathVariable("id") String ids,
                            HttpServletResponse response, HttpServletRequest request) {

        int idInt = WeixinqingUtil.parseUserId(ids);

        File headImg = new File(headImgDirectory + "/" + String.valueOf(idInt) + ".jpg");

        if (!headImg.exists()) {
            headImg = new File(headImgDirectory + "/" + String.valueOf(-1) + ".jpg");
        }
        ServletOutputStream responseOutputStream = null;
        InputStream userHeadimgStream = null;
        try {
            responseOutputStream = response.getOutputStream();
            userHeadimgStream = new BufferedInputStream(new FileInputStream(headImg), SIZE);
            int length = -1;
            while ((length = userHeadimgStream.read(bytes)) != -1) {
                responseOutputStream.write(bytes, 0, length);
            }
        } catch (FileNotFoundException e) {
            log.error("用户头像图片输出流失败" + e.getMessage());
        } catch (IOException e) {
            log.error("用户头像图片输出流失败" + e.getMessage());
        } finally {
            try {
                responseOutputStream.close();
                userHeadimgStream.close();
            } catch (IOException e) {
                log.error("用户头像图片输出流关闭失败" + e.getMessage());
            }
            return;
        }
    }

}
