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
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


/**
 * 为了数据库的线程安全，该类设计为单例模式。
 * 应该用应用的context来构造。
 * @author Tyrael
 *
 */
public class AuctionSQLiteHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "auction.db";
	private static final int VERSION = 2;
	/*
	 * 商品和拍卖 	一对多 	同一种商品可能有多个拍卖,一个拍卖品只能是一个商品
	 * 拍卖和出价	一对多
	 * 
	 * 机主和拍卖	 多对多	一个机主可能想要多个拍卖品，一个拍卖品可能被多个机主想要	
	 * 目前考虑单一机主， 即一对多关系
	 * 
	 */
	static final String TABLE_AUCTION = "auction";
	static final String TABLE_COMMODITY = "commodity";
	static final String TABLE_BID = "bid";
	static final String TABLE_RELATION_MY_AUCTION = "realtion_my_auction";
	static final String TABLE_PRICE_HISTORY = "price_history";
	
	//sqlite integer 64bit
	//前缀jd来自京东
	private static final String SQL_CREATE_COMMODITY = "create table " + TABLE_COMMODITY +" ( " +
			//物理主键
			" _id integer primary key autoincrement, " +
			" name varchar(100) not null, " +
			//如果数据不存在，可能为0
			" jd_price integer, " +
			" jd_id integer not null, " +
			" price_ideal integer, " +
			" price_extreme integer " +
			")";
	private static final String SQL_CREATE_AUCTION = "create table " + TABLE_AUCTION +" ( " +
			" _id integer primary key autoincrement," +
			" jd_id integer not null," +
			" start_time integer," +
			" end_time integer,"+
			" commodity_jd_id integer" +
			")";
	//实际上下面两个表都是存的是jd bid记录
	//一个是拍卖一对多，一个是商品一对多
	private static final String SQL_CREATE_BID = "create table " + TABLE_BID +" ( " +
			//物理主键
			" _id integer primary key autoincrement," +
			" username varchar(30) not null," +
			" time integer not null," +
			" price integer not null," +
			" auction_jd_id integer not null"+
			")";
	private static final String SQL_CREATE_PRICE_HISTORY = "create table " + TABLE_PRICE_HISTORY +" ( " +
			//物理主键
			" _id integer primary key autoincrement," +
			" username varchar(30) not null," +
			" time integer not null," +
			" price integer not null," +
			" commodity_jd_id integer not null"+
			")"; 
	//想要参与的拍卖
	private static final String SQL_CREATE_MY_AUCTION = "create table " + TABLE_RELATION_MY_AUCTION +" ( " +
			//物理主键
			" _id integer primary key autoincrement," +
			" auction_jd_id integer not null"+
			")";
	
	private static AuctionSQLiteHelper auctionSQLiteHelper;
	
	public static synchronized AuctionSQLiteHelper getInstance(Context context){
		if(auctionSQLiteHelper == null){
			auctionSQLiteHelper = new AuctionSQLiteHelper(context);
		}
		return auctionSQLiteHelper;
	}
	
	
	private AuctionSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	private AuctionSQLiteHelper(Context context) {
		this(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_COMMODITY);
		db.execSQL(SQL_CREATE_AUCTION);
		db.execSQL(SQL_CREATE_BID);
		db.execSQL(SQL_CREATE_PRICE_HISTORY);
		db.execSQL(SQL_CREATE_MY_AUCTION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
