package in.tyrael.raider.bean;

public class BidBean {
	private int _id;
	private int jdId;
	private long time;
	private int price;// 以分为单位
	private String userName;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BidBean() {
		super();
	}

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
}
