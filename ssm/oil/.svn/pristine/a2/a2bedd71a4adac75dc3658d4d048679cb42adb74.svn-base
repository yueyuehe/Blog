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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.NewBaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;

/**
 * 管道保护电位测量记录
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/services/base_new")
public class WS_PlMeasure_New {

	@Autowired
	private NewBaseService baseService;

	@Autowired
	private UserService userService;
	
	/**
	 * 管道保护电位测量记录 列表
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
	@RequestMapping(value = "/pl_measure/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody JsonResWrapper plMeasure_list(HttpServletRequest request,
			@RequestParam(required = false) Integer pl,	@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String c_month,
			@RequestParam(required = false) Integer status) {
		JsonResWrapper response = new JsonResWrapper();

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

		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
		}

		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<PotentialMeasure> pmList = baseService.queryPotentialMeasure(
				param, ps);

		response.setStatus(ResponseStatus.OK);
		response.setData(pmList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 管道保护电位测量记录 详细
	 * @param pm_id
	 * @return
	 */
	@RequestMapping(value = "/pl_measure/show", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody JsonResWrapper plMeasureShow(@RequestParam Integer pm_id) {
		JsonResWrapper response = new JsonResWrapper();
		PotentialMeasure pm = baseService.queryPotentialMeasureById(pm_id);
		List<PotentialMeasureDetail> pmdList = pm.getDetailList();
		
		response.setStatus(ResponseStatus.OK);
		response.setData(pm);
		return response;
	}
	
	@RequestMapping(value = "/pl_measure/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper plMeasure_save(HttpServletRequest request, @RequestParam(required = false) Integer id,
			@RequestParam Integer pl, @RequestParam Integer section,
			@RequestParam Integer spec, @RequestParam(required=false) String jinzhan,
			@RequestParam String c_month, @RequestParam String unit,
			@RequestParam String save_date, @RequestParam String[] no,@RequestParam String up_id,
			@RequestParam(required=false) String[] m_date, @RequestParam(required=false) Float[] potential,
			@RequestParam(required=false) Float[] a, @RequestParam(required=false) Float[] v,
			@RequestParam(required=false) Float[] tongdian, @RequestParam(required=false) String[] measurer,
			@RequestParam(required=false) String[] remark) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		PotentialMeasure pm = new PotentialMeasure();
		pm.setId(id);
		pm.setPl_id(pl);
		pm.setPl_section_id(section);
		pm.setPl_spec_id(spec);
		pm.setJinzhan(jinzhan);
		pm.setC_month(Integer.valueOf(c_month.replace("-", "")));
		pm.setUnit(unit);
		//pm.setVerify_name(verify_name);
		pm.setSave_date(save_date);
		pm.setStatus(0);
		pm.setUp_id(up_id);
		pm.setCreater(ud.getId());
		List<PotentialMeasureDetail> pmdList = new ArrayList<PotentialMeasureDetail>();
		int i = 0;
		for (String n : no) {
			PotentialMeasureDetail pmd = new PotentialMeasureDetail();
			
			pmd.setNo(n);
			pmd.setM_date(DateFormatter.stringToDate(m_date[i],
						"yyyy-MM-dd"));
			pmd.setPotential(potential[i]);
			pmd.setMeasurer(measurer[i]);
			pmd.setRemark(remark[i]);
			pmdList.add(pmd);
			
			i++;
		}
		
		if (pmdList.size() > 0) {
			baseService.savePlMeasure(pm, pmdList);
			response.setMessage("保存成功");
			response.setStatus(ResponseStatus.OK);
		} else {
			response.setMessage("数据不全");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		}

		return response;
	}
}
