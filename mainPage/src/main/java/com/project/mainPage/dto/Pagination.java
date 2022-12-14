package com.project.mainPage.dto;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
@Data
public class Pagination {
	private int page; 	// 요청한 페이지 (현재 위치)
	private int count;	// 총 개수 
	private String url;
	private int row = 10; 
	
	private int start;
	private int end; 
	private int firstPage = 1;
	private int lastPage;
	private int previousPage;
	private int nextPage;
	
	private boolean yourFirst;
	private boolean yourLast;
	private boolean yourPreviousPage;
	private boolean yourNext;
	
	public Pagination(int page, int count, String url) {
		this.page = page;
		this.count = count;
		this.url = url;
		this.setAll();
	}
	
	public Pagination(int page, int count, String url, int row) {
		this.page = page;
		this.count = count;
		this.url = url;
		this.row = row;
		this.setAll();
	}

	public void setAll() {
		if(count > 0) {
			this.previousPage = page - 1;
			this.nextPage = page + 1;
			this.lastPage = count/row + ((count % row > 0) ? 1 : 0);
			
			this.yourFirst = (page > firstPage) ? true : false;
			this.yourPreviousPage = (page > firstPage) ? true : false; 	
			this.yourLast = (page < lastPage) ? true : false;
			this.yourNext = (page < lastPage) ? true : false;
			
			this.start = page - 2;
			this.end = page + 2;
			
			if(start < firstPage) {
				end = end - start + 1;
				if(end > lastPage) {
					end = lastPage;
				}
				start = firstPage;
			}
			
			if(end > lastPage) {
				start = start - end + lastPage;
				if(start < firstPage) {
					start = firstPage;
				}
				end = lastPage;
			}
		} else {
			this.end = 1;
			this.start = 1;
			this.firstPage = 1;
			this.lastPage = 1;
			this.nextPage = 1;
			this.previousPage = 1;
			this.yourFirst = false;
			this.yourLast = false;
			this.yourNext = false;
			this.yourPreviousPage = false;
		}
	}
}