package me.mymilkbottles.weixinqing.alone.async.handler;

import me.mymilkbottles.weixinqing.alone.async.Event;
import me.mymilkbottles.weixinqing.alone.async.EventModel;
import me.mymilkbottles.weixinqing.alone.service.SearchService;
import me.mymilkbottles.weixinqing.alone.util.EntityType;
import org.apache.log4j.Logger;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2017/06/13 13:35.
 */
@Component
public class IndexHandler implements Event {

    private static final Logger log = Logger.getLogger(IndexHandler.class);

    @Override
    public Boolean doHandler(EventModel eventModel) {

        int eventId = eventModel.getEventId();
        String data = String.valueOf(eventModel.getExt("data"));
        SolrInputDocument doc =  new SolrInputDocument();

        doc.setField("id", eventId);

        if (EntityType.INDEX_USERNAME.getValue() == eventModel.getEntityType().getValue()) {
            doc.setField(SearchService.WEIBO_USERNAME_FIELD, data);
        } else {
            doc.setField(SearchService.WEIBO_CONTENT_FIELD, data);
        }

        UpdateResponse response = null;
        try {
            response = SearchService.client.add(doc, 1000);
        } catch (SolrServerException e) {
            log.error("添加索引失败" + e.getMessage());
            return Boolean.FALSE;
        } catch (IOException e) {
            log.error("添加索引失败" + e.getMessage());
            return Boolean.FALSE;
        }
        return response != null && response.getStatus() == 0;

    }

    @Override
    public List<EntityType> getSupportEntityType() {

        return Arrays.asList(new EntityType[]{EntityType.INDEX_CONTENT, EntityType.INDEX_USERNAME});
    }
}
