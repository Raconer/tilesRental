package bookRental.tiles.DAO;

import java.util.ArrayList;
import java.util.HashMap;

import bookRental.tiles.VO.bookVO;


public interface MybatisDAO {

	ArrayList<bookVO> selectList(int startNo, int endNo);			//	�� å List
	ArrayList<bookVO> selectItemList(HashMap<Object, Object> map);	//	�˻� ������ List
	ArrayList<bookVO> selectRentalList(int startNo, int endNo);		//	�뿩�� å List

	int selectCount();												//	å ��ü ����
	int selectItemCount(String search);								//	�˻��� å�� ��ü ����
	int selectRentalCount();										//	�뿩�� å�� ��ü ����
	
	
	bookVO selectByIdx(int idx);									//	å �ѱ��� ������ �б�

	void insert(bookVO vo);											//	å �߰�
	boolean delete(int idx);										//	å ����
	void update(bookVO vo);											//	å ����
	
	void rental(int idx);											//	å �뿩 �ϱ�
	boolean bReturn(int idx);										//	å �ݳ� �ϱ�
}
