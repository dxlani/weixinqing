package me.mymilkbottles.weixinqing.alone.controller;

import me.mymilkbottles.weixinqing.alone.intercepter.LoginIntercepter;
import me.mymilkbottles.weixinqing.alone.model.HostHolder;
import me.mymilkbottles.weixinqing.alone.model.User;
import me.mymilkbottles.weixinqing.alone.model.ViewObject;
import me.mymilkbottles.weixinqing.alone.model.Weibo;
import me.mymilkbottles.weixinqing.alone.service.SearchService;
import me.mymilkbottles.weixinqing.alone.service.UserService;
import me.mymilkbottles.weixinqing.alone.service.WeiboService;
import me.mymilkbottles.weixinqing.alone.util.WeixinqingUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    SearchService searchService;


    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) {
        List<Weibo> weiboList = weiboService.getWeibo(0, PAGE_COUNT);
        List<ViewObject> vos = weiboService.addWeiboDetail(weiboList, PAGE_COUNT);
        model.addAttribute("vos", vos);
        return "index";
    }

    @RequestMapping("/search")
    public String search(Model model , @RequestParam("keyword") String keyword) throws IOException, SolrServerException {

        ViewObject viewObject;
        List<User> userList;
        try {
            Long.parseLong(keyword);
            viewObject = searchService.searchWeibo("", 0, PAGE_COUNT);
            userList = new ArrayList<>(1);
        } catch (Exception e) {
            viewObject = searchService.searchWeibo(keyword, 0, PAGE_COUNT);
            userList = searchService.searchUsername(keyword, 0, PAGE_COUNT);
        }

        List<ViewObject> vos = weiboService.addWeiboDetail(viewObject, PAGE_COUNT);

        model.addAttribute("vos", vos);

        model.addAttribute("users", userList);
        model.addAttribute("keyword", keyword);
        return "result";
    }

    @RequestMapping("/searchjson")
    @ResponseBody
    public ViewObject searchJson(Model model , @RequestParam("keyword") String keyword) throws IOException, SolrServerException {
        ViewObject viewObject = searchService.searchWeibo(keyword, 0, PAGE_COUNT);
        List<ViewObject> vos = weiboService.addWeiboDetail(viewObject, PAGE_COUNT);

        List<User> userList = searchService.searchUsername(keyword, 0, PAGE_COUNT);


        ViewObject vo = new ViewObject();
        vo.add("vos", vos);
        vo.add("users", userList);
        vo.add("keyword", keyword);
        return vo;
    }

    @RequestMapping(value = {"/indexJson"})
    @ResponseBody
    public List<ViewObject> indexJson() {
        List<Weibo> weiboList = weiboService.getWeibo(0, PAGE_COUNT);
        List<ViewObject> vos = weiboService.addWeiboDetail(weiboList, PAGE_COUNT);
        return vos;
    }

    @RequestMapping("/index/news/{ids}")
    @ResponseBody
    public List<ViewObject> indexNews(@PathVariable("ids") String ids) {
        int id = WeixinqingUtil.parseUserId(ids);

        if (id != -1) {
            List<Weibo> weiboList = weiboService.getWeiboBeforeId(id, PAGE_COUNT);
            return weiboService.addWeiboDetail(weiboList, PAGE_COUNT);
        }
        return null;
    }

    @RequestMapping("/img/{name}")
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
