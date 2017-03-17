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

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.Invest;
import cn.hm.oil.domain.InvestDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;

/**
 * 投资项目管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/admin/base")
public class InvestController {
	@Autowired
	private BaseService baseService;
	
	/**
	 * 获取投资项目资料列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/invest/list", method = {RequestMethod.GET,RequestMethod.POST})
	public String investList(Model model,HttpServletRequest request) {
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Invest> inList = baseService.queryInvest(ps);
		model.addAttribute("inList", inList);
		return "pages/projects/invest_list";
	}
	
	/**
	 * 投资项目创建
	 * @param model
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/invest/create", method = RequestMethod.GET)
	public String investCreate(Model model, @RequestParam(required=false) String status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		/*List<BasePipeline> pipeLineList = baseService.queryPipeLineByUser(ud.getId());
		
		model.addAttribute("pipeLineList", pipeLineList);*/
		if (StringUtils.equals(status, "1")) {
			model.addAttribute("msg", "保存成功！");
		} else if (StringUtils.equals(status, "0")) {
			model.addAttribute("msg", "保存失败！");
		}
		Prompt prompt = baseService.quertPromptByType(16);
		if(prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		return "pages/projects/invest_create";
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
	@RequestMapping(value = "/invest/save",method = RequestMethod.POST)
	public String investSave(Model model,HttpServletRequest request, @RequestParam(required=false) String project_name,
			@RequestParam(required=false) String survey,@RequestParam(required=false) String leader,
			@RequestParam(required=false) String progress,@RequestParam(required=false) Integer[] no,
			@RequestParam(required=false) String[] content,@RequestParam(required=false) String[] plan_time,
			@RequestParam(required=false) String[] workable_time,@RequestParam(required=false) String[] workspace,
			@RequestParam(required=false) String[] contacts,@RequestParam(required=false) String[] telnum,@RequestParam(required=false) String[] remark,
			@RequestParam(required=false) Integer id, @RequestParam(required=false) Integer[] has_annex_no, @RequestParam(required=false) String[] has_annex) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Invest invest = new Invest();
		invest.setProject_name(project_name);
		invest.setLeader(leader);
		invest.setProgress(progress);
		invest.setSurvey(survey);
		invest.setId(id);
		
		List<String> annex = null;
		try {
			annex = DataUtil.uploadFile(request,"annex");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<InvestDetail> indList = new ArrayList<InvestDetail>();
		
		if(id != null && id.intValue() > 0) {
			int i = 0;
			for(String c : content) {
				InvestDetail ind = new InvestDetail();
				if(!StringUtils.isBlank(c)) {
					int x = 0;
					for(Integer n : has_annex_no){
						if(n == no[i] && annex.get(i) == null) {
							ind.setAnnex(has_annex[x]);
						}
						x++;
					}
					if(!StringUtils.isBlank(annex.get(i))) {
						ind.setAnnex(annex.get(i));
					}
					ind.setContacts(contacts[i]);
					ind.setNo(no[i]);
					ind.setContent(content[i]);
					ind.setPlan_time(DateFormatter.stringToDate(plan_time[i], "yyyy-MM-dd"));
					ind.setWorkable_time(DateFormatter.stringToDate(workable_time[i], "yyyy-MM-dd"));
					ind.setWorkspace(workspace[i]);
					ind.setTelnum(telnum[i]);
					ind.setRemark(remark[i]);
					//ind.setOperate_num();
					ind.setOperator(ud.getId());
					indList.add(ind);
				}
				i++;
			}
			baseService.saveInvest(invest, indList);
			model.addAttribute("msg", "修改成功");
			return "redirect:/admin/base/invest/edit?id=" + id + "&status=1";
		} else {
			invest.setCreater(ud.getId());
		}
		int i = 0;
		for(String c : content) {
			InvestDetail ind = new InvestDetail();
			if(!StringUtils.isBlank(c)) {
				ind.setAnnex(annex.get(i));
				ind.setContacts(contacts[i]);
				ind.setNo(no[i]);
				ind.setContent(content[i]);
				ind.setPlan_time(DateFormatter.stringToDate(plan_time[i], "yyyy-MM-dd"));
				ind.setWorkable_time(DateFormatter.stringToDate(workable_time[i], "yyyy-MM-dd"));
				ind.setWorkspace(workspace[i]);
				ind.setTelnum(telnum[i]);
				ind.setRemark(remark[i]);
				
				indList.add(ind);
			}
			i++;
		}
		String status = "0";
		if (indList.size() > 0) {
			baseService.saveInvest(invest, indList);
			status = "1";
		}

		return "redirect:/admin/base/invest/create?status=" + status;
	}

	/**
	 * 投资项目详细
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/invest/show", method = RequestMethod.GET)
	public String investShow(Model model, @RequestParam Integer id) {
		Invest invest = baseService.queryInvestById(id);
		model.addAttribute("invest", invest);
		return "pages/projects/invest_show";
	}
	
	@RequestMapping(value = "/invest/edit", method = RequestMethod.GET)
	public String investEdit(Model model, @RequestParam Integer id, @RequestParam(required=false) Integer status) {
		Invest invest = baseService.queryInvestById(id);
		if(status !=null && status == 1) {
			model.addAttribute("msg", "修改成功");
		}
		model.addAttribute("invest", invest);
		return "pages/projects/invest_edit";
	}
	
}
