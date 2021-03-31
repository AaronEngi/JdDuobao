package tyrael.duobao.jdapi;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import in.tyreal.raider.net.RaiderHttpAgent;
import tyrael.duobao.dev.DevConfig;
import wang.tyrael.http.url.UrlBuilder;

public class DuobaoApi {
    private static final DuobaoApi duobaoApi = new DuobaoApi();

    private DuobaoApi() {

    }

    public static DuobaoApi getDuobaoApi() {
        return duobaoApi;
    }
    //    https://api.m.jd.com/api?functionId=paipai.dbd.reminder.auctionList
    // &body={"readStatus":"","endStatus":"0","pageNo":1,"pageSize":20,"mpSource":1}&t=1616856477221&appid=paipai_h5

//    {
//        "ca:0,
//        "result":{
//        "code":200,
//                "data":{
//            "pageSize":20,
//                    "pageNo":1,
//                    "totalCount":2,
//                    "pageList":[
//            {
//                "id":259926977,
//                    "usedNo":44181172569920260,
//                    "productName":"Redmi显示器1A 23.8英寸 IPS技术硬屏 三微边设计 低蓝光 纤薄机身  黑色",
//                    "startTime":1616932020000,
//                    "endTime":1616934720000,
//                    "startPrice":1,
//                    "minPrice":1,
//                    "maxPrice":400,
//                    "cappedPrice":799,
//                    "category1":670,
//                    "category1Name":"电脑、办公",
//                    "category2":677,
//                    "category2Name":"电脑组件",
//                    "category3":688,
//                    "category3Name":"显示器",
//                    "primaryPic":"jfs/t1/128882/8/16697/89819/5f9a7204Eb39e7742/a5cb2cd2248670a2.jpg",
//                    "currentPrice":51,
//                    "recordCount":4,
//                    "bidder":"yayoyo",
//                    "status":2,
//                    "quality":"95新",
//                    "spectatorCount":0,
//                    "reminding":1,
//                    "shopId":null,
//                    "shopName":null,
//                    "productType":1,
//                    "size":null,
//                    "brandId":18374,
//                    "brandName":"小米（MI）",
//                    "auctionType":1,
//                    "actualEndTime":1616934720000,
//                    "delayCount":null,
//                    "shortProductName":null,
//                    "hasSameAuctions":true,
//                    "sameSkuCount":4,
//                    "openProtect":null,
//                    "protectPrice":null,
//                    "shieldType":null
//            }
//            ],
//            "cursorMark":null
//        },
//        "list":null,
//                "message":null
//    }
//    }

    public ReminderListResponse reminderList() {
        Map<String, String> queries = new HashMap<>();
        queries.put("functionId", "paipai.dbd.reminder.auctionList");
        queries.put("body", "{\"readStatus\":\"\",\"endStatus\":\"0\",\"pageNo\":1,\"pageSize\":20,\"mpSource\":1}");
        queries.put("t", String.valueOf(System.currentTimeMillis()));
        queries.put("appid", "paipai_h5");

        UrlBuilder urlBuilder = new UrlBuilder(DuobaoUrl.BASE, queries);
        Log.d(DevConfig.TAG, "reminderList: " + urlBuilder.build());
        String r = RaiderHttpAgent.getResponseBody(urlBuilder.build());
        Log.d(DevConfig.TAG, "reminderList: " + r);

        return JSON.parseObject(r, ReminderListResponse.class);
    }

    public void bid() {

    }
}
