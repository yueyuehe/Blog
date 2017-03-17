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
import cn.hm.oil.domain.Construction;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.ConstructionService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.CommonsMethod;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

/**
 * 第三方施工协调工作记录
 * @author Admin
 *
 */

@Controller
@RequestMapping(value = "/admin/base")
public class ConstructionController {

	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ConstructionService constructionService;
	
	@RequestMapping(value="/constru/create")
	public String constru_create(Model model, @RequestParam(required = false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = baseService.queryLeaderList();
		model.addAttribute("br", br);
		Prompt prompt = baseService.quertPromptByType(24);
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
		return "pages/base/constru_create";
	}
	
	@RequestMapping(value="/constru/getTime")
	public @ResponseBody JsonResWrapper GetTime(@RequestParam Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		Construction pc = constructionService.queryConstructionById(id);
		pc.setCreate_t(DateFormatter.dateToString(pc.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
		if(pc.getVerify_time() != null){
			pc.setVerify_t(DateFormatter.dateToString(pc.getVerify_time(), "yyyy-MM-dd HH:mm:ss"));
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pc);
		return response;
	}
	
	@RequestMapping(value="/constru/setTime")
	public @ResponseBody JsonResWrapper setTime(@RequestParam Integer id,@RequestParam String create_t,@RequestParam String verify_t) {
		JsonResWrapper response = new JsonResWrapper();
		constructionService.updateConstructionTime(id, create_t, verify_t);
		response.setStatus(ResponseStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/constru/save", method = RequestMethod.POST)
	public String construSave(Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam Integer pl, @RequestParam String jingzhan,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String acquainted, @RequestParam String address,
			@RequestParam String chiefer, @RequestParam String con_date,
			@RequestParam String descrip, @RequestParam String lczh,
			@RequestParam String getporter, @RequestParam String ministry,
			@RequestParam String owner, @RequestParam String partic,
			@RequestParam String promoter, @RequestParam String reason,
			@RequestParam String recorder, @RequestParam String remethod,
			@RequestParam String reply, @RequestParam String reporter,@RequestParam String up_id,
			@RequestParam String result,
			@RequestParam(required=false) String status) {
		Construction rc = new Construction();
		if(id != null && id.intValue() > 0)
			rc.setId(id);
		rc.setPl_id(pl);
		rc.setPl_section_id(section);
		rc.setPl_spec_id(spec);
		//rc.setStatus(0);
		rc.setJingzhan(jingzhan);
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		rc.setCreater(ud.getId());
		rc.setAcquainted(acquainted);
		rc.setAddress(address);
		rc.setChiefer(chiefer);
		rc.setCon_date(DateFormatter.stringToDate(con_date.replace(",", "-"),"dd-MM-yyyy"));
		rc.setDescrip(descrip);
		rc.setGetporter(getporter);
		rc.setLczh(lczh);
		rc.setMinistry(ministry);
		rc.setOwner(owner);
		rc.setPartic(partic);
		rc.setPromoter(promoter);
		rc.setReason(reason);
		rc.setRecorder(recorder);
		rc.setRemethod(remethod);
		rc.setReply(reply);
		rc.setReporter(reporter);
		rc.setResult(result);
		rc.setUp_id(up_id);
		rc.setStatus(Integer.parseInt(status));
		
		String status1 = "0";
		
		try {
			constructionService.saveConstruction(rc);
			status1 = "1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		String page = "create" + "?status=" + status1;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status1;
		}
		
		return "redirect:/admin/base/constru/"+page;
	}
	
	@RequestMapping(value = "/constru/modify", method = RequestMethod.POST)
	public String construModify(Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer rid,
			@RequestParam Integer pl, @RequestParam String jingzhan,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String acquainted, @RequestParam String address,
			@RequestParam String chiefer, @RequestParam String con_date,
			@RequestParam String descrip, @RequestParam String lczh,
			@RequestParam String getporter, @RequestParam String ministry,
			@RequestParam String owner, @RequestParam String partic,
			@RequestParam String promoter, @RequestParam String reason,
			@RequestParam String recorder, @RequestParam String remethod,
			@RequestParam String reply, @RequestParam String reporter,@RequestParam String up_id,
			@RequestParam String result,
			@RequestParam(required=false) String status) {
		Construction rc = new Construction();
		if(rid != null && rid.intValue() > 0)
			rc.setId(rid);
		rc.setPl_id(pl);
		rc.setPl_section_id(section);
		rc.setPl_spec_id(spec);
		//rc.setStatus(0);
		rc.setJingzhan(jingzhan);
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		rc.setCreater(ud.getId());
		rc.setAcquainted(acquainted);
		rc.setAddress(address);
		rc.setChiefer(chiefer);
		rc.setCon_date(DateFormatter.stringToDate(con_date.replace(",", "-"),"dd-MM-yyyy"));
		rc.setDescrip(descrip);
		rc.setGetporter(getporter);
		rc.setLczh(lczh);
		rc.setMinistry(ministry);
		rc.setOwner(owner);
		rc.setPartic(partic);
		rc.setPromoter(promoter);
		rc.setReason(reason);
		rc.setRecorder(recorder);
		rc.setRemethod(remethod);
		rc.setReply(reply);
		rc.setReporter(reporter);
		rc.setResult(result);
		rc.setUp_id(up_id);
		rc.setStatus(Integer.parseInt(status));
		
		String status1 = "0";
		
		try {
			constructionService.saveConstruction(rc);
			status1 = "1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		String page = "show";
		if (id != null && id.intValue() > 0) {
			page = "show?id=" + id;
		}
		
		return "redirect:/admin/base/constru/"+page;
	}
	
	@RequestMapping(value = "/constru/batch_changestatus", method = {RequestMethod.GET,RequestMethod.POST})
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
		if(id != null)
		{
			param.put("id", id);
		}
		CommonsMethod.getDataByRole(authentication, userService, param);
		if(!StringUtils.isBlank(r_month)){
			param.put("r_month", r_month);
		}

		List<Construction> rcs = constructionService.queryConstruction_new(param);
		for(Construction p : rcs)
		{
			if(Arrays.asList(oldStatus).contains(p.getStatus()))
			{
				constructionService.updateChangeStatus(new Integer(p.getId()), newStatus);
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
		return "redirect:/admin/base/constru/show"+page;
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
	@RequestMapping(value = "/constru/list_old", method = { RequestMethod.GET, RequestMethod.POST })
	public String constru_list(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
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

		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		if (role == 3) {//维护工
			param.put("whCreater", ud.getId());
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Construction> rcList = constructionService.queryConstruction(param, ps);

		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("rcList", rcList);
		return "pages/base/constru_list";
	}
	
	/**
	 * 阴极保护站运行记录查看
	 * hansen
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param r_month
	 * @return
	 */
	@RequestMapping(value = "/constru/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String constru_list_new(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) Integer del,
			@RequestParam(required=false) Boolean all,
			@RequestParam(required = false) Integer verify) {
		
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Map<String, Object> param = new HashMap<String, Object>();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param, all);
		if(verify != null && verify.intValue() == 1)
			param.put("status", 0);
		else if(role==2)
		{
			param.put("status", "-1,0,1");
		}
		//规格查询条件		
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			//段落 下拉列表
			Map<String,Object> par = new HashMap<String,Object>(param);
			par.put("pl_id", pl);
			List<BasePipelineSection> sectionList = constructionService.querySectionByAdminConstruction(par);
			
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);

			//规格 下拉列表
			List<BasePipelineSpec> specs = baseService.querySpec(section);
			model.addAttribute("specs", specs);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}
		if(all != null)
		{
			model.addAttribute("all", all);
		}
		
		//获取用户角色ID
		model.addAttribute("role", role);
		
		//规格 显示列表
		List<BasePipelineSpec> specList = constructionService.querySpecListNewByAdminConstruction(param);
		model.addAttribute("specLists", specList);
		//管线 下拉列表
		List<BasePipeline> pipeLineList = constructionService.queryPipeLineByAdminConstruction(param);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("verify", verify);
		
		return "pages/base/constru_list";
	}
	
	@RequestMapping(value = "/constru/section/get", method = RequestMethod.GET)
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
			return constructionService.querySectionByAdminConstruction(param);
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
	@RequestMapping(value = "/constru/verify", method = { RequestMethod.GET,RequestMethod.POST })
	public String constru_verify(Model model, HttpServletRequest request,
								Authentication authentication,
								@RequestParam(required = false) Integer pl, 
								@RequestParam(required = false) Integer section,
								@RequestParam(required = false) Integer spec, 
								@RequestParam(required = false) String r_month) {
		//管线 下拉列表
		/*List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		
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
		
		if (!StringUtils.isBlank(r_month)) {
		param.put("r_month", r_month);
		model.addAttribute("r_month", r_month);
		}
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);
		
		List<BasePipelineSpec> specList = constructionService.queryConstructionAuditSpecList(param);
		
		model.addAttribute("verify", 1);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("specLists", specList);
				
		return "pages/base/constru_list";*/
		String pages="";
		pages = CommonsMethod.putUrlParam(pages, "pl", pl);
		pages = CommonsMethod.putUrlParam(pages, "section", section);
		pages = CommonsMethod.putUrlParam(pages, "spec", spec);
		pages = CommonsMethod.putUrlParam(pages, "r_month", r_month);
		pages = CommonsMethod.putUrlParam(pages, "verify", new Integer(1));
		pages = CommonsMethod.putUrlParam(pages, "all", new Boolean(false));
		return "redirect:/admin/base/constru/list"+pages;
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
	@RequestMapping(value = "/constru/verify_old", method = { RequestMethod.GET,RequestMethod.POST })
	public String constru_verify_old(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String year) {


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

		if (!StringUtils.isBlank(year)) {
			param.put("year", year);
			model.addAttribute("year", year);
		}

		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("verify", "1");
		param.put("user_id", ud.getId());
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Construction> rcList = constructionService.queryConstruction(param, ps);

		
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("rcList", rcList);
		model.addAttribute("verify", 1);
		return "pages/base/constru_list";
	}
		
	/**
	 * 阴极保护站运行记录详细查看
	 * hansen
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/constru/show", method = {RequestMethod.GET,RequestMethod.POST})
	public String constru_show(Model model, Authentication authentication,HttpServletRequest request,
							  @RequestParam Integer id, 
							  @RequestParam(required = false) String r_month,
							  @RequestParam(required = false) Integer verify,
							 // @RequestParam Integer offset, 
							  @RequestParam(required=false) Integer tips_id,
							  @RequestParam(required=false) Integer f) {
		
		if (tips_id != null && tips_id.intValue() > 0){
			baseService.deleteTips(tips_id);
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		if(id != null)
		{
			param.put("id", id);
		}
		
		CommonsMethod.getDataByRole(authentication, userService, param);
		if(!StringUtils.isBlank(r_month)){
			param.put("r_month", r_month);
			model.addAttribute("r_month", r_month);
		}
		
		{
			Map<String, Object> param1 = new HashMap<String, Object>(param);
			param1.put("limit", 10000);
			param1.put("offset", 0);
			List<Construction> rcs1 = constructionService.queryConstruction_new(param1);
			LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
			List<Integer> pmIdList = null;
			Object obj = request.getSession().getAttribute(ud.getId() + "pmIdList");
			if (obj != null && f == null) {
				pmIdList = (List<Integer>)obj;
			} else {
				request.getSession().removeAttribute(ud.getId() + "pmIdList");
				pmIdList = new ArrayList<Integer>();
			}
			if (!CollectionUtils.isEmpty(rcs1)) {
				for (Construction pmd : rcs1) {
					if (!pmIdList.contains(pmd.getId())) {
						pmIdList.add(pmd.getId());
					}
				}
				request.getSession().setAttribute(ud.getId() + "pmIdList", pmIdList);
			}
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		ps.setPageSize(1);
		List<Construction> rcs = constructionService.queryConstruction_new(param, ps);
		
		if(rcs==null || rcs.isEmpty()){
			return "pages/base/constru_show";
		}
		
		
		
		//int vpTotal = constructionService.queryConstructionByParamTotal(param);
		Construction rc = rcs.get(0);
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		List<BasePipelineSection> sectionList = baseService.querySection(rc.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(rc.getPl_section_id());

		//获取用户角色ID
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);
		rc.resetCanEidt(role);
		
		model.addAttribute("rc", rc);
		model.addAttribute("rcs", rcs);
		
	//	model.addAttribute("total", vpTotal);
	//	model.addAttribute("offset", offset);
		model.addAttribute("id", id);
		model.addAttribute("verify", verify);
		
		model.addAttribute("pl", rc.getPl_id());
		model.addAttribute("spec", rc.getPl_spec_id());
		model.addAttribute("section", rc.getPl_section_id());
		
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);

		return "pages/base/constru_show";
	}
	/**
	 * 阴极保护站运行记录详细查看
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/constru/show_old", method = RequestMethod.GET)
	public String constru_show_old(Model model, @RequestParam Integer id, @RequestParam(required=false) Integer tips_id) {
		if (tips_id != null && tips_id.intValue() > 0)
			baseService.deleteTips(tips_id);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		Construction rc = constructionService.queryConstructionById(id);
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

		return "pages/base/constru_show";
	}

	/**
	 * 审核
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/constru/show_verify", method = RequestMethod.GET)
	public String constru_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		Construction rc = constructionService.queryConstructionById(id);
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
		return "pages/base/constru_show";
	}
	
	
	/**
	 * 阴极保护站运行记录导出
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/constru/exp", method = RequestMethod.GET)
	public String constru_export(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String year) {
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

		if (!StringUtils.isBlank(year)) {
			param.put("year", year);
		}

		List<Construction> rcList = constructionService.queryConstruction(param, null);
		if (rcList.size() == 0) {
			return "redirect:/admin/base/constru/list";
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
			
			sheet1.setMargin(HSSFSheet.LeftMargin, 0.5);
			sheet1.setMargin(HSSFSheet.RightMargin, 0.45);
			
			HSSFPrintSetup ps = sheet1.getPrintSetup();    
            ps.setLandscape(false); // 打印方向，true：横向，false：纵向(默认)    
            ps.setVResolution((short)600);    
            ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //纸张类型    
			
			HSSFCell cell;
			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 表头格式
			CellStyle titleStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();
			//左对齐
			CellStyle leftStyle = work.createCellStyle();

			// 内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			// dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			// dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			// dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			// dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			leftStyle.setBorderBottom(CellStyle.BORDER_THIN);
			leftStyle.setBorderLeft(CellStyle.BORDER_THIN);
			leftStyle.setBorderRight(CellStyle.BORDER_THIN);
			leftStyle.setBorderTop(CellStyle.BORDER_THIN);

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
			leftStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
			leftStyle.setWrapText(true);

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
			leftStyle.setFont(datafont);

			// 设置列宽度
			sheet1.setColumnWidth(0, 10 * 256);
			sheet1.setColumnWidth(1, 5 * 256);
			sheet1.setColumnWidth(2, 10 * 256);
			sheet1.setColumnWidth(3, 8 * 256);
			sheet1.setColumnWidth(4, 22 * 256);
			sheet1.setColumnWidth(5, 10 * 256);
			sheet1.setColumnWidth(6, 28 * 256);

			int row_index = 0;
			for (Construction rc : rcList) {
				// 合并单元格
				sheet1.addMergedRegion(new CellRangeAddress(row_index, row_index, 0, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+1, row_index+1, 0, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+2, row_index+2, 1, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+3, row_index+3, 1, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+4, row_index+4, 1, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+5, row_index+5, 0, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+6, row_index+6, 0, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+7, row_index+7, 0, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+8, row_index+8, 0, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+9, row_index+9, 0, 1));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+9, row_index+9, 2, 4));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+10, row_index+10, 0, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+11, row_index+11, 1, 2));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+12, row_index+12, 0, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+13, row_index+13, 0, 6));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+14, row_index+14, 1, 4));
				sheet1.addMergedRegion(new CellRangeAddress(row_index+15, row_index+15, 1, 4));
				
				rc = constructionService.queryConstructionById(rc.getId());

				// 第一行
				HSSFRow row = sheet1.createRow(row_index);
				row.setHeightInPoints((float) 23.5);
				row_index++;
				// 新建单元格
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue("第三方施工协调工作记录");
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
				cell.setCellValue("井(站) ："  + rc.getJingzhan());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(6);
				cell.setCellValue(DateFormatter.dateToString(rc.getCon_date(), "yyyy年MM月dd日"));
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				for(int i = 0; i < 14; i++) {
					row = sheet1.createRow(i + row_index);
					for(int j = 0; j < 7; j++) {
						cell = row.createCell(j);
						cell.setCellStyle(dataStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
				}
				
				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(33);
				cell = row.getCell(0);
				cell.setCellValue("管线名称及规格");
				
				cell = row.getCell(1);
				cell.setCellValue(rc.getPl_name() + "-" + rc.getPl_section_name() + "-" + rc.getPl_spec_name());
				
				cell = row.getCell(3);
				cell.setCellValue("里程桩号");
				cell = row.getCell(4);
				cell.setCellValue(rc.getLczh());
				cell = row.getCell(5);
				cell.setCellValue("地 点");
				cell = row.getCell(6);
				cell.setCellValue(rc.getAddress());
				
				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(33);
				cell = row.getCell(0);
				cell.setCellValue("处理事由");
				cell = row.getCell(1);
				cell.setCellValue(rc.getReason());

				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(33);
				cell = row.getCell(0);
				cell.setCellValue("现场处理参加人员");
				cell = row.getCell(1);
				cell.setCellValue(rc.getPartic());

				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(22);
				cell = row.getCell(0);
				cell.setCellValue("第三方施工情况描述");

				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(110);
				cell = row.getCell(0);
				cell.setCellStyle(leftStyle);
				cell.setCellValue(rc.getDescrip());

				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(22);
				cell = row.getCell(0);
				cell.setCellValue("处理结果");

				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(110);
				cell = row.getCell(0);
				cell.setCellValue(rc.getResult());
				cell.setCellStyle(leftStyle);

				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(33);
				cell = row.getCell(0);
				cell.setCellValue("管道告知及管道保护宣传情况");
				
				cell = row.getCell(2);
				cell.setCellValue(rc.getAcquainted());
				
				cell = row.getCell(5);
				cell.setCellValue("受宣传人（签名）");
				cell = row.getCell(6);
				cell.setCellValue(rc.getPromoter());

				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(22);
				cell = row.getCell(0);
				cell.setCellValue("处理情况汇报");

				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(33);
				cell = row.getCell(0);
				cell.setCellValue("汇报人");
				cell = row.getCell(1);
				cell.setCellValue(rc.getReporter());
				cell = row.getCell(3);
				cell.setCellValue("受报人");
				cell = row.getCell(4);
				cell.setCellValue(rc.getGetporter());
				cell = row.getCell(5);
				cell.setCellValue("汇报方式");
				cell = row.getCell(6);
				cell.setCellValue(rc.getRemethod());
				
				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(22);
				cell = row.getCell(0);
				cell.setCellValue("答复意见");
				
				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(110);
				cell = row.getCell(0);
				cell.setCellValue(rc.getReply());
				cell.setCellStyle(leftStyle);
				
				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(33);
				cell = row.getCell(0);
				cell.setCellValue("业主");
				
				cell= row.getCell(1);
				cell.setCellValue(rc.getOwner());
				cell = row.getCell(5);
				cell.setCellValue("现场负责人");
				cell = row.getCell(6);
				cell.setCellValue(rc.getChiefer());
				
				row = sheet1.getRow(row_index++);
				row.setHeightInPoints(33);
				cell = row.getCell(0);
				cell.setCellValue("政府部门");
				cell = row.getCell(1);
				cell.setCellValue(rc.getMinistry());
				cell = row.getCell(5);
				cell.setCellValue("记录人");
				cell = row.getCell(6);
				cell.setCellValue(rc.getRecorder());
				
				row_index += 4;
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

	@RequestMapping(value = "/constru/verify_save", method = RequestMethod.POST)
	public @ResponseBody String constru_verify_save(Model model,HttpServletRequest request, 
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		Object obj = request.getSession().getAttribute(ud.getId() + "pmIdList");
		if (obj != null) {
			List<Integer> pmIdList = (List<Integer>)obj;
			for (Integer pmId : pmIdList)
				constructionService.saveConstructionVerify(pmId, ud.getId(), status, verify_desc);
			request.getSession().removeAttribute(ud.getId() + "pmIdList");
		}
		

		return JsonUtil.toJson("OK");
	}

	
	/**
	 * 阴极保护站运行记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/constru/edit", method = RequestMethod.GET)
	public String constru_edit(Model model, @RequestParam Integer id,
			@RequestParam(required = false) String status) {
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		Construction rc = constructionService.queryConstructionById(id);
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
		return "pages/base/constru_edit";
	}
	
	@RequestMapping(value = "/constru/del", method = RequestMethod.GET)
	public String constru_del(Model model, @RequestParam Integer id) {
		constructionService.deleteConstructionById(id);
		return "redirect:/admin/base/constru/list?del=1";
	}
}
