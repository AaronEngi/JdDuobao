package test;

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
	public static final String URL_MY_AUCTION = "http://auction.jd.com/interest.action";
	public static final String URL_HOST = "http://jd.com";
	static final String URL_SERVER_TIME = "http://auction.jd.com/json/paimai/now";
	
	public static final String URL_GET_BID = "http://auction.jd.com/json/paimai/bid_records?pageSize=2147483647&dealId=";//http://auction.jd.com/json/paimai/bid_records?dealId=3988052
	public static final String URL_BID = "http://auction.jd.com/json/paimai/bid?";
	//http://auction.jd.com/json/paimai/bid?t=1385019456535&dealId=3991662&price=2
	
	public static final String DOMAIN_HOST = "jd.com";
	
	public static String getHtml(String url, HttpClient httpclient){
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;

		StringBuilder sb = new StringBuilder();
		try {
			response = httpclient.execute(httpget);
			// int code = response.getStatusLine().getStatusCode();
			// System.out.print("code is=" + code);// 返回200是正确的
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
	
	public static String getHtml(String url, HttpClient httpclient, String cookie){
		HttpGet httpget = new HttpGet(url);
		if(cookie != null){
			httpget.addHeader("Cookie", cookie);
		}
		
		HttpResponse response;

		StringBuilder sb = new StringBuilder();
		try {
			response = httpclient.execute(httpget);
			// int code = response.getStatusLine().getStatusCode();
			// System.out.print("code is=" + code);// 返回200是正确的
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
	 * 貌似无用
	 * @param url
	 * @param httpContext
	 * @return
	 */
	private static String getHtml(String url, HttpContext httpContext){
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;

		StringBuilder sb = new StringBuilder();
		try {
			response = httpclient.execute(httpget, httpContext);
			// int code = response.getStatusLine().getStatusCode();
			// System.out.print("code is=" + code);// 返回200是正确的
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
	 * @param cookie
	 * @return
	 */
	public static String getHtml(String url, String cookie) {
		BasicCookieStore cookieJar = RaiderHttpUtil
				.parseStringToCookieStore(cookie);
		// Log.d("page", cookieJar.toString());
		BasicHttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieJar);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;

		StringBuilder sb = new StringBuilder();
		try {
			response = httpclient.execute(httpget, localContext);
			// int code = response.getStatusLine().getStatusCode();
			// System.out.print("code is=" + code);// 返回200是正确的
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
			// int code = response.getStatusLine().getStatusCode();
			// System.out.print("code is=" + code);// 返回200是正确的
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
	 * 解析有问题，cookie中可能含有等号。 这里分解的应该是第一个等号
	 * 
	 * 用正则表达式来做。杀鸡牛刀？
	 * 
	 * @param strCookie
	 * @return
	 */
	public static BasicCookieStore parseStringToCookieStore(String strCookie) {
		Pattern p = Pattern.compile("(.+)=(.+)");
		BasicCookieStore cookieJar = new BasicCookieStore();
		String _cookie = strCookie;
		if (_cookie != null && !_cookie.equals("")) {
			String[] cookies = _cookie.split(";");
			for (int i = 0; i < cookies.length; i++) {
				Log.d("util", cookies[i]);
				Matcher m = p.matcher(cookies[i]);
				if (m.find()) {
					BasicClientCookie c = new BasicClientCookie(m.group(1),
							m.group(2));
					c.setDomain(DOMAIN_HOST);// 这里是自己的主机地址
					cookieJar.addCookie(c);
				} else {
					throw new RuntimeException("cookie字符串 解析错误");
				}
			}
		}
		return cookieJar;
	}
	

}
