package cn.hm.oil.util;

import java.util.Map;

import org.springframework.security.core.Authentication;

import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.service.UserService;

public class CommonsMethod {
	
	/**
	 * 维护工只能查看自己的数据
	 * @param authentication
	 * @param userService
	 * @param param
	 * @return
	 */
	public static Integer getDataByRole(Authentication authentication,UserService userService,	Map<String, Object> param, Boolean all){
		
		LoginUserInfo ud = (LoginUserInfo) authentication.getPrincipal();
		Integer role = userService.queryRoleIdByUserId(ud.getId());
		if (role !=null && role == 3) {
			//判断用户是否是维护工，维护工只查看自己的信息
			param.put("whCreater", ud.getId());//预定删除
			param.put("creater", ud.getId());
		}		
		if(all != null)
			param.put("all", all);
		if(role != null && (all == null || all.booleanValue() == false))
		{
			if(role == 3)
				param.put("user_id", ud.getId());
			else
				param.put("up_id", ud.getId());
		}
		if(role != null)
		{
			param.put("role", role);
		}
		return role;
	}
	
	public static Integer getDataByRole(Authentication authentication,UserService userService,	Map<String, Object> param){		
		return CommonsMethod.getDataByRole(authentication, userService, param, null);
	}
	
	public static String putUrlParam(String params, String key, Object t)
	{
		if(t == null || t.toString().length() == 0)
			return params;
		if(params.isEmpty())
			params = "?";
		else
			params += "&";
		params += key + "=" + t.toString();
		return params;
	}
}
