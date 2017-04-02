package in.tyrael.raider.service;

import java.util.List;

import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.bean.BidBean;
import in.tyrael.raider.dao.AuctionDaoImpl;
import in.tyrael.raider.dao.AuctionSQLiteHelper;
import in.tyrael.raider.dao.face.AuctionDao;
import in.tyrael.raider.service.BidService.CommandExecutorReceiver;
import in.tyrael.raider.service.BidService.CommandStorerReceiver;

import in.tyreal.raider.net.RaiderHttpAgent;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;
import android.util.Log;

/**
 * 每当创建一个auction时，就设定一个闹钟，在该auction结束后，获取成交价格
 * @author DELL
 *
 */
public class PriceHistoryService extends Service {
	public static final String RECEIVER_PRICE_HISTORY = 
			"in.tyrael.raider.PriceHistoryService.SAVE_PRICE_HISTORY";
	
	
	private CommandExecutorReceiver rCommandExecutor = new CommandExecutorReceiver();
	// TODO 1. 确定开始时是不是这个方法
	// 2. 同步问题
	public void onCreate() {
		IntentFilter filter = new IntentFilter(RECEIVER_PRICE_HISTORY);
		registerReceiver(rCommandExecutor, filter);

	}

	@Override
	// TODO
	public void onDestroy() {
		unregisterReceiver(rCommandExecutor);
	}
	
	//TODO 持久化
	//状态变量
	private static int alarmId = 0;
	
	/**
	 * 
	 * @return 一个唯一的闹钟id
	 */
	public static synchronized int getAlarmId(){
		return alarmId ++;
	}
	
	public PriceHistoryService() {
	}
	
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	// 有个reciever接受处理发送程序
	// 任务执行器
	public class CommandExecutorReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			AuctionBean b = (AuctionBean) intent.getSerializableExtra("AuctionBean");			
			new Thread(new ThreadPriceHistory(b)).start();
		}
	}

	private class ThreadPriceHistory implements Runnable {
		private AuctionBean auctionBean;
		ThreadPriceHistory(AuctionBean b){
			auctionBean = b;
		}
				
		@Override
		public void run() {
			//1. 从网络上获取成交价
			RaiderHttpAgent agent = RaiderHttpAgent.getInstance();
			List<BidBean> lBean = agent.getBid(auctionBean);
			//2. 存储
			AuctionDao auctionDao = AuctionDaoImpl.getAuctionDao(getApplicationContext());
			auctionDao.insertPriceHistory(lBean.get(0), auctionBean.getCommodity());
		}

	}
	
	
}
