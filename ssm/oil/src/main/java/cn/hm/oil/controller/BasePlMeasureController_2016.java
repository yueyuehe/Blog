/**
 * 
 */
package cn.hm.oil.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.PotentialCurve_2016;
import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.PotentialMeasure_2016;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.ValvePatrol;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BasePlCurveService_2016;
import cn.hm.oil.service.BasePlMeasureService_2016;
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
 *         管道保护电位测量_管理
 * 
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping(value = "/admin/base/pl_measure/2016")
public class BasePlMeasureController_2016 {

	@Autowired
	private BasePlCurveService_2016 baseCureService;
	
	@Autowired
	private BasePlMeasureService_2016 baseService;
	
	@Autowired
	private NewBaseService baseService_new;
	
	@Autowired
	private BaseService baseService_old;

	@Autowired
	private UserService userService;
	
	
	/**
	 * 管道保护电位测量记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String plMeasure_create(Model model) {
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
		model.addAttribute("remarkWidth", SettingUtils.getCommonSetting("pl_measure.line.with"));
		
		return "pages/base/pl_measure_create_2016";
	}
	
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String plMeasure_save(Model model,
			@RequestParam String name,
			@RequestParam(required = false) Integer pm_id,
			@RequestParam Integer pl_id,
			@RequestParam String c_month, @RequestParam String created_by,
			@RequestParam String auditor, @RequestParam String[] no,@RequestParam String up_id,
			@RequestParam(required=false) String[] m_date, @RequestParam(required=false) Float[] potential,
			@RequestParam(required=false) String[] measurer,
			@RequestParam(required=false) String[] remark ,
			
			@RequestParam(required = false) Boolean all,
			@RequestParam(required = false) String s_c_month,
			@RequestParam(required = false) String s_pl_name,//搜索管线名称
			@RequestParam(required = false) Integer s_id,//搜索人的id
			@RequestParam(required = false) String s_user_name, // 搜索人的名字
			@RequestParam(required = false) Integer s_pl_id, //搜索管线id
			@RequestParam(required = false) Integer modify, //是否是修改
			@RequestParam(required=false) String status) {

		PotentialMeasure_2016 pm = new PotentialMeasure_2016();
		if(pm_id != null)
			pm.setId(pm_id);
		pm.setPl_id(pl_id);
		pm.setC_month(Integer.valueOf(c_month.replace("-", "")));
		pm.setCreated_by(created_by);
		pm.setStatus(Integer.parseInt(status));
		pm.setAuditor(auditor);
		pm.setUp_id(up_id);
		pm.setName(name);
		Date createTime = new Date();
		pm.setCreate_time(createTime);
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		pm.setCreater(ud.getId());
		List<PotentialMeasureDetail> pmdList = new ArrayList<PotentialMeasureDetail>();
		int i = 0;
		Map<String,Object> pCurMap = new HashMap<String,Object>();
		for (String n : no) {
			if(StringUtil.isBlank(n) == false)
			{
				PotentialMeasureDetail pmd = new PotentialMeasureDetail();
				
				pmd.setNo(n);
				Date date = DateFormatter.stringToDate(m_date[i],"yyyy-MM-dd");
				pmd.setM_date(date);
				pmd.setPotential(potential[i]);
				pmd.setMeasurer(measurer[i]);
				pmd.setRemark(remark[i]);
				pmd.setStatus(Integer.parseInt(status));
				pmdList.add(pmd);
				
				if(pCurMap.containsKey(n) == false)
				{
					Map<String,Object> data = new HashMap<String,Object>();
					pCurMap.put(n, data);
				}
				Map<String,Object> data = (Map<String, Object>) pCurMap.get(n);
				if(date.getDate() <= 15)
				{
					data.put("p1", potential[i]);
				}
				else{
					data.put("p2", potential[i]);
				}
				data.put("measurer", measurer[i]);
				data.put("remark", remark[i]);
				data.put("no", n);
			}
			i++;
		}
		
		String params = "";
		if(modify != null && modify.intValue() == 1)
		{
			params = CommonsMethod.putUrlParam(params, "s_id", s_id);
			params = CommonsMethod.putUrlParam(params, "all", all);
			if(s_pl_id != null && s_pl_id.intValue() > 0)
				params = CommonsMethod.putUrlParam(params, "s_pl_id", s_pl_id);
			params = CommonsMethod.putUrlParam(params, "s_c_month", s_c_month);
			params = CommonsMethod.putUrlParam(params, "s_user_name", s_user_name);
			params = CommonsMethod.putUrlParam(params, "s_pl_name", s_pl_name);
		}
		
		
		if (pmdList.size() > 0) {
			baseService.savePlMeasure(pm, pmdList);
		}
		else{
			return "redirect:/admin/base/pl_measure/2016/" + ((modify != null && modify.intValue() == 1)?"show"+params:"create");
		}
		
		/*PotentialMeasure_2016 savedPm = baseService.queryPotentialMeasureByCreate(createTime, ud.getId());
		if(savedPm == null || savedPm.getId() <= 0)
		{
			return "redirect:/admin/base/pl_measure/2016/" + ((modify != null && modify.intValue() == 1)?"show"+params:"create");
		}*/
		/***
		 * 将数据填充到potential_curve potential_curve_Detail表中	
		 */
		PotentialCurve_2016 pc = baseCureService.queryPotentialCurveByMeasureId(pm.getId());
		if(pc == null)
		{
			pc = new PotentialCurve_2016();
		}
		pc.setC_month(Integer.valueOf(c_month.replace("-", "")));
		pc.setPl_id(pl_id);
		pc.setCreater(ud.getId());
		pc.setUp_id(up_id);
		pc.setCreate_time(new Date());
		pc.setName(name);
		pc.setPl_measure_id(pm.getId());
		//需要将该月的所有数据查询出来		
		//Map<String, PotentialCurveDetail> mp = new HashMap<String,PotentialCurveDetail>();
		
		List<PotentialCurveDetail> pcds = new ArrayList<PotentialCurveDetail>();
		for( Object o : pCurMap.values())
		{
			Map<String,Object> obj = (Map<String,Object>)o;
			PotentialCurveDetail pcd = new PotentialCurveDetail();
			pcd.setNo((String)obj.get("no"));
			pcd.setMeasurer((String)obj.get("measurer"));
			pcd.setRemark((String)obj.get("remark"));
			if(obj.containsKey("p1"))
				pcd.setP_early((Float)obj.get("p1"));
			if(obj.containsKey("p2"))
				pcd.setP_end((Float)obj.get("p2"));
			pcds.add(pcd);
		}
		baseCureService.savePlCurve(pc, pcds);
		
		
		
		return "redirect:/admin/base/pl_measure/2016/" + ((modify != null && modify.intValue() == 1)?"show"+params:"create");
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String plMeasure_list(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required=false) Boolean all,
			@RequestParam(required = false) Integer s_pl_id, // 管线
			@RequestParam(required = false) String s_pl_name, // 管线名称
			@RequestParam(required = false) String s_user_name, // 用户名字
			@RequestParam(required = false) String s_c_month, // 时间			
			@RequestParam(required = false) Integer verify) {
		
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
		//规格查询条件
		
		if(all != null)
		{
			model.addAttribute("all", all);
			paramPipeline.put("all", all);
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
		
		if (!StringUtils.isBlank(s_c_month)) {
			param.put("c_month", Integer.valueOf(s_c_month.replace("-", "")));
			model.addAttribute("s_c_month", s_c_month);
		}
				
		if(verify != null && verify.intValue() > 0)
		{
			param.put("verify", verify);
			model.addAttribute("verify", verify);
		}
		
		

		List<SysUsers> users = baseService.queryUsers(param);
		model.addAttribute("users", users);
		
		//管线 下拉列表
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByAdminPlMeasure(paramPipeline);// .queryPipeLineByAdminRcComp(param);
		model.addAttribute("pipeLineList", pipeLineList);
				
		return "pages/base/pl_measure_list_2016";
	}
	
	
	/**
	 * 管道保护电位测量记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/show", method = {RequestMethod.GET,RequestMethod.POST})
	public String plMeasure_show(Model model, Authentication authentication,HttpServletRequest request,
								  @RequestParam(required = false) Integer s_id,
								  @RequestParam(required = false) Integer verify,
								  @RequestParam(required = false) Integer s_pl_id, // 管线
								  @RequestParam(required = false) String s_pl_name, // 管线名称
								  @RequestParam(required = false) String s_user_name, // 用户名字
								  @RequestParam(required=false) Boolean all,
								  @RequestParam(required = false) String s_c_month // 时间
			) {
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Map<String, Object> param = new HashMap<String, Object>();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param);
		model.addAttribute("role", role);
		Map<String, Object> paramPipeline = new HashMap<String, Object>();
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
		
		if (!StringUtils.isBlank(s_c_month)) {
			param.put("c_month", Integer.valueOf(s_c_month.replace("-", "")));
			model.addAttribute("s_c_month", s_c_month);
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

		if(s_id != null && s_id.intValue() > 0)
		{
			param.put("user_id", s_id);
			model.addAttribute("s_id", s_id);
		}
		if(verify != null && verify.intValue() > 0)
		{
			param.put("verify", verify);
			model.addAttribute("verify", verify);
		}
		
		if(all != null)
		{
			model.addAttribute("all", all);
			paramPipeline.put("all", all);
		}
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByAdminPlMeasure(paramPipeline);
		model.addAttribute("pipeLineList", pipeLineList);

		{
			Map<String, Object> param1 = new HashMap<String,Object>(param);
			param1.put("limit", 10000);
			param1.put("offset", 0);
			param1.put("order", 1);
			List<PotentialMeasure_2016> pmList1 = baseService.queryPlMeasures(param1, null);
			
			List<Integer> ppIdList = null;
			Object obj = request.getSession().getAttribute(ud.getId() + "ppIdList");
			if (obj != null) {
				ppIdList = (List<Integer>)obj;
			} else {
				request.getSession().removeAttribute(ud.getId() + "ppIdList");
				ppIdList = new ArrayList<Integer>();
			}
			if (!CollectionUtils.isEmpty(pmList1)) {
				for (PotentialMeasure_2016 ppd : pmList1) {
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
			List<PotentialMeasure_2016> pmList = baseService.queryPlMeasures(param, ps);
			if(pmList==null || pmList.size() == 0){
				return "pages/base/pl_measure_show_2016";
			}
			PotentialMeasure_2016 pm = pmList.get(0);
			pm.resetCanEidt(role);
			int s = pm.getDetailList().size();
			for(PotentialMeasureDetail e : pm.getDetailList())
			{
				e.setStatus(pm.getStatus());
			}
			if(s < 12)
			{
				for(int i = s; i < 30; ++i)
				{
					PotentialMeasureDetail e = new PotentialMeasureDetail();
					e.setStatus(-3);
					pm.getDetailList().add(e);
				}
			}
			model.addAttribute("pm", pm);
			model.addAttribute("pms", pmList);
		}
		
		return "pages/base/pl_measure_show_2016";
	}

	/**
	 * 管道保护电位测量记录导出
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exp", method = RequestMethod.GET)
	public String plMeasure_export(Model model, HttpServletRequest request,
			HttpServletResponse response,Authentication authentication,
			@RequestParam(required = false) Integer s_pl_id, 
			@RequestParam(required = false) String s_c_month,			
			@RequestParam(required = false) Integer s_id,//人员ID
			@RequestParam(required = false) Integer verify,
			@RequestParam(required = false) String s_pl_name, // 管线名称
			@RequestParam(required=false) Boolean all,
			@RequestParam(required = false) String s_user_name // 用户名字
			) {
		Map<String, Object> param = new HashMap<String, Object>();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param);
		model.addAttribute("role", role);
		if (!StringUtils.isBlank(s_c_month)) {
			param.put("c_month", Integer.valueOf(s_c_month.replace("-", "")));
		}
		
		if (s_id != null && s_id.intValue() > 0) {
			param.put("user_id", s_id);
		}
		
		if(all != null)
		{
			param.put("all", all);
		}
		
		if (s_pl_id != null && s_pl_id.intValue() > 0) {
			param.put("pl_id", s_pl_id);
		}		
		
		if (!StringUtils.isBlank(s_pl_name)) {
			param.put("name", s_pl_name);
		}
		
		if (!StringUtils.isBlank(s_user_name)) {
			param.put("user_name", s_user_name);
		}


		List<PotentialMeasure_2016> pmList = baseService.queryPlMeasures(param, null);
		if (pmList.size() == 0) {
			return "redirect:/admin/base/pl_measure/2016/list";
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
			sheet1.setColumnWidth(0, 7 * 256);
			sheet1.setColumnWidth(1, 7 * 256);
			sheet1.setColumnWidth(2, 5 * 256);
			sheet1.setColumnWidth(3, 7 * 256);
			sheet1.setColumnWidth(4, 16 * 256);
			sheet1.setColumnWidth(5, 3 * 256);
			sheet1.setColumnWidth(6, 7 * 256);
			sheet1.setColumnWidth(7, 7 * 256);
			sheet1.setColumnWidth(8, 5 * 256);
			sheet1.setColumnWidth(9, 7 * 256);
			sheet1.setColumnWidth(10, 16 * 256);
			sheet1.setColumnWidth(11, 3 * 256);
			sheet1.setColumnWidth(12, 7 * 256);
			sheet1.setColumnWidth(13, 7 * 256);
			sheet1.setColumnWidth(14, 5 * 256);
			sheet1.setColumnWidth(15, 7 * 256);
			sheet1.setColumnWidth(16, 16 * 256);
			
			int row_index = 0;
			for (PotentialMeasure_2016 pm : pmList) {
				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 16));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 0, 4));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 6, 9));
				
				// 新建行
				HSSFRow row = sheet1.createRow(row_index);
				row_index++;
				// 新建单元格
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue("管道保护电位测量记录");
				// 设置单元格格式
				cell.setCellStyle(cellStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row = sheet1.createRow(row_index);
				row_index++;
				// 设置行高度
				row.setHeightInPoints(20);
				cell = row.createCell(0);
				cell.setCellValue("管线名称：" + pm.getPl_name());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(6);
				cell.setCellValue("管线规格: " + pm.getName());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(16);
				cell.setCellValue(pm.getC_month().toString().substring(0, 4)
						+ " 年 " + pm.getC_month().toString().substring(4, 6)
						+ " 月 ");
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

								
				Integer sz = 10;
				if((pm.getDetailList().size()/3) > 10)
					sz = pm.getDetailList().size()/3;
				if((pm.getDetailList().size()%3) > 0) {
					sz++;
				}
				
				// 添加边框线
				int end_row = 0;
				for (int rown = 0; rown <= sz; rown++) { //(pmd.getDetailList().size()/2)+4
					row = sheet1.createRow(rown+row_index);
					row.setHeightInPoints((float)25.5);
					for (int celln = 0; celln < 17; celln++) {
						if(celln == 5 || celln == 11) {
							continue;
						}
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown+row_index;
				}

				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(30);
				cell = row.getCell(0);
				cell.setCellValue("测量日期");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(1);
				cell.setCellValue("测试桩编号");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("电位(-V)");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				cell.setCellValue("测量人");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(4);
				cell.setCellValue("备注");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(6);
				cell.setCellValue("测量日期");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(7);
				cell.setCellValue("测试桩编号");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(8);
				cell.setCellValue("电位(-V)");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(9);
				cell.setCellValue("测量人");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(10);
				cell.setCellValue("备注");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(12);
				cell.setCellValue("测量日期");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(13);
				cell.setCellValue("测试桩编号");
				// cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(14);
				cell.setCellValue("电位(-V)");
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
			
				// 填写数据
				int count = 0;
				int indexl = row_index;
				int indexr = row_index;
				int indexc = row_index;
				for (PotentialMeasureDetail pd : pm.getDetailList()) {
					if (count < sz) {
						row = sheet1.getRow(indexl++);
						System.out.println("index == " + indexl);
						cell = row.getCell(0);
						// cell.setCellStyle(dataStyle);
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

						cell = row.getCell(1);
						cell.setCellValue(pd.getNo());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(2);
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(pd.getPotential()==null?"":pd.getPotential()+"");

						cell = row.getCell(3);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getMeasurer());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(4);
						cell.setCellStyle(dataStyle);
						if (pd.getRemark() != null) {
							cell.setCellValue(pd.getRemark());
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}
					} else if(count >= sz && count < 2*sz) {
						System.out.println("index == " + indexc);
						row = sheet1.getRow(indexc++);
						System.out.print("row is NUll:" + (row == null));
						cell = row.getCell(6);
						// cell.setCellStyle(dataStyle);
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

						cell = row.getCell(7);
						cell.setCellValue(pd.getNo());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(7);
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(pd.getPotential()==null?"":pd.getPotential()+"");

						cell = row.getCell(9);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(pd.getMeasurer());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(10);
						cell.setCellStyle(dataStyle);
						if (pd.getRemark() != null) {
							cell.setCellValue(pd.getRemark());
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}
					} else {
						row = sheet1.getRow(indexr++);
						System.out.println("index == " + indexr);
						cell = row.getCell(12);
						// cell.setCellStyle(dataStyle);
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

						cell = row.getCell(13);
						cell.setCellValue(pd.getNo());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);

						cell = row.getCell(14);
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(pd.getPotential()==null?"":pd.getPotential()+"");

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
				
				sheet1.addMergedRegion(new CellRangeAddress(sz+row_index, sz+row_index, 0, 2));
				//尾部
				row = sheet1.createRow(sz+row_index);
				row.setHeightInPoints(18);
				cell = row.createCell(0);
				cell.setCellValue("填报人: " + pm.getCreated_by());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(16);
				cell.setCellValue("审核人: " + pm.getAuditor() + "");
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

	@RequestMapping(value = "/verify_save", method = RequestMethod.POST)
	public @ResponseBody String plMeasure_verify_save(Model model,HttpServletRequest request,
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Object obj = request.getSession().getAttribute(ud.getId() + "ppIdList");
		if (obj != null) {
			List<Integer> ppIdList = (List<Integer>)obj;
			for (Integer ppId : ppIdList)
				baseService.updatePlMeasureVerifyStatus(ppId, ud.getId(), status, verify_desc);
			request.getSession().removeAttribute(ud.getId() + "ppIdList");
		}
		/*if(status!=null){
			PotentialMeasure p = basePotentialMeasureDao.queryPotentialMeasureById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的管道保护电位测量记录已审核通过！";
			} else {
				content = "您提交的管道保护电位测量记录未审核通过！";
			}
				saveTips(content, p.getCreater(), "/admin/base/pl_measure/show?id=" + id);
				//eventDao.updateEventStatus(status, message, event_id, type_id);
			}*/
		return JsonUtil.toJson("OK");
	}	
}
