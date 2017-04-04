package in.tyreal.raider.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

/**
 * 实现非业务逻辑，通用功能
 * @author DELL
 *
 */
public class RaiderHttpUtil {
	
	// 模拟电脑访问
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36";

	public static final String URL_AUCTION_DETAIL = "http://auction.jd.com/detail/"; //http://auction.jd.com/detail/3984525
	public static final String URL_ITEM = "http://item.jd.com/";//http://item.jd.com/862379.html
	public static final String URL_MY_AUCTION = "https://dbd.jd.com/myTreasureBox.html";
	static final String URL_SERVER_TIME = "http://auction.jd.com/json/paimai/now";
	//https://paimai.jd.com/services/now.action?t=1491147324856&callback=jQuery3470348&_=1491147324859
	
	public static final String URL_GET_BID = "http://auction.jd.com/json/paimai/bid_records?pageSize=2147483647&dealId=";//http://auction.jd.com/json/paimai/bid_records?dealId=3988052
	public static final String URL_BID = "http://auction.jd.com/json/paimai/bid?";
	//http://auction.jd.com/json/paimai/bid?t=1385019456535&dealId=3991662&price=2
			
	public static String getHtml(String url, String cookie){
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		if(cookie != null){
			httpget.addHeader("Cookie", cookie);
		}
		
		HttpResponse response;

		StringBuilder sb = new StringBuilder();
		try {
			response = httpclient.execute(httpget);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = null;
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
		
	/**
	 * get方法，返回html字符串
	 * @param url
	 * @return
	 */
	public static String getHtml(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;

		StringBuilder sb = new StringBuilder();
		try {
			response = httpclient.execute(httpget);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = null;
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

}
