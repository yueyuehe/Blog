package cn.hm.oil.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.DynamicConsequence;
import cn.hm.oil.domain.DynamicConsequenceDetail;
import cn.hm.oil.domain.DynamicConsequenceDetailAttachement;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

/**
 * 高后果区资料管理（动态）
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BaseDSequelController {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 动态高后果区资料创建
	 * @param model
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/d_sequel/create")
	public String DSequelCreate(Model model, @RequestParam(required=false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		} else if (StringUtils.equals(status, "2")) {
			model.addAttribute("msg", "无可用信息,请重新填写!");
		}
		Prompt prompt = baseService.quertPromptByType(13);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		return "pages/base/d_sequel_create";
	}
	
	@RequestMapping(value="/d_sequel/save", method=RequestMethod.POST)
	public String DSequelSave(Model model, @RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec, @RequestParam(required = false) Integer id,
			@RequestParam String[] unit_name,@RequestParam String[] pipeline_name, @RequestParam String[] section_name, @RequestParam Integer[] num, @RequestParam Integer[] s_start,
			@RequestParam Integer[] s_end, @RequestParam Float[] s_length, @RequestParam String[] place_name, @RequestParam String[] description, @RequestParam String[] u_date,
			@RequestParam String[] recogner, @RequestParam String[] recogner_tel, @RequestParam String[] remark, @RequestParam String[] annex_file, HttpServletRequest request) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		DynamicConsequence dc = new DynamicConsequence();
		dc.setPl_id(pl);
		dc.setPl_section_id(section);
		dc.setPl_spec_id(spec);
		dc.setCreater(ud.getId());
		dc.setId(id);
		
		/*List<String> annex_fileList = null;
		try {
			annex_fileList = DataUtil.uploadFileList(request, "annex_file");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		List<DynamicConsequenceDetail> dcdList = new ArrayList<DynamicConsequenceDetail>();
		
		int i = 0;
		for(String u : unit_name) {
			if(StringUtils.isBlank(u) && StringUtils.isBlank(pipeline_name[i]) && StringUtils.isBlank(section_name[i])) {
				break;
			}
			DynamicConsequenceDetail dcd = new DynamicConsequenceDetail();
			dcd.setUnit_name(u);
			dcd.setNum(num[i]);
			dcd.setPipeline_name(pipeline_name[i]);
			dcd.setPlace_name(place_name[i]);
			dcd.setRecogner(recogner[i]);
			//System.out.println(recogner_tel[i]);
			dcd.setRecogner_tel(recogner_tel[i]);
			dcd.setRemark(remark[i]);
			dcd.setS_end(s_end[i]);
			dcd.setS_start(s_start[i]);
			dcd.setS_length(s_length[i]);
			dcd.setSection_name(section_name[i]);
			dcd.setU_date(DateFormatter.stringToDate(u_date[i], "yyyy-MM-dd"));
			dcd.setDescription(description[i]);
			
			dcd.setAnnex_file(annex_file[i]);
			
			dcdList.add(dcd);
			i++;
		}
		
		baseService.saveDynamicConseq(dc, dcdList);
		
		return "redirect:/admin/base/d_sequel/create?status=1";
	}
	
	@RequestMapping(value="/d_sequel/list")
	public String DSequelList(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required=false) Integer pl,@RequestParam(required=false) Integer section, 
			@RequestParam(required=false) Integer spec,@RequestParam(required=false) String create_time) {
		
		
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
		if (!StringUtils.isBlank(create_time)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(create_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			param.put("create_time", date);
			model.addAttribute("create_time", create_time);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<DynamicConsequence> dcList = baseService.queryDynamicConsequence(param, ps);
		//System.out.println(hcList.size());
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		
		model.addAttribute("dcList", dcList);
		
		return "pages/base/d_sequel_list";
	}
	
	/**
	 * 动态高后果区资料审核
	 * @param model
	 * @param request
	 * @param authentication
	 * @param pl
	 * @param section
	 * @param spec
	 * @param create_time
	 * @return
	 */
	@RequestMapping(value="/d_sequel/verify")
	public String DSequelVerify(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required=false) Integer pl,@RequestParam(required=false) Integer section, 
			@RequestParam(required=false) Integer spec,@RequestParam(required=false) String create_time) {
		
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
		if (!StringUtils.isBlank(create_time)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(create_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			param.put("create_time", date);
			model.addAttribute("create_time", create_time);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<DynamicConsequence> dcList = baseService.queryDynamicConsequence(param, ps);
		//System.out.println(hcList.size());
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);

		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("verify", 1);
		
		model.addAttribute("dcList", dcList);
		return "pages/base/d_sequel_list";
	}
	
	/**
	 * 审核页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/d_sequel/show_verify")
	public String DSequelShowVerify(Model model, @RequestParam Integer id) {

		DynamicConsequence dc = baseService.queryDynamicById(id);
		//System.out.println(dc.getPl_id());
		List<BasePipelineSection> sectionList = baseService.querySection(dc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(dc.getPl_section_id());
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		model.addAttribute("dc", dc);
		model.addAttribute("pl", dc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", dc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", dc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("verify", 1);
		return "pages/base/d_sequel_detail";
	}
	
	/**
	 * 页面显示
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/d_sequel/show")
	public String DSequelShow(Model model, @RequestParam Integer id, @RequestParam(required = false) Integer tips_id) {
		if (tips_id != null && tips_id.intValue() > 0)
			baseService.deleteTips(tips_id);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine( new HashMap<String,Object>());
		DynamicConsequence dc = baseService.queryDynamicById(id);
		//System.out.println(dc.getPl_id());
		List<BasePipelineSection> sectionList = baseService.querySection(dc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(dc.getPl_section_id());
		
		model.addAttribute("dc", dc);
		model.addAttribute("pl", dc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", dc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", dc.getPl_spec_id());
		model.addAttribute("specList", specList);
		return "pages/base/d_sequel_detail";
	}
	
	/**
	 * 页面显示
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/d_sequel/add_attache")
	public String add_attache(Model model, @RequestParam Integer id, @RequestParam(required = false) Integer status) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		DynamicConsequence dc = baseService.queryDynamicById(id);
		//System.out.println(dc.getPl_id());
		List<BasePipelineSection> sectionList = baseService.querySection(dc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(dc.getPl_section_id());
		
		model.addAttribute("dc", dc);
		model.addAttribute("pl", dc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", dc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", dc.getPl_spec_id());
		model.addAttribute("specList", specList);
		if(status!=null && status ==1){
			model.addAttribute("msg", "保存成功!");
		}
		return "pages/base/d_sequel_detail_add_attache";
	}
	
	/**
	 * 保存附件
	 * @param model
	 * @param id
	 * @param annex_file
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/d_sequel/add_attache/save")
	public String add_attache_save(Model model,
			@RequestParam(required = false) Integer id, 
			@RequestParam String[] annex_file, 
			HttpServletRequest request) {
		DynamicConsequence dc = baseService.queryDynamicById(id);
		if(!CollectionUtils.isEmpty(dc.getDetailList())){
			Integer i = 0;
			for(DynamicConsequenceDetail dcd : dc.getDetailList()){
				dcd.setAnnex_file(annex_file[i]);
				i++;
			}
			baseService.updateHighConsequenceAttachement(dc);
		}
		return "redirect:/admin/base/d_sequel/add_attache?status=1&id="+id;
	}
	
	/**
	 * 保存审核结果
	 * @param model
	 * @param id
	 * @param status
	 * @param verify_desc
	 * @return
	 */
	@RequestMapping(value="/d_sequel/verify_save")
	public @ResponseBody String DSequelVerifySave(Model model, @RequestParam Integer id, @RequestParam Integer status, @RequestParam(required = false) String verify_desc) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DynamicConsequence dc = baseService.queryDynamicById(id);
		dc.setStatus(status);
		dc.setId(id);
		dc.setVerify_desc(verify_desc);
		dc.setVerifier(ud.getId());
		Integer verify_month = baseService.queryVerifyMonthByDcId(id);
		dc.setVerify_month(verify_month);
		baseService.saveDynamicVerify(dc);
		return "Ok";
	}
	
	/**
	 * 动态高后果区修改
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/d_sequel/edit")
	public String DSequelEdit(Model model, @RequestParam Integer id) {
		DynamicConsequence dc = baseService.queryDynamicById(id);
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		
		List<BasePipelineSection> sectionList = baseService.querySection(dc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(dc.getPl_section_id());
		
		model.addAttribute("pl", dc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", dc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", dc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("dc", dc);
		
		return "pages/base/d_sequel_edit";
	}
	
	/**
	 * 动态高后果区资料导出
	 * @param model
	 * @param request
	 * @param authentication
	 * @param pl
	 * @param section
	 * @param spec
	 * @param create_time
	 * @return
	 */
	@RequestMapping(value="/d_sequel/exp")
	public @ResponseBody String DSequelExp(Model model, Authentication authentication,HttpServletResponse response,
			@RequestParam(required=false) Integer pl,@RequestParam(required=false) Integer section, 
			@RequestParam(required=false) Integer spec,@RequestParam(required=false) String create_time) {
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
		if (!StringUtils.isBlank(create_time)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(create_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("------------------"+date);
			param.put("create_time", date);
			model.addAttribute("create_time", create_time);
		}
		List<DynamicConsequence> dcList = baseService.queryDynamicConsequence(param, null);
		
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.path");
		String path = fileDir;
		String excelPath = path + sep + "excel" + ".xls";
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		try {
			HSSFWorkbook work = new HSSFWorkbook();
			Random random = new Random();
			//String date = DateFormatter.dateToString(dc.getCreate_time(), "yyyy-MM-dd");
			//String excelPath = path;
			dirPath = new File(path);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			FileOutputStream fileOut = new FileOutputStream(path + sep +"excel.xls");

			HSSFSheet sheet = work.createSheet("sheet1");
			HSSFCell cell;

			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();

			// 内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			// dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			// dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			// dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			// dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

			// 标题居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			// 内容居中
			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);

			// 标题字体
			Font font = work.createFont();
			// 内容字体
			Font datafont = work.createFont();

			font.setFontHeightInPoints((short) 20);
			font.setFontName("方正仿宋简体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");
			//datafont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			// 把字体加入到格式中
			cellStyle.setFont(font);
			dataStyle.setFont(datafont);
			// 设置列宽度
			sheet.setColumnWidth(0, 11 * 256);
			sheet.setColumnWidth(1, 10 * 256);
			sheet.setColumnWidth(2, 10 * 256);
			sheet.setColumnWidth(3, 10 * 256);
			sheet.setColumnWidth(4, 10 * 256);
			sheet.setColumnWidth(5, 10 * 256);
			sheet.setColumnWidth(6, 10 * 256);
			sheet.setColumnWidth(7, 10 * 256);
			sheet.setColumnWidth(8, 12 * 256);
			sheet.setColumnWidth(9, 10 * 256);
			sheet.setColumnWidth(10, 10 * 256);
			sheet.setColumnWidth(11, 17 * 256);
			sheet.setColumnWidth(12, 10 * 256);
			sheet.setColumnWidth(13, 10 * 256);

			int row_index = 0;
			for (DynamicConsequence dc : dcList) {
				// 合并单元格
				sheet.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 13));
				
				// 设置标题格
				HSSFRow row = sheet.createRow(row_index);
				row_index++;
				row.setHeightInPoints(30);
				cell = row.createCell(0);
				cell.setCellValue("成都作业区管道高后果区管理明细表");
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet.createRow(row_index);
				row_index++;
				row.setHeightInPoints(24);
				cell = row.createCell(0);
				cell.setCellValue("管理单位");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(1);
				cell.setCellValue("管线名称");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(2);
				cell.setCellValue("管段名称");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(3);
				cell.setCellValue("高后果区编号");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(4);
				cell.setCellValue("高后果区起点（m）");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(5);
				cell.setCellValue("高后果区终点（m）");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(6);
				cell.setCellValue("高后果区长度（m）");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(7);
				cell.setCellValue("地名");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(8);
				cell.setCellValue("高后果区特征描述");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(9);
				cell.setCellValue("管段名称");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(10);
				cell.setCellValue("识别或更新时间");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(11);
				cell.setCellValue("管理人员");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(12);
				cell.setCellValue("管理人员联系电话");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(13);
				cell.setCellValue("附件");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				int x = 12;
				
				DynamicConsequence qdc = baseService.queryDynamicById(dc.getId());
				
				if(qdc.getDetailList().size() > 12) {
					x = qdc.getDetailList().size();
				}
				
				int end_row = 0;
				for (int rown = 0; rown < x + 1; rown++) {
					row = sheet.createRow(rown + row_index);
					row.setHeightInPoints((float) 30);
					for (int celln = 0; celln < 14; celln++) {
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown+row_index;
				}

				int count = 0;
				//int index = 2;
				int in = 1;
				for (DynamicConsequenceDetail dcd : qdc.getDetailList()) {
					row = sheet.getRow(row_index++);
					cell = row.getCell(0);
					cell.setCellValue(dcd.getUnit_name());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.getCell(1);
					cell.setCellValue(dcd.getPipeline_name());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.getCell(2);
					cell.setCellValue(dcd.getSection_name());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.getCell(3);
					cell.setCellValue(dcd.getNum()==null?0:dcd.getNum());
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					
					cell = row.getCell(4);
					cell.setCellValue(dcd.getS_start()==null?0:dcd.getS_start());
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					
					cell = row.getCell(5);
					cell.setCellValue(dcd.getS_end()==null?0:dcd.getS_end());
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					
					cell = row.getCell(6);
					cell.setCellValue(dcd.getS_length()==null?0:dcd.getS_length());
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					
					cell = row.getCell(7);
					cell.setCellValue(dcd.getPlace_name());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.getCell(8);
					cell.setCellValue(dcd.getDescription());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.getCell(9);
					cell.setCellValue(DateFormatter.dateToString(dcd.getU_date()==null? new Date() : dcd.getU_date(), "yyyy/MM/dd"));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.getCell(10);
					cell.setCellValue(dcd.getRecogner());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.getCell(11);
					cell.setCellValue(dcd.getRecogner_tel());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.getCell(12);
					cell.setCellValue(dcd.getRemark());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					if(!CollectionUtils.isEmpty(dcd.getAttachementList())) {
						String newPath = path + sep + "AnnexFile"+sep+qdc.getId().toString()+sep+in;
						File newFile = new File(newPath);
						if (!newFile.exists()) {
							newFile.mkdirs();
						}
						Integer ii = 1;
						for(DynamicConsequenceDetailAttachement dcda:dcd.getAttachementList()){
							if(!StringUtils.isBlank(dcda.getPath())){
								String[] paths = StringUtils.split(dcda.getPath(), sep);
								//System.out.println(paths.length);
						
								int pathIndex = paths.length - 1;
							
								//FileOutputStream newFileOut = new FileOutputStream(newFile);
								//System.out.println(newPath+"------------");
								String[] pa = paths[pathIndex].split("\\.");
								System.out.println(ii+"-------");
								FileUtils.copyFile(fileDir + sep + dcda.getPath(),newPath + sep + ii + "月" + "." + pa[1]);
								//System.out.println();
						}
						ii++;
						}
						cell = row.getCell(13);
						cell.setCellValue("文件夹"+qdc.getId().toString()+"/"+in);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					in++;
				}
				row_index = end_row + 4;
			}

			// 将创建好的excel存到指定文件夹下
			work.write(fileOut);
			fileOut.close();
			FileUtils.createZip(response, excelPath,path + sep + "AnnexFile", DateFormatter.dateToString(new Date(), "yyyy-MM-dd_HH:mm:ss:SSS"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "OK";
	}
	
	//附件界面
	@RequestMapping(value = "/d_sequel/attachement/{id}", method = RequestMethod.GET)
	public String mapMark(HttpServletRequest request, Model model,
			@PathVariable String id) {
		String[] ids = id.split("!");
		if (Integer.valueOf(ids[1]) > 0) {
			List<DynamicConsequenceDetailAttachement> dcds = baseService.queryDynamicAttachementByDetailId(Integer.valueOf(ids[1]));
			model.addAttribute("dcds", dcds);
		}
		model.addAttribute("ids", ids[0]);
		return "pages/attachement";
	}
	
	//附件界面
		@RequestMapping(value = "/d_sequel/attachement_d/{id}", method = RequestMethod.GET)
		public String mapMark_d(HttpServletRequest request, Model model,
				@PathVariable String id) {
			System.out.println(id);
			String[] ids = id.split("!");
			if (Integer.valueOf(ids[1]) > 0) {
				List<DynamicConsequenceDetailAttachement> dcds = baseService.queryDynamicAttachementByDetailId(Integer.valueOf(ids[1]));
				model.addAttribute("dcds", dcds);
				if(ids.length>2){
					DynamicConsequence dc = baseService.queryDynamicById(dcds.get(0).getDc_id());
					if(dc.getVerify_month()!=null){
						model.addAttribute("vm", dc.getVerify_month());
					}
				}
			}
			model.addAttribute("ids", ids[0]);
			return "pages/attachement_d";
		}
}
