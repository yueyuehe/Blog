package cn.hm.oil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.PromptType;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.util.JsonUtil;
@Controller
@RequestMapping(value = "/admin/base")
public class PromptController {
	
	@Autowired
	private BaseService baseService;
	/**
	 * 填写注意事项的修改页面
	 * @return
	 */
	@RequestMapping(value = "/edit_prompt", method = RequestMethod.GET)
	public String editPrompt(Model model) {
		List<PromptType> ptList = baseService.queryPromptType();
		model.addAttribute("ptList", ptList);
		return "pages/base/edit_prompt";
	}
	
	@RequestMapping(value = "/save_prompt")
	public @ResponseBody String savePrompt(Model model, @RequestParam Integer type_id, @RequestParam String describe) {
		Prompt prompt = new Prompt();
		prompt.setDescribe(describe);
		prompt.setType_id(type_id);
		baseService.updatePromptByType(prompt);
		return JsonUtil.toJson("OK");
	}
	
	/**
	 * 查询指定类型提示内容
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/get_prompt", method = RequestMethod.GET)
	public @ResponseBody Prompt queryPrompt(Model model, @RequestParam Integer type_id) {
		return baseService.quertPromptByType(type_id);
	}
}
