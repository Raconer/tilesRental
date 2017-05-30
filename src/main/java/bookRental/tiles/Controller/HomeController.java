
package bookRental.tiles.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import bookRental.tiles.VO.rentalVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private SqlSession sqlSession;
	private SimpleDateFormat sdf = new SimpleDateFormat("yy년 MM월 dd일 HH시mm분ss초");
	
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
	@RequestMapping("/delete")												//	삭제 페이지로 이동
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
	@RequestMapping("/update")												//	수정 페이지로 이동
	public String update(Model model){
		model.addAttribute("state","update");								// "state" 에 "update"를 넣어 보낸다.
		return "redirect:/index";
	}
	
	/*	책 수정 양식이 있는.jsp로 이동 */
	@RequestMapping("/uBtn")												//	Udate Button 이란 뜻으로 update 버튼을 누르면 수정 가능 페이지로 이동한다.
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
		vo.setBname(request.getParameter("bName"));
		vo.setAuth(request.getParameter("auth"));
		vo.setPublisher(request.getParameter("publisher"));
		vo.setPrice(Integer.parseInt(request.getParameter("price")));
		
		dao.update(vo);
		model.addAttribute("idx",vo.getIdx());								//	idx를 보내서 수정되었을때 를 보여준다.
		
		return "redirect:uBtn";
	}
	
	/* CUD가 가능한 페이지로 이동 */
	@RequestMapping("/CUD")													//	C : create, U :update, D : delete
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
		dao.rentalList(idx);
		return true;
	}
	
	/*	ajax를 사용하여 반납하기 */
	@RequestMapping(value="bReturn", method=RequestMethod.POST)
	@ResponseBody
	public boolean bReturn(@RequestParam int idx, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		Date date;
		Date date1 = new Date();	
		int R = 0;
		date = dao.selectDate(idx);
		System.out.println("대여시간 : "+sdf.format(date)+" 반납시간 : " +sdf.format(date1));
		if(date1.after(date)){
			R = 1;
		}
		dao.RBList(idx,R);													//	책 반납시 책 대여 리스트 반납시간 추가
		
		
		boolean t = dao.bReturn(idx);										//	책 반납하기
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
			map.put("item", item);
			model.addAttribute("item", item);
			list.setList(dao.selectItemList(map));
		}else{
			list.setList(dao.selectRentalList(list.getStartNo(), list.getEndNo()));
		}
		model.addAttribute("list",list);
		model.addAttribute("state",state);		
		return "rental.tiles";
	}
	
	@RequestMapping("File")
	public String file(Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		int id = 0;
		ArrayList<rentalVO> rList = dao.rentalRecord();
		ArrayList<bookVO> list = new ArrayList<bookVO>();
	  	for(rentalVO rVO : rList){
	  		if(id != rVO.getBook()){
	  			list.add(dao.selectByIdx(rVO.getBook()));
	  		}
	  		id = rVO.getBook();
	  	}
	  	model.addAttribute("rList", rList);
	  	model.addAttribute("list", list);
		return "rentalList.tiles";
	}

	@RequestMapping("txtDown")
	public String txtDown(HttpServletRequest request, Model mode){
	  	MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
	  	
	  	String data = "";
	  	int id = 0;
	  	ArrayList<rentalVO> rList = dao.rentalRecord(); 
	  	
	  	for(rentalVO rVO : rList){
	  		if(id != rVO.getBook()){
	  			bookVO vo = dao.selectByIdx(rVO.getBook());
	  			data += vo.toString()+"\r\n\r\n";
	  		}
	  		data +="		대여 시간 : " + sdf.format(rVO.getrDate()) + "		반납시간 : " + (rVO.getbDate() == null?"	미반납":sdf.format(rVO.getbDate()))+" " +(rVO.getOverRental() == 1? "	연체" : "") +"\r\n\r\n";
	  		id = rVO.getBook();
	  	}
	
		Date date = new Date();
	  	String fileName = sdf.format(date)+".txt";					//생성할 파일명
		String filePath = request.getSession().getServletContext().getRealPath("/");						//생성할 파일명을 전체경로에 결합
		String totalName = filePath + fileName;
		try{
		  		File f = new File(totalName); 								// 파일객체생성
		  		f.createNewFile(); 										//파일생성
		  // 파일쓰기
		  	FileWriter fw = new FileWriter(totalName); 						//파일쓰기객체생성
	
		  	fw.write(data); 												//파일에다 작성
		  	fw.close(); }catch (IOException e) { 
			    System.out.println(e.toString()); 							//에러 발생시 메시지 출력
			 }	
	  
		mode.addAttribute("fileName",fileName);
		
		return "redirect:down";
	}

	@RequestMapping("excelDown")
	public String excelDown(HttpServletRequest request, Model mode) throws IOException{
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		ArrayList<rentalVO> rList = dao.rentalRecord(); 
		int id = 0, i = 0;
	
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("책 대여 기록");
		HSSFRow		row		= null;
		HSSFCell	cell	= null;
	
		
		for(rentalVO rVO : rList){
			row = sheet.createRow(i++);
			if(id != rVO.getBook()){
	  			bookVO vo = dao.selectByIdx(rVO.getBook());
	  			for(short j=0; j < 10; j++){
	  				cell = row.createCell((short)j);
	  				switch(j){
	  					case 0:
	  						cell.setCellValue("idx");
	  						break;
	  					case 1:
	  						cell.setCellValue(vo.getIdx());
	  						break;
	  					case 2:
	  						cell.setCellValue("bname");
	  						break;
	  					case 3:
	  						cell.setCellValue(vo.getBname());
	  						break;
	  					case 4:
	  						cell.setCellValue("auth");
	  						break;
	  					case 5:
	  						cell.setCellValue(vo.getAuth());
	  						break;
	  					case 6:
	  						cell.setCellValue("publisher");
	  						break;
	  					case 7:
	  						cell.setCellValue(vo.getPublisher());
	  						break;
	  					case 8:
	  						cell.setCellValue("price");
	  						break;
	  					case 9:
	  						cell.setCellValue(vo.getPrice());
	  						break;
	  				}
	  			}
	  			row = sheet.createRow(i++);
	  		}
			
			cell = row.createCell((short)1);
			cell.setCellValue("대여시간");
			cell = row.createCell((short)2);
			cell.setCellValue(sdf.format(rVO.getrDate()));
			cell = row.createCell((short)3);
			cell.setCellValue("반납시간 ");
			cell = row.createCell((short)4);
			cell.setCellValue( (rVO.getbDate() == null?"	미반납":sdf.format(rVO.getbDate())));
			cell = row.createCell((short)6);
			cell.setCellValue((rVO.getOverRental() == 1? "	연체" : ""));
	  		//data +="		대여 시간 : " + sdf.format(rVO.getrDate()) + "		반납시간 : " + (rVO.getbDate() == null?"	미반납":sdf.format(rVO.getbDate()))+" " +(rVO.getOverRental() == 1? "	연체" : "") +"\r\n\r\n";
	  		id = rVO.getBook();
	  	}
		
		String fileName = sdf.format(new Date())+".xls"; 							//생성할 파일명
		String filePath = request.getSession().getServletContext().getRealPath("./"); //파일을 생성할 전체경로
		filePath += fileName;
		
		FileOutputStream fs = new FileOutputStream(filePath);
		workbook.write(fs);
		fs.close();
		
		
		mode.addAttribute("fileName",fileName);
		return "redirect:down";
	}
	
	@RequestMapping("down")
	public void down(HttpServletRequest request, HttpServletResponse response){
	  
		String root = request.getSession().getServletContext().getRealPath("/");
		String savePath = root;
		String fileName = request.getParameter("fileName");
		String orgfilename = fileName;	      
	    InputStream in = null;
	    OutputStream os = null;
	    File file = null;
	    boolean skip = false;
	    String client = "";
	 
	    try{
	        // 파일을 읽어 스트림에 담기
	        try{
	            file = new File(savePath, fileName);
	            in = new FileInputStream(file);
	        }catch(FileNotFoundException fe){
	            skip = true;
	        }
	        client = request.getHeader("User-Agent");
	        // 파일 다운로드 헤더 지정
	        response.reset();
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Description", "JSP Generated Data");
	        if(!skip){
	        	// IE
	            if(client.indexOf("MSIE") != -1){
	                response.setHeader ("Content-Disposition", "attachment; filename="+new String(orgfilename.getBytes("KSC5601"),"ISO8859_1"));
	            }else{
	                // 한글 파일명 처리
	                orgfilename = new String(orgfilename.getBytes("utf-8"),"iso-8859-1");
	                response.setHeader("Content-Disposition", "attachment; filename=\"" + orgfilename + "\"");
	                response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
	            } 
	            response.setHeader ("Content-Length", ""+file.length() );
	            os = response.getOutputStream();
	            byte b[] = new byte[(int)file.length()];
	            int leng = 0;
	            while( (leng = in.read(b)) > 0 ){
	                os.write(b,0,leng);
	            }
	        }else{
	            response.setContentType("text/html;charset=UTF-8");
	          
	        }
	        in.close();
	        os.close();
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	    if( file.exists()){
	    	file.delete();
	    } 
	}
}