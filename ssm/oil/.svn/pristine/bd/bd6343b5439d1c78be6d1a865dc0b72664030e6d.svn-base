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
import cn.hm.oil.domain.RunRecord;
import cn.hm.oil.domain.RunRecordDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;

/**
 * 阴极保护站月综合运行记录
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/services/base")
public class WS_RunRecord {
	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;
	
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
	@RequestMapping(value = "/rc/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody JsonResWrapper rcRecord_list(HttpServletRequest request,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String r_month,
			@RequestParam(required = false) Integer status) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
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
		if (!StringUtils.isBlank(r_month)) {
			param.put("r_month", Integer.valueOf(r_month.replace("-", "")));
		}

		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<RunRecord> rcList = baseService.queryRunRecord(param, ps);

		response.setStatus(ResponseStatus.OK);
		response.setData(rcList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 阴极保护站月综合运行记录 查看
	 * @param rc_id
	 * @return
	 */
	@RequestMapping(value="/rc/show")
	public @ResponseBody JsonResWrapper rcRecordShow(@RequestParam Integer rc_id) {
		JsonResWrapper response = new JsonResWrapper();
		RunRecord rc = baseService.queryRunRecordById(rc_id);
		response.setStatus(ResponseStatus.OK);
		response.setData(rc);
		return response;
	}
	
	/**
	 * 阴极保护站月综合运行记录 保存
	 * @param id
	 * @param pl
	 * @param section
	 * @param spec
	 * @param jinzhan
	 * @param auditor
	 * @param r_month
	 * @param day
	 * @param time
	 * @param jldy
	 * @param zlsc_a
	 * @param zlsc_v
	 * @param tdddw
	 * @param recorder
	 * @param comment
	 * @param others
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/rc/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper rcRecordSave(@RequestParam(required = false) Integer id,
			@RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam String auditor,	@RequestParam String r_month, @RequestParam Integer[] day,
			@RequestParam Integer[] time, @RequestParam Float[] jldy, @RequestParam Float[] zlsc_a, @RequestParam Float[] zlsc_v,
			@RequestParam Float[] tdddw, @RequestParam String[] recorder, @RequestParam String[] comment, 
			@RequestParam String[] others,HttpServletRequest request) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		RunRecord rc = new RunRecord();
		if(id != null && id.intValue() > 0) {
			rc.setId(id);
		}
		rc.setAuditor(auditor);
		rc.setJinzhan(jinzhan);
		rc.setPl_id(pl);
		rc.setPl_section_id(section);
		rc.setPl_spec_id(spec);
		rc.setR_month(Integer.valueOf(r_month.replace("-", "")));
		rc.setStatus(0);
		rc.setCreater(ud.getId());
		
		List<RunRecordDetail> rcdList = new ArrayList<RunRecordDetail>();
		int i = 0;
		for (String r : recorder) {
			RunRecordDetail rcd = new RunRecordDetail();
			rcd.setDay(day[i]);
			rcd.setTime(time[i]);
			rcd.setJldy(jldy[i]);
			rcd.setOthers(others[i]);
			rcd.setRecorder(recorder[i]);
			rcd.setTdddw(tdddw[i]);
			rcd.setZlsc_a(zlsc_a[i]);
			rcd.setZlsc_v(zlsc_v[i]);
			rcd.setComment(comment[i]);

			rcdList.add(rcd);
			
			i++;
		}
		if (rcdList.size() > 0) {
			baseService.saveRcRecord(rc, rcdList);
			response.setStatus(ResponseStatus.OK);
			response.setMessage("保存成功");
		} else {
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
			response.setMessage("参数不全");
		}
		return response;
	}
	
	/**
	 * 阴极保护站月综合运行记录 审核保存
	 * @param model
	 * @param id
	 * @param status
	 * @param verify_desc
	 * @return
	 */
	@RequestMapping(value = "/rc/verify_save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper rc_verify_save(HttpServletRequest request,
			@RequestParam Integer id, @RequestParam Integer status,	@RequestParam String verify_desc) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		baseService.updateRunRecordVerifyStatus(id, ud.getId(), status,
				verify_desc);

		response.setStatus(ResponseStatus.OK);
		response.setMessage("审核成功");
		return response;
	}

}
