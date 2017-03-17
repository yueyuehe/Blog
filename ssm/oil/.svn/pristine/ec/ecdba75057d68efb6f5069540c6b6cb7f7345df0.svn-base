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
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.ValvePatrol;
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
 *         管道保护电位测量_管理
 * 
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping(value = "/admin/base/new")
public class BasePlMeasureControllerNew {

	@Autowired
	private NewBaseService newBaseService;
	
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

		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByUser(ud
				.getId());
		model.addAttribute("pipeLineList", pipeLineList);
		List<BaseReceiver> br = newBaseService.queryLeaderList();
		model.addAttribute("br", br);
		Prompt prompt = newBaseService.quertPromptByType(22);
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
		model.addAttribute("remarkWidth", SettingUtils.getCommonSetting("pl_measure.line.with"));
		return "pages/base/pl_measure_create_new";
	}
	
	@RequestMapping(value="/pl_measure/getTime")
	public @ResponseBody JsonResWrapper GetTime(@RequestParam Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		PotentialMeasure pc = newBaseService.queryPotentialMeasureById(id);
		pc.setCreate_t(DateFormatter.dateToString(pc.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
		if(pc.getVerify_time() != null){
			pc.setVerify_t(DateFormatter.dateToString(pc.getVerify_time(), "yyyy-MM-dd HH:mm:ss"));
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pc);
		return response;
	}
	
	@RequestMapping(value="/pl_measure/setTime")
	public @ResponseBody JsonResWrapper setTime(@RequestParam Integer id,@RequestParam String create_t,@RequestParam String verify_t) {
		JsonResWrapper response = new JsonResWrapper();
		newBaseService.updatePotentialMeasureTime(id, create_t, verify_t);
		response.setStatus(ResponseStatus.OK);
		return response;
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
			@RequestParam Integer spec, @RequestParam(required=false) String jinzhan,
			@RequestParam String c_month, @RequestParam String unit,
			@RequestParam String save_date, @RequestParam String[] no,@RequestParam String up_id,
			@RequestParam(required=false) String[] m_date, @RequestParam(required=false) Float[] potential,
			@RequestParam(required=false) Float[] a, @RequestParam(required=false) Float[] v,
			@RequestParam(required=false) Float[] tongdian, @RequestParam(required=false) String[] measurer,
			@RequestParam(required=false) String[] remark ,@RequestParam(required=false) String status) {

		PotentialMeasure pm = new PotentialMeasure();
		pm.setId(id);
		pm.setPl_id(pl);
		pm.setPl_section_id(section);
		pm.setPl_spec_id(spec);
		pm.setJinzhan(jinzhan);
		pm.setC_month(Integer.valueOf(c_month.replace("-", "")));
		pm.setUnit(unit);
		pm.setStatus(Integer.parseInt(status));
		//pm.setVerify_name(verify_name);
		pm.setSave_date(save_date);
		//pm.setStatus(0);
		pm.setUp_id(up_id);
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		pm.setCreater(ud.getId());
		List<PotentialMeasureDetail> pmdList = new ArrayList<PotentialMeasureDetail>();
		int i = 0;
		for (String n : no) {
			if(StringUtil.isBlank(n) == false)
			{
				PotentialMeasureDetail pmd = new PotentialMeasureDetail();
				
				pmd.setNo(n);
				pmd.setM_date(DateFormatter.stringToDate(m_date[i],
							"yyyy-MM-dd"));
				pmd.setPotential(potential[i]);
				pmd.setMeasurer(measurer[i]);
				pmd.setRemark(remark[i]);
				pmd.setStatus(Integer.parseInt(status));
				pmdList.add(pmd);
			}
			i++;
		}
		if (pmdList.size() > 0) {
			newBaseService.savePlMeasure(pm, pmdList);
		}

		String page = "create" + "?status=" + status;
		if (id != null && id.intValue() > 0) {
			page = "edit?id=" + id + "&status=" + status;
		}

		/***
		 * 将数据填充到potential_curve potential_curve_Detail表中
		 */
		PotentialCurve pc = new PotentialCurve();
		pc.setC_month(Integer.valueOf(c_month.replace("-", "")));
		pc.setPl_id(pl);
		pc.setPl_section_id(section);
		pc.setPl_spec_id(spec);
		//pc.setUnit(unit);
		pc.setCreater(ud.getId());
		pc.setUp_id(up_id);
		//pc.setAnalysis(analysis);
		pc.setCreate_time(new Date());
        //pc.setStatus();
		//需要将该月的所有数据查询出来
		
		
		
		Map<String, PotentialCurveDetail> mp = new HashMap<String,PotentialCurveDetail>();//
		
		List<PotentialCurveDetail> pcds = new ArrayList<PotentialCurveDetail>();
		for (String n : no) {
			PotentialCurveDetail pcd = new PotentialCurveDetail();
			pcds.add(pcd);
			pcd.setId(0);
			pcd.setNo(n);
		}
		
		return "redirect:/admin/base/new/pl_measure/" + page;
	}

	@RequestMapping(value = "/pl_measure/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String plMeasure_list(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String c_month,
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
			List<BasePipelineSection> sectionList = newBaseService.querySectionByAdminPlMeasure(par);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);

			//规格 下拉列表
			List<BasePipelineSpec> specs = baseService.querySpec(section);
			model.addAttribute("specList", specs);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}
		if (!StringUtils.isBlank(c_month)) {
			param.put("r_month", Integer.valueOf(c_month.replace("-", "")));
			model.addAttribute("c_month", c_month);
		}		
		
		if(all != null)
		{
			model.addAttribute("all", all);
		}
		
		model.addAttribute("role", role);
		if(verify!=null)
		model.addAttribute("verify", verify);
		//规格 显示列表
		List<BasePipelineSpec> specList = newBaseService.querySpecListNewByAdminPlMeasure(param);
		model.addAttribute("specLists", specList);
		
		//管线 下拉列表
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByAdminPlMeasure(param);
		model.addAttribute("pipeLineList", pipeLineList);
		
		return "pages/base/pl_measure_list_new";
	}
	
	@RequestMapping(value = "/pl_measure/section/get", method = RequestMethod.GET)
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
			return newBaseService.querySectionByAdminPlMeasure(param);
		}
		return baseService.querySectionByUser(pl_id, ud.getId());
	}
	
	@RequestMapping(value = "/pl_measure/book_list", method = { RequestMethod.GET, RequestMethod.POST })
	public String plMeasure_book_list(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) Integer st,
			@RequestParam(required = false) String c_month) {
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLine();

		Map<String, Object> param = new HashMap<String, Object>();
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("user_id", ud.getId());
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);

			List<BasePipelineSection> sectionList = newBaseService.querySection(pl);
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

		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
			model.addAttribute("c_month", c_month);
		}
		if (st != null && st.intValue() != 2) {
			param.put("status", st);
			model.addAttribute("st", st);
		}

		List<PotentialMeasure> pmList = newBaseService.queryPotentialMeasureMerge(param);

		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("pmList", pmList);
		return "pages/base/pl_measure_book_list";
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
	@RequestMapping(value = "/pl_measure/verify_old", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String plMeasure_verify(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String c_month,
			@RequestParam(required = false) Integer del) {
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

		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
			model.addAttribute("c_month", c_month);
		}

		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("verify", "1");
		param.put("user_id", ud.getId());
		PageSupport ps = PageSupport.initPageSupport(request);
		List<PotentialMeasure> pmList = newBaseService.queryPotentialMeasure(
				param, ps);

		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		
		Integer role = userService.queryRoleIdByUserId(ud.getId());

		model.addAttribute("role", role);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("pmList", pmList);
		model.addAttribute("verify", 1);
		return "pages/base/pl_measure_list_new";
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
	@RequestMapping(value = "/pl_measure/verify", method = { RequestMethod.GET, RequestMethod.POST })
	public String plMeasure_verify_merge(Model model, HttpServletRequest request, Authentication authentication
			,@RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String p_month
			,@RequestParam(required=false) Boolean all) {		
		/*Map<String, Object> param = new HashMap<String, Object>();
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = CommonsMethod.getDataByRole(authentication, userService, param, false);
		param.put("status", 0);
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);
			
			Map<String,Object> par = new HashMap<String,Object>(param);
			par.put("pl_id", pl);
			List<BasePipelineSection> sectionList = newBaseService.querySectionByAdminPlMeasure(par);
			
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
			param.put("r_month", Integer.valueOf(p_month.replace("-", "")));
			model.addAttribute("c_month", p_month);
		}
		if(all != null)
		{
			model.addAttribute("all", all);
		}
		
		model.addAttribute("role", role);
		model.addAttribute("verify", 1);
		//规格 显示列表
		List<BasePipelineSpec> specList = newBaseService.querySpecListNewByAdminPlMeasure(param);
		model.addAttribute("specLists", specList);
		
		//管线 下拉列表
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByAdminPlMeasure(param);
		model.addAttribute("pipeLineList", pipeLineList);
		
		return "pages/base/pl_measure_list_new";*/
		String pages="";
		pages = CommonsMethod.putUrlParam(pages, "pl", pl);
		pages = CommonsMethod.putUrlParam(pages, "section", section);
		pages = CommonsMethod.putUrlParam(pages, "spec", spec);
		pages = CommonsMethod.putUrlParam(pages, "c_month", p_month);
		pages = CommonsMethod.putUrlParam(pages, "verify", new Integer(1));
		pages = CommonsMethod.putUrlParam(pages, "all", new Boolean(false));
		return "redirect:/admin/base/new/pl_measure/list"+pages;
	}
	/*@RequestMapping(value = "/pl_measure/verify", method = { RequestMethod.GET, RequestMethod.POST })
	public String plMeasure_verify_merge(Model model, HttpServletRequest request, Authentication authentication) {

		Map<String, Object> param = new HashMap<String, Object>();
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		param.put("user_id", ud.getId());
		param.put("status", 0);
		List<PotentialMeasure> pmList = newBaseService.queryPotentialMeasureMerge(param);

		model.addAttribute("pmList", pmList);
		return "pages/base/pl_measure_verify_list";
	}*/

	/**
	 * 管道保护电位测量记录删除
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/del", method = RequestMethod.GET)
	public String plMeasure_del(Model model, @RequestParam Integer id) {
		newBaseService.deletePotentialMeasureById(id);
		return "redirect:/admin/base/new/pl_measure/list?del=1";
	}

	/**
	 * 管道保护电位测量记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/show", method = {RequestMethod.GET,RequestMethod.POST})
	public String plMeasure_show(Model model, Authentication authentication,HttpServletRequest request,
								  @RequestParam(required = false) Integer id, 
								  @RequestParam(required = false) String r_month,
								  @RequestParam(required = false) Integer verify,
								 // @RequestParam Integer offset, 
								  @RequestParam(required=false) Integer tips_id) {
		
		if (tips_id != null && tips_id.intValue() > 0){
			baseService.deleteTips(tips_id);
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		if(id != null)
		{
			param.put("id", id);
		}
	//	param.put("offset", offset);
	//	param.put("pageSize", 1);
		CommonsMethod.getDataByRole(authentication, userService, param);
		if(!StringUtils.isBlank(r_month)){
			param.put("r_month", Integer.valueOf(r_month.replace("-", "")));
			model.addAttribute("r_month", r_month);
		}
		
		PotentialMeasure pm = newBaseService.queryPotentialMeasureByParam(param);
		
		if(pm==null){
			return "pages/base/pl_measure_show_new";
		}
//		int vpTotal = newBaseService.queryPotentialMeasureByParamTotal(param);
		
	//	List<PotentialMeasureDetail> pmdList = pm.getDetailList();
	//	List<PotentialMeasureDetail> oneList = pmdList.subList(0, pmdList.size() /3);
	//	List<PotentialMeasureDetail> twoList = pmdList.subList(pmdList.size() / 3, pmdList.size() * 2/3);
	//	List<PotentialMeasureDetail> thrList = pmdList.subList(pmdList.size() * 2/3, pmdList.size());
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		List<BasePipelineSection> sectionList = baseService.querySection(pm.getPl_id());
		List<BasePipelineSpec> specList = baseService.querySpec(pm.getPl_section_id());

		//获取用户角色ID
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		model.addAttribute("role", role);
		{
			PageSupport ps = PageSupport.initPageSupport(request);
			param.put("pl", pm.getPl_id());
			param.put("spec", pm.getPl_spec_id());
			param.put("section", pm.getPl_section_id());
			ps.setPageSize(30);
			List<PotentialMeasureDetail> pmd = newBaseService.queryPotentialMeasureDetail(param, ps);
			for(PotentialMeasureDetail p : pmd)
			{
				newBaseService.updateCheckPotentialMeasureDetailStatus(p);
				p.resetCanEidt(role);           
			}
			
			if(pmd.size() < ps.getPageSize())
			{
				for(int i = pmd.size(); i < ps.getPageSize(); ++i)
				{
					PotentialMeasureDetail n = new PotentialMeasureDetail();
					pmd.add(n);
				}
			}
			model.addAttribute("twoList", pmd);
		}
		model.addAttribute("pm", pm);
		model.addAttribute("pl", pm.getPl_id());
		model.addAttribute("spec", pm.getPl_spec_id());
		model.addAttribute("section", pm.getPl_section_id());
		
	//	model.addAttribute("oneList", oneList);
	//	model.addAttribute("twoList", twoList);
	//	model.addAttribute("thrList", thrList);
		
//		model.addAttribute("total", vpTotal);
//		model.addAttribute("offset", offset);
		model.addAttribute("id", id);
		model.addAttribute("verify", verify);
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("specList", specList);
		model.addAttribute("pipeLineList", pipeLineList);
		
		return "pages/base/pl_measure_show_new";
	}
	
	/**
	 * 管道巡检工作记录修改
	 * 
	 */
	@RequestMapping(value = "/pl_measure/modify", method = RequestMethod.POST)
	public String plPatrol_modify(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer id,
			@RequestParam String[] no,
			@RequestParam(required=false) String[] m_date, @RequestParam(required=false) Float[] potential,
			@RequestParam(required=false) String[] measurer,@RequestParam(required=false) String[] remark,			
			@RequestParam(required = false) String r_month,
			@RequestParam(required = false) Integer verify,
			@RequestParam(required=false) Integer tips_id,
			@RequestParam Integer[] pmd_id
			)
			//@RequestParam(required = false) String up_id,@RequestParam(required = false) String status)
	{
		int i = 0;
		for (Integer pId : pmd_id)
		{
			if(StringUtil.isBlank(no[i]) == false)
			{
				PotentialMeasureDetail pmd = new PotentialMeasureDetail();
				pmd.setM_date(DateFormatter.stringToDate(m_date[i],
						"yyyy-MM-dd"));
				pmd.setPotential(potential[i]);
				pmd.setMeasurer(measurer[i]);
				pmd.setRemark(remark[i]);
				pmd.setNo(no[i]);
				pmd.setId(pId);
				pmd.setStatus(new Integer(-2));	
				newBaseService.modifyPlMeasureDetail(pmd);
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
		
		return "redirect:/admin/base/new/pl_measure/show"+page;//"pages/base/pl_patrol_show_new";
	}
	
	/**
	 * 管道巡检工作记录批量更改状态
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/batch_changestatus", method = {RequestMethod.GET,RequestMethod.POST})
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
		
		CommonsMethod.getDataByRole(authentication, userService, param);
		PerformanceMeasure pm = newBaseService.queryValvePatrolByList(param);
		
		if(pm!=null)
		{
			param.put("pl", pm.getPl_id());
			param.put("spec", pm.getPl_spec_id());
			param.put("section", pm.getPl_section_id());
			List<PotentialMeasureDetail> pmd = newBaseService.queryPotentialMeasureDetail(param);
			for(PotentialMeasureDetail p : pmd)
			{
				if(Arrays.asList(oldStatus).contains(p.getStatus()))
				{
					newBaseService.updateChangePotentialMeasureDetailStatus(new Integer(p.getId()), newStatus);   
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
		return "redirect:/admin/base/new/pl_measure/show"+page;
	}
	
	/**
	 * 管道保护电位测量记录创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/book_show", method = { RequestMethod.GET, RequestMethod.POST })
	public String plMeasure_book_show(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam Integer pl_id, @RequestParam Integer pl_section_id, @RequestParam Integer pl_spec_id,
			@RequestParam(required = false) Integer status, @RequestParam(required = false) String c_month) {
		
		PageSupport ps = PageSupport.initPageSupport(request);
		ps.setPageSize(30);
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		
		Map<String, Object> param = new HashMap<String, Object>();
		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
			model.addAttribute("c_month", c_month);
		}
		
		if (status != null && (status.intValue() == -1 || status.intValue() == 0 || status.intValue() == 1)) {
			param.put("status", status);
			model.addAttribute("status", status);
		}
		param.put("pl_id", pl_id);
		param.put("pl_section_id", pl_section_id);
		param.put("pl_spec_id", pl_spec_id);
		param.put("user_id", ud.getId());
		
		PotentialMeasure pm = newBaseService.queryPotentialMeasureMergeInfo(param);
		List<PotentialMeasureDetail> pmdList = newBaseService.queryPotentialMeasureDetailMerge(param, ps);
		
		int d = 30 - pmdList.size();
		if (d > 0) {
			for (int i = 0; i < d; i++) {
				PotentialMeasureDetail e = new PotentialMeasureDetail();
				pmdList.add(e);
			}
		}
		
		List<PotentialMeasureDetail> oneList = pmdList.subList(0, 10);
		List<PotentialMeasureDetail> twoList = pmdList.subList(10, 20);
		List<PotentialMeasureDetail> thrList = pmdList.subList(20, 30);

		model.addAttribute("oneList", oneList);
		model.addAttribute("twoList", twoList);
		model.addAttribute("thrList", thrList);
		model.addAttribute("pm", pm);
		model.addAttribute("pmdList", pmdList);
		model.addAttribute("pl_id", pl_id);
		model.addAttribute("pl_section_id", pl_section_id);
		model.addAttribute("pl_spec_id", pl_spec_id);
		return "pages/base/pl_measure_book_show";
	}

	/**
	 * 管道保护电位测量记录审核页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/show_verify_old", method = RequestMethod.GET)
	public String plMeasure_show_verify(Model model, @RequestParam Integer id) {
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLine();
		PotentialMeasure pm = newBaseService.queryPotentialMeasureById(id);
		List<BasePipelineSection> sectionList = newBaseService.querySection(pm
				.getPl_id());
		List<BasePipelineSpec> specList = newBaseService.querySpec(pm
				.getPl_section_id());
		List<PotentialMeasureDetail> pmdList = pm.getDetailList();
		List<PotentialMeasureDetail> oneList = pmdList.subList(0, pmdList.size() /3);
		List<PotentialMeasureDetail> twoList = pmdList.subList(pmdList.size() / 3, pmdList.size() * 2/3);
		List<PotentialMeasureDetail> thrList = pmdList.subList(pmdList.size() * 2/3, pmdList.size());

		model.addAttribute("oneList", oneList);
		model.addAttribute("twoList", twoList);
		model.addAttribute("thrList", thrList);
		model.addAttribute("pl", pm.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pm.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pm", pm);
		model.addAttribute("verify", 1);
		return "pages/base/pl_measure_show_new";
	}
	
	/**
	 * 管道保护电位测量记录审核页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/show_verify", method = {RequestMethod.GET, RequestMethod.POST})
	public String plMeasure_show_verify_merge(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam Integer pl_id, @RequestParam Integer pl_section_id, @RequestParam Integer pl_spec_id,
			@RequestParam(required=false) Integer f) {
		PageSupport ps = PageSupport.initPageSupport(request);
		ps.setPageSize(30);
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", 0);
		param.put("pl_id", pl_id);
		param.put("pl_section_id", pl_section_id);
		param.put("pl_spec_id", pl_spec_id);
		param.put("user_id", ud.getId());
		
		PotentialMeasure pm = newBaseService.queryPotentialMeasureMergeInfo(param);
		Map<String, Object> param1 = new HashMap<String,Object>(param);
		param1.put("limit", 10000);
		param1.put("offset", 0);
		param1.put("order", 1);
		List<PotentialMeasureDetail> pmdList1 = newBaseService.queryPotentialMeasureDetailMerge(param1);
		ps.setPageSize(30);
		List<PotentialMeasureDetail> pmdList = newBaseService.queryPotentialMeasureDetailMerge(param, ps);
		
		List<Integer> pmIdList = null;
		Object obj = request.getSession().getAttribute(ud.getId() + "pmIdList");
		if (obj != null && f == null) {
			pmIdList = (List<Integer>)obj;
		} else {
			request.getSession().removeAttribute(ud.getId() + "pmIdList");
			pmIdList = new ArrayList<Integer>();
		}
		if (!CollectionUtils.isEmpty(pmdList1)) {
			for (PotentialMeasureDetail pmd : pmdList1) {
				if (!pmIdList.contains(pmd.getPm_id())) {
					pmIdList.add(pmd.getPm_id());
				}
			}
			request.getSession().setAttribute(ud.getId() + "pmIdList", pmIdList);
		}
		int d = ps.getPageSize() - pmdList.size();
		if (d > 0) {
			for (int i = 0; i < d; i++) {
				PotentialMeasureDetail e = new PotentialMeasureDetail();
				pmdList.add(e);
			}
		}
		
		List<PotentialMeasureDetail> oneList = pmdList.subList(0, 10);
		List<PotentialMeasureDetail> twoList = pmdList.subList(10, 20);
		List<PotentialMeasureDetail> thrList = pmdList.subList(20, 30);

		model.addAttribute("oneList", oneList);
		model.addAttribute("twoList", twoList);
		model.addAttribute("thrList", thrList);
		model.addAttribute("pm", pm);
		model.addAttribute("pmdList", pmdList);
		model.addAttribute("pl_id", pl_id);
		model.addAttribute("pl_section_id", pl_section_id);
		model.addAttribute("pl_spec_id", pl_spec_id);
		return "pages/base/pl_measure_show_merge";
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

		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
			paramD.put("r_month", Integer.valueOf(c_month.replace("-", "")));
		}

		List<PotentialMeasure> pmList = newBaseService.queryPotentialMeasure(
				param, null);
		if (pmList.size() == 0) {
			return "redirect:/admin/base/new/pl_measure/list";
		}
		
		List<PotentialMeasureDetail> pmdList = newBaseService.queryPotentialMeasureDetail(paramD, null);
		int datas_row = 30;//每页数据行数
		
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
			//for (PotentialMeasure pm : pmList) {
			int pages = (pmdList.size()+datas_row-1)/datas_row;
			PotentialMeasure p = pmList.get(0);
			PotentialMeasure pm = newBaseService.queryPotentialMeasureById(p.getId());
			for(int iPage = 0; iPage < pages; ++iPage) {
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
				cell.setCellValue("管线名称：" + pm.getPl_name() + "/" + pm.getPl_section_name());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(6);
				cell.setCellValue("管线规格: " + pm.getPl_spec_name());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(16);
				cell.setCellValue(pm.getC_month().toString().substring(0, 4)
						+ " 年 " + pm.getC_month().toString().substring(4, 6)
						+ " 月 ");
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				
				//PotentialMeasure pmd = newBaseService.queryPotentialMeasureById(pm.getId());
				Integer sz = 10;
				/*if((pmd.getDetailList().size()/3) > 10)
					sz = pmd.getDetailList().size()/3;
				if((pmd.getDetailList().size()%3) > 0) {
					sz++;
				}*/
				
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
				//for (PotentialMeasureDetail pd : pmd.getDetailList()) {
				for(int iRow = 0; iRow < datas_row; ++iRow){
					int detailIndex = iPage*datas_row + iRow;
					if(detailIndex >= pmdList.size())
						break;
					PotentialMeasureDetail pd = pmdList.get(detailIndex);
					if (count < sz) {
						row = sheet1.getRow(indexl++);
						System.out.println("index == " + indexl);
						cell = row.getCell(0);
						// cell.setCellStyle(dataStyle);
						if(pd.getM_date()!=null) {
							Calendar ca = Calendar.getInstance();
							ca.setTime(pd.getM_date());
							// 获取date中的天数
							String v = String.format("%04d-%02d-%02d", ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
							cell.setCellValue(v);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
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
							String v = String.format("%04d-%02d-%02d", ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
							cell.setCellValue(v);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
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
							String v = String.format("%04d-%02d-%02d", ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
							cell.setCellValue(v);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
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
				cell.setCellValue("填报人: " + pm.getUnit());
				cell.setCellStyle(titleStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(16);
				cell.setCellValue("审核人: " + pm.getSave_date() + "");
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

		newBaseService.updatePotentialMeasureVerifyStatus(id, ud.getId(), status, verify_desc);
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

	@RequestMapping(value = "/pl_measure/verify_save_merge", method = RequestMethod.POST)
	public @ResponseBody String plMeasure_verify_save_merge(Model model, HttpServletRequest request, 
			@RequestParam Integer status, @RequestParam String verify_desc) {
		LoginUserInfo ud = (LoginUserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Object obj = request.getSession().getAttribute(ud.getId() + "pmIdList");
		if (obj != null) {
			List<Integer> pmIdList = (List<Integer>)obj;
			for (Integer pmId : pmIdList)
				newBaseService.updatePotentialMeasureVerifyStatus(pmId, ud.getId(), status, verify_desc);
			request.getSession().removeAttribute(ud.getId() + "pmIdList");
		}
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
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLineByUser(ud.getId());
		PotentialMeasure pm = newBaseService.queryPotentialMeasureById(id);
		List<BasePipelineSection> sectionList = newBaseService.querySection(pm
				.getPl_id());
		List<BasePipelineSpec> specList = newBaseService.querySpec(pm
				.getPl_section_id());
		List<PotentialMeasureDetail> pmdList = pm.getDetailList();
		List<PotentialMeasureDetail> oneList = pmdList.subList(0, pmdList.size() /3);
		List<PotentialMeasureDetail> twoList = pmdList.subList(pmdList.size() / 3, pmdList.size() * 2/3);
		List<PotentialMeasureDetail> thrList = pmdList.subList(pmdList.size() * 2/3, pmdList.size());

		model.addAttribute("oneList", oneList);
		model.addAttribute("twoList", twoList);
		model.addAttribute("thrList", thrList);
		model.addAttribute("pl", pm.getPl_id());
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("section", pm.getPl_section_id());
		model.addAttribute("sectionList", sectionList);
		model.addAttribute("spec", pm.getPl_spec_id());
		model.addAttribute("specList", specList);
		model.addAttribute("pm", pm);
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
		model.addAttribute("remarkWidth", SettingUtils.getCommonSetting("pl_measure.line.with"));
		return "pages/base/pl_measure_edit_new";
	}
	
}
