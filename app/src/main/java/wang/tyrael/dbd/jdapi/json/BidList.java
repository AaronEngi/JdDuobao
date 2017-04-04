package wang.tyrael.dbd.jdapi.json;

import java.util.ArrayList;
import java.util.List;

import in.tyrael.raider.bean.BidBean;

/**
 * Created by Administrator on 2017/4/4.
 */

public class BidList {
    public String nextPrice;

    public List<Bid> bidList ;

    public String remainTime;

    public String nextPriceStr;

    public String auctionStatus;

    public String stockNum;

    public int refreshTime;

    public String currentPrice;

    public int bidCount;

    public int orderStatus;

    public String currentNum;

    public List<BidBean> toBidBeanList(){
        List<BidBean> list = new ArrayList<>();
        for (Bid bid:
             bidList) {
            BidBean bb = bid.toBidBean();
            list.add(bb);
        }
        return list;
    }
}
