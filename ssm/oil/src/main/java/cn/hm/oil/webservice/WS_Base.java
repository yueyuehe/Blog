package cn.hm.oil.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.mail.iap.Response;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;

@Controller
@RequestMapping(value = "/services/base")
public class WS_Base {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;

	/**
	 * 获取管线下的所有起止段落
	 * @param by_user 是否通过用户查询,1:是
	 * @param pl_id
	 * @return
	 */
	@RequestMapping(value = "/section/get", method = RequestMethod.GET)
	public @ResponseBody JsonResWrapper section_get(@RequestParam Integer pl_id,@RequestParam(required=false) Integer by_user,HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		List<BasePipelineSection> pps = null;
		if(by_user != null && by_user.intValue() == 1) {
			SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
			if(ud == null) {
				response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
				response.setMessage("请先登录");
				return response;
			} else {
				pps = baseService.querySectionByUser(pl_id, ud.getId());
			}
		} else {
			pps = baseService.querySection(pl_id);
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pps);
		return response;
	}
	
	/**
	 * 获取所有管线
	 * @param by_user 是否通过用户查询，1：是
	 * @return
	 */
	@RequestMapping(value = "/pipeline/get")
	public @ResponseBody JsonResWrapper pipeline_get(HttpServletRequest request, @RequestParam(required=false) Integer by_user) {
		JsonResWrapper response = new JsonResWrapper();
		List<BasePipeline> pl = null;
		if(by_user != null && by_user.intValue() == 1) {
			SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
			if(ud == null) {
				response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
				response.setMessage("请先登录");
				return response;
			} else {
			//System.out.println(ud.toString());
				pl = baseService.queryPipeLineByUser(ud.getId());
			}
		}else {
			pl = baseService.queryPipeLine(new HashMap<String,Object>());
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pl);
		return response;
	}
	
	/**
	 * 获取起止管段下的规格
	 * @param pl_section_id
	 * @param by_user 是否通过用户查询，1：是
	 * @return
	 */
	@RequestMapping(value= "/spec/get" )
	public @ResponseBody JsonResWrapper spec_get(@RequestParam Integer pl_section_id, @RequestParam(required=false) Integer by_user, HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		List<BasePipelineSpec> pls = null;
		if(by_user != null && by_user.intValue() == 1) {
			SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
			if(ud == null) {
				response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
				response.setMessage("请先登录");
				return response;
			} else {
				pls = baseService.querySpecByUser(pl_section_id, ud.getId());
			}
		} else {
			pls = baseService.querySpec(pl_section_id);
		}
		response.setStatus(ResponseStatus.OK);
		response.setData(pls);
		return response;
	}
	
	/**
	 * 获取上报上级的列表
	 * @return
	 */
	@RequestMapping(value = "/getReceiver")
	public @ResponseBody JsonResWrapper getReceiver() {
		JsonResWrapper response = new JsonResWrapper();
		List<BaseReceiver> br = baseService.queryLeaderList();
		response.setStatus(ResponseStatus.OK);
		response.setData(br);
		return response;
	}
}
