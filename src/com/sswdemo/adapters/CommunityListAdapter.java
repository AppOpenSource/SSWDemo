package com.sswdemo.adapters;

import java.util.HashMap;
import java.util.List;
import com.sswdemo.activitys.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 社区ListAdapter
 * 
 */
public class CommunityListAdapter extends BaseAdapter{
	
	private List<HashMap<String,Object>> mlistData;
	private LayoutInflater mInflater;
	
	public CommunityListAdapter(Activity activity,List<HashMap<String,Object>> listData){
		   this.mlistData = listData;
		   this.mInflater = LayoutInflater.from(activity);
	}
	public int getCount() {
		return mlistData == null ? 0 : mlistData.size();
	}

	@Override
	public HashMap<String,Object> getItem(int position) {
		return mlistData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		HashMap<String,Object> map = mlistData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.community_list_item,null);
			holder.user_img = (ImageView)convertView.findViewById(R.id.user_img);
			holder.name_text = (TextView)convertView.findViewById(R.id.name_text);
			holder.time_text = (TextView)convertView.findViewById(R.id.time_text);
			holder.info_text = (TextView)convertView.findViewById(R.id.info_text);
			holder.comment_text = (TextView)convertView.findViewById(R.id.comment_text);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.user_img.setBackgroundResource((Integer) map.get("imgId"));
		holder.name_text.setText((String)map.get("name"));
		holder.time_text.setText((String)map.get("time"));
		holder.info_text.setText((String)map.get("info"));
		holder.comment_text.setText((String)map.get("comment"));
	
		return convertView;
	}
	
	private final class ViewHolder {
		public ImageView user_img;
		public TextView name_text;
		public TextView time_text;
		public TextView info_text;
		public TextView comment_text;
	}

}
