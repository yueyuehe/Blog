package cn.hm.oil.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.Tips;
import cn.hm.oil.service.TipsService;
import cn.hm.oil.util.PageSupport;

@Controller
public class HomeController {

	@Autowired
	private TipsService tipsService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, Model model, @RequestParam(required = false) String error) {
		model.addAttribute("error", error);
		return "pages/frame/login";
	}

	@RequestMapping(value = { "/", "/index" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String home(Model model) {
		return "pages/frame/main";
	}

	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public String header(Model model, @RequestParam(required = false) String error) {
		return "pages/frame/top";
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcome(HttpServletRequest request, Model model, @RequestParam(required = false) String error, @RequestParam(required=false) Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user_id", ud.getId());
		List<Tips> tipsList = tipsService.queryTips(param, ps);
		model.addAttribute("tipsList", tipsList);
		return "pages/frame/index";
	}

	@RequestMapping(value = "/left", method = RequestMethod.GET)
	public String menuList(Model model, @RequestParam(required = false) String error) {
		return "pages/frame/left";
	}
}