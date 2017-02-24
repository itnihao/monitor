package com.dataeye.omp.common;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 
 * <pre>
 * 分页数据
 * 
 * @author Ivan <br>
 * @since  2015年8月14日 下午3:30:01 <br>
 * @version 1.0
 * <br>
 */
public class PageData<T> {
	/** 总共有多少条记录 */
	@Expose
	private int totalRecord;
	/** 每页显示多少记录 */
	@Expose
	private int pageSize;

	/** 总动可以分多少页 */
	private int totalPage;
	/** 当前返回的是第几页 */
	@Expose
	private int pageID;

	/** 结果集排序规则 */
	private String orderBy;

	private int order;

	/**
	 * 返回数据
	 */
	@Expose
	private List<T> content = new ArrayList<T>();

	public PageData() {
	}

	public PageData(int pageSize, int pageID) {
		setPageSize(pageSize);
		setPageID(pageID);
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		setTotalPage();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setTotalPage() {
		if (totalRecord > 0 && pageSize > 0) {
			this.totalPage = (totalRecord + pageSize - 1) / pageSize;
		} else if (pageSize == -1) {
			this.totalPage = 1;
		}

	}

	public int getPageID() {
		return pageID;
	}

	public void setPageID(int pageID) {
		this.pageID = (pageID <= 0) ? 1 : pageID;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return the content
	 */
	public List<T> getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(List<T> content) {
		this.content = content;
	}

}
