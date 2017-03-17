package cn.hm.oil.webservice;

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
import cn.hm.oil.domain.FacilitiesMaintenance;
import cn.hm.oil.domain.FacilitiesMaintenanceDetail;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;

/**
 * 集输气管线附属设施维护记录
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/services/base")
public class WS_FMaint {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 集输气管线附属设施维护记录 列表
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param create_date
	 * @return
	 */
	@RequestMapping(value = "/f_maint/list", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody JsonResWrapper FMaint_list(HttpServletRequest request,@RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String create_date, @RequestParam(required=false) Integer status) {
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
		
		if (!StringUtils.isBlank(create_date)) {
			Date date = DateFormatter.stringToDate(create_date, "yyyy-MM-dd");
			param.put("create_date", date);
		}
		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<FacilitiesMaintenance> fmList = baseService.queryFacilitiesMaintenance(param, ps);
		
		response.setStatus(ResponseStatus.OK);
		response.setData(fmList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 集输气管线附属设施维护记录 详细
	 * @param fm_id
	 * @return
	 */
	@RequestMapping(value="/f_maint/show")
	public @ResponseBody JsonResWrapper FMaintShow(@RequestParam Integer fm_id) {
		JsonResWrapper response = new JsonResWrapper();
		FacilitiesMaintenance fm = baseService.queryFacilitiesMaintenanceById(fm_id);
		response.setStatus(ResponseStatus.OK);
		response.setData(fm);
		return response;
	}
	
	/**
	 * 集输气管线附属设施维护记录 保存
	 * @param pl
	 * @param section
	 * @param spec
	 * @param jinzhan
	 * @param create_date
	 * @param categoryT
	 * @param category
	 * @param lcz_no
	 * @param address
	 * @param jgxs
	 * @param ssxz_desc
	 * @param m_time
	 * @param description
	 * @param recorder
	 * @param no
	 * @param latitude
	 * @param longitude
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/f_maint/save", method=RequestMethod.POST)
	public @ResponseBody JsonResWrapper FmaintSave(@RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String jinzhan, @RequestParam String create_date,@RequestParam String[] categoryT, @RequestParam String [] category, @RequestParam String [] lcz_no,
			@RequestParam String [] address, @RequestParam String [] jgxs, @RequestParam String [] ssxz_desc, @RequestParam String [] m_time, 
			@RequestParam String [] description, @RequestParam String [] recorder, @RequestParam Integer [] no, @RequestParam Double [] latitude, 
			@RequestParam Double [] longitude, @RequestParam(required = false) Integer id, HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		FacilitiesMaintenance fm = new FacilitiesMaintenance();
		fm.setPl_id(pl);
		fm.setPl_section_id(section);
		fm.setPl_spec_id(spec);
		fm.setJinzhan(jinzhan);
		fm.setCreate_date(DateFormatter.stringToDate(create_date, "yyyy-MM-dd"));
		fm.setCreater(ud.getId());
		if(id != null && id.intValue() > 0)
			fm.setId(id);
		
		List<FacilitiesMaintenanceDetail> fmdList = new ArrayList<FacilitiesMaintenanceDetail>();
		int i = 0;
		for (String l : lcz_no) {
			FacilitiesMaintenanceDetail fmd = new FacilitiesMaintenanceDetail();
			if (!StringUtils.isBlank(l)) {
				fmd.setAddress(address[i]);
				fmd.setCategory(categoryT[i] + " : " + category[i]);
				fmd.setDescription(description[i]);
				fmd.setJgxs(jgxs[i]);
				fmd.setLcz_no(lcz_no[i]);
				fmd.setM_time(m_time[i]);
				fmd.setNo(no[i]);
				fmd.setRecorder(recorder[i]);
				fmd.setSsxz_desc(ssxz_desc[i]);
				fmd.setLatitude(latitude[i]);
				fmd.setLongitude(longitude[i]);
				
				fmdList.add(fmd);
			}
			i++;
		}
		
		if (fmdList.size() > 0) {
			baseService.saveFMaint(fm, fmdList);
			response.setMessage("保存成功");
			response.setStatus(ResponseStatus.OK);
		} else {
			response.setMessage("参数不全");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		}

		return response;
	}
	
	/**
	 * 审核结果保存  
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/f_maint/verify_save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper fMaint_verify_save(HttpServletRequest request,
			@RequestParam Integer id, @RequestParam Integer status,	@RequestParam String verify_desc) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		baseService.updateFacilitiesMaintenanceVerifyStatus(id, ud.getId(), status, verify_desc);
		
		response.setMessage("审核成功");
		response.setStatus(ResponseStatus.OK);
		return response;
	}
}
