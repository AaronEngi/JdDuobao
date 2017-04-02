package in.tyrael.raider.service;

import in.tyrael.raider.RaiderUtil;
import in.tyrael.raider.activity.MainActivity;
import in.tyrael.raider.bean.AuctionBean;
import in.tyreal.raider.net.RaiderHttpAgent;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

//TODO 同步锁
/**
 * 设计该类的原因是因为 
 * 1. 无法区分多个pendingintent 
 * 2. 执行定时任务
 * 
 * TODO 可以为每个闹钟设定一个requescode来区分
 * 
 * @author DELL
 * 
 */
public class BidService extends Service {
	public static final String RECEIVER_STORE_COMMAND = "in.tyrael.raider.BidService.STORE_COMMAND";
	public static final String RECEIVER_EXCUTE_COMMAND = "in.tyrael.raider.BidService.EXECUTE_COMMAND";
	
	private final Logger log = Logger.getLogger(BidService.class);
	// TODO 考虑同时执行的任务
	// 时间 任务
	// 使用一个堆，来保存发送任务。
	// 两个状态变量
	// TODO map 的持久化，防止服务被不正常终止
	SortedMap<Long, BidCommand> smBid = new TreeMap<Long, BidCommand>();
	//等待触发的命令
	BidCommand pendingCommand;
	RaiderHttpAgent raiderHttpAgent;
	AlarmManager am;

	// 跨函数变量
	private Intent intent;
	private PendingIntent pendingIntent;

	private CommandStorerReceiver rCommandStorer = new CommandStorerReceiver();
	private CommandExecutorReceiver rCommandExecutor = new CommandExecutorReceiver();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// TODO 1. 确定开始时是不是这个方法
	// 2. 同步问题
	public void onCreate() {

		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		raiderHttpAgent = RaiderHttpAgent.getInstance();
		// 登录问题交给agent负责
		intent = new Intent();
		intent.setAction(BidService.RECEIVER_EXCUTE_COMMAND);
		pendingIntent = PendingIntent.getBroadcast(BidService.this, 0, intent,
				0);

		IntentFilter filter = new IntentFilter(RECEIVER_STORE_COMMAND);
		registerReceiver(rCommandStorer, filter);

		filter = new IntentFilter(RECEIVER_EXCUTE_COMMAND);
		registerReceiver(rCommandExecutor, filter);

	}

	@Override
	// TODO
	public void onDestroy() {
		unregisterReceiver(rCommandStorer);
		unregisterReceiver(rCommandExecutor);
	}

	// 任务接收器
	public class CommandStorerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("raiderservice", "received2");

			// 接受一个AuctionBean
			AuctionBean ab = (AuctionBean) intent
					.getSerializableExtra("AuctionBean");
			// TODO 1. 检查是否已经存在该auction,如果存在则删除、更新

			// 创建command
			if (ab == null) {
				return;
			}

			BidCommand c = new BidCommand();
			c.setAuctionBean(ab);
			c.setExecuteTime(ab.getEndTime() - 5000);// 提前5秒出价
			log.info("设定时间" + RaiderUtil.bidDateFormat.format(new Date(ab.getEndTime() - 5000)));

			// 检查当前执行的闹钟是否早于新来的命令
			// 新的早，取消之前的闹钟，执行新的闹钟
			// 新的迟，插入有序map
			// TODO 取消时，是否 intent的第一个context具有决定性
			
			synchronized (BidService.this) {
				long elapsed = 0;
				if (pendingCommand == null) {
					pendingCommand = c;
					elapsed = RaiderHttpAgent.elapsedTime(c.getExecuteTime()); 
					am.set(AlarmManager.ELAPSED_REALTIME, elapsed, pendingIntent);

				} else if (c.getExecuteTime() < pendingCommand.getExecuteTime()) {
					am.cancel(pendingIntent);
					smBid.put(pendingCommand.getExecuteTime(), pendingCommand);

					pendingCommand = c;
					elapsed = RaiderHttpAgent.elapsedTime(c.getExecuteTime());
					am.set(AlarmManager.RTC, elapsed, pendingIntent);

				} else {
					smBid.put(c.getExecuteTime(), c);
				}
			}
			
			//日志
			log.info(ab.getCommodity().getName() + "：	出价信息已保存1");
		}
	}

	// 有个alarmservice负责定时发送广播

	// 有个reciever接受处理发送程序
	// 任务执行器
	public class CommandExecutorReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			log.info("触发时间" + RaiderUtil.bidDateFormat.format(new Date(System.currentTimeMillis())));
			new Thread(new ThreadBid()).start();

		}
	}

	private class ThreadBid implements Runnable {

		@Override
		public void run() {
			synchronized (BidService.this) {
				if (pendingCommand == null) {
					throw new RuntimeException("command null");
				}
				
				pendingCommand.bid(raiderHttpAgent);				
				pendingCommand = null;
								
				// 设定下一个闹钟
				if (!smBid.isEmpty()) {
					pendingCommand = smBid.get(smBid.firstKey());
					smBid.remove(smBid.firstKey());
					long elapsed = RaiderHttpAgent.elapsedTime(pendingCommand.getExecuteTime());
					am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
							elapsed, pendingIntent);
				}
			}
		}

	}
}
