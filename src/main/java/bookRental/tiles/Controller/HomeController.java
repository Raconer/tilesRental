
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
	
	
	/* 책 전체 List를 불러와 main.tile에 정보 입력 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String main(HttpServletRequest request, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		
		String page = request.getParameter("page");
		int pageNo = 1;														//	초기 처음의 페이지 번호
		int pageSize = 10;													//	한 페잊  List Size
		int totalCount = 0;													//	총 List 갯수
		String state = "";													//	delete,update를 구분 하는 state(아무것도 입력이 안되면 view만 가능)
		String item = "";													//	검색어
		
		try {pageNo = Integer.parseInt(page);} catch (Exception e) {}		//	현재 페이지 번호
		try {state = request.getParameter("state");} catch (Exception e) {}	//	state인 상태를 넘겨 오면 테이블의 칸이 추가되  update,delete 버튼이 생겨 나기 위한 data
		try {item = request.getParameter("item");} catch (Exception e) {}	//	검색어 data
		
		if(item != null){													//	검색어인 item이 있을때
			totalCount = dao.selectItemCount(item);
		}else{																//	검색어인 item이 없을때
			totalCount = dao.selectCount();
		}
		
		bookList list = new bookList(pageSize, totalCount, pageNo);			//	list를 생성하여 페이지를 startPage 번호와 endPage 번호를 계산한다.
		
		if(item != null){													//	검색어가 존재 할때
			HashMap<Object, Object> map = new HashMap();					//	int 형인 startNo와 endNo 그리고 String형인 item을 HashMap을 사용하여 data를 보낸다.
			map.put("startNo", list.getStartNo());
			map.put("endNo", list.getEndNo());
			map.put("item", item);
			model.addAttribute("item", item);
			list.setList(dao.selectItemList(map));
		}else{																//	검색어가 존재 하지 않을떄
			list.setList(dao.selectList(list.getStartNo(), list.getEndNo()));
		}
		model.addAttribute("list",list);									//	List를 넘겨준다.
		model.addAttribute("state",state);									//	state를 넘겨 준다
		return "main.tiles";												//	main.tiles로 화면 구성
	}
	
	/*	책 입력 하기*/
	@RequestMapping(value="/insert", method = RequestMethod.POST)
	public String bInsert(HttpServletRequest request, Model model){		
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);			
		
		bookVO vo = new bookVO();											//	책을 입력할 vo를 형성한후 정보를 입력한다.											
		vo.setBname(request.getParameter("bName"));
		vo.setAuth(request.getParameter("auth"));
		vo.setPublisher(request.getParameter("publisher"));
		vo.setPrice(Integer.parseInt(request.getParameter("price")));
		
		dao.insert(vo);														//	insert쿼리문 실행
		
		return "CUD.tiles";													//	CUD.tiles로 이동한다.
	}
	
	/*	책 삭제.jsp 이동 */
	@RequestMapping("/delete")										//	삭제 페이지로 이동
	public String delete(Model model){			
		model.addAttribute("state","delete");								//	main.tiles에 "state"안에  "delete"를 넣어 보낸다.
		return "redirect:/index";
	}
	/*	책 삭제 ajax */
	@RequestMapping(value="/dBtn", method=RequestMethod.POST)				//	Delete Button 이란 뜻으로 삭제 버튼을 눌렀을 때 실행된다.
	@ResponseBody
	public boolean dBtn(@RequestParam int idx){								//	ajax에서 idx를 받아온다.
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		boolean i = dao.delete(idx);										//	idx를 삭제하고 삭제가 되었으면 boolean 으로 입력 받고 return 한다.
		return i;
	}
	
	/*	책 수정.jsp 이동 */
	@RequestMapping("/update")										//	수정 페이지로 이동
	public String update(Model model){
		model.addAttribute("state","update");								// "state" 에 "update"를 넣어 보낸다.
		return "redirect:/index";
	}
	
	/*	책 수정 양식이 있는.jsp로 이동 */
	@RequestMapping("/uBtn")											//	Udate Button 이란 뜻으로 update 버튼을 누르면 수정 가능 페이지로 이동한다.
	public String uBtn(HttpServletRequest request,Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		int idx = Integer.parseInt(request.getParameter("idx"));			//	idx를 받아와 selectByIdx(idx로 책 한권의 정보를 얻어온다.)	
		model.addAttribute("state","update");								
		bookVO vo = dao.selectByIdx(idx);
		model.addAttribute("vo",vo);										//	정보를 Vo로 받아와 update.tiles로 넘겨준다.
		return "update.tiles";
	}
	/* 양식이 입력이 된후  update */
	@RequestMapping("/up")
	public String up(HttpServletRequest request,Model model){				//	Update가 되는 문장
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		bookVO vo = new bookVO();											//	Vo를 생성한후 수정된 정보를 입력 받는다.
		vo.setIdx(Integer.parseInt(request.getParameter("idx")));
		vo.setBname(request.getParameter("bname"));
		vo.setAuth(request.getParameter("auth"));
		vo.setPublisher(request.getParameter("publisher"));
		vo.setPrice(Integer.parseInt(request.getParameter("price")));
		
		dao.update(vo);														//	데이터를 수정한다
		
		model.addAttribute("idx",vo.getIdx());								//	idx를 보내서 수정되었을때 를 보여준다.
		return "redirect:uBtn";
	}
	
	/* CUD가 가능한 페이지로 이동 */
	@RequestMapping("/CUD")											//	C : create, U :update, D : delete
	public String CRD(HttpServletRequest request, Model model){
		return "CUD.tiles";													//	CUD.tiles로 이동한다.
	}
	
	/* idx를 기준으로 한권의 책 내용을 얻어온다.*/
	@RequestMapping("/selectByIdx")
	public String selectByIdx(HttpServletRequest request, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		int idx = Integer.parseInt(request.getParameter("idx"));
		bookVO vo = dao.selectByIdx(idx);									//	idx를 기준으로 책정보를 얻어온후
		
		model.addAttribute("vo",vo);										//	vo로 보내준다.
		return "selectByIdx.tiles";
	}
	
	/*	ajax를 사용하여 대여 하기 */
	@RequestMapping(value="rental", method=RequestMethod.POST)
	@ResponseBody
	public boolean rental(@RequestParam int idx, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		dao.rental(idx);
		return true;
	}
	
	/*	ajax를 사용하여 반납하기 */
	@RequestMapping(value="bReturn", method=RequestMethod.POST)
	@ResponseBody
	public boolean bReturn(@RequestParam int idx, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		boolean t = dao.bReturn(idx);
		return t;
	}
	
	/*	대여한 책 목록을 얻어오는 함수 (기존 함수와 같지만 조건이 하나 더 붙었다.)*/
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
