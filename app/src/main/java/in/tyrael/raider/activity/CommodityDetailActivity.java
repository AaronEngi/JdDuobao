package in.tyrael.raider.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.tyrael.raider.R;
import in.tyrael.raider.R.layout;
import in.tyrael.raider.R.menu;

import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.bean.BidBean;
import in.tyrael.raider.bean.CommodityBean;
import in.tyrael.raider.dao.AuctionDaoImpl;
import in.tyrael.raider.dao.AuctionSQLiteHelper;
import in.tyrael.raider.dao.face.AuctionDao;
import in.tyrael.raider.service.BidService;
import in.tyreal.raider.net.RaiderHttpAgent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CommodityDetailActivity extends Activity {
	CommodityBean commodityBean;
	AuctionDao auctionDao;
	LvBidAdapter lvBidAdapter;

	TextView tvPriceIdeal;
	TextView tvPriceExtreme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commodity_detail);

		ButtonOnClickListener bnListener = new ButtonOnClickListener();
		
		auctionDao = AuctionDaoImpl.getAuctionDao(getApplicationContext());

		Button bnSave = (Button) findViewById(R.id.bn_detail_save);
		tvPriceIdeal = (TextView) findViewById(R.id.tv_detail_ideal_price);
		tvPriceExtreme = (TextView) findViewById(R.id.tv_detail_extreme_price);

		TextView tvDetailName = (TextView) findViewById(R.id.tv_detail_name);
		TextView tvDetailJdPrice = (TextView) findViewById(R.id.tv_detail_price);

		// ListView lvBid = (ListView) findViewById(R.id.lv_bid);
		// lvBid.setAdapter(lvBidAdapter);

		commodityBean = (CommodityBean) getIntent().getSerializableExtra(
				"CommodityBean");
		if (commodityBean != null) {
			tvDetailName.setText(commodityBean.getName());
			tvDetailJdPrice
					.setText(Integer.toString(commodityBean.getJdPrice() / 100)
							+ "元");
			tvPriceIdeal.setText(Integer.toString(commodityBean.getPriceIdeal() / 100)
					+ "元");
			tvPriceExtreme.setText(Integer.toString(commodityBean.getPriceExtreme() / 100)
					+ "元");

			bnSave.setOnClickListener(bnListener);

		}
		
		lvBidAdapter = new LvBidAdapter(CommodityDetailActivity.this);
		List<BidBean> lbb = AuctionDaoImpl.getAuctionDao(CommodityDetailActivity.this).getPriceHistory(commodityBean);
		lvBidAdapter.setListBidBean(lbb);
		ListView lvBid = (ListView) findViewById(R.id.lv_bid);
		lvBid.setAdapter(lvBidAdapter);

	}

	private class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bn_detail_save:
				float priceIdeal = Float.valueOf(tvPriceIdeal.getText()
						.toString());
				float priceExtreme = Float.valueOf(tvPriceExtreme.getText()
						.toString());
				commodityBean.setPriceIdeal(
						(int) (priceIdeal * 100));
				commodityBean.setPriceExtreme(
						(int) (priceExtreme * 100));
				// 数据库保存
				auctionDao.insertOrUpdateCommodity(commodityBean);
				break;
			}

		}

	}
}
