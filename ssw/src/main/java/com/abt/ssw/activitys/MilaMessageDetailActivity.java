package com.abt.ssw.activitys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.abt.ssw.model.ChatMsgEntity;
import com.abt.ssw.model.DBOpenHelper;
import com.abt.ssw.model.DBService;
import com.abt.ssw.activitys.R;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MilaMessageDetailActivity extends Activity {

	private List<ChatMsgEntity> mData;
	private DBOpenHelper dbHelper;
	
	private ListView listview;
	private EditText editText;
	private Button button;
	private ImageButton imgButton;
	
	private MilaMessageDetailListAdapter mAdapter;
	private DBService dbService;
	private String conversationTitle;
	
	private static final int COME_FROM_OUTSIDE = 1;
	private static final int COME_FROM_LOCAL = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mila_message_detail);

		dbService = new DBService(this);
		button = (Button) findViewById(R.id.send_btn);
		listview = (ListView) findViewById(R.id.list_view);
		imgButton = (ImageButton) findViewById(R.id.back_btn);
		imgButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		editText = (EditText) findViewById(R.id.body_edit);
		editText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendReply();
			}
		});

		dbHelper = new DBOpenHelper(this);
		conversationTitle = getIntent().getStringExtra("title");
		mData = getDetailListData(conversationTitle);
		mAdapter = new MilaMessageDetailListAdapter(this);
		listview.setAdapter(mAdapter);
	}
	
	private void sendReply() {
		String replyMsg = editText.getText().toString();
		if (replyMsg.length() > 0) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setTime(getDate());
			entity.setTitle(mData.get(0).getTitle());
			entity.setType(0);
			entity.setMsg(replyMsg);
			dbService.insertEntity(entity);

			mData = getDetailListData(conversationTitle);
			mAdapter.notifyDataSetChanged();
			editText.setText("");
			listview.setSelection(listview.getCount() - 1);
		}
	}
	
    private String getDate() {
        Calendar calendar = Calendar.getInstance();

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(calendar.get(Calendar.MINUTE));
       
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins); 
        return sbBuffer.toString();
    }

	private List<ChatMsgEntity> getDetailListData(String title) {
		List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
		try {
			SQLiteDatabase db =	dbHelper.getWritableDatabase();
			String sql = "SELECT id, title, msg, time, type FROM msglist WHERE title = ?";
			Cursor cursor = db.rawQuery(sql, new String[]{ title });
			while (cursor.moveToNext()) {
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				entity.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
				entity.setTime(cursor.getString(cursor.getColumnIndex("time")));
				entity.setType(cursor.getInt(cursor.getColumnIndex("type")));
				list.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public final class ViewHolder1 {
		public ImageView img;
		public TextView title;
		public TextView msg;
		public TextView time;
	}

	public final class ViewHolder2 {
		public ImageView img;
		public TextView title;
		public TextView msg;
		public TextView time;
	}

	public class MilaMessageDetailListAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		final int VIEW_TYPE = 2;
		final int TYPE_1 = 0;
		final int TYPE_2 = 1;

		public MilaMessageDetailListAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mData == null ? 0 : mData.size();
		}
		
		@Override
		public int getItemViewType(int position) {
			ChatMsgEntity entity = (ChatMsgEntity) mData.get(position);
			int isComMsg = entity.getType();
			if (isComMsg == COME_FROM_OUTSIDE) {
				return TYPE_1;
			} else if (isComMsg == COME_FROM_LOCAL) {
				return TYPE_2;
			}
			return -1;
		}
		
		@Override
		public int getViewTypeCount() {
			return VIEW_TYPE;
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder1 holder1 = null;
			ViewHolder2 holder2 = null;
			int type = getItemViewType(position);
			

			if (convertView == null) {
				switch (type) {
				case TYPE_1:
					convertView = mInflater.inflate(R.layout.mila_message_detail_listview,
							null);
					holder1 = new ViewHolder1();
					holder1.img = (ImageView) convertView.findViewById(R.id.img);
					holder1.title = (TextView) convertView
							.findViewById(R.id.title);
					holder1.msg = (TextView) convertView
							.findViewById(R.id.msg);
					holder1.time = (TextView) convertView
							.findViewById(R.id.time);
					convertView.setTag(holder1);
					break;
				case TYPE_2:
					convertView = mInflater.inflate(R.layout.mila_message_reply_listview,
							null);
					holder2 = new ViewHolder2();
					holder2.img = (ImageView) convertView.findViewById(R.id.img);
					holder2.title = (TextView) convertView
							.findViewById(R.id.title);
					holder2.msg = (TextView) convertView
							.findViewById(R.id.msg);
					holder2.time = (TextView) convertView
							.findViewById(R.id.time);
					convertView.setTag(holder2);
					break;
				}
			} else {
				switch (type) {
				case TYPE_1:
					holder1 = (ViewHolder1) convertView.getTag();
					break;
				case TYPE_2:
					holder2 = (ViewHolder2) convertView.getTag();
					break;
				}
			}
			
			ChatMsgEntity entity = (ChatMsgEntity) mData.get(position);
			switch (type) {
			case TYPE_1:
				holder1.img.setBackgroundResource((Integer) R.drawable.user_img);
				holder1.title.setText((String) entity.getTitle());
				holder1.msg.setText((String) entity.getMsg());
				holder1.time.setText((String) entity.getTime());
				break;
			case TYPE_2:
				holder2.img.setBackgroundResource((Integer) R.drawable.mini_avatar_shadow);
				holder2.title.setText((String) entity.getTitle());
				holder2.msg.setText((String) entity.getMsg());
				holder2.time.setText((String) entity.getTime());
				break;
			}
			return convertView;
		}
	}
}
