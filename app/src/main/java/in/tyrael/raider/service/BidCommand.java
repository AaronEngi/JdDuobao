package in.tyrael.raider.service;

import in.tyrael.raider.RaiderUtil;
import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.bean.BidBean;
import in.tyrael.raider.bean.CommodityBean;
import in.tyreal.raider.net.RaiderHttpAgent;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class BidCommand implements Serializable{
	private final Logger log = Logger.getLogger(BidService.class);

	private static final long serialVersionUID = 1L;
		
	private AuctionBean auctionBean;
	private long executeTime;
	
	//TODO context问题
	public void bid(RaiderHttpAgent rha){
		CommodityBean cb = null;
		if(auctionBean == null ){
			throw new RuntimeException("bid::null ");
		}else{
			cb = auctionBean.getCommodity();
			if(cb == null){
				throw new RuntimeException("bid::null ");
			}
		}
		//获取当前最高价格
		
		//出价
		log.info("试图获取当前价格" + RaiderUtil.bidDateFormat.format(new Date(System.currentTimeMillis())));
		rha = RaiderHttpAgent.getInstance();
		List<BidBean> lbb = rha.getBid(auctionBean);		
		int maxPrice =0;
		int price = 1;
		if(lbb != null && lbb.size() !=0){
			maxPrice = lbb.get(0).getPrice();
			if(maxPrice + cb.getPriceStepIdeal()< cb.getPriceIdeal() ){
				price = maxPrice + cb.getPriceStepIdeal();
			}else if(maxPrice + cb.getPriceStepExtreme() <cb.getPriceExtreme()){
				price = maxPrice +cb.getPriceStepExtreme();
			}else if(maxPrice + 1 < cb.getPriceStepExtreme() ){
				price = maxPrice +cb.getPriceStepExtreme();
			}else{
				return;
			}
		}
		log.info("已获取当前价格" + RaiderUtil.bidDateFormat.format(new Date(System.currentTimeMillis())));
		rha.bid(auctionBean, price);
	};

	public long getExecuteTime() {
		return executeTime;
	}

	public AuctionBean getAuctionBean() {
		return auctionBean;
	}

	public void setAuctionBean(AuctionBean auctionBean) {
		this.auctionBean = auctionBean;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}
}
