package in.tyrael.raider.model;

import in.tyrael.raider.bean.CommodityBean;
import in.tyrael.raider.model.face.PricePolicy;

public class PricePolicyImpl implements PricePolicy {
	private float coefficientExtreme = 0.8f;
	private float coefficientIdeal = 0.5f; 

	@Override
	public CommodityBean setPrice(CommodityBean commodityBean) {
		int pExtreme = (int) (coefficientExtreme * commodityBean.getJdPrice());
		int pIdeal = (int) (coefficientIdeal * commodityBean.getJdPrice());
		commodityBean.setPriceStepExtreme(pExtreme);
		commodityBean.setPriceStepExtreme(pIdeal);		
		return commodityBean;
	}

}
