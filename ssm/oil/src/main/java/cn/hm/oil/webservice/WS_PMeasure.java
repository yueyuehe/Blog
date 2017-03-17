package cn.hm.oil.webservice;

import java.util.ArrayList;
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

import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PerformanceMeasure;
import cn.hm.oil.domain.PerformanceMeasureDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.PageSupport;

/**
 * 绝缘接头(法兰)性能测量记录
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/services/base")
public class WS_PMeasure {
	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;
	
	/**
	 * 绝缘接头(法兰)性能测量记录 列表查看
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param m_year
	 * @return
	 */
	@RequestMapping(value="/p_measure/list")
	public @ResponseBody JsonResWrapper pMeasure_list(HttpServletRequest request,
			@RequestParam(required = false) Integer pl,	@RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) Integer m_year,
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

		if (m_year != null && m_year.intValue() > 0) {
			param.put("m_year", m_year);
		}

		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<PerformanceMeasure> pmList = baseService.queryPerformanceMeasure(param, ps);
		
		response.setData(pmList);
		response.setStatus(ResponseStatus.OK);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 绝缘接头(法兰)性能测量记录 保存
	 * @param model
	 * @param id
	 * @param pl
	 * @param section
	 * @param spec
	 * @param jinzhan
	 * @param m_year
	 * @param created_by
	 * @param auditor
	 * @return
	 */
	@RequestMapping(value = "/p_measure/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper pMeasure_save(HttpServletRequest request,
			@RequestParam(required = false) Integer id,	@RequestParam Integer pl,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam Integer m_year,
			@RequestParam String created_by, @RequestParam String auditor,
			@RequestParam String[] position, @RequestParam String[] month_1,
			@RequestParam String[] month_2, @RequestParam String[] month_3,
			@RequestParam String[] month_4, @RequestParam String[] month_5,
			@RequestParam String[] month_6, @RequestParam String[] month_7,
			@RequestParam String[] month_8, @RequestParam String[] month_9,
			@RequestParam String[] month_10, @RequestParam String[] month_11,
			@RequestParam String[] month_12) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		PerformanceMeasure pm = new PerformanceMeasure();
		if(id != null && id.intValue() > 0) {
			pm.setId(id);
		}
		pm.setAuditor(auditor);
		pm.setCreated_by(created_by);
		pm.setJinzhan(jinzhan);
		pm.setM_year(m_year);
		pm.setPl_id(pl);
		pm.setPl_section_id(section);
		pm.setPl_spec_id(spec);
		pm.setStatus(0);
		pm.setCreater(ud.getId());
		
		List<PerformanceMeasureDetail> pmdList = new ArrayList<PerformanceMeasureDetail>();
		int i = 0;
		for (String m1 : month_1) {
			PerformanceMeasureDetail pmd = new PerformanceMeasureDetail();
				if (i == 0) {
					pmd.setType(1);
				} else if ((i % 2) == 1) {
					pmd.setType(2);
				} else {
					pmd.setType(3);
				}
				if (i > 0) {
					pmd.setPosition(position[(i - 1) / 2]);
				}
				pmd.setMonth_1(month_1[i]);
				pmd.setMonth_2(month_2[i]);
				pmd.setMonth_3(month_3[i]);
				pmd.setMonth_4(month_4[i]);
				pmd.setMonth_5(month_5[i]);
				pmd.setMonth_6(month_6[i]);
				pmd.setMonth_7(month_7[i]);
				pmd.setMonth_8(month_8[i]);
				pmd.setMonth_9(month_9[i]);
				pmd.setMonth_10(month_10[i]);
				pmd.setMonth_11(month_11[i]);
				pmd.setMonth_12(month_12[i]);

				pmdList.add(pmd);
			
			i++;
		}
		if (pmdList.size() > 0) {
			baseService.savePMeasure(pm, pmdList);
			response.setStatus(ResponseStatus.OK);
			response.setMessage("保存成功");
		} else {
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
			response.setMessage("参数不全");
		}
		
		return response;
	}
	
	/**
	 * 绝缘接头(法兰)性能测量记录 详细
	 * @param pm_id
	 * @return
	 */
	@RequestMapping(value = "/p_measure/show")
	public @ResponseBody JsonResWrapper PMeasureShow(@RequestParam Integer pm_id) {
		JsonResWrapper response = new JsonResWrapper();
		PerformanceMeasure pm = baseService.queryPerformanceMeasureById(pm_id);
		response.setData(pm);
		response.setStatus(ResponseStatus.OK);
		return response;
	}
}
