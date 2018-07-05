package com.abt.ssw.adapters;

import java.util.List;

import com.abt.ssw.activitys.R;
import com.abt.ssw.activitys.ShoppingCartActivity;

import com.abt.ssw.beans.GoodListsDataBean;
import com.abt.ssw.loadimage.LoadImage;

import com.abt.ssw.servcie.ShoppingCartDBService;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsListAdapter extends BaseAdapter{
	private Context mActivity;
	private List<GoodListsDataBean> mlistData;
	private LayoutInflater mInflater;
	public LoadImage loader;
	public GoodsListAdapter(Activity activity, List<GoodListsDataBean> listData){
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
		GoodListsDataBean mGoodListsDataBean = mlistData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.goodslist_item,null);
			holder.img = (ImageView)convertView.findViewById(R.id.serve_img);
			holder.name_text = (TextView)convertView.findViewById(R.id.name_text);
			holder.address_text = (TextView)convertView.findViewById(R.id.address_text);
			holder.telephone_text = (TextView)convertView.findViewById(R.id.telephone_text);
			holder.gocat_btn = (Button)convertView.findViewById(R.id.mygocat_btn);
			convertView.setTag(holder);
		}else {
			
			holder = (ViewHolder) convertView.getTag();
		}
		if (mGoodListsDataBean.getGoodsimage() != null) {
			holder.img.setTag(mGoodListsDataBean.getGoodsimage() );
			loader.addTask(mGoodListsDataBean.getGoodsimage(),holder.img);
			loader.doTask();
		}
		holder.name_text.setText(mGoodListsDataBean.getGoodsname());
		holder.address_text.setText(mGoodListsDataBean.getGoodsdesc());
		holder.telephone_text.setText(mGoodListsDataBean.getGoodsprice());
		holder.gocat_btn.setTag(position);
		holder.gocat_btn.setOnClickListener(gocatClickListener);
		return convertView;
	}
	public final class ViewHolder {
		public ImageView img;
		public TextView name_text;
		public TextView address_text;
		public TextView telephone_text;
		public Button gocat_btn;
	}
	
	OnClickListener gocatClickListener = new OnClickListener() {
		public void onClick(View v) {
			int index = (Integer)v.getTag();
			ShoppingCartDBService sdbs = new ShoppingCartDBService(mActivity);
			try {
				sdbs.insert(Integer.parseInt(mlistData.get(index).getShopid()), 
						mlistData.get(index).getShopname(), 
						Integer.parseInt(mlistData.get(index).getGoods_id()), 
						mlistData.get(index).getGoodsimage(),
						mlistData.get(index).getGoodsname(),
						Float.valueOf(mlistData.get(index).getGoodsprice()),
						mlistData.get(index).getGoodsdesc(), 
						1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           //对话框
			dialog();
			
		}
	};
	
	protected void dialog() {
		  AlertDialog.Builder builder = new Builder(mActivity);
		  builder.setMessage("商品已成功加入购物车");

		  builder.setTitle("添加成功！");

		  builder.setPositiveButton("去购物车", new AlertDialog.OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();

		    Intent intent = new Intent(mActivity,ShoppingCartActivity.class);
			mActivity.startActivity(intent);
		   }
		  });

		  builder.setNegativeButton("再逛逛", new AlertDialog.OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
		 }

}
