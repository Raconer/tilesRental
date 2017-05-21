
package bookRental.tiles.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
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
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String main(HttpServletRequest request, Model model){
		System.out.println("main 접속 성공");
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		
		String page = request.getParameter("page");
		int pageNo = 1;
		int pageSize = 10;
		int totalCount = 0;
		
		try {pageNo = Integer.parseInt(page);} catch (Exception e) {}
		totalCount = dao.selectCount();
		bookList list = new bookList(pageSize, totalCount, pageNo);
		
		list.setList(dao.selectList(list.getStartNo(), list.getEndNo()));
		
		model.addAttribute("list",list);
		
		return "main.tiles";
	}
	
	@RequestMapping("insert.do")
	public String bInsert(HttpServletRequest request, Model model){
		System.out.println("잘들어온다");
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		
		bookVO vo = new bookVO();
		vo.setBname(request.getParameter("bName"));
		vo.setAuth(request.getParameter("auth"));
		vo.setPublisher(request.getParameter("publisher"));
		vo.setPrice(Integer.parseInt(request.getParameter("price")));
		
		dao.insert(vo);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="itemSearch", method=RequestMethod.POST)
	@ResponseBody
	public bookList formOK(HttpServletRequest request,@RequestParam String keyword, @RequestParam String search, Model model) {
		System.out.println("된다."+keyword+" "+search);
		
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		
		String page = request.getParameter("page");
		int pageNo = 1;
		int pageSize = 10;
		int totalCount = 0;
		
		try {pageNo = Integer.parseInt(page);} catch (Exception e) {}
		totalCount = dao.selectCount();
		bookList list = new bookList(pageSize, totalCount, pageNo);
		list.setList(dao.selectList(list.getStartNo(), list.getEndNo()));
		return  list;
	}
}
