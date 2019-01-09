package com.radarwin.framework.elasticsearch;

import com.radarwin.framework.util.PropertyReader;
import com.radarwin.framework.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Created by josh on 15/7/8.
 */
public class ClientConnection {
    private static Client client = null;

    private static Logger logger = LogManager.getLogger(ClientConnection.class);

    private ClientConnection() {
    }

    static {

        logger.info("start init elasticsearch");
        long start = System.currentTimeMillis();

        String hosts = PropertyReader.get("es.host", PropertyReader.ES_FILE);
        String clusterName = PropertyReader.get("es.cluster.name", PropertyReader.ES_FILE);

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", clusterName) // 集群名称
                .put("client.transport.sniff", true).build();

        TransportClient transportClient = new TransportClient(settings);
        if (hosts != null) {
            String[] hostAry = hosts.split(StringUtil.COMMA);
            for (String host : hostAry) {
                String[] hostAndPort = host.split(StringUtil.COLON);
                transportClient.addTransportAddress(
                        new InetSocketTransportAddress(hostAndPort[0], Integer.valueOf(hostAndPort[1]))
                );
            }
        }
        client = transportClient;

        logger.info("end init elasticsearch");
        logger.info("cost time " + (System.currentTimeMillis() - start) + " ms");

    }

    public static final Client getClient() {
        return client;
    }
}
