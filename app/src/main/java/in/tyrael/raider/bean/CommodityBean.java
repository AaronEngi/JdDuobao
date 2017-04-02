package in.tyrael.raider.bean;

import java.io.Serializable;

public class CommodityBean implements Serializable{
	private int _id;
	private String name;
	private int jdPrice;//以分为单位
	private int jdId;//京东的商品id
	
	private int priceIdeal;
	private int priceExtreme;
	private int priceStepIdeal = 3600;
	private int priceStepExtreme = 600;

	@Override 
	/**
	 * 以name为判断标准
	 */
	public boolean equals(Object o){
		if(this == o) return true;
		if(o instanceof CommodityBean ){
			return this.name.endsWith( ((CommodityBean)o).name);
		}else return false;
	} 
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CommodityBean() {
		super();
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getJdPrice() {
		return jdPrice;
	}

	public void setJdPrice(int jdPrice) {
		this.jdPrice = jdPrice;
	}

	public int getJdId() {
		return jdId;
	}

	public void setJdId(int jdId) {
		this.jdId = jdId;
	}

	public int getPriceIdeal() {
		return priceIdeal;
	}

	public void setPriceIdeal(int priceIdeal) {
		this.priceIdeal = priceIdeal;
	}

	public int getPriceExtreme() {
		return priceExtreme;
	}

	public void setPriceExtreme(int priceExtreme) {
		this.priceExtreme = priceExtreme;
	}

	public int getPriceStepIdeal() {
		return priceStepIdeal;
	}

	public void setPriceStepIdeal(int priceStepIdeal) {
		this.priceStepIdeal = priceStepIdeal;
	}

	public int getPriceStepExtreme() {
		return priceStepExtreme;
	}

	public void setPriceStepExtreme(int priceStepExtreme) {
		this.priceStepExtreme = priceStepExtreme;
	}
}
