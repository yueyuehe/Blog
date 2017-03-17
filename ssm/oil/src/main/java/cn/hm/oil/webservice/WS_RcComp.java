package cn.hm.oil.webservice;

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

import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.RunRecordcomprehensive;
import cn.hm.oil.domain.RunRecordcomprehensiveDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.PageSupport;

/**
 * 阴极保护站运行月综合记录
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/services/base")
public class WS_RcComp {

	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;
	
	/**
	 * 阴极保护站运行月综合记录 列表
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param r_month
	 * @return
	 */
	@RequestMapping(value="/rc_comp/list")
	public @ResponseBody JsonResWrapper RcCompList(HttpServletRequest request,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String r_month,
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
		if (!StringUtils.isBlank(r_month)) {
			param.put("r_month", Integer.valueOf(r_month.replace("-", "")));
		}
		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<RunRecordcomprehensive> rcList = baseService.queryRunRecordcomprehensive(param, ps);
		response.setStatus(ResponseStatus.OK);
		response.setData(rcList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	 
	/**
	 * 阴极保护站运行月综合记录 保存
	 * @param model
	 * @param id
	 * @param pl
	 * @param comment
	 * @param section
	 * @param spec
	 * @param jinzhan
	 * @param r_month
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/rc_comp/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper RcCompSave(@RequestParam(required = false) Integer id,
			@RequestParam Integer pl, @RequestParam String comment,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam String r_month,
			@RequestParam String created_by, @RequestParam String auditor,
			@RequestParam String estimated, @RequestParam String actual,
			@RequestParam String wdtd, @RequestParam String jztd,
			@RequestParam String ljtd, @RequestParam String jcgxtd,
			@RequestParam String fljctd, @RequestParam String qttd,
			@RequestParam String o_max_a, @RequestParam String o_min_a,
			@RequestParam String o_avg_a, @RequestParam String o_max_v,
			@RequestParam String o_min_v, @RequestParam String o_avg_v,
			@RequestParam String tdd_v_max, @RequestParam String tdd_v_min,
			@RequestParam String sdl, @RequestParam String bhl,
			@RequestParam String sbwhl, HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		RunRecordcomprehensive rec = new RunRecordcomprehensive();
		if(id != null && id.intValue() > 0){
			rec.setId(id);
		}
		rec.setAuditor(auditor);
		rec.setCreated_by(created_by);
		rec.setJinzhan(jinzhan);
		rec.setPl_id(pl);
		rec.setPl_section_id(section);
		rec.setPl_spec_id(spec);
		rec.setR_month(Integer.valueOf(r_month.replace("-", "")));
		rec.setStatus(0);
		rec.setCreater(ud.getId());
		
		RunRecordcomprehensiveDetail recd = new RunRecordcomprehensiveDetail();
		recd.setEstimated(estimated);
		recd.setActual(actual);
		recd.setWdtd(wdtd);
		recd.setJztd(jztd);
		recd.setLjtd(ljtd);
		recd.setJcgxtd(jcgxtd);
		recd.setFljctd(fljctd);
		recd.setQttd(qttd);
		recd.setO_max_a(o_max_a);
		recd.setO_min_a(o_min_a);
		recd.setO_avg_a(o_avg_a);
		recd.setO_avg_v(o_avg_v);
		recd.setO_max_v(o_max_v);
		recd.setO_min_v(o_min_v);
		recd.setTdd_v_max(tdd_v_max);
		recd.setTdd_v_min(tdd_v_min);
		recd.setSdl(sdl);
		recd.setBhl(bhl);
		recd.setSbwhl(sbwhl);
		recd.setComment(comment);
		if (recd != null) {
			baseService.saveRcComp(rec, recd);
		}
		response.setMessage("保存成功");
		response.setStatus(ResponseStatus.OK);
		return response;
	}
	
	/**
	 * 阴极保护站运行月综合记录 详细
	 * @param rc_id
	 */
	@RequestMapping(value="/rc_comp/show")
	public @ResponseBody JsonResWrapper RcCompShow(@RequestParam Integer rc_id) {
		JsonResWrapper response = new JsonResWrapper();
		RunRecordcomprehensive rc = baseService.queryRunRecordcomprehensiveById(rc_id);
		response.setStatus(ResponseStatus.OK);
		response.setData(rc);
		return response;
	}
	
	/**
	 * 阴极保护站运行月综合记录 审核保存
	 * @param id
	 * @param status
	 * @param verify_desc
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/rc_comp/verify_save")
	public @ResponseBody JsonResWrapper RcCompVerifySave(@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc,HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		baseService.updateRunRecordcomprehensiveVerifyStatus(id, ud.getId(), status,verify_desc);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("审核成功");
		return response;
	}
}
