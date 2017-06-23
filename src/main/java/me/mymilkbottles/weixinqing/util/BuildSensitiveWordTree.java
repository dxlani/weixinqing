package me.mymilkbottles.weixinqing.util;

import me.mymilkbottles.weixinqing.util.LogUtil;
import me.mymilkbottles.weixinqing.util.SensitiveWordFilterUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/06/23 17:52.
 */
@Component
public class BuildSensitiveWordTree implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {

        String path = ClassUtils.getDefaultClassLoader().getResource("sensitiveWords").getPath();

        File directory = new File(path);

        String sensitiveWord = null;
        int sensitiveWordCount = 0;

        Set<String> sensitiveWordSet = new HashSet<String>();
        long startTime = System.nanoTime();
        for (File file : directory.listFiles()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((sensitiveWord = reader.readLine()) != null) {
                sensitiveWord = sensitiveWord.trim();
                if (!sensitiveWordSet.contains(sensitiveWord)) {
                    if (StringUtils.isNotBlank(sensitiveWord)) {
                        sensitiveWordSet.add(sensitiveWord);
                        SensitiveWordFilterUtil.addSensitiveWord(sensitiveWord);
                        LogUtil.info("add sensitiveword " + sensitiveWord);
                        ++sensitiveWordCount;
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        LogUtil.info("addSensitiveWord cost:" + (endTime - startTime) / 1e9 + "s");
        LogUtil.info("sensitiveWordCount=" + sensitiveWordCount);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
