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
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
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
import cn.hm.oil.domain.RoutineAttention;
import cn.hm.oil.domain.RoutineAttentionDetail;
import cn.hm.oil.domain.RunRecordDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseRoutineAttentionService;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.NewBaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.CommonsMethod;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

@Controller
@RequestMapping(value = "/admin/base")
public class RoutineAttentionController {
	@Autowired
	private NewBaseService newBaseService;
	
	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;

	@Autowired
	private BaseRoutineAttentionService baseRoutineAttentionService;

	@RequestMapping(value = "/routine/create")
	public String routine_create(Model model, @RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		Prompt prompt = baseService.quertPromptByType(23);
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
		return "pages/base/routine_create";
	}

	@RequestMapping(value = "/routine/getTime")
	public @ResponseBody JsonResWrapper GetTime(@RequestParam Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		RoutineAttention pc = baseRoutineAttentionService.queryRoutineAttentionById(id);
		pc.setCreate_t(DateFormatter.dateToString(pc.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
		if (pc.getVerify_time() != null) {
			pc.setVerify_t(DateFormatter.dateToString(pc.getVerify_time(), "yyyy-MM-dd HH:mm:ss"));
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pc);
		return response;
	}

	@RequestMapping(value = "/routine/setTime")
	public @ResponseBody JsonResWrapper setTime(@RequestParam Integer id, @RequestParam String create_t,
			@RequestParam String verify_t) {
		JsonResWrapper response = new JsonResWrapper();
		baseRoutineAttentionService.updateRoutineAttentionTime(id, create_t, verify_t);
		response.setStatus(ResponseStatus.OK);
		return response;
	}

	/**
	 * 管线日常维护工作记录保存
	 * 
	 * @param model
	 * @param id
	 * @param pl
	 * @param section
	 * @param spec
	 * @param jinzhan
	 * @param weihu
	 *            维护管线（段）
	 * @param year
	 * @param no
	 *            编号
	 * @param type
	 *            类别
	 * @param lczh
	 *            检查头里程桩号
	 * @param address
	 *            所处地址
	 * @param descrip
	 *            现状描述
	 * @param atten_date
	 *            维护时间
	 * @param content
	 *            工作内容
	 * @param person
	 *            填报人
	 * @return
	 */
	@RequestMapping(value = "/routine/save", method = RequestMethod.POST)
	public String routine_save(Model model, @RequestParam(required = false) Integer id, @RequestParam Integer pl,
			@RequestParam Integer section, @RequestParam Integer spec, @RequestParam String jinzhan,
			@RequestParam String weihu, @RequestParam Integer year, @RequestParam String[] no,
			@RequestParam String up_id, @RequestParam String[] type, @RequestParam String[] lczh,
			@RequestParam String[] address, @RequestParam String[] descrip, @RequestParam String[] atten_date,
			@RequestParam String[] content, @RequestParam String[] person,@RequestParam String status) {
		RoutineAttention rc = new RoutineAttention();
		if (id != null && id.intValue() > 0)
			rc.setId(id);
		rc.setPl_id(pl);
		rc.setPl_section_id(section);
		rc.setPl_spec_id(spec);
		rc.setJinzhan(jinzhan);
		rc.setWeihu(weihu);
		rc.setYear(year);
		rc.setStatus(Integer.parseInt(status));//添加状态值
		rc.setUp_id(up_id);
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		rc.setCreater(ud.getId());

		List<RoutineAttentionDetail> rcdList = new ArrayList<RoutineAttentionDetail>();
		int i = 0;
		for (String n : no) {
			if (n.equals("") || n == null) {
				continue;
			}
			RoutineAttentionDetail rcd = new RoutineAttentionDetail();
			rcd.setAddress(address[i]);
			rcd.setNo(n);
			rcd.setAtten_date(atten_date[i]);
			rcd.setContent(content[i]);
			rcd.setDescrip(descrip[i]);
			rcd.setLczh(lczh[i]);
			rcd.setPerson(person[i]);
			rcd.setType(type[i]);
			rcd.setStatus(Integer.parseInt(status));//添加状态值
			rcdList.add(rcd);
			i++;
		}
		String status1 = "0";
		if (rcdList.size() > 0) {
			rc.setDetaillist(rcdList);
		}
		try {
			baseRoutineAttentionService.saveRoutionAttention(rc);
			status1 = "1";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		String page = "create" + "?status=" + status1;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status1;
		}

		return "redirect:/admin/base/routine/" + page;
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
	@RequestMapping(value = "/routine/book_list", method = { RequestMethod.GET, RequestMethod.POST })
	public String routine_book_list(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) Integer st,
			@RequestParam(required = false) String year) {

		Map<String, Object> param = new HashMap<String, Object>();
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("user_id", ud.getId());
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

		if (!StringUtils.isBlank(year)) {
			param.put("year", year);
			model.addAttribute("year", year);
		}

		if (st != null && st.intValue() != 2) {
			param.put("status", st);
			model.addAttribute("st", st);
		}

		List<RoutineAttention> rcList = baseRoutineAttentionService.queryRoutineAttentionVerifyMerge(param);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);

		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("rcList", rcList);
		return "pages/base/routine_book_list";
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
	@RequestMapping(value = "/routine/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String routine_list(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String year,
			@RequestParam(required = false) Integer del,
			@RequestParam(required=false) Boolean all,
			@RequestParam(required=false) Integer verify) {
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
			List<BasePipelineSection> sectionList = newBaseService.querySectionByAdminRoutine(par);
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

		if (!StringUtils.isBlank(year)) {
			param.put("year", year);
			model.addAttribute("year", year);
		}
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		if(all != null)
		{
			model.addAttribute("all", all);
		}
		// 判读用户是否是维护工，维护工只能查看自己的数据
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByAdminRoutine(param);
		PageSupport ps = PageSupport.initPageSupport(request);
		List<RoutineAttention> rcList = baseRoutineAttentionService.queryRoutineAttention(param, ps);
		// 规格 显示列表
		List<BasePipelineSpec> specList = newBaseService.querySpecListNewByAdminRoutine(param);
		model.addAttribute("specLists", specList);
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("rcList", rcList);
		model.addAttribute("verify", verify);
		return "pages/base/routine_list";
	}
	
	@RequestMapping(value = "/routine/section/get", method = RequestMethod.GET)
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
			return newBaseService.querySectionByAdminRoutine(param);
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
	@RequestMapping(value = "/routine/verify_old", method = { RequestMethod.GET, RequestMethod.POST })
	public String routine_verify(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String year) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String, Object>());

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

		if (!StringUtils.isBlank(year)) {
			param.put("year", year);
			model.addAttribute("year", year);
		}

		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("verify", "1");
		param.put("user_id", ud.getId());
		PageSupport ps = PageSupport.initPageSupport(request);
		List<RoutineAttention> rcList = baseRoutineAttentionService.queryRoutineAttention(param, ps);

		Integer role = userService.queryRoleIdByUserId(ud.getId());

		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("rcList", rcList);
		model.addAttribute("verify", 1);
		return "pages/base/routine_list";
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
	@RequestMapping(value = "/routine/verify", method = { RequestMethod.GET, RequestMethod.POST })
	public String routine_verify_merge(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(required = false) Integer pl, 
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, 
			@RequestParam(required = false) String r_month) {
		/*Map<String, Object> param = new HashMap<String, Object>();
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("user_id", ud.getId());
		param.put("status", 0);
		List<RoutineAttention> rcList = baseRoutineAttentionService.queryRoutineAttentionVerifyMerge(param);

		Integer role = userService.queryRoleIdByUserId(ud.getId());

		model.addAttribute("role", role);
		model.addAttribute("rcList", rcList);
		return "pages/base/routine_verify_list";*/
		String pages="";
		pages = CommonsMethod.putUrlParam(pages, "pl", pl);
		pages = CommonsMethod.putUrlParam(pages, "section", section);
		pages = CommonsMethod.putUrlParam(pages, "spec", spec);
		pages = CommonsMethod.putUrlParam(pages, "r_month", r_month);
		pages = CommonsMethod.putUrlParam(pages, "verify", new Integer(1));
		pages = CommonsMethod.putUrlParam(pages, "all", new Boolean(false));
		return "redirect:/admin/base/routine/list"+pages;
	}

	/**
	 * 阴极保护站运行记录详细查看
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/routine/show", method = {RequestMethod.GET,RequestMethod.POST})
	public String routine_show(Model model, Authentication authentication, @RequestParam Integer id, HttpServletRequest request,
			@RequestParam(required = false) Integer tips_id,
			@RequestParam(required = false) String r_month,
			@RequestParam(required = false) Integer verify) {
		if (tips_id != null && tips_id.intValue() > 0) {
			baseService.deleteTips(tips_id);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		if(id != null)
		{
			param.put("id", id);
		}
		
		param.put("pl_spec_id", id);
		CommonsMethod.getDataByRole(authentication, userService, param);
		if (r_month != null && r_month.trim().length() != 0) {
			param.put("year", r_month.replace("-", ""));
		}

		List<RoutineAttention> rcs = baseRoutineAttentionService.queryRoutineAttention(param);
		

		if (rcs == null || rcs.size() <= 0) {
			return "pages/base/routine_show";
		}

		RoutineAttention rc = rcs.get(0);
		rc.setDetaillist(baseRoutineAttentionService.queryRoutineAttentionDetailByraid(rc.getId()));
		List<BasePipelineSection> sectionList = baseService.querySection(rc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(rc.getPl_section_id());
		
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
			List<RoutineAttentionDetail> rrdList1 = newBaseService.queryRoutineAttentionDetail(param0, ps);
			
			List<Integer> rrIdList = null;
			Object obj = request.getSession().getAttribute(ud.getId() + "radIdList");
			if (obj != null) {
				rrIdList = (List<Integer>)obj;
			} else {
				request.getSession().removeAttribute(ud.getId() + "radIdList");
				rrIdList = new ArrayList<Integer>();
			}
			if (!CollectionUtils.isEmpty(rrdList1)) {
				for (RoutineAttentionDetail rrd : rrdList1) {
					if (!rrIdList.contains(rrd.getId())) {
						rrIdList.add(rrd.getId());
					}
				}
				request.getSession().setAttribute(ud.getId() + "radIdList", rrIdList);
			}
			model.addAttribute("verify", verify);
		}
		
		{
			PageSupport ps = PageSupport.initPageSupport(request);
			ps.setPageSize(9);
			List<RoutineAttentionDetail> rad = newBaseService.queryRoutineAttentionDetail(param, ps);
			for(RoutineAttentionDetail p : rad)
			{
				newBaseService.updateCheckRoutineAttentionDetailStatus(p);
				p.resetCanEidt(role);           
			}
					
			if(rad.size() < ps.getPageSize())
			{
				for(int i = rad.size(); i < ps.getPageSize(); ++i)
				{
					RoutineAttentionDetail n = new RoutineAttentionDetail();
					rad.add(n);
				}
			}
			model.addAttribute("detaillist", rad);
		}
				
		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", rc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("rc", rc);
		//model.addAttribute("offset", ps.getPageOffset());
		//model.addAttribute("total", ps.getTotalPage());
		model.addAttribute("id", id);

		return "pages/base/routine_show"; 
	}
	
	/**
	 * 管道巡检工作记录修改
	 * 
	 */
	@RequestMapping(value = "/routine/modify", method = RequestMethod.POST)
	public String routine_modify(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String up_id, @RequestParam String[] no,
			@RequestParam String[] type, @RequestParam String[] lczh,
			@RequestParam String[] address, @RequestParam String[] descrip, @RequestParam String[] atten_date,
			@RequestParam String[] content, @RequestParam String[] person,		
			@RequestParam(required = false) String r_month,
			@RequestParam(required = false) Integer verify,
			@RequestParam(required=false) Integer tips_id,
			@RequestParam Integer[] rad_id
			)
			//@RequestParam(required = false) String up_id,@RequestParam(required = false) String status)
	{
		int i = 0;
		for (Integer rId : rad_id)
		{
			if(StringUtil.isBlank(no[i]) == false)
			{
				RoutineAttentionDetail rad = new RoutineAttentionDetail();
				rad.setAddress(address[i]);
				rad.setNo(no[i]);
				rad.setAtten_date(atten_date[i]);
				rad.setContent(content[i]);
				rad.setDescrip(descrip[i]);
				rad.setLczh(lczh[i]);
				rad.setPerson(person[i]);
				rad.setType(type[i]);
				rad.setId(rId);
				rad.setStatus(new Integer(-2));	
				newBaseService.modifyRoutineAttentionDetail(rad);
			}
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
		
		return "redirect:/admin/base/routine/show"+page;//"pages/base/pl_patrol_show_new";
	}
	
	/**
	 * 管道巡检工作记录批量更改状态
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/routine/batch_changestatus", method = {RequestMethod.GET,RequestMethod.POST})
	public String routine_batch_changestatus(Authentication authentication,HttpServletRequest request,
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
		List<RoutineAttention> rcs = baseRoutineAttentionService.queryRoutineAttention(param);
		

		if (rcs == null || rcs.size() <= 0) {
			return "pages/base/routine_show";
		}

		RoutineAttention rc = rcs.get(0);

		if(rc!=null)
		{
			param.put("pl", rc.getPl_id());
			param.put("spec", rc.getPl_spec_id());
			param.put("section", rc.getPl_section_id());
			List<RoutineAttentionDetail> rad = newBaseService.queryRoutineAttentionDetail(param);
			for(RoutineAttentionDetail p : rad)
			{
				if(Arrays.asList(oldStatus).contains(p.getStatus()))
				{
					newBaseService.updateChangeRoutineAttentionDetailStatus(new Integer(p.getId()), newStatus);   
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
		return "redirect:/admin/base/routine/show"+page;
	}

	/**
	 * 阴极保护站运行记录详细查看
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/routine/book_show", method = { RequestMethod.GET, RequestMethod.POST })
	public String routine_book_show(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam Integer pl_id, @RequestParam Integer pl_section_id, @RequestParam Integer pl_spec_id,
			@RequestParam(required = false) Integer status, @RequestParam(required = false) String year) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pl_id", pl_id);
		param.put("pl_section_id", pl_section_id);
		param.put("pl_spec_id", pl_spec_id);
		if (!StringUtils.isBlank(year)) {
			param.put("year", year);
			model.addAttribute("year", year);
		}

		if (status != null && (status.intValue() == -1 || status.intValue() == 0 || status.intValue() == 1)) {
			param.put("status", status);
			model.addAttribute("status", status);
		}
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("user_id", ud.getId());

		RoutineAttention ra = baseRoutineAttentionService.queryRoutineAttentionMergeInfor(param);
		PageSupport ps = PageSupport.initPageSupport(request);
		ps.setPageSize(9);
		List<RoutineAttentionDetail> radList = baseRoutineAttentionService.queryRoutineAttentionDetailVerifyMerge(param,
				ps);

		model.addAttribute("radList", radList);
		model.addAttribute("rc", ra);
		model.addAttribute("pl_id", pl_id);
		model.addAttribute("pl_section_id", pl_section_id);
		model.addAttribute("pl_spec_id", pl_spec_id);
		return "pages/base/routine_book_show";
	}

	/**
	 * 审核
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/routine/show_verify_old", method = RequestMethod.GET)
	public String routine_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String, Object>());
		RoutineAttention rc = baseRoutineAttentionService.queryRoutineAttentionById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(rc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(rc.getPl_section_id());

		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", rc.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("rc", rc);
		model.addAttribute("verify", 1);
		return "pages/base/routine_show";
	}

	/**
	 * 审核
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/routine/show_verify", method = { RequestMethod.GET, RequestMethod.POST })
	public String routine_show_verify_merge(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam Integer pl_id, @RequestParam Integer pl_section_id, @RequestParam Integer pl_spec_id,
			@RequestParam(required = false) Integer f) {
		PageSupport ps = PageSupport.initPageSupport(request);
		ps.setPageSize(9);

		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", 0);
		param.put("pl_id", pl_id);
		param.put("pl_section_id", pl_section_id);
		param.put("pl_spec_id", pl_spec_id);
		param.put("user_id", ud.getId());
		RoutineAttention rc = baseRoutineAttentionService.queryRoutineAttentionMergeInfor(param);
		
		Map<String, Object> param1 = new HashMap<String,Object>(param);
		param1.put("limit", 10000);
		param1.put("offset", 0);
		param1.put("order", 1);
		List<RoutineAttentionDetail> radList1 = baseRoutineAttentionService.queryRoutineAttentionDetailVerifyMerge(param1);
		
		List<RoutineAttentionDetail> radList = baseRoutineAttentionService.queryRoutineAttentionDetailVerifyMerge(param,
				ps);

		List<Integer> radIdList = null;
		Object obj = request.getSession().getAttribute(ud.getId() + "radIdList");
		if (obj != null && f == null) {
			radIdList = (List<Integer>) obj;
		} else {
			request.getSession().removeAttribute(ud.getId() + "radIdList");
			radIdList = new ArrayList<Integer>();
		}
		if (!CollectionUtils.isEmpty(radList1)) {
			for (RoutineAttentionDetail rad : radList1) {
				if (!radIdList.contains(rad.getRa_id())) {
					radIdList.add(rad.getRa_id());
				}
			}
			request.getSession().setAttribute(ud.getId() + "radIdList", radIdList);
		}
		
		int d = ps.getPageSize() - radList.size();
		if (d > 0) {
			for (int i = 0; i < d; i++) {
				RoutineAttentionDetail e = new RoutineAttentionDetail();
				radList.add(e);
			}
		}

		model.addAttribute("radList", radList);
		model.addAttribute("rc", rc);
		model.addAttribute("pl_id", pl_id);
		model.addAttribute("pl_section_id", pl_section_id);
		model.addAttribute("pl_spec_id", pl_spec_id);
		return "pages/base/routine_verify_show";
	}

	/**
	 * 阴极保护站运行记录导出
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/routine/exp", method = RequestMethod.GET)
	public String routine_export(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String year) {
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

		if (!StringUtils.isBlank(year)) {
			param.put("year", year);
			//paramD.put("r_month", Integer.valueOf(p_month.replace("-", "")));
		}

		List<RoutineAttention> rcList = baseRoutineAttentionService.queryRoutineAttention(param);
		if (rcList.size() == 0) {
			return "redirect:/admin/base/routine/list";
		}
		
		List<RoutineAttentionDetail> rcdList = newBaseService.queryRoutineAttentionDetail(paramD, null);
		int datas_row = 9;//每页数据行数
		
		
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
			FileOutputStream fileOut = new FileOutputStream(path + sep + "excel" + ".xls");

			HSSFSheet sheet1 = work.createSheet("sheet1");
			sheet1.setMargin(HSSFSheet.TopMargin, 0.5);// 页边距（上）
			// sheet1.setMargin(HSSFSheet.BottomMargin,);// 页边距（下）
			sheet1.setMargin(HSSFSheet.LeftMargin, 0.6);// 页边距（左）
			// sheet1.setMargin(HSSFSheet.RightMargin,20);// 页边距（右

			HSSFPrintSetup ps = sheet1.getPrintSetup();
			ps.setLandscape(true); // 打印方向，true：横向，false：纵向(默认)
			ps.setVResolution((short) 600);
			ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); // 纸张类型

			HSSFCell cell;
			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 表头格式
			CellStyle titleStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();
			// 左对齐
			CellStyle leftStyle = work.createCellStyle();
			// 左对齐边框
			CellStyle leftBorder = work.createCellStyle();

			// 内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			// dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			// dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			// dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			// dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

			// 内容加上边框
			leftBorder.setBorderBottom(CellStyle.BORDER_THIN);
			leftBorder.setBorderLeft(CellStyle.BORDER_THIN);
			leftBorder.setBorderRight(CellStyle.BORDER_THIN);
			leftBorder.setBorderTop(CellStyle.BORDER_THIN);
			leftBorder.setAlignment(CellStyle.ALIGN_LEFT);
			leftBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			leftBorder.setWrapText(true);

			// 居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle.setWrapText(true);

			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);

			leftStyle.setAlignment(CellStyle.ALIGN_LEFT);
			leftStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			leftStyle.setWrapText(true);

			// 标题字体
			Font font = work.createFont();
			// 表头字体
			Font titlefont = work.createFont();
			// 内容字体
			Font datafont = work.createFont();

			font.setFontHeightInPoints((short) 20);
			font.setFontName("方正大黑简体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			titlefont.setFontHeightInPoints((short) 10);
			titlefont.setFontName("方正大黑简体");
			titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");
			datafont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			// datafont.setColor(HSSFColor.RED.index);

			// 把字体加入到格式中
			cellStyle.setFont(font);
			titleStyle.setFont(titlefont);
			dataStyle.setFont(datafont);
			leftBorder.setFont(datafont);
			leftStyle.setFont(titlefont);

			// 设置列宽度
			sheet1.setColumnWidth(0, (int) (4.88 * 295));
			sheet1.setColumnWidth(1, (int) (8.50 * 295));
			sheet1.setColumnWidth(2, (int) (9.38 * 295));
			sheet1.setColumnWidth(3, (int) (23.75 * 295));
			sheet1.setColumnWidth(4, (int) (25.50 * 295));
			sheet1.setColumnWidth(5, (int) (8.50 * 295));
			sheet1.setColumnWidth(6, (int) (27.50 * 295));
			sheet1.setColumnWidth(7, (int) (7.75 * 295));

			int row_index = 0;
			//for (RoutineAttention rc : rcList) {
			int pages = (rcdList.size()+datas_row-1)/datas_row;
			RoutineAttention p = rcList.get(0);
			RoutineAttention rc = baseRoutineAttentionService.queryRoutineAttentionById(p.getId());
			for(int iPage = 0; iPage < pages; ++iPage) {
				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 7));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 1, row_index + 1, 0, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 1, row_index + 1, 3, 4));
				sheet1.addMergedRegion(new CellRangeAddress(row_index + 1, row_index + 1, 5, 6));

				// 第一行
				HSSFRow row = sheet1.createRow(row_index);
				row.setHeightInPoints((float) 36.0);
				row_index++;
				// 新建单元格
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue("管线日常维护工作记录");
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
				cell.setCellStyle(leftStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(3);
				cell.setCellValue("管线名称及规格 " + rc.getPl_name() + "/" + rc.getPl_spec_name());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(5);
				cell.setCellValue("维护管线（段） " + rc.getWeihu());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(7);
				cell.setCellValue(rc.getYear() + "年");
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				//RoutineAttention ra = baseRoutineAttentionService.queryRoutineAttentionById(rc.getId());

				Integer sz = 9;
			/*	if (ra.getDetaillist().size() > 8) {
					sz = ra.getDetaillist().size();
				}*/

				int end_row = 0;
				// 添加边框线
				for (int rown = 0; rown < sz + 2; rown++) {
					row = sheet1.createRow(rown + row_index);
					row.setHeightInPoints((float) 51.75);
					for (int celln = 0; celln < 8; celln++) {
						cell = row.createCell(celln);
						cell.setCellStyle(dataStyle);
					}
				}
				end_row = sz + row_index + 2;
				sheet1.addMergedRegion(new CellRangeAddress(end_row - 1, end_row - 1, 0, 7));

				// 审核人行
				row = sheet1.getRow(row_index);
				row.setHeightInPoints(33);
				row_index++;
				cell = row.getCell(0);
				cell.setCellValue("编号");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(1);
				cell.setCellValue("类别");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(2);
				cell.setCellValue("检查头\n里程桩号");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(3);
				cell.setCellValue("所处地址");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(4);
				cell.setCellValue("现状描述");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(5);
				cell.setCellValue("维护时间");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(6);
				cell.setCellValue("维护工作内容");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.getCell(7);
				cell.setCellValue("填报人");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				DateFormatter df = new DateFormatter();

				// int index = 4;
				//for (RoutineAttentionDetail pd : ra.getDetaillist()) {
				for(int iRow = 0; iRow < datas_row; ++iRow){
					int detailIndex = iPage*datas_row + iRow;
					if(detailIndex >= rcdList.size())
						break;
					RoutineAttentionDetail pd = rcdList.get(detailIndex);
					
					row = sheet1.getRow(row_index++);
					// row.setHeightInPoints(18);
					cell = row.getCell(0);
					if (pd.getNo() != null) {
						cell.setCellValue(pd.getNo());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					cell = row.getCell(1);
					if (pd.getType() != null) {
						cell.setCellValue(pd.getType());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					cell = row.getCell(2);
					if (pd.getLczh() != null) {
						cell.setCellValue(pd.getLczh());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					cell = row.getCell(3);
					if (pd.getAddress() != null) {
						cell.setCellValue(pd.getAddress());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					cell = row.getCell(4);
					if (pd.getDescrip() != null) {
						cell.setCellValue(pd.getDescrip());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					cell = row.getCell(5);
					if (pd.getAtten_date() != null) {
						cell.setCellValue(pd.getAtten_date().replaceAll("-", "."));
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					cell = row.getCell(6);
					if (pd.getContent() != null) {
						cell.setCellValue(pd.getContent());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					cell = row.getCell(7);
					if (pd.getPerson() != null) {
						cell.setCellValue(pd.getPerson());
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
				}
				row = sheet1.getRow(end_row - 1);
				row.setHeightInPoints(99);
				cell = row.getCell(0);
				cell.setCellValue(
						"    注：1.根据《西南油气田公司天然气管道保护工工作质量标准》，日常维护工作共分为“管线露管、浮管”、“管线标识、警示标志”、“管线5m范围内深根植物”、“线路阀室（井）”、“护坡堡坎”、“穿跨越管道”、“管道检测与修复”、“阴极保护”、“管道风险等级变化”等9个类别，应按上述类别及相应工作标准填写。2.各项内容不得漏填；该栏内容填满直接在下栏继续填写。");
				cell.setCellStyle(leftBorder);
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

	@RequestMapping(value = "/routine/verify_save_old", method = RequestMethod.POST)
	public @ResponseBody String routine_verify_save(Model model, @RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		baseRoutineAttentionService.updateRoutineAttentionVerifyStatus(id, ud.getId(), status, verify_desc);

		return JsonUtil.toJson("OK");
	}

	@RequestMapping(value = "/routine/verify_save", method = RequestMethod.POST)
	public @ResponseBody String routine_verify_save_merge(Model model, HttpServletRequest request,
			@RequestParam Integer status, @RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object obj = request.getSession().getAttribute(ud.getId() + "radIdList");
		if (obj != null) {
			List<Integer> radIdList = (List<Integer>) obj;
			for (Integer radId : radIdList)
				baseRoutineAttentionService.updateRoutineAttentionVerifyStatus(radId, ud.getId(), status, verify_desc);
			request.getSession().removeAttribute(ud.getId() + "radIdList");
		}

		return JsonUtil.toJson("OK");
	}

	/**
	 * 阴极保护站运行记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/routine/edit", method = RequestMethod.GET)
	public String routine_edit(Model model, @RequestParam Integer id, @RequestParam(required = false) String status) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String, Object>());
		RoutineAttention rc = baseRoutineAttentionService.queryRoutineAttentionById(id);
		List<BasePipelineSection> sectionList = baseService.querySection(rc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(rc.getPl_section_id());

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

		String[] temp = new String[9 - rc.getDetaillist().size()];
		model.addAttribute("temp", temp);
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<Integer, Integer> m = userService.getUsersRef();
		if (m != null && m.containsKey(ud.getId())) {
			model.addAttribute("up_id", m.get(ud.getId()));
		}
		return "pages/base/routine_edit";
	}

	@RequestMapping(value = "/routine/del", method = RequestMethod.GET)
	public String routine_del(Model model, @RequestParam Integer id) {
		baseRoutineAttentionService.deleteRoutineAttentionById(id);
		return "redirect:/admin/base/routine/list?del=1";
	}
}
