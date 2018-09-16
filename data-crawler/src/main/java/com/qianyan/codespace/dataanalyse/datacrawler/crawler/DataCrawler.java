package com.qianyan.codespace.dataanalyse.datacrawler.crawler;

import com.qianyan.codespace.dataanalyse.datacrawler.dataobject.JobEntry;
import com.qianyan.codespace.dataanalyse.datacrawler.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataCrawler fetch data from a specified URL
 */
public class DataCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(DataCrawler.class);
    private String dstUrl;  // https://www.lagou.com/jobs/list_大数据?px=default&gx=全职&city=杭州#order

    public DataCrawler(String url) {
        this.dstUrl = url;
    }

    public List<JobEntry> fetchData(HttpClient client, String url) throws Exception { // todo: support http & https
        List<JobEntry> jobs = new ArrayList<>();

        // get response from url
        HttpResponse response = HttpUtils.getRawHtml(client, url);
        int statusCode = response.getStatusLine().getStatusCode();
        LOG.info("status code:{}", response.getEntity().getContent());
        // if status code is 200, get entity data
        if(statusCode == 200){
            String entity = EntityUtils.toString(response.getEntity(),"utf-8");
            LOG.info("response entity is:{}", entity);
            //jobs = getData(entity);
            EntityUtils.consume(response.getEntity());
        }else {
            // if status code is not 200, just consume entity
            EntityUtils.consume(response.getEntity());
        }
        return jobs;
    }

    /**
     * create https client
     * @return
     */
    protected static CloseableHttpClient getHttpsClient() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            // not do host verification
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),
                    NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslConnectionSocketFactory)
                    .build();

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(100);
            return HttpClients.custom()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setConnectionManager(cm).build();
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            LOG.error("fail to get http client, error:{}", e.getMessage());
            return HttpClients.createDefault();
        }
    }

    private static List<JobEntry> getData (String html) throws Exception{
        List<JobEntry> data = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements elements=doc.select("ul[class=gl-warp clearfix]").select("li[class=gl-item]");
        for (Element ele:elements) {
            String bookID=ele.attr("data-sku");
            String bookPrice=ele.select("div[class=p-price]").select("strong").select("i").text();
            String bookName=ele.select("div[class=p-name]").select("em").text();
            JobEntry jobEntry=new JobEntry();
            // set jobentry's value
            data.add(jobEntry);
        }
        return data;
    }

    public static void main(String[] args) {
        String url = "https://www.lagou.com/jobs/list_大数据?px=default&gx=全职&city=杭州#order";
        DataCrawler dataCrawler = new DataCrawler(url);
        try {
            dataCrawler.fetchData(getHttpsClient(), url);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("fail to fetch data, error:{}", e.getMessage());
        }
    }
}
