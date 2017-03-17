package cn.hm.oil.webservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.SpaceTime;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.Tips;
import cn.hm.oil.domain.UsersLocation;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.TipsService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.PageSupport;

@Controller
@RequestMapping(value = "/services/")
public class WS_Home {
	@Autowired
	private TipsService tipsService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/welcome")
	public @ResponseBody JsonResWrapper welcome(HttpServletRequest request, @RequestParam(required=false) Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user_id", ud.getId());
		List<Tips> tipsList = tipsService.queryTips(param, ps);
		response.setData(tipsList);
		return response;
	}
	
	@RequestMapping(value = "/save_location", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody JsonResWrapper saveLocation(HttpServletRequest request, @RequestParam Float longitude, @RequestParam Float latitude,
			@RequestParam String describe) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		UsersLocation lo = new UsersLocation();
		lo.setUser_id(ud.getId());
		lo.setLatitude(latitude);
		lo.setLongitude(longitude);
		lo.setDescribe(describe);
		userService.saveLocation(lo);
		response.setStatus(ResponseStatus.OK);
		Map<String,Object> param = new HashMap<String,Object>();
		SpaceTime time = userService.querySpaceTime();
		param.put("time", time);
		response.setData(param);
		return response;
	}
}
