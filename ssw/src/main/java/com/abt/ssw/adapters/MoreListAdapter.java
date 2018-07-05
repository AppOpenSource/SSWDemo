package com.abt.ssw.adapters;

import java.util.List;
import com.abt.ssw.activitys.R;
import com.abt.ssw.model.MoreListItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreListAdapter extends BaseAdapter {
	
	private List<MoreListItem> mData;
	private LayoutInflater mInflater;
	
	public MoreListAdapter(Context context,List<MoreListItem> data){
		   this.mData = data;
		   this.mInflater = LayoutInflater.from(context);
	}
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public MoreListItem getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.more_list_item,null);
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
			viewHolder.info = (TextView) convertView.findViewById(R.id.tvInfo);
			viewHolder.submenu = (ImageView) convertView.findViewById(R.id.submenu);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		MoreListItem item = mData.get(position);
		
		if (position == 0) {
			convertView.setBackgroundResource(R.drawable.list_above_background);
		} else if (position == (getCount()-1)) {
			convertView.setBackgroundResource(R.drawable.list_below_background);
		} else {
			convertView.setBackgroundResource(R.drawable.list_mid_background);
		}
		if (item.getLogo() == -1) {
			viewHolder.icon.setVisibility(View.GONE);
		} else {
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(item.getLogo());
		}
		viewHolder.icon.setImageResource(item.getLogo());
		viewHolder.info.setText(item.getTitle());
		viewHolder.submenu.setImageResource(item.getSubmenu());
		return convertView;
	}
	public static class ViewHolder {
		ImageView icon;
		ImageView submenu;
		TextView info;
	}

}
