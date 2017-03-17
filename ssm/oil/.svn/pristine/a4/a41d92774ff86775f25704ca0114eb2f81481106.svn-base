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
import cn.hm.oil.domain.DynamicConsequence;
import cn.hm.oil.domain.DynamicConsequenceDetail;
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

/**
 * 高后果区资料管理（动态）
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/services/base")
public class WS_DSequel {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 获取动态高后果区资料列表
	 * @param model
	 * @param request
	 * @param authentication
	 * @param pl
	 * @param section
	 * @param spec
	 * @param create_time
	 * @return
	 */
	@RequestMapping(value="/d_sequel/list")
	public @ResponseBody JsonResWrapper DSequelList(HttpServletRequest request, @RequestParam(required=false) Integer pl,@RequestParam(required=false) Integer section, 
			@RequestParam(required=false) Integer spec,@RequestParam(required=false) String create_time, @RequestParam(required = false) Integer status) {
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
		List<DynamicConsequence> dcList = baseService.queryDynamicConsequence(param, ps);
		//System.out.println(hcList.size());
		response.setStatus(ResponseStatus.OK);
		response.setData(dcList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	

	/**
	 * 动态保存
	 * @param model
	 * @param pl
	 * @param section
	 * @param spec
	 * @param id
	 * @param unit_name
	 * @param pipeline_name
	 * @param section_name
	 * @param num
	 * @param s_start
	 * @param s_end
	 * @param s_length
	 * @param place_name
	 * @param description
	 * @param u_date
	 * @param recogner
	 * @param recogner_tel
	 * @param remark
	 * @param request
	 * @param fileName
	 * @return
	 */
	@RequestMapping(value="/d_sequel/save", method=RequestMethod.POST)
	public @ResponseBody JsonResWrapper DSequelSave(@RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec, @RequestParam(required = false) Integer id,
			@RequestParam String[] unit_name,@RequestParam String[] pipeline_name, @RequestParam String[] section_name, @RequestParam Integer[] num, @RequestParam Integer[] s_start,
			@RequestParam Integer[] s_end, @RequestParam Float[] s_length, @RequestParam String[] place_name, @RequestParam String[] description, @RequestParam String[] u_date,
			@RequestParam String[] recogner, @RequestParam String[] recogner_tel, @RequestParam String[] remark, @RequestParam String[] annex_file, HttpServletRequest request) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		DynamicConsequence dc = new DynamicConsequence();
		dc.setPl_id(pl);
		dc.setPl_section_id(section);
		dc.setPl_spec_id(spec);
		dc.setCreater(ud.getId());
		dc.setId(id);
		
		/*List<String> annex_fileList = null;
		try {
			annex_fileList = DataUtil.uploadFileList(request, "annex_file");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		List<DynamicConsequenceDetail> dcdList = new ArrayList<DynamicConsequenceDetail>();
		
		int i = 0;
		for(String u : unit_name) {
			if(StringUtils.isBlank(u) && StringUtils.isBlank(pipeline_name[i]) && StringUtils.isBlank(section_name[i])) {
				break;
			}
			DynamicConsequenceDetail dcd = new DynamicConsequenceDetail();
			dcd.setUnit_name(u);
			dcd.setNum(num[i]);
			dcd.setPipeline_name(pipeline_name[i]);
			dcd.setPlace_name(place_name[i]);
			dcd.setRecogner(recogner[i]);
			//System.out.println(recogner_tel[i]);
			dcd.setRecogner_tel(recogner_tel[i]);
			dcd.setRemark(remark[i]);
			dcd.setS_end(s_end[i]);
			dcd.setS_start(s_start[i]);
			dcd.setS_length(s_length[i]);
			dcd.setSection_name(section_name[i]);
			dcd.setU_date(DateFormatter.stringToDate(u_date[i], "yyyy-MM-dd"));
			dcd.setDescription(description[i]);
			
			dcd.setAnnex_file(annex_file[i]);
			
			dcdList.add(dcd);
			i++;
		}
		
		baseService.saveDynamicConseq(dc, dcdList);
		
		response.setMessage("保存成功");
		response.setStatus(ResponseStatus.OK);
		return response;
	}
}
