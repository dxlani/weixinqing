package me.mymilkbottles.weixinqing.controller;

import me.mymilkbottles.weixinqing.model.ViewObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/06/20 11:47.
 */
@Controller
public class MessageController {

    @RequestMapping("/getadvice")
    @ResponseBody
    public ViewObject getAdvice() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewObject viewObject = new ViewObject();

        viewObject.add("atmeNumber", 10);
        viewObject.add("commentNumber", 20);
        viewObject.add("assistNumber", 30);
        viewObject.add("unfocusNumber", 40);
        viewObject.add("privateMsgNumber", 5000);

        return viewObject;
    }
}
