package me.mymilkbottles.weixinqing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DataAnalysisController {

    @RequestMapping("/user/admin/analysis")
    public String dataAnalysis(HttpServletRequest request) {
        return "analysis";
    }
}
