package cn.hm.oil.webservice;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import cn.hm.oil.domain.Event;
import cn.hm.oil.domain.EventAttachement;
import cn.hm.oil.domain.EventReceiver;
import cn.hm.oil.domain.EventReply;
import cn.hm.oil.domain.EventReplyAttachement;
import cn.hm.oil.domain.EventType;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.EventService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

@Controller
@RequestMapping(value = "/services/event")
public class WS_Event {

	@Autowired
	private EventService eventService;
	
	@Autowired
	private BaseService baseService;
		
	@Autowired
	private UserService userService;
	
	/**
	 * 一事一案列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody JsonResWrapper list(HttpServletRequest request, @RequestParam(required=false) Integer pl,
			@RequestParam(required=false) Integer section, @RequestParam(required=false) Integer spec,
			@RequestParam(required=false) String create_date,@RequestParam(required=false) String key_w,
			@RequestParam(required=false) Integer status) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
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
		if(status != null) {
			param.put("status", status);
		}
		if (!StringUtils.isBlank(create_date)) {
			Date date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = df.parse(create_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			param.put("create_date_s", date);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			param.put("create_date_e", c.getTime());
		}
		if(!StringUtils.isBlank(key_w)){
			param.put("key_w", key_w);
		}
		//待处理中只能显示所有未关闭的一事一案
		param.put("close", 0);
		
		PageSupport ps = PageSupport.initPageSupport(request);
		param.put("user_id", ud.getId());
		
		List<Event> events = eventService.queryEvent(param, ps);
		
		response.setStatus(ResponseStatus.OK);
		response.setData(events);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 获取一事一案类别
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/type", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody JsonResWrapper listType(HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		
		List<EventType> exentList = eventService.queryEventType();
		response.setStatus(ResponseStatus.OK);
		response.setData(exentList);
		return response;
	}
	
	/**
	 * 一事一案保存
	 * @param request
	 * @param model
	 * @param pl
	 * @param section
	 * @param spec
	 * @param discover_date
	 * @param position_start
	 * @param position_end
	 * @param longitude
	 * @param latitude
	 * @param type_id
	 * @param content
	 * @param remark
	 * @param report_object
	 * @param send_notice
	 * @param is_warn
	 * @param id
	 * @param edit
	 * @param keyword
	 * @param fileName
	 * @param notice_file
	 * @param warn_file
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper save(HttpServletRequest request, Model model, @RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String discover_date, @RequestParam String position_start, @RequestParam(required=false) String position_end,
			@RequestParam String longitude, @RequestParam String latitude, @RequestParam Integer type_id,
			@RequestParam String content, @RequestParam String remark, @RequestParam(required=false) String report_object,
			@RequestParam Integer send_notice, @RequestParam Integer is_warn, @RequestParam(required = false) Integer id,
			@RequestParam(required=false) String edit, @RequestParam(required=false) String keyword,
			@RequestParam(required=false) String[] fileName, @RequestParam(required=false) String notice_file
			, @RequestParam(required=false) String warn_file, @RequestParam(required = false) String moon_file, @RequestParam(required=false) String scene_file,
			@RequestParam(required=false) String ef_length,
			@RequestParam(required=false) String pl_no,
			@RequestParam(required=false) String risk,
			@RequestParam(required=false) String link_name,
			@RequestParam(required=false) String link_duty,
			@RequestParam(required=false) String link_method,
			@RequestParam(required=false) String unit_name,
			@RequestParam(required=false) String unit_address,
			@RequestParam(required=false) String unit_post,
			@RequestParam Integer is_ma_remark,
			@RequestParam(required=false) String ma_remark) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		Event event = new Event();
		if (fileName!=null) {
			List<String> fileNames = new ArrayList<String>();

			//String[] names = filename.split(";");
			for (String name : fileName) {
				if (!StringUtils.isBlank(name)) {
					String fn = name;
					fileNames.add(fn);
				}
			}
			event.setFileNames(fileNames);
		}
		
		if(send_notice.intValue() == 1) {
			if(!StringUtils.isBlank(notice_file)){
				event.setNotice_file(notice_file);
				event.setSend_notice(true);
			} else {
				event.setSend_notice(false);
			}
		} else {
			event.setSend_notice(false);
		}
		
		if(is_warn.intValue() == 1) {
			if(!StringUtils.isBlank(warn_file)){
				event.setWarn_file(warn_file);
				event.setIs_warn(true);
			} else {
				event.setIs_warn(false);
			}
		} else {
			event.setIs_warn(false);
		}
		
		if(is_ma_remark.intValue() == 1) {
			if(!StringUtils.isBlank(ma_remark)){
				event.setMa_remark(ma_remark);
				event.setIs_ma_remark(true);
			} else {
				event.setIs_ma_remark(false);
			}
		} else {
			event.setIs_ma_remark(false);
		}
		
		if(!StringUtils.isBlank(scene_file)) {
			event.setScene_file(scene_file);
		} else {
			event.setScene_file(null);
		}
		if(!StringUtils.isBlank(moon_file)) {
			event.setMoon_file(moon_file);
		} else {
			event.setMoon_file(null);
		}
		event.setContent(content);
		event.setCreated_by(ud.getId());
		event.setDiscover_date(DateFormatter.stringToDate(discover_date));
		event.setLongitude(longitude);
		event.setLatitude(latitude);
		event.setId(id);
		//event.setIs_warn(is_warn.intValue() == 1);
		event.setPl_id(pl);
		event.setPl_section_id(section);
		event.setPl_spec_id(spec);
		event.setPosition_end(position_end);
		event.setPosition_start(position_start);
		event.setRemark(remark);
		event.setReport_object(report_object);
		event.setType_id(type_id);
		event.setStatus(0);
		event.setKeyword(keyword);
		event.setEf_length(ef_length);
		event.setPl_no(pl_no);
		event.setRisk(risk);
		event.setLink_name(link_name);
		event.setLink_duty(link_duty);
		event.setLink_method(link_method);
		event.setUnit_name(unit_name);
		event.setUnit_address(unit_address);
		event.setUnit_post(unit_post);
		//event.setSend_notice(send_notice.intValue() == 1);
		eventService.saveEvent(event);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("保存成功");
		return response;
	}
	
	/**
	 * 查找列表
	 * @param request
	 * @param model
	 * @param pl_id
	 * @param section_id
	 * @return
	 */
	@RequestMapping(value = "/findWorker", method = { RequestMethod.POST, RequestMethod.GET })
	public  @ResponseBody JsonResWrapper findWorker(HttpServletRequest request, Model model, @RequestParam(required=false) Integer pl_id, 
			@RequestParam(required=false) Integer section_id) {
		JsonResWrapper response = new JsonResWrapper();
		if (pl_id != null && pl_id.intValue() <= 0)
			pl_id = null;
		if (section_id != null && section_id.intValue() <= 0)
			section_id = null;
		List<EventReceiver> receivers = eventService.queryWorkerList(pl_id, section_id);
		response.setStatus(ResponseStatus.OK);
		response.setData(receivers);
		return response;
	}
	
	/**
	 * 查看详情页面重新保存指派人员
	 */
	@RequestMapping(value = "/saveWorker", method = RequestMethod.POST)
	public  @ResponseBody JsonResWrapper saveWorker(Model model, @RequestParam Integer id, @RequestParam(required=false) String[] users, 
			@RequestParam(required=false) Integer up_to_level, @RequestParam(required=false) String message,
			@RequestParam(required=false) Integer close) {
		JsonResWrapper response = new JsonResWrapper();
		/*if (close != null && close.intValue() == 1) {
			eventService.updateEventClose(id, message);
			return "redirect:/admin/event/detail_show?success=1&id=" + id;
		} else {*/
			if (users!=null) {
				List<Integer> receivers = new ArrayList<Integer>();
				//String [] userArr = users.split(",");
				for (String ids : users) {
					if (!StringUtils.isBlank(ids))
						receivers.add(Integer.valueOf(ids));
				}
				eventService.updateEventStatus(null, null, id, receivers, null, up_to_level, message,null,0);
			}
			response.setStatus(ResponseStatus.OK);
			response.setMessage("指派成功");
			return response;
		//}
	}
	
	/**
	 * 回复一事一案
	 * @param model
	 * @param request
	 * @param msg_content
	 * @param id
	 * @param fileName
	 * @return
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper reply(Model model, HttpServletRequest request,  @RequestParam String msg_content, @RequestParam Integer id,
			@RequestParam(required=false) String[] fileName) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		EventReply nr = new EventReply();
		nr.setContent(msg_content);
		nr.setEvent_id(id);
		nr.setUser_id(ud.getId());
		try {
			if (fileName!=null) {
				List<String> fileNames = new ArrayList<String>();

				//String[] names = filename.split(";");
				for (String name : fileName) {
					if (!StringUtils.isBlank(name)) {
						fileNames.add(name);
					}
				}
				nr.setFileNames(fileNames);
			}
			/*List<String> paths = DataUtil.uploadFile(request, "file");
			if (!CollectionUtils.isEmpty(paths)) {
				nr.setPath(paths.get(0));
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		eventService.insertEventReply(nr);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("回复成功");
		return response;
	}
	
	/**
	 * 一事一案详情
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public @ResponseBody JsonResWrapper detail(Model model,HttpServletRequest request, @RequestParam Integer id, @RequestParam(required=false) Integer success,
			@RequestParam(required=false) Integer tips_id) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		
		if (tips_id != null && tips_id.intValue() > 0)
			eventService.deleteTips(tips_id);
		
		Event e = eventService.queryEventById(id);
		/*if (tips_id != null && tips_id.intValue() > 0)
			eventService.deleteTips(tips_id);*/
		String base = SettingUtils.getCommonSetting("base.file.url");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("pipeLineList", baseService.queryPipeLine(new HashMap<String,Object>()));
		param.put("sectionList", baseService.querySection(e.getPl_id()));
		param.put("specList", baseService.querySpec(e.getPl_section_id()));
		param.put("types", eventService.queryEventType());
		if(!StringUtils.isBlank(e.getWarn_file())){
			e.setWarn_file(base+e.getWarn_file());
		}
		if(!StringUtils.isBlank(e.getNotice_file())){
			e.setNotice_file(base+e.getNotice_file());
		}
		param.put("e", e);
		/*model.addAttribute("pipeLineList", baseService.queryPipeLine());
		model.addAttribute("sectionList", baseService.querySection(e.getPl_id()));
		model.addAttribute("specList", baseService.querySpec(e.getPl_section_id()));
		model.addAttribute("types", eventService.queryEventType());
		model.addAttribute("e", e);*/
		
		List<BasePipeline> basePipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		param.put("basePipeLineList", basePipeLineList);
		
		if (ud.getLevel().intValue() == 4) {//普通干部， 4级
			List<EventReceiver> allReceivers = eventService.queryWorkerList(null, null);
			param.put("allReceivers", allReceivers);
			
			List<EventReceiver> receivers = eventService.queryEventReceiver(id);
			param.put("receivers", receivers);
		} else {
			List<EventReceiver> leaders = eventService.queryLeaderList(ud.getLevel() + 1);
			param.put("leaders", leaders);
		}
		
		List<EventReply> replies = eventService.queryEventReply(id);
		List<EventReply> leaderPHReplies = new ArrayList<EventReply>();
		for (EventReply er : replies) {
			if (er.getLeader() != null && er.getLeader().intValue() == 1) {
				leaderPHReplies.add(er);
			} else{
				if(!CollectionUtils.isEmpty(er.getEras())){
					List<String> fns = new ArrayList<String>();
					for(EventReplyAttachement ef:er.getEras()){
						if(!StringUtils.isBlank(ef.getPath())){
							fns.add(base+ef.getPath());
						}
					}
					er.setFileNames(fns);
				}
			}
		}
		replies.removeAll(leaderPHReplies);
		param.put("leaderPHReplies", leaderPHReplies);
		param.put("replies", replies);
		/*if (success != null) {
			model.addAttribute("msg", "操作成功！");
		}*/
		
		List<EventAttachement> ealist = eventService.queryEventAttachementByEventId(e.getId());
		for(EventAttachement ea:ealist){
			if(ea!=null){
			 if(!StringUtils.isBlank(ea.getPath())){
				 ea.setPath(base+ea.getPath());
			 }
			}
		}
		param.put("ealist", ealist);
		
		response.setStatus(ResponseStatus.OK);
		response.setData(param);
		return response;
	}
	
}
