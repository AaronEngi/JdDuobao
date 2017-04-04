package com.csmall.mail;

import com.csmall.version.VersionManager;

/**
 * Created by wangchao on 2015/8/31.
 */
public class MailData {
    public static final String TITLE_FEEDBACK = "反馈" + VersionManager.getVersionName();

    public String mailTitle = "默认标题";
    public String mailContent = "默认内容";
    public String mailAttach;
}
