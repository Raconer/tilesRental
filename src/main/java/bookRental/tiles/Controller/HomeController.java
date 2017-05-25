
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
	
	
	/* 홈페이지 main 가장 먼저 출력되는 게시판 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String main(HttpServletRequest request, Model model){
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
			totalCount = dao.selectCount();
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
			list.setList(dao.selectList(list.getStartNo(), list.getEndNo()));
		}
		model.addAttribute("list",list);
		model.addAttribute("state",state);		
		return "main.tiles";
	}
	
	@RequestMapping(value="/insert", method = RequestMethod.POST)
	public String bInsert(HttpServletRequest request, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		
		bookVO vo = new bookVO();
		vo.setBname(request.getParameter("bName"));
		vo.setAuth(request.getParameter("auth"));
		vo.setPublisher(request.getParameter("publisher"));
		vo.setPrice(Integer.parseInt(request.getParameter("price")));
		
		dao.insert(vo);
		
		return "CUD.tiles";
	}
	
	@RequestMapping(value="/delete")
	public String delete(Model model){
		model.addAttribute("state","delete");
		return "redirect:/index";
	}
	
	@RequestMapping(value="/dBtn", method=RequestMethod.POST)
	@ResponseBody
	public boolean dBtn(@RequestParam int idx){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		boolean i = dao.delete(idx);
		return i;
	}
	
	@RequestMapping(value="/CUD")
	public String CRD(HttpServletRequest request, Model model){
		return "CUD.tiles";
	}

	@RequestMapping(value="/selectByIdx")
	public String selectByIdx(HttpServletRequest request, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		int idx = Integer.parseInt(request.getParameter("idx"));
		bookVO vo = dao.selectByIdx(idx);
		
		model.addAttribute("vo",vo);
		return "selectByIdx.tiles";
	}
	
	@RequestMapping(value="/rental", method=RequestMethod.POST)
	@ResponseBody
	public boolean rental(@RequestParam int idx, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		dao.rental(idx);
		return true;
	}
}
