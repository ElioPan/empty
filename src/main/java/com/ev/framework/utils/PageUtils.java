package com.ev.framework.utils;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class  PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	private int total;
	private List<?> rows;

	public PageUtils(List<?> list, int total) {
		this.rows = list;
		this.total = total;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	/**
	 * 开始分页
	 *
	 * @param list 分页前的数据
	 * @param pageNum  页码
	 * @param pageSize 每页多少条数据
	 * @return 分页后的数据
	 */
	public static <T> List<T> startPage(List<T> list, Integer pageNum, Integer pageSize) {
		if (list == null) {
			return null;
		}
		if (list.size() == 0) {
			return null;
		}

		Integer count = list.size(); //记录总数
		Integer pageCount = 0; //页数
		if (count % pageSize == 0) {
			pageCount = count / pageSize;
		} else {
			pageCount = count / pageSize + 1;
		}

		if (pageNum > pageCount) {
			return Lists.newArrayList();
		}

		int fromIndex = 0; //开始索引
		int toIndex = 0; //结束索引

		if (!pageNum.equals(pageCount)) {
			fromIndex = (pageNum - 1) * pageSize;
			toIndex = fromIndex + pageSize;
		} else {
			fromIndex = (pageNum - 1) * pageSize;
			toIndex = count;
		}

		return list.subList(fromIndex, toIndex);
	}

}
