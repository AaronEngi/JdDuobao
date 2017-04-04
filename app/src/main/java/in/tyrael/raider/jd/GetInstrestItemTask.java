package in.tyrael.raider.jd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.bean.CommodityBean;
import in.tyreal.raider.net.RaiderHttpAgent;
import in.tyreal.raider.net.RaiderHttpUtil;

/**
 * Created by Administrator on 2017/4/4.
 */

public class GetInstrestItemTask implements Runnable {
    @Override
    public void run() {

    }

    /**
     * 获取commodity详细信息
     * TODO 目前是通过解析auction详情页面获取商品信息,必须有auction的id
     *
     * auction详情页面

     * @return
     */
    public CommodityBean getCommodityByAuction(CommodityBean cb, AuctionBean ab) {
        if (cb == null || ab == null || ab.getCommodity() == null) {
            throw new RuntimeException("商品不能为空");
        }
        String html = RaiderHttpAgent.getHtml(
                RaiderHttpUtil.URL_AUCTION_DETAIL + ab.getJdId());
        if (html != null && !"".equals(html)) {
            Document doc = Jsoup.parse(html);
            Elements esJdId = doc.select("#aprinfo");
            String str = esJdId.get(0).attr("data-url");///json/paimai/productDesciption?productId=713714
            String strJdId = str.substring(str.indexOf("=") + 1);
            int jdId = Integer.valueOf(strJdId);
            cb.setJdId(jdId);
            ab.getCommodity().setJdId(jdId);

            Elements esPrice = doc.select("#product-intro").select("del");
            String strp = esPrice.text();
            strp = strp.substring(1, strp.length());// ￥679.00
            cb.setJdPrice((int) (Float.valueOf(strp) * 100));// 单位分
            ab.getCommodity().setJdPrice((int) (Float.valueOf(strp) * 100));
            return cb;

        }else{
            throw new RuntimeException("html为空");
        }
    }
}
