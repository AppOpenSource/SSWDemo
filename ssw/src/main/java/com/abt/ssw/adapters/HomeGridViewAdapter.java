package com.abt.ssw.adapters;

import java.util.List;
import java.util.Map;
import com.abt.ssw.activitys.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeGridViewAdapter extends BaseAdapter{
	private Context mActivity;
	private List<Map<String,Object>> mlistData;
	private LayoutInflater mInflater;
	public HomeGridViewAdapter(Activity activity,List<Map<String,Object>> listData){
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Map<String,Object> map = mlistData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.gridview_item,null);
			holder.name_text = (TextView)convertView.findViewById(R.id.text);
			holder.img = (ImageView)convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		}else {
			
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.img.setBackgroundResource((Integer) map.get("imgId"));
		holder.name_text.setText((String)map.get("name"));
			
	
		return convertView;
	}
	public final class ViewHolder {
		public ImageView img;
		public TextView name_text;
	}

}
