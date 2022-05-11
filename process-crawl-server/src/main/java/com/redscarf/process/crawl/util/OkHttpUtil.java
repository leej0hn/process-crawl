package com.redscarf.process.crawl.util;

import com.google.common.base.Throwables;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>function:
 * <p>User: leejohn
 * <p>Date: 17/3/13
 * <p>Version: 1.0
 */
public class OkHttpUtil {
    private static Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();

    public static String get(String url) {
        Headers headers = new Headers.Builder().build();
        return get(url, headers);
    }

    public static String get(String url, Headers headers) {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        return result(request);
    }

    public static void downloadfile(String url, String savePath) {
        try {

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();
            byte[] data = new byte[1024];
            int len = 0;
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(savePath);
                while ((len = inputStream.read(data)) != -1) {
                    fileOutputStream.write(data, 0, len);
                }
            } catch (IOException e) {
                logger.error(Throwables.getStackTraceAsString(e));
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        logger.error(Throwables.getStackTraceAsString(e));
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        logger.error(Throwables.getStackTraceAsString(e));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(Throwables.getStackTraceAsString(e));
        }
    }

    public static String upload(byte[] content, String uploadUrl) throws IOException {
        logger.info("OkHttpUtil.upload url : " + uploadUrl);
        RequestBody fileBody = RequestBody.create(MediaType.parse("text/xml "), content);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", "content", fileBody)
                .build();
        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build();
        Response response;
        response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String upload(File file, String uploadUrl) throws IOException {
        logger.info("OkHttpUtil.upload url : " + uploadUrl);
        RequestBody fileBody = RequestBody.create(MediaType.parse("text/xml "), file);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build();
        Response response;
        response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postJson(String url, String json) {
        return postJson(url, json, Headers.of(Collections.emptyMap()));
    }

    public static String postJson(String url, String json, Headers headers) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headers)
                .build();
        return result(request);
    }

    public static String postForm(String url, Map<String, String> params, Headers headers) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .headers(headers)
                .build();
        return postForm(url, params, request);
    }

    public static String postForm(String url, Map<String, String> params, String cookie) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .addHeader("Cookie", cookie)
                .build();
        return postForm(url, params, request);
    }

    public static String postForm(String url, Map<String, String> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        return postForm(url, params, request);
    }

    public static String postForm(String url, Map<String, String> params, Request request) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }
        return result(request);
    }

    private static String result(Request request) {
        String result = null;
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException ioe) {
            logger.error(Throwables.getStackTraceAsString(ioe));
        }
        return result;
    }

    public static void uploadAsy(String uploadUrl, String fileName, File file, Map<String, String> params, Callback responseCallback) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(fileName, file.getName(), fileBody);
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(builder.build())
                .build();
        resultAsy(request, responseCallback);
    }

    public static void postFormAsy(String url, Map<String, String> params, Request request, Callback responseCallback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
        resultAsy(request, responseCallback);
    }

    public static void postFormAsy(String url, Map<String, String> params, Callback responseCallback) {
        Headers headers = new Headers.Builder().build();
        postFormAsy(url, params, headers, responseCallback);
    }

    public static void postFormAsy(String url, Map<String, String> params, Headers headers, Callback responseCallback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .post(builder.build())
                .build();
        resultAsy(request, responseCallback);
    }

    public static void postJsonAsy(String url, String jsonBody, Headers headers, Callback responseCallback) {
        RequestBody body = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .post(body)
                .build();
        resultAsy(request, responseCallback);
    }

    public static void postJsonAsy(String url, String jsonBody, String token, Callback responseCallback) {
        Headers headers = new Headers.Builder()
                .add("token_header", token).build();
        postJsonAsy(url, jsonBody, headers, responseCallback);
    }

    private static void resultAsy(Request request, Callback responseCallback) {
        client.newCall(request).enqueue(responseCallback);
    }

}
