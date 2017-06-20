package me.mymilkbottles.weixinqing.prepare;


import me.mymilkbottles.weixinqing.model.ViewObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.View;
import javax.xml.ws.RequestWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/06/19 18:38.
 */
@Controller
public class PrepareStudy {

    @RequestMapping("/study")
    public String prepare(Model model) {
        ViewObject viewObject = new ViewObject();

        viewObject.add("number", 10);

        List<String> list = new ArrayList<>();
        list.add("list1");
        list.add("list2");
        list.add("list3");
        list.add("list4");
        viewObject.add("lists", list);

        Map<String, Object> map = new HashMap<>();
        map.put("map1", "value1");
        map.put("map2", "value2");
        map.put("map3", "value3");

        model.addAttribute("vo",viewObject);

        System.out.println("prepare");
        return "prepare";
    }
}
