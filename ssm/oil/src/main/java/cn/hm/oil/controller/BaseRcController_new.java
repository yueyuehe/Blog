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
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.RunRecord;
import cn.hm.oil.domain.RunRecordDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
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
 *         阴极保护站运行记录查看
 * 
 */
@Controller
@RequestMapping(value = "/admin/base/new")
public class BaseRcController_new {
	@Autowired
	private NewBaseService newBaseService;
	
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
	@RequestMapping(value = "/rc/create_old", method = RequestMethod.GET)
	public String rc_create(Model model,
			@RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByUser(ud
				.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		Prompt prompt = newBaseService.quertPromptByType(21);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}

		return "pages/base/rc_create_new";
	}
	
	/**
	 * 阴极保护站运行记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/create", method = RequestMethod.GET)
	public String rc_create_merge(Model model, @RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByUser(ud
				.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		Prompt prompt = newBaseService.quertPromptByType(21);
		if(prompt != null) {
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
		model.addAttribute("commentWidth", SettingUtils.getCommonSetting("rc.line.with"));
		return "pages/base/rc_create_merge";
	}

	@RequestMapping(value="/rc/getTime")
	public @ResponseBody JsonResWrapper GetTime(@RequestParam Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		RunRecord pc = newBaseService.queryRunRecordById(id);
		pc.setCreate_t(DateFormatter.dateToString(pc.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
		if(pc.getVerify_time() != null){
			pc.setVerify_t(DateFormatter.dateToString(pc.getVerify_time(), "yyyy-MM-dd HH:mm:ss"));
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pc);
		return response;
	}
	
	@RequestMapping(value="/rc/setTime")
	public @ResponseBody JsonResWrapper setTime(@RequestParam Integer id,@RequestParam String create_t,@RequestParam String verify_t) {
		JsonResWrapper response = new JsonResWrapper();
		newBaseService.updateRunRecordTime(id, create_t, verify_t);
		response.setStatus(ResponseStatus.OK);
		return response;
	}
	
	/**
	 * 阴极保护站运行记录保存
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/save_old", method = RequestMethod.POST)
	public String rc_save(Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam Integer pl,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam String auditor,
			@RequestParam String r_month, @RequestParam Integer[] day,
			@RequestParam Integer[] time, @RequestParam Float[] jldy,
			@RequestParam Float[] zlsc_a, @RequestParam Float[] zlsc_v,
			@RequestParam Float[] tdddw, @RequestParam String[] recorder,@RequestParam String up_id,
			@RequestParam String comment, @RequestParam(required=false) String[] others) {
		RunRecord rc = new RunRecord();
		rc.setId(id);
		rc.setAuditor(auditor);
		rc.setJinzhan(jinzhan);
		rc.setPl_id(pl);
		rc.setPl_section_id(section);
		rc.setPl_spec_id(spec);
		rc.setComment(comment);
		rc.setR_month(Integer.valueOf(r_month.replace("-", "")));
		rc.setStatus(0);
		rc.setUp_id(up_id);
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
			rcd.setRecorder(recorder[i]);
			rcd.setTdddw(tdddw[i]);
			rcd.setZlsc_a(zlsc_a[i]);
			rcd.setZlsc_v(zlsc_v[i]);
			if(others != null) {
				rcd.setOthers(others[i]);
			}
			rcdList.add(rcd);
			
			i++;
		}
		String status = "0";
		if (rcdList.size() > 0) {
			newBaseService.saveRcRecord(rc, rcdList);
			status = "1";
		}
		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}
		
		return "redirect:/admin/base/new/rc/"+page;
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
			@RequestParam(required = false) Integer del,
			@RequestParam(required=false) Boolean all,
			@RequestParam(required = false) Integer verify) {
	
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
			List<BasePipelineSection> sectionList = newBaseService.querySectionByAdminRc(par);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);

			List<BasePipelineSpec> specList = newBaseService.querySpec(section);
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
		if(all != null)
		{
			model.addAttribute("all", all);
		}
		//判读用户是否是维护工，维护工只能查看自己的数据
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByAdminRc(param);
		
		PageSupport ps = PageSupport.initPageSupport(request);
		List<RunRecord> rcList = newBaseService.queryRunRecord(param, ps);

		List<BasePipelineSpec> specList = newBaseService.querySpecListNewByAdminRc(param);
		model.addAttribute("specLists", specList);
				

		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("rcList", rcList);
		model.addAttribute("verify", verify);
		return "pages/base/rc_create_list_new";
	}
	
	@RequestMapping(value = "/rc/section/get", method = RequestMethod.GET)
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
			return newBaseService.querySectionByAdminRc(param);
		}
		return baseService.querySectionByUser(pl_id, ud.getId());
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
	@RequestMapping(value = "/rc/verify_old", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String rc_verify(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String r_month) {
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLine();

		Map<String, Object> param = new HashMap<String, Object>();

		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			List<BasePipelineSection> sectionList = newBaseService
					.querySection(pl);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);

			List<BasePipelineSpec> specList = newBaseService.querySpec(section);
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

		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("verify", "1");
		param.put("user_id", ud.getId());
		PageSupport ps = PageSupport.initPageSupport(request);
		List<RunRecord> rcList = newBaseService.queryRunRecord(param, ps);

		
		Integer role = userService.queryRoleIdByUserId(ud.getId());

		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("rcList", rcList);
		model.addAttribute("verify", 1);
		return "pages/base/rc_create_list_new";
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
	@RequestMapping(value = "/rc/verify", method = { RequestMethod.GET, RequestMethod.POST })
	public String rc_verify_merge(Model model, HttpServletRequest request,
								Authentication authentication,
								@RequestParam(required = false) Integer pl, 
								@RequestParam(required = false) Integer section,
								@RequestParam(required = false) Integer spec, 
								@RequestParam(required = false) String r_month) {		
				
		/*Map<String, Object> param = new HashMap<String, Object>();
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("status", 0);
		param.put("user_id", ud.getId());
		List<RunRecord> rcList = newBaseService.queryRunRecordMerge(param);
		
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);
		
		model.addAttribute("rcList", rcList);
		return "pages/base/rc_verify_list_merge";*/
		String pages="";
		pages = CommonsMethod.putUrlParam(pages, "pl", pl);
		pages = CommonsMethod.putUrlParam(pages, "section", section);
		pages = CommonsMethod.putUrlParam(pages, "spec", spec);
		pages = CommonsMethod.putUrlParam(pages, "r_month", r_month);
		pages = CommonsMethod.putUrlParam(pages, "verify", new Integer(1));
		pages = CommonsMethod.putUrlParam(pages, "all", new Boolean(false));
		return "redirect:/admin/base/new/rc/list"+pages;
	}
	
	/**
	 * 阴极保护站运行记录详细查看
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/rc/show_old", method = RequestMethod.GET)
	public String rc_show(Model model,Authentication authentication,
							  @RequestParam Integer id, 
							  @RequestParam(required = false) String r_month,
							  @RequestParam(required = false) Integer verify,
							  @RequestParam Integer offset, 
							  @RequestParam(required=false) Integer tips_id) {
		
		if (tips_id != null && tips_id.intValue() > 0){
			baseService.deleteTips(tips_id);
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("offset", offset);
		param.put("pageSize", 1);
		

		if(!StringUtils.isBlank(r_month)){
			param.put("r_month", r_month);
			model.addAttribute("r_month", r_month);
		}
		
		RunRecord rc = newBaseService.queryRunRecordById(id);
		if(rc==null){
			return "pages/base/rc_create_show_new";
		}
		int vpTotal = newBaseService.queryValvePatrolByListTotal(param);
		
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		List<BasePipelineSection> sectionList = newBaseService.querySection(rc.getPl_id());
		List<BasePipelineSpec> specList = newBaseService.querySpec(rc.getPl_section_id());

		//获取用户角色ID
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);
		
		model.addAttribute("rc", rc);
		model.addAttribute("total", vpTotal);
		model.addAttribute("offset", offset);
		model.addAttribute("id", id);
		model.addAttribute("verify", verify);
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);
		
		return "pages/base/rc_create_show_new";
	}
	
	/**
	 * 阴极保护站运行记录详细查看
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/rc/show", method = {RequestMethod.GET,RequestMethod.POST})
	public String rc_show_merge(Model model,Authentication authentication,HttpServletRequest request,
							  @RequestParam(required = false) Integer id, 
							  @RequestParam(required = false) String r_month,
							  @RequestParam(required = false) Integer verify,
							  //@RequestParam Integer offset, 
							  @RequestParam(required=false) Integer tips_id) {
		if (tips_id != null && tips_id.intValue() > 0){
			baseService.deleteTips(tips_id);
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		if(id != null)
		{
			param.put("id", id);
		}
		//param.put("offset", offset);
		//param.put("pageSize", 1);

		if(!StringUtils.isBlank(r_month)){
			param.put("r_month", Integer.valueOf(r_month.replace("-", "")));
			model.addAttribute("r_month", r_month);
		}
		
		CommonsMethod.getDataByRole(authentication, userService, param);
		RunRecord rc = newBaseService.RunRecordByList(param);
		if(rc==null){
			return "pages/base/rc_create_show_new";
		}
	//	int vpTotal = newBaseService.RunRecordByListTotal(param);
		
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		List<BasePipelineSection> sectionList = newBaseService.querySection(rc.getPl_id());
		List<BasePipelineSpec> specList = newBaseService.querySpec(rc.getPl_section_id());

		//获取用户角色ID
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);
		param.put("pl", rc.getPl_id());
		param.put("spec", rc.getPl_spec_id());
		param.put("section", rc.getPl_section_id());
		
		if(verify != null && verify.intValue() == 1)
		{
			//param.put("status", 0);
			Map<String, Object> param0 = new HashMap<String, Object>(param);			
			PageSupport ps = PageSupport.initPageSupport(request);
			ps.setPageSize(10000);
			List<RunRecordDetail> rrdList1 = newBaseService.queryRunRecordDetail(param0, ps);
			
			List<Integer> rrIdList = null;
			Object obj = request.getSession().getAttribute(ud.getId() + "rrIdList");
			if (obj != null) {
				rrIdList = (List<Integer>)obj;
			} else {
				request.getSession().removeAttribute(ud.getId() + "rrIdList");
				rrIdList = new ArrayList<Integer>();
			}
			if (!CollectionUtils.isEmpty(rrdList1)) {
				for (RunRecordDetail rrd : rrdList1) {
					if (!rrIdList.contains(rrd.getR_id())) {
						rrIdList.add(rrd.getR_id());
					}
				}
				request.getSession().setAttribute(ud.getId() + "rrIdList", rrIdList);
			}
		}
		{
			PageSupport ps = PageSupport.initPageSupport(request);
			
			ps.setPageSize(32);
			List<RunRecordDetail> rcd = newBaseService.queryRunRecordDetail(param, ps);
			for(RunRecordDetail p : rcd)
			{
				newBaseService.updateCheckRunRecordDetailStatus(p);
				p.resetCanEidt(role);           
			}
			
			if(rcd.size() < ps.getPageSize())
			{
				for(int i = rcd.size(); i < ps.getPageSize(); ++i)
				{
					RunRecordDetail n = new RunRecordDetail();
					rcd.add(n);
				}
			}
			model.addAttribute("rcd", rcd);
		}
		
		model.addAttribute("rc", rc);
		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("section", rc.getPl_section_id());
		
	//	model.addAttribute("total", vpTotal);
	//	model.addAttribute("offset", offset);
		model.addAttribute("id", id);
		model.addAttribute("verify", verify);
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);
	
		return "pages/base/rc_create_show_new";
	}
	
	/**
	 * 阴极保护站运行记录保存
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/save", method = RequestMethod.POST)
	public String rc_save_merge(Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam Integer pl,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam String auditor,
			@RequestParam String r_month, @RequestParam Integer[] day,
			@RequestParam Integer[] time, @RequestParam Float[] jldy,
			@RequestParam Float[] zlsc_a, @RequestParam Float[] zlsc_v,
			@RequestParam Float[] tdddw, @RequestParam String[] recorder,@RequestParam String up_id,
			@RequestParam String[] comment, @RequestParam(required=false) String[] others,
			@RequestParam(required=false) String status
			,@RequestParam(required = false) Integer[] sign
			) {
		RunRecord rc = new RunRecord();
		rc.setId(id);
		rc.setAuditor(auditor);
		rc.setJinzhan(jinzhan);
		rc.setPl_id(pl);
		rc.setPl_section_id(section);
		rc.setPl_spec_id(spec);
		rc.setR_month(Integer.valueOf(r_month.replace(",", "")));
		rc.setStatus(0);
		rc.setUp_id(up_id);
		rc.setStatus(Integer.parseInt(status));
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		rc.setCreater(ud.getId());
		
		List<RunRecordDetail> rcdList = new ArrayList<RunRecordDetail>();
		int i = 0;
		int used = -1;
		for(Integer s : sign)
		{
			boolean canEdit = s.intValue() != 0;//该行可编辑
			if(canEdit)
			{
				if(used >= day.length)
					break;
				++used;
			}				
			do{
				if(used >= day.length || used >= time.length || used < 0)
					break;
				Integer d = day[used];
				Integer t = time[used];
				boolean daytimeisnull = (d==null || d.toString().isEmpty()) || (t==null || t.toString().isEmpty());
				if((canEdit && daytimeisnull ) || (canEdit==false && StringUtils.isBlank(comment[i])))
					break;
				if ((canEdit && daytimeisnull == false) || !StringUtils.isBlank(comment[i])) {
					RunRecordDetail rcd = new RunRecordDetail();
					if (daytimeisnull == false && canEdit) {
						rcd.setDay(day[used]);
						rcd.setTime(time[used]);
						rcd.setJldy(jldy[used]);
						rcd.setRecorder(recorder[used]);
						rcd.setTdddw(tdddw[used]);
						rcd.setZlsc_a(zlsc_a[used]);
						rcd.setZlsc_v(zlsc_v[used]);
						
						rcd.setStatus(Integer.parseInt(status));
						if(others != null) {
							rcd.setOthers(others[used]);
						}						
					}

					//添加状态值,-2表示草稿 -1表示待审核
					rcd.setStatus(Integer.parseInt(status));
					
					rcd.setComment(StringUtils.isBlank(comment[i]) ? "" : comment[i]);
					rcdList.add(rcd);
				}
			}while(false);
			++i;
		}
		if (rcdList.size() > 0) {
			newBaseService.saveRcRecord(rc, rcdList);
		}
		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}
		
		return "redirect:/admin/base/new/rc/"+page;
	}
	
	/**
	 * 阴极保护站运行记录详细修改
	 * 
	 */
	@RequestMapping(value = "/rc/modify", method = RequestMethod.POST)
	public String rc_modify(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer id,		
			@RequestParam(required = false) String r_month,
			@RequestParam(required = false) Integer verify,
			@RequestParam(required=false) Integer tips_id,
			@RequestParam(required = false) Integer[] rcd_id,

			@RequestParam Integer[] day,
			@RequestParam Integer[] time, @RequestParam Float[] jldy,
			@RequestParam Float[] zlsc_a, @RequestParam Float[] zlsc_v,
			@RequestParam Float[] tdddw, @RequestParam String[] recorder,
			@RequestParam String[] comment, @RequestParam(required=false) String[] others,
			@RequestParam(required=false) String status
			,@RequestParam(required = false) Integer[] sign
			)
			//@RequestParam(required = false) String up_id,@RequestParam(required = false) String status)
	{
		
		
		int i = 0;
		for (Integer rId : rcd_id)
		{
			RunRecordDetail rcd = new RunRecordDetail();
			rcd.setDay(day[i]);
			rcd.setTime(time[i]);
			rcd.setJldy(jldy[i]);
			rcd.setRecorder(recorder[i]);
			rcd.setTdddw(tdddw[i]);
			rcd.setZlsc_a(zlsc_a[i]);
			rcd.setZlsc_v(zlsc_v[i]);

			if(others != null) {
				rcd.setOthers(others[i]);
			}
			rcd.setId(rId);
			rcd.setStatus(new Integer(-2));	
			rcd.setComment(StringUtils.isBlank(comment[i]) ? "" : comment[i]);
			newBaseService.modifyRunRecordDetail(rcd);
			++i;
		}	

		String page = "";
		if(id!=null && id.intValue() > 0)
		{
			if(page.isEmpty())
				page += ".jhtml?";
			else
				page += "&";
			page += "id="+id.toString();
		}
		if(r_month!=null && StringUtil.isBlank(r_month)==false)
		{
			if(page.isEmpty())
				page += ".jhtml?";
			else
				page += "&";
			page += "r_month="+r_month;
		}
		if(verify!=null)
		{
			if(page.isEmpty())
				page += ".jhtml?";
			else
				page += "&";
			page += "verify="+verify.toString();
		}
		if(tips_id!=null)
		{
			if(page.isEmpty())
				page += ".jhtml?";
			else
				page += "&";
			page += "tips_id="+tips_id.toString();
		}
		
		return "redirect:/admin/base/new/rc/show"+page;//"pages/base/pl_patrol_show_new";
	}
	
	/**
	 * 阴极保护站运行记录详细更改状态
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/rc/batch_changestatus", method = {RequestMethod.GET,RequestMethod.POST})
	public String rc_batch_changestatus(Authentication authentication,HttpServletRequest request,
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
		
		CommonsMethod.getDataByRole(authentication, userService, param);
		RunRecord rc = newBaseService.RunRecordByList(param);
		
		if(rc!=null)
		{
			param.put("pl", rc.getPl_id());
			param.put("spec", rc.getPl_spec_id());
			param.put("section", rc.getPl_section_id());
			List<RunRecordDetail> rcd = newBaseService.queryRunRecordDetail(param);
			for(RunRecordDetail p : rcd)
			{
				if(Arrays.asList(oldStatus).contains(p.getStatus()))
				{
					newBaseService.updateChangeRunRecordDetailStatus(new Integer(p.getId()), newStatus);   
				}
			}
		}
		
		String page = "";
		if(id!=null && id.intValue() > 0)
		{
			if(page.isEmpty())
				page += ".jhtml?";
			page += "id="+id.toString();
		}
		if(r_month!=null && StringUtil.isBlank(r_month)==false)
		{
			if(page.isEmpty())
				page += ".jhtml?";
			page += "r_month="+r_month;
		}
		if(verify!=null)
		{
			if(page.isEmpty())
				page += ".jhtml?";
			page += "verify="+verify.toString();
		}
		if(tips_id!=null)
		{
			if(page.isEmpty())
				page += ".jhtml?";
			page += "tips_id="+tips_id.toString();
		}
		return "redirect:/admin/base/new/rc/show"+page;
	}
	
	/**
	 * 审核
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/rc/show_verify_old", method = RequestMethod.GET)
	public String rc_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLine();
		RunRecord rc = newBaseService.queryRunRecordById(id);
		List<BasePipelineSection> sectionList = newBaseService.querySection(rc
				.getPl_id());
		List<BasePipelineSpec> specList = newBaseService.querySpec(rc
				.getPl_section_id());

		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", rc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("rc", rc);
		model.addAttribute("verify", 1);
		return "pages/base/rc_create_show_new";
	}
	
	/**
	 * 审核
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/rc/show_verify", method = {RequestMethod.GET, RequestMethod.POST})
	public String rc_show_verify_merge(Model model, HttpServletRequest request, Authentication authentication,
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
		RunRecord rc = newBaseService.queryRunRecordMergeInfo(param);
		ps.setPageSize(10000);
		List<RunRecordDetail> rrdList1 = newBaseService.queryRunRecordDetailMerge(param, ps);
		ps.setPageSize(34);
		List<RunRecordDetail> rrdList = newBaseService.queryRunRecordDetailMerge(param, ps);

		List<Integer> rrIdList = null;
		Object obj = request.getSession().getAttribute(ud.getId() + "rrIdList");
		if (obj != null && f == null) {
			rrIdList = (List<Integer>)obj;
		} else {
			request.getSession().removeAttribute(ud.getId() + "rrIdList");
			rrIdList = new ArrayList<Integer>();
		}
		if (!CollectionUtils.isEmpty(rrdList1)) {
			for (RunRecordDetail rrd : rrdList1) {
				if (!rrIdList.contains(rrd.getR_id())) {
					rrIdList.add(rrd.getR_id());
				}
			}
			request.getSession().setAttribute(ud.getId() + "rrIdList", rrIdList);
		}

		model.addAttribute("rrdList", rrdList);
		model.addAttribute("rc", rc);
		model.addAttribute("pl_id", pl_id);
		model.addAttribute("pl_section_id", pl_section_id);
		model.addAttribute("pl_spec_id", pl_spec_id);
		return "pages/base/rc_verify_show_merge";
	}
	
	
	/**
	 * 阴极保护站运行记录导出
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/exp", method = RequestMethod.GET)
	public String rc_export(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String r_month) {
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

		if (!StringUtils.isBlank(r_month)) {
			param.put("r_month", Integer.valueOf(r_month.replace("-", "")));
			paramD.put("r_month", Integer.valueOf(r_month.replace("-", "")));
		}

		List<RunRecord> rcList = newBaseService.queryRunRecord(param, null);
		if (rcList.size() == 0) {
			return "redirect:/admin/base/new/rc/list";
		}

		List<RunRecordDetail> rcdList = newBaseService.queryRunRecordDetail(paramD, null);
		int datas_row = 32;//每页数据行数
		
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
			sheet1.setColumnWidth(2, 10 * 256);
			sheet1.setColumnWidth(3, 7 * 256);
			sheet1.setColumnWidth(4, 7 * 256);
			sheet1.setColumnWidth(5, 10 * 256);
			sheet1.setColumnWidth(6, 10 * 256);
			sheet1.setColumnWidth(7, 32 * 256);

			int row_index = 0;
			int pages = (rcdList.size()+datas_row-1)/datas_row;
			RunRecord p = rcList.get(0);
			RunRecord rc = newBaseService.queryRunRecordById(p.getId());
			for(int iPage = 0; iPage < pages; ++iPage) {
			//for (RunRecord rc : rcList) {
				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 7));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 0, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 3, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+2, 0, 1));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+2, 3, 4));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 2, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 5, 5));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 6, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 7, 7));
				//sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+3, 8, 8));

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

				cell = row.createCell(7);
				cell.setCellValue(rc.getR_month().toString().substring(0, 4)
						+ " 年 " + rc.getR_month().toString().substring(4, 6)
						+ " 月 ");
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				//RunRecord rcd = newBaseService.queryRunRecordById(rc.getId());
				
				Integer sz = 32;
				/*if(rc.getDetailList().size() > 32) {
					sz = rcd.getDetailList().size();
				}*/
				
				int end_row = 0;
				// 添加边框线
				for (int rown = 0; rown < sz + 2; rown++) {
					row = sheet1.createRow(rown+row_index);
					row.setHeightInPoints((float)17.25);
					for (int celln = 0; celln < 8; celln++) {
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
					end_row = rown + row_index;
				}

				//sheet1.addMergedRegion(new CellRangeAddress(row_index+2, sz + row_index+1, 7, 7));
				//sheet1.addMergedRegion(new CellRangeAddress(sz + row_index+2, sz + row_index+2, 7, 8));
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

				/*cell = row.getCell(8);
				cell.setCellValue("维护换机等情况");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);*/

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
				
				System.out.println("row_index=" + row_index + "|" + rc.getComment());
				row = sheet1.getRow(row_index);
				cell = row.getCell(7);
				cell.setCellValue(rc.getComment());
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				//int index = 4;
				//for (RunRecordDetail pd : rcd.getDetailList()) {
				for(int iRow = 0; iRow < datas_row; ++iRow){
					int detailIndex = iPage*datas_row + iRow;
					if(detailIndex >= rcdList.size())
						break;
					RunRecordDetail pd = rcdList.get(detailIndex);
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
					if (pd.getRecorder() != null) {
						cell.setCellValue(pd.getComment());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}

					/*cell = row.getCell(7);
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
					}*/
				}
				sheet1.setRowBreak(end_row + 1);
				row_index = end_row + 2;
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

	@RequestMapping(value = "/rc/verify_save_old", method = RequestMethod.POST)
	public @ResponseBody String rc_verify_save(Model model,
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		newBaseService.updateRunRecordVerifyStatus(id, ud.getId(), status,
				verify_desc);

		return JsonUtil.toJson("OK");
	}

	@RequestMapping(value = "/rc/verify_save", method = RequestMethod.POST)
	public @ResponseBody String rc_verify_save_merge(Model model, HttpServletRequest request, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object obj = request.getSession().getAttribute(ud.getId() + "rrIdList");
		if (obj != null) {
			List<Integer> rrIdList = (List<Integer>)obj;
			for (Integer rrId : rrIdList)
				newBaseService.updateRunRecordVerifyStatus(rrId, ud.getId(), status, verify_desc);
			request.getSession().removeAttribute(ud.getId() + "rrIdList");
		}
		/*if(status!=null){
			RunRecord p = baseRunRecordNewDao.queryRunRecordById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的阴极保护站运行记录已审核通过！";
			} else {
				content = "您提交的阴极保护站运行记录未审核通过！";
			}
				saveTips(content, p.getCreater(), "/admin/base/rc/show?id=" + id);
				//eventDao.updateEventStatus(status, message, event_id, type_id);
			}*/
		return JsonUtil.toJson("OK");
	}
	
	/**
	 * 阴极保护站运行记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/edit_old", method = RequestMethod.GET)
	public String rc_edit(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLine();
		RunRecord rc = newBaseService.queryRunRecordById(id);
		List<BasePipelineSection> sectionList = newBaseService.querySection(rc
				.getPl_id());
		List<BasePipelineSpec> specList = newBaseService.querySpec(rc
				.getPl_section_id());

		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", rc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("rc", rc);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}

		return "pages/base/rc_edit_new";
	}
	
	/**
	 * 阴极保护站运行记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rc/edit", method = RequestMethod.GET)
	public String rc_edit_merge(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLine();
		RunRecord rc = newBaseService.queryRunRecordById(id);
		List<BasePipelineSection> sectionList = newBaseService.querySection(rc.getPl_id());
		List<BasePipelineSpec> specList = newBaseService.querySpec(rc.getPl_section_id());

		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", rc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("rc", rc);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/rc_edit_merge";
	}
	
	@RequestMapping(value = "/rc/del", method = RequestMethod.GET)
	public String rc_del(Model model, @RequestParam Integer id) {
		newBaseService.deleteRunRecordById(id);
		return "redirect:/admin/base/new/rc/list?del=1";
	}
	
}
