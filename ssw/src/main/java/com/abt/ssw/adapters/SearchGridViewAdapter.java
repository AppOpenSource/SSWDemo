package com.abt.ssw.adapters;

import java.util.HashMap;
import java.util.List;
import com.abt.ssw.activitys.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 搜索GridView Adapter
 * 
 */
public class SearchGridViewAdapter extends BaseAdapter{
	
	private List<HashMap<String,Object>> mlistData;
	private LayoutInflater mInflater;
	
	public SearchGridViewAdapter(Activity activity,List<HashMap<String,Object>> listData){
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
			convertView = mInflater.inflate(R.layout.search_gridview_item,null);
			holder.name_text = (TextView)convertView.findViewById(R.id.search_name);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name_text.setText((String)map.get("name"));
	
		return convertView;
	}
	
	private final class ViewHolder {
		public TextView name_text;
	}

}
