package com.abt.ssw.adapters;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.abt.ssw.activitys.R;
import com.abt.ssw.beans.OrderDetailsListsBean;
import com.abt.ssw.loadimage.LoadImage;

public class OrderDetailsListsAdapter extends BaseAdapter{
	private Context mActivity;
	private List<OrderDetailsListsBean>  mlistData;
	private LayoutInflater mInflater;
	public LoadImage loader;
	public OrderDetailsListsAdapter(Activity activity,List<OrderDetailsListsBean> listData){
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
		OrderDetailsListsBean orderDetailsListsBean  = mlistData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.orderdetailslists_item,null);
			holder.goods_img = (ImageView)convertView.findViewById(R.id.goods_img);
			holder.goods_name = (TextView)convertView.findViewById(R.id.goods_name);
			holder.goods_des = (TextView)convertView.findViewById(R.id.goods_des);
			holder.goods_price = (TextView)convertView.findViewById(R.id.goods_price);
			holder.goods_amount = (TextView)convertView.findViewById(R.id.goods_amount);
			convertView.setTag(holder);
		}else {
			
			holder = (ViewHolder) convertView.getTag();
		}
		if (orderDetailsListsBean.getGoodsimage() != null) {
			holder.goods_img.setTag(orderDetailsListsBean.getGoodsimage());
			loader.addTask(orderDetailsListsBean.getGoodsimage(),holder.goods_img);
			loader.doTask();
		}
		holder.goods_name.setText(orderDetailsListsBean.getGoods_name());
		holder.goods_des.setText(orderDetailsListsBean.getGoods_number());
		holder.goods_price.setText(orderDetailsListsBean.getGoods_price());
		holder.goods_amount.setText(orderDetailsListsBean.getGoods_number());
	
		return convertView;
	}
	public final class ViewHolder {
		public ImageView goods_img;
		public TextView goods_name;
		public TextView goods_des;
		public TextView goods_price;
		public TextView goods_amount;
	}
	

}
