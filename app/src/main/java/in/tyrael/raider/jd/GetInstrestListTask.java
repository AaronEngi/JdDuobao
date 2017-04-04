package in.tyrael.raider.jd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.bean.CommodityBean;
import in.tyreal.raider.net.RaiderHttpAgent;
import in.tyreal.raider.net.RaiderHttpUtil;

/**
 * Created by Administrator on 2017/4/4.
 */

public class GetInstrestListTask implements Runnable {
    @Override
    public void run() {

    }

    /**
     * 返回我的夺宝箱，解析加入数据库
     */
    public List<AuctionBean> getMyAuction() {
        // TODO
//        if (!isLogin()) {
//
//        }
        List<AuctionBean> lab = new ArrayList<AuctionBean>();
        String htmlMyAuction = RaiderHttpAgent.getHtml(RaiderHttpUtil.URL_MY_AUCTION);
        // 解析 TODO
        if (htmlMyAuction != null && !"".equals(htmlMyAuction)) {
            Document doc = Jsoup.parse(htmlMyAuction);
            Elements trs = doc.select(".auctionList").select("li");
            for (int i = 0; i < trs.size(); i++) {

                Element li = trs.get(i);
                Element name = li.select(".name").get(0);
                String a  = name.select("a").attr("href");
                int index = a.lastIndexOf("/");
                String jdId = a.substring(index + 1);

                // 1. 查看商品是否存在
                CommodityBean cb = new CommodityBean();
                cb.setName(name.select("a").get(0).val());

                // 2. 创建Auction对象。
                AuctionBean ab = new AuctionBean();

                ab.setJdId(Integer.valueOf(jdId));
                ab.setCommodity(cb);
//                ab.setCommodity(getCommodityByAuction(cb, ab));

                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm");
//                try {
//                    ab.setStartTime(sdf.parse(tds.get(4).text()).getTime());
//                    ab.setEndTime(sdf.parse(tds.get(5).text()).getTime());
//                } catch (ParseException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }

                lab.add(ab);
            }

        }
        return lab;
    }

}
