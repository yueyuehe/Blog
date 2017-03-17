package cn.hm.oil.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.FacilitiesMaintenance;
import cn.hm.oil.domain.FacilitiesMaintenanceDetail;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;


/**
 * @author Administrator
 * 
 * 集输气管线附属设施维护记录_管理
 * 
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BaseFMaintController {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 集输气管线附属设施维护记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/f_maint/create", method = RequestMethod.GET)
	public String plMeasure_create(Model model, @RequestParam(required=false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		Prompt prompt = baseService.quertPromptByType(6);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		
		return "pages/base/f_maint_create";
	}
	
	/**
	 * 集输气管线附属设施维护记录保存
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/f_maint/save", method = RequestMethod.POST)
	public String plMeasure_save(Model model, @RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam String create_date,@RequestParam String[] categoryT, @RequestParam String [] category, @RequestParam String [] lcz_no,
			@RequestParam String [] address, @RequestParam String [] jgxs, @RequestParam String [] ssxz_desc, @RequestParam String [] m_time, 
			@RequestParam String [] description, @RequestParam String [] recorder, @RequestParam Integer [] no, @RequestParam Double [] latitude, 
			@RequestParam Double [] longitude, @RequestParam(required = false) Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		FacilitiesMaintenance fm = new FacilitiesMaintenance();
		fm.setPl_id(pl);
		fm.setPl_section_id(section);
		fm.setPl_spec_id(spec);
		fm.setJinzhan(jinzhan);
		fm.setCreate_date(DateFormatter.stringToDate(create_date, "yyyy-MM-dd"));
		fm.setCreater(ud.getId());
		fm.setId(id);
		
		List<FacilitiesMaintenanceDetail> fmdList = new ArrayList<FacilitiesMaintenanceDetail>();
		int i = 0;
		for (String l : lcz_no) {
			FacilitiesMaintenanceDetail fmd = new FacilitiesMaintenanceDetail();
			if (!StringUtils.isBlank(l)) {
				fmd.setAddress(address[i]);
				fmd.setCategory(categoryT[i].trim() + " : " + category[i].trim());
				fmd.setDescription(description[i]);
				fmd.setJgxs(jgxs[i]);
				fmd.setLcz_no(lcz_no[i]);
				fmd.setM_time(m_time[i]);
				fmd.setNo(no[i]);
				fmd.setRecorder(recorder[i]);
				fmd.setSsxz_desc(ssxz_desc[i]);
				fmd.setLatitude(latitude[i]);
				fmd.setLongitude(longitude[i]);
				
				fmdList.add(fmd);
			}
			i++;
		}
		
		String status = "0";
		if (fmdList.size() > 0) {
			baseService.saveFMaint(fm, fmdList);
			status = "1";
		}
		
		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}
		return "redirect:/admin/base/f_maint/" + page;
	}
	
	/**
	 * 集输气管线附属设施维护记录查看
	 * 
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param create_date
	 * @return
	 */
	@RequestMapping(value = "/f_maint/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String plMeasure_list(Model model, HttpServletRequest request, Authentication authentication,@RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String create_date,
			@RequestParam(required = false) Integer del) {
	
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);
			
			List<BasePipelineSection> sectionList = baseService.querySection(pl);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);
			
			List<BasePipelineSpec> specList = baseService.querySpec(section);
			model.addAttribute("specList", specList);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}
		
		if (!StringUtils.isBlank(create_date)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(create_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			param.put("create_date", date);
			model.addAttribute("create_date", create_date);
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		List<FacilitiesMaintenance> fmList = baseService.queryFacilitiesMaintenance(param, ps);
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("fmList", fmList);
		return "pages/base/f_maint_list";
	}
	
	/**
	 * 集输气管线附属设施维护记录详细
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/f_maint/show", method = RequestMethod.GET)
	public String plPatrol_show(Model model, @RequestParam Integer id,@RequestParam(required=false) Integer tips_id) {
		if (tips_id != null && tips_id.intValue() > 0)
			baseService.deleteTips(tips_id);
		FacilitiesMaintenance fm = baseService.queryFacilitiesMaintenanceById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(fm.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(fm.getPl_section_id());
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		model.addAttribute("pl", fm.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", fm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", fm.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("fm", fm);
		return "pages/base/f_maint_show";
	}
	
	/**
	 * 集输气管线附属设施维护记录导出
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/f_maint/exp", method = RequestMethod.GET)
	public String plMeasure_export(Model model, 
			 HttpServletRequest request, 
			 HttpServletResponse response,
			 @RequestParam(required=false) Integer pl,
			 @RequestParam(required=false) Integer section, 
			 @RequestParam(required=false) Integer spec,
			 @RequestParam(required=false) String create_date) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
		}
		
		if (!StringUtils.isBlank(create_date)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(create_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			param.put("create_date", date);
			
		}
		
		List<FacilitiesMaintenance> fmList = baseService.queryFacilitiesMaintenance(param, null);
		if(fmList.size()==0){
			return "redirect:/admin/base/f_maint/list";
		}
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称
		String path=fileDir;
		String excelPath = path + sep + "excel" + ".xls";
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		try{
			
			HSSFWorkbook work = new HSSFWorkbook();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			FileOutputStream fileOut = new FileOutputStream(path + sep
					+ "excel" + ".xls");

			/*fm.getPl_name() + "_" + fm.getPl_section_name()
			+ df.format(fm.getCreate_date())*/
			
			HSSFSheet sheet1 = work.createSheet("sheet1");
			HSSFCell cell;
			
			//设置列宽度
			sheet1.setColumnWidth(0, 4*256);
			sheet1.setColumnWidth(1, 4*256);
			sheet1.setColumnWidth(2, 4*256);
			sheet1.setColumnWidth(3, 22*256);
			sheet1.setColumnWidth(4, 9*256);
			sheet1.setColumnWidth(5, 9*256);
			sheet1.setColumnWidth(6, 10*256);
			sheet1.setColumnWidth(7, 24*256);
			sheet1.setColumnWidth(8, 10*256);
			sheet1.setColumnWidth(9, 22*256);
			sheet1.setColumnWidth(10, 9*256);
			
			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 表头格式
			CellStyle titleStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();
			
			//内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			//dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			//dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			//dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			//dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

			
			//居中
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

			font.setFontHeightInPoints((short) 20);
			font.setFontName("方正仿宋简体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			titlefont.setFontHeightInPoints((short) 10);
			titlefont.setFontName("方正仿宋简体");
			titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");
			datafont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			//datafont.setColor(HSSFColor.RED.index);

			//把字体加入到格式中
			cellStyle.setFont(font);
			titleStyle.setFont(titlefont);
			dataStyle.setFont(datafont);
			
			int row_index = 0;
			for(FacilitiesMaintenance  fm:fmList){
				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 10));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 0, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 3, 7));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 8, 10));
				
				// 第一行
				HSSFRow row = sheet1.createRow(row_index);
				row_index++;
				//新建单元格
				cell = row.createCell(0);
				//设置内容
				cell.setCellValue("集输气管线附属设施维护记录");
				//设置单元格格式
				cell.setCellStyle(cellStyle);
				//设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				

				//第二行
				row = sheet1.createRow(row_index);
				row_index++;
				//设置行高度
				row.setHeightInPoints(24);
				cell = row.createCell(0);
				cell.setCellValue("井(站) " + fm.getJinzhan());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				

				cell = row.createCell(3);
				cell.setCellValue("管线名称及规格: " + fm.getPl_name() + "/"
						+ fm.getPl_spec_name());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				

				cell = row.createCell(8);
				Calendar ca = Calendar.getInstance();
				ca.setTime(fm.getCreate_date());
				cell.setCellValue("建立时间 "+ca.get(Calendar.YEAR)+ " 年 " +(ca.get(Calendar.MONTH)+1) 
						+ " 月 "+ca.get(Calendar.DAY_OF_MONTH));
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				FacilitiesMaintenance fml = baseService.queryFacilitiesMaintenanceById(fm.getId());
				
				Integer sz = 10;
				if(fml.getDetailList().size() > 10)sz = fml.getDetailList().size();
				
				//添加边框线
				int end_row = 0;
				for(int rown=0 ; rown <= sz ; rown++){
					row = sheet1.createRow(rown+row_index);
					//row.setHeightInPoints(40);
					for(int celln = 0 ; celln < 11 ; celln++){
						cell=row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown+row_index;
				}
				
				//第三行
				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(36);
				cell = row.getCell(0);
				cell.setCellValue("编号");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(1);
				cell.setCellValue("类别");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(2);
				cell.setCellValue("检查头里程编号");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(3);
				cell.setCellValue("所处地址");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(4);
				cell.setCellValue("经度");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(5);
				cell.setCellValue("维度");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(6);
				cell.setCellValue("结构形式");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(7);
				cell.setCellValue("设施现状描述");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(8);
				cell.setCellValue("维修时间");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(9);
				cell.setCellValue("维修情况");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(10);
				cell.setCellValue("填报人");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				//int index = 3;
				for(FacilitiesMaintenanceDetail  fmd:fml.getDetailList()){
					row = sheet1.getRow(row_index);
					row_index++;
					//row.setHeightInPoints(36);
					cell = row.getCell(0);
					if(fmd.getNo() != null){
					cell.setCellValue(fmd.getNo());
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}
					
					cell = row.getCell(1);
					if(fmd.getCategory() != null){
					cell.setCellValue(fmd.getCategory());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					cell = row.getCell(2);
					if(fmd.getLcz_no()!=null){
					cell.setCellValue(fmd.getLcz_no());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					cell = row.getCell(3);
					if(fmd.getAddress()!=null){
					cell.setCellValue(fmd.getAddress());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					cell = row.getCell(4);
					if(fmd.getLongitude()!=null) {
						cell.setCellValue(fmd.getLongitude());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					cell = row.getCell(5);
					if(fmd.getLongitude()!=null) {
						cell.setCellValue(fmd.getLatitude());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					cell = row.getCell(6);
					if(fmd.getJgxs()!=null){
					cell.setCellValue(fmd.getJgxs());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					cell = row.getCell(7);
					if(fmd.getSsxz_desc()!=null){
					cell.setCellValue(fmd.getSsxz_desc());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					cell = row.getCell(8);
					if(fmd.getM_time()!=null){
					cell.setCellValue(fmd.getM_time());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					cell = row.getCell(9);
					if(fmd.getDescription()!=null){
					cell.setCellValue(fmd.getDescription());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					cell = row.getCell(10);
					if(fmd.getRecorder()!=null){
					cell.setCellValue(fmd.getRecorder());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
				}
				row_index = end_row + 3;
			}

		//将创建好的excel存到指定文件夹下
		work.write(fileOut);
		fileOut.close();
		//压缩文件夹并下载，下载后删除文件夹
		FileUtils.createZip(response, excelPath, DateFormatter.dateToString(new Date(), "yyyy-MM-dd_HH:mm:ss:SSS"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 审核列表
	 * @param model
	 * @param request
	 * @param authentication
	 * @param pl
	 * @param section
	 * @param spec
	 * @param create_date
	 * @return
	 */
	@RequestMapping(value = "/f_maint/verify", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String fMaint_verify(Model model, HttpServletRequest request, Authentication authentication,@RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String create_date) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);
			
			List<BasePipelineSection> sectionList = baseService.querySection(pl);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);
			
			List<BasePipelineSpec> specList = baseService.querySpec(section);
			model.addAttribute("specList", specList);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}
		
		if (!StringUtils.isBlank(create_date)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(create_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			param.put("create_date", date);
			model.addAttribute("create_date", create_date);
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		List<FacilitiesMaintenance> fmList = baseService.queryFacilitiesMaintenance(param, ps);
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("fmList", fmList);
		model.addAttribute("verify", 1);
		return "pages/base/f_maint_list";
	}
	
	/**
	 * 审核查看
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/f_maint/show_verify", method = RequestMethod.GET)
	public String fMaint_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		FacilitiesMaintenance fm = baseService.queryFacilitiesMaintenanceById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(fm.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(fm.getPl_section_id());
		
		model.addAttribute("pl", fm.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", fm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", fm.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("fm", fm);
		model.addAttribute("verify", 1);
		return "pages/base/f_maint_show";
	}
	
	/**
	 * 审核结果保存  
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/f_maint/verify_save", method = RequestMethod.POST)
	public @ResponseBody String fMaint_verify_save(Model model,
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		baseService.updateFacilitiesMaintenanceVerifyStatus(id, ud.getId(), status, verify_desc);
		
		return JsonUtil.toJson("OK");
	}
	
	@RequestMapping(value = "/f_maint/edit", method = RequestMethod.GET)
	public String fMaint_edit(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		FacilitiesMaintenance fm = baseService.queryFacilitiesMaintenanceById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(fm.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(fm.getPl_section_id());
		
		model.addAttribute("pl", fm.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", fm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", fm.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("fm", fm);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		return "pages/base/f_maint_edit";
	}
	
	@RequestMapping(value = "/f_maint/del", method = RequestMethod.GET)
	public String rc_del(Model model, @RequestParam Integer id) {
		baseService.deleteFacilitiesMaintenanceById(id);
		return "redirect:/admin/base/f_maint/list?del=1";
	}
}
