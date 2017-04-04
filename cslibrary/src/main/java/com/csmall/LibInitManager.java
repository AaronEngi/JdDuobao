package com.csmall;

import android.app.Application;

import com.csmall.android.ApplicationHolder;
import com.csmall.crash.BuglyApi;
import com.csmall.log.LogHelper;
import com.csmall.mail.MailSender;

/**
 * Created by wangchao on 2017/3/29.
 */

public class LibInitManager {

    public static void initOnAppCreate(LibInitData data){
        ApplicationHolder.setApplication(data.app);
//        BuglyApi.initOnAppCreate(data.buglyAppId);
        MailSender.setMailConfig(data.mailConfig);
        LogHelper.init();
    }
}
