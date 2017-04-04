package wang.tyrael.dbd.jdapi;

import com.csmall.log.LogHelper;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import in.tyrael.raider.bean.CommodityBean;
import in.tyreal.raider.net.RaiderHttpAgent;
import wang.tyrael.dbd.jdapi.json.BidList;
import wang.tyrael.dbd.jdapi.json.JdPrice;

/**
 * Created by Administrator on 2017/4/4.
 */

public class DuobaoApi {
    private static final java.lang.String TAG = "DuobaoApi";

    public static BidList requestBidList(int duobaoId){
        String url = String.format(Url.BID_LIST, duobaoId);
        String r = RaiderHttpAgent.getHtml(url);
        String sJson = ResponseProcessor.removeFunction(r);
        return new Gson().fromJson(sJson, BidList.class);
    }

    public static CommodityBean requestCommodityDetail(int duobaoId){
        String url = String.format(Url.DUOBAO_DETAIL, duobaoId);
        String r = RaiderHttpAgent.getHtml(url);
        Document doc = Jsoup.parse(r);
        Element eName = doc.select(".intro_detail").get(0).select(".name").get(0);
        String name = eName.attr("title");
        LogHelper.d(TAG, "name:" + name);
        Element eProductId = doc.select("#productId").first();
        String productId = eProductId.attr("value");
        LogHelper.d(TAG, "productId:" + productId);

        CommodityBean cb = new CommodityBean();
        cb.setName(name);
        cb.setJdId(Integer.parseInt(productId));
        return cb;
    }

    public static JdPrice requestJdPrice(String sku, int duobaoId){
        String url = String.format(Url.JD_PRICE, sku, duobaoId);
        String s = RaiderHttpAgent.getHtml(url);
        return new Gson().fromJson(s, JdPrice.class);
    }
}
