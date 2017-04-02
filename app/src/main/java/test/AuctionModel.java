package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.dao.AuctionDaoImpl;
import in.tyrael.raider.dao.face.AuctionDao;
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
	private static final String URL_MY_AUCTION = "http://auction.jd.com/interest.action";
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
				ab.setCommodity(auctionDao.insertCommodity(ab.getCommodity()));
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

	public void test() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(URL_MY_AUCTION);

		HttpResponse httpResponse = null;
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			httpResponse = httpClient.execute(get);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						entity.getContent()));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("brc", sb.toString());

		// try {
		// /**
		// * 使用Jsoup解析html
		// */
		// //连接主页，获取html，开始进行解析
		// Document doc = Jsoup.connect(URL_MAIN).get();
		// //获得一个以movie_show_shot（热播电影）为id节点
		// Element nodes = doc.getElementById("movie_show_hot");
		// //获得一个以<class="video"节点集合
		// Elements links = nodes.getElementsByClass("video");
		// StringBuffer stringBuffer = new StringBuffer();
		// int i = 0;
		// for (i = 0; i < links.size(); i++) {
		// //遍历集合获得第一个节点元素
		// Element et = links.get(i).select("a[href]").first();
		// //获取元素的href属性
		// stringBuffer.append(URL_MAIN + et.attr("href") + "\n");
		// }
		// content = stringBuffer.toString();
		// mHandler.sendEmptyMessage(0);
		// } catch (IOException e) {
		//
		// e.printStackTrace();
		// }
	}

	// auctionStatus: 0
	// datas: [,…]
	// 0: {id:17837973, paimaiDealId:3977986, productId:862379,
	// userNickName:465057236_m, bidTime:1384523906180,…}
	// bidStatus: null
	// bidTime: 1384523906180
	// id: 17837973
	// ipAddress: "14.217.175.170"
	// paimaiDealId: 3977986
	// price: 2800
	// productId: 862379
	// userNickName: "465057236_m"
	// 1: {id:17837361, paimaiDealId:3977986, productId:862379,
	// userNickName:momovv11, bidTime:1384522964785,…}
	// 2: {id:17837027, paimaiDealId:3977986, productId:862379,
	// userNickName:yuyue836, bidTime:1384522455986,…}
	// 3: {id:17836730, paimaiDealId:3977986, productId:862379,
	// userNickName:jd_6b315d1d362e3,…}
	// 4: {id:17835440, paimaiDealId:3977986, productId:862379,
	// userNickName:等待夏天, bidTime:1384520148937,…}
	// 5: {id:17835433, paimaiDealId:3977986, productId:862379,
	// userNickName:13798887716, bidTime:1384520139892,…}
	// 6: {id:17835318, paimaiDealId:3977986, productId:862379,
	// userNickName:等待夏天, bidTime:1384519969283,…}
	// 7: {id:17835208, paimaiDealId:3977986, productId:862379,
	// userNickName:13798887716, bidTime:1384519795572,…}
	// pageNo: 1
	// pageSize: 8
	// totalItem: 22
	// totalPages: 3
	// trxBuyerName: null
	// trxPrice: null

	public static String parseJson() {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(URL_JSON);

		HttpResponse httpResponse = null;
		String line = null;
		JSONObject jo = null;
		try {
			httpResponse = httpClient.execute(get);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						entity.getContent()));
				line = null;
				line = br.readLine();
			}

			jo = new JSONObject(line);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("brc", line);
		return line;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



}
