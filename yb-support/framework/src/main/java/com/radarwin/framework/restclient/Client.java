package com.radarwin.framework.restclient;

import com.radarwin.framework.util.PropertyReader;
import com.radarwin.framework.util.StringUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by josh on 15/9/23.
 */
public class Client {

    private static Logger logger = LogManager.getLogger(Client.class);

    private static CloseableHttpClient httpClient = null;

    private static int maxTotal = 400;

    private static int defaultMaxPerRoute = 200;

    private static int connectTimeout = 3000;

    private static int socketTimeout = 3000;

    private static int keepAlive = 5000;

    private static RequestConfig requestConfig = null;

    private static SocketConfig socketConfig = null;

    static {
        resolvePropertyConfig();
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
                            return true;
                        }
                    }).build();

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    NoopHostnameVerifier.INSTANCE);

            Registry registry = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionSocketFactory).build();

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(maxTotal);
            cm.setDefaultMaxPerRoute(defaultMaxPerRoute);

            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout)
                    .build();

            socketConfig = SocketConfig.custom()
                    .setSoTimeout(socketTimeout)
                    .setTcpNoDelay(true)
                    .build();

            httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultSocketConfig(socketConfig)
                    .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
                        @Override
                        public long getKeepAliveDuration(HttpResponse response,
                                                         HttpContext context) {
                            long ka = super.getKeepAliveDuration(response, context);
                            if (ka == -1) {
                                ka = keepAlive;
                            }
                            return ka;
                        }
                    }).build();

        } catch (Exception e) {
            logger.error("Init httpclient error", e);
        }
    }

    public static CloseableHttpClient getRestClient() {
        return httpClient;
    }

    public static RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public static SocketConfig getSocketConfig() {
        return socketConfig;
    }

    private static void resolvePropertyConfig() {
        String configFileName = "restClientConfig.properties";

        String conMaxTotal = PropertyReader.get("connection.maxTotal", configFileName);
        String conDefaultMaxPerRoute = PropertyReader.get("connection.defaultMaxPerRoute", configFileName);
        String conTimeout = PropertyReader.get("connection.timeout", configFileName);
        String soTimeout = PropertyReader.get("connection.socket.timeout", configFileName);
        String conKeepAlive = PropertyReader.get("connection.keepAlive", configFileName);

        if (StringUtil.isNotBlank(conMaxTotal) && !conMaxTotal.contains("$")) {
            try {
                maxTotal = Integer.valueOf(conMaxTotal);
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }

        if (StringUtil.isNotBlank(conDefaultMaxPerRoute) && !conDefaultMaxPerRoute.contains("$")) {
            try {
                defaultMaxPerRoute = Integer.valueOf(conDefaultMaxPerRoute);
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }

        if (StringUtil.isNotBlank(conTimeout) && !conTimeout.contains("$")) {
            try {
                connectTimeout = Integer.valueOf(conTimeout);
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }

        if (StringUtil.isNotBlank(soTimeout) && !soTimeout.contains("$")) {
            try {
                socketTimeout = Integer.valueOf(soTimeout);
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        if (StringUtil.isNotBlank(conKeepAlive) && !conKeepAlive.contains("$")) {
            try {
                keepAlive = Integer.valueOf(conKeepAlive);
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
