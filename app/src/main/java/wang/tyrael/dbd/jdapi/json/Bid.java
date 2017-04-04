package wang.tyrael.dbd.jdapi.json;

import in.tyrael.raider.bean.BidBean;

/**
 * Created by Administrator on 2017/4/4.
 */

public class Bid {
    public int bidTime;

    public int bidTime2;

    public String bidTimeStr;

    public String bidTimeStr1;

    public String bidTimeStr2;

    public String icon;

    public int paimaiId;

    public int platType;

    public double price;

    public String priceStr;

    public int productId;

    public String userArea;

    public String userLevel;

    public String userName1;

    public String username;

    public BidBean toBidBean(){
        BidBean bidBean = new BidBean();
        bidBean.setTime(this.bidTime);
        bidBean.setUserName(this.username);
        int price = (int) (this.price * 100);
        bidBean.setPrice(price);
        return bidBean;
    }
}
