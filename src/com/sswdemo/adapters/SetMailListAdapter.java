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

public class SetMailListAdapter extends BaseAdapter {
	
	private List<MoreListItem> mData;
	private LayoutInflater mInflater;
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	
	public SetMailListAdapter(Context context,List<MoreListItem> data){
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
				convertView = mInflater.inflate(R.layout.good_grade_big_list_item, null);
				holder1 = new ViewHolder1();
				holder1.info = (TextView) convertView.findViewById(R.id.tvInfo);
				convertView.setTag(holder1);
				break;
			case TYPE_2:
				convertView = mInflater.inflate(R.layout.good_grade_list_item, null);
				holder2 = new ViewHolder2();
				holder2.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder2.info = (TextView) convertView.findViewById(R.id.tvInfo);
				holder2.submenu = (ImageView) convertView.findViewById(R.id.submenu);
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
			holder1.info.setText("鲜菌清汤炭火锅底	1份	128元\n贡品羔羊肉	2份	约200g/份	96元\n" +
					"特级精品肥牛	2份	约200g/份	96元\n高级花枝丸	1份	约200g	22元\n" +
					"蔬菜大拼	1份	48元\n糕点组合(双拼)	1份	25元\n" +
					"传统杂面	1份	8元\n传统秘制麻酱	5份,不可续	30元\n" +
					"配料	1份	6元\n桂花酸梅汤	1扎	约1000ml,不可续	25元\n");
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
		TextView info;
	}

	class ViewHolder2 {
		ImageView icon;
		ImageView submenu;
		TextView info;
	}

}
