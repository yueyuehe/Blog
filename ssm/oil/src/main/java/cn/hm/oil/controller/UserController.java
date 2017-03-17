package cn.hm.oil.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.FeedBack;
import cn.hm.oil.domain.SpaceTime;
import cn.hm.oil.domain.SysMenus;
import cn.hm.oil.domain.SysRoles;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.UsersLocation;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
/**
 * TODO
 * @author Admin
 *
 */
@Controller
@RequestMapping(value = "/admin/user")
public class UserController {

	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;

	/**
	 * 查看维护工gps信息
	 * @param request
	 * @param model
	 * @param pl
	 * @param section
	 * @param spec
	 * @return
	 */
	@RequestMapping(value = "/gps", method = {RequestMethod.GET,RequestMethod.POST})
	public String gps(HttpServletRequest request, Model model, @RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec) {
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);
			
			List<BasePipelineSection> sectionList = baseService.querySection(pl);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);
			
			List<BasePipelineSpec> specList = baseService.querySpec(section);
			model.addAttribute("specList", specList);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		param.put("role_id", SysRoles.WORKER);
		
		List<SysUsers> users = userService.queryWorkerUser(param, ps);
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		model.addAttribute("pipeLineList", pipeLineList);
		
		model.addAttribute("users", users);
		return "pages/user/gps";
	}
	
	
	@RequestMapping(value = "/gps_detail", method = {RequestMethod.GET,RequestMethod.POST})
	public String gps_detaile(Model model,HttpServletRequest request,@RequestParam(required = false) Integer id){
		SysUsers su = userService.querySysUserById(id);
		model.addAttribute("su", su);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", id);
		PageSupport ps = PageSupport.initPageSupport(request);
		List<UsersLocation> los = userService.queryUserLocationByUserId(param,ps); 
		model.addAttribute("los", los);
		return "pages/user/gps_detail";
	}
	
	@RequestMapping(value = "/gps_set", method = RequestMethod.GET)
	public String gps_set(Model model,@RequestParam(required = false) Integer add){
		SpaceTime st = userService.querySpaceTime();
		model.addAttribute("st", st);
		if(add!=null && add.intValue() == 1){
			model.addAttribute("msg", "保存成功!");
		}
		return "pages/user/gps_set";
	}
	
	
	@RequestMapping(value = "/gps_save", method = RequestMethod.POST)
	public String gps_save(Model model,@RequestParam(required = false) Integer set_time,@RequestParam(required = false) String start_time,@RequestParam(required = false) String end_time){
		SpaceTime st = new SpaceTime();
		st.setTime(set_time);
		st.setStart_time(start_time);
		st.setEnd_time(end_time);
		userService.saveSpaceTime(st);
		return "redirect:/admin/user/gps_set?add=1";
	}
	
	/**
	 * 创建、修改干部账号
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/leader/edit", method = RequestMethod.GET)
	public String leaderEdit(Model model, @RequestParam(required=false) Integer id, @RequestParam(required=false) Integer add) {
		List<SysMenus> menus = userService.queryRoleMenus(SysRoles.LEADER);
		List<Integer> filterMenus = null;
		if (id != null && id.intValue() > 0) {
			filterMenus = userService.queryMenuFilterByUsrId(id);
			SysUsers user = userService.querySysUserById(id);
			
			model.addAttribute("user", user);
		}
		
		Map<Integer, SysMenus> map = new LinkedHashMap<Integer, SysMenus>();
		Map<Integer, Integer> ot = new HashMap<Integer, Integer>();
		for (SysMenus sm : menus) {
			if (filterMenus != null && filterMenus.contains(sm.getId())) {
				sm.setDisable(1);
			}
			
			if (sm.getParent_id().intValue() == 0) {//一级菜单
				map.put(sm.getId(), sm);
				continue;
			}
			SysMenus psm = map.get(sm.getParent_id());
			if (psm != null) {
				psm.addSubMenus(sm);//设置二级菜单
				ot.put(sm.getId(), sm.getParent_id());
			} else {//三级菜单
				int mid = ot.get(sm.getParent_id());//获取一级菜单ID
				for (SysMenus tm : map.get(mid).getSubMenus()) {
					if (tm.getId().intValue() == sm.getParent_id().intValue()) {
						tm.addSubMenus(sm);
						break;
					}
				}
			}
		}
		
		List<SysMenus> newMenus = new ArrayList<SysMenus>();
		Iterator<SysMenus> is = map.values().iterator();
		while(is.hasNext()) {
			newMenus.add(is.next());
		}
		
		model.addAttribute("menus", newMenus);
		if (add != null && add.intValue() == -1) {
			model.addAttribute("msg", "保存失败，两次密码输入错误！");
		} else if (add != null && add.intValue() == 1) {
			model.addAttribute("msg", "保存成功！");
		}
		return "pages/user/leader_edit";
	}
	
	/**
	 * 保存干部账号
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/leader/save", method = RequestMethod.POST)
	public String leaderSave(Model model, @RequestParam String username, @RequestParam String name, @RequestParam(required=false) String mail_address,
			@RequestParam(required=false) String password, @RequestParam(required=false) String repassword,
			@RequestParam(required=false) String remark, @RequestParam(required=false) String removeMenuId,
			@RequestParam Integer id) {
		Integer status = 1;
		if (id.intValue() == 0 && (StringUtils.isBlank(password) || !StringUtils.equals(password, repassword))) {
			status = -1;
		} else if (id.intValue() >= 0 && !StringUtils.isBlank(password) && !StringUtils.equals(password, repassword)) {
			status = -1;
		}
		
		if (status == 1) {
			SysUsers su = new SysUsers();
			su.setUsername(username);
			su.setName(name);
			su.setMail_address(mail_address);
			if(!StringUtils.isBlank(password)){
				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String pwd = passwordEncoder.encode(password);
				su.setPassword(pwd);
			}
			su.setRemark(remark);
			String [] rids = null;
			if (!StringUtils.isBlank(removeMenuId)) {
				rids = removeMenuId.split(",");
			}
			su.setId(id);
			
			userService.saveLeaderUser(su, rids);
		}
		return "redirect:/admin/user/leader/edit?add=" + status;
	}
	
	/**
	 * 修改维护工账号
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/worker/edit", method = RequestMethod.GET)
	public String workerEdit(Model model, @RequestParam Integer id, @RequestParam(required=false) Integer add) {
		SysUsers user = userService.querySysUserById(id);
		
		model.addAttribute("user", user);
		if (add != null && add.intValue() == -1) {
			model.addAttribute("msg", "保存失败，两次密码输入错误！");
		} else if (add != null && add.intValue() == 1) {
			model.addAttribute("msg", "保存成功！");
		}
		return "pages/user/worker_edit";
	}
	
	/**
	 * 保存维护工账号
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/worker/save", method = RequestMethod.POST)
	public String workerSave(Model model, @RequestParam String username, @RequestParam String name, @RequestParam String mail_address,
			@RequestParam(required=false) String password, @RequestParam(required=false) String repassword,
			@RequestParam(required=false) String remark, @RequestParam Integer id) {
		Integer status = StringUtils.equals(password, repassword) ? 1 : -1;
		
		if (status == 1) {
			SysUsers su = new SysUsers();
			su.setUsername(username);
			su.setName(name);
			su.setMail_address(mail_address);
			if(!StringUtils.isBlank(password)){
				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String pwd = passwordEncoder.encode(password);
				su.setPassword(pwd);
			}
			su.setRemark(remark);
			su.setId(id);
			
			userService.saveUser(su);
		}
		return "redirect:/admin/user/worker/edit?id=" + id + "&add=" + status;
	}
	
	/**
	 * 维护工账号查看
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/worker/list", method = {RequestMethod.POST, RequestMethod.GET})
	public String workerList(HttpServletRequest request, Model model, @RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
			model.addAttribute("pl", pl);
			
			List<BasePipelineSection> sectionList = baseService.querySection(pl);
			model.addAttribute("sectionList", sectionList);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
			model.addAttribute("section", section);
			
			List<BasePipelineSpec> specList = baseService.querySpec(section);
			model.addAttribute("specList", specList);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
			model.addAttribute("spec", spec);
		}
		
		PageSupport ps = PageSupport.initPageSupport(request);
		param.put("role_id", SysRoles.WORKER);
		
		List<SysUsers> users = userService.queryWorkerUser(param, ps);
		
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(param);
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("users", users);
		return "pages/user/worker_list";
	}
	
	/**
	 * 干部账号查看
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/leader/list", method = {RequestMethod.POST, RequestMethod.GET})
	public String leaderList(HttpServletRequest request, Model model, @RequestParam(required=false) Integer del) {
		
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("role_id", SysRoles.LEADER);
		
		List<SysUsers> users = userService.queryUserByRole(param, ps);
		
		model.addAttribute("users", users);
		if (del != null) {
			model.addAttribute("msg", "删除成功！");
		}
		return "pages/user/leader_list";
	}
	
	/**
	 * 删除干部账号
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/leader/del", method = RequestMethod.GET)
	public String leaderDel(Model model, @RequestParam Integer id) {
		userService.deleteUser(id);
		
		return "redirect:/admin/user/leader/list?del=1";
	}
	
	/**
	 * 修改密码
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mod_pwd", method = RequestMethod.GET)
	public String mod_pwd(Model model) {
		return "pages/user/mod_pwd";
	}

	@RequestMapping(value = "/modPassword", method = { RequestMethod.POST })
	public String savePassword(HttpServletRequest request, Model model, @RequestParam String historyPwd, @RequestParam String newPwd) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		SysUsers u = userService.querySysUserByUsername(ud.getUsername());
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (passwordEncoder.matches(historyPwd, u.getPassword())) {
			userService.updatePassword(u.getId(), passwordEncoder.encode(newPwd));
			model.addAttribute("state", "修改成功！");
		} else {
			model.addAttribute("state", "修改失败！原始密码不正确！");
		}
		return "pages/user/mod_pwd";
	}
	
	@RequestMapping(value = "/checkUsername", method = RequestMethod.GET)
	public @ResponseBody String checkUsername(HttpServletRequest request, Model model, @RequestParam String username, @RequestParam(required=false) Integer id) {
		if (id != null && id.intValue() == 0) {
			id = null;
		}
		Integer count = userService.querySysUserByUsernameID(id, username);
		String info = "ok";
		if (count != null && count.intValue() > 0) {
			info = "exists";
		}
		return JsonUtil.toJson(info);
	}
	
	@RequestMapping(value = "/feedback", method = { RequestMethod.GET })
	public String feedback(HttpServletRequest request, Model model, 
			@RequestParam(required = false) Integer save) {
		if(save != null && save.intValue() == 1){
			model.addAttribute("msg", "保存成功!");
		}
		return "pages/user/feedback";
	}
	
	@RequestMapping(value = "/feedback_save", method = { RequestMethod.POST })
	public String feedback(HttpServletRequest request, Model model, 
			@RequestParam String title, 
			@RequestParam String content,
			@RequestParam String name,
			@RequestParam String phone) {
		FeedBack fb = new FeedBack();
		fb.setContent(content);
		fb.setName(name);
		fb.setPhone(phone);
		fb.setTitle(title);
		try {
			List<String> paths = DataUtil.uploadFile(request, "file");
			if (!CollectionUtils.isEmpty(paths)) {
				fb.setPath(paths.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		userService.saveFeedBack(fb);
		return "redirect:feedback?save=1";
	}
	
	@RequestMapping(value = "/feedback_list", method = { RequestMethod.GET })
	public String feedback_list(HttpServletRequest request, Model model) {
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		List<FeedBack> bs = userService.queryFeedBacks(param, ps);
		model.addAttribute("bs", bs);
		return "pages/user/feedback_list";
	}
	
	@RequestMapping(value = "/feedback_detail", method = { RequestMethod.GET })
	public String feedback_detail(HttpServletRequest request, Model model,@RequestParam Integer id,@RequestParam(required = false) Integer update) {
		FeedBack fb = userService.queryFeedBackById(id);
		model.addAttribute("fb", fb);
		if(update != null && update.intValue() == 1){
			model.addAttribute("msg", "状态改变成功!");
		}
		return "pages/user/feedback_detail";
	}
	
	@RequestMapping(value = "/feedback_update", method = { RequestMethod.GET })
	public String feedback_update(HttpServletRequest request, Model model,@RequestParam Integer id,@RequestParam Integer close) {
		userService.updateFeedBackClose(id, close);;
		return "redirect:feedback_detail?update=1&id="+id;
	}
	
	
}