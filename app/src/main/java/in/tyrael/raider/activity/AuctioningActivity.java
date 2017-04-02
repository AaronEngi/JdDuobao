package in.tyrael.raider.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.tyrael.raider.R;
import in.tyrael.raider.R.layout;
import in.tyrael.raider.R.menu;
import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.dao.AuctionDaoImpl;
import in.tyrael.raider.dao.AuctionSQLiteHelper;
import in.tyrael.raider.dao.face.AuctionDao;
import in.tyrael.raider.model.AuctionModel;
import in.tyreal.raider.net.RaiderHttpAgent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AuctioningActivity extends Activity {
	List<AuctionBean> lab;
	BaseAdapter lvAuctionAdapter = new LvAuctionAdapter();
	Handler auctionHandler = new AuctionHandler();
	AuctionModel auctionModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auctioning);

		ButtonClicker bnClicker = new ButtonClicker();
		
		auctionModel = new AuctionModel(AuctioningActivity.this);
		AuctionDao ad = AuctionDaoImpl.getAuctionDao(getApplicationContext());
		lab = ad.getAuctionCurrent();

		ListView lvAuction = (ListView) findViewById(R.id.lv_auctioning);
		lvAuction.setAdapter(lvAuctionAdapter);
		lvAuction.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(AuctioningActivity.this,
						AuctionDetailActivity.class);
				intent.putExtra("AuctionBean", lab.get(position));
				startActivity(intent);
			}
		});

		Button bnRefresh = (Button) findViewById(R.id.bn_auctioning_refresh);
		bnRefresh.setOnClickListener(bnClicker);
	}

	private class ButtonClicker implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.bn_auctioning_refresh:
				// 创建一个线程，读取夺宝箱的内容
				new Thread(new Runnable() {
					@Override
					public void run() {
						auctionModel.syncAuction();
						
						AuctionDao ad = AuctionDaoImpl.getAuctionDao(getApplicationContext());
						
						Log.d("browser", "我的拍卖列表更新完成1");
						lab = ad.getAuctionCurrent();
						auctionHandler.sendEmptyMessage(0);
					}

				}).start();
				break;
			}

		}
	}

	private class AuctionHandler extends Handler {
		@Override
		public void handleMessage(Message m) {
			lvAuctionAdapter.notifyDataSetChanged();
		}
	}

	private class LvAuctionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (lab == null) {
				return 0;
			}
			return lab.size();
		}

		@Override
		public Object getItem(int position) {
			if (lab == null) {
				return null;
			}
			return lab.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			// if(convertView == null){
			LayoutInflater layoutInflater = LayoutInflater
					.from(AuctioningActivity.this);
			view = layoutInflater.inflate(R.layout.lv_item_auctioning, null);
			Log.d("auction", "done1");
			// }else{
			// view = convertView;
			// }
			TextView tvName = (TextView) view.findViewById(R.id.tv_name);
			TextView tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
			// tvName.setText("234");
			Log.d("auction", "done2");
			if (lab != null && position < lab.size()) {
				tvName.setText(lab.get(position).getCommodity().getName());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				tvEndTime.setText(sdf.format(new Date(lab.get(position)
						.getEndTime())));
			}
			return view;
		}

	}

}
