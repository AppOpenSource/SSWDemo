package com.abt.ssw.adapters;

import java.util.List;

import com.abt.ssw.activitys.R;
import com.abt.ssw.beans.ServeListDataBean;
import com.abt.ssw.loadimage.LoadImage;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ServeListAdapter extends BaseAdapter{
	private Context mActivity;
	private List<ServeListDataBean> mlistData;
	private LayoutInflater mInflater;
	public LoadImage loader;
	public ServeListAdapter(Activity activity,List<ServeListDataBean> listData){
		this.mActivity = activity;
		this.mlistData = listData;
		this.mInflater = LayoutInflater.from(activity);
		loader = LoadImage.getInstance();
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
		ServeListDataBean mServeListDataBean = mlistData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.servelist_item,null);
			holder.img = (ImageView)convertView.findViewById(R.id.serve_img);
			holder.name_text = (TextView)convertView.findViewById(R.id.name_text);
			holder.address_text = (TextView)convertView.findViewById(R.id.address_text);
			holder.telephone_text = (TextView)convertView.findViewById(R.id.telephone_text);
			convertView.setTag(holder);
		}else {

			holder = (ViewHolder) convertView.getTag();
		}
		if (mServeListDataBean.getShopimage() != null) {
			holder.img.setTag(mServeListDataBean.getShopimage() );
			loader.addTask(mServeListDataBean.getShopimage(),holder.img);
			loader.doTask();
		}

		holder.name_text.setText(mServeListDataBean.getShopname());
		holder.address_text.setText(mServeListDataBean.getShopdesc());
		holder.telephone_text.setText(mServeListDataBean.getTelphone());

		return convertView;
	}
	public final class ViewHolder {
		public ImageView img;
		public TextView name_text;
		public TextView address_text;
		public TextView telephone_text;
	}

}
