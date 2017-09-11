package me.mymilkbottles.weixinqing.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/06/23 17:52.
 */
@Component
public class BuildSensitiveWordTree implements InitializingBean {

    private static final Logger log = Logger.getLogger(BuildSensitiveWordTree.class);

    @Override
    public void afterPropertiesSet() throws Exception {

        String path = ClassUtils.getDefaultClassLoader().getResource("sensitiveWords").getPath();

        File directory = new File(path);

        String sensitiveWord = null;
//        int sensitiveWordCount = 0;

        Set<String> sensitiveWordSet = new HashSet<String>();
//        long startTime = System.nanoTime();
        for (File file : directory.listFiles()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((sensitiveWord = reader.readLine()) != null) {
                sensitiveWord = sensitiveWord.trim();
                if (!sensitiveWordSet.contains(sensitiveWord)) {
                    if (StringUtils.isNotBlank(sensitiveWord)) {
                        sensitiveWordSet.add(sensitiveWord);
                        SensitiveWordFilterUtil.addSensitiveWord(sensitiveWord);
//                        log.info("add sensitiveword " + sensitiveWord);
//                        ++sensitiveWordCount;
                    }
                }
            }
        }
//        long endTime = System.nanoTime();
//        log.info("addSensitiveWord cost:" + (endTime - startTime) / 1e9 + "s");
//        log.info("sensitiveWordCount=" + sensitiveWordCount);
    }
}
