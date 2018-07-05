package com.sswdemo.adapters;

import java.util.List;
import com.sswdemo.activitys.R;
import com.sswdemo.model.ReplyCommunityItem;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReplyCommunityAdapter extends BaseAdapter implements OnClickListener {

	private List<ReplyCommunityItem> mList;
	private ReplyCommunityAdapterCallbackListener mCallback;
	private LayoutInflater mInflater;

	public ReplyCommunityAdapter(Context context, List<ReplyCommunityItem> list, ReplyCommunityAdapterCallbackListener callback) {
		mInflater = LayoutInflater.from(context);
		mList = list;
		mCallback = callback;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ReplyCommunityItem getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.reply_community_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.feedLayout = (LinearLayout)convertView.findViewById(R.id.feed_layout);
			viewHolder.feedContent = (TextView)convertView.findViewById(R.id.feed_content);
			viewHolder.feedAttachmentList = (LinearLayout)convertView.findViewById(R.id.feed_attachment_list);
			viewHolder.feedSourceType = (TextView)convertView.findViewById(R.id.feed_source_type);
			viewHolder.feedTime = (TextView)convertView.findViewById(R.id.feed_time);
			viewHolder.feedReplyCount = (TextView)convertView.findViewById(R.id.feed_reply_count);
			viewHolder.feedFavorite = (ImageView)convertView.findViewById(R.id.feed_favorite);
			
			//回复内容属性
			viewHolder.repliesLayout = (LinearLayout)convertView.findViewById(R.id.replie_layout);
			viewHolder.repliesAvatar = (ImageView)convertView.findViewById(R.id.replies_avatar);
			viewHolder.repliesSponsor = (TextView)convertView.findViewById(R.id.replies_sponsor);
			viewHolder.repliesContent = (TextView)convertView.findViewById(R.id.replies_content);
			viewHolder.repliesTime = (TextView)convertView.findViewById(R.id.replies_time);
			viewHolder.repliesAttachment = (ImageView)convertView.findViewById(R.id.replies_attachment);
			viewHolder.repliesAttachmentNumber = (TextView)convertView.findViewById(R.id.replies_attachment_number);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.feedFavorite.setOnClickListener(this);
		ReplyCommunityItem item = getItem(position);
		//清除附件列表
		viewHolder.feedAttachmentList.removeAllViews();
		switch (position) {
		case 0:
			viewHolder.feedLayout.setVisibility(View.VISIBLE);
			viewHolder.repliesLayout.setVisibility(View.GONE);
			this.setFeedContetn(viewHolder, item, position);
			break;
		default:
			viewHolder.feedLayout.setVisibility(View.GONE);
			viewHolder.repliesLayout.setVisibility(View.VISIBLE);
			this.setReplies(viewHolder, item, position);
			break;
		}
		return convertView;
	}

	/**设置内容*/
	private void setFeedContetn(ViewHolder viewHolder, ReplyCommunityItem item, int position) {
		SpannableStringBuilder content=new SpannableStringBuilder();
//		if (title!= null && title.length() > 0 && !title.equals("null")) {
//			content.append("#").append(title).append("# ");
//			content.setSpan(new StyleSpan(Typeface.BOLD), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
		content.append(item.content);
		//设置内容
		if (content.length() > 0) {
			viewHolder.feedContent.setVisibility(View.VISIBLE);
			viewHolder.feedContent.setText(content);
		}else {
			viewHolder.feedContent.setVisibility(View.GONE);
		}
		
		viewHolder.feedSourceType.setText("来源：" + item.sourceType);
		
		viewHolder.feedTime.setText(item.createTime);
		viewHolder.feedReplyCount.setText(""+item.replyCount);
	}

	/**设置回复*/
	private void setReplies(ViewHolder viewHolder, ReplyCommunityItem item, int position) {
		viewHolder.repliesAvatar.setImageResource(R.drawable.user_img);
		viewHolder.repliesSponsor.setText(item.username);
		
		viewHolder.repliesContent.setText(item.content);
		viewHolder.repliesTime.setText(item.createTime);
		viewHolder.repliesAttachmentNumber.setVisibility(View.GONE);
		viewHolder.repliesAttachment.setVisibility(View.GONE);
	}
	
	private static class ViewHolder {
		//动态内容属性
		LinearLayout feedLayout;
		TextView feedContent;
		LinearLayout feedAttachmentList;
		TextView feedSourceType;
		TextView feedTime;
		TextView feedReplyCount;
		ImageView feedFavorite;
		
		//回复内容属性
		LinearLayout repliesLayout;
		ImageView repliesAvatar;//头像
		TextView repliesSponsor;//姓名
		TextView repliesContent;
		TextView repliesTime;
		ImageView repliesAttachment;//单个附件显示
		TextView repliesAttachmentNumber;//附件数量
	}
	
	public interface ReplyCommunityAdapterCallbackListener {
		
		/**获得滚动状态*/
		public int getScrollState();
		
		/**动态收藏*/
		public void onFeedFavorite(View view, boolean isFavortite);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.feed_favorite:
//			Boolean boo = (Boolean)v.getTag();
//			boo = boo == null ? false : boo;
//			mCallback.onFeedFavorite(v, !boo);
//			break;
		}
		
	}
}
