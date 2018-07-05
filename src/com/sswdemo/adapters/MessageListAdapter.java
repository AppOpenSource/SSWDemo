package com.sswdemo.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sswdemo.activitys.R;
import com.sswdemo.beans.MessageDataInfoBean;

@SuppressLint("SimpleDateFormat")
public class MessageListAdapter extends BaseAdapter{
	private Context mActivity;
	private List<MessageDataInfoBean> mlistData;
	private LayoutInflater mInflater;
	public MessageListAdapter(Activity activity,List<MessageDataInfoBean> listData){
		   this.mActivity = activity;
		   this.mlistData = listData;
		   this.mInflater = LayoutInflater.from(activity);
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return mlistData == null ? 0 : mlistData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlistData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MessageDataInfoBean mMessageDataInfoBean = mlistData.get(position);
		if (convertView == null) {
			
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.messagelist_item,null);
			holder.name_text = (TextView)convertView.findViewById(R.id.name_text);
			holder.content_text = (TextView)convertView.findViewById(R.id.content_text);
			holder.time_text = (TextView)convertView.findViewById(R.id.time_text);
			convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name_text.setText(getTypeString(mMessageDataInfoBean.getType()));
		holder.content_text.setText(mMessageDataInfoBean.getContent().get(0).getContent());
		holder.time_text.setText(Times(Long.parseLong(mMessageDataInfoBean.getAddtime())));
		//holder.time_text.setText(mMessageDataInfoBean.getAddtime());
	
		return convertView;
	}
	public final class ViewHolder {
		public TextView name_text;
		public TextView content_text;
		public TextView time_text;
	}
	public String getTypeString(String type){
		if(type.equals("1"))
			return "订单消息";
		if(type.equals("2"))
			return "商户消息";
		if(type.equals("3"))
			return "系统消息";
		return "未知消息";
	}
	public String Times(long longtime){
		if(longtime != 0){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	        return sdf.format(longtime * 1000);
		}else{
			return "";
		}
		
	}

}
