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

public class SetMailListAdapter_old extends BaseAdapter {
	
	private List<MoreListItem> mData;
	private LayoutInflater mInflater;
	
	public SetMailListAdapter_old(Context context,List<MoreListItem> data){
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
			if (position == (getCount() -2)) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.good_grade_big_list_item,null);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
				viewHolder.info = (TextView) convertView.findViewById(R.id.tvInfo);
				viewHolder.submenu = (ImageView) convertView.findViewById(R.id.submenu);
			} else {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.good_grade_list_item,null);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
				viewHolder.info = (TextView) convertView.findViewById(R.id.tvInfo);
				viewHolder.submenu = (ImageView) convertView.findViewById(R.id.submenu);
			}
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		MoreListItem item = mData.get(position);
		
		convertView.setBackgroundResource(R.drawable.list_mid_background);

		if (item.getLogo() == -1) {
			viewHolder.icon.setVisibility(View.GONE);
		} else {
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(item.getLogo());
		}
		if (position == (getCount() -2)) {
			viewHolder.icon.setImageResource(item.getLogo());
			viewHolder.info.setText("鲜菌清汤炭火锅底	1份	128元\n贡品羔羊肉	2份	约200g/份	96元\n" +
					"特级精品肥牛	2份	约200g/份	96元\n高级花枝丸	1份	约200g	22元\n" +
					"蔬菜大拼	1份	48元\n糕点组合(双拼)	1份	25元\n" +
					"传统杂面	1份	8元\n传统秘制麻酱	5份,不可续	30元\n" +
					"配料	1份	6元\n桂花酸梅汤	1扎	约1000ml,不可续	25元\n");
			viewHolder.submenu.setImageResource(item.getSubmenu());
		} else {
			viewHolder.icon.setImageResource(item.getLogo());
			viewHolder.info.setText(item.getTitle());
			viewHolder.submenu.setImageResource(item.getSubmenu());
		}
		return convertView;
	}
	public static class ViewHolder {
		ImageView icon;
		ImageView submenu;
		TextView info;
	}

}
