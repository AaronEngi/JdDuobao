package in.tyrael.raider.presenter;

import android.os.AsyncTask;

import in.tyrael.raider.activity.AuctionDetailActivity;
import in.tyrael.raider.bean.CommodityBean;
import wang.tyrael.dbd.jdapi.DuobaoApi;
import wang.tyrael.dbd.jdapi.json.BidList;
import wang.tyrael.dbd.jdapi.json.JdPrice;

/**
 * Created by Administrator on 2017/4/4.
 */

public class AuctionDetailPresenter {
    private AuctionDetailActivity auctionDetailActivity;

    private int jdDuobaoId;

    private String jdProductId;

    public void loadMain(){

    }

    private void requestBidList(){
        new AsyncTask<Integer, Integer, BidList>(){
            @Override
            protected BidList doInBackground(Integer... no) {
                BidList bidList = DuobaoApi.requestBidList(jdDuobaoId);
                return bidList;
            }

            @Override
            protected void onPostExecute(BidList bidList) {
                auctionDetailActivity.updateBid(bidList.toBidBeanList());
            }
        }.execute();
        BidList bidList = DuobaoApi.requestBidList(jdDuobaoId);
        auctionDetailActivity.updateBid(bidList.toBidBeanList());
    }

    private void requestProduct(){
        new AsyncTask<String, Integer, CommodityBean>(){
            @Override
            protected CommodityBean doInBackground(String... params) {
                JdPrice price = DuobaoApi.requestJdPrice(jdProductId,jdDuobaoId);
                CommodityBean cb = DuobaoApi.requestCommodityDetail(jdDuobaoId);
                cb.setJdPrice(price.jdprice);
                return cb;
            }

            @Override
            protected void onPostExecute(CommodityBean commodityBean) {
                auctionDetailActivity.updateProdcut(commodityBean);
            }
        }.execute();
    }


    public void setDuobaoId(int duobaoId) {
        this.jdDuobaoId = duobaoId;
    }
}
