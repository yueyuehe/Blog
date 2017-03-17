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
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.RunRecord;
import cn.hm.oil.domain.RunRecordDetail;
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
 *         阴极保护站运行记录查看
 * 
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BaseRcController {
	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;

	/**
	 * 阴极保护站运行记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/create", method = RequestMethod.GET)
	public String rc_create(Model model,
			@RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud
				.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		Prompt prompt = baseService.quertPromptByType(5);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}

		return "pages/base/rc_create";
	}

	/**
	 * 阴极保护站运行记录保存
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/save", method = RequestMethod.POST)
	public String rcRecord_save(Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam Integer pl,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam String auditor,
			@RequestParam String r_month, @RequestParam Integer[] day,
			@RequestParam Integer[] time, @RequestParam Float[] jldy,
			@RequestParam Float[] zlsc_a, @RequestParam Float[] zlsc_v,
			@RequestParam Float[] tdddw, @RequestParam String[] recorder,
			@RequestParam String[] comment, @RequestParam String[] others) {
		RunRecord rc = new RunRecord();
		rc.setId(id);
		rc.setAuditor(auditor);
		rc.setJinzhan(jinzhan);
		rc.setPl_id(pl);
		rc.setPl_section_id(section);
		rc.setPl_spec_id(spec);
		rc.setR_month(Integer.valueOf(r_month.replace("-", "")));
		rc.setStatus(0);
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		rc.setCreater(ud.getId());
		
		List<RunRecordDetail> rcdList = new ArrayList<RunRecordDetail>();
		int i = 0;
		for (String r : recorder) {
			RunRecordDetail rcd = new RunRecordDetail();
			rcd.setDay(day[i]);
			rcd.setTime(time[i]);
			rcd.setJldy(jldy[i]);
			rcd.setOthers(others[i]);
			rcd.setRecorder(recorder[i]);
			rcd.setTdddw(tdddw[i]);
			rcd.setZlsc_a(zlsc_a[i]);
			rcd.setZlsc_v(zlsc_v[i]);
			rcd.setComment(comment[i]);

			rcdList.add(rcd);
			
			i++;
		}
		String status = "0";
		if (rcdList.size() > 0) {
			baseService.saveRcRecord(rc, rcdList);
			status = "1";
		}
		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}
		
		return "redirect:/admin/base/rc/"+page;
	}

	/**
	 * 阴极保护站运行记录查看
	 * 
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param r_month
	 * @return
	 */
	@RequestMapping(value = "/rc/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String rcRecord_list(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String r_month,
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

		if (!StringUtils.isBlank(r_month)) {
			param.put("r_month", Integer.valueOf(r_month.replace("-", "")));
			model.addAttribute("r_month", r_month);
		}
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<RunRecord> rcList = baseService.queryRunRecord(param, ps);

		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("rcList", rcList);
		return "pages/base/rc_create_list";
	}

	/**
	 * 阴极保护站运行记录审核
	 * 
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param r_month
	 * @return
	 */
	@RequestMapping(value = "/rc/verify", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String rcRecord_verify(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String r_month) {
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

		if (!StringUtils.isBlank(r_month)) {
			param.put("r_month", Integer.valueOf(r_month.replace("-", "")));
			model.addAttribute("r_month", r_month);
		}

		PageSupport ps = PageSupport.initPageSupport(request);
		List<RunRecord> rcList = baseService.queryRunRecord(param, ps);

		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());

		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("rcList", rcList);
		model.addAttribute("verify", 1);
		return "pages/base/rc_create_list";
	}
	
	
	/**
	 * 阴极保护站运行记录详细查看
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/rc/show", method = RequestMethod.GET)
	public String plPatrol_show(Model model, 
			@RequestParam Integer id, 
			@RequestParam(required=false) Integer tips_id) {
		if (tips_id != null && tips_id.intValue() > 0)
			baseService.deleteTips(tips_id);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		RunRecord rc = baseService.queryRunRecordById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(rc
				.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(rc
				.getPl_section_id());

		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", rc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("rc", rc);

		return "pages/base/rc_create_show";
	}

	/**
	 * 审核
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/rc/show_verify", method = RequestMethod.GET)
	public String plPatrol_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		RunRecord rc = baseService.queryRunRecordById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(rc
				.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(rc
				.getPl_section_id());

		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", rc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("rc", rc);
		model.addAttribute("verify", 1);
		return "pages/base/rc_create_show";
	}
	
	
	/**
	 * 阴极保护站运行记录导出
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/exp", method = RequestMethod.GET)
	public String plMeasure_export(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String r_month) {
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

		if (!StringUtils.isBlank(r_month)) {
			param.put("r_month", Integer.valueOf(r_month.replace("-", "")));
		}

		List<RunRecord> rcList = baseService.queryRunRecord(param, null);
		if (rcList.size() == 0) {
			return "redirect:/admin/base/rc/list";
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
			sheet1.setColumnWidth(0, 7 * 256);
			sheet1.setColumnWidth(1, 7 * 256);
			sheet1.setColumnWidth(2, 8 * 256);
			sheet1.setColumnWidth(3, 8 * 256);
			sheet1.setColumnWidth(4, 8 * 256);
			sheet1.setColumnWidth(5, 8 * 256);
			sheet1.setColumnWidth(6, 8 * 256);
			sheet1.setColumnWidth(7, 16 * 256);
			sheet1.setColumnWidth(8, 16 * 256);

			int row_index = 0;
			for (RunRecord rc : rcList) {
				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 8));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 0, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 3, 7));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+2, 0, 1));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+2, 3, 4));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 2, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 5, 5));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 6, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 7, 7));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 8, 8));

				// 第一行
				HSSFRow row = sheet1.createRow(row_index);
				row_index++;
				// 新建单元格
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue("阴极保护站运行记录");
				// 设置单元格格式
				cell.setCellStyle(cellStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				// 第二行
				row = sheet1.createRow(row_index);
				row_index++;
				// 设置行高度
				row.setHeightInPoints(25);
				cell = row.createCell(0);
				cell.setCellValue("井(站) " + rc.getJinzhan());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(3);
				cell.setCellValue("管线名称及规格: " + rc.getPl_name() + "/"
						+ rc.getPl_spec_name());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(8);
				cell.setCellValue(rc.getR_month().toString().substring(0, 4)
						+ " 年 " + rc.getR_month().toString().substring(4, 6)
						+ " 月 ");
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				RunRecord rcd = baseService.queryRunRecordById(rc.getId());
				
				Integer sz = 32;
				if(rcd.getDetailList().size() > 32) {
					sz = rcd.getDetailList().size();
				}
				
				int end_row = 0;
				// 添加边框线
				for (int rown = 0; rown < sz + 2; rown++) {
					row = sheet1.createRow(rown+row_index);
					// row.setHeightInPoints(18);
					for (int celln = 0; celln < 9; celln++) {
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown + row_index;
				}
				
				sheet1.addMergedRegion(new CellRangeAddress(sz + row_index+2, sz + row_index+2, 7, 8));
				// 审核人行
				row = sheet1.createRow(sz + row_index+2);
				row.setHeightInPoints(18);
				cell = row.createCell(7);
				cell.setCellValue("审核人:" + rc.getAuditor());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				// 第三行
				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(15);
				cell = row.getCell(0);
				cell.setCellValue("时间");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("交流电压(V)");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				cell.setCellValue("直流输出");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(5);
				cell.setCellValue("通电点电位(-V)");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(6);
				cell.setCellValue("记录人");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(7);
				cell.setCellValue("大 事 纪 要");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(8);
				cell.setCellValue("维护换机等情况");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				// 第四行
				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(28);
				cell = row.getCell(0);
				cell.setCellValue("日");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(1);
				cell.setCellValue("时");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				cell.setCellValue("A");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(4);
				cell.setCellValue("V");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				//int index = 4;
				for (RunRecordDetail pd : rcd.getDetailList()) {
					row = sheet1.getRow(row_index++);
					// row.setHeightInPoints(18);
					cell = row.getCell(0);
					if (pd.getDay() != null) {
						cell.setCellValue(pd.getDay());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}

					cell = row.getCell(1);
					if (pd.getTime() != null) {
						cell.setCellValue(pd.getTime());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}

					cell = row.getCell(2);
					if (pd.getJldy() != null) {
						cell.setCellValue(pd.getJldy());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}

					cell = row.getCell(3);
					if (pd.getZlsc_a() != null) {
						cell.setCellValue(pd.getZlsc_a());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}

					cell = row.getCell(4);
					if (pd.getZlsc_v() != null) {
						cell.setCellValue(pd.getZlsc_v());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}

					cell = row.getCell(5);
					if (pd.getTdddw() != null) {
						cell.setCellValue(pd.getTdddw());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					}

					cell = row.getCell(6);
					if (pd.getRecorder() != null) {
						cell.setCellValue(pd.getRecorder());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}

					cell = row.getCell(7);
					if (pd.getComment() != null) {
						cell.setCellValue(pd.getComment());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}

					cell = row.getCell(8);
					if (pd.getOthers() != null) {
						cell.setCellValue(pd.getOthers());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
				}
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

	@RequestMapping(value = "/rc/verify_save", method = RequestMethod.POST)
	public @ResponseBody String plMeasure_verify_save(Model model,
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		baseService.updateRunRecordVerifyStatus(id, ud.getId(), status,
				verify_desc);

		return JsonUtil.toJson("OK");
	}

	
	/**
	 * 阴极保护站运行记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/edit", method = RequestMethod.GET)
	public String rc_edit(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		RunRecord rc = baseService.queryRunRecordById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(rc
				.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(rc
				.getPl_section_id());

		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", rc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("rc", rc);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}

		return "pages/base/rc_edit";
	}
	
	@RequestMapping(value = "/rc/del", method = RequestMethod.GET)
	public String rc_del(Model model, @RequestParam Integer id) {
		baseService.deleteRunRecordById(id);
		return "redirect:/admin/base/rc/list?del=1";
	}
	
}
