package in.tyrael.raider.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import in.tyrael.raider.R;
import in.tyreal.raider.net.RaiderHttpAgent;
import in.tyreal.raider.net.RaiderHttpUtil;

//login activity
public class BrowserActivity extends Activity {
	String htmlMyAuction;

	private static final String URL_LOGIN = "https://passport.jd.com/uc/login";
	private static final String URL_POST_LOGIN = "https://www.jd.com";
	private static final String URL_POST_LOGIN_MOBILE = "https://m.jd.com";

	public static final String FILE_MY_AUCTION = "/mnt/sdcard/myAuction.html";

	// 登陆
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);

		CookieManager.getInstance().setAcceptCookie(true);

		final WebView wvBrowser = findViewById(R.id.wv_browser);
		//模拟电脑访问
		wvBrowser.getSettings().setUserAgentString(RaiderHttpUtil.USER_AGENT);
		//登陆页面有js操作
		wvBrowser.getSettings().setJavaScriptEnabled(true);

		wvBrowser.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				// 正常跳转
				Log.d("shouldOverrideUrlLoading", url);
				wvBrowser.loadUrl(url);
				// 登陆成功，跳转到 URL_POST_LOGIN
				if (url.equals(URL_POST_LOGIN + "/")
						|| url.equals(URL_POST_LOGIN_MOBILE + "/")
						|| url.contains("http://sale.jd.com/act")) {
					
					final String cookie = CookieManager.getInstance()
							.getCookie(url);
					Log.d("shouldOverrideUrlLoading", cookie);
					
					RaiderHttpAgent rha = RaiderHttpAgent.getInstance();
					rha.setCookie(cookie);

					//干什么用的？
					CookieSyncManager.getInstance().sync();
					Toast.makeText(BrowserActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
					//退出activity
					finish();
				}

				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.d("onPageFinished", url);

			}

		});

		//登陆页面
		wvBrowser.loadUrl(URL_LOGIN);

	}

}
