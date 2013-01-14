package eFrame.db;

import java.util.List;

public class Page<T> {
	
	private int pageSize = 10;
	/** total results */
	private long total = 0;
	/** which page */
	private int pageNo = 1;
	/** results */
	private List<T> list;
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public Page(int pageSize, long total, int pageNo, List<T> list) {
		super();
		this.pageSize = pageSize;
		this.total = total;
		this.pageNo = pageNo;
		this.list = list;
	}
}
