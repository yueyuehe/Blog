package cn.hm.oil.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
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
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.NewBaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

@Controller
@RequestMapping(value = "/services/base_new/pl_patrol")
public class WS_PlPatrol_New {
	@Autowired
	private NewBaseService newBaseService;
	
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
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody JsonResWrapper plPatrol_list(HttpServletRequest request,Authentication authentication, @RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) Integer del) {
		JsonResWrapper response = new JsonResWrapper();
		List<BasePipeline> pipeLineList = newBaseService.queryPipeLine();
		
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
		PageSupport ps = PageSupport.initPageSupport(request);
		List<PipelinePatrol> ppList = newBaseService.queryPipelinePatrol(param, ps);
		
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
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper plPatrol_save(HttpServletRequest request,@RequestParam(required = false) Integer id,@RequestParam(required=false) Integer[] pld_id, @RequestParam(required=false) String patroler,
			@RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,@RequestParam(required=false) Integer[] has_annex_id, @RequestParam(required=false) String[] has_annex,
			@RequestParam(required = false) String jinzhan, @RequestParam(required = false) String p_month, @RequestParam(required=false) String patrol_no,
			@RequestParam(required = false) String[] p_date, @RequestParam(required = false) String[] work_place, @RequestParam(required = false) String[] content,
			@RequestParam(required = false) String[] question, @RequestParam(required = false) String[] voice_record, @RequestParam(required = false) String[] worker,
			@RequestParam(required = false) String[] auditor, @RequestParam(required = false) String save_jinzhan, @RequestParam String[] content_1,@RequestParam String[] content_2,
			@RequestParam String[] content_3,@RequestParam String[] content_4,@RequestParam String[] content_5,@RequestParam String[] content_6,@RequestParam String[] content_7,@RequestParam String[] content_8,
			@RequestParam String[] content_9,@RequestParam String[] content_10,@RequestParam String[] content_11,@RequestParam String[] content_12,@RequestParam String[] content_13,@RequestParam String[] content_14,
			@RequestParam String[] content_15,@RequestParam String up_id,@RequestParam String[] content_16,@RequestParam String[] remark){
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		
		PipelinePatrol pp = new PipelinePatrol();
		pp.setJinzhan(jinzhan);
		pp.setPl_id(pl);
		pp.setPl_section_id(section);
		pp.setPl_spec_id(spec);
		pp.setSave_jinzhan(save_jinzhan);
		pp.setCreater(ud.getId());
		pp.setPatroler(patroler);
		pp.setId(id);
		pp.setPatrol_no(patrol_no);
		pp.setUp_id(up_id);
		
		List<PipelinePatrolDetail> ppdList = new ArrayList<PipelinePatrolDetail>();
		int i = 0;
		for (String r : p_date) {
			if(!StringUtils.isBlank(r)){
				PipelinePatrolDetail ppd = new PipelinePatrolDetail();
				Calendar ca = Calendar.getInstance();
				ca.setTime(new Date());
				ppd.setP_date(DateFormatter.stringToDate(ca.get(Calendar.YEAR)+"-"+r, "yyyy-MM-dd"));
				ppd.setRemark(remark[i]);
				ppd.setContent_1(content_1[i]);
				ppd.setContent_2(content_2[i]);
				ppd.setContent_3(content_3[i]);
				ppd.setContent_4(content_4[i]);
				ppd.setContent_5(content_5[i]);
				ppd.setContent_6(content_6[i]);
				ppd.setContent_7(content_7[i]);
				ppd.setContent_8(content_8[i]);
				ppd.setContent_9(content_9[i]);
				ppd.setContent_10(content_10[i]);
				ppd.setContent_11(content_11[i]);
				ppd.setContent_12(content_12[i]);
				ppd.setContent_13(content_13[i]);
				ppd.setContent_14(content_14[i]);
				ppd.setContent_15(content_15[i]);
				ppd.setContent_16(content_16[i]);
				
				ppdList.add(ppd);
			}
			i++;
		}
		if (ppdList.size() > 0) {
			newBaseService.savePlPatrol(pp, ppdList);
			response.setMessage("保存成功");
			response.setStatus(ResponseStatus.OK);
		} else {
			response.setMessage("数据不全");
			response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
		}
		return response;
	}
	
}
