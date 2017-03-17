/**
 * 
 */
package cn.hm.oil.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.CheckLog;
import cn.hm.oil.domain.Notice;
import cn.hm.oil.domain.NoticeRead;
import cn.hm.oil.domain.NoticeReply;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.TipsService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;

/**
 * @author Administrator
 * 
 *         管道保护电位测量_管理
 * 
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BaseController {

	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TipsService tipsService;

	/**
	 * 获取管线下的所有起止段落
	 *   
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/section/get", method = RequestMethod.GET)
	public @ResponseBody List<BasePipelineSection> section_get(Model model,
			@RequestParam Integer pl_id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userService.queryRoleIdByUserId(ud.getId()) != 3) {
			return baseService.querySection(pl_id);
		}
		return baseService.querySectionByUser(pl_id, ud.getId());
	}

	/**
	 * 获取起止段落下的规格
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/spec/get", method = RequestMethod.GET)
	public @ResponseBody List<BasePipelineSpec> spec_get(Model model,
			@RequestParam Integer pl_section_id) {
		return baseService.querySpec(pl_section_id);
	}

	/**
	 * 获取管线下的所有起止段落
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/section/get_by_user", method = RequestMethod.GET)
	public @ResponseBody List<BasePipelineSection> section_getByUser(
			Model model,@RequestParam Integer pl_id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userService.queryRoleIdByUserId(ud.getId()) != 3) {
			return baseService.querySection(pl_id);
		}
		return baseService.querySectionByUser(pl_id, ud.getId());
	}

	/**
	 * 获取起止段落下的规格
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/spec/get_by_user", method = RequestMethod.GET)
	public @ResponseBody List<BasePipelineSpec> spec_getByUser(Model model,
			@RequestParam Integer pl_section_id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userService.queryRoleIdByUserId(ud.getId()) != 3) {
			return baseService.querySpec(pl_section_id);
		}
		return baseService.querySpecByUser(pl_section_id, ud.getId());
	}

	
	//考勤后台管理
	@RequestMapping(value = "/check/list", method = {RequestMethod.GET,RequestMethod.POST})
	public String check_list(Model model,HttpServletRequest request,@RequestParam(required = false) String s_time,@RequestParam(required = false) String e_time,@RequestParam(required = false) Integer del){
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		if(!StringUtils.isBlank(s_time)){
			param.put("s_time", s_time);
			param.put("e_time", e_time);
			model.addAttribute("s_time", s_time);
			model.addAttribute("e_time", e_time);
		}
		List<CheckLog> checks = tipsService.queryCheckList(param, ps);
		model.addAttribute("checks", checks);
		if(del!=null && del.intValue() == 1){
			model.addAttribute("msg", "删除成功");
		}
		return "pages/check/list";
	}
	
	@RequestMapping(value = "/check/add", method = RequestMethod.GET)
	public String checkadd(Model model) {
		
		return "pages/check/add";
	}
	
	@RequestMapping(value = "/check/del", method = RequestMethod.GET)
	public String checkdel(Model model,@RequestParam Integer id) {
		tipsService.deleteCheckLogByid(id);
		return "redirect:list?del=1";
	}
	
	@RequestMapping(value = "/check/edit", method = RequestMethod.GET)
	public String checkedit(Model model,@RequestParam Integer id,@RequestParam(required = false) Integer add) {
		CheckLog cl = tipsService.queryCheckById(id);
		model.addAttribute("cl", cl);
		if(add!=null && add.intValue() == 1){
			model.addAttribute("msg", "保存成功!");
		}
		return "pages/check/edit";
	}
	
	@RequestMapping(value = "/check/save", method = RequestMethod.POST)
	public String checksave(Model model,HttpServletRequest request,
			@RequestParam(required = false) Integer id,
			@RequestParam String content,
			@RequestParam String user_name,
			@RequestParam String reason,
			@RequestParam String remark) {
		CheckLog cl = new CheckLog();
		cl.setId(id);
		cl.setContent(content);
		cl.setUser_name(user_name);
		cl.setReason(reason);
		cl.setRemark(remark);
		tipsService.saveCheckLog(cl);
		
		return "redirect:edit?add=1&id="+cl.getId();
	}
	
	@RequestMapping(value = "/check/exp", method = {RequestMethod.GET,RequestMethod.POST})
	public String check_exp(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String s_time,@RequestParam(required = false) String e_time){
		Map<String,Object> param = new HashMap<String,Object>();
		if(!StringUtils.isBlank(s_time)){
			param.put("s_time", s_time);
			param.put("e_time", e_time);
		}
		List<CheckLog> checks = tipsService.queryCheckList(param, null);
		if(CollectionUtils.isEmpty(checks)){
			return "redirect:list";
		}
		try {
			// 设置返回为下载
			String en = "考勤信息.xls";
			String excelName = new String(en.getBytes("UTF-8"), "ISO-8859-1");
			response.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");
			   if (request.getHeader("user-agent").toLowerCase().indexOf("msie") > 0)//IE浏览器
			   {
			       response.setHeader("content-disposition", "filename=" + java.net.URLEncoder.encode(en, "UTF-8"));
			   } else {
				   response.setHeader("Content-Disposition", "attachment; filename="
						   + excelName);
			   }
			OutputStream os = null;
			// HSSFWorkbook work = null;
			HSSFWorkbook work = new HSSFWorkbook();
			HSSFSheet sheet1 = work.createSheet("sheet1");

			// 合并单元格
			sheet1.addMergedRegion(new CellRangeAddress(0, 2, 0, 4));

			
			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 表头格式
			CellStyle titleStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();

			/*
			 * //内容加上边框 dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			 * //dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			 * dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			 * //dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			 * dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			 * //dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			 * dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			 * //dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			 */

			// 居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle.setWrapText(true);

			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);

			// 标题字体
			Font font = work.createFont();
			// 表头字体
			Font titlefont = work.createFont();
			// 内容字体
			Font datafont = work.createFont();

			font.setFontHeightInPoints((short) 16);
			font.setFontName("宋体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			titlefont.setFontHeightInPoints((short) 11);
			titlefont.setFontName("宋体");
			titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 11);
			datafont.setFontName("宋体");
			// datafont.setColor(HSSFColor.RED.index);

			// 把字体加入到格式中
			cellStyle.setFont(font);
			titleStyle.setFont(titlefont);
			dataStyle.setFont(datafont);

			sheet1.setColumnWidth(0, 24 * 256);
			sheet1.setColumnWidth(1, 24 * 256);
			sheet1.setColumnWidth(2, 24 * 256);
			sheet1.setColumnWidth(3, 24 * 256);
			sheet1.setColumnWidth(4, 24 * 256);
			
			// 设置标题
			Row row = sheet1.createRow(0);
			Cell cell = row.createCell(0);
			// 设置内容
			cell.setCellValue("考勤信息");
			// 设置单元格格式
			cell.setCellStyle(cellStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			// 设置表头
			row = sheet1.createRow(3);

			cell = row.createCell(0);
			// 设置内容
			cell.setCellValue("内容");
			// 设置单元格格式
			cell.setCellStyle(titleStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			// 二层
			cell = row.createCell(1);
			// 设置内容
			cell.setCellValue("责任人");
			// 设置单元格格式
			cell.setCellStyle(titleStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			// 三层
			cell = row.createCell(2);
			// 设置内容
			cell.setCellValue("事由");
			// 设置单元格格式
			cell.setCellStyle(titleStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			// 四层
			cell = row.createCell(3);
			// 设置内容
			cell.setCellValue("备注");
			// 设置单元格格式
			cell.setCellStyle(titleStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			// 五层
			cell = row.createCell(4);
			// 设置内容
			cell.setCellValue("时间");
			// 设置单元格格式
			cell.setCellStyle(titleStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			

			Integer x=4;
			for (CheckLog c : checks) {
				cell = row.createCell(0);
					// 设置内容
				cell.setCellValue(c.getContent());
					// 设置单元格格式
				cell.setCellStyle(dataStyle);
					// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(1);
				// 设置内容
			cell.setCellValue(c.getUser_name());
				// 设置单元格格式
			cell.setCellStyle(dataStyle);
				// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			cell = row.createCell(2);
			// 设置内容
		cell.setCellValue(c.getReason());
			// 设置单元格格式
		cell.setCellStyle(dataStyle);
			// 设置单元格内容类型
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		
		
		cell = row.createCell(3);
		// 设置内容
	cell.setCellValue(c.getRemark());
		// 设置单元格格式
	cell.setCellStyle(dataStyle);
		// 设置单元格内容类型
	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	
	cell = row.createCell(4);
	// 设置内容
	cell.setCellValue(DateFormatter.dateToString(c.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
	// 设置单元格格式
	cell.setCellStyle(dataStyle);
	// 设置单元格内容类型
	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				
				row = sheet1.createRow(x++);
				
			}

			

			os = response.getOutputStream();

			work.write(os);
			os.flush();
			os.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
}
