package com.csmall.net.ordinary;

import java.util.Map;

/**
 * Created by wangchao on 2017/3/7.
 */

public class RequestData {
    public String method;
    public String url;
    public Map<String, String> headers;
    public RequestListener listener;

    public Map<String, String> params;
    public String body;
}
