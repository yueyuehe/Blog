package cn.hm.oil.webservice;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.ValvePatrol;
import cn.hm.oil.domain.ValvePatrolDetail;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;

/**
 * 获取阀室、阀井巡检工作
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/services/base")
public class WS_VPatrol {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 获取阀室、阀井巡检工作列表
	 * @param pl
	 * @param section
	 * @param spec
	 * @param check_date
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/v_patrol/list")
	public @ResponseBody JsonResWrapper VPatrolList(@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
	 @RequestParam(required = false) Integer spec, @RequestParam(required = false) String check_date,HttpServletRequest request, @RequestParam(required=false) Integer status) {
		JsonResWrapper response = new JsonResWrapper();
		Map<String,Object> param = new HashMap<String, Object>();
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
		}
		if (!StringUtils.isBlank(check_date)) {
			Date date = DateFormatter.stringToDate(check_date, "yyyy-MM-dd");
			param.put("check_date", date);
		}
		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<ValvePatrol> vpList = baseService.queryValvePatrol(param, ps);
		response.setStatus(ResponseStatus.OK);
		response.setData(vpList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 通过ID获取 阀室、阀井巡检工作记录详细
	 * @param vp_id
	 * @return
	 */
	@RequestMapping(value="/v_patrol/show")
	public @ResponseBody JsonResWrapper VPatrolShow(@RequestParam Integer vp_id) {
		JsonResWrapper response = new JsonResWrapper();
		ValvePatrol vp = baseService.queryValvePatrolById(vp_id);
		response.setStatus(ResponseStatus.OK);
		response.setData(vp);
		return response;
	}
	
	/**
	 * 保存阀室，阀井巡检工作记录
	 * @param normal1
	 * @param normal2
	 * @param normal3
	 * @param normal4
	 * @param remark5
	 * @param valve_name
	 * @param check_date
	 * @param id
	 * @param checker
	 * @param pl
	 * @param section
	 * @param spec
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/v_patrol/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper VPatrolSave(@RequestParam(required = false) Integer normal1, @RequestParam(required = false) Integer normal2,
			@RequestParam(required = false) Integer normal3, @RequestParam(required = false) Integer normal4, @RequestParam(required = false) Integer normal5,  
			@RequestParam(required = false) Integer normal6, @RequestParam(required = false) Integer normal7, @RequestParam(required = false) Integer normal8, 
			@RequestParam(required = false) Integer normal9, @RequestParam(required = false) Integer normal10, @RequestParam(required = false) Integer normal11, 
			@RequestParam(required = false) Integer normal12, @RequestParam(required = false) Integer normal13, @RequestParam(required = false) String handle1, 
			@RequestParam(required = false) String handle2, @RequestParam(required = false) String handle3, @RequestParam(required = false) String handle4, 
			@RequestParam(required = false) String handle5, @RequestParam(required = false) String handle6, @RequestParam(required = false) String handle7, 
			@RequestParam(required = false) String handle8, @RequestParam(required = false) String handle9, @RequestParam(required = false) String handle10, 
			@RequestParam(required = false) String handle11, @RequestParam(required = false) String handle12, @RequestParam(required = false) String handle13, 
			@RequestParam(required = false) String remark1, @RequestParam(required = false) String remark9, @RequestParam(required = false) String remark10, 
			@RequestParam(required = false) String remark2, @RequestParam(required = false) String remark8, @RequestParam(required = false) String remark11, 
			@RequestParam(required = false) String remark3, @RequestParam(required = false) String remark7, @RequestParam(required = false) String remark12, 
			@RequestParam(required = false) String remark4, @RequestParam(required = false) String remark6, @RequestParam(required = false) String remark13, 
			@RequestParam(required = false) String remark5, @RequestParam String valve_name, @RequestParam String check_date,@RequestParam(required = false) Integer id,
			@RequestParam String checker, @RequestParam Integer pl, @RequestParam(required = false) Integer section, @RequestParam Integer spec,
			HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		ValvePatrol vp = new ValvePatrol();
		if(id != null && id.intValue() > 0) {
			vp.setId(id);
		}
		vp.setCheck_date(DateFormatter.stringToDate(check_date, "yyyy-MM-dd"));
		vp.setChecker(checker);
		vp.setPl_id(pl);
		vp.setPl_section_id(section);
		vp.setPl_spec_id(spec);
		vp.setCreater(ud.getId());
		vp.setValve_name(valve_name);
		//System.out.println(vp.getCheck_date().toString());
		ValvePatrolDetail vpd = new ValvePatrolDetail();
		vpd.setHandle1(handle1);
		vpd.setHandle10(handle10);
		vpd.setHandle11(handle11);
		vpd.setHandle12(handle12);
		vpd.setHandle13(handle13);
		vpd.setHandle2(handle2);
		vpd.setHandle3(handle3);
		vpd.setHandle4(handle4);
		vpd.setHandle5(handle5);
		vpd.setHandle6(handle6);
		vpd.setHandle7(handle7);
		vpd.setHandle8(handle8);
		vpd.setHandle9(handle9);
		vpd.setNormal1(normal1);
		vpd.setNormal2(normal2);
		vpd.setNormal3(normal3);
		vpd.setNormal4(normal4);
		vpd.setNormal5(normal5);
		vpd.setNormal6(normal6);
		vpd.setNormal7(normal7);
		vpd.setNormal8(normal8);
		vpd.setNormal9(normal9);
		vpd.setNormal10(normal10);
		vpd.setNormal11(normal11);
		vpd.setNormal12(normal12);
		vpd.setNormal13(normal13);
		vpd.setRemark1(remark1);
		vpd.setRemark2(remark2);
		vpd.setRemark3(remark3);
		vpd.setRemark4(remark4);
		vpd.setRemark5(remark5);
		vpd.setRemark6(remark6);
		vpd.setRemark7(remark7);
		vpd.setRemark8(remark8);
		vpd.setRemark9(remark9);
		vpd.setRemark10(remark10);
		vpd.setRemark11(remark11);
		vpd.setRemark12(remark12);
		vpd.setRemark13(remark13);
		baseService.saveVPatrol(vp, vpd);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("保存成功");
		return response;
	}
	
	/**
	 * 保存 阀室、阀井巡检工作审核
	 * @param verify_desc
	 * @param status
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/v_patrol/verify_save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper VPatrolSaveVerify(@RequestParam(required = false) String verify_desc, @RequestParam Integer status, 
			@RequestParam Integer id,HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		ValvePatrol vp = new ValvePatrol();
		vp.setId(id);
		vp.setStatus(status);
		vp.setVerify_desc(verify_desc);
		vp.setVerifier(ud.getId());
		baseService.saveValveVerify(vp);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("审核成功");
		return response;
	}
}
