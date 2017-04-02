package in.tyrael.raider.activity;


import java.util.List;

import in.tyrael.raider.R;

import in.tyrael.raider.bean.CommodityBean;
import in.tyrael.raider.dao.AuctionDaoImpl;
import in.tyrael.raider.dao.AuctionSQLiteHelper;
import in.tyrael.raider.dao.face.AuctionDao;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CommodityListActivity extends Activity {
	List<CommodityBean> lab;
	BaseAdapter lvAdapter = new LvAdapter();
	Handler commodityHandler = new CommodityHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commidity_list);		

		AuctionDao ad = AuctionDaoImpl.getAuctionDao(getApplicationContext());
		lab = ad.getCommodityAll();

		ListView lv = (ListView) findViewById(R.id.lv_commodity);
		lv.setAdapter(lvAdapter);		
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent();
				intent.setClass(CommodityListActivity.this, CommodityDetailActivity.class);
				intent.putExtra("CommodityBean", lab.get(position));
				startActivity(intent);				
			}});
		
	}
	
	private class CommodityHandler extends Handler{
		@Override
		public void handleMessage(Message m) {
			lvAdapter.notifyDataSetChanged();
		}
	} 
	
	private class LvAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(lab == null){
				return 0;
			}
			return lab.size();
		}

		@Override
		public Object getItem(int position) {
			if(lab == null){
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
					.from(CommodityListActivity.this);
			view = layoutInflater
					.inflate(R.layout.lv_item_commodity_list, null);
			// }else{
			// view = convertView;
			// }
			TextView tvName = (TextView) view.findViewById(R.id.tv_commodity_name);

			if (lab != null && position < lab.size()) {
				tvName.setText(lab.get(position).getName());
			}
			return view;
		}
		
	}
	
}
