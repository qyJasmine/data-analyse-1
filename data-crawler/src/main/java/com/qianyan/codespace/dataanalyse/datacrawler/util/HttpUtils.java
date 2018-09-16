package com.qianyan.codespace.dataanalyse.datacrawler.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    public static HttpResponse getRawHtml(HttpClient client, String personalUrl) {
        // get response as html, method type: GET
        HttpGet getMethod = new HttpGet(personalUrl);
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        try {
            // execute GET method
            response = client.execute(getMethod);
        } catch (IOException e) {
            LOG.error("fail to get html from url:{}, error:{}", personalUrl, e.getMessage());
        } finally {
            getMethod.abort();
        }
        return response;
    }

}
