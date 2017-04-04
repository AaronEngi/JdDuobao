package com.csmall.crash;

import com.csmall.android.ApplicationHolder;
import com.tencent.bugly.Bugly;

/**
 * Created by wangchao on 2017/3/29.
 */

public class BuglyApi {
    public static void initOnAppCreate(String buglyAppId){
        Bugly.init(ApplicationHolder.getApplication(), buglyAppId, false);
    }
}
