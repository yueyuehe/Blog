package cn.hm.oil.webservice;

import java.io.IOException;
import java.util.ArrayList;
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
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PipelinePatrolDetail;
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
public class WS_PlPatrol {
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 管道巡检工作记录查询
	 * 
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param p_month
	 * @param del
	 * @return
	 */
	@RequestMapping(value = "/pl_patrol/list", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody JsonResWrapper plPatrol_list(HttpServletRequest request, @RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String p_month, @RequestParam(required=false) Integer status) {
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
		
		if (!StringUtils.isBlank(p_month)) {
			param.put("p_month", Integer.valueOf(p_month.replace("-", "")));
		}
		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		
		//SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		//Integer role = userService.queryRoleIdByUserId(ud.getId());
		
		List<PipelinePatrol> ppList = baseService.queryPipelinePatrol(param, ps);
		
		response.setStatus(ResponseStatus.OK);
		response.setData(ppList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 管道巡检工作记录保存
	 * 
	 */
	@RequestMapping(value = "/pl_patrol/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper plPatrol_save(Model model, HttpServletRequest request,@RequestParam(required = false) Integer id,@RequestParam(required=false) Integer[] pld_id,
			@RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,@RequestParam(required=false) Integer[] has_annex_id, @RequestParam(required=false) String[] has_annex,
			@RequestParam String jinzhan, @RequestParam String p_month, @RequestParam String[] p_date, @RequestParam String[] work_place, @RequestParam String[] content,
			@RequestParam String[] question, @RequestParam String[] voice_record, @RequestParam String[] worker,
			@RequestParam String[] auditor, @RequestParam String[] fileName,@RequestParam String save_jinzhan){
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		PipelinePatrol pp = new PipelinePatrol();
		pp.setJinzhan(jinzhan);
		pp.setP_month(Integer.valueOf(p_month.replace("-", "")));
		pp.setPl_id(pl);
		pp.setPl_section_id(section);
		pp.setPl_spec_id(spec);
		pp.setSave_jinzhan(save_jinzhan);
		pp.setCreater(ud.getId());
		pp.setId(id);
		List<String> imgs = new ArrayList<String>();
		//try {
		for (String name : fileName) {
			imgs.add(name);
		}
		/*} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setMessage("图片移动失败");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
			return response;
		}*/
		
		Map<Integer, String> annexMap = new HashMap<Integer, String>();
		if(has_annex_id != null && has_annex_id.length > 0) {
			int i = 0;
			for(Integer in : has_annex_id) {
				if(in.intValue() > 0) {
					annexMap.put(in, has_annex[i]);
				}
				i++;
			}
		}
		
		List<PipelinePatrolDetail> ppdList = new ArrayList<PipelinePatrolDetail>();
		int i = 0;
		for (String w : work_place) {
			PipelinePatrolDetail ppd = new PipelinePatrolDetail();
			if(!StringUtils.isBlank(w)){
				ppd.setWork_place(w);
				ppd.setAuditor(auditor[i]);
				ppd.setContent(content[i]);
				ppd.setP_date(DateFormatter.stringToDate(p_date[i], "yyyy-MM-dd"));
				ppd.setQuestion(question[i]);
				ppd.setWorker(worker[i]);
				ppd.setVoice_record(voice_record[i]);
				if(pld_id != null) {
					ppd.setAnnex(annexMap.get(pld_id[i]));
				}
				if(!StringUtils.isBlank(imgs.get(i))) {
					ppd.setAnnex(imgs.get(i));
				}
				ppdList.add(ppd);
			}
			i++;
		}
		if (ppdList.size() > 0) {
			baseService.savePlPatrol(pp, ppdList);
			response.setMessage("保存成功");
			response.setStatus(ResponseStatus.OK);
		} else {
			response.setMessage("数据不全");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		}

		return response;
	}
	
}
