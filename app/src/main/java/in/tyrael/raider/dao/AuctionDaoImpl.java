package in.tyrael.raider.dao;

import in.tyrael.raider.bean.AuctionBean;
import in.tyrael.raider.bean.BidBean;
import in.tyrael.raider.bean.CommodityBean;
import in.tyrael.raider.dao.face.AuctionDao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * db应该是线程安全的
 * 该类基于db
 * soh是单例的
 * soh返回的应该是同一个
 * @author Tyrael
 * 
 * 该类是否有必要设计为单例模式？
 * 非单例的优点和缺点？
 * 
 * 该例的唯一状态和属性是soh，而soh是属于应用的，不由该类实例管理。
 * 
 * 该类的方法与soh紧密联系。
 * soh负责创建，该类负责查询
 * 
 * 该类的性质与soh一致，也应该设计为 单例模式。
 * 
 * 状态无关的方法应该设计为静态的
 *
 */
public class AuctionDaoImpl implements AuctionDao{
	private static SQLiteOpenHelper soh;
	//@GuardedBy(AuctionDaoImpl.class)
	private static AuctionDao ad; 
	
	/**
	 * @param 参数是应用的context
	 * 
	 */
	public static AuctionDao getAuctionDao(Context c) {
		soh = AuctionSQLiteHelper.getInstance(c);
		synchronized(AuctionDaoImpl.class){
			if(ad == null){
				ad = new AuctionDaoImpl();
			}
			return ad;
		}		
	}

	private AuctionDaoImpl() {

	}

	
	public void clearData(){
		SQLiteDatabase db = soh.getWritableDatabase();
		db.execSQL("drop table " + AuctionSQLiteHelper.TABLE_AUCTION + ";");
		db.execSQL("drop table " + AuctionSQLiteHelper.TABLE_BID + ";");
		db.execSQL("drop table " + AuctionSQLiteHelper.TABLE_COMMODITY + ";");
		db.execSQL("drop table " + AuctionSQLiteHelper.TABLE_PRICE_HISTORY + ";");
		db.execSQL("drop table " + AuctionSQLiteHelper.TABLE_RELATION_MY_AUCTION + ";");
		soh.onCreate(db);		
	}


	public void insertPriceHistory(BidBean bidBean, CommodityBean cb) {
		SQLiteDatabase db = soh.getWritableDatabase();
		db.execSQL(
				SQL_INSERT_PRICE_HISTORY,
				new String[] { bidBean.getUserName(),
						String.valueOf(bidBean.getTime()),
						String.valueOf(bidBean.getPrice()),
						String.valueOf(cb.getJdId()), });
	}

	public List<BidBean> getPriceHistory(CommodityBean commodityBean) {
		List<BidBean> la = new ArrayList<BidBean>();
		BidBean ab = null;
		SQLiteDatabase db = soh.getReadableDatabase();
		Cursor c = db.rawQuery(SQL_SELECT_PRICE_HISTORY,
				new String[] { String.valueOf(commodityBean.getJdId()) });
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ab = new BidBean();

			// int jdId = c.getInt(c.getColumnIndex("jd_id"));

			// ab.setJdId(auctionJdId);
			ab.set_id(c.getInt(c.getColumnIndex("_id")));
			ab.setUserName(c.getString(c.getColumnIndex("username")));
			ab.setTime(c.getLong(c.getColumnIndex("time")));
			ab.setPrice(c.getInt(c.getColumnIndex("price")));

			la.add(ab);
			c.moveToNext();
		}
		c.close();
		return la;
	}

	/**
	 * 不存在就插入，并返回包含id的对象 存在则升级
	 * 
	 * @param cb
	 * @return
	 */

	public CommodityBean insertOrUpdateCommodity(CommodityBean cb) {
		String sql1 = " select _id from " + AuctionSQLiteHelper.TABLE_COMMODITY
				+ " where name == ?; ";

		String sql = " insert into " + AuctionSQLiteHelper.TABLE_COMMODITY
				+ " (name, jd_price, jd_id, price_ideal, price_extreme) "
				+ "values" + "(?, ?, ?, ?, ?);";

		String sql2 = " update " + AuctionSQLiteHelper.TABLE_COMMODITY
				+ " set name = ?,  jd_price = ?, jd_id = ?, price_ideal = ?,"
				+ " price_extreme = ? " + " where name = ?;";

		SQLiteDatabase db = soh.getWritableDatabase();
		Cursor c = db.rawQuery(sql1, new String[] { cb.getName() });

		c.moveToFirst();
		// 不存在,则插入
		if (c.isAfterLast()) {
			db.execSQL(
					sql,
					new String[] { cb.getName(),
							String.valueOf(cb.getJdPrice()),
							String.valueOf(cb.getJdId()),
							String.valueOf(cb.getPriceIdeal()),
							String.valueOf(cb.getPriceExtreme()) });

		} else {
			db.execSQL(
					sql2,
					new String[] { cb.getName(),
							String.valueOf(cb.getJdPrice()),
							String.valueOf(cb.getJdId()),
							String.valueOf(cb.getPriceIdeal()),
							String.valueOf(cb.getPriceExtreme()), cb.getName() });
		}
		c.close();
		c = db.rawQuery(sql1, new String[] { cb.getName() });
		c.moveToFirst();
		cb.set_id(c.getInt(c.getColumnIndex("_id")));
		c.close();
		db.close();
		return cb;
	}

	/**
	 * 不存在就插入，并返回包含id的对象
	 * 
	 * @param cb
	 * @return
	 */
	public CommodityBean insertCommodity(CommodityBean cb) {
		// 如果已经存在就不用插入
		SQLiteDatabase db = soh.getWritableDatabase();
		Cursor c = db.rawQuery(SQL_SELECT_COMMODITY_BY_JDID, new String[] { String.valueOf(cb.getJdId()) });
		c.moveToFirst();
		// 不存在,则插入
		if (c.isAfterLast()) {
			db.execSQL(
					SQL_INSERT_COMMODITY,
					new String[] { cb.getName(),
							String.valueOf(cb.getJdPrice()),
							String.valueOf(cb.getJdId()),
							String.valueOf(cb.getPriceIdeal()),
							String.valueOf(cb.getPriceExtreme()) });
			c.close();
			c = db.rawQuery(SQL_SELECT_COMMODITY_BY_JDID, new String[] { String.valueOf(cb.getJdId()) });
			c.moveToFirst();
		}
		cb.set_id(c.getInt(c.getColumnIndex("_id")));
		c.close();
		db.close();
		return cb;
	}

	/**
	 * 物理主键意义不大 其中的commodity需要有id
	 * 
	 * @param ab
	 */
	public AuctionBean insertAuction(AuctionBean ab) {
		if (null == ab.getCommodity() || 0 == ab.getCommodity().getJdId()) {
			throw new RuntimeException("商品id不能为空");
		}

		String sql = " insert into "
				+ AuctionSQLiteHelper.TABLE_AUCTION
				+ "(jd_id, start_time, end_time, commodity_jd_id) values(?, ?, ?, ?);";

		SQLiteDatabase db = soh.getWritableDatabase();
		Cursor c = db.rawQuery(SQL_SELECT_AUCTION,
				new String[] { String.valueOf(ab.getJdId()) });
		c.moveToFirst();
		// 不存在,则插入
		if (c.isAfterLast()) {
			db.execSQL(
					sql,
					new String[] { String.valueOf(ab.getJdId()),
							String.valueOf(ab.getStartTime()),
							String.valueOf(ab.getEndTime()),
							String.valueOf(ab.getCommodity().getJdId()) });
			Log.d("test", "dao auction inserted");
		}
		c.close();
		db.close();
		return ab;
	}

	public void syncMyAuction(List<AuctionBean> lab) {
		for (AuctionBean ab : lab) {
			ab.setCommodity(insertCommodity(ab.getCommodity()));
			insertAuction(ab);
			insertAuctionWanted(ab);
		}
	}

	/**
	 * 插入想要的物品,仅仅是关系表
	 * 
	 * 
	 * @param ab
	 */
	public void insertAuctionWanted(AuctionBean ab) {
		String sql1 = " select auction_jd_id " + "from "
				+ AuctionSQLiteHelper.TABLE_RELATION_MY_AUCTION
				+ " where auction_jd_id == ?; ";

		String sql = " insert into "
				+ AuctionSQLiteHelper.TABLE_RELATION_MY_AUCTION
				+ " (auction_jd_id) values (?) ; ";

		SQLiteDatabase db = soh.getWritableDatabase();
		Cursor c = db.rawQuery(sql1,
				new String[] { String.valueOf(ab.getJdId()) });
		c.moveToFirst();
		// 不存在,则插入
		if (c.isAfterLast()) {
			db.execSQL(sql, new String[] { String.valueOf(ab.getJdId()) });
		}
		c.close();
		db.close();
	}

	public CommodityBean getCommodityByJdId(int id) {
		String sql = " select * from " + AuctionSQLiteHelper.TABLE_COMMODITY
				+ " c where c.jd_id == ?;";

		CommodityBean cb = new CommodityBean();
		SQLiteDatabase db = soh.getReadableDatabase();

		Cursor c = db.rawQuery(sql, new String[] { String.valueOf(id) });
		c.moveToFirst();
		if (!c.isAfterLast()) {
			cb.set_id(c.getInt(c.getColumnIndex("_id")));
			cb.setName(c.getString(c.getColumnIndex("name")));
			cb.setJdPrice(c.getInt(c.getColumnIndex("jd_price")));
			cb.setJdId(c.getInt(c.getColumnIndex("jd_id")));
			cb.setPriceIdeal(c.getInt(c.getColumnIndex("price_ideal")));
			cb.setPriceStepIdeal(c.getInt(c.getColumnIndex("price_extreme")));
		}

		c.close();
		// TODO soh.getReadableDatabase();获取的是同一个数据库实例？？？单例？？？
		// db.close();
		return cb;
	}

	// *********************
	// 方便函数
	public List<AuctionBean> getAuctionCurrent() {
		return getAuction(System.currentTimeMillis());
	}

	public List<AuctionBean> getAuctionAll() {
		return getAuction(0);
	}

	// /////////基础函数

	public List<AuctionBean> getAuction(long endTime) {
		String sql = " select * from " + AuctionSQLiteHelper.TABLE_AUCTION
				+ " a inner join "
				+ AuctionSQLiteHelper.TABLE_RELATION_MY_AUCTION + " raw "
				+ " on a.jd_id == raw.auction_jd_id "
				+ " where a.end_time > ? " + " order by a.end_time desc; ";
		String sql1 = " select * from " + AuctionSQLiteHelper.TABLE_BID + " b "
				+ " where b.auction_jd_id = ? " + " order by b.time desc; ";

		List<AuctionBean> la = new ArrayList<AuctionBean>();
		// 获取基本信息
		SQLiteDatabase db = soh.getReadableDatabase();

		String[] selectArgs = null;
		if (endTime > 0) {
			selectArgs = new String[] { String.valueOf(endTime) };
		} else {
			selectArgs = new String[] { String.valueOf(0) };
		}

		Cursor c = db.rawQuery(sql, selectArgs);
		AuctionBean ab = null;
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ab = new AuctionBean();
			int auctionJdId = c.getInt(c.getColumnIndex("jd_id"));
			ab.set_id(c.getInt(c.getColumnIndex("_id")));
			ab.setJdId(auctionJdId);
			ab.setStartTime(c.getLong(c.getColumnIndex("start_time")));
			ab.setEndTime(c.getLong(c.getColumnIndex("end_time")));

			// 获取商品信息
			ab.setCommodity(getCommodityByJdId(c.getInt(c
					.getColumnIndex("commodity_jd_id"))));

			// 获取出价信息
			List<BidBean> lb = new ArrayList<BidBean>();
			Cursor c1 = db.rawQuery(sql1,
					new String[] { String.valueOf(auctionJdId) });
			c1.moveToFirst();
			while (!c1.isAfterLast()) {
				BidBean b = new BidBean();
				b.set_id(c1.getInt(c1.getColumnIndex("_id")));
				b.setUserName(c1.getString(1));
				b.setTime(c1.getLong(2));
				b.setPrice(c1.getInt(3));
				b.setJdId(c1.getInt(c1.getColumnIndex("auction_jd_id")));// TODO
																			// 理解不正正确
				lb.add(b);
				c1.moveToNext();
			}
			c1.close();
			ab.setListBid(lb);

			la.add(ab);
			c.moveToNext();
		}
		c.close();
		db.close();
		return la;
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public List<AuctionBean> getMyAuction() {
		String sql = " select * from " + AuctionSQLiteHelper.TABLE_AUCTION
				+ " a inner join "
				+ AuctionSQLiteHelper.TABLE_RELATION_MY_AUCTION + " raw "
				+ " on a.jd_id == raw.auction_jd_id order by a.end_time desc; ";
		String sql1 = " select * from " + AuctionSQLiteHelper.TABLE_BID + " b "
				+ " where b.auction_jd_id = ? " + " order by b.time desc; ";

		List<AuctionBean> la = new ArrayList<AuctionBean>();
		// 获取基本信息
		SQLiteDatabase db = soh.getReadableDatabase();

		Cursor c = db.rawQuery(sql, null);
		AuctionBean ab = null;
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ab = new AuctionBean();
			int auctionJdId = c.getInt(c.getColumnIndex("jd_id"));
			ab.set_id(c.getInt(c.getColumnIndex("_id")));
			ab.setJdId(auctionJdId);
			ab.setStartTime(c.getLong(c.getColumnIndex("start_time")));
			ab.setEndTime(c.getLong(c.getColumnIndex("end_time")));

			// 获取商品信息
			ab.setCommodity(getCommodityByJdId(c.getInt(c
					.getColumnIndex("commodity_jd_id"))));

			// 获取出价信息
			List<BidBean> lb = new ArrayList<BidBean>();
			Cursor c1 = db.rawQuery(sql1,
					new String[] { String.valueOf(auctionJdId) });
			c1.moveToFirst();
			while (!c1.isAfterLast()) {
				BidBean b = new BidBean();
				b.set_id(c1.getInt(c1.getColumnIndex("_id")));
				b.setUserName(c1.getString(1));
				b.setTime(c1.getLong(2));
				b.setPrice(c1.getInt(3));
				b.setJdId(c1.getInt(c1.getColumnIndex("auction_jd_id")));
				lb.add(b);
				c1.moveToNext();
			}
			c1.close();
			ab.setListBid(lb);

			la.add(ab);
			c.moveToNext();
		}
		c.close();
		db.close();
		return la;
	}

	public List<CommodityBean> getCommodityAll() {
		CommodityBean cb = null;
		List<CommodityBean> lcb = new ArrayList<CommodityBean>();
		SQLiteDatabase db = soh.getReadableDatabase();
		Cursor c = db.rawQuery(SQL_SELECT_COMMODITY, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			cb = new CommodityBean();

			cb.set_id(c.getInt(c.getColumnIndex("_id")));
			cb.setName(c.getString(c.getColumnIndex("name")));
			cb.setJdPrice(c.getInt(c.getColumnIndex("jd_price")));
			cb.setJdId(c.getInt(c.getColumnIndex("jd_id")));
			cb.setPriceIdeal(c.getInt(c.getColumnIndex("price_ideal")));
			cb.setPriceStepIdeal(c.getInt(c.getColumnIndex("price_extreme")));

			lcb.add(cb);
			c.moveToNext();
		}

		c.close();
		// TODO soh.getReadableDatabase();获取的是同一个数据库实例？？？单例？？？
		// db.close();
		return lcb;
	}



	/**
	 * 
	 * @param ab
	 * @return
	 */
	public boolean existAuction(AuctionBean ab) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = soh.getReadableDatabase();
		Cursor c = db.rawQuery(SQL_SELECT_AUCTION,
				new String[] { String.valueOf(ab.getJdId()) });
		c.moveToFirst();
		// 不存在
		boolean result = false;
		if (c.isAfterLast()) {
			result = false;
		} else {
			result = true;
		}
		c.close();
		db.close();

		return result;
	}


	
	private static final String SQL_SELECT_AUCTION = " select jd_id from "
			+ AuctionSQLiteHelper.TABLE_AUCTION + " where jd_id == ?; ";
	private static final String SQL_SELECT_COMMODITY = " select * from "
			+ AuctionSQLiteHelper.TABLE_COMMODITY + " ;";
	private static final String SQL_SELECT_COMMODITY_BY_JDID = " select * from "
			+ AuctionSQLiteHelper.TABLE_COMMODITY + " where jd_id == ?; ";

	private static final String SQL_SELECT_PRICE_HISTORY = " select * from "
			+ AuctionSQLiteHelper.TABLE_PRICE_HISTORY
			+ " where commodity_jd_id = ?;";
	private static final String SQL_INSERT_PRICE_HISTORY = " insert into "
			+ AuctionSQLiteHelper.TABLE_PRICE_HISTORY
			+ " (username, time, price, commodity_jd_id-) " + "values"
			+ "(?, ?, ?, ?);";
	
	private static final String SQL_INSERT_COMMODITY = " insert into " + AuctionSQLiteHelper.TABLE_COMMODITY
			+ " (name, jd_price, jd_id, price_ideal, price_extreme) "
			+ "values" + "(?, ?, ?, ?, ?);";

}
