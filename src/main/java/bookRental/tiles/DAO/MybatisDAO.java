package bookRental.tiles.DAO;

import java.util.ArrayList;
import java.util.HashMap;

import bookRental.tiles.VO.bookVO;


public interface MybatisDAO {

	ArrayList<bookVO> selectList(int startNo, int endNo);
	int selectCount();
	void insert(bookVO vo);
	bookVO selectByIdx(int i);

	int selectItemCount(String search);
	ArrayList<bookVO> selectItemList(HashMap<Object, Object> map);
	void rental(int idx);
	boolean delete(int idx);

}
