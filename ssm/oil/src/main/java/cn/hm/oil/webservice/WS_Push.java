package cn.hm.oil.webservice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.SysPush;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.PushService;
import cn.hm.oil.util.Constants;

@Controller
@RequestMapping(value = "/services/base")
public class WS_Push {
	@Autowired
	private PushService pushService;
	
	@RequestMapping(value = "/push/save", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody JsonResWrapper savePush(HttpServletRequest request, @RequestParam Long ChannelId, @RequestParam String UserId) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		//System.out.println("=================运行到这里===================");
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		System.out.println(ud.getId() + "========================================ID");
		SysPush sysPush = pushService.queryPush(ud.getId());
		System.out.println(sysPush);
		if(sysPush == null) {
			sysPush = new SysPush();
		}
		sysPush.setUser_id(ud.getId());
		sysPush.setChannelId(ChannelId);
		sysPush.setUserId(UserId);
		
		pushService.savePush(sysPush);
		response.setStatus(ResponseStatus.OK);
		return response;
	}
}
