package com.abt.ssw.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abt.ssw.activitys.R;
import com.abt.ssw.beans.RecommendedInfoDataBean;

//适配器
 public class PopNoImgAdapter extends BaseAdapter {
	 private Context mcontext;
	 private List<RecommendedInfoDataBean> mlistData;
	 private LayoutInflater mInflater;
	 public PopNoImgAdapter(Context context,List<RecommendedInfoDataBean> listData){
		   this.mcontext = context;
		   this.mlistData = listData;
		   this.mInflater = LayoutInflater.from(context);
	}
		public int getCount() {
			// TODO Auto-generated method stub
			return mlistData.size();
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
			RecommendedInfoDataBean  mRecommendedInfoDataBean= mlistData.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.pomenu_item,null);
				holder.img = (ImageView)convertView.findViewById(R.id.pop_img);
				holder.name_text = (TextView)convertView.findViewById(R.id.textView);
				convertView.setTag(holder);
			}else {
				
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.name_text.setText(mRecommendedInfoDataBean.getTypename());
		
			return convertView;
		}
		public final class ViewHolder {
			public ImageView img;
			public TextView name_text;
		}
		
	}
