package in.tyrael.raider.dao.face;

import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.bean.BidBean;
import in.tyrael.raider.bean.CommodityBean;

import java.util.List;

/**
 * 
 * @author Tyrael
 *
 */
public interface AuctionDao {
	//默认 public abstrct
	void clearData();
	
	/**
	 * 
	 * @param ab
	 * @return
	 */
	public boolean existAuction(AuctionBean ab);

	public List<AuctionBean> getAuction(long endTime);

	public List<AuctionBean> getAuctionAll();

	// *********************
	// 方便函数
	public List<AuctionBean> getAuctionCurrent();

	public List<CommodityBean> getCommodityAll();
	public CommodityBean getCommodityByJdId(int id);

	/**
	 * TODO
	 * 
	 * @return
	 */
	public List<AuctionBean> getMyAuction();

	public List<BidBean> getPriceHistory(CommodityBean commodityBean);

	/**
	 * 物理主键意义不大 其中的commodity需要有id
	 * 
	 * @param ab
	 */
	public AuctionBean insertAuction(AuctionBean ab);

	/**
	 * 插入想要的物品,仅仅是关系表
	 * 
	 * 
	 * @param ab
	 */
	public void insertAuctionWanted(AuctionBean ab);

	// /////////基础函数

	/**
	 * 不存在就插入，并返回包含id的对象
	 * 
	 * @param cb
	 * @return
	 */
	public CommodityBean insertCommodity(CommodityBean cb);

	/**
	 * 不存在就插入，并返回包含id的对象 存在则升级
	 * 
	 * @param cb
	 * @return
	 */

	public CommodityBean insertOrUpdateCommodity(CommodityBean cb);

	void insertPriceHistory(BidBean bidBean, CommodityBean cb);



	public void syncMyAuction(List<AuctionBean> lab);

}
