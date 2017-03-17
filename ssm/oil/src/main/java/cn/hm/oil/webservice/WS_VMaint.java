package cn.hm.oil.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.ValveMaint;
import cn.hm.oil.domain.ValveMaintDetail;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;

/**
 * 阀室、阀井维护保养工作记录
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/services/base")
public class WS_VMaint {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 阀室、阀井维护保养工作记录列表查看
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/v_maint/list", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody JsonResWrapper VMaintList(HttpServletRequest request,@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			 @RequestParam(required = false) Integer spec, @RequestParam(required = false) String check_date, @RequestParam(required=false) Integer status) {
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
		if (!StringUtils.isBlank(check_date)) {
			Date date = DateFormatter.stringToDate(check_date, "yyyy-MM-dd");
			param.put("check_date", date);
		}
		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<ValveMaint> vmList = baseService.queryValveMaint(param, ps);
		response.setStatus(ResponseStatus.OK);
		response.setData(vmList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 通过id返回阀室，阀井维护保养工作记录
	 * @param vm_id
	 * @return
	 */
	@RequestMapping(value="/v_maint/show")
	public @ResponseBody JsonResWrapper VMaintShow(@RequestParam Integer vm_id) {
		JsonResWrapper response = new JsonResWrapper();
		ValveMaint vm = baseService.queryValveMaintById(vm_id);
		response.setStatus(ResponseStatus.OK);
		response.setData(vm);
		return response;
	}
	
	/**
	 * 保存/修改 阀室、阀井维护保养工作记录
	 * @param pl
	 * @param section
	 * @param spec
	 * @param valve_name 阀室名称
	 * @param check_date 时间
	 * @param id 记录id
	 * @param particips 参加人员
	 * @param content 工作内容
	 * @param question 存在问题说明
	 * @return
	 */
	@RequestMapping(value = "/v_maint/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper VMaintSave(HttpServletRequest request,@RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam(required = false) String valve_name, @RequestParam(required = false) String check_date, @RequestParam(required = false) Integer id,
			@RequestParam(required = false) String particips, @RequestParam(required = false) String content, @RequestParam(required = false) String question) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setMessage("请先登录！");
			response.setStatus(ResponseStatus.FAILED);
			return response;
		}
		ValveMaint vm = new ValveMaint();
		ValveMaintDetail vmd = new ValveMaintDetail();
		if(id != null && id.intValue() > 0) {
			vm.setId(id);
		}
		vm.setCreater(ud.getId());
		vm.setPl_id(pl);
		vm.setPl_section_id(section);
		vm.setPl_spec_id(spec);
		vm.setValve_name(valve_name);
		vm.setCheck_date(DateFormatter.stringToDate(check_date, "yyyy-MM-dd"));
		vm.setCreater(ud.getId());
		vm.setId(id);
		vmd.setContent(content);
		vmd.setParticips(particips);
		vmd.setQuestion(question);
		baseService.saveValveMaint(vm, vmd);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("保存成功");
		return response;
	}
	
	/**
	 * 阀室、阀井维护保养工作记录 审核保存
	 * @param model
	 * @param status
	 * @param verify_desc
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/v_maint/verify_save")
	public @ResponseBody JsonResWrapper VerifySave(HttpServletRequest request, @RequestParam Integer status, @RequestParam(required = false) String verify_desc,
			@RequestParam Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		ValveMaint vm = new ValveMaint();
		vm.setId(id);
		vm.setVerify_desc(verify_desc);
		vm.setStatus(status);
		vm.setVerifier(ud.getId());
		baseService.saveVMaintVerify(vm);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("审核成功");
		return response;
	}
}
