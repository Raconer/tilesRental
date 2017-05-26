package bookRental.tiles.VO;

import java.util.ArrayList;

public class bookList {
	private ArrayList<bookVO> list;	//	계산된 페이지 별로 가져온 책List
	private int totalCount;			//	책의 총갯수
	private int totalPage;			//	리스트의 갯수로 나눈 page수
	private int currentPage;		//	현재 페이지 번호
	private int pageSize;			//	화면당 불러올 리스트 갯수
	private int startNo;			//	시작 번호
	private int endNo;				//	총 번호
	private int startPage;			//	페이지 시작 번호
	private int endPage;			//	페이지 마지막 번호 (ex.1~9,10~19,...)
	
	public bookList(int pageSize,int totalCount,int currentPage) {
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.currentPage = currentPage;
		calculator();	
	}
	private void calculator() {
		totalPage = (totalCount - 1) /pageSize + 1;
		currentPage = currentPage > totalPage? totalPage :currentPage;
		startNo = (currentPage - 1) * pageSize + 1;
		endNo = startNo + pageSize - 1;
		endNo = endNo > totalCount ? totalCount : endNo;
		startPage = (currentPage - 1)/ 10 * 10 + 1;
		endPage  = startPage + 9;
		endPage = endPage >  totalPage ?  totalPage : endPage;
	}
	
	public ArrayList<bookVO> getList() {
		return list;
	}
	public void setList(ArrayList<bookVO> list) {
		this.list = list;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getStartNo() {
		return startNo;
	}
	public void setStartNo(int startNo) {
		this.startNo = startNo;
	}
	public int getEndNo() {
		return endNo;
	}
	public void setEndNo(int endNo) {
		this.endNo = endNo;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	
	@Override
	public String toString() {
		return "bookList [list=" + list + ", totalCount=" + totalCount + ", totalPage=" + totalPage + ", currentPage="
				+ currentPage + ", pageSize=" + pageSize + ", startNo=" + startNo + ", endNo=" + endNo + ", startPage="
				+ startPage + ", endPage=" + endPage + "]";
	}
}
