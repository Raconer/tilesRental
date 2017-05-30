package bookRental.tiles.VO;

public class bookVO {
	
	private int idx;			// PK
	private String bname;		// 책이름
	private String auth;		// 저자
	private String publisher;	// 출판사
	private int price;			// 가격
	private String rental;		// 대여 여부
		
	public bookVO() {

	}
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String getRental() {
		return rental;
	}
	public void setRental(String rental) {
		this.rental = rental;
	}
	
	@Override
	public String toString() {
		return "idx=" + idx + ", bname=" + bname + ", auth=" + auth + ", publisher=" + publisher + ", price="+ price;
	}
	
}
