package cn.hm.oil.webservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.Construction;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.ConstructionService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;

@Controller
@RequestMapping(value = "/services/base")
public class WS_Construction {
	@Autowired
	private BaseService baseService;
	@Autowired
	private ConstructionService constructionService;
	
	@RequestMapping(value = "/constru/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper construSave(HttpServletRequest request, @RequestParam(required = false) Integer id,
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
			@RequestParam String result) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		Construction rc = new Construction();
		if(id != null && id.intValue() > 0)
			rc.setId(id);
		rc.setPl_id(pl);
		rc.setPl_section_id(section);
		rc.setPl_spec_id(spec);
		rc.setStatus(0);
		rc.setJingzhan(jingzhan);
		rc.setCreater(ud.getId());
		rc.setAcquainted(acquainted);
		rc.setAddress(address);
		rc.setChiefer(chiefer);
		rc.setCon_date(DateFormatter.stringToDate(con_date,"yyyy-MM-dd"));
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
		try {
			constructionService.saveConstruction(rc);
			response.setStatus(ResponseStatus.OK);
			response.setMessage("保存成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.setStatus(ResponseStatus.FAILED);
			response.setMessage("保存失败");
		}
		
		return response;
	}
	
	@RequestMapping(value = "/constru/list", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody JsonResWrapper constru_list(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer pl,
			@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec) {
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
		
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Construction> rcList = constructionService.queryConstruction(param, ps);

		JsonResWrapper response = new JsonResWrapper();
		response.setData(rcList);
		response.setStatus(ResponseStatus.OK);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
}
