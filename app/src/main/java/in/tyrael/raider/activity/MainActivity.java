package in.tyrael.raider.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.log4j.Logger;

import java.util.Date;

import in.tyrael.raider.R;
import in.tyrael.raider.RaiderUtil;
import in.tyrael.raider.dao.AuctionDaoImpl;
import in.tyrael.raider.dao.face.AuctionDao;
import in.tyrael.raider.databinding.ActivityMainBinding;
import in.tyrael.raider.log.ConfigureLog4J;
import in.tyrael.raider.service.BidService;
import in.tyrael.raider.service.BidService.CommandExecutorReceiver;
import in.tyreal.raider.net.RaiderHttpAgent;

public class MainActivity extends Activity implements OnClickListener {
	private final Logger log = Logger.getLogger(MainActivity.class);
	private TextView tvServerTime;
	private EditText etDuobaoId;

	private ActivityMainBinding viewBinding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ConfigureLog4J.configure();

		viewBinding = ActivityMainBinding.inflate(getLayoutInflater());

		setContentView(viewBinding.getRoot());
		findViewById(R.id.tv_auctioning).setOnClickListener(this);
		findViewById(R.id.tv_login).setOnClickListener(this);
		findViewById(R.id.tv_commodity_list).setOnClickListener(this);
		findViewById(R.id.tv_clear).setOnClickListener(this);
		findViewById(R.id.main_bn_get_time).setOnClickListener(this);
		tvServerTime = findViewById(R.id.main_tv_server_time);
		etDuobaoId = findViewById(R.id.et_duobaoId);

		viewBinding.btToDuobao.setOnClickListener(this);

		// 启动服务
		Intent i = new Intent(MainActivity.this, BidService.class);
		startService(i);

		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		Intent intent2 = new Intent(MainActivity.this,
				CommandExecutorReceiver.class);
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(
//				MainActivity.this, 0, intent2, Intent.FLAG_ACTIVITY_NEW_TASK);
//		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000,
//				pendingIntent);
		Log.d("raiderservice", "set");
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		int id = v.getId();
		if (v == viewBinding.btToDuobao) {
			int duoBaoId = Integer.parseInt(etDuobaoId.getText().toString());
			intent = new Intent();
			intent.setClass(MainActivity.this, AuctionDetailActivity.class);
			intent.putExtra(AuctionDetailActivity.BUNDLE_DUOBAO_ID, duoBaoId);
			startActivity(intent);
		} else if (id == R.id.tv_auctioning) {
			intent = new Intent();
			intent.setClass(MainActivity.this, AuctioningActivity.class);
			startActivity(intent);
		} else if (id == R.id.tv_login) {
			intent = new Intent();
			intent.setClass(MainActivity.this, BrowserActivity.class);
			startActivity(intent);
		} else if (id == R.id.tv_commodity_list) {
			intent = new Intent();
			intent.setClass(MainActivity.this, CommodityListActivity.class);
			startActivity(intent);
		} else if (id == R.id.tv_clear) {// TODO
			AuctionDao ad = AuctionDaoImpl
					.getAuctionDao(getApplicationContext());
			ad.clearData();
			Toast.makeText(MainActivity.this, "数据已清除", Toast.LENGTH_SHORT)
					.show();
		} else if (id == R.id.main_bn_get_time) {
			log.info("按下按钮");
			new TimeAsyncTask().execute((Void) null);
		}

	}

	private class TimeAsyncTask extends AsyncTask<Void, Void, Long> {

		@Override
		protected Long doInBackground(Void... params) {
			log.info("开始更新1");
			long time = -RaiderHttpAgent.elapsedTime(0);
			log.info("开始更新1" + time);
			return time;
		}

		@Override
		protected void onPostExecute(Long result) {
			log.info("更新");
			tvServerTime.setText(RaiderUtil.bidDateFormat.format(new Date(
					result)));
		}

	}

}
