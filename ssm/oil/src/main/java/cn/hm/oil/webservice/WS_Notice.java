package cn.hm.oil.webservice;

import java.util.ArrayList;
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

import cn.hm.oil.domain.Notice;
import cn.hm.oil.domain.NoticeRead;
import cn.hm.oil.domain.NoticeReceiver;
import cn.hm.oil.domain.NoticeReply;
import cn.hm.oil.domain.Readed;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.NoticeService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.PageSupport;

@Controller
@RequestMapping(value = "/services")
public class WS_Notice {

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 临时通知查看
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_list", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody JsonResWrapper tmpNoticeList(HttpServletRequest request,@RequestParam(required = false) String search, 
			@RequestParam(required = false) String key_w, @RequestParam(required = false) Integer uid, @RequestParam(required = false) Integer close,
			@RequestParam(required = false) String f_time,
			@RequestParam(required = false) String t_time) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		Integer roleId = userService.queryRoleIdByUserId(ud.getId());
		
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", Notice.TYPE_TEMP);
		param.put("user_id", ud.getId());
		param.put("roleId", roleId);

		if(!StringUtils.isBlank(key_w)){
			param.put("key_w", key_w);
		}
		if (uid != null && uid.intValue() > 0){
			param.put("uid", uid);
		}
		if (close != null){
			param.put("status", close);
		}
		if(!StringUtils.isBlank(f_time)){
			param.put("f_time", f_time);
			param.put("t_time", t_time);
		}
		List<Notice> notices = noticeService.queryNotice(param, ps);
		response.setStatus(ResponseStatus.OK);
		response.setData(notices);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 临时通知详情
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_detail", method = RequestMethod.GET)
	public @ResponseBody JsonResWrapper tmpNoticeCreate(Model model, @RequestParam(required = false) Integer add, 
			@RequestParam(required = false) Integer id, @RequestParam(required = false) Integer tips_id) {
		JsonResWrapper response = new JsonResWrapper();
			if (tips_id != null && tips_id.intValue() > 0)
				noticeService.deleteTips(tips_id);
			
			Notice notice = noticeService.queryTempNoticeDetailById(id);
			List<NoticeReceiver> receivers = noticeService.queryNoticeReceiver(notice.getId());
			List<NoticeReply> replies = noticeService.queryNoticeReplyByNoticeId(id);
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("receivers", receivers);
			param.put("replies", replies);
			param.put("notice", notice);

			response.setStatus(ResponseStatus.OK);
			response.setData(param);
			return response;
		}
	
	/**
	 * 保存临时通知
	 * 
	 * @param model
	 * @return
	 */
/*	@RequestMapping(value = "/tmp_notice_save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper tmpNoticeSave(Model model, HttpServletRequest request, @RequestParam String title, @RequestParam String content, 
			@RequestParam String users, @RequestParam String postil, @RequestParam Integer send,@RequestParam(required=false) String fileName) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setType(Notice.TYPE_TEMP);
		notice.setPostil(postil);
		notice.setSend(send);
		notice.setUser_id(ud.getId());
		try {
			if(!StringUtils.isBlank(fileName)){
				notice.setPath(fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Integer> receivers = new ArrayList<Integer>();
		String [] userArr = users.split(",");
		for (String ids : userArr) {
			receivers.add(Integer.valueOf(ids));
		}
		
		noticeService.saveTempNotice(notice, receivers);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("保存成功");
		return response;
	}
	*/
	/**
	 * 临时通知状态更新
	 * 
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value = "/tmp_notice_update_status", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody JsonResWrapper tmpNoticeUpdateStatus(HttpServletRequest request, Model model, @RequestParam Integer id, 
			@RequestParam Integer status) {
		JsonResWrapper response = new JsonResWrapper();
		noticeService.updateNoticeStatus(id, status);
		
		response.setStatus(ResponseStatus.OK);
		response.setMessage("保存成功");
		return response;
	}
	*/
	/**
	 * 临时通知恢复
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_reply", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper tmpNoticeReply(Model model, HttpServletRequest request,  @RequestParam String msg_content, @RequestParam Integer id
			,@RequestParam(required=false) String fileName) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		NoticeReply nr = new NoticeReply();
		nr.setContent(msg_content);
		nr.setNotice_id(id);
		nr.setUser_id(ud.getId());
		try {
			if(!StringUtils.isBlank(fileName)){
				nr.setPath(fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(ResponseStatus.FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
		noticeService.insertNoticeReply(nr);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("回复成功");
		return response;
	}
	
	
	/**
	 * 公共通知查看
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_list", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody JsonResWrapper noticeList(HttpServletRequest request, @RequestParam(required = false) Integer del,
			 @RequestParam(required=false) String s_title,@RequestParam(required=false) String f_time,@RequestParam(required=false) String t_time) {
		JsonResWrapper response = new JsonResWrapper();
		
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", Notice.TYPE_PUBLIC);
		if(!StringUtils.isBlank(s_title)){
			param.put("s_title", s_title);
		}
		if(!StringUtils.isBlank(f_time)){
			param.put("f_time", f_time);
			param.put("t_time", t_time);
		}

		List<Notice> notices = noticeService.queryNotice(param, ps);
		
		response.setStatus(ResponseStatus.OK);
		response.setData(notices);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 公共通知详情
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_detail", method = RequestMethod.GET)
	public @ResponseBody JsonResWrapper noticeCreate(Model model,HttpServletRequest request, @RequestParam(required = false) Integer add, 
			@RequestParam(required = false) Integer id, @RequestParam(required=false) Integer tips_id) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
			if (tips_id != null && tips_id.intValue() > 0)
				noticeService.deleteTips(tips_id);
			
			Notice notice = noticeService.queryPublicNoticeDetailById(id);
			//System.out.println(notice.getId());
			notice.setId(id);
			
			NoticeRead noticeread = noticeService.queryNoticeRead(id, ud.getId());
			List<NoticeReply> replies = noticeService.queryNoticeReplyByNoticeId(id);
			if(noticeread == null){
				noticeread = new NoticeRead();
				noticeread.setNotice_id(id);
				noticeread.setUser_id(ud.getId());
				noticeService.insertNoticeRead(noticeread);
			}	
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("replies", replies);
			param.put("notice", notice);
			response.setStatus(ResponseStatus.OK);
			response.setData(param);
			return response;
		}
	
	/**
	 * 保存公共通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper noticeSave(Model model, HttpServletRequest request, @RequestParam String title, @RequestParam String content,
			@RequestParam(required=false) String fileName) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setType(Notice.TYPE_PUBLIC);
		notice.setUser_id(ud.getId());
		try {
			if(!StringUtils.isBlank(fileName)){
				notice.setPath(fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		noticeService.insertPublicNotice(notice);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("保存成功");
		return response;
	}
	
	/**
	 * 公共通知回复
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_reply", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper noticeReply(Model model, HttpServletRequest request,  @RequestParam String msg_content, @RequestParam Integer id,
			@RequestParam(required=false) String fileName) {
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		NoticeReply nr = new NoticeReply();
		nr.setContent(msg_content);
		nr.setNotice_id(id);
		nr.setUser_id(ud.getId());
		try {
			if(!StringUtils.isBlank(fileName)){
				nr.setPath(fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		noticeService.insertNoticeReply(nr);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("回复成功");
		return response;
	}
	
	
	/**
	 * 临时性请示查看
	 * @param request
	 * @param del
	 * @param verify
	 * @return
	 */
	@RequestMapping(value="/ask_notice_list")
	public @ResponseBody JsonResWrapper askNoticeList(HttpServletRequest request, @RequestParam(required = false) Integer verify,
			@RequestParam(required = false) String key_w, 
			@RequestParam(required = false) Integer uid, 
			@RequestParam(required = false) Integer verify_status,
			@RequestParam(required = false) String f_time,
			@RequestParam(required = false) String t_time) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		Integer roleId = userService.queryRoleIdByUserId(ud.getId());
		
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", Notice.TYPE_ASK);
		param.put("user_id", ud.getId());
		if(roleId == 2) {
			param.put("roleId", roleId);
			/*if(verify != null && verify==1 && roleId == 2) {
				model.addAttribute("verify", 1);
			}*/
		}
		if(!StringUtils.isBlank(key_w)){
			param.put("key_w", key_w);
		}
		if (uid != null && uid.intValue() > 0){
			param.put("uid", uid);
		}
		if (verify_status != null){
			param.put("verify_status", verify_status);
		}
		if(!StringUtils.isBlank(f_time)){
			param.put("f_time", f_time);
			param.put("t_time", t_time);
		}
		//
		List<Notice> notices = noticeService.queryNotice(param, ps);
		
		response.setStatus(ResponseStatus.OK);
		response.setData(notices);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 临时性请示详情
	 * @param model
	 * @param add
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/ask_notice_detail", method = RequestMethod.GET)
	public  @ResponseBody JsonResWrapper askNoticeCreate(Model model, @RequestParam(required = false) Integer add, @RequestParam(required = false) Integer id ,@RequestParam(required = false) Integer tips_id) {
			JsonResWrapper response = new JsonResWrapper();
			if (tips_id != null && tips_id.intValue() > 0)
				noticeService.deleteTips(tips_id);
			
			Notice notice = noticeService.queryTempNoticeDetailById(id);
			List<NoticeReceiver> receivers = noticeService.queryNoticeReceiver(notice.getId());
			List<NoticeReply> replies = noticeService.queryNoticeReplyByNoticeId(id);
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("receivers", receivers);
			param.put("replies", replies);
			param.put("notice", notice);
			response.setStatus(ResponseStatus.OK);
			response.setData(notice);
			return response;
		}
	
	/**
	 * 临时性工作请示保存
	 * @return
	 */
	@RequestMapping(value = "/ask_notice_save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper askNoticeSave(Model model,HttpServletRequest request, @RequestParam String title, @RequestParam String content, @RequestParam String ask_type,
			@RequestParam Integer send, @RequestParam String users, @RequestParam(required=false) String fileName){
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		JsonResWrapper response = new JsonResWrapper();
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		Notice notice = new Notice();
		notice.setType(Notice.TYPE_ASK);
		notice.setTitle(title);
		notice.setSend(send);
		notice.setTitle(title);
		notice.setContent(content);
		notice.setUser_id(ud.getId());
		notice.setAsk_type(ask_type);
		try {
			if(!StringUtils.isBlank(fileName)){
				//String fn = DataUtil.moveToFileDir(fileName);
				notice.setPath(fileName);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Integer> receivers = new ArrayList<Integer>();
		String [] userArr = users.split(",");
		for (String ids : userArr) {
			receivers.add(Integer.valueOf(ids));
		}
		
		noticeService.saveAskNotice(notice, receivers);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("保存成功");
		return response;
	}
	
	@RequestMapping(value = "/findWorker", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody JsonResWrapper findWorker(HttpServletRequest request, Model model, @RequestParam(required=false) Integer pl_id, 
			@RequestParam(required=false) Integer section_id) {
		JsonResWrapper response = new JsonResWrapper();
		if (pl_id != null && pl_id.intValue() <= 0)
			pl_id = null;
		if (section_id != null && section_id.intValue() <= 0)
			section_id = null;
		List<NoticeReceiver> receivers = noticeService.queryWorkerList(pl_id, section_id);
		response.setStatus(ResponseStatus.OK);
		response.setData(receivers);
		return response;
	}
	
	@RequestMapping(value = "/findLeader", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody JsonResWrapper findLeader(HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		List<NoticeReceiver> leaders = noticeService.queryLeaderList();
		response.setStatus(ResponseStatus.OK);
		response.setData(leaders);
		return response;
	}
	
	@RequestMapping(value="/notice_read")
	public @ResponseBody JsonResWrapper noticeRead(HttpServletRequest request, Model model) {	
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		JsonResWrapper response = new JsonResWrapper();
		List<Readed> readeds = noticeService.queryNoticeReadList(param, ps);
		
		response.setStatus(ResponseStatus.OK);
		response.setData(readeds);
		return response;
	}
	
	@RequestMapping(value = "/getAuthors", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody JsonResWrapper getAuthors(HttpServletRequest request, Model model, 
			@RequestParam Integer type) {
		JsonResWrapper response = new JsonResWrapper();
		
		List<SysUsers> su = noticeService.queryNoticeAuthors(type);
		response.setStatus(ResponseStatus.OK);
		response.setData(su);
		return response;
	}
	
}
