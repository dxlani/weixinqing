package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.intercepter.LoginIntercepter;
import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.model.ViewObject;
import me.mymilkbottles.weixinqing.model.Weibo;
import me.mymilkbottles.weixinqing.service.UserService;
import me.mymilkbottles.weixinqing.service.WeiboService;
import me.mymilkbottles.weixinqing.util.WeixinqingUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2017/06/18 20:53.
 */
@Controller
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(LoginIntercepter.class);

    private static final int PAGE_COUNT = 10;

    private static final int SIZE = 1024;

    private static byte[] bytes = new byte[SIZE];

    @Autowired
    HostHolder hostHolder;

    @Autowired
    WeiboService weiboService;

    @Autowired
    UserService userService;

    @Value("${weixinqing.img.weibo-directory}")
    String weiboImgDirectory;


    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) {
        List<Weibo> weiboList = weiboService.getWeibo(0, PAGE_COUNT);
        List<ViewObject> vos = weiboService.addWeiboDetail(weiboList, PAGE_COUNT);
        model.addAttribute("vos", vos);
        return "index";
    }

    @RequestMapping(value = {"/indexJson"})
    @ResponseBody
    public List<ViewObject> indexJson() {
        List<Weibo> weiboList = weiboService.getWeibo(0, PAGE_COUNT);
        List<ViewObject> vos = weiboService.addWeiboDetail(weiboList, PAGE_COUNT);
        return vos;
    }


//    @RequestMapping("/index/news/{ids}")
//    @ResponseBody
//    public ViewObject indexNew(@PathVariable("ids") String ids) {
//        int id = WeixinqingUtil.parseUserId(ids);
//        if (id == -1) {
//            log.error("indexNew函数的参数ids必须为数字类型");
//            throw new IllegalArgumentException("参数ids必须为数字类型");
//        }
//        List<Weibo> weiboList = weiboService.getWeiboBrforeId(0, PAGE_COUNT, id);
//        ViewObject vo = weiboService.addWeiboDetail(weiboList, PAGE_COUNT);
//        return vo;
//    }

    @RequestMapping("/weixinqing/img/{name}")
    public void weiboImg(@PathVariable("name") String name, HttpServletResponse response) {

        File headImg = null;
        if (StringUtils.isNotBlank(name) && name.length() <= 50) {
            headImg = new File(weiboImgDirectory + "/" + name + ".jpg");
        }

        if (headImg != null && !headImg.exists()) {
            headImg = new File(weiboImgDirectory + "/none.jpg");
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
            log.error("用户微博图片输出流失败" + e.getMessage());
        } catch (IOException e) {
            log.error("用户微博图片输出流失败" + e.getMessage());
        } finally {
            try {
                responseOutputStream.close();
                userHeadimgStream.close();
            } catch (IOException e) {
                log.error("用户微博图片输出流关闭失败" + e.getMessage());
            }
            return;
        }
    }
}
