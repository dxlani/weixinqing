package me.mymilkbottles.weixinqing.service;

import com.sun.glass.ui.View;
import me.mymilkbottles.weixinqing.model.User;
import me.mymilkbottles.weixinqing.model.ViewObject;
import me.mymilkbottles.weixinqing.model.Weibo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/07/27 15:04.
 */
@Service
public class SearchService implements InitializingBean{

    @Autowired
    UserService userService;

    @Autowired
    WeiboService weiboService;

    @Value("${solr.url}")
    private String SOLR_URL;

    public static HttpSolrClient client;

    public static final String WEIBO_USERNAME_FIELD = "weibo_username";

    public static final String WEIBO_CONTENT_FIELD = "weibo_content";

    public ViewObject searchWeibo(String keyword, int offset, int count)
            throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        query.set("df", WEIBO_CONTENT_FIELD);
        query.set("hl.fl", WEIBO_CONTENT_FIELD);
        query.set("hl.preserveMulti", true);
        query.set("hl.fragsize", 0);

        QueryResponse response = client.query(query);

        ViewObject viewObject = new ViewObject();
        List<Weibo> weiboList = new ArrayList<>(count);
        List<User> userList = new ArrayList<>(count);

        if (response != null && response.getHighlighting() != null) {
            for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
                int weiboId = Integer.parseInt(entry.getKey());
                Weibo weibo = weiboService.getWeiboById(weiboId);

                User user = new User();
                user.setId(weibo.getMasterId());
                user.setUsername(entry.getValue().get(WEIBO_CONTENT_FIELD).get(1));

                weibo.setContent(entry.getValue().get(WEIBO_CONTENT_FIELD).get(0));

                weiboList.add(weibo);
                userList.add(user);
            }
        }
        viewObject.add("weibos", weiboList);
        viewObject.add("users", userList);
        return viewObject;
    }

    public List<User> searchUsername(String keyword, int offset, int count)
            throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        query.set("df", WEIBO_USERNAME_FIELD);
        query.set("hl.fl", WEIBO_USERNAME_FIELD);
        QueryResponse response = client.query(query);

        List<User> userList = new ArrayList<>(count);
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            int userId = Integer.parseInt(entry.getKey());
            User user = userService.getUserById(userId);
            user.setUsername(entry.getValue().get(WEIBO_USERNAME_FIELD).get(0));
            userList.add(user);
        }
        return userList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(SOLR_URL);
        client = new HttpSolrClient.Builder(SOLR_URL).build();
    }
}
