package com.abt.ssw.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abt.ssw.activitys.R;
import com.abt.ssw.beans.OrderbacklogDataBean;

public class OrderbacklogAdapter extends BaseAdapter{
	private Context mActivity;
	private List<OrderbacklogDataBean>  mlistData;
	private LayoutInflater mInflater;
	public OrderbacklogAdapter(Activity activity,List<OrderbacklogDataBean>  listData){
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
		OrderbacklogDataBean mOrderbacklogDataBean = mlistData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.orderbackloglist_item,null);
			holder.id_text = (TextView)convertView.findViewById(R.id.order_id);
			holder.time_text = (TextView)convertView.findViewById(R.id.order_time);
			holder.state_text = (TextView)convertView.findViewById(R.id.order_state);
			convertView.setTag(holder);
		}else {
			
			holder = (ViewHolder) convertView.getTag();
		}
		holder.id_text.setText(mOrderbacklogDataBean.getOrder_sn());
		holder.time_text.setText(mOrderbacklogDataBean.getConfirm_time());
		holder.state_text.setText(getStatus(mOrderbacklogDataBean.getOrder_status()));
	
		return convertView;
	}
	public final class ViewHolder {
		public TextView id_text;
		public TextView time_text;
		public TextView state_text;
	}
	public String getStatus(String status){
		if(status.equals("0")){
			return "等待备库";
		}
		if(status.equals("1")){
			return "商品出库";
		}
		if(status.equals("2")){
			return "等待收货";
		}
		return "订单出错";
		
	}

}
