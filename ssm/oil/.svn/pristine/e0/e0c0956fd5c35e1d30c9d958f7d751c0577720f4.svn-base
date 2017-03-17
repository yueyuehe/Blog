package cn.hm.oil.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.hm.oil.domain.Locality;
import cn.hm.oil.domain.LocalityDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;


/**
 * 地方出资管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/admin/base")
public class LocalityController {
	@Autowired
	private BaseService baseService;
	
	/**
	 * 获取地方出资资料列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/locality/list", method = {RequestMethod.GET,RequestMethod.POST})
	public String localityList(Model model,HttpServletRequest request) {
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Locality> loList = baseService.queryLocality(ps);
		model.addAttribute("loList", loList);
		return "pages/projects/locality_list";
	}
	
	/**
	 * 投资项目创建
	 * @param model
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/locality/create", method = RequestMethod.GET)
	public String localityCreate(Model model, @RequestParam(required=false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		/*List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		
		model.addAttribute("pipeLineList", pipeLineList);*/
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		Prompt prompt = baseService.quertPromptByType(18);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		return "pages/projects/locality_create";
	}
	
	/**
	 * 投资项目保存
	 * @param model
	 * @param request
	 * @param telnum
	 * @param annex
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "/locality/save",method = RequestMethod.POST)
	public String localitySave(Model model,HttpServletRequest request, @RequestParam(required=false) String project_name,
			@RequestParam(required=false) String survey,@RequestParam(required=false) String leader,
			@RequestParam(required=false) String progress,@RequestParam(required=false) Integer[] no,
			@RequestParam(required=false) String[] content,@RequestParam(required=false) String[] plan_time,
			@RequestParam(required=false) String[] workable_time,@RequestParam(required=false) String[] workspace,
			@RequestParam(required=false) String[] contacts,@RequestParam(required=false) String[] telnum,
			@RequestParam(required=false) String[] remark, @RequestParam(required=false) String[] has_annex,@RequestParam(required=false) Integer[] has_annex_no,
			@RequestParam(required=false) Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Locality locality = new Locality();
		locality.setProject_name(project_name);
		locality.setLeader(leader);
		locality.setProgress(progress);
		locality.setSurvey(survey);
		locality.setId(id);
		
		List<String> annex = null;
		try {
			annex = DataUtil.uploadFile(request,"annex");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<LocalityDetail> lodList = new ArrayList<LocalityDetail>();
		
		if(id != null && id.intValue() > 0) {
			int i = 0;
			for(String c : content) {
				LocalityDetail lod = new LocalityDetail();
				if(!StringUtils.isBlank(c)) {
					int x = 0;
					for(Integer n : has_annex_no){
						if(n == no[i] && annex.size() > 0 && StringUtils.isBlank(annex.get(i))) {
							lod.setAnnex(has_annex[x]);
						}
						x++;
					}
					if(!StringUtils.isBlank(annex.get(i))) {
						lod.setAnnex(annex.get(i));
					}
					lod.setContacts(contacts[i]);
					lod.setNo(no[i]);
					lod.setContent(content[i]);
					lod.setPlan_time(DateFormatter.stringToDate(plan_time[i], "yyyy-MM-dd"));
					lod.setWorkable_time(DateFormatter.stringToDate(workable_time[i], "yyyy-MM-dd"));
					lod.setWorkspace(workspace[i]);
					lod.setTelnum(telnum[i]);
					lod.setRemark(remark[i]);
					lod.setOperator(ud.getId());
					
					lodList.add(lod);
				}
				i++;
			}
			baseService.saveLocality(locality, lodList);
			model.addAttribute("msg", "修改成功");
			return "redirect:/admin/base/locality/edit?id=" + id + "&status=1";
		} else {
			locality.setCreater(ud.getId());
		}
		int i = 0;
		for(String c : content) {
			LocalityDetail lod = new LocalityDetail();
			if(!StringUtils.isBlank(c)) {
				lod.setAnnex(annex.get(i));
				lod.setContacts(contacts[i]);
				lod.setNo(no[i]);
				lod.setContent(content[i]);
				lod.setPlan_time(DateFormatter.stringToDate(plan_time[i], "yyyy-MM-dd"));
				lod.setWorkable_time(DateFormatter.stringToDate(workable_time[i], "yyyy-MM-dd"));
				lod.setWorkspace(workspace[i]);
				lod.setTelnum(telnum[i]);
				lod.setRemark(remark[i]);
				
				lodList.add(lod);
			}
			i++;
		}
		String status = "0";
		if (lodList.size() > 0) {
			baseService.saveLocality(locality, lodList);
			status = "1";
		}

		return "redirect:/admin/base/locality/create?status=" + status;
	}

	/**
	 * 投资项目详细
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/locality/show", method = RequestMethod.GET)
	public String localityShow(Model model, @RequestParam Integer id) {
		Locality locality = baseService.queryLocalityById(id);
		model.addAttribute("locality", locality);
		return "pages/projects/locality_show";
	}
	
	@RequestMapping(value = "/locality/edit", method = RequestMethod.GET)
	public String localityEdit(Model model, @RequestParam Integer id, @RequestParam(required=false) Integer status) {
		Locality locality = baseService.queryLocalityById(id);
		model.addAttribute("locality", locality);
		if(status != null && status ==1) {
			model.addAttribute("msg", "修改成功");
		}
		return "pages/projects/locality_edit";
	}
}
