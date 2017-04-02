package in.tyrael.raider.activity;

import in.tyrael.raider.R;
import in.tyrael.raider.bean.BidBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LvBidAdapter extends BaseAdapter{
	public LvBidAdapter(Context context) {
		super();
		this.context = context;
	}

	List<BidBean> listBidBean;
	Context context;
	
	@Override
	public int getCount() {
		if (listBidBean == null)
			return 0;
		return listBidBean.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (listBidBean == null)
			return null;
		return listBidBean.get(position);
	}

	@Override
	// TODO
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	// TODO
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (listBidBean == null)
			return null;
		View view = null;
		LayoutInflater layoutInflater = LayoutInflater
				.from(context);
		view = layoutInflater.inflate(R.layout.lv_item_bid, null);
		TextView tvUserName = (TextView) view
				.findViewById(R.id.tv_bid_user_name);
		TextView tvPrice = (TextView) view.findViewById(R.id.tv_bid_price);
		TextView tvTime = (TextView) view.findViewById(R.id.tv_bid_time);
		tvUserName.setText(listBidBean.get(position).getUserName());
		tvPrice.setText(String
				.valueOf(listBidBean.get(position).getPrice() / 100));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		tvTime.setText(sdf.format(new Date(listBidBean.get(position)
				.getTime())));
		return view;
	}

	public void setListBidBean(List<BidBean> listBidBean) {
		this.listBidBean = listBidBean;
	}
}
