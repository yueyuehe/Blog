package cn.hm.oil.webservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.Upgrade;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.UpgradeService;




@Controller
@RequestMapping(value = "/services")
public class WS_Upgrade {

	@Autowired
	private UpgradeService upgradeService;
	/**
	 * 获取更新信息
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/upgrade/info", method = RequestMethod.GET)
	public @ResponseBody
	JsonResWrapper upgradeInfo(Model model) {
		JsonResWrapper res = new JsonResWrapper();
		res.setStatus(ResponseStatus.OK);
		List<Upgrade> us = upgradeService.selectAllUpgrade();
		res.setData(us);
		return res;
	}
}
