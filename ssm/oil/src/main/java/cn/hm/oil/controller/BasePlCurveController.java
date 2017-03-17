package cn.hm.oil.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.RunRecordDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.NewBaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.CommonsMethod;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.DrawPic;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

/**
 * @author Administrator
 * 
 *         管道保护电位曲线图查看
 * 
 */
@Controller
@RequestMapping(value = "/admin/base")
public class BasePlCurveController {

	String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称

	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;

	@Autowired
	private NewBaseService newBaseService;
	/**
	 * 管道保护电位曲线图创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_curve/create", method = RequestMethod.GET)
	public String plCurve_create(Model model, @RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		model.addAttribute("pipeLineList", pipeLineList);

		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		Prompt prompt = baseService.quertPromptByType(2);
		if (prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/pl_curve_create";
	}

	@RequestMapping(value = "/pl_curve/getTime")
	public @ResponseBody JsonResWrapper GetTime(@RequestParam Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		PotentialCurve pc = baseService.queryPotentialCurveById(id);
		pc.setCreate_t(DateFormatter.dateToString(pc.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
		if (pc.getVerify_time() != null) {
			pc.setVerify_t(DateFormatter.dateToString(pc.getVerify_time(), "yyyy-MM-dd HH:mm:ss"));
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pc);
		return response;
	}

	@RequestMapping(value = "/pl_curve/setTime")
	public @ResponseBody JsonResWrapper setTime(@RequestParam Integer id, @RequestParam String create_t,
			@RequestParam String verify_t) {
		JsonResWrapper response = new JsonResWrapper();
		baseService.updatePotentialCurveTime(id, create_t, verify_t);
		response.setStatus(ResponseStatus.OK);
		return response;
	}

	/**
	 * 管道保护电位曲线图保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pl_curve/save", method = RequestMethod.POST)
	public String plCurve_save(Model model, @RequestParam Integer pl, @RequestParam Integer section,
			@RequestParam Integer spec, @RequestParam String unit, @RequestParam String c_month,
			@RequestParam String[] no, @RequestParam Float[] p_early, @RequestParam Float[] p_end,
			@RequestParam String[] measurer, @RequestParam String[] remark, @RequestParam String up_id,
			@RequestParam String analysis, @RequestParam(required = false) Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		PotentialCurve pc = new PotentialCurve();
		pc.setC_month(Integer.valueOf(c_month.replace("-", "")));
		pc.setPl_id(pl);
		pc.setPl_section_id(section);
		pc.setPl_spec_id(spec);
		pc.setUnit(unit);
		pc.setCreater(ud.getId());
		pc.setUp_id(up_id);
		pc.setAnalysis(analysis);
		if (id != null) {
			pc.setId(id);
		}
		List<PotentialCurveDetail> pcdList = new ArrayList<PotentialCurveDetail>();
		int i = 0;
		for (String n : no) {
			PotentialCurveDetail pcd = new PotentialCurveDetail();
			if (!StringUtils.isBlank(n)) {
				pcd.setNo(n);
				pcd.setMeasurer(measurer[i]);
				pcd.setP_early(p_early[i]);
				pcd.setP_end(p_end[i]);
				pcd.setRemark(remark[i]);

				pcdList.add(pcd);
			}
			i++;
		}
		String chartPath = null;

		String status = "0";
		try {
			List<Double> p_earlyList = new ArrayList<Double>();
			List<Double> p_endList = new ArrayList<Double>();

			// 获取点电位数据
			for (PotentialCurveDetail pcd : pcdList) {
				p_earlyList.add((double) pcd.getP_early());
				p_endList.add((double) pcd.getP_end());
			}

			// 生成电位图
			CategoryDataset dataset = DrawPic.createDataset(p_earlyList, p_endList);
			JFreeChart chart = DrawPic.createChart(dataset);
			chartPath = DrawPic.saveAsFile(
					pc.getPl_id() + "-" + pc.getPl_section_id() + "-" + pc.getPl_spec_id() + "-" + pc.getC_month(),
					chart);
		} catch (Exception e) {
			return "redirect:/admin/base/pl_curve/create?status=" + status;
		}
		if (chartPath == null) {
			return "redirect:/admin/base/pl_curve/create?status=" + status;
		}
		pc.setChartPath(chartPath);
		if (pcdList.size() > 0) {
			baseService.savePlCurve(pc, pcdList);
			status = "1";
		}

		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}
		return "redirect:/admin/base/pl_curve/" + page;
	}
	
	
	
	@RequestMapping(value = "/pl_curve/modify", method = RequestMethod.POST)
	public String plPatrol_modify(Model model, HttpServletRequest request,@RequestParam(required = false) Integer pc_id,
			@RequestParam(required = false) Integer verify,
			@RequestParam(required=false) Integer tips_id,
			
			@RequestParam Integer pl, @RequestParam Integer section,
			@RequestParam Integer spec, @RequestParam String unit, @RequestParam String c_month,
			@RequestParam String[] no, @RequestParam Float[] p_early, @RequestParam Float[] p_end,
			@RequestParam String[] measurer, @RequestParam String[] remark, @RequestParam String up_id,
			@RequestParam String analysis,
			@RequestParam Integer status
			)
			//@RequestParam(required = false) String up_id,@RequestParam(required = false) String status)
			{
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PotentialCurve pc = new PotentialCurve();
		pc.setUnit(unit);
		pc.setCreater(ud.getId());
		pc.setUp_id(up_id);
		pc.setAnalysis(analysis);
		pc.setId(pc_id);
		if(status != null)
			pc.setStatus(status);
		List<PotentialCurveDetail> pcdList = new ArrayList<PotentialCurveDetail>();
		int i = 0;
		for (String n : no) {
			PotentialCurveDetail pcd = new PotentialCurveDetail();
			if (!StringUtils.isBlank(n)) {
				pcd.setNo(n);
				pcd.setMeasurer(measurer[i]);
				pcd.setP_early(p_early[i]);
				pcd.setP_end(p_end[i]);
				pcd.setRemark(remark[i]);
				pcd.setPc_id(pc_id);
				pcdList.add(pcd);
			}
			i++;
		}
		String page = "";
		page = CommonsMethod.putUrlParam(page, "id", spec);
		page = CommonsMethod.putUrlParam(page, "r_month", c_month);
		page = CommonsMethod.putUrlParam(page, "verify", verify);
		page = CommonsMethod.putUrlParam(page, "tips_id", tips_id);
		
		String chartPath = null;

		try {
			List<Double> p_earlyList = new ArrayList<Double>();
			List<Double> p_endList = new ArrayList<Double>();

			// 获取点电位数据
			for (PotentialCurveDetail pcd : pcdList) {
				p_earlyList.add((double) pcd.getP_early());
				p_endList.add((double) pcd.getP_end());
			}

			// 生成电位图
			CategoryDataset dataset = DrawPic.createDataset(p_earlyList, p_endList);
			JFreeChart chart = DrawPic.createChart(dataset);
			chartPath = DrawPic.saveAsFile(
					pc.getPl_id() + "-" + pc.getPl_section_id() + "-" + pc.getPl_spec_id() + "-" + pc.getC_month(),
					chart);
		} catch (Exception e) {
			return "redirect:/admin/base/pl_curve/show"+page;
		}
		if (chartPath == null) {
			return "redirect:/admin/base/pl_curve/show"+page;
		}
		pc.setChartPath(chartPath);
		if (pcdList.size() > 0) {
			baseService.savePlCurve(pc, pcdList);
		}		
		return "redirect:/admin/base/pl_curve/show"+page;//"pages/base/pl_patrol_show_new";
	}
	
	@RequestMapping(value = "/pl_curve/batch_changestatus", method = {RequestMethod.GET,RequestMethod.POST})
	public String plPatrol_batch_changestatus(Authentication authentication,HttpServletRequest request,
							  @RequestParam(required = false) Integer id, 
							  @RequestParam(required = false) String r_month,
							  @RequestParam(required = false) Integer verify,
							  @RequestParam(required = false) Integer[] oldStatus,
							  @RequestParam(required = false) Integer newStatus,
							  @RequestParam(required=false) Integer tips_id) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object obj = request.getSession().getAttribute(ud.getId() + "rrIdList");
		if (obj != null) {
			List<Integer> rrIdList = (List<Integer>)obj;
			for (Integer rrId : rrIdList)
				//newBaseService.updateRunRecordVerifyStatus(rrId, ud.getId(), status, verify_desc);
				newBaseService.updateChangeCurveStatus(rrId, newStatus);
			request.getSession().removeAttribute(ud.getId() + "rrIdList");
		}
		
		String page = "";
		page = CommonsMethod.putUrlParam(page, "id", id);
		page = CommonsMethod.putUrlParam(page, "r_month", r_month);
		page = CommonsMethod.putUrlParam(page, "verify", verify);
		page = CommonsMethod.putUrlParam(page, "tips_id", tips_id);

		return "redirect:/admin/base/pl_curve/show"+page;
	}
	@RequestMapping(value = "/pl_curve/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String plCurve_list(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required = false) Integer pl, // 管线
			@RequestParam(required = false) Integer section, // 起止段落
			@RequestParam(required = false) Integer spec, // 规格
			@RequestParam(required = false) String c_month, // 时间
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

		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
			model.addAttribute("c_month", c_month);
		}

		PageSupport ps = PageSupport.initPageSupport(request);

		// 判读用户是否是维护工，维护工只能查看自己的数据
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param);
		model.addAttribute("role", role);

		List<PotentialCurve> pcList = baseService.queryPotentialCurve(param, ps);
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}

		// 查询出所有有数据
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);

		param.put("pl_curve", "pl_curve");// 判定需要联查的表
		// 规格 显示列表
		List<BasePipelineSpec> specList = baseService.querySpecListNew(param);
		model.addAttribute("specLists", specList);

		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("pcList", pcList);
		return "pages/base/pl_curve_list";
	}

	/**
	 * 管道曲线图详细查看
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/pl_curve/show", method = {RequestMethod.GET,RequestMethod.POST})
	public String plCurve_show(Model model, Authentication authentication, @RequestParam Integer id,
			@RequestParam(required = false) Integer tips_id, @RequestParam(required = false) Integer verify,
			@RequestParam(required = false) String r_month,
			HttpServletRequest request) throws IOException {

		if (tips_id != null && tips_id.intValue() > 0) {
			baseService.deleteTips(tips_id);
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pl_spec_id", id);

		Integer role = CommonsMethod.getDataByRole(authentication, userService, param);
		model.addAttribute("role", role);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		if (r_month != null && r_month.trim().length()!=0) {
			param.put("c_month", r_month.replace("-", ""));
		}
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		//if(verify != null && verify.intValue() == 1)
		{
			//param.put("status", 0);
			Map<String, Object> param0 = new HashMap<String, Object>(param);			
			PageSupport ps = PageSupport.initPageSupport(request);
			ps.setPageSize(10000);
			List<PotentialCurve> rrdList1 = baseService.queryPotentialCurve(param, ps);
			
			List<Integer> rrIdList = null;
			Object obj = request.getSession().getAttribute(ud.getId() + "rrIdList");
			if (obj != null) {
				rrIdList = (List<Integer>)obj;
			} else {
				request.getSession().removeAttribute(ud.getId() + "rrIdList");
				rrIdList = new ArrayList<Integer>();
			}
			if (!CollectionUtils.isEmpty(rrdList1)) {
				for (PotentialCurve rrd : rrdList1) {
					if (!rrIdList.contains(rrd.getId())) {
						rrIdList.add(rrd.getId());
					}
				}
				request.getSession().setAttribute(ud.getId() + "rrIdList", rrIdList);
			}
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		ps.setPageSize(1);
		//ps.setPageOffset(offset);

		List<PotentialCurve> pcs = baseService.queryPotentialCurve(param, ps);
		if (pcs == null || pcs.size() <= 0) {
			return "pages/base/pl_curve_show";
		}
		
		
		PotentialCurve pc = pcs.get(0);
		pc.resetCanEidt(role);
		List<BasePipelineSection> sectionList = baseService.querySection(pc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pc.getPl_section_id());
		pc.setDetailList(baseService.queryPotentialCurveDetailByPcid(pc.getId()));
		{
			int size = pc.getDetailList().size();
			if(size < 12)
			{
				for(int i = size; i < 12; i++)
				{
					PotentialCurveDetail n = new PotentialCurveDetail();
					pc.getDetailList().add(n);
				}
			}
		}
		
		model.addAttribute("pl", pc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pc", pc);
		model.addAttribute("pcs", pcs);
		model.addAttribute("r_month", r_month);
		model.addAttribute("offset", ps.getPageOffset());
		model.addAttribute("total", ps.getTotalPage());
		model.addAttribute("id", id);
		model.addAttribute("verify", verify);
		return "pages/base/pl_curve_show";
	}

	/**
	 * 管道保护电位曲线图导出
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_curve/exp", method = RequestMethod.GET)
	public String plCurve_export(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String c_month) {
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

		List<PotentialCurve> pcList = baseService.queryPotentialCurve(param, null);
		if (pcList.size() == 0) {
			return "redirect:/admin/base/pl_curve/list";
		}
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称
		String imageDir = SettingUtils.getCommonSetting("upload.image.path");
		String path = fileDir;
		String excelPath = path + sep + "excel" + ".xls";
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		try {
			HSSFWorkbook work = new HSSFWorkbook();
			FileOutputStream fileOut = new FileOutputStream(path + sep + "excel" + ".xls");

			/*
			 * pc.getPl_name() + "_" + pc.getPl_section_name() + pc.getC_month()
			 * +
			 */

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
			sheet1.setColumnWidth(1, 9 * 256);
			sheet1.setColumnWidth(2, 9 * 256);
			sheet1.setColumnWidth(3, 9 * 256);
			sheet1.setColumnWidth(4, 9 * 256);
			sheet1.setColumnWidth(5, 9 * 256);
			sheet1.setColumnWidth(6, 8 * 256);
			sheet1.setColumnWidth(7, 8 * 256);
			sheet1.setColumnWidth(8, 8 * 256);
			sheet1.setColumnWidth(9, 8 * 256);
			sheet1.setColumnWidth(10, 8 * 256);
			sheet1.setColumnWidth(11, 8 * 256);
			sheet1.setColumnWidth(12, 8 * 256);
			sheet1.setColumnWidth(13, 8 * 256);
			sheet1.setColumnWidth(14, 8 * 256);

			// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();

			int row_index = 0;
			int img_row = 0;
			for (PotentialCurve pc : pcList) {

				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 14));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 1, row_index + 1, 0, 3));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 1, row_index + 1, 5, 9));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 1, row_index + 1, 12, 14));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 2, row_index + 33, 5, 14));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 34, row_index + 37, 5, 14));

				// 第一行
				HSSFRow row = sheet1.createRow(row_index);
				row_index++;
				// 新建单元格
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue("管   道   保   护   电   位   曲   线   图");
				// 设置单元格格式
				cell.setCellStyle(cellStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				// 第二行
				row = sheet1.createRow(row_index);
				row_index++;
				// 设置行高度
				row.setHeightInPoints(19);
				cell = row.createCell(0);
				cell.setCellValue("单位 :" + pc.getUnit());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(5);
				cell.setCellValue("管线名称 " + pc.getPl_name() + "/" + pc.getPl_spec_name());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(12);
				cell.setCellValue(pc.getC_month().toString().substring(0, 4) + " 年 "
						+ pc.getC_month().toString().substring(4, 6) + " 月 ");
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				PotentialCurve pcd = baseService.queryPotentialCurveById(pc.getId());
				// 添加边框线
				Integer sz = 35;
				if (pcd.getDetailList().size() > 35)
					sz = pcd.getDetailList().size();
				int end_row = 0;
				for (int rown = 0; rown <= sz; rown++) {
					
					row = sheet1.createRow(rown + row_index);
					// row.setHeightInPoints(40);
					for (int celln = 0; celln < 15; celln++) {
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown + row_index;
				}

				// 第三行
				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(24);
				cell = row.getCell(0);
				cell.setCellValue("测试桩编号");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(1);
				cell.setCellValue("上旬电位(-V)");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("下旬电位(-V)");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				cell.setCellValue("测量人");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(4);
				cell.setCellValue("备注");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				/*
				 * // 第35行 row = sheet1.getRow(row_index+33); //row_index++; //
				 * row.setHeightInPoints(24); cell = row.getCell(5);
				 * cell.setCellValue("分析:");
				 * cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				 */

				// int index = 3;
				/*
				 * List<Double> p_earlyList = new ArrayList<Double>();
				 * List<Double> p_endList = new ArrayList<Double>();
				 */
				/*
				 * int u = 0; int d = 0;
				 */
				for (PotentialCurveDetail pcdd : pcd.getDetailList()) {
					row = sheet1.getRow(row_index);
					row_index++;
					// row.setHeightInPoints(24);
					// 测试桩编号
					cell = row.getCell(0);
					if (pcdd.getNo() != null) {
						cell.setCellValue(pcdd.getNo());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}

					// 上旬电位(-V)
					cell = row.getCell(1);
					if (pcdd.getP_early() != null) {
						cell.setCellValue(pcdd.getP_early());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						// p_earlyList.add((double) pcdd.getP_early());
					}

					// 下旬电位(-V)
					cell = row.getCell(2);
					if (pcdd.getP_end() != null) {
						cell.setCellValue(pcdd.getP_end());
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						// p_endList.add((double) pcdd.getP_end());
					}

					// 测量人
					cell = row.getCell(3);
					if (pcdd.getMeasurer() != null) {
						cell.setCellValue(pcdd.getMeasurer());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}

					// 备注
					cell = row.getCell(4);
					if (pcdd.getRemark() != null) {
						cell.setCellValue(pcdd.getRemark());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}

				}

				/*
				 * String[] rowKeys = { "上旬电位", "下旬电位" }; // 生成电位图
				 * CategoryDataset dataset = DrawPic.createDataset(p_earlyList,
				 * p_endList); JFreeChart chart = DrawPic.createChart(dataset);
				 * String b = DrawPic.saveAsFile("pic", chart);
				 */
				/*
				 * if(b) { System.out.println("图片储存成功"); }
				 */

				// 读取图片
				ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
				try {
					BufferedImage bufferImg = ImageIO.read(new File(imageDir + sep + pc.getChartPath()));
					ImageIO.write(bufferImg, "png", byteArrayOut);
					// anchor主要用于设置图片的属性
					HSSFClientAnchor anchor = new HSSFClientAnchor(10, 5, 0, 0, (short) 5, 2 + img_row, (short) 15,
							34 + img_row);
					anchor.setAnchorType(3);
					// 插入图片
					patriarch.createPicture(anchor,
							work.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
				} catch (IIOException e) {
					e.printStackTrace();
				}
				row_index = end_row + 4;
				img_row = row_index;
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

	/**
	 * 管道保护电位曲线图审核查看
	 * 
	 * @param model
	 * @param request
	 * @param authentication
	 * @param pl
	 * @param section
	 * @param spec
	 * @param c_month
	 * @return
	 */
	@RequestMapping(value = "/pl_curve/verify", method = { RequestMethod.GET, RequestMethod.POST })
	public String plCurve_verify(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String c_month) {
		//List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String, Object>());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", 0);
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param, false);
		model.addAttribute("role", role);
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			//List<BasePipelineSection> sectionList = baseService.querySection(pl);
			List<BasePipelineSection> sectionList = newBaseService.querySectionByAdminPlCurve(param);
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
		
		List<BasePipelineSpec> specList = newBaseService.querySpecListNewByAdminPlCurve(param);
		model.addAttribute("specLists", specList);
		
		model.addAttribute("verify", 1);
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByAdminPlCurve(param);		
		model.addAttribute("pipeLineList", pipeLineList);
		
		return "pages/base/pl_curve_list";
	}

	/**
	 * 管道保护电位曲线图修改
	 * 
	 * @param model
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/pl_curve/edit", method = RequestMethod.GET)
	public String plCurve_edit(Model model, @RequestParam Integer id, @RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		PotentialCurve pc = baseService.queryPotentialCurveById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(pc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pc.getPl_section_id());

		model.addAttribute("pl", pc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pc", pc);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/pl_curve_edit";
	}

	/**
	 * 管道保护电位曲线图审核
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_curve/show_verify", method = RequestMethod.GET)
	public String plCurve_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String, Object>());
		PotentialCurve pc = baseService.queryPotentialCurveById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(pc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pc.getPl_section_id());

		List<PotentialCurveDetail> pcdList = pc.getDetailList();

		List<Double> p_earlyList = new ArrayList<Double>();
		List<Double> p_endList = new ArrayList<Double>();

		// 获取点电位数据
		for (PotentialCurveDetail pcd : pcdList) {
			p_earlyList.add((double) pcd.getP_early());
			p_endList.add((double) pcd.getP_end());
		}

		String[] rowKeys = { "上旬电位", "下旬电位" };
		// 生成电位图
		CategoryDataset dataset = DrawPic.createDataset(p_earlyList, p_endList);
		JFreeChart chart = DrawPic.createChart(dataset);
		String b = DrawPic.saveAsFile("pic", chart);

		/*
		 * ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		 * BufferedImage bufferImg = ImageIO.read(new File( fileDir +
		 * "/pic/pic.png")); ImageIO.write(bufferImg, "png", byteArrayOut);
		 * Image img = byteArrayOut.
		 */
		model.addAttribute("img", fileDir + "/pic/pic.png");
		model.addAttribute("pl", pc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pc", pc);
		model.addAttribute("verify", 1);

		return "pages/base/pl_curve_show";
	}

	@RequestMapping(value = "/pl_curve/verify_save", method = RequestMethod.POST)
	public @ResponseBody String plCurve_verify_save(Model model, HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object obj = request.getSession().getAttribute(ud.getId() + "rrIdList");
		if (obj != null) {
			List<Integer> rrIdList = (List<Integer>)obj;
			for (Integer rrId : rrIdList)
				//newBaseService.updateRunRecordVerifyStatus(rrId, ud.getId(), status, verify_desc);
				baseService.updatePotentialCurveVerifyStatus(rrId, ud.getId(), status, verify_desc);
			request.getSession().removeAttribute(ud.getId() + "rrIdList");
		}		
		/*if (status != null) {
			PotentialCurve p = basePotentialCurveDao.queryPotentialCurveById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的管道保护电位曲线图已审核通过！";
			} else {
				content = "您提交的管道保护电位曲线图未审核通过！";
			}
			saveTips(content, p.getCreater(), "/admin/base/pl_curve/show?id=" + id);
			// eventDao.updateEventStatus(status, message, event_id, type_id);
		}*/
		return JsonUtil.toJson("OK");
	}

	@RequestMapping(value = "/pl_curve/del", method = RequestMethod.GET)
	public String pl_curve_del(Model model, @RequestParam Integer id) {
		baseService.deletePotentialCurveById(id);
		return "redirect:/admin/base/pl_curve/list?del=1";
	}
}
