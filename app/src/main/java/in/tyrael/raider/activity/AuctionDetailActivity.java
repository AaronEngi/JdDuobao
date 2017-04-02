package in.tyrael.raider.activity;

import in.tyrael.raider.R;
import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.dao.AuctionDaoImpl;
import in.tyrael.raider.dao.face.AuctionDao;
import in.tyrael.raider.service.BidService;
import in.tyreal.raider.net.RaiderHttpAgent;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AuctionDetailActivity extends Activity {
	private static final int BID_MESSAGE_WHAT = 168;
	private final Logger log = Logger.getLogger(MainActivity.class);

	AuctionBean auctionBean;
	RaiderHttpAgent raiderHttpAgent;
	AuctionDao auctionDao;
	
	TextView tvPriceInstant;
	TextView tvPriceIdeal;
	TextView tvPriceExtreme;

	LvBidAdapter lvBidAdapter;

	Handler bidHandler = new Handler() {
		@Override
		public void handleMessage(Message m) {
			if (m.what == BID_MESSAGE_WHAT) {
				lvBidAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auction_detail);

		ButtonOnClickListener bnListener = new ButtonOnClickListener();
		lvBidAdapter = new LvBidAdapter(AuctionDetailActivity.this);
		
		auctionDao = AuctionDaoImpl.getAuctionDao(getApplicationContext());

		Button bnBidInstant = (Button) findViewById(R.id.bn_detail_bid_instant);
		tvPriceInstant = (TextView) findViewById(R.id.tv_detail_price_instant);

		Button bnSave = (Button) findViewById(R.id.bn_detail_save);
		tvPriceIdeal = (TextView) findViewById(R.id.tv_detail_ideal_price);
		tvPriceExtreme = (TextView) findViewById(R.id.tv_detail_extreme_price);

		TextView tvDetailName = (TextView) findViewById(R.id.tv_detail_name);
		TextView tvDetailJdPrice = (TextView) findViewById(R.id.tv_detail_price);
		ListView lvBid = (ListView) findViewById(R.id.lv_bid);
		lvBid.setAdapter(lvBidAdapter);

		auctionBean = (AuctionBean) getIntent().getSerializableExtra(
				"AuctionBean");
		if (auctionBean != null || auctionBean.getCommodity() != null) {
			tvDetailName.setText(auctionBean.getCommodity().getName());
			tvDetailJdPrice.setText(Integer.toString(auctionBean.getCommodity()
					.getJdPrice() / 100) + "元");
			tvPriceIdeal.setText(Integer.toString(auctionBean.getCommodity().getPriceIdeal() / 100)
					+ "元");
			tvPriceExtreme.setText(Integer.toString(auctionBean.getCommodity().getPriceExtreme() / 100)
					+ "元");
			raiderHttpAgent = RaiderHttpAgent.getInstance();
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					lvBidAdapter.setListBidBean(raiderHttpAgent.getBid(auctionBean));
					bidHandler.removeMessages(BID_MESSAGE_WHAT); // 移除过时的消息
					bidHandler.sendEmptyMessage(BID_MESSAGE_WHAT); // 告诉主线程，更新
				}
			}, 0, 800);

			bnSave.setOnClickListener(bnListener);
			bnBidInstant.setOnClickListener(bnListener);

		}

	}

	private class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bn_detail_bid_instant:
				final float priceInstant = Float.valueOf(tvPriceInstant
						.getText().toString());
				new Thread(new Runnable() {

					@Override
					public void run() {
						raiderHttpAgent.bid(auctionBean,
								(int) (priceInstant * 100));
						// Toast.makeText(AuctionDetailActivity.this,
						// "出价成功",
						// Toast.LENGTH_LONG);
					}
				}).start();
				break;
			case R.id.bn_detail_save:				
				float priceIdeal = Float.valueOf(tvPriceIdeal.getText()
						.toString());
				float priceExtreme = Float.valueOf(tvPriceExtreme.getText()
						.toString());
				auctionBean.getCommodity().setPriceIdeal(
						(int) (priceIdeal * 100));
				auctionBean.getCommodity().setPriceExtreme(
						(int) (priceExtreme * 100));
				// 数据库保存
				auctionDao.insertOrUpdateCommodity(auctionBean
						.getCommodity());
				// 出价消息发送
				Intent i = new Intent();
				i.putExtra("AuctionBean", auctionBean);
				i.setAction(BidService.RECEIVER_STORE_COMMAND);
				sendBroadcast(i);
				
				Toast.makeText(AuctionDetailActivity.this, "：	出价信息已广播", Toast.LENGTH_SHORT).show();
				//****************
				//日志
				log.info(auctionBean.getCommodity().getName() + "：	出价信息已广播");
				
				break;
			}

		}

	}

}
