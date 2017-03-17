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
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
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
@RequestMapping(value = "/admin/base/new")
public class BasePlPatrolController_new {
	
	@Autowired
	private NewBaseService newBaseService;
	
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 管道巡检工作记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/create", method = RequestMethod.GET)
	public String plPatrol_create(Model model, @RequestParam(required=false) String status){
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByUser(ud.getId());
		
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		Prompt prompt = newBaseService.quertPromptByType(19);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		model.addAttribute("remarkWidth", SettingUtils.getCommonSetting("pl_protrol.line.with"));
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/pl_patrol_create_new";
	}
	
	
	@RequestMapping(value="/pl_patrol/getTime")
	public @ResponseBody JsonResWrapper GetTime(@RequestParam Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		PipelinePatrol pc = newBaseService.queryPipelinePatrolById(id);
		pc.setCreate_t(DateFormatter.dateToString(pc.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
		if(pc.getVerify_time() != null){
			pc.setVerify_t(DateFormatter.dateToString(pc.getVerify_time(), "yyyy-MM-dd HH:mm:ss"));
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pc);
		return response;
	}
	
	@RequestMapping(value="/pl_patrol/setTime")
	public @ResponseBody JsonResWrapper setTime(@RequestParam Integer id,@RequestParam String create_t,@RequestParam String verify_t) {
		JsonResWrapper response = new JsonResWrapper();
		newBaseService.updatePipelinePatrolTime(id, create_t, verify_t);
		response.setStatus(ResponseStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/pl_patrol/section/get", method = RequestMethod.GET)
	public @ResponseBody List<BasePipelineSection> section_get(Model model,
			@RequestParam Integer pl_id
			,@RequestParam Boolean all
			,@RequestParam(required=false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userService.queryRoleIdByUserId(ud.getId()) != 3) {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("pl_id", pl_id);
			Integer role = CommonsMethod.getDataByRole(SecurityContextHolder.getContext().getAuthentication(), userService, param, all);
			if(status==null)
			{
				if(role==2)
					param.put("status", "-1,0,1");
			}
			else
				param.put("status", status);
			return newBaseService.querySectionByAdminPatrol(param);
		}
		return baseService.querySectionByUser(pl_id, ud.getId());
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
	@RequestMapping(value = "/pl_patrol/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String plPatrol_list(Model model, HttpServletRequest request,Authentication authentication, @RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String p_month
			,@RequestParam(required=false) Boolean all
			, @RequestParam(required=false) Integer del,
			@RequestParam(required = false) Integer verify
			) {		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Map<String, Object> param = new HashMap<String, Object>();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param, all);
		if(verify != null && verify.intValue() == 1)
			param.put("status", 0);
		else if(role==2)
		{
			param.put("status", "-1,0,1");
		}
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);
			
			Map<String,Object> par = new HashMap<String,Object>(param);
			par.put("pl_id", pl);
			List<BasePipelineSection> sectionList = newBaseService.querySectionByAdminPatrol(par);
			
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
		
		if (!StringUtils.isBlank(p_month)) {
			param.put("p_month", Integer.valueOf(p_month.replace("-", "")));
			model.addAttribute("p_month", p_month);
		}
		if(all != null)
		{
			model.addAttribute("all", all);
		}

		//判读用户是否是维护工，维护工只能查看自己的数据
				
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByAdminPatrol(param);
		
		//PageSupport ps = PageSupport.initPageSupport(request);
		
		//List<PipelinePatrol> ppList = newBaseService.queryPipelinePatrol(param, ps);
		// 规格 显示列表
		List<BasePipelineSpec> specList = newBaseService.querySpecListNewByAdminPatrol(param);
		for(BasePipelineSpec bSpec : specList)
		{
			//稍后处理
		}
		/*if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}*/
		
		model.addAttribute("role", role);
		
		model.addAttribute("pipeLineList", pipeLineList);
		//model.addAttribute("ppList", ppList);
		model.addAttribute("specLists", specList);
		return "pages/base/pl_patrol_list_new";
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
	@RequestMapping(value = "/pl_patrol/book_list", method = {RequestMethod.GET, RequestMethod.POST})
	public String plPatrol_book_list(Model model, HttpServletRequest request,Authentication authentication, 
			@RequestParam(required=false) Integer pl, @RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String p_month, @RequestParam(required = false) Integer st) {
		
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
		
		if (!StringUtils.isBlank(p_month)) {
			param.put("p_month", Integer.valueOf(p_month.replace("-", "")));
			model.addAttribute("p_month", p_month);
		}
		
		if (st != null && st.intValue() != 2) {
			param.put("status", st);
			model.addAttribute("st", st);
		}
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("user_id", ud.getId());
		List<PipelinePatrol> ppList = newBaseService.queryPipelinePatrolMerge(param);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("ppList", ppList);
		return "pages/base/pl_patrol_book_list";
	}
	
	/**
	 * 管道巡检审核查看
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/book_show", method = {RequestMethod.GET,RequestMethod.POST})
	public String plPatrol_book_show(Model model, 
			HttpServletRequest request, 
			Authentication authentication,
			@RequestParam Integer pl_id,
			@RequestParam Integer pl_section_id, 
			@RequestParam Integer pl_spec_id,
			@RequestParam(required=false) String p_month, @RequestParam(required = false) Integer status) {
		PageSupport ps = PageSupport.initPageSupport(request);
		ps.setPageSize(12);
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		
		Map<String, Object> param = new HashMap<String, Object>();
		if (status != null && (status.intValue() == -1 || status.intValue() == 0 || status.intValue() == 1)) {
			param.put("status", status);
			model.addAttribute("status", status);
		}
		if (!StringUtils.isBlank(p_month)) {
			param.put("p_month", Integer.valueOf(p_month.replace("-", "")));
			model.addAttribute("p_month", p_month);
		}
		
		param.put("pl_id", pl_id);
		param.put("pl_section_id", pl_section_id);
		param.put("pl_spec_id", pl_spec_id);
		param.put("user_id", ud.getId());
		
		PipelinePatrol pp = newBaseService.queryPipelinePatrolMergeInfo(param);
		if (pp != null) {
			List<PipelinePatrolDetail> detailList = newBaseService.queryPipelinePatrolDetailMerge(param, ps);
			pp.setDetailList(detailList);
			
			int d = 12 - detailList.size();
			if (d > 0) {
				for (int i = 0; i < d; i++) {
					PipelinePatrolDetail e = new PipelinePatrolDetail();
					detailList.add(e);
				}
			}
		}
		model.addAttribute("pp", pp);
		model.addAttribute("pl_id", pl_id);
		model.addAttribute("pl_section_id", pl_section_id);
		model.addAttribute("pl_spec_id", pl_spec_id);
		return "pages/base/pl_patrol_book_show";
	}
	
	/**
	 * 管道巡检工作记录保存
	 * 
	 */
	@RequestMapping(value = "/pl_patrol/save", method = RequestMethod.POST)
	public String plPatrol_save(Model model, HttpServletRequest request,@RequestParam(required = false) Integer id,@RequestParam(required=false) Integer[] pld_id, @RequestParam(required=false) String patroler,
			@RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,@RequestParam(required=false) Integer[] has_annex_id, @RequestParam(required=false) String[] has_annex,
			@RequestParam(required = false) String jinzhan, @RequestParam(required = false) String p_month, @RequestParam(required=false) String patrol_no,
			@RequestParam(required = false) String[] p_date, @RequestParam(required = false) String[] work_place, @RequestParam(required = false) String[] content,
			@RequestParam(required = false) String[] question, @RequestParam(required = false) String[] voice_record, @RequestParam(required = false) String[] worker,
			@RequestParam(required = false) String[] auditor, @RequestParam(required = false) String save_jinzhan, @RequestParam String[] content_1,@RequestParam String[] content_2,
			@RequestParam String[] content_3,@RequestParam String[] content_4,@RequestParam String[] content_5,@RequestParam String[] content_6,@RequestParam String[] content_7,@RequestParam String[] content_8,
			@RequestParam String[] content_9,@RequestParam String[] content_10,@RequestParam String[] content_11,@RequestParam String[] content_12,@RequestParam String[] content_13,@RequestParam String[] content_14,
			@RequestParam String[] content_15,@RequestParam String up_id,@RequestParam String[] content_16,@RequestParam String[] remark,@RequestParam String status,
			@RequestParam(required = false) Integer[] sign
			){
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		PipelinePatrol pp = new PipelinePatrol();
		pp.setJinzhan(jinzhan);
		//pp.setP_month(Integer.valueOf(p_month.replace("-", "")));
		pp.setPl_id(pl);
		pp.setPl_section_id(section);
		pp.setPl_spec_id(spec);
		pp.setSave_jinzhan(save_jinzhan);
		pp.setCreater(ud.getId());
		pp.setPatroler(patroler);
		pp.setId(id);
		pp.setPatrol_no(patrol_no);
		pp.setUp_id(up_id);
		pp.setStatus(Integer.parseInt(status));//添加状态值
		List<String> imgs = null;
	
		try {
			imgs = DataUtil.uploadImg(request, "annex");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "图片上传错误，请检查图片是否正确！");
			return "pages/base/pl_patrol_create_new";
		}
		
		Map<Integer, String> annexMap = new HashMap<Integer, String>();
		if(has_annex_id != null && has_annex_id.length > 0) {
			int i = 0;
			for(Integer in : has_annex_id) {
				if(in.intValue() > 0) {
					annexMap.put(in, has_annex[i]);
				}
				i++;
			}
		}
		
		List<PipelinePatrolDetail> ppdList = new ArrayList<PipelinePatrolDetail>();
		int i = 0;
		int used = -1;
		for(Integer s : sign)
		{
			boolean canEdit = s.intValue() != 0;//该行可编辑
			if(canEdit)
			{
				if(used >= p_date.length)
					break;
				++used;
			}				
			do{
				if(used >= p_date.length || used < 0)
					break;
				String r = p_date[used];
				if((canEdit && StringUtils.isBlank(r)) || (canEdit==false && StringUtils.isBlank(remark[i])))
					break;
				if ((canEdit && !StringUtils.isBlank(r)) || !StringUtils.isBlank(remark[i])) {
					PipelinePatrolDetail ppd = new PipelinePatrolDetail();
					if (!StringUtils.isBlank(r) && canEdit) {
						Calendar ca = Calendar.getInstance();
						ca.setTime(new Date());
						ppd.setP_date(DateFormatter.stringToDate(ca.get(Calendar.YEAR) + "-" + r, "yyyy-MM-dd"));
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
			newBaseService.savePlPatrol(pp, ppdList);
		}

		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}
		return "redirect:/admin/base/new/pl_patrol/"+ page;
	}
	
	/**
	 * 管道巡检工作记录修改
	 * 
	 */
	@RequestMapping(value = "/pl_patrol/modify", method = RequestMethod.POST)
	public String plPatrol_modify(Model model, HttpServletRequest request,@RequestParam(required = false) Integer id,@RequestParam(required=false) Integer[] pld_id,
			@RequestParam String[] content_1,@RequestParam String[] content_2,@RequestParam String[] content_3,@RequestParam String[] content_4,
			@RequestParam String[] content_5,@RequestParam String[] content_6,@RequestParam String[] content_7,@RequestParam String[] content_8,
			@RequestParam String[] content_9,@RequestParam String[] content_10,@RequestParam String[] content_11,@RequestParam String[] content_12,
			@RequestParam String[] content_13,@RequestParam String[] content_14,@RequestParam String[] content_15,@RequestParam(required = false) String[] content_16,@RequestParam String[] remark,
			@RequestParam(required = false) String r_month,
			@RequestParam(required = false) Integer verify,
			@RequestParam(required=false) Integer tips_id,
			@RequestParam(required=false) Boolean submit
			)
			//@RequestParam(required = false) String up_id,@RequestParam(required = false) String status)
			{
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(pld_id != null)
		{
			int i = 0;
			for(Integer pId : pld_id)
			{
				PipelinePatrolDetail ppd = new PipelinePatrolDetail();
				ppd.setContent_1(content_1[i]);
				ppd.setContent_2(content_2[i]);
				ppd.setContent_3(content_3[i]);
				ppd.setContent_4(content_4[i]);
				ppd.setContent_5(content_5[i]);
				ppd.setContent_6(content_6[i]);
				ppd.setContent_7(content_7[i]);
				ppd.setContent_8(content_8[i]);
				ppd.setContent_9(content_9[i]);
				ppd.setContent_10(content_10[i]);
				ppd.setContent_11(content_11[i]);
				ppd.setContent_12(content_12[i]);
				ppd.setContent_13(content_13[i]);
				ppd.setContent_14(content_14[i]);
				ppd.setContent_15(content_15[i]);
				ppd.setContent_16(content_16[i]);
				ppd.setRemark(StringUtils.isBlank(remark[i]) ? "" : remark[i]);
				ppd.setId(pId);
				ppd.setStatus(new Integer(-2));
				if(submit != null && submit.booleanValue())
				{
					newBaseService.updateChangePipelinePatrolDetailStatus(pId, new Integer(0));
				}
				newBaseService.modifyPlPatrolDetail(ppd);
				++i;
			}			
		}
		String page = "";
		page = CommonsMethod.putUrlParam(page, "id", id);
		page = CommonsMethod.putUrlParam(page, "r_month", r_month);
		page = CommonsMethod.putUrlParam(page, "verify", verify);
		page = CommonsMethod.putUrlParam(page, "tips_id", tips_id);
		return "redirect:/admin/base/new/pl_patrol/show"+page;//"pages/base/pl_patrol_show_new";
	}
	
	/**
	 * 管道巡检工作记录批量更改状态
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/batch_changestatus", method = {RequestMethod.GET,RequestMethod.POST})
	public String plPatrol_batch_changestatus(Authentication authentication,HttpServletRequest request,
							  @RequestParam(required = false) Integer id, 
							  @RequestParam(required = false) String r_month,
							  @RequestParam(required = false) Integer verify,
							  @RequestParam(required = false) Integer[] oldStatus,
							  @RequestParam(required = false) Integer newStatus,
							  @RequestParam(required=false) Integer tips_id) {
		
		if (tips_id != null && tips_id.intValue() > 0){
			baseService.deleteTips(tips_id);
		}
		
		Map<String, Object> param = new HashMap<String, Object>();

		CommonsMethod.getDataByRole(authentication, userService, param);
		if(!StringUtils.isBlank(r_month)){
			param.put("r_month", r_month);
		}
		if(id != null)
		{
			param.put("id", id);
		}
		
		PipelinePatrol pp = newBaseService.queryPipelinePatrolByList(param); 
		
		if(pp!=null)
		{
			param.put("pl", pp.getPl_id());
			param.put("spec", pp.getPl_spec_id());
			param.put("section", pp.getPl_section_id());
			List<PipelinePatrolDetail> ppd = newBaseService.queryPipelinePatrolDetail(param);
			for(PipelinePatrolDetail p : ppd)
			{
				if(Arrays.asList(oldStatus).contains(p.getStatus()))
				{
					newBaseService.updateChangePipelinePatrolDetailStatus(new Integer(p.getId()), newStatus);  
				}     
			}
		}
		
		String page = "";
		page = CommonsMethod.putUrlParam(page, "id", id);
		page = CommonsMethod.putUrlParam(page, "r_month", r_month);
		page = CommonsMethod.putUrlParam(page, "verify", verify);
		page = CommonsMethod.putUrlParam(page, "tips_id", tips_id);

		return "redirect:/admin/base/new/pl_patrol/show"+page;
	}
	
	/**
	 * 管道巡检工作记录查询
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/show", method = {RequestMethod.GET,RequestMethod.POST})
	public String plPatrol_show(Model model, Authentication authentication,HttpServletRequest request,
							  @RequestParam(required = false) Integer id, 
							  @RequestParam(required = false) String r_month,
							  @RequestParam(required = false) Integer verify,
							//  @RequestParam Integer offset, 
							  @RequestParam(required=false) Integer tips_id) {
		
		if (tips_id != null && tips_id.intValue() > 0){
			baseService.deleteTips(tips_id);
			model.addAttribute("tips_id", tips_id);
		}
		
		Map<String, Object> param = new HashMap<String, Object>();

		CommonsMethod.getDataByRole(authentication, userService, param);
		if(!StringUtils.isBlank(r_month)){
			param.put("r_month", Integer.valueOf(r_month.replace("-", "")));
			model.addAttribute("r_month", r_month);
		}
		if(id != null)
		{
			param.put("id", id);
			model.addAttribute("id", id);
		}
		
		PipelinePatrol pp = newBaseService.queryPipelinePatrolByList(param); 
		
		if(pp==null){
			return "pages/base/pl_patrol_show_new";
		}

		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		List<BasePipelineSection> sectionList = baseService.querySection(pp.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pp.getPl_section_id());

		//获取用户角色ID
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal(); 
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);
		
		model.addAttribute("verify", verify);
		/*{
			Map<String, Object> param1 = new HashMap<String,Object>(param);
			param1.put("limit", 10000);
			param1.put("offset", 0);
			param1.put("order", 1);
			List<PipelinePatrolDetail> detailList1 = newBaseService.queryPipelinePatrolDetailMerge(param1);
			//pp.setDetailList(detailList);
			
			List<Integer> ppIdList = null;
			Object obj = request.getSession().getAttribute(ud.getId() + "ppIdList");
			if (obj != null) {
				ppIdList = (List<Integer>)obj;
			} else {
				request.getSession().removeAttribute(ud.getId() + "ppIdList");
				ppIdList = new ArrayList<Integer>();
			}
			if (!CollectionUtils.isEmpty(detailList1)) {
				for (PipelinePatrolDetail ppd : detailList1) {
					if (!ppIdList.contains(ppd.getPp_id())) {
						ppIdList.add(ppd.getPp_id());
					}
				}
				request.getSession().setAttribute(ud.getId() + "ppIdList", ppIdList);
			}
		}*/
		{
			PageSupport ps = PageSupport.initPageSupport(request);
			param.put("pl", pp.getPl_id());
			param.put("spec", pp.getPl_spec_id());
			param.put("section", pp.getPl_section_id());
			ps.setPageSize(12);
			List<PipelinePatrolDetail> ppd = newBaseService.queryPipelinePatrolDetail(param, ps);
			for(PipelinePatrolDetail p : ppd)
			{
				newBaseService.updateCheckPipelinePatrolDetailStatus(p);
				p.resetCanEidt(role);           
			}
			
			if(ppd.size() < ps.getPageSize())
			{
				for(int i = ppd.size(); i < ps.getPageSize(); ++i)
				{
					PipelinePatrolDetail n = new PipelinePatrolDetail();
					ppd.add(n);
				}
			}
			model.addAttribute("ppd", ppd);
		}
		
		model.addAttribute("pp", pp);
		model.addAttribute("pl", pp.getPl_id());
		model.addAttribute("spec", pp.getPl_spec_id());
		model.addAttribute("section", pp.getPl_section_id());
		
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		
		return "pages/base/pl_patrol_show_new";
	}
	
	/**
	 * 管道巡检工作记录删除
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/del", method = RequestMethod.GET)
	public String plPatrol_del(Model model, @RequestParam Integer id) {
		System.out.println(id);
		newBaseService.deletePipelinePatrolById(id);
		return "redirect:/admin/base/new/pl_patrol/list?del=1";
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
	@RequestMapping(value = "/pl_patrol/exp", method = RequestMethod.GET)
	public String plPatrol_export(Model model, 
			 HttpServletRequest request, 
			 HttpServletResponse response,
			 @RequestParam(required=false) Integer pl,
			 @RequestParam(required=false) Integer section, 
			 @RequestParam(required=false) Integer spec,
			 @RequestParam(required=false) String p_month) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> paramD = new HashMap<String, Object>();
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			paramD.put("pl", pl);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			paramD.put("section", section);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			paramD.put("spec", spec);
		}

		if (!StringUtils.isBlank(p_month)) {
			param.put("p_month", Integer.valueOf(p_month.replace("-", "")));
			paramD.put("r_month", Integer.valueOf(p_month.replace("-", "")));
		}
		
		List<PipelinePatrol> ppList = newBaseService.queryPipelinePatrol(param, null);		
		
		if(ppList.size()==0){
			return "redirect:/admin/base/new/pl_patrol/list";
		}
		
		List<PipelinePatrolDetail> ppdList = newBaseService.queryPipelinePatrolDetail(paramD, null);
		int datas_row = 12;//每页数据行数
		
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
			int pages = (ppdList.size()+datas_row-1)/datas_row;
			PipelinePatrol p = ppList.get(0);
			PipelinePatrol pp = newBaseService.queryPipelinePatrolById(p.getId());
			for(int iPage = 0; iPage < pages; ++iPage) {
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
				
				row = sheet1.createRow(row_index);
				row_index++;
				row.setHeightInPoints((float)30.75);
				cell = row.createCell(0);
				cell.setCellValue("单位：");
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(2);
				cell.setCellValue(pp.getJinzhan());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(5);
				cell.setCellValue("巡检人：");
				cell.setCellStyle(titleStyle2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(7);
				cell.setCellValue(pp.getPatroler());
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(9);
				cell.setCellValue("管线名称及规格: " + p.getPl_name() + "/"
						+ p.getPl_spec_name());
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(17);
				cell.setCellValue("巡线管线（段）: "+pp.getPatrol_no());
				cell.setCellStyle(titleStyle1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				//查询数据库
				//PipelinePatrol ppl = newBaseService.queryPipelinePatrolById(pp.getId());
				
				Integer sz = 13;
				/*if(ppl.getDetailList().size() > 13) {
					sz = ppl.getDetailList().size();
				}*/
				
				/*int addRow = 0;
				for(PipelinePatrolDetail ppd : ppl.getDetailList()) {
					int rowNum = (int) Math.ceil((double)(Double.valueOf(ppd.getRemark().length()) / 21d));
					rowNum = rowNum - (int)Math.floor((double)(Double.valueOf(rowNum) / 3d));//每到三行减一行
					//rowNum = rowNum - (int)Math.floor((double)(Double.valueOf(rowNum) / 5d));//然后再每到4行减一行
					addRow += rowNum;
				}
				addRow++;//必须自加1,否则会出现少建一行的情况
				
				if(addRow > sz) {
					sz = addRow;
				}*/
				
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
				//for (PipelinePatrolDetail ppd : ppl.getDetailList()) {
				for(int iRow = 0; iRow < datas_row; ++iRow){
					int detailIndex = iPage*datas_row + iRow;
					if(detailIndex >= ppdList.size())
						break;
					PipelinePatrolDetail ppd = ppdList.get(detailIndex);
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
					cell.setCellValue(ppd.getContent_11());
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
					cell.setCellValue(ppd.getContent_16());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

					if(!StringUtils.isBlank(ppd.getRemark())) {
						cell = row.getCell(17);
						cell.setCellStyle(dataStyle);
						cell.setCellValue(ppd.getRemark());
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					
					//每行21个字
					/*if(ppd.getRemark().length() > 21) {
						int remarkRowNum = (int) Math.ceil((double)(Double.valueOf(ppd.getRemark().length()) / 21d));
						//System.out.println("remarkRowNum===============>" + ppd.getRemark() + "|length=" + ppd.getRemark().length() + "|"  + remarkRowNum);
						remarkRowNum = remarkRowNum - (int)Math.floor((double)(Double.valueOf(remarkRowNum) / 3d));
						//remarkRowNum = remarkRowNum - (int)Math.floor((double)(Double.valueOf(remarkRowNum) / 5d));
						sheet1.addMergedRegion(new CellRangeAddress(row_index -1 ,row_index - 2 + remarkRowNum,17,17));
						
						row_index += remarkRowNum - 1;
					}*/
					
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
	
	/**
	 * 管道巡检记录修改
	 * @param model
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/edit", method = RequestMethod.GET)
	public String plPatrol_edit(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByUser(ud.getId());
		PipelinePatrol pp = newBaseService.queryPipelinePatrolById(id);
		List<BasePipelineSection> sectionList = newBaseService.querySection(pp
				.getPl_id());
		List<BasePipelineSpec> specList = newBaseService.querySpec(pp
				.getPl_section_id());
		
		model.addAttribute("pl", pp.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pp.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pp.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pp", pp);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		
		String[] temp = new String[12- pp.getDetailList().size()];
		model.addAttribute("temp", temp);
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/pl_patrol_edit_new";
	}
	
	@RequestMapping(value = "/pl_patrol/verify", method = { RequestMethod.GET,RequestMethod.POST })
	public String plPatrol_verify(Model model, HttpServletRequest request, Authentication authentication,@RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String p_month
			,@RequestParam(required=false) Boolean all) {
		Map<String, Object> param = new HashMap<String, Object>();
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		CommonsMethod.getDataByRole(authentication, userService, param, false);
		param.put("status", 0);
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);
			
			Map<String,Object> par = new HashMap<String,Object>(param);
			par.put("pl_id", pl);
			List<BasePipelineSection> sectionList = newBaseService.querySectionByAdminPatrol(par);
			
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
		
		if (!StringUtils.isBlank(p_month)) {
			param.put("p_month", Integer.valueOf(p_month.replace("-", "")));
			model.addAttribute("p_month", p_month);
		}
		if(all != null)
		{
			model.addAttribute("all", all);
		}
		List<BasePipelineSpec> specList = newBaseService.querySpecListNewByAdminPatrol(param);
		//List<PipelinePatrol> ppList = newBaseService.queryPipelinePatrolMerge(param);
		model.addAttribute("specList", specList);


		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByAdminPatrol(param);
		model.addAttribute("pipeLineList", pipeLineList);
		
		return "pages/base/pl_patrol_verify";
	}

	/**
	 * 管道巡检审核查看
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/show_verify", method = {RequestMethod.GET,RequestMethod.POST})
	public String plPatrol_show_verify(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam Integer pl_id, @RequestParam Integer pl_section_id, @RequestParam Integer pl_spec_id,
			@RequestParam(required=false) Integer f) {
		PageSupport ps = PageSupport.initPageSupport(request);
	
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", 0);
		param.put("pl_id", pl_id);
		param.put("pl_section_id", pl_section_id);
		param.put("pl_spec_id", pl_spec_id);
		param.put("user_id", ud.getId());
		
		PipelinePatrol pp = newBaseService.queryPipelinePatrolMergeInfo(param);
		if (pp != null) {
			Map<String, Object> param1 = new HashMap<String,Object>(param);
			param1.put("limit", 10000);
			param1.put("offset", 0);
			param1.put("order", 1);
			List<PipelinePatrolDetail> detailList1 = newBaseService.queryPipelinePatrolDetailMerge(param1);
			ps.setPageSize(12);
			List<PipelinePatrolDetail> detailList = newBaseService.queryPipelinePatrolDetailMerge(param, ps);
			//pp.setDetailList(detailList);
			
			List<Integer> ppIdList = null;
			Object obj = request.getSession().getAttribute(ud.getId() + "ppIdList");
			if (obj != null && f == null) {
				ppIdList = (List<Integer>)obj;
			} else {
				request.getSession().removeAttribute(ud.getId() + "ppIdList");
				ppIdList = new ArrayList<Integer>();
			}
			if (!CollectionUtils.isEmpty(detailList1)) {
				for (PipelinePatrolDetail ppd : detailList1) {
					if (!ppIdList.contains(ppd.getPp_id())) {
						ppIdList.add(ppd.getPp_id());
					}
				}
				request.getSession().setAttribute(ud.getId() + "ppIdList", ppIdList);
			}
			int d = ps.getPageSize() - detailList.size();
			if (d > 0) {
				for (int i = 0; i < d; i++) {
					PipelinePatrolDetail e = new PipelinePatrolDetail();
					detailList.add(e);
				}
			}
			model.addAttribute("ppd", detailList);
		}
		model.addAttribute("pp", pp);
		model.addAttribute("pl_id", pl_id);
		model.addAttribute("pl_section_id", pl_section_id);
		model.addAttribute("pl_spec_id", pl_spec_id);
		return "pages/base/pl_patrol_verify_show";
	}
	
	@RequestMapping(value = "/pl_patrol/verify_save", method = RequestMethod.POST)
	public @ResponseBody String plPatrol_verify_save(Model model, HttpServletRequest request, 
			@RequestParam Integer status, @RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Object obj = request.getSession().getAttribute(ud.getId() + "ppIdList");
		if (obj != null) {
			List<Integer> ppIdList = (List<Integer>)obj;
			for (Integer ppId : ppIdList)
				newBaseService.updatePipelinePatrolVerifyStatus(ppId, ud.getId(), status, verify_desc);
			request.getSession().removeAttribute(ud.getId() + "ppIdList");
		}
		/*if(status!=null){
			PipelinePatrol p = basePipelinePatrolNewDAO.queryPipelinePatrolById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的管道巡检工作记录已审核通过！";
			} else {
				content = "您提交的管道巡检工作记录未审核通过！";
			}
			saveTips(content, p.getCreater(), "/admin/base/pl_patrol/show?id=" + id);
		}*/
		return JsonUtil.toJson("OK");
	}
}
	

