package com.abt.ssw.model;

public class ReplyCommunityItem {

	public String username;
	public String content;
	public String createTime;
	public String sourceType;
	public int replyCount;
	
	/**
	 * 
	 * @param username 用户名
	 * @param content 内容
	 * @param createTime 创建时间
	 * @param sourceType 来源
	 * @param replyCount 回复数量
	 */
	public ReplyCommunityItem(String username, String content, String createTime, String sourceType, int replyCount) {
		this.username = username;
		this.content = content;
		this.createTime = createTime;
		this.sourceType = sourceType;
		this.replyCount = replyCount;
	}
	
	public ReplyCommunityItem() {}
	
}
