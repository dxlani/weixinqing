package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.HostHolder;
import me.mymilkbottles.weixinqing.model.ViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/06/20 11:47.
 */
@Controller
public class MessageController {

    @Autowired
    HostHolder hostHolder;

    @RequestMapping("/getadvice")
    @ResponseBody
    public ViewObject getAdvice() {

        ViewObject viewObject = new ViewObject();
        if (hostHolder.getUser() == null) {
            viewObject.add("Status", -1);
            return viewObject;
        }
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        viewObject.add("Status", 0);
        viewObject.add("atmeNumber", 10);
        viewObject.add("commentNumber", 20);
        viewObject.add("assistNumber", 30);
        viewObject.add("unfocusNumber", 40);
        viewObject.add("privateMsgNumber", 5000);

        return viewObject;
    }
}
