package bookRental.tiles.VO;

public class bookVO {
	
	private int idx;			// PK
	private String bname;		// å�̸�
	private String auth;		// ����
	private String publisher;	// ���ǻ�
	private int price;			// ����
	private String rental;		// �뿩 ����
		
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
