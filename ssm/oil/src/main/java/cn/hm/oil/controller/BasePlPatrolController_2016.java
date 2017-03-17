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
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
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
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PipelinePatrol_2016;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BasePipelinePatrolService_2016;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.NewBaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.CommonsMethod;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;


/**
 * @author Administrator
 * 
 * 管道巡检工作记录(新)_管理
 * 
 */
@Controller
@RequestMapping(value = "/admin/base/pl_patrol/2016")
public class BasePlPatrolController_2016 {
	
	@Autowired
	private NewBaseService baseService_new;
	
	@Autowired
	private BasePipelinePatrolService_2016 baseService;
	
	@Autowired
	private BaseService baseService_old;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 管道巡检工作记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/create", method = {RequestMethod.GET,RequestMethod.POST})
	public String plPatrol_create(Model model
			,@RequestParam(required=false) Integer pl//管线id
			,@RequestParam(required=false) String pl_name//管线名字
			,@RequestParam(required=false) Integer newPage//新开一页
			){
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);

		model.addAttribute("remarkWidth", SettingUtils.getCommonSetting("pl_protrol.line.with"));
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		if(newPage == null || newPage.intValue() != 1)
		{
			PipelinePatrol_2016 patrol = baseService.queryLastPatrolByUserId(ud.getId(),pl);
			if(patrol != null)
			{
				model.addAttribute("pp", patrol);
				model.addAttribute("pl", patrol.getPl_id());
				model.addAttribute("ppLength", patrol.getDetailList().size());
				model.addAttribute("id", patrol.getId());
				model.addAttribute("pl_name", patrol.getName());
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
		
		return "pages/base/pl_patrol_create_2016";
	}
	
	/**
	 * 管道巡检工作记录查询
	 * 
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param p_month
	 * @param del
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String plPatrol_list(Model model, HttpServletRequest request,Authentication authentication, 
			@RequestParam(required = false) String s_p_month,
			@RequestParam(required = false) String s_pl_name,//搜索管线名称
			@RequestParam(required = false) Integer s_id,//搜索人的id
			@RequestParam(required = false) String s_user_name, // 搜索人的名字
			@RequestParam(required = false) Integer s_pl_id, //搜索管线id
			
			@RequestParam(required=false) Boolean all,
			@RequestParam(required = false) Integer verify
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
		
		if (!StringUtils.isBlank(s_p_month)) {
			param.put("p_month", Integer.valueOf(s_p_month.replace("-", "")));
			model.addAttribute("p_month", s_p_month);
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

		//判读用户是否是维护工，维护工只能查看自己的数据
				
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByAdminPatrol(paramPipeline);
		model.addAttribute("pipeLineList", pipeLineList);
		
		List<SysUsers> users = baseService.queryUsers(param);

		model.addAttribute("users", users);

		return "pages/base/pl_patrol_list_2016";
	}
	
	/**
	 * 管道巡检工作记录保存
	 * 
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String plPatrol_save(Model model, HttpServletRequest request, 
			@RequestParam(required = false) Integer pp_id, @RequestParam Integer pl_id,//页面管线id
			@RequestParam(required = false) Integer pl,//页面管线id
			@RequestParam(required = false) String jinzhan, @RequestParam(required=false) String patroler,	@RequestParam String name, 		
			@RequestParam(required=false) String patrol_no,
			
			@RequestParam(required = false) String[] p_date,  @RequestParam(required = false) String[] remark,@RequestParam(required = false) Integer[] sign,
			@RequestParam(required = false) String[] content_1,@RequestParam(required = false) String[] content_2,@RequestParam(required = false) String[] content_3,@RequestParam(required = false) String[] content_4,
			@RequestParam(required = false) String[] content_5,@RequestParam(required = false) String[] content_6,@RequestParam(required = false) String[] content_7,@RequestParam(required = false) String[] content_8,
			@RequestParam(required = false) String[] content_9,@RequestParam(required = false) String[] content_10,@RequestParam(required = false) String[] content_11,@RequestParam(required = false) String[] content_12,
			@RequestParam(required = false) String[] content_13,@RequestParam(required = false) String[] content_14,@RequestParam(required = false) String[] content_15,@RequestParam(required = false) String[] content_16,
			@RequestParam String up_id,
			
			@RequestParam(required = false) String s_p_month,
			@RequestParam(required = false) Boolean all,
			@RequestParam(required = false) String s_pl_name,//搜索管线名称
			@RequestParam(required = false) Integer s_id,//搜索人的id
			@RequestParam(required = false) String s_user_name, // 搜索人的名字
			@RequestParam(required = false) Integer s_pl_id, //搜索管线id
			@RequestParam(required = false) Integer modify, //是否是修改
			@RequestParam String status, @RequestParam(required = false) Integer newPage
			){
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		PipelinePatrol_2016 pp = new PipelinePatrol_2016();
		pp.setJinzhan(jinzhan);
		pp.setPl_id(pl_id);
		pp.setName(name);
		pp.setCreater(ud.getId());
		pp.setPatroler(patroler);
		if(pp_id != null && pp_id.intValue() > 0)
			pp.setId(pp_id);
		pp.setPatrol_no(patrol_no);
		pp.setUp_id(up_id);
		pp.setStatus(Integer.parseInt(status));//添加状态值
		/*List<String> imgs = null;
	
		try {
			imgs = DataUtil.uploadImg(request, "annex");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "图片上传错误，请检查图片是否正确！");
			return "pages/base/pl_patrol_create_2016";
		}*/
		
		List<PipelinePatrolDetail> ppdList = new ArrayList<PipelinePatrolDetail>();
		int i = 0;
		int used = -1;
		for(Integer s : sign)
		{
			boolean canEdit = s.intValue() != 0;//该行可编辑
			if(canEdit)
			{
				++used;
			}				
			do{
				if((canEdit && StringUtils.isBlank(p_date[used])) || (canEdit==false && StringUtils.isBlank(remark[i])))
					break;
				if ((canEdit && !StringUtils.isBlank(p_date[used])) || !StringUtils.isBlank(remark[i])) {
					PipelinePatrolDetail ppd = new PipelinePatrolDetail();
					if (canEdit && !StringUtils.isBlank(p_date[used])) {
						ppd.setP_date(DateFormatter.stringToDate(p_date[used], "yyyy-MM-dd"));
						ppd.setContent_1(content_1[used]);
						ppd.setContent_2(content_2[used]);
						ppd.setContent_3(content_3[used]);
						ppd.setContent_4(content_4[used]);
						ppd.setContent_5(content_5[used]);
						ppd.setContent_6(content_6[used]);
						ppd.setContent_7(content_7[used]);
						ppd.setContent_8(content_8[used]);
						ppd.setContent_9(content_9[used]);
						ppd.setContent_10(content_10[used]);
						ppd.setContent_11(content_11[used]);
						ppd.setContent_12(content_12[used]);
						ppd.setContent_13(content_13[used]);
						ppd.setContent_14(content_14[used]);
						ppd.setContent_15(content_15[used]);
						ppd.setContent_16(content_16[used]);
					}
					//添加状态值,-2表示草稿 -1表示待审核
					ppd.setStatus(Integer.parseInt(status));
					
					ppd.setRemark(StringUtils.isBlank(remark[i]) ? "" : remark[i]);
					ppdList.add(ppd);
				}
			}while(false);
			++i;
		}	 
		if (ppdList.size() > 0) {
			baseService.savePlPatrol(pp, ppdList);
		}
		
		String params = "";
		if(modify != null && modify.intValue() == 1)
		{
			params = CommonsMethod.putUrlParam(params, "s_id", s_id);
			params = CommonsMethod.putUrlParam(params, "all", all);
			if(s_pl_id != null && s_pl_id.intValue() > 0)
				params = CommonsMethod.putUrlParam(params, "s_pl_id", s_pl_id);
			params = CommonsMethod.putUrlParam(params, "s_p_month", s_p_month);
			params = CommonsMethod.putUrlParam(params, "s_user_name", s_user_name);
			params = CommonsMethod.putUrlParam(params, "s_pl_name", s_pl_name);
		}
		else{
			params = CommonsMethod.putUrlParam(params, "newPage", newPage);
			params = CommonsMethod.putUrlParam(params, "pl", pl);
		}
		
		return "redirect:/admin/base/pl_patrol/2016/" +  ((modify != null && modify.intValue() == 1)?"show":"create") +params;
	}
		
	/**
	 * 管道巡检工作记录查询
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/show", method = {RequestMethod.GET,RequestMethod.POST})
	public String plPatrol_show(Model model, Authentication authentication,HttpServletRequest request,
							  @RequestParam(required = false) Integer verify,		
							  @RequestParam(required=false) Boolean all,
								@RequestParam(required = false) String s_p_month,
								@RequestParam(required = false) String s_pl_name,//搜索管线名称
								@RequestParam(required = false) Integer s_id,//搜索人的id
								@RequestParam(required = false) String s_user_name, // 搜索人的名字
								@RequestParam(required = false) Integer s_pl_id //搜索管线id
			) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
		
		model.addAttribute("remarkWidth", SettingUtils.getCommonSetting("pl_protrol.line.with"));
		if(!StringUtils.isBlank(s_p_month)){
			param.put("p_month", Integer.valueOf(s_p_month.replace("-", "")));
			model.addAttribute("p_month", s_p_month);
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
		List<BasePipeline> pipeLineList = baseService.queryPipeLineByAdminPatrol(paramPipeline);

		
		{
			Map<String, Object> param1 = new HashMap<String,Object>(param);
			param1.put("limit", 10000);
			param1.put("offset", 0);
			param1.put("order", 1);
			List<PipelinePatrol_2016> ppList1 = baseService.queryPatrols(param1, null);
			//pp.setDetailList(detailList);
			
			List<Integer> ppIdList = null;
			Object obj = request.getSession().getAttribute(ud.getId() + "ppIdList");
			if (obj != null) {
				ppIdList = (List<Integer>)obj;
			} else {
				request.getSession().removeAttribute(ud.getId() + "ppIdList");
				ppIdList = new ArrayList<Integer>();
			}
			if (!CollectionUtils.isEmpty(ppList1)) {
				for (PipelinePatrol_2016 ppd : ppList1) {
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
			List<PipelinePatrol_2016> pps = baseService.queryPatrols(param, ps);
			if(pps==null || pps.size() == 0){
				return "pages/base/pl_patrol_show_2016";
			}
			
			PipelinePatrol_2016 pp = pps.get(0);
			pp.resetCanEidt(role);
			int s = pp.getDetailList().size();
			for(PipelinePatrolDetail e : pp.getDetailList())
			{
				e.setStatus(pp.getStatus());
			}
			if(s < 12)
			{
				for(int i = s; i < 12; ++i)
				{
					PipelinePatrolDetail e = new PipelinePatrolDetail();
					e.setStatus(-3);
					pp.getDetailList().add(e);
				}
			}
			model.addAttribute("pp", pp);
			model.addAttribute("pps", pps);
		}
		
		
		model.addAttribute("pipeLineList", pipeLineList);
		
		return "pages/base/pl_patrol_show_2016";
	}
	
	/**
	 * 管道巡检工作记录导出
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param pl
	 * @param section
	 * @param spec
	 * @param p_month
	 * @return
	 */
	@RequestMapping(value = "/exp", method = RequestMethod.GET)
	public String plPatrol_export(Model model, Authentication authentication,
			 HttpServletRequest request, 
			 HttpServletResponse response,
			 @RequestParam(required = false) String s_p_month,
				@RequestParam(required = false) String s_pl_name,//搜索管线名称
				@RequestParam(required = false) Integer s_id,//搜索人的id
				@RequestParam(required = false) Boolean all,
				@RequestParam(required = false) String s_user_name, // 搜索人的名字
				@RequestParam(required = false) Integer s_pl_id //搜索管线id
				) {
		Map<String, Object> param = new HashMap<String, Object>();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param);
		model.addAttribute("role", role);
		if(!StringUtils.isBlank(s_p_month)){
			param.put("p_month", Integer.valueOf(s_p_month.replace("-", "")));
			model.addAttribute("s_p_month", s_p_month);
		}
		
		if(s_id != null && s_id.intValue() > 0)
		{
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
		
		List<PipelinePatrol_2016> ppList = baseService.queryPatrols(param, null);

		if(ppList.size()==0){
			return "redirect:/admin/base/pl_patrol/2016/list";
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
			FileOutputStream fileOut = new FileOutputStream(path + sep
					+ "excel" + ".xls");
			/* pp.getPl_name() + "_" + pp.getPl_section_name()
				+ pp.getP_month() + "_" + pp.getJinzhan()*/
			HSSFSheet sheet1 = work.createSheet("sheet1");
			sheet1.setMargin(HSSFSheet.TopMargin,0.7d);// 页边距（上）    	
			sheet1.setMargin(HSSFSheet.BottomMargin,0.7d);// 页边距（下）    
			sheet1.setMargin(HSSFSheet.LeftMargin, 0.7d);// 页边距（左）    
			sheet1.setMargin(HSSFSheet.RightMargin,0.7d);// 页边距（右   
			
			//设置打印纸张等
			HSSFPrintSetup ps = sheet1.getPrintSetup();    
            ps.setLandscape(true); // 打印方向，true：横向，false：纵向(默认)    
            ps.setVResolution((short)600);    
            ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //纸张类型 
			
			HSSFCell cell;
			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 表头格式
			CellStyle titleStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();
			//内容格式1
			CellStyle dataStyle1 = work.createCellStyle();
			//井站格式
			CellStyle titleStyle1 = work.createCellStyle();
			//年月格式
			CellStyle titleStyle2 = work.createCellStyle();
			
			//内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			//dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			//dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			//dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			//dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle1.setBorderBottom(CellStyle.BORDER_THIN);
			dataStyle1.setBorderLeft(CellStyle.BORDER_THIN);
			dataStyle1.setBorderRight(CellStyle.BORDER_THIN);
			dataStyle1.setBorderTop(CellStyle.BORDER_THIN);
			
			//居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle.setWrapText(true);

			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);
			
			//顶格
			dataStyle1.setAlignment(CellStyle.ALIGN_LEFT);
			dataStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle1.setWrapText(true);
			titleStyle1.setAlignment(CellStyle.ALIGN_LEFT);
			titleStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle1.setWrapText(true);
			titleStyle2.setAlignment(CellStyle.ALIGN_RIGHT);
			titleStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle2.setWrapText(true);
			
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
			//datafont.setColor(HSSFColor.RED.index);

			//把字体加入到格式中
			cellStyle.setFont(font);
			titleStyle.setFont(titlefont);
			dataStyle.setFont(datafont);
			dataStyle1.setFont(datafont);
			titleStyle1.setFont(titlefont);
			titleStyle2.setFont(titlefont);
			
			//设置列宽度
			sheet1.setColumnWidth(0, 6*256);
			sheet1.setColumnWidth(1, 6*256);
			sheet1.setColumnWidth(2, 5*256);
			sheet1.setColumnWidth(3, 5*256);
			sheet1.setColumnWidth(4, 5*256);
			sheet1.setColumnWidth(5, 6*256);
			sheet1.setColumnWidth(6, 5*256);
			sheet1.setColumnWidth(7, 6*256);
			sheet1.setColumnWidth(8, 5*256);
			sheet1.setColumnWidth(9, 6*256);
			sheet1.setColumnWidth(10, 6*256);
			sheet1.setColumnWidth(11, 5*256);
			sheet1.setColumnWidth(12, 5*256);
			sheet1.setColumnWidth(13, 6*256);
			sheet1.setColumnWidth(14, 5*256);
			sheet1.setColumnWidth(15, 7*256);
			sheet1.setColumnWidth(16, 5*256);
			sheet1.setColumnWidth(17, 40*256);
			
			int row_index = 0;
			for(PipelinePatrol_2016 p:ppList) {
				//合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index,row_index,0,17));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1,row_index+1,2,4));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1,row_index+1,5,6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1,row_index+1,0,1));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1,row_index+1,7,8));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1,row_index+1,9,16));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2,row_index+3,0,0));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2,row_index+2,1,16));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2,row_index+3,17,17));

				HSSFRow row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float)26.25);
				//新建单元格
				cell = row.createCell(0);
				//设置内容
				cell.setCellValue("管线巡检工作记录");
				//设置单元格格式
				cell.setCellStyle(cellStyle);
				//设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				
				//PipelinePatrol pp = newBaseService.queryPipelinePatrolById(p.getId());
				
				row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float)30.75);
				cell = row.createCell(0);
				cell.setCellValue("单位：");
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(2);
				cell.setCellValue(p.getJinzhan());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(5);
				cell.setCellValue("巡检人：");
				cell.setCellStyle(titleStyle2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(7);
				cell.setCellValue(p.getPatroler());
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(9);
				cell.setCellValue("管线名称及规格: " + p.getName());
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(17);
				cell.setCellValue("巡线管线（段）: "+p.getPatrol_no());
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				//查询数据库
				//PipelinePatrol ppl = newBaseService.queryPipelinePatrolById(pp.getId());
				
				Integer sz = 13;
				/*if(ppl.getDetailList().size() > 13) {
					sz = ppl.getDetailList().size();
				}*/
				
				int addRow = 0;
				for(PipelinePatrolDetail ppd : p.getDetailList()) {
					int rowNum = (int) Math.ceil((double)(Double.valueOf(ppd.getRemark().length()) / 21d));
					rowNum = rowNum - (int)Math.floor((double)(Double.valueOf(rowNum) / 3d));//每到三行减一行
					//rowNum = rowNum - (int)Math.floor((double)(Double.valueOf(rowNum) / 5d));//然后再每到4行减一行
					addRow += rowNum;
				}
				addRow++;//必须自加1,否则会出现少建一行的情况
				
				if(addRow > sz) {
					sz = addRow;
				}
				
				//添加边框线
				int end_row = 0;
				for(int rown=0 ; rown < sz + 1 ; rown++){
					row = sheet1.createRow(rown+row_index);
					row.setHeightInPoints((float) 23.50);
					for(int celln = 0 ; celln < 18 ; celln++){
						cell=row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown+row_index;
				}

				//sheet1.addMergedRegion(new CellRangeAddress(sz+row_index+1,sz+row_index+1,0,2));
				
				/*row = sheet1.getRow(sz+row_index);
				row_index++;
				row.setHeightInPoints(22);
				cell = row.getCell(0);
				cell.setCellValue("保存井(站): " + pp.getSave_jinzhan());
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);*/
				
				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(22);
				cell = row.getCell(0);
				cell.setCellValue("日期");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(1);
				cell.setCellValue("巡检内容");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(17);
				cell.setCellValue("备注");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				row = sheet1.getRow(row_index);
				row_index++;
				row.setHeightInPoints(100);
				
				cell = row.getCell(1);
				cell.setCellValue("管线“三桩”、警示牌");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(2);
				cell.setCellValue("管道护坡、堡坎");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(3);
				cell.setCellValue("埋地管道");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(4);
				cell.setCellValue("明管跨越管段");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(5);
				cell.setCellValue("铁路、公路穿越段");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(6);
				cell.setCellValue("隧道穿越段");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(7);
				cell.setCellValue("穿越河流、沟渠管段");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(8);
				cell.setCellValue("管道两侧环境");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(9);
				cell.setCellValue("管道沿途地址灾害");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(10);
				cell.setCellValue("管道高后果区及高风险段");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(11);
				cell.setCellValue("管道泄漏情况");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(12);
				cell.setCellValue("站场阴保装置");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(13);
				cell.setCellValue("阴极保护测试桩");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(14);
				cell.setCellValue("阳极线路");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(15);
				cell.setCellValue("线路阀室（井）");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.getCell(16);
				cell.setCellValue("管道保护宣传");
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				int count = 0;
				//int index = row_index;
				for (PipelinePatrolDetail ppd : p.getDetailList()) {
					//System.out.println("============>ppl id is " + ppl.getId() + "|ppd id is " + ppd.getId() + "|当前行数" + (row_index + 1));
					row = sheet1.getRow(row_index++);
					cell = row.getCell(0);
					
					CellStyle dataCellStyle = work.createCellStyle();
					//设置日期格式
					short df=work.createDataFormat().getFormat("dd");
					dataCellStyle.setDataFormat(df);
					dataCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
					dataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					dataCellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
					dataCellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
					dataCellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
					dataCellStyle.setFont(datafont);
					if(ppd.getP_date()!=null) {
						cell.setCellValue(DateFormatter.dateToString(ppd.getP_date(), "MM-dd"));
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(dataStyle);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					cell = row.getCell(1);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_1());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(2);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_2());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(3);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_3());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(4);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_4());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(5);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_5());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(6);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_6());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(7);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_7());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(8);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_8());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(9);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_9());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(10);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_10());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(11);
					cell.setCellStyle(dataStyle);
					String content11 = ppd.getContent_11();
					if(!StringUtil.isBlank(content11))
					{
						if(content11.compareTo("1") == 0)
							cell.setCellValue("有");
						else if(content11.compareTo("0") == 0)
							cell.setCellValue("无");
					}
					
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(12);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_12());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(12);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_12());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(13);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_13());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(14);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_14());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(15);
					cell.setCellStyle(dataStyle);
					cell.setCellValue(ppd.getContent_15());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					cell = row.getCell(16);
					cell.setCellStyle(dataStyle);
					String content16 = ppd.getContent_16();
					if(!StringUtil.isBlank(content11))
					{
						if(content16.compareTo("1") == 0)
							cell.setCellValue("是");
						else if(content16.compareTo("0") == 0)
							cell.setCellValue("否");
					}
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					if(!StringUtils.isBlank(ppd.getRemark())) {
						cell = row.getCell(17);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(ppd.getRemark());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					//每行21个字
					if(ppd.getRemark().length() > 21) {
						int remarkRowNum = (int) Math.ceil((double)(Double.valueOf(ppd.getRemark().length()) / 21d));
						//System.out.println("remarkRowNum===============>" + ppd.getRemark() + "|length=" + ppd.getRemark().length() + "|"  + remarkRowNum);
						remarkRowNum = remarkRowNum - (int)Math.floor((double)(Double.valueOf(remarkRowNum) / 3d));
						//remarkRowNum = remarkRowNum - (int)Math.floor((double)(Double.valueOf(remarkRowNum) / 5d));
						sheet1.addMergedRegion(new CellRangeAddress(row_index -1 ,row_index - 2 + remarkRowNum,17,17));
						
						row_index += remarkRowNum - 1;
					}
					
					count++;
				}

				sheet1.setRowBreak(end_row);
				row_index = end_row + 1;
			}
			//将创建好的excel存到指定文件夹下
			work.write(fileOut);
			fileOut.close();
			FileUtils.createZip(response, excelPath,path + sep + "AnnexFile", DateFormatter.dateToString(new Date(), "yyyy-MM-dd_HH:mm:ss:SSS"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/verify_save", method = RequestMethod.POST)
	public @ResponseBody String plPatrol_verify_save(Model model, HttpServletRequest request, 
			@RequestParam Integer status, @RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Object obj = request.getSession().getAttribute(ud.getId() + "ppIdList");
		if (obj != null) {
			List<Integer> ppIdList = (List<Integer>)obj;
			for (Integer ppId : ppIdList)
				baseService.updatePipelinePatrolVerifyStatus(ppId, ud.getId(), status, verify_desc);
			request.getSession().removeAttribute(ud.getId() + "ppIdList");
		}
		//if(status!=null){
			//PipelinePatrol p = basePipelinePatrolNewDAO.queryPipelinePatrolById(id);
			//String content;
			//if (status.intValue() == 1) {
			//	content = "您提交的管道巡检工作记录已审核通过！";
			//} else {
			//	content = "您提交的管道巡检工作记录未审核通过！";
			//}
			//saveTips(content, p.getCreater(), "/admin/base/pl_patrol/show?id=" + id);
		//}
		return JsonUtil.toJson("OK");
	}
}
	

