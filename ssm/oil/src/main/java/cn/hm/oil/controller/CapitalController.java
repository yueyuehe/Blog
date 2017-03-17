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

import cn.hm.oil.domain.Capital;
import cn.hm.oil.domain.CapitalDetail;
import cn.hm.oil.domain.Invest;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;

/**
 * 大修项目管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/admin/base")
public class CapitalController {
	@Autowired
	private BaseService baseService;
	
	@RequestMapping(value = "capital/create")
	public String capotalCreate(Model model, @RequestParam(required=false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		/*List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		
		model.addAttribute("pipeLineList", pipeLineList);*/
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		Prompt prompt = baseService.quertPromptByType(17);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		return "pages/projects/capital_create";
	}
	
	@RequestMapping(value = "/capital/save",method = RequestMethod.POST)
	public String investSave(Model model,HttpServletRequest request, @RequestParam(required=false) String project_name,
			@RequestParam(required=false) String survey,@RequestParam(required=false) String leader,
			@RequestParam(required=false) String progress,@RequestParam(required=false) Integer[] no,
			@RequestParam(required=false) String[] content,@RequestParam(required=false) String[] plan_time,
			@RequestParam(required=false) String[] workable_time,@RequestParam(required=false) String[] workspace,
			@RequestParam(required=false) String[] contacts,@RequestParam(required=false) String[] telnum,
			@RequestParam(required=false) String[] remark, @RequestParam(required = false) String[] has_annex, @RequestParam(required = false) Integer[] has_annex_no,
			@RequestParam(required=false) Integer creater,@RequestParam(required=false) Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Capital capital = new Capital();
		capital.setProject_name(project_name);
		capital.setLeader(leader);
		capital.setProgress(progress);
		capital.setSurvey(survey);
		capital.setId(id);
		
		List<String> annex = null;
		try {
			annex = DataUtil.uploadFile(request,"annex");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<CapitalDetail> cadList = new ArrayList<CapitalDetail>();
		
		if(id != null && id.intValue() > 0) {
			int i = 0;
			for(String c : content) {
				CapitalDetail cad = new CapitalDetail();
				if(!StringUtils.isBlank(c)) {
					//如果has_annex_no 与ni【i】相同，则此项的annex为hax_annex
					int x = 0;
					for(Integer n : has_annex_no){
						if(n == no[i] && annex.get(i) == null) {
							cad.setAnnex(has_annex[x]);
						}
						x++;
					}
					if(!StringUtils.isBlank(annex.get(i))) {
						cad.setAnnex(annex.get(i));
					}
					cad.setContacts(contacts[i]);
					cad.setNo(no[i]);
					cad.setContent(content[i]);
					cad.setPlan_time(DateFormatter.stringToDate(plan_time[i], "yyyy-MM-dd"));
					cad.setWorkable_time(DateFormatter.stringToDate(workable_time[i], "yyyy-MM-dd"));
					cad.setWorkspace(workspace[i]);
					cad.setTelnum(telnum[i]);
					cad.setRemark(remark[i]);
					cad.setOperator(ud.getId());
					
					cadList.add(cad);
				}
				i++;
			}
			baseService.saveCapital(capital, cadList);
			model.addAttribute("msg", "修改成功");
			return "redirect:/admin/base/capital/edit?id=" + id + "&status=1";
		} else {
			capital.setCreater(ud.getId());
		}
		
		int i = 0;
		for(String c : content) {
			CapitalDetail cad = new CapitalDetail();
			if(!StringUtils.isBlank(c)) {
				cad.setAnnex(annex.get(i));
				cad.setContacts(contacts[i]);
				cad.setNo(no[i]);
				cad.setContent(content[i]);
				cad.setPlan_time(DateFormatter.stringToDate(plan_time[i], "yyyy-MM-dd"));
				cad.setWorkable_time(DateFormatter.stringToDate(workable_time[i], "yyyy-MM-dd"));
				cad.setWorkspace(workspace[i]);
				cad.setTelnum(telnum[i]);
				cad.setRemark(remark[i]);
				
				cadList.add(cad);
			}
			i++;
		}
		String status = "0";
		if (cadList.size() > 0) {
			baseService.saveCapital(capital, cadList);
			status = "1";
		}

		return "redirect:/admin/base/capital/create?status=" + status;
	}
	/**
	 * 大修项目列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/capital/list", method = {RequestMethod.GET,RequestMethod.POST})
	public String capitalList(Model model,HttpServletRequest request) {
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Capital> caList = baseService.queryCapital(ps);
		model.addAttribute("caList", caList);
		return "pages/projects/capital_list";
	}
	
	/**
	 * 大修项目详细
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/capital/show", method = RequestMethod.GET)
	public String investShow(Model model, @RequestParam Integer id) {
		Capital capital = baseService.queryCapitalById(id);
		model.addAttribute("capital", capital);
		return "pages/projects/capital_show";
	}
	
	@RequestMapping(value = "/capital/edit", method = RequestMethod.GET)
	public String investEdit(Model model, @RequestParam Integer id,@RequestParam(required=false) Integer status) {
		Capital capital = baseService.queryCapitalById(id);
		model.addAttribute("capital", capital);
		if(status !=null && status == 1) {
			model.addAttribute("msg", "修改成功");
		}
		return "pages/projects/capital_edit";
	}
}
