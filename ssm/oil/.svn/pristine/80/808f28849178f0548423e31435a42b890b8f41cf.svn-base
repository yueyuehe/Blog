package cn.hm.oil.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
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
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PerformanceMeasure;
import cn.hm.oil.domain.PerformanceMeasureDetail;
import cn.hm.oil.domain.PotentialMeasure;
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
 *         绝缘接头(法兰)性能测量记录查看
 * 
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BasePMeasureController {

	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;

	/**
	 * 绝缘接头(法兰)性能测量记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/p_measure/create", method = RequestMethod.GET)
	public String pMeasure_create(Model model,
			@RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud
				.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		Prompt prompt = baseService.quertPromptByType(3);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}

		return "pages/base/p_measure_create";
	}

	/**
	 * 绝缘接头(法兰)性能测量记录保存
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/p_measure/save", method = RequestMethod.POST)
	public String pMeasure_save(Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam Integer pl,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam Integer m_year,
			@RequestParam String created_by, @RequestParam String auditor,
			@RequestParam String[] position, @RequestParam String[] month_1,
			@RequestParam String[] month_2, @RequestParam String[] month_3,
			@RequestParam String[] month_4, @RequestParam String[] month_5,
			@RequestParam String[] month_6, @RequestParam String[] month_7,
			@RequestParam String[] month_8, @RequestParam String[] month_9,
			@RequestParam String[] month_10, @RequestParam String[] month_11,
			@RequestParam String[] month_12) {
		PerformanceMeasure pm = new PerformanceMeasure();
		pm.setId(id);
		pm.setAuditor(auditor);
		pm.setCreated_by(created_by);
		pm.setJinzhan(jinzhan);
		pm.setM_year(m_year);
		pm.setPl_id(pl);
		pm.setPl_section_id(section);
		pm.setPl_spec_id(spec);
		pm.setStatus(0);
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		pm.setCreater(ud.getId());
		
		List<PerformanceMeasureDetail> pmdList = new ArrayList<PerformanceMeasureDetail>();
		int i = 0;
		for (String m1 : month_1) {
			PerformanceMeasureDetail pmd = new PerformanceMeasureDetail();
				if (i == 0) {
					pmd.setType(1);
				} else if ((i % 2) == 1) {
					pmd.setType(2);
				} else {
					pmd.setType(3);
				}
				if (i > 0) {
					pmd.setPosition(position[(i - 1) / 2]);
				}
				pmd.setMonth_1(month_1[i]);
				pmd.setMonth_2(month_2[i]);
				pmd.setMonth_3(month_3[i]);
				pmd.setMonth_4(month_4[i]);
				pmd.setMonth_5(month_5[i]);
				pmd.setMonth_6(month_6[i]);
				pmd.setMonth_7(month_7[i]);
				pmd.setMonth_8(month_8[i]);
				pmd.setMonth_9(month_9[i]);
				pmd.setMonth_10(month_10[i]);
				pmd.setMonth_11(month_11[i]);
				pmd.setMonth_12(month_12[i]);

				pmdList.add(pmd);
			
			i++;
		}
		String status = "0";
		if (pmdList.size() > 0) {
			baseService.savePMeasure(pm, pmdList);
			status = "1";
		}
		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}
		
		return "redirect:/admin/base/p_measure/" + page;
	}

	/**
	 * 绝缘接头(法兰)性能测量记录查看
	 * 
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param m_year
	 * @return
	 */
	@RequestMapping(value = "/p_measure/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String pMeasure_list(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) Integer m_year,
			@RequestParam(required = false) Integer del) {
		

		Map<String, Object> param = new HashMap<String, Object>();

		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			List<BasePipelineSection> sectionList = baseService
					.querySection(pl);
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

		if (m_year != null && m_year.intValue() > 0) {
			param.put("m_year", m_year);
			model.addAttribute("m_year", m_year);
		}

		PageSupport ps = PageSupport.initPageSupport(request);
		List<PerformanceMeasure> pmList = baseService.queryPerformanceMeasure(
				param, ps);
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());

		model.addAttribute("role", role);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("pmList", pmList);
		return "pages/base/p_measure_list";
	}

	
	/**
	 * 审核记录
	 * 
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param m_year
	 * @return
	 */
	@RequestMapping(value = "/p_measure/verify", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String pMeasure_verify(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) Integer m_year) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());

		Map<String, Object> param = new HashMap<String, Object>();

		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			List<BasePipelineSection> sectionList = baseService
					.querySection(pl);
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

		if (m_year != null && m_year.intValue() > 0) {
			param.put("m_year", m_year);
			model.addAttribute("m_year", m_year);
		}

		PageSupport ps = PageSupport.initPageSupport(request);
		List<PerformanceMeasure> pmList = baseService.queryPerformanceMeasure(
				param, ps);

		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());

		model.addAttribute("role", role);

		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("pmList", pmList);
		model.addAttribute("verify", 1);
		return "pages/base/p_measure_list";
	}
	
	
	/**
	 * 绝缘接头(法兰)性能测量记录详细查看
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/p_measure/show", method = RequestMethod.GET)
	public String pMeasure_show(Model model, @RequestParam Integer id, @RequestParam(required=false) Integer tips_id) {
		if (tips_id != null && tips_id.intValue() > 0)
			baseService.deleteTips(tips_id);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		PerformanceMeasure pm = baseService.queryPerformanceMeasureById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(pm
				.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pm
				.getPl_section_id());

		model.addAttribute("pl", pm.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pm.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pm", pm);
		return "pages/base/p_measure_show";
	}

	
	/**
	 * 绝缘接头(法兰)性能测量审核页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/p_measure/show_verify", method = RequestMethod.GET)
	public String pMeasure_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		PerformanceMeasure pm = baseService.queryPerformanceMeasureById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(pm
				.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pm
				.getPl_section_id());

		model.addAttribute("pl", pm.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pm.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pm", pm);
		model.addAttribute("verify", 1);
		return "pages/base/p_measure_show";
	}
	
	
	@RequestMapping(value = "/p_measure/exp", method = RequestMethod.GET)
	public String pMeasure_export(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String m_year) {
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

		if (!StringUtils.isBlank(m_year)) {
			param.put("m_year", m_year);
		}

		List<PerformanceMeasure> pmList = baseService.queryPerformanceMeasure(
				param, null);
		if (pmList.size() == 0) {
			return "redirect:/admin/base/p_measure/list";
		}

		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称
		String path = fileDir;
		String excelPath = path + sep + "excel" + ".xls";
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		try {
			HSSFWorkbook work = new HSSFWorkbook();
			FileOutputStream fileOut = new FileOutputStream(path + sep
					+ "excel" + ".xls");

			HSSFSheet sheet = work.createSheet("sheet1");
			HSSFCell cell;

			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 左边抬头格式
			CellStyle titleStyle = work.createCellStyle();
			// 右边抬头格式
			CellStyle titleStyle1 = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();
			// 内容格式左对齐
			CellStyle dataStyle1 = work.createCellStyle();
			// 管线名称格式
			CellStyle dataStyle2 = work.createCellStyle();

			// 内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			// dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			// dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			// dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			// dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle1.setBorderBottom(CellStyle.BORDER_THIN);
			dataStyle1.setBorderLeft(CellStyle.BORDER_THIN);
			dataStyle1.setBorderRight(CellStyle.BORDER_THIN);
			dataStyle1.setBorderTop(CellStyle.BORDER_THIN);

			// 居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle2.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			// 左对齐
			titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle.setWrapText(true);

			// 右对齐
			titleStyle1.setAlignment(CellStyle.ALIGN_RIGHT);
			titleStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle1.setWrapText(true);

			// 内容居中
			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);

			// 内容左对齐
			dataStyle1.setAlignment(CellStyle.ALIGN_LEFT);
			dataStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle1.setWrapText(true);

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

			// 把字体加入到格式中
			cellStyle.setFont(font);
			titleStyle.setFont(titlefont);
			dataStyle.setFont(datafont);
			dataStyle1.setFont(datafont);
			titleStyle1.setFont(titlefont);
			dataStyle2.setFont(datafont);
			// 设置列宽度
			sheet.setColumnWidth(0, 10 * 256);
			sheet.setColumnWidth(1, 14 * 256);
			sheet.setColumnWidth(2, 9 * 256);
			sheet.setColumnWidth(3, 9 * 256);
			sheet.setColumnWidth(4, 9 * 256);
			sheet.setColumnWidth(5, 9 * 256);
			sheet.setColumnWidth(6, 9 * 256);
			sheet.setColumnWidth(7, 9 * 256);
			sheet.setColumnWidth(8, 9 * 256);
			sheet.setColumnWidth(9, 9 * 256);
			sheet.setColumnWidth(10, 9 * 256);
			sheet.setColumnWidth(11, 9 * 256);
			sheet.setColumnWidth(12, 9 * 256);
			sheet.setColumnWidth(13, 9 * 256);

			int row_index = 0;
			for (PerformanceMeasure pm : pmList) {
				// 合并单元格
				sheet.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 13));
				sheet.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 0, 2));
				sheet.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 11, 13));
				sheet.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 4, 8));
				sheet.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 0, 0));
				
				// 设置标题格
				HSSFRow row = sheet.createRow(row_index);
				row_index++;
				row.setHeightInPoints(30);
				cell = row.createCell(0);
				cell.setCellValue("绝   缘   接   头   (法   兰) 性   能   测   量   记   录");
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet.createRow(row_index);
				row_index++;
				row.setHeightInPoints(21);
				cell = row.createCell(0);
				cell.setCellValue("井(站)" + pm.getJinzhan());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(4);
				cell.setCellValue("管线名称及规格: " + pm.getPl_name() + "/"
						+ pm.getPl_spec_name());
				cell.setCellStyle(dataStyle2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(11);
				cell.setCellValue(pm.getM_year() + "年");
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);


				// 查询数据库
				PerformanceMeasure pfm = baseService
						.queryPerformanceMeasureById(pm.getId());
				
				int x = 13;
				
				if(pfm.getDetailList().size() > 13) {
					x = pfm.getDetailList().size();
				}
				
				int end_row = 0;
				for (int rown = 0; rown < x + 1; rown++) {
					row = sheet.createRow(rown+row_index);
					row.setHeightInPoints((float) 27);
					for (int celln = 0; celln < 14; celln++) {
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown+row_index;
				}

				row = sheet.getRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 28.5);
				cell = row.getCell(0);
				cell.setCellValue("位置");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(1);
				cell.setCellValue("类别");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("1月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				cell.setCellValue("2月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(4);
				cell.setCellValue("3月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(5);
				cell.setCellValue("4月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(6);
				cell.setCellValue("5月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(7);
				cell.setCellValue("6月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(8);
				cell.setCellValue("7月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(9);
				cell.setCellValue("8月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(10);
				cell.setCellValue("9月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(11);
				cell.setCellValue("10月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(12);
				cell.setCellValue("11月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(13);
				cell.setCellValue("12月");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				//条数大于13时需要手动合并单元格
				if(pfm.getDetailList().size() > 13) {
					int i = 0;
					while(i < pfm.getDetailList().size()/2) {
						sheet.addMergedRegion(new CellRangeAddress(row_index+1+(2*i), row_index+1+(2*i)+1, 0, 0));
						 i++;
					}
					sheet.addMergedRegion(new CellRangeAddress(row_index+1+(2*i), row_index+1+(2*i), 0, 6));
					sheet.addMergedRegion(new CellRangeAddress(row_index+1+(2*i), row_index+1+(2*i), 7, 13));
					
					row = sheet.createRow(row_index+1+(2*i));
					row.setHeightInPoints(21);
					cell = row.createCell(0);
					cell.setCellValue("填报人：" + pm.getCreated_by());
					cell.setCellStyle(titleStyle);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.createCell(7);
					cell.setCellValue("审核人：" + pm.getAuditor());
					cell.setCellStyle(titleStyle1);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				} else {
					sheet.addMergedRegion(new CellRangeAddress(row_index+1, row_index+2, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+3, row_index+4, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+5, row_index+6, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+7, row_index+8, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+9, row_index+10, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+11, row_index+12, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+13, row_index+13, 0, 6));
					sheet.addMergedRegion(new CellRangeAddress(row_index+13, row_index+13, 7, 13));
					
					row = sheet.createRow(row_index+13);
					row.setHeightInPoints(21);
					cell = row.createCell(0);
					cell.setCellValue("填报人：" + pm.getCreated_by());
					cell.setCellStyle(titleStyle);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.createCell(7);
					cell.setCellValue("审核人：" + pm.getAuditor());
					cell.setCellStyle(titleStyle1);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}

				int count = 0;
				//int index = 3;
				for (PerformanceMeasureDetail pmd : pfm.getDetailList()) {
					row = sheet.getRow(row_index++);
					if (pmd.getType() == 1 || pmd.getType() == 3) {
						cell = row.getCell(1);
						if (pmd.getType() == 1) {
							cell.setCellValue("通电点电位(-V)");
							cell.setCellStyle(dataStyle1);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						} else {
							cell.setCellValue("末保护端(-V)");
							cell.setCellStyle(dataStyle1);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}
					} else {
						cell = row.getCell(0);
						cell.setCellValue(pmd.getPosition());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell = row.getCell(1);
						cell.setCellValue("保护端(-V)");
						cell.setCellStyle(dataStyle1);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					cell = row.getCell(2);
					cell.setCellValue(pmd.getMonth_1()==null?"":pmd.getMonth_1()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(3);
					cell.setCellValue(pmd.getMonth_2()==null?"":pmd.getMonth_2()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(4);
					cell.setCellValue(pmd.getMonth_3()==null?"":pmd.getMonth_3()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(5);
					cell.setCellValue(pmd.getMonth_4()==null?"":pmd.getMonth_4()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(6);
					cell.setCellValue(pmd.getMonth_5()==null?"":pmd.getMonth_5()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(7);
					cell.setCellValue(pmd.getMonth_6()==null?"":pmd.getMonth_6()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(8);
					cell.setCellValue(pmd.getMonth_7()==null?"":pmd.getMonth_7()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(9);
					cell.setCellValue(pmd.getMonth_8()==null?"":pmd.getMonth_8()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(10);
					cell.setCellValue(pmd.getMonth_9()==null?"":pmd.getMonth_9()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(11);
					cell.setCellValue(pmd.getMonth_10()==null?"":pmd.getMonth_10()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(12);
					cell.setCellValue(pmd.getMonth_11()==null?"":pmd.getMonth_11()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(13);
					cell.setCellValue(pmd.getMonth_12()==null?"":pmd.getMonth_12()+"");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				row_index = end_row + 6;
			}
			// 将创建好的excel存到指定文件夹下
			work.write(fileOut);
			fileOut.close();
			FileUtils.createZip(response, excelPath, DateFormatter.dateToString(new Date(), "yyyy-MM-dd_HH:mm:ss:SSS"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/p_measure/verify_save", method = RequestMethod.POST)
	public @ResponseBody String pMeasure_verify_save(Model model,
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		baseService.updatePerformanceMeasureVerifyStatus(id, ud.getId(), status,
				verify_desc);

		return JsonUtil.toJson("OK");
	}
	
	/**
	 * 绝缘接头编辑
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/p_measure/edit", method = RequestMethod.GET)
	public String plMeasure_edit(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		PerformanceMeasure pm = baseService.queryPerformanceMeasureById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(pm
				.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pm
				.getPl_section_id());

		model.addAttribute("pl", pm.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pm.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pm", pm);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		return "pages/base/p_measure_edit";
	}
	
	@RequestMapping(value = "/p_measure/del", method = RequestMethod.GET)
	public String pl_curve_del(Model model, @RequestParam Integer id) {
		baseService.deletePerformanceMeasureById(id);
		return "redirect:/admin/base/p_measure/list?del=1";
	}
	
}
