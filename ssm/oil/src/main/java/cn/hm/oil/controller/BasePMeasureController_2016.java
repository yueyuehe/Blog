package cn.hm.oil.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.jsoup.helper.StringUtil;
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
import cn.hm.oil.domain.PerformanceMeasure;
import cn.hm.oil.domain.PerformanceMeasureDetail;
import cn.hm.oil.domain.PerformanceMeasure_2016;
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.ValvePatrol;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BasePMeasureService_2016;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.NewBaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.CommonsMethod;
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
@RequestMapping(value = "/admin/base/p_measure/2016/")
public class BasePMeasureController_2016 {

	@Autowired
	private BasePMeasureService_2016 baseService;
	
	@Autowired
	private NewBaseService baseService_new;
	
	@Autowired
	private BaseService baseService_old;

	@Autowired
	private UserService userService;

	/**
	 * 绝缘接头(法兰)性能测量记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/create", method = {RequestMethod.GET,RequestMethod.POST})
	public String pMeasure_create(Model model
			,@RequestParam(required=false) Integer pl//管线id
			,@RequestParam(required=false) String pl_name//管线名字
			,@RequestParam(required=false) Integer newPage//新开一页
			) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);

		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		if(newPage == null || newPage.intValue() != 1)
		{
			PerformanceMeasure_2016 pm = baseService.queryLastPMeasureByUserId(ud.getId(),pl);
			if(pm != null)
			{
				model.addAttribute("pm", pm);
				model.addAttribute("pl", pm.getPl_id());
				model.addAttribute("ppLength", pm.getDetailList().size());
				model.addAttribute("id", pm.getId());
				model.addAttribute("pl_name", pm.getName());
			}
			else
			{
				model.addAttribute("pl", pl==null?0:pl);
				model.addAttribute("ppLength", 0);
				model.addAttribute("pl_name", pl_name);
			}
		}
		else
		{
			model.addAttribute("pl", pl==null?0:pl);
			model.addAttribute("ppLength", 0);
			model.addAttribute("pl_name", pl_name);
		}
		return "pages/base/p_measure_create_2016";
	}
	
	/**
	 * 绝缘接头(法兰)性能测量记录保存
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String pMeasure_save(Model model,
			@RequestParam(required = false) Integer pm_id,@RequestParam String name, 
			@RequestParam Integer pl_id,@RequestParam String jinzhan, @RequestParam Integer m_year,
			@RequestParam String created_by, @RequestParam String auditor,@RequestParam String up_id,
			@RequestParam String[] position, 
			@RequestParam String[] month_1,@RequestParam String[] month_2, @RequestParam String[] month_3,
			@RequestParam String[] month_4, @RequestParam String[] month_5,@RequestParam String[] month_6,
			@RequestParam String[] month_7,@RequestParam String[] month_8, @RequestParam String[] month_9,
			@RequestParam String[] month_10, @RequestParam String[] month_11,@RequestParam String[] month_12,
			
			@RequestParam(required = false) Boolean all,//搜索管线名称
			@RequestParam(required = false) String s_pl_name,//搜索管线名称
			@RequestParam(required = false) Integer s_id,//搜索人的id
			@RequestParam(required = false) String s_user_name, // 搜索人的名字
			@RequestParam(required = false) Integer s_pl_id, //搜索管线id
			@RequestParam(required = false) String s_year, //搜索管线id
			@RequestParam(required = false) Integer modify, //是否是修改
			
			@RequestParam String status,
			@RequestParam(required = false) Integer newPage) {
		PerformanceMeasure_2016 pm = new PerformanceMeasure_2016();
		if(pm_id != null && pm_id.intValue() > 0)
			pm.setId(pm_id);
		pm.setAuditor(auditor);
		pm.setCreated_by(created_by);
		pm.setJinzhan(jinzhan);
		pm.setM_year(m_year);
		pm.setPl_id(pl_id);
		pm.setName(name);
		pm.setStatus(0);
		pm.setUp_id(up_id);
		pm.setStatus(Integer.parseInt(status));
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		pm.setCreater(ud.getId());
		
		List<PerformanceMeasureDetail> pmdList = new ArrayList<PerformanceMeasureDetail>();
		int i = 0;
		for (String ps : position) {
			if(StringUtil.isBlank(ps))
			{
				i += 3;
				continue;
			}
			for(int j = 0; j < 3; ++j)
			{
				PerformanceMeasureDetail pmd = new PerformanceMeasureDetail();
				pmd.setType(j+1);
				pmd.setPosition(ps);				
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
				pmd.setStatus(Integer.parseInt(status));
				pmdList.add(pmd);			
				i++;
			}
		}
		if (pmdList.size() > 0) {
			baseService.savePMeasure(pm, pmdList);
		}
		
		String params = "";
		if(modify != null && modify.intValue() == 1)
		{
			params = CommonsMethod.putUrlParam(params, "s_id", s_id);
			params = CommonsMethod.putUrlParam(params, "all", all);
			if(s_pl_id != null && s_pl_id.intValue() > 0)
				params = CommonsMethod.putUrlParam(params, "s_pl_id", s_pl_id);
			params = CommonsMethod.putUrlParam(params, "s_year", s_year);
			params = CommonsMethod.putUrlParam(params, "s_user_name", s_user_name);
			params = CommonsMethod.putUrlParam(params, "s_pl_name", s_pl_name);
		}
		else{
			params = CommonsMethod.putUrlParam(params, "newPage", newPage);
		}
		
		return "redirect:/admin/base/p_measure/2016/" +  ((modify != null && modify.intValue() == 1)?"show":"create") +params;
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
	@RequestMapping(value = "/list", method = { RequestMethod.GET,RequestMethod.POST })
	public String pMeasure_list(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) String s_year,
			@RequestParam(required = false) String s_pl_name,//搜索管线名称
			@RequestParam(required = false) Integer s_id,//搜索人的id
			@RequestParam(required = false) String s_user_name, // 搜索人的名字
			@RequestParam(required = false) Integer s_pl_id, //搜索管线id
			
			@RequestParam(required=false) Boolean all,
			@RequestParam(required = false) Integer verify
			) {
		//获取用户角色ID
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//规格查询条件
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> paramPipeline = new HashMap<String, Object>();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param, all);
		model.addAttribute("role", role);
		
		if(role==2)//技术工
		{
			if(verify != null && verify.intValue() == 1)
			{
				param.put("status", 0);
				paramPipeline.put("status", 0);
			}
			else{
				param.put("status", "-1,0,1");
				paramPipeline.put("status", "-1,0,1");
			}
			
			paramPipeline.put("up_id", ud.getId());
		}
		else if(role==3)//维护工		
		{			
			paramPipeline.put("user_id", ud.getId());
		}
		
		if (s_pl_id != null && s_pl_id.intValue() > 0) {
			param.put("pl_id", s_pl_id);
			model.addAttribute("s_pl_id", s_pl_id);
		}

		if(all != null)
		{
			model.addAttribute("all", all);
			paramPipeline.put("all", all);
		}

		if (!StringUtils.isBlank(s_pl_name)) {
			param.put("name", s_pl_name);
			model.addAttribute("s_pl_name", s_pl_name);
		}
		
		if (!StringUtils.isBlank(s_user_name)) {
			param.put("user_name", s_user_name);
			model.addAttribute("s_user_name", s_user_name);
		}
		
		if (!StringUtils.isBlank(s_year)) {
			param.put("year", Integer.valueOf(s_year));
			model.addAttribute("s_year", s_year);
		}
		
		if(verify != null && verify.intValue() > 0)
		{
			param.put("verify", verify);
			model.addAttribute("verify", verify);
		}
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByAdminPMeasure(paramPipeline);
		model.addAttribute("pipeLineList", pipeLineList);

		List<SysUsers> users = baseService.queryUsers(param);
		model.addAttribute("users", users);
		
		return "pages/base/p_measure_list_2016";
	}
	
	/**
	 * 绝缘接头(法兰)性能测量记录详细查看
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/show", method = {RequestMethod.GET,RequestMethod.POST})
	public String pMeasure_show(Model model,Authentication authentication,HttpServletRequest request,
							  @RequestParam(required = false) Integer verify,
							  @RequestParam(required=false) Boolean all,
							  @RequestParam(required = false) String s_year,
							  @RequestParam(required = false) String s_pl_name,//搜索管线名称
							  @RequestParam(required = false) Integer s_id,//搜索人的id
							  @RequestParam(required = false) String s_user_name, // 搜索人的名字
							  @RequestParam(required = false) Integer s_pl_id //搜索管线id
							  
							  ) {
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> paramPipeline = new HashMap<String, Object>();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param, all);
		model.addAttribute("role", role);
		if(role==2)//技术工
		{
			if(verify != null && verify.intValue() == 1)
			{
				param.put("status", 0);
				paramPipeline.put("status", 0);
			}
			else{
				param.put("status", "-1,0,1");
				paramPipeline.put("status", "-1,0,1");
			}
			
			paramPipeline.put("up_id", ud.getId());
		}
		else if(role==3)//维护工		
		{			
			paramPipeline.put("user_id", ud.getId());
		}
		
		if(verify != null && verify.intValue() > 0)
		{
			param.put("verify", verify);
			model.addAttribute("verify", verify);
		}
		
		if(!StringUtils.isBlank(s_year)){
			param.put("year", Integer.valueOf(s_year));
			model.addAttribute("s_year", s_year);
		}
		
		if(s_id != null && s_id.intValue() > 0)
		{
			param.put("user_id", s_id);
			model.addAttribute("s_id", s_id);
		}
		
		if (s_pl_id != null && s_pl_id.intValue() > 0) {
			param.put("pl_id", s_pl_id);
			model.addAttribute("s_pl_id", s_pl_id);
		}		
		
		if (!StringUtils.isBlank(s_pl_name)) {
			param.put("name", s_pl_name);
			model.addAttribute("s_pl_name", s_pl_name);
		}
		
		if (!StringUtils.isBlank(s_user_name)) {
			param.put("user_name", s_user_name);
			model.addAttribute("s_user_name", s_user_name);
		}
		
		if(all != null)
		{
			model.addAttribute("all", all);
			paramPipeline.put("all", all);
		}
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByAdminPMeasure(paramPipeline);

		//获取用户角色ID

		{
			Map<String, Object> param1 = new HashMap<String,Object>(param);
			param1.put("limit", 10000);
			param1.put("offset", 0);
			param1.put("order", 1);
			List<PerformanceMeasure_2016> detailList1 = baseService.queryPMeasures(param1, null);
			
			List<Integer> ppIdList = null;
			Object obj = request.getSession().getAttribute(ud.getId() + "ppIdList");
			if (obj != null) {
				ppIdList = (List<Integer>)obj;
			} else {
				request.getSession().removeAttribute(ud.getId() + "ppIdList");
				ppIdList = new ArrayList<Integer>();
			}
			if (!CollectionUtils.isEmpty(detailList1)) {
				for (PerformanceMeasure_2016 ppd : detailList1) {
					if (!ppIdList.contains(ppd.getId())) {
						ppIdList.add(ppd.getId());
					}
				}
				request.getSession().setAttribute(ud.getId() + "ppIdList", ppIdList);
			}
		}
		{
			PageSupport ps = PageSupport.initPageSupport(request);
			ps.setPageSize(1);
			List<PerformanceMeasure_2016> pmd = baseService.queryPMeasures(param, ps);
			if(pmd==null || pmd.size() == 0){
				return "pages/base/p_measure_show_2016";
			}
			for(PerformanceMeasure_2016 p : pmd)
			{
				p.resetCanEidt(role);           
			}
			PerformanceMeasure_2016 pm = pmd.get(0);
			int s = pm.getDetailList().size();
			for(PerformanceMeasureDetail e : pm.getDetailList())
			{
				e.setStatus(pm.getStatus());
			}
			if(s < 18)
			{
				for(int i = s; i < 18; ++i)
				{
					PerformanceMeasureDetail e = new PerformanceMeasureDetail();
					e.setStatus(-3);
					pm.getDetailList().add(e);
				}
			}
			model.addAttribute("pm", pm);
		}
		model.addAttribute("pipeLineList", pipeLineList);
		
		return "pages/base/p_measure_show_2016";
	}
	
	
	@RequestMapping(value = "/exp", method = RequestMethod.GET)
	public String pMeasure_export(Model model, HttpServletRequest request,
			HttpServletResponse response,Authentication authentication,			
			@RequestParam(required = false) String s_year,
			@RequestParam(required = false) String s_pl_name,//搜索管线名称
			@RequestParam(required = false) Integer s_id,//搜索人的id
			@RequestParam(required = false) Boolean all,
			@RequestParam(required = false) String s_user_name, // 搜索人的名字
			@RequestParam(required = false) Integer s_pl_id //搜索管线id
			) {
		Map<String, Object> param = new HashMap<String, Object>();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param);
		model.addAttribute("role", role);
		
		if(!StringUtils.isBlank(s_year)){
			param.put("year", Integer.valueOf(s_year));
		}
		
		if(s_id != null && s_id.intValue() > 0)
		{
			param.put("user_id", s_id);
		}
		
		if (s_pl_id != null && s_pl_id.intValue() > 0) {
			param.put("pl_id", s_pl_id);
		}		
		
		if(all != null)
		{
			param.put("all", all);
		}
		
		if (!StringUtils.isBlank(s_pl_name)) {
			param.put("name", s_pl_name);
		}
		
		if (!StringUtils.isBlank(s_user_name)) {
			param.put("user_name", s_user_name);
		}
		
		List<PerformanceMeasure_2016> pmList = baseService.queryPMeasures(param, null);

		if (pmList.size() == 0) {
			return "redirect:/admin/base/p_measure/2016/list";
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

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");

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
			for (PerformanceMeasure_2016 pm : pmList) {
				// 合并单元格
				sheet.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 13));
				sheet.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 0, 2));
				sheet.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 11, 13));
				sheet.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 4, 8));
				//sheet.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 0, 0));
				
				// 设置标题格
				HSSFRow row = sheet.createRow(row_index);
				row_index++;
				row.setHeightInPoints(32);
				cell = row.createCell(0);
				cell.setCellValue("绝缘接头（法兰）性能测量记录");
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float) 23);
				cell = row.createCell(0);
				cell.setCellValue("井(站)" + pm.getJinzhan());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(4);
				//cell.setCellValue("管线名称及规格: " + pm.getPl_name() + "/" + pm.getPl_spec_name());
				cell.setCellStyle(dataStyle2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(11);
				cell.setCellValue(pm.getM_year() + "年");
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);


				// 查询数据库
				//PerformanceMeasure pfm = newBaseService.queryPerformanceMeasureById(pm.getId());
				
				int x = 15;
				
				if(pm.getDetailList().size() > 15) {
					x = pm.getDetailList().size();
				}
				
				int end_row = 0;
				for (int rown = 0; rown < x + 1; rown++) {
					row = sheet.createRow(rown+row_index);
					row.setHeightInPoints((float) 25);
					for (int celln = 0; celln < 14; celln++) {
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown+row_index;
				}

				row = sheet.getRow(row_index);
				row_index++;
				
				row.setHeightInPoints((float) 22);
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
				if(pm.getDetailList().size() > 15) {
					int i = 0;
					while(i < pm.getDetailList().size()/3) {
						sheet.addMergedRegion(new CellRangeAddress(row_index+(3*i), row_index+(3*i)+2, 0, 0));
						 i++;
					}
					sheet.addMergedRegion(new CellRangeAddress(row_index+(3*i), row_index+(3*i), 0, 6));
					sheet.addMergedRegion(new CellRangeAddress(row_index+(3*i), row_index+(3*i), 7, 13));
					
					row = sheet.createRow(row_index+(3*i));
					row.setHeightInPoints(20);
					cell = row.createCell(0);
					cell.setCellValue("填报人：" + pm.getCreated_by());
					cell.setCellStyle(titleStyle);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.createCell(7);
					cell.setCellValue("审核人：" + pm.getAuditor());
					cell.setCellStyle(titleStyle1);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				} else {

					sheet.addMergedRegion(new CellRangeAddress(row_index, row_index+2, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+3, row_index+5, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+6, row_index+8, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+9, row_index+11, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+12, row_index+14, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(row_index+15, row_index+15, 0, 6));
					sheet.addMergedRegion(new CellRangeAddress(row_index+15, row_index+15, 7, 13));
					
					row = sheet.createRow(row_index+15);
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
				for (PerformanceMeasureDetail pmd : pm.getDetailList()) {
					row = sheet.getRow(row_index++);
					if ((count % 3) == 0) {
						/*cell = row.getCell(1);
						if (pmd.getType() == 1) {
							cell.setCellValue("通电点电位(-V)");
							cell.setCellStyle(dataStyle1);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						} else {
							cell.setCellValue("末保护端(-V)");
							cell.setCellStyle(dataStyle1);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}*/
						cell = row.getCell(0);
						cell.setCellValue(pmd.getPosition());
						cell.setCellStyle(dataStyle1);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell = row.getCell(1);
						cell.setCellValue("通电点电位(-V)");
						cell.setCellStyle(dataStyle1);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					} else if((count % 3) == 1) {
						cell = row.getCell(1);
						cell.setCellValue("保护端(-V)");
						cell.setCellStyle(dataStyle1);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					} else {
						cell = row.getCell(1);
						cell.setCellValue("末保护端(-V)");
						cell.setCellStyle(dataStyle1);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					cell = row.getCell(2);
					cell.setCellValue(pmd.getMonth_1()==null?"":
						pmd.getMonth_1()+"");
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
					count++;
				}
				row_index = end_row + 2;
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
	
	@RequestMapping(value = "/verify_save", method = RequestMethod.POST)
	public @ResponseBody String pMeasure_verify_save(Model model,HttpServletRequest request, 
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();		
		Object obj = request.getSession().getAttribute(ud.getId() + "ppIdList");
		if (obj != null) {
			List<Integer> ppIdList = (List<Integer>)obj;
			for (Integer ppId : ppIdList)				
				baseService.updatePerformanceMeasureVerifyStatus(ppId, ud.getId(), status, verify_desc);
			request.getSession().removeAttribute(ud.getId() + "ppIdList");
		}
		//if(status!=null){
		//	PerformanceMeasure p = basePerformanceMeasureNewDao.queryPerformanceMeasureById(id);
		//	String content;
		//	if (status.intValue() == 1) {
		//		content = "您提交的绝缘接头(法兰)性能测量记录已审核通过！";
		//	} else {
		//		content = "您提交的绝缘接头(法兰)性能测量记录未审核通过！";
		//	}
		//		saveTips(content, p.getCreater(), "/admin/base/p_measure/show?id=" + id);
		//		//eventDao.updateEventStatus(status, message, event_id, type_id);
		//	}
		return JsonUtil.toJson("OK");
	}
	
}
