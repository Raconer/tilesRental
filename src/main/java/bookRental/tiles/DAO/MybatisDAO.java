package bookRental.tiles.DAO;

import java.util.ArrayList;

import bookRental.tiles.VO.bookVO;


public interface MybatisDAO {

	ArrayList<bookVO> selectList(int startNo, int endNo);
	int selectCount();
	void insert(bookVO vo);
	bookVO selectByIdx(int i);

}
