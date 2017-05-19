package bookRental.tiles.Controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bookRental.tiles.DAO.MybatisDAO;
import bookRental.tiles.VO.bookList;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String main(HttpServletRequest request, Model model){
		System.out.println("main ���� ����");
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
	
	@RequestMapping("bInsert")
	public String bInsert(){
		
		return "insert.tiles";
	}
	
}
