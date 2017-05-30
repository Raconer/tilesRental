package bookRental.tiles.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import bookRental.tiles.VO.bookVO;
import bookRental.tiles.VO.rentalVO;


public interface MybatisDAO {

	ArrayList<bookVO> selectList(int startNo, int endNo);			//	총 책 List
	ArrayList<bookVO> selectItemList(HashMap<Object, Object> map);	//	검색 했을때 List
	ArrayList<bookVO> selectRentalList(int startNo, int endNo);		//	대여한 책 List

	int selectCount();												//	책 전체 갯수
	int selectItemCount(String search);								//	검색한 책의 전체 갯수
	int selectRentalCount();										//	대여한 책의 전체 갯수
	
	
	bookVO selectByIdx(int idx);									//	책 한권의 정보를 읽기

	void insert(bookVO vo);											//	책 추가
	boolean delete(int idx);										//	책 삭제
	void update(bookVO vo);											//	책 수정
	
	void rental(int idx);											//	책 대여 하기
	boolean bReturn(int idx);										//	책 반납 하기
	
	void rentalList(int idx);										//	대여 기록 리스트 추가
	void RBList(int idx, int r);									//	반납시 대여 기록 리스트 반납 시간 Update
	Date selectDate(int idx);										//	연체 시간을 알기 위해 기존의 대여시간에 +(연체기준 시간) 을 하여 출력
	
	ArrayList<rentalVO> rentalRecord();								//	대여 기록 
	
}
