/**
 * 
 */
package cn.hm.oil.webservice;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.SysPush;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.PushService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.AndroidPushMessageSample;
import cn.hm.oil.util.Constants;

/**
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "/services")
public class WS_Login {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PushService pushService;

	/**
	 * App登录接口
	 * 
	 * @param request
	 * @param model
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper login(HttpServletRequest request, Model model, @RequestParam(required = false) String username, @RequestParam(required = false) String password,
			@RequestParam(required = false) Integer community_id) {
		JsonResWrapper response = new JsonResWrapper();
		PasswordEncoder pe = new BCryptPasswordEncoder();
		if (StringUtils.isBlank(username)) {
			response.setMessage("用户名为空！");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		} else if (StringUtils.isBlank(password)) {
			response.setMessage("密码为空！");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		} else {
			SysUsers user = userService.querySysUserByUsername(username);
			if(user == null || !pe.matches(password, user.getPassword())) {
				response.setMessage("用户名或密码错误！");
				response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
			} else {
				request.getSession().setAttribute(Constants.SESSION_APP_LOGIN_USER, user);
				//AndroidPushMessageSample.pushMessageAll("登陆成功", user.getName() + "登陆成功");
			}
		}
		System.out.println(response.getStatus());
		return response;
	}
	
	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param pid
	 * @return
	 */
	@RequestMapping(value = "/change_pwd", method = RequestMethod.GET)
	public @ResponseBody
	JsonResWrapper changePwd(HttpServletRequest request, @RequestParam(required = false) String old_password, @RequestParam(required = false) String password) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setMessage("请先登录！");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		} else if (StringUtils.isBlank(old_password)) {
			response.setMessage("原始密码[old_password]为空！");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		} else if (StringUtils.isBlank(password)) {
			response.setMessage("新密码[password]为空！");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		} else {
			PasswordEncoder spe = new StandardPasswordEncoder();
			SysUsers user = userService.querySysUserById(ud.getId());
			if (user == null) {
				response.setMessage("用户名错误！");
				response.setStatus(ResponseStatus.FAILED);
			} else if(!spe.matches(old_password, user.getPassword())) {
				response.setMessage("原始密码错误！");
				response.setStatus(ResponseStatus.FAILED);
			}else {
				String pwd = spe.encode(password);
				userService.updatePassword(user.getId(), pwd);
				response.setMessage("密码修改成功！");
			}
		}
		return response;
	}
	
	/**
	 * 退出登录接口
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login_out", method = RequestMethod.GET)
	public @ResponseBody JsonResWrapper loginOut(HttpServletRequest request, Model model) {
		request.getSession().removeAttribute(Constants.SESSION_APP_LOGIN_USER);
		request.getSession().invalidate();
		JsonResWrapper response = new JsonResWrapper();
		return response;
	}
}
