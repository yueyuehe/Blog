package cn.hm.oil.webservice;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import cn.hm.oil.domain.HighConsequence;
import cn.hm.oil.domain.HighConsequenceDetail;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;

@Controller
@RequestMapping(value = "/services/base")
public class WS_HSequel {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 高后果区资料查看
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param create_time
	 * @return
	 */
	@RequestMapping(value = "/h_sequel/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody JsonResWrapper hSequel_list(HttpServletRequest request, @RequestParam(required=false) Integer pl,@RequestParam(required=false) Integer section, 
			@RequestParam(required=false) Integer spec,@RequestParam(required=false) String create_time, @RequestParam(required=false) Integer status) {
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
		if (!StringUtils.isBlank(create_time)) {
			Date date = DateFormatter.stringToDate(create_time, "yyyy-MM-dd");
			param.put("create_time", date);
		}
		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<HighConsequence> hcList = baseService.queryHighConsequence(param, ps);
		//System.out.println(hcList.size());

		response.setStatus(ResponseStatus.OK);
		response.setData(hcList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 资料保存
	 * @param model
	 * @param request
	 * @param id
	 * @param pl
	 * @param section
	 * @param spec
	 * @param num
	 * @param start_lat
	 * @param start_lon
	 * @param end_lat
	 * @param end_lon
	 * @param s_start
	 * @param s_end
	 * @param s_length
	 * @param s_soure
	 * @param place_name
	 * @param description
	 * @param u_date
	 * @param recogner
	 * @param pic1
	 * @param pic2
	 * @param pic3
	 * @param pic4
	 * @param pic5
	 * @return
	 */
	@RequestMapping(value = "/h_sequel/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper hSequel_save(Model model,HttpServletRequest request,
			@RequestParam(required = false) Integer id,@RequestParam Integer pl,
			@RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam Integer[] num,@RequestParam Double[] start_lat,
			@RequestParam Double[] start_lon,@RequestParam Double[] end_lat,
			@RequestParam Double[] end_lon,@RequestParam Integer[] s_start,
			@RequestParam Integer[] s_end,@RequestParam Float[] s_length,
			@RequestParam Integer[] s_soure,@RequestParam String[] place_name,
			@RequestParam String[] description,@RequestParam String[] u_date,@RequestParam String[] recogner,
			@RequestParam String[] pic1,@RequestParam String[] pic2,@RequestParam String[] pic3
			,@RequestParam String[] pic4
			,@RequestParam String[] pic5) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		HighConsequence hc = new HighConsequence();
		hc.setCreater(ud.getId());
		hc.setPl_id(pl);
		hc.setPl_section_id(section);
		hc.setPl_spec_id(spec);
		hc.setId(id);
		List<String> pic1List = new ArrayList<String>();
		List<String> pic2List = new ArrayList<String>();
		List<String> pic3List = new ArrayList<String>();
		List<String> pic4List = new ArrayList<String>();
		List<String> pic5List = new ArrayList<String>();
		
		//获取图片1列表
		//try {
			for (String name : pic1) {
				if (!StringUtils.isBlank(name)) {
					String fn = name;
					pic1List.add(fn);
					
				} else {
					pic1List.add(null);
				}
			}
			for (String name : pic2) {
				if (!StringUtils.isBlank(name)) {
					String fn = name;
					pic2List.add(fn);
				} else {
					pic2List.add(null);
				}
			}
			for (String name : pic3) {
				if (!StringUtils.isBlank(name)) {
					String fn = name;
					pic3List.add(fn);
				} else {
					pic3List.add(null);
				}
			}
			for (String name : pic4) {
				if (!StringUtils.isBlank(name)) {
					String fn = name;
					pic4List.add(fn);
				} else {
					pic4List.add(null);
				}
			}
			for (String name : pic5) {
				if (!StringUtils.isBlank(name)) {
					String fn = name;
					pic5List.add(fn);
				} else {
					pic5List.add(null);
				}
			}
			
		/*} catch (Exception e) {
			e.printStackTrace();
		}*/
		List<HighConsequenceDetail> hcdList = new ArrayList<HighConsequenceDetail>();
		int i = 0;
		for(Integer pn : num) {
			if(pn == null) {
				break;
			}
			HighConsequenceDetail hcd = new HighConsequenceDetail();
			hcd.setNum(num[i]);
			hcd.setStart_lat(start_lat[i]);
			hcd.setEnd_lat(end_lat[i]);
			hcd.setPlace_name(place_name[i]);
			hcd.setStart_lon(start_lon[i]);
			hcd.setEnd_lon(end_lon[i]);
			hcd.setS_start(s_start[i]);
			hcd.setS_end(s_end[i]);
			hcd.setS_length(s_length[i]);
			hcd.setS_soure(s_soure[i]);
			hcd.setDescription(description[i]);
			hcd.setU_date(DateFormatter.stringToDate(u_date[i],
						"yyyy-MM-dd"));
			hcd.setRecogner(recogner[i]);
			hcd.setPic1(pic1List.get(i));
			hcd.setPic2(pic2List.get(i));
			hcd.setPic3(pic3List.get(i));
			hcd.setPic4(pic4List.get(i));
			hcd.setPic5(pic5List.get(i));
			//System.out.println(hcd.getPic1());
			hcdList.add(hcd);
			i++;
		}
		//System.out.println(i);
		
		if (hcdList.size() > 0) {
			baseService.saveHighConse(hc, hcdList);
			response.setMessage("保存成功");
			response.setStatus(ResponseStatus.OK);
		} else if (hcdList.size() == 0) {
			response.setMessage("数据不全");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		}
		
		return response;
	}
}
