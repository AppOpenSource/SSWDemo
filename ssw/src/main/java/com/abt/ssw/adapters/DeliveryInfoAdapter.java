package com.abt.ssw.adapters;

import java.util.List;

import com.abt.ssw.activitys.R;

import com.abt.ssw.beans.DeliveryInfoDataBean;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeliveryInfoAdapter extends BaseAdapter{
	private Context mActivity;
	private List<DeliveryInfoDataBean> mlistData;
	private LayoutInflater mInflater;
	//public LoadImage loader;
	public DeliveryInfoAdapter(Activity activity, List<DeliveryInfoDataBean> listData){
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
		DeliveryInfoDataBean mDeliveryInfoDataBean = mlistData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.delivery_item,null);
			holder.img = (ImageView)convertView.findViewById(R.id.delivery_img);
			holder.name_text = (TextView)convertView.findViewById(R.id.context_text);
			holder.address_text = (TextView)convertView.findViewById(R.id.time_text);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setBackgroundResource(getResouse(mDeliveryInfoDataBean.getStatus()));
		holder.name_text.setText(mDeliveryInfoDataBean.getAction_info());
		holder.address_text.setText(mDeliveryInfoDataBean.getLogtime());
		return convertView;
	}
	private int getResouse(String status){
		if(status.equals("3"))
			return R.drawable.stop_img;
		return R.drawable.run_img;
	}
	public final class ViewHolder {
		public ImageView img;
		public TextView name_text;
		public TextView address_text;
	}
}
