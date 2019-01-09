package com.radarwin.framework.elasticsearch;

import com.radarwin.framework.page.Page;
import com.radarwin.framework.page.PageList;
import com.radarwin.framework.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;

import java.util.*;

/**
 * Created by josh on 15/7/8.
 */
public class EsApi {

    private static final Logger logger = LogManager.getLogger(EsApi.class);

    private static final int defaultPageSize = 500;

    /**
     * 添加单条索引
     *
     * @param index
     * @param documentType
     * @param key
     * @param map
     * @return
     */
    public static final boolean index(String index, String documentType, String key, Map<String, Object> map) {
        IndexResponse response =
                ClientConnection.getClient().
                        prepareIndex(index, documentType, key).setSource(map).execute().actionGet();
        return response.isCreated();
    }

    /**
     * 删除单条索引
     *
     * @param index
     * @param documentType
     * @param key
     */
    public static final void delete(String index, String documentType, String key) {
        ClientConnection.getClient().prepareDelete(index, documentType, key)
                .execute()
                .actionGet();

    }

    /**
     * 查询索引是否存在
     *
     * @param index
     * @param documentType
     * @param key
     */
    public static final boolean exists(String index, String documentType, String key) {
        return ClientConnection.getClient().prepareGet()
                .setIndex(index)
                .setType(documentType)
                .setId(key)
                .setFetchSource(false).execute().actionGet().isExists();
    }

    /**
     * 获取单条索引
     *
     * @param index
     * @param documentType
     * @param key
     * @return
     */
    public static final Map<String, Object> get(String index, String documentType, String key) {
        return ClientConnection.getClient().prepareGet()
                .setIndex(index)
                .setType(documentType)
                .setId(key)
                .execute().actionGet().getSource();
    }

    /**
     * 根据索引ID更新一个字段的值
     *
     * @param index
     * @param documentType
     * @param key
     * @param field
     * @param value
     */
    public static final void update(String index, String documentType, String key, String field, Object value) throws Exception {

        UpdateRequestBuilder updateRequestBuilder = ClientConnection.getClient().prepareUpdate(index, documentType, key);
        XContentBuilder contentBuilder = XContentFactory.jsonBuilder();
        contentBuilder.startObject().field(field, value).endObject();
        updateRequestBuilder.setDoc(contentBuilder).get();
    }

    /**
     * 根据索引ID更新多个字段的值
     *
     * @param index
     * @param documentType
     * @param key
     * @param map
     */
    public static final void update(String index, String documentType, String key, Map<String, Object> map) {
        UpdateRequestBuilder updateRequestBuilder = ClientConnection.getClient().prepareUpdate(index, documentType, key);
        updateRequestBuilder.setDoc(map).get();
    }

    /**
     * 批量新增索引(默认500个分次页)
     *
     * @param index
     * @param documentType
     * @param keyName
     * @param list
     */
    public static final List<BulkItemResponse> bulkIndex(String index, String documentType, String keyName, List<Map<String, Object>> list) {
        return pageBulkIndex(index, documentType, keyName, list, defaultPageSize);
    }

    /**
     * 批量新增索引（带分页）
     *
     * @param index
     * @param documentType
     * @param keyName
     * @param list
     * @param pageSize
     * @return
     */
    public static final List<BulkItemResponse> pageBulkIndex(String index, String documentType, String keyName, List<Map<String, Object>> list, int pageSize) {
        List<BulkItemResponse> resultList = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return resultList;
        }

        if (StringUtil.isBlank(keyName)) {
            throw new RuntimeException("pageBulkIndex bulk key name is null or empty");
        }

        Client client = ClientConnection.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        int cnt = 0;

        for (Map<String, Object> map : list) {

            if (map.containsKey(keyName)) {
                bulkRequest.add(client.prepareIndex(index, documentType, String.valueOf(map.get(keyName)))
                        .setSource(map));
                cnt++;
            }

            if (cnt > 0 && bulkRequest.numberOfActions() % pageSize == 0) {
                BulkResponse bulkResponse = bulkRequest.execute().actionGet();
                addFailures(bulkResponse, resultList);
                bulkRequest = client.prepareBulk();
                cnt = 0;
            }
        }

        if (bulkRequest.numberOfActions() > 0) {
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            addFailures(bulkResponse, resultList);
        }
        return resultList;
    }

    /**
     * 批量更新索引(默认500个分次页)
     *
     * @param index
     * @param documentType
     * @param keyName
     * @param list
     */
    public static final List<BulkItemResponse> bulkUpdate(String index, String documentType, String keyName, List<Map<String, Object>> list) {
        return pageBulkUpdate(index, documentType, keyName, list, defaultPageSize);
    }

    /**
     * 批量更新索引（带分页）
     *
     * @param index
     * @param documentType
     * @param keyName
     * @param list
     * @param pageSize
     * @return
     */
    public static final List<BulkItemResponse> pageBulkUpdate(String index, String documentType, String keyName, List<Map<String, Object>> list, int pageSize) {
        List<BulkItemResponse> resultList = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return resultList;
        }

        if (StringUtil.isBlank(keyName)) {
            throw new RuntimeException("pageBulkUpdate bulk key name is null or empty");
        }

        Client client = ClientConnection.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        int cnt = 0;

        for (Map<String, Object> map : list) {

            if (map.containsKey(keyName)) {
                bulkRequest.add(client.prepareUpdate(index, documentType, String.valueOf(map.get(keyName)))
                        .setDoc(map));
                cnt++;
            }

            if (cnt > 0 && bulkRequest.numberOfActions() % pageSize == 0) {
                BulkResponse bulkResponse = bulkRequest.execute().actionGet();
                addFailures(bulkResponse, resultList);
                bulkRequest = client.prepareBulk();
                cnt = 0;
            }
        }

        if (bulkRequest.numberOfActions() > 0) {
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            addFailures(bulkResponse, resultList);
        }
        return resultList;
    }

    /**
     * 批量删除索引（带分页）
     *
     * @param index
     * @param documentType
     * @param keyList      索引ID列表
     * @param pageSize
     * @return
     */
    public static final List<BulkItemResponse> pageBulkDelete(String index, String documentType, List<String> keyList, int pageSize) {
        List<BulkItemResponse> resultList = new ArrayList<>();
        if (keyList == null || keyList.size() == 0) {
            return resultList;
        }
        Client client = ClientConnection.getClient();

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        int cnt = 0;

        for (String key : keyList) {

            bulkRequest.add(client.prepareDelete(index, documentType, key));
            cnt++;

            if (cnt > 0 && bulkRequest.numberOfActions() % pageSize == 0) {
                BulkResponse bulkResponse = bulkRequest.execute().actionGet();
                addFailures(bulkResponse, resultList);
                bulkRequest = client.prepareBulk();
                cnt = 0;
            }
        }

        if (bulkRequest.numberOfActions() > 0) {
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            addFailures(bulkResponse, resultList);
        }
        return resultList;
    }

    /**
     * 分页获取数据
     *
     * @param page
     * @param index
     * @param documentType
     * @return
     */
    public static final PageList searchPageList(Page page, String index, String documentType) {
        PageList pageList = new PageList();
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder =
                ClientConnection.getClient().prepareSearch(index).setTypes(documentType);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        SearchResponse response = searchRequestBuilder
                .setFrom(page.getStartIndex())
                .setSize(page.getPageSize() + 1)
                .setExplain(false).execute().actionGet();

        SearchHits searchHits = response.getHits();

        // 判断是否有下一页
        if (searchHits.getHits().length == (page.getPageSize() + 1)) {
            pageList.setHasNext(true);
        } else {
            pageList.setHasNext(false);
        }

        SearchHit[] searchHitAry = searchHits.getHits();
        for (int i = 0; i < Math.min(searchHitAry.length, page.getPageSize()); i++) {
            list.add(searchHitAry[i].getSource());
        }
        pageList.setResultList(list);
        return pageList;
    }

    /**
     * 分页获取数据
     *
     * @param page
     * @param index
     * @param documentType
     * @return
     */
    public static final PageList searchPageList(Page page, String index, String documentType, String[] source) {
        PageList pageList = new PageList();
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder =
                ClientConnection.getClient().prepareSearch(index).setTypes(documentType);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        SearchResponse response = searchRequestBuilder
                .setFrom(page.getStartIndex())
                .setSize(page.getPageSize() + 1)
                .setExplain(false).execute().actionGet();

        SearchHits searchHits = response.getHits();

        // 判断是否有下一页
        if (searchHits.getHits().length == (page.getPageSize() + 1)) {
            pageList.setHasNext(true);
        } else {
            pageList.setHasNext(false);
        }

        SearchHit[] searchHitAry = searchHits.getHits();
        for (int i = 0; i < Math.min(searchHitAry.length, page.getPageSize()); i++) {
            Map<String, Object> map = searchHitAry[i].getSource();
            Map<String, Object> m = new HashMap<>();
            for (String key : source) {
                if (map.containsKey(key)) {
                    m.put(key, map.get(key));
                }
            }
            list.add(m);
        }
        pageList.setResultList(list);
        return pageList;
    }

    /**
     * 分页获取数据
     *
     * @param page
     * @param index
     * @param documentType
     * @param filterBuilder
     * @return
     */
    public static final PageList searchPageList(Page page, String index, String documentType, FilterBuilder filterBuilder) {
        PageList pageList = new PageList();
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder =
                ClientConnection.getClient().prepareSearch(index).setTypes(documentType);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequestBuilder.setPostFilter(filterBuilder);
        SearchResponse response = searchRequestBuilder
                .setFrom(page.getStartIndex())
                .setSize(page.getPageSize() + 1)
                .setExplain(false).execute().actionGet();

        SearchHits searchHits = response.getHits();

        // 判断是否有下一页
        if (searchHits.getHits().length == (page.getPageSize() + 1)) {
            pageList.setHasNext(true);
        } else {
            pageList.setHasNext(false);
        }

        SearchHit[] searchHitAry = searchHits.getHits();
        for (int i = 0; i < Math.min(searchHitAry.length, page.getPageSize()); i++) {
            list.add(searchHitAry[i].getSource());
        }
        pageList.setResultList(list);
        return pageList;
    }

    /**
     * 分页获取数据
     *
     * @param page
     * @param index
     * @param documentType
     * @param queryBuilder
     * @return
     */
    public static final PageList searchPageList(Page page, String index, String documentType, QueryBuilder queryBuilder) {
        PageList pageList = new PageList();
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder =
                ClientConnection.getClient().prepareSearch(index).setTypes(documentType);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequestBuilder.setQuery(queryBuilder);
        SearchResponse response = searchRequestBuilder
                .setFrom(page.getStartIndex())
                .setSize(page.getPageSize() + 1)
                .setExplain(false).execute().actionGet();

        SearchHits searchHits = response.getHits();

        // 判断是否有下一页
        if (searchHits.getHits().length == (page.getPageSize() + 1)) {
            pageList.setHasNext(true);
        } else {
            pageList.setHasNext(false);
        }

        SearchHit[] searchHitAry = searchHits.getHits();
        for (int i = 0; i < Math.min(searchHitAry.length, page.getPageSize()); i++) {
            list.add(searchHitAry[i].getSource());
        }
        pageList.setResultList(list);
        return pageList;
    }

    /**
     * 分页获取数据
     *
     * @param page
     * @param index
     * @param documentType
     * @param filterBuilder
     * @return
     */
    public static final PageList searchPageList(Page page, String index, String documentType, FilterBuilder filterBuilder, String[] source) {
        PageList pageList = new PageList();
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder =
                ClientConnection.getClient().prepareSearch(index).setTypes(documentType);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequestBuilder.setPostFilter(filterBuilder);
        SearchResponse response = searchRequestBuilder
                .setFrom(page.getStartIndex())
                .setSize(page.getPageSize() + 1)
                .setExplain(false).execute().actionGet();

        SearchHits searchHits = response.getHits();

        // 判断是否有下一页
        if (searchHits.getHits().length == (page.getPageSize() + 1)) {
            pageList.setHasNext(true);
        } else {
            pageList.setHasNext(false);
        }

        SearchHit[] searchHitAry = searchHits.getHits();
        for (int i = 0; i < Math.min(searchHitAry.length, page.getPageSize()); i++) {
            Map<String, Object> map = searchHitAry[i].getSource();
            Map<String, Object> m = new HashMap<>();
            for (String key : source) {
                if (map.containsKey(key)) {
                    m.put(key, map.get(key));
                }
            }
            list.add(m);
        }
        pageList.setResultList(list);
        return pageList;
    }

    /**
     * 分页获取数据
     *
     * @param page
     * @param index
     * @param documentType
     * @param queryBuilder
     * @return
     */
    public static final PageList searchPageList(Page page, String index, String documentType, QueryBuilder queryBuilder, String[] source) {
        PageList pageList = new PageList();
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder =
                ClientConnection.getClient().prepareSearch(index).setTypes(documentType);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequestBuilder.setQuery(queryBuilder);
        SearchResponse response = searchRequestBuilder
                .setFrom(page.getStartIndex())
                .setSize(page.getPageSize() + 1)
                .setExplain(false).execute().actionGet();

        SearchHits searchHits = response.getHits();

        // 判断是否有下一页
        if (searchHits.getHits().length == (page.getPageSize() + 1)) {
            pageList.setHasNext(true);
        } else {
            pageList.setHasNext(false);
        }

        SearchHit[] searchHitAry = searchHits.getHits();
        for (int i = 0; i < Math.min(searchHitAry.length, page.getPageSize()); i++) {
            Map<String, Object> map = searchHitAry[i].getSource();
            Map<String, Object> m = new HashMap<>();
            for (String key : source) {
                if (map.containsKey(key)) {
                    m.put(key, map.get(key));
                }
            }
            list.add(m);
        }
        pageList.setResultList(list);
        return pageList;
    }

    /**
     * 分页获取数据
     *
     * @param page
     * @param index
     * @param documentType
     * @param queryBuilder
     * @return
     */
    public static final PageList searchPageList(Page page, String index, String documentType, QueryBuilder queryBuilder, FilterBuilder filterBuilder, String[] source) {
        PageList pageList = new PageList();
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder =
                ClientConnection.getClient().prepareSearch(index).setTypes(documentType);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequestBuilder.setQuery(queryBuilder);
        searchRequestBuilder.setPostFilter(filterBuilder);

        SearchResponse response = searchRequestBuilder
                .setFrom(page.getStartIndex())
                .setSize(page.getPageSize() + 1)
                .setExplain(false).execute().actionGet();

        SearchHits searchHits = response.getHits();

        // 判断是否有下一页
        if (searchHits.getHits().length == (page.getPageSize() + 1)) {
            pageList.setHasNext(true);
        } else {
            pageList.setHasNext(false);
        }

        SearchHit[] searchHitAry = searchHits.getHits();
        for (int i = 0; i < Math.min(searchHitAry.length, page.getPageSize()); i++) {
            Map<String, Object> map = searchHitAry[i].getSource();
            Map<String, Object> m = new HashMap<>();
            for (String key : source) {
                if (map.containsKey(key)) {
                    m.put(key, map.get(key));
                }
            }
            list.add(m);
        }
        pageList.setResultList(list);
        return pageList;
    }

    public static Map<String, Object> convertSearchFiledToMap(Map<String, SearchHitField> searchHitFieldMap) {

        Map<String, Object> map = new HashMap<>();

        if (searchHitFieldMap != null && searchHitFieldMap.size() > 0) {
            Set<Map.Entry<String, SearchHitField>> mapEntrySet = searchHitFieldMap.entrySet();
            Iterator<Map.Entry<String, SearchHitField>> entryIterator = mapEntrySet.iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<String, SearchHitField> entry = entryIterator.next();
                String key = entry.getKey();
                SearchHitField searchHitField = entry.getValue();
                List values = searchHitField.getValues();

                if (!key.contains(StringUtil.DOT)) {
                    if (values == null || values.size() == 0) {
                        map.put(key, null);
                    } else if (values.size() == 1) {
                        map.put(key, values.get(0));
                    } else {
                        map.put(key, values);
                    }
                } else {
                    int dotIndex = key.indexOf(StringUtil.DOT);
                    String childKey = key.substring(0, dotIndex);
                    String childPropertyKey = key.substring(dotIndex + 1);

                    if (map.containsKey(childKey)) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(childKey);
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).put(childPropertyKey, values.get(i));
                        }
                    } else {
                        List<Map<String, Object>> list = new ArrayList<>();
                        for (Object value : values) {
                            Map<String, Object> m = new HashMap<>();
                            m.put(childPropertyKey, value);
                            list.add(m);
                        }
                        map.put(childKey, list);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 查询总数
     *
     * @param index
     * @param documentType
     * @return
     */
    public static long count(String index, String documentType) {
        CountResponse countResponse = ClientConnection.getClient().prepareCount()
                .setIndices(index)
                .setTypes(documentType).execute().actionGet();

        return countResponse.getCount();
    }

    /**
     * 查询总数
     *
     * @param index
     * @param documentType
     * @return
     */
    public static long count(String index, String documentType, QueryBuilder queryBuilder) {
        CountResponse countResponse = ClientConnection.getClient().prepareCount()
                .setIndices(index)
                .setTypes(documentType)
                .setQuery(queryBuilder)
                .execute().actionGet();

        return countResponse.getCount();
    }

    /**
     * 添加错误信息
     *
     * @param bulkResponse
     * @param list
     */
    private static final void addFailures(BulkResponse bulkResponse, List<BulkItemResponse> list) {
        if (bulkResponse.hasFailures()) {
            if (bulkResponse.hasFailures()) {
                BulkItemResponse[] bulkItemResponses = bulkResponse.getItems();
                for (BulkItemResponse bulkItemResponse : bulkItemResponses) {
                    list.add(bulkItemResponse);
                }
            }
        }
    }
}
