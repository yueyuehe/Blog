/**
 * 
 */
package cn.hm.oil.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import cn.hm.oil.domain.LoginUserInfo;
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
 *         管道保护电位测量_管理
 * 
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping(value = "/admin/base")
public class BasePlMeasureController {

	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;

	/**
	 * 管道保护电位测量记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/create", method = RequestMethod.GET)
	public String plMeasure_create(Model model,
			@RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud
				.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		Prompt prompt = baseService.quertPromptByType(1);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		return "pages/base/pl_measure_create";
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/save", method = RequestMethod.POST)
	public String plMeasure_save(Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam Integer pl, @RequestParam Integer section,
			@RequestParam Integer spec, @RequestParam String jinzhan,
			@RequestParam String c_month, @RequestParam String unit,
			@RequestParam String save_date, @RequestParam String[] no,
			@RequestParam String[] m_date, @RequestParam Float[] potential,
			@RequestParam Float[] a, @RequestParam Float[] v,
			@RequestParam Float[] tongdian, @RequestParam String[] measurer,
			@RequestParam String[] remark) {

		PotentialMeasure pm = new PotentialMeasure();
		pm.setId(id);
		pm.setPl_id(pl);
		pm.setPl_section_id(section);
		pm.setPl_spec_id(spec);
		pm.setJinzhan(jinzhan);
		pm.setC_month(Integer.valueOf(c_month.replace("-", "")));
		pm.setUnit(unit);
		pm.setSave_date(save_date);
		pm.setStatus(0);
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		pm.setCreater(ud.getId());
		List<PotentialMeasureDetail> pmdList = new ArrayList<PotentialMeasureDetail>();
		int i = 0;
		for (String n : no) {
			PotentialMeasureDetail pmd = new PotentialMeasureDetail();
			if (!StringUtils.isBlank(n)) {
				pmd.setNo(n);
				pmd.setM_date(DateFormatter.stringToDate(m_date[i],
						"yyyy-MM-dd"));
				pmd.setPotential(potential[i]);
				pmd.setA(a[i]);
				pmd.setV(v[i]);
				pmd.setTongdian(tongdian[i]);
				pmd.setMeasurer(measurer[i]);
				pmd.setRemark(remark[i]);

				pmdList.add(pmd);
			}
			i++;
		}
		String status = "0";
		if (pmdList.size() > 0) {
			baseService.savePlMeasure(pm, pmdList);
			status = "1";
		}

		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}

		return "redirect:/admin/base/pl_measure/" + page;
	}

	@RequestMapping(value = "/pl_measure/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String plMeasure_list(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String c_month,
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

		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
			model.addAttribute("c_month", c_month);
		}

		PageSupport ps = PageSupport.initPageSupport(request);
		List<PotentialMeasure> pmList = baseService.queryPotentialMeasure(
				param, ps);

		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("pmList", pmList);
		return "pages/base/pl_measure_list";
	}

	/**
	 * 审核记录
	 * 
	 * @param model
	 * @param request
	 * @param authentication
	 * @param pl
	 * @param section
	 * @param spec
	 * @param c_month
	 * @param del
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/verify", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String plMeasure_verify(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String c_month,
			@RequestParam(required = false) Integer del) {
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

		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
			model.addAttribute("c_month", c_month);
		}

		PageSupport ps = PageSupport.initPageSupport(request);
		List<PotentialMeasure> pmList = baseService.queryPotentialMeasure(
				param, ps);

		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());

		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("pmList", pmList);
		model.addAttribute("verify", 1);
		return "pages/base/pl_measure_list";
	}

	/**
	 * 管道保护电位测量记录删除
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/del", method = RequestMethod.GET)
	public String plMeasure_del(Model model, @RequestParam Integer id) {
		baseService.deletePotentialMeasureById(id);
		return "redirect:/admin/base/pl_measure/list?del=1";
	}

	/**
	 * 管道保护电位测量记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/show", method = RequestMethod.GET)
	public String plMeasure_show(Model model, @RequestParam Integer id, @RequestParam(required=false) Integer tips_id) {
		if (tips_id != null && tips_id.intValue() > 0)
			baseService.deleteTips(tips_id);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		PotentialMeasure pm = baseService.queryPotentialMeasureById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(pm.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pm
				.getPl_section_id());

		model.addAttribute("pl", pm.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pm.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pm", pm);
		return "pages/base/pl_measure_show";
	}

	/**
	 * 管道保护电位测量记录审核页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/show_verify", method = RequestMethod.GET)
	public String plMeasure_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		PotentialMeasure pm = baseService.queryPotentialMeasureById(id);
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
		return "pages/base/pl_measure_show";
	}

	/**
	 * 管道保护电位测量记录导出
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/exp", method = RequestMethod.GET)
	public String plMeasure_export(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String c_month) {
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

		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
		}

		List<PotentialMeasure> pmList = baseService.queryPotentialMeasure(
				param, null);
		if (pmList.size() == 0) {
			return "redirect:/admin/base/pl_measure/list";
		}
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称
		String path = fileDir;
		String excelPath = path + sep + "excel.xls";
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		
		try {
			HSSFWorkbook work = new HSSFWorkbook();
			FileOutputStream fileOut = new FileOutputStream(path + sep
					+ "excel.xls");

			/*pm.getPl_name() + "_" + pm.getPl_section_name()
			+ pm.getC_month()*/
			
			HSSFSheet sheet1 = work.createSheet("sheet1");
			HSSFCell cell;

			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 表头格式
			CellStyle titleStyle = work.createCellStyle();
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

			font.setFontHeightInPoints((short) 20);
			font.setFontName("方正仿宋简体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			titlefont.setFontHeightInPoints((short) 10);
			titlefont.setFontName("方正仿宋简体");
			titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");
			datafont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			// datafont.setColor(HSSFColor.RED.index);

			// 把字体加入到格式中
			cellStyle.setFont(font);
			titleStyle.setFont(titlefont);
			dataStyle.setFont(datafont);

			// 设置列宽度
			sheet1.setColumnWidth(0, 8 * 256);
			sheet1.setColumnWidth(1, 5 * 256);
			sheet1.setColumnWidth(2, 9 * 256);
			sheet1.setColumnWidth(3, 6 * 256);
			sheet1.setColumnWidth(4, 6 * 256);
			sheet1.setColumnWidth(5, 7 * 256 + 50);
			sheet1.setColumnWidth(6, 8 * 256);
			sheet1.setColumnWidth(7, 14 * 256);
			sheet1.setColumnWidth(8, 256);
			sheet1.setColumnWidth(9, 8 * 256);
			sheet1.setColumnWidth(10, 5 * 256);
			sheet1.setColumnWidth(11, 9 * 256);
			sheet1.setColumnWidth(12, 6 * 256);
			sheet1.setColumnWidth(13, 6 * 256);
			sheet1.setColumnWidth(14, 7 * 256 + 50);
			sheet1.setColumnWidth(15, 8 * 256);
			sheet1.setColumnWidth(16, 14 * 256);
			
			int row_index = 0;
			for (PotentialMeasure pm : pmList) {
				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 16));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 0, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 5, 11));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 15, 16));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+2, 3, 5));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+2, 12, 14));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 0, 0));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 1, 1));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 2, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 6, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 7, 7));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 9, 9));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 10, 10));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 11, 11));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 15, 15));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 16, 16));
				
				// 新建行
				HSSFRow row = sheet1.createRow(row_index);
				row_index++;
				// 新建单元格
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue("管   道   保   护   电   位   测   量   记   录");
				// 设置单元格格式
				cell.setCellStyle(cellStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.createRow(row_index);
				row_index++;
				// 设置行高度
				row.setHeightInPoints(20);
				cell = row.createCell(0);
				cell.setCellValue("井(站) " + pm.getJinzhan());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(5);
				cell.setCellValue("管线名称及规格: " + pm.getPl_name() + "/"
						+ pm.getPl_spec_name());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(15);
				cell.setCellValue(pm.getC_month().toString().substring(0, 4)
						+ " 年 " + pm.getC_month().toString().substring(4, 6)
						+ " 月 ");
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				
				PotentialMeasure pmd = baseService.queryPotentialMeasureById(pm
						.getId());
				Integer sz = 16;
				if(((pmd.getDetailList().size()+1)/2) > 16)
					sz = (pmd.getDetailList().size()+1)/2;
				
				// 添加边框线
				int end_row = 0;
				for (int rown = 0; rown <= sz+1; rown++) { //(pmd.getDetailList().size()/2)+4
					row = sheet1.createRow(rown+row_index);
					// row.setHeightInPoints((float)22.5);
					for (int celln = 0; celln < 17; celln++) {
						if (celln == 8)
							continue;
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown+row_index;
				}

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(15);
				cell = row.getCell(0);
				cell.setCellValue("测试桩编号");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(1);
				cell.setCellValue("测量日期");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("电位(-V)");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				cell.setCellValue("阴极保护站输出");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(6);
				cell.setCellValue("测量人");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(7);
				cell.setCellValue("备注");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(9);
				cell.setCellValue("测试桩编号");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(10);
				cell.setCellValue("测量日期");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(11);
				cell.setCellValue("电位(-V)");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(12);
				cell.setCellValue("阴极保护站输出");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(15);
				cell.setCellValue("测量人");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(16);
				cell.setCellValue("备注");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(25);
				cell = row.getCell(3);
				cell.setCellValue("A");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(4);
				cell.setCellValue("V");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(5);
				cell.setCellValue("通电点(-V)");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(12);
				cell.setCellValue("A");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(13);
				cell.setCellValue("V");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(14);
				cell.setCellValue("通电点(-V)");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				// 填写数据
				
				
				int count = 0;
				int indexl = row_index;
				int indexr = row_index;
				for (PotentialMeasureDetail pd : pmd.getDetailList()) {
					if (count/2 < sz/2) {
						row = sheet1.getRow(indexl++);
						cell = row.getCell(0);
						// cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getNo());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(1);
						if(pd.getM_date()!=null) {
							Calendar ca = Calendar.getInstance();
							ca.setTime(pd.getM_date());
							// 获取date中的天数
							cell.setCellValue(ca.get(Calendar.DAY_OF_MONTH));
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						} else {
							cell.setCellValue("");
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}
						cell.setCellStyle(dataStyle);

						cell = row.getCell(2);
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(pd.getPotential()==null?"":pd.getPotential()+"");

						cell = row.getCell(3);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getA()==null?"":pd.getA()+"");
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(4);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getV()==null?"":pd.getV()+"");
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(5);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getTongdian()==null?"":pd.getTongdian()+"");
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(6);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getMeasurer());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(7);
						cell.setCellStyle(dataStyle);
						if (pd.getRemark() != null) {
							cell.setCellValue(pd.getRemark());
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}
					} else {
						row = sheet1.getRow(indexr++);
						cell = row.getCell(9);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getNo());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(10);
						if(pd.getM_date()!=null) {
							Calendar ca = Calendar.getInstance();
							ca.setTime(pd.getM_date());
							// 获取date中的天数
							cell.setCellValue(ca.get(Calendar.DAY_OF_MONTH));
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						} else {
							cell.setCellValue("");
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}
						cell.setCellStyle(dataStyle);

						cell = row.getCell(11);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getPotential()==null?"":pd.getPotential()+"");
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						
						cell = row.getCell(12);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getA()==null?"":pd.getA()+"");
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						
						cell = row.getCell(13);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getV()==null?"":pd.getV()+"");
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(14);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getTongdian()==null?"":pd.getTongdian()+"");
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(15);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getMeasurer());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(16);
						cell.setCellStyle(dataStyle);
						if (pd.getRemark() != null) {
							cell.setCellValue(pd.getRemark());
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}

					}
					count++;
				}
				
				sheet1.addMergedRegion(new CellRangeAddress(sz+row_index, sz+row_index, 0, 5));
				sheet1.addMergedRegion(new CellRangeAddress(sz+row_index, sz+row_index, 9, 11));
				//尾部
				row = sheet1.createRow(sz+row_index);
				row.setHeightInPoints(18);
				cell = row.createCell(0);
				cell.setCellValue("保存单位: " + pm.getUnit());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(9);
				cell.setCellValue("保存期限: " + pm.getSave_date().toString() + "年");
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				row_index = end_row + 4; 
			}
			// 将创建好的excel存到指定文件夹下
			work.write(fileOut);
			fileOut.close();
			// 压缩文件夹并下载，下载后删除文件夹
			FileUtils.createZip(response, excelPath, DateFormatter.dateToString(new Date(), "yyyy-MM-dd_HH:mm:ss:SSS"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/pl_measure/verify_save", method = RequestMethod.POST)
	public @ResponseBody String plMeasure_verify_save(Model model,
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		baseService.updatePotentialMeasureVerifyStatus(id, ud.getId(), status,
				verify_desc);

		return JsonUtil.toJson("OK");
	}

	/**
	 * 管道保护电位测量记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/edit", method = RequestMethod.GET)
	public String plMeasure_edit(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		PotentialMeasure pm = baseService.queryPotentialMeasureById(id);
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
		return "pages/base/pl_measure_edit";
	}
}
