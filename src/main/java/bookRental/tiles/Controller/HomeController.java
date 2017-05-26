
package bookRental.tiles.Controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bookRental.tiles.DAO.MybatisDAO;
import bookRental.tiles.VO.bookList;
import bookRental.tiles.VO.bookVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private SqlSession sqlSession;
	
	
	/* å ��ü List�� �ҷ��� main.tile�� ���� �Է� */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String main(HttpServletRequest request, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		
		String page = request.getParameter("page");
		int pageNo = 1;														//	�ʱ� ó���� ������ ��ȣ
		int pageSize = 10;													//	�� ����  List Size
		int totalCount = 0;													//	�� List ����
		String state = "";													//	delete,update�� ���� �ϴ� state(�ƹ��͵� �Է��� �ȵǸ� view�� ����)
		String item = "";													//	�˻���
		
		try {pageNo = Integer.parseInt(page);} catch (Exception e) {}		//	���� ������ ��ȣ
		try {state = request.getParameter("state");} catch (Exception e) {}	//	state�� ���¸� �Ѱ� ���� ���̺��� ĭ�� �߰���  update,delete ��ư�� ���� ���� ���� data
		try {item = request.getParameter("item");} catch (Exception e) {}	//	�˻��� data
		
		if(item != null){													//	�˻����� item�� ������
			totalCount = dao.selectItemCount(item);
		}else{																//	�˻����� item�� ������
			totalCount = dao.selectCount();
		}
		
		bookList list = new bookList(pageSize, totalCount, pageNo);			//	list�� �����Ͽ� �������� startPage ��ȣ�� endPage ��ȣ�� ����Ѵ�.
		
		if(item != null){													//	�˻�� ���� �Ҷ�
			HashMap<Object, Object> map = new HashMap();					//	int ���� startNo�� endNo �׸��� String���� item�� HashMap�� ����Ͽ� data�� ������.
			map.put("startNo", list.getStartNo());
			map.put("endNo", list.getEndNo());
			map.put("item", item);
			model.addAttribute("item", item);
			list.setList(dao.selectItemList(map));
		}else{																//	�˻�� ���� ���� ������
			list.setList(dao.selectList(list.getStartNo(), list.getEndNo()));
		}
		model.addAttribute("list",list);									//	List�� �Ѱ��ش�.
		model.addAttribute("state",state);									//	state�� �Ѱ� �ش�
		return "main.tiles";												//	main.tiles�� ȭ�� ����
	}
	
	/*	å �Է� �ϱ�*/
	@RequestMapping(value="/insert", method = RequestMethod.POST)
	public String bInsert(HttpServletRequest request, Model model){		
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);			
		
		bookVO vo = new bookVO();											//	å�� �Է��� vo�� �������� ������ �Է��Ѵ�.											
		vo.setBname(request.getParameter("bName"));
		vo.setAuth(request.getParameter("auth"));
		vo.setPublisher(request.getParameter("publisher"));
		vo.setPrice(Integer.parseInt(request.getParameter("price")));
		
		dao.insert(vo);														//	insert������ ����
		
		return "CUD.tiles";													//	CUD.tiles�� �̵��Ѵ�.
	}
	
	/*	å ����.jsp �̵� */
	@RequestMapping("/delete")										//	���� �������� �̵�
	public String delete(Model model){			
		model.addAttribute("state","delete");								//	main.tiles�� "state"�ȿ�  "delete"�� �־� ������.
		return "redirect:/index";
	}
	/*	å ���� ajax */
	@RequestMapping(value="/dBtn", method=RequestMethod.POST)				//	Delete Button �̶� ������ ���� ��ư�� ������ �� ����ȴ�.
	@ResponseBody
	public boolean dBtn(@RequestParam int idx){								//	ajax���� idx�� �޾ƿ´�.
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		boolean i = dao.delete(idx);										//	idx�� �����ϰ� ������ �Ǿ����� boolean ���� �Է� �ް� return �Ѵ�.
		return i;
	}
	
	/*	å ����.jsp �̵� */
	@RequestMapping("/update")										//	���� �������� �̵�
	public String update(Model model){
		model.addAttribute("state","update");								// "state" �� "update"�� �־� ������.
		return "redirect:/index";
	}
	
	/*	å ���� ����� �ִ�.jsp�� �̵� */
	@RequestMapping("/uBtn")											//	Udate Button �̶� ������ update ��ư�� ������ ���� ���� �������� �̵��Ѵ�.
	public String uBtn(HttpServletRequest request,Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		int idx = Integer.parseInt(request.getParameter("idx"));			//	idx�� �޾ƿ� selectByIdx(idx�� å �ѱ��� ������ ���´�.)	
		model.addAttribute("state","update");								
		bookVO vo = dao.selectByIdx(idx);
		model.addAttribute("vo",vo);										//	������ Vo�� �޾ƿ� update.tiles�� �Ѱ��ش�.
		return "update.tiles";
	}
	/* ����� �Է��� ����  update */
	@RequestMapping("/up")
	public String up(HttpServletRequest request,Model model){				//	Update�� �Ǵ� ����
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		bookVO vo = new bookVO();											//	Vo�� �������� ������ ������ �Է� �޴´�.
		vo.setIdx(Integer.parseInt(request.getParameter("idx")));
		vo.setBname(request.getParameter("bname"));
		vo.setAuth(request.getParameter("auth"));
		vo.setPublisher(request.getParameter("publisher"));
		vo.setPrice(Integer.parseInt(request.getParameter("price")));
		
		dao.update(vo);														//	�����͸� �����Ѵ�
		
		model.addAttribute("idx",vo.getIdx());								//	idx�� ������ �����Ǿ����� �� �����ش�.
		return "redirect:uBtn";
	}
	
	/* CUD�� ������ �������� �̵� */
	@RequestMapping("/CUD")											//	C : create, U :update, D : delete
	public String CRD(HttpServletRequest request, Model model){
		return "CUD.tiles";													//	CUD.tiles�� �̵��Ѵ�.
	}
	
	/* idx�� �������� �ѱ��� å ������ ���´�.*/
	@RequestMapping("/selectByIdx")
	public String selectByIdx(HttpServletRequest request, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		int idx = Integer.parseInt(request.getParameter("idx"));
		bookVO vo = dao.selectByIdx(idx);									//	idx�� �������� å������ ������
		
		model.addAttribute("vo",vo);										//	vo�� �����ش�.
		return "selectByIdx.tiles";
	}
	
	/*	ajax�� ����Ͽ� �뿩 �ϱ� */
	@RequestMapping(value="rental", method=RequestMethod.POST)
	@ResponseBody
	public boolean rental(@RequestParam int idx, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		dao.rental(idx);
		return true;
	}
	
	/*	ajax�� ����Ͽ� �ݳ��ϱ� */
	@RequestMapping(value="bReturn", method=RequestMethod.POST)
	@ResponseBody
	public boolean bReturn(@RequestParam int idx, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		boolean t = dao.bReturn(idx);
		return t;
	}
	
	/*	�뿩�� å ����� ������ �Լ� (���� �Լ��� ������ ������ �ϳ� �� �پ���.)*/
	@RequestMapping("/ren")
	public String ren(HttpServletRequest request,Model model){
	MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		
		String page = request.getParameter("page");
		int pageNo = 1;
		int pageSize = 10;
		int totalCount = 0;
		String state = "";
		String item = "";
		
		try {pageNo = Integer.parseInt(page);} catch (Exception e) {}
		try {state = request.getParameter("state");} catch (Exception e) {}
		try {item = request.getParameter("item");} catch (Exception e) {}
		
		if(item != null){
			totalCount = dao.selectItemCount(item);
		}else{
			totalCount = dao.selectRentalCount();
		}
		bookList list = new bookList(pageSize, totalCount, pageNo);
		if(item != null){
			HashMap<Object, Object> map = new HashMap();
			map.put("startNo", list.getStartNo());
			map.put("endNo", list.getEndNo());
			map.put("item", "%"+item+"%");
			model.addAttribute("item", item);
			list.setList(dao.selectItemList(map));
		}else{
			list.setList(dao.selectRentalList(list.getStartNo(), list.getEndNo()));
		}
		model.addAttribute("list",list);
		model.addAttribute("state",state);		
		return "rental.tiles";
	}
}
