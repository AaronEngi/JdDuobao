package in.tyrael.raider.bean;

import java.io.Serializable;
import java.util.List;

public class AuctionBean implements Serializable{
	// 物理id
	private int _id;
	private int jdId;
	private long startTime;
	private long endTime;
	private List<BidBean> listBid;
	private CommodityBean commodity;


	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getJdId() {
		return jdId;
	}

	public void setJdId(int jdId) {
		this.jdId = jdId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<BidBean> getListBid() {
		return listBid;
	}

	public void setListBid(List<BidBean> listBid) {
		this.listBid = listBid;
	}

	public AuctionBean(int _id, int jdId, long startTime, long endTime,
			List<BidBean> listBid) {
		this._id = _id;
		this.jdId = jdId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.listBid = listBid;
	}

	public AuctionBean() {
	}

	public CommodityBean getCommodity() {
		return commodity;
	}

	public void setCommodity(CommodityBean commodity) {
		this.commodity = commodity;
	}


}
