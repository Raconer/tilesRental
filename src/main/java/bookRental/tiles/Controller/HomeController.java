
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
	private SimpleDateFormat sdf = new SimpleDateFormat("yy�� MM�� dd�� HH��mm��ss��");
	
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
	@RequestMapping("/delete")												//	���� �������� �̵�
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
	@RequestMapping("/update")												//	���� �������� �̵�
	public String update(Model model){
		model.addAttribute("state","update");								// "state" �� "update"�� �־� ������.
		return "redirect:/index";
	}
	
	/*	å ���� ����� �ִ�.jsp�� �̵� */
	@RequestMapping("/uBtn")												//	Udate Button �̶� ������ update ��ư�� ������ ���� ���� �������� �̵��Ѵ�.
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
		vo.setBname(request.getParameter("bName"));
		vo.setAuth(request.getParameter("auth"));
		vo.setPublisher(request.getParameter("publisher"));
		vo.setPrice(Integer.parseInt(request.getParameter("price")));
		
		dao.update(vo);
		model.addAttribute("idx",vo.getIdx());								//	idx�� ������ �����Ǿ����� �� �����ش�.
		
		return "redirect:uBtn";
	}
	
	/* CUD�� ������ �������� �̵� */
	@RequestMapping("/CUD")													//	C : create, U :update, D : delete
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
		dao.rentalList(idx);
		return true;
	}
	
	/*	ajax�� ����Ͽ� �ݳ��ϱ� */
	@RequestMapping(value="bReturn", method=RequestMethod.POST)
	@ResponseBody
	public boolean bReturn(@RequestParam int idx, Model model){
		MybatisDAO dao = sqlSession.getMapper(MybatisDAO.class);
		Date date;
		Date date1 = new Date();	
		int R = 0;
		date = dao.selectDate(idx);
		System.out.println("�뿩�ð� : "+sdf.format(date)+" �ݳ��ð� : " +sdf.format(date1));
		if(date1.after(date)){
			R = 1;
		}
		dao.RBList(idx,R);													//	å �ݳ��� å �뿩 ����Ʈ �ݳ��ð� �߰�
		
		
		boolean t = dao.bReturn(idx);										//	å �ݳ��ϱ�
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
	  		data +="		�뿩 �ð� : " + sdf.format(rVO.getrDate()) + "		�ݳ��ð� : " + (rVO.getbDate() == null?"	�̹ݳ�":sdf.format(rVO.getbDate()))+" " +(rVO.getOverRental() == 1? "	��ü" : "") +"\r\n\r\n";
	  		id = rVO.getBook();
	  	}
	
		Date date = new Date();
	  	String fileName = sdf.format(date)+".txt";					//������ ���ϸ�
		String filePath = request.getSession().getServletContext().getRealPath("/");						//������ ���ϸ��� ��ü��ο� ����
		String totalName = filePath + fileName;
		try{
		  		File f = new File(totalName); 								// ���ϰ�ü����
		  		f.createNewFile(); 										//���ϻ���
		  // ���Ͼ���
		  	FileWriter fw = new FileWriter(totalName); 						//���Ͼ��ⰴü����
	
		  	fw.write(data); 												//���Ͽ��� �ۼ�
		  	fw.close(); }catch (IOException e) { 
			    System.out.println(e.toString()); 							//���� �߻��� �޽��� ���
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
		HSSFSheet sheet = workbook.createSheet("å �뿩 ���");
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
			cell.setCellValue("�뿩�ð�");
			cell = row.createCell((short)2);
			cell.setCellValue(sdf.format(rVO.getrDate()));
			cell = row.createCell((short)3);
			cell.setCellValue("�ݳ��ð� ");
			cell = row.createCell((short)4);
			cell.setCellValue( (rVO.getbDate() == null?"	�̹ݳ�":sdf.format(rVO.getbDate())));
			cell = row.createCell((short)6);
			cell.setCellValue((rVO.getOverRental() == 1? "	��ü" : ""));
	  		//data +="		�뿩 �ð� : " + sdf.format(rVO.getrDate()) + "		�ݳ��ð� : " + (rVO.getbDate() == null?"	�̹ݳ�":sdf.format(rVO.getbDate()))+" " +(rVO.getOverRental() == 1? "	��ü" : "") +"\r\n\r\n";
	  		id = rVO.getBook();
	  	}
		
		String fileName = sdf.format(new Date())+".xls"; 							//������ ���ϸ�
		String filePath = request.getSession().getServletContext().getRealPath("./"); //������ ������ ��ü���
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
	        // ������ �о� ��Ʈ���� ���
	        try{
	            file = new File(savePath, fileName);
	            in = new FileInputStream(file);
	        }catch(FileNotFoundException fe){
	            skip = true;
	        }
	        client = request.getHeader("User-Agent");
	        // ���� �ٿ�ε� ��� ����
	        response.reset();
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Description", "JSP Generated Data");
	        if(!skip){
	        	// IE
	            if(client.indexOf("MSIE") != -1){
	                response.setHeader ("Content-Disposition", "attachment; filename="+new String(orgfilename.getBytes("KSC5601"),"ISO8859_1"));
	            }else{
	                // �ѱ� ���ϸ� ó��
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