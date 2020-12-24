package com.example.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    //现在我们发起一条HTTP请求只需要调用sendOkHttpRequest()方法，传入请求地址，并注册一
    //个回调来处理服务器响应就可以了。
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
