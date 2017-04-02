package in.tyrael.raider.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.bean.CommodityBean;
import in.tyrael.raider.dao.AuctionDaoImpl;
import in.tyrael.raider.dao.face.AuctionDao;
import in.tyrael.raider.model.face.PricePolicy;
import in.tyrael.raider.service.BidService;
import in.tyrael.raider.service.PriceHistoryService;
import in.tyreal.raider.net.RaiderHttpAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 业务逻辑，综合数据库和网络操作
 * 
 * @author Tyrael
 * 
 */
public class AuctionModel {
	private static final String URL_JSON = 
			"http://auction.jd.com/json/paimai/bid_records?t=1384522506650&dealId=3977982&pageNo=1&pageSize=2147483647";

	private AuctionDao auctionDao;
	private RaiderHttpAgent raiderHttpAgent;
	private Context context;
	
	public AuctionModel(Context context) {
		super();
		this.context = context;
		auctionDao = AuctionDaoImpl.getAuctionDao(context);
		raiderHttpAgent = RaiderHttpAgent.getInstance();
	}

	/**
	 * 1. 从网络获取夺宝箱项目列表 
	 * 2. 存储到数据库，存储拍卖项目，存储商品项目 
	 * 3. 获取成交价格
	 */
	public void syncAuction() {
		List<AuctionBean> lab = raiderHttpAgent.getMyAuction();

		for (AuctionBean ab : lab) {
			// 如果已经存在，则不插入
			if (!auctionDao.existAuction(ab)) {
				PricePolicy p = new PricePolicyImpl();
				CommodityBean cb = p.setPrice(ab.getCommodity());
				ab.setCommodity(auctionDao.insertCommodity(cb));
				auctionDao.insertAuction(ab);
				auctionDao.insertAuctionWanted(ab);
				Log.d("test", "auction inserted");

				// 发送广播，获取成交价格，这个操作需要在拍卖结束时进行。
				AlarmManager alarmManager = (AlarmManager) context
						.getSystemService(context.ALARM_SERVICE);
				Intent intent = new Intent();
				intent.setAction(PriceHistoryService.RECEIVER_PRICE_HISTORY);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						context, PriceHistoryService.getAlarmId(), intent, 0);
				Long triggerTime = ab.getEndTime() + 3000;
				triggerTime = System.currentTimeMillis() > triggerTime ? System
						.currentTimeMillis() + 3000 : triggerTime;
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						ab.getEndTime() + 3000, pendingIntent);
			}
		}
	}
}
