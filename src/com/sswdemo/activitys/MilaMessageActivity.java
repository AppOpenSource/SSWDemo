package com.sswdemo.activitys;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sswdemo.activitys.R;
import com.sswdemo.adapters.ServeListAdapter;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.ServeListBean;
import com.sswdemo.beans.ServeListDataBean;
import com.sswdemo.helper.QHttpClient;
import com.sswdemo.loadimage.LoadImage;
import com.sswdemo.model.ChatMsgBean;
import com.sswdemo.model.ChatMsgEntity;
import com.sswdemo.model.DBOpenHelper;
import com.sswdemo.views.MyListView;
import com.sswdemo.views.MyListView.OnRefreshListener;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MilaMessageActivity extends ListActivity {

	private List<ChatMsgEntity> mData;
	private List<ChatMsgBean> mListData;
	private DBOpenHelper dbHelper;
	private Context context;

	private Dialog mProgressDialog;

	private MyListView servelist;
	private MilaMessageListAdapter mServeListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mila_message);

		dbHelper = new DBOpenHelper(this);
		mData = getMessageListData();
		setListAdapter(new MilaMessageListAdapter(this));
	}

	private List<ChatMsgEntity> getMessageListData() {
		List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();

		try {
			// String sql =
			// "SELECT id, title, msg, time, type FROM msglist WHERE title = ?";
			// Cursor cursor = db.rawQuery(sql, new String[]{ title });

			SQLiteDatabase db = dbHelper.getWritableDatabase();
			// String sqlnow =
			// "SELECT id, title, msg, time, type FROM(SELECT *, row_number() over(partition by title order by id desc) as RowNo from msglist) as t where RowNo=1";
			Cursor cursor = db.query("msglist", new String[] {}, null, null,
					null, null, null);

			while (cursor.moveToNext()) {
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				entity.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
				entity.setTime(cursor.getString(cursor.getColumnIndex("time")));
				entity.setImg(R.drawable.user_img);
				list.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent(this, MilaMessageDetailActivity.class);
		ChatMsgEntity entity = (ChatMsgEntity) mData.get(position);
		intent.putExtra("title", entity.getTitle());
		startActivity(intent);
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView msg;
		public TextView time;
	}

	public class MilaMessageListAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		ViewHolder holder = null;

		public MilaMessageListAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}
		
		public MilaMessageListAdapter(Activity activity, List<ChatMsgBean> listData) {
			context = activity;
			mListData = listData;
			this.mInflater = LayoutInflater.from(activity);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.mila_message_listview,
						null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.msg = (TextView) convertView.findViewById(R.id.msg);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setBackgroundResource((Integer) mData.get(position)
					.getImg());
			holder.title.setText((String) mData.get(position).getTitle());
			holder.msg.setText((String) mData.get(position).getMsg());
			holder.time.setText((String) mData.get(position).getTime());

			return convertView;
		}
	}

	private class ServeListAsyncTask extends AsyncTask<Void, Void, ChatMsgBean> {
		private String _type;
		private String _order;

		public ServeListAsyncTask(String type, String order) {
			this._type = type;
			this._order = order;
		}

		protected ChatMsgBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			//JSONObject json = new JSONObject();
			ChatMsgBean mChatMsgBean = null;
			List<NameValuePair> parm = new ArrayList<NameValuePair>(); 
			try {
				parm.add(new BasicNameValuePair("upasswd", _type)); 
				parm.add(new BasicNameValuePair("uname", _order)); 
				/*json.put("type", _type);
				json.put("order", _order);
				json.put("location_x", "1.0");
				json.put("location_y", "2.0");*/
				String request = http.httpPost(AppParameters.getInstance().url_getServeList(), parm);
				Gson gson = new Gson();
				mChatMsgBean = gson.fromJson(request, ChatMsgBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mChatMsgBean;
		}

		protected void onPreExecute() {
			mProgressDialog = new Dialog(MilaMessageActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
		}

		protected void onPostExecute(ChatMsgBean mChatMsgBean) {
			mProgressDialog.dismiss();
			if (mChatMsgBean != null) {
				mListData = mChatMsgBean.getData();
				initListView();
			}
		}
	}

	public void initListView() {
		servelist = (MyListView) findViewById(R.id.serve_list);
		mServeListAdapter = new MilaMessageListAdapter(MilaMessageActivity.this, mListData);
		servelist.setAdapter(mServeListAdapter);
		servelist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MilaMessageActivity.this,
						MilaMessageDetailActivity.class);
				startActivity(intent);
			}

		});
		servelist.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				servelist.onRefreshComplete();
			}
		});
	}

}
