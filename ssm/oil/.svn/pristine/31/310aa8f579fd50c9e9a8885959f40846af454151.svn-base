package cn.hm.oil.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.RoutineAttention;
import cn.hm.oil.domain.RoutineAttentionDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseRoutineAttentionService;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.PageSupport;

@Controller
@RequestMapping(value = "/services/base/routine")
public class WS_Routine {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BaseRoutineAttentionService baseRoutineAttentionService;
	
	/**
	 * 管线日常维护工作记录保存
	 * @param model
	 * @param id
	 * @param pl
	 * @param section
	 * @param spec
	 * @param jinzhan
	 * @param weihu  维护管线（段）
	 * @param year  
	 * @param no  编号
	 * @param type  类别
	 * @param lczh  检查头里程桩号
	 * @param address   所处地址
	 * @param descrip   现状描述
	 * @param atten_date  维护时间
	 * @param content   工作内容
	 * @param person 填报人
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper RoutineSave(HttpServletRequest request,
			@RequestParam(required = false) Integer id,
			@RequestParam Integer pl,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam String weihu,
			@RequestParam Integer year, @RequestParam String[] no,@RequestParam String up_id,
			@RequestParam String[] type, @RequestParam String[] lczh,
			@RequestParam String[] address, @RequestParam String[] descrip,
			@RequestParam String[] atten_date, @RequestParam String[] content,
			@RequestParam String[] person) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		RoutineAttention rc = new RoutineAttention();
		if(id != null && id.intValue() > 0)
			rc.setId(id);
		rc.setPl_id(pl);
		rc.setPl_section_id(section);
		rc.setPl_spec_id(spec);
		rc.setJinzhan(jinzhan);
		rc.setWeihu(weihu);
		rc.setYear(year);
		rc.setStatus(0);
		rc.setUp_id(up_id);
		rc.setCreater(ud.getId());
		
		List<RoutineAttentionDetail> rcdList = new ArrayList<RoutineAttentionDetail>();
		int i = 0;
		for (String n : no) {
			/*if(n.equals("") || n == null) {
				continue;
			}*/
			RoutineAttentionDetail rcd = new RoutineAttentionDetail();
			rcd.setAddress(address[i]);
			rcd.setNo(n);
			rcd.setAtten_date(atten_date[i]);
			rcd.setContent(content[i]);
			rcd.setDescrip(descrip[i]);
			rcd.setLczh(lczh[i]);
			rcd.setPerson(person[i]);
			rcd.setType(type[i]);
			
			rcdList.add(rcd);
			i++;
		}
		String status = "0";
		if (rcdList.size() > 0) {
			rc.setDetaillist(rcdList);
		}
		try {
			baseRoutineAttentionService.saveRoutionAttention(rc);
			status = "1";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.setMessage("保存失败");
			response.setStatus(ResponseStatus.FAILED);
		}
		if (id != null && id.intValue() > 0) {
			response.setMessage("保存成功");
			response.setStatus("200");
		}
		return response;
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
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody JsonResWrapper rcRecord_list(Model model, HttpServletRequest request,
			Authentication authentication,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec,
			@RequestParam(required = false) String year,
			@RequestParam(required = false) Integer del) {
		JsonResWrapper response = new JsonResWrapper();
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());

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
		if (del != null && del.intValue() == 1) {
			model.addAttribute("msg", "删除成功！");
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<RoutineAttention> rcList = baseRoutineAttentionService.queryRoutineAttention(param, ps);

		response.setData(rcList);
		response.setStatus(ResponseStatus.OK);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
}
