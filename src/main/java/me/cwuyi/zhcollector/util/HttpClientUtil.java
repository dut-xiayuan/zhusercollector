package me.cwuyi.zhcollector.util;

/**
 * Created by xia on 17-10-15.
 * <p>
 * cited by wycm/zhihu-crawler
 */

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * HttpClient工具类
 */
public class HttpClientUtil {
    private static CookieStore cookieStore = new BasicCookieStore();
    private static CloseableHttpClient httpClient;
    private final static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36";
    private static HttpHost proxy;
    private static RequestConfig requestConfig;

    static{
        init();
    }
    private static void init() {
        try {
            SSLContext sslContext =
                    SSLContexts.custom()
                            .loadTrustMaterial(KeyStore.getInstance(KeyStore.getDefaultType()), new TrustStrategy() {
                                public boolean isTrusted(X509Certificate[] chain, String authType)
                                        throws CertificateException {
                                    return true;
                                }
                            }).build();
            SSLConnectionSocketFactory sslSFactory =
                    new SSLConnectionSocketFactory(sslContext);
            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslSFactory)
                            .build();

            PoolingHttpClientConnectionManager connManager =
                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(Constants.TIMEOUT).setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);

            ConnectionConfig connectionConfig =
                    ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
                            .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(500);
            connManager.setDefaultMaxPerRoute(300);
            HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    if (executionCount > 2) {
                        return false;
                    }
                    if (exception instanceof InterruptedIOException) {
                        return true;
                    }
                    if (exception instanceof ConnectTimeoutException) {
                        return true;
                    }
                    if (exception instanceof UnknownHostException) {
                        return true;
                    }
                    if (exception instanceof SSLException) {
                        return true;
                    }
                    HttpRequest request = HttpClientContext.adapt(context).getRequest();
                    if (!(request instanceof HttpEntityEnclosingRequest)) {
                        return true;
                    }
                    return false;
                }
            };
            HttpClientBuilder httpClientBuilder =
                    HttpClients.custom().setConnectionManager(connManager)
                            .setRetryHandler(retryHandler)
                            .setDefaultCookieStore(cookieStore).setUserAgent(userAgent);
            if (proxy != null) {
                httpClientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy)).build();
            }
            httpClient = httpClientBuilder.build();

            requestConfig = RequestConfig.custom().setSocketTimeout(Constants.TIMEOUT).
                    setConnectTimeout(Constants.TIMEOUT).
                    setConnectionRequestTimeout(Constants.TIMEOUT).
                    setCookieSpec(CookieSpecs.STANDARD).
                    build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getWebPage(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        return getWebPage(request, "utf-8");
    }
    public static String getWebPage(HttpRequestBase request) throws IOException {
        return getWebPage(request, "utf-8");
    }
    public static String postRequest(String postUrl, Map<String, String> params) throws IOException {
        HttpPost post = new HttpPost(postUrl);
        setHttpPostParams(post, params);
        return getWebPage(post, "utf-8");
    }
    /**
     * @param encoding 字符编码
     * @return 网页内容
     */
    public static String getWebPage(HttpRequestBase request
            , String encoding) throws IOException {
        CloseableHttpResponse response = null;
        response = getResponse(request);
        String content = EntityUtils.toString(response.getEntity(),encoding);
        request.releaseConnection();
        return content;
    }
    public static CloseableHttpResponse getResponse(HttpRequestBase request) throws IOException {
        if (request.getConfig() == null){
            request.setConfig(requestConfig);
        }
        request.setHeader("User-Agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)]);
        HttpClientContext httpClientContext = HttpClientContext.create();
        httpClientContext.setCookieStore(cookieStore);

        CloseableHttpResponse response = httpClient.execute(request, httpClientContext);
        return response;
    }
    public static CloseableHttpResponse getResponse(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        return getResponse(request);
    }
    /**
     * 序列化对象
     * @param object
     * @throws Exception
     */
    public static void serializeObject(Object object,String filePath){
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            fos.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 反序列化对象
     * @param path
     * @throws Exception
     */
    public static Object deserializeObject(String path) throws Exception {
//		InputStream fis = HttpClientUtil.class.getResourceAsStream(name);
        File file = new File(path);
        InputStream fis = new FileInputStream(file);
        ObjectInputStream ois = null;
        Object object = null;
        ois = new ObjectInputStream(fis);
        object = ois.readObject();
        fis.close();
        ois.close();
        return object;
    }

    /**
     * 下载图片
     * @param fileURL 文件地址
     * @param path 保存路径
     * @param saveFileName 文件名，包括后缀名
     * @param isReplaceFile 若存在文件时，是否还需要下载文件
     */
    public static void downloadFile(String fileURL
            , String path
            , String saveFileName
            , Boolean isReplaceFile){
        try{
            CloseableHttpResponse response = getResponse(fileURL);
            File file =new File(path);
            //如果文件夹不存在则创建
            if  (!file .exists()  && !file .isDirectory()){
                file.mkdirs();
            } else{
            }
            file = new File(path + saveFileName);
            if(!file.exists() || isReplaceFile){
                //如果文件不存在，则下载
                try {
                    OutputStream os = new FileOutputStream(file);
                    InputStream is = response.getEntity().getContent();
                    byte[] buff = new byte[(int) response.getEntity().getContentLength()];
                    while(true) {
                        int readed = is.read(buff);
                        if(readed == -1) {
                            break;
                        }
                        byte[] temp = new byte[readed];
                        System.arraycopy(buff, 0, temp, 0, readed);
                        os.write(temp);
                    }
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
            }
            response.close();
        } catch(IllegalArgumentException e){
        } catch(Exception e1){
            e1.printStackTrace();
        }
    }
    public static CookieStore getCookieStore() {
        return cookieStore;
    }

    public static void setCookieStore(CookieStore cookieStore) {
        HttpClientUtil.cookieStore = cookieStore;
    }
    /**
     * 有bug 慎用
     * unicode转化String
     * @return
     */
    public static String decodeUnicode(String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            start = dataStr.indexOf("\\u", start - (6 - 1));
            if (start == -1){
                break;
            }
            start = start + 2;
            end = start + 4;
            String tempStr = dataStr.substring(start, end);
            String charStr = "";
            charStr = dataStr.substring(start, end);
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            dataStr = dataStr.replace("\\u" + tempStr, letter + "");
            start = end;
        }
        return dataStr;
    }
    /**
     * 设置request请求参数
     * @param request
     * @param params
     */
    public static void setHttpPostParams(HttpPost request,Map<String,String> params){
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            formParams.add(new BasicNameValuePair(key,params.get(key)));
        }
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formParams, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request.setEntity(entity);
    }
    public static org.apache.http.client.config.RequestConfig.Builder getRequestConfigBuilder(){
        return RequestConfig.custom().setSocketTimeout(Constants.TIMEOUT).
                setConnectTimeout(Constants.TIMEOUT).
                setConnectionRequestTimeout(Constants.TIMEOUT).
                setCookieSpec(CookieSpecs.STANDARD);
    }
}