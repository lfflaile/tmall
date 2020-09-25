package com.how2j.tmall.util;

import java.util.List;

import org.springframework.data.domain.Page;


public class Page4Navigator<T> {
	//JPA提供的page对象 用来分页用的
	Page<T> pageFromJPA;
	//指示有多少页 如果传个7的话 就是前面3页 后面3页  像这样 1 2 3 ￥ 5 6 7
	int navigatePages;
	//一共有多少页
	int totalPages;
	int number;
	long totalElements;
	int size;
	int numberOfElements;
	List<T> content;
	boolean isHasContent;
	boolean first;
	boolean last;
	boolean isHasNext;
	boolean isHasPrevious;
	int []navigatepageNums;
	
	public Page4Navigator(){
		
	}
	
	public Page4Navigator(Page<T> pageFromJPA,int navigatePages){
		//获得JPA的page对象
		this.pageFromJPA = pageFromJPA;
		this.navigatePages = navigatePages;
		//返回分页总数
		totalPages = pageFromJPA.getTotalPages();
		//当前第几页
		number = pageFromJPA.getNumber();
		//返回元素总数
		totalElements = pageFromJPA.getTotalElements();
		//返回当前页面大小
		size = pageFromJPA.getSize();
		//返回当前页上的元素数
		numberOfElements = pageFromJPA.getNumberOfElements();
		//将所有数据返回List
		content = pageFromJPA.getContent();
		//返回的数据是否有内容
		isHasContent = pageFromJPA.hasContent();
		//返回当前页是否是第一页
		first = pageFromJPA.isFirst();
		//返回当前页是否是最后一页
		last = pageFromJPA.isLast();
		//返回是否有下一页
		isHasNext = pageFromJPA.hasNext();
		//返回是否有前一页
		isHasPrevious = pageFromJPA.hasPrevious();
		calcNavigatepageNums();
	}
	
	public void calcNavigatepageNums(){
		
		int navigatepageNums[];
		int totalPages = getTotalPages();
		int num = getNumber();
		 //当总页数小于或等于导航页码数时
        if (totalPages <= navigatePages) {
            navigatepageNums = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatepageNums = new int[navigatePages];
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;
  
            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        } 
        this.navigatepageNums = navigatepageNums;
	}

	public Page<T> getPageFromJPA() {
		return pageFromJPA;
	}

	public void setPageFromJPA(Page<T> pageFromJPA) {
		this.pageFromJPA = pageFromJPA;
	}

	public int getNavigatePages() {
		return navigatePages;
	}

	public void setNavigatePages(int navigatePages) {
		this.navigatePages = navigatePages;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public boolean isHasContent() {
		return isHasContent;
	}

	public void setHasContent(boolean isHasContent) {
		this.isHasContent = isHasContent;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isHasNext() {
		return isHasNext;
	}

	public void setHasNext(boolean isHasNext) {
		this.isHasNext = isHasNext;
	}

	public boolean isHasPrevious() {
		return isHasPrevious;
	}

	public void setHasPrevious(boolean isHasPrevious) {
		this.isHasPrevious = isHasPrevious;
	}

	public int[] getNavigatepageNums() {
		return navigatepageNums;
	}

	public void setNavigatepageNums(int[] navigatepageNums) {
		this.navigatepageNums = navigatepageNums;
	}
	
	
}
