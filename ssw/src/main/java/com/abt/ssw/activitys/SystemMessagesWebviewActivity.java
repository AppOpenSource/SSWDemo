package com.abt.ssw.activitys;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

/****************************************************************
 * 系统消息webview Activity
 * @author xuena.ni
 */
public class SystemMessagesWebviewActivity extends Activity  implements OnClickListener{
	private WebView wv;
	private Handler handler;
	private Dialog mProgressDialog;
	private ImageButton backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_message_webview_layout);
		init();//执行初始化函数
		 Intent inte = getIntent();     
	     String noticeid1 = inte.getStringExtra("noticeid");   
		loadurl(wv,"http://121.199.13.117/beta/index.php/PhoneMessage/showmsg/notice_id/"+noticeid1);
	}
	
	public void init(){
		backBtn = (ImageButton)findViewById(R.id.back_btn);
		backBtn.setOnClickListener(this);
		mProgressDialog = new Dialog(SystemMessagesWebviewActivity.this,R.style.theme_dialog_alert);
		mProgressDialog.setContentView(R.layout.window_layout);
		handler=new Handler(){
			public void handleMessage(Message msg)
			{//定义一个Handler，用于处理下载线程与UI间通讯
				if (!Thread.currentThread().isInterrupted())
				{
					switch (msg.what)
					{
					case 0:
						mProgressDialog.show();
						break;
					case 1:
						mProgressDialog.dismiss();
						break;
					}
				}
				super.handleMessage(msg);
			}
		};
		
		// WebView
		wv=(WebView)findViewById(R.id.wv);
		wv.getSettings().setJavaScriptEnabled(true);//可用JS
		wv.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		wv.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
				loadurl(view,url);//载入网页
				return true;
			}//重写点击动作,用webview载入

		});
		wv.setWebChromeClient(new WebChromeClient(){
			public void onProgressChanged(WebView view,int progress){//载入进度改变而触发
				if(progress==100){
					handler.sendEmptyMessage(1);//如果全部载入,隐藏进度对话框
				}
				super.onProgressChanged(view, progress);
			}
		});
	}

	public void loadurl(final WebView view,final String url){
		new Thread(){
			public void run(){
				handler.sendEmptyMessage(0);
				view.loadUrl(url);//载入网页
			}
		}.start();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;

		default:
			break;
		}
		
	}
}
