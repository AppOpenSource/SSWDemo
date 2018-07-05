package com.sswdemo.adapters;

import java.util.List;
import com.sswdemo.activitys.R;
import com.sswdemo.model.MoreListItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodDetailsListAdapter extends BaseAdapter {
	
	private List<MoreListItem> mData;
	private LayoutInflater mInflater;
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	
	public GoodDetailsListAdapter(Context context,List<MoreListItem> data){
		   this.mData = data;
		   this.mInflater = LayoutInflater.from(context);
	}
	
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		if (position == (getCount() - 2))
			return TYPE_1;
		else
			return TYPE_2;
	}
	
	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE;
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
		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);

		if (convertView == null) {
			switch (type) {
			case TYPE_1:
				convertView = mInflater.inflate(R.layout.good_detail_list_item,
						null);
				holder1 = new ViewHolder1();
				holder1.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder1.info = (TextView) convertView
						.findViewById(R.id.good_title);
				holder1.submenu = (ImageView) convertView
						.findViewById(R.id.submenu);
				holder1.location = (TextView) convertView
						.findViewById(R.id.good_location);
				holder1.distance = (TextView) convertView
						.findViewById(R.id.good_distance);
				convertView.setTag(holder1);
				break;
			case TYPE_2:
				convertView = mInflater.inflate(R.layout.good_grade_list_item,
						null);
				holder2 = new ViewHolder2();
				holder2.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder2.info = (TextView) convertView.findViewById(R.id.tvInfo);
				holder2.submenu = (ImageView) convertView
						.findViewById(R.id.submenu);
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

		MoreListItem item = mData.get(position);
		convertView.setBackgroundResource(R.drawable.list_mid_background);

		switch (type) {
		case TYPE_1:
			holder1.icon.setImageResource(item.getLogo());
			holder1.info.setText("东来顺饭庄（马甸店）");
			holder1.submenu.setImageResource(item.getSubmenu());
			holder1.location.setText("朝阳区北三环裕民中路8号");
			holder1.distance.setText("3.6km");
			break;
		case TYPE_2:
			holder2.icon.setImageResource(item.getLogo());
			holder2.info.setText(item.getTitle());
			holder2.submenu.setImageResource(item.getSubmenu());
			break;
		}
		return convertView;
	}
	
	class ViewHolder1 {
		ImageView icon;
		ImageView submenu;
		TextView info;
		TextView location;
		TextView distance;
	}

	class ViewHolder2 {
		ImageView icon;
		ImageView submenu;
		TextView info;
	}

}
