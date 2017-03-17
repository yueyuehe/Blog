package cn.hm.oil.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import cn.hm.oil.domain.EventLevel;
import cn.hm.oil.domain.Notice;
import cn.hm.oil.domain.NoticeAttachement;
import cn.hm.oil.domain.NoticeDelay;
import cn.hm.oil.domain.NoticeRead;
import cn.hm.oil.domain.NoticeReceiver;
import cn.hm.oil.domain.NoticeRecord;
import cn.hm.oil.domain.NoticeReply;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.Readed;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.EventService;
import cn.hm.oil.service.NoticeService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.FileUtils;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

@Controller
@RequestMapping(value = "/admin")
public class NoticeController {

	@Autowired
	private EventService eventService;
	
	@Autowired
	private NoticeService noticeService;

	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;

	/**
	 * 创建公共通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_create", method = RequestMethod.GET)
	public String noticeCreate(Model model,
			@RequestParam(required = false) Integer add,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer tips_id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (id != null && id.intValue() > 0) {
			if (tips_id != null && tips_id.intValue() > 0)
				noticeService.deleteTips(tips_id);

			Notice notice = noticeService.queryPublicNoticeDetailById(id);
			// System.out.println(notice.getId());
			if (notice != null) {
				notice.setId(id);
				model.addAttribute("notice", notice);
			} else {
				Notice n = new Notice();
				n.setContent("该通知已被删除");
				model.addAttribute("notice", n);

			}
			NoticeRead noticeread = noticeService.queryNoticeRead(id,
					ud.getId());
			List<NoticeReply> replies = noticeService
					.queryNoticeReplyByNoticeId(id);
			model.addAttribute("replies", replies);
			if (noticeread == null) {
				noticeread = new NoticeRead();
				noticeread.setNotice_id(id);
				noticeread.setUser_id(ud.getId());
				noticeService.insertNoticeRead(noticeread);
			}

			return "pages/notice/detail";
		}
		Prompt prompt = baseService.quertPromptByType(14);
		if (prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		if (add != null && add.intValue() == -1) {
			model.addAttribute("msg", "保存失败！");
		} else if (add != null && add.intValue() == 1) {
			model.addAttribute("msg", "保存成功！");
		}
		return "pages/notice/create";
	}

	/**
	 * 编辑公共通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_edit", method = RequestMethod.GET)
	public String noticeEdit(Model model,
			@RequestParam(required = false) Integer add,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer tips_id,
			@RequestParam(required = false) Integer del) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Notice notice = noticeService.queryPublicNoticeDetailById(id);
		// System.out.println(notice.getId());
		if (notice != null) {
			notice.setId(id);
			model.addAttribute("notice", notice);
		} else {
			Notice n = new Notice();
			n.setContent("该通知已被删除");
			model.addAttribute("notice", n);

		}
		NoticeRead noticeread = noticeService.queryNoticeRead(id, ud.getId());
		List<NoticeReply> replies = noticeService
				.queryNoticeReplyByNoticeId(id);
		model.addAttribute("replies", replies);
		if (noticeread == null) {
			noticeread = new NoticeRead();
			noticeread.setNotice_id(id);
			noticeread.setUser_id(ud.getId());
			noticeService.insertNoticeRead(noticeread);
		}
		if (add != null) {
			model.addAttribute("msg", "修改成功!");
		}
		if (del != null) {
			model.addAttribute("msg", "删除成功！");
		}
		return "pages/notice/edit";
	}

	/**
	 * 保存公共通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_save", method = RequestMethod.POST)
	public String noticeSave(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer id,
			@RequestParam String title, @RequestParam String content) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (id != null && id.intValue() > 0) {
			Notice notice = new Notice();
			notice.setId(id);
			notice.setTitle(title);
			notice.setContent(content);
			try {
				List<String> paths = DataUtil.uploadFile(request, "file");
				if (!CollectionUtils.isEmpty(paths)) {
					notice.setPath(paths.get(0));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			noticeService.insertPublicNotice(notice);
			return "redirect:/admin/notice_edit?add=1&id=" + id;
		} else {
			Notice notice = new Notice();
			notice.setTitle(title);
			notice.setContent(content);
			notice.setType(Notice.TYPE_PUBLIC);
			notice.setUser_id(ud.getId());
			try {
				List<String> paths = DataUtil.uploadFile(request, "file");
				if (!CollectionUtils.isEmpty(paths)) {
					notice.setPath(paths.get(0));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			noticeService.insertPublicNotice(notice);
			return "redirect:/admin/notice_create?add=1";
		}
	}

	/**
	 * 公共通知查看
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_list", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String noticeList(HttpServletRequest request, Model model,
			@RequestParam(required = false) Integer del,
			@RequestParam(required = false) String s_title,
			@RequestParam(required = false) String f_time,
			@RequestParam(required = false) String t_time) {

		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", Notice.TYPE_PUBLIC);
		if (!StringUtils.isBlank(s_title)) {
			param.put("s_title", s_title);
			model.addAttribute("s_title", s_title);
		}
		if (!StringUtils.isBlank(f_time)) {
			param.put("f_time", f_time);
			param.put("t_time", t_time);
			model.addAttribute("f_time", f_time);
			model.addAttribute("t_time", t_time);
		}
		List<Notice> notices = noticeService.queryNotice(param, ps);

		model.addAttribute("notices", notices);
		if (del != null) {
			model.addAttribute("msg", "删除成功！");
		}
		return "pages/notice/list";
	}

	/**
	 * 删除通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_del", method = RequestMethod.GET)
	public String noticeDel(Model model, HttpServletRequest request,
			@RequestParam Integer id) {
		// 把要删除的文件放入delete文件夹中
		try {
			List<NoticeReply> np = noticeService.queryNoticeReplyByNoticeId(id);
			if (!CollectionUtils.isEmpty(np)) {
				for (NoticeReply n : np) {
					if (!StringUtils.isBlank(n.getPath())) {
						DataUtil.moveFileToDeleteFileDir(n.getPath());
					}
				}
			}
			Notice nt = noticeService.queryPublicNoticeDetailById(id);
			if (!StringUtils.isBlank(nt.getPath())) {
				DataUtil.moveFileToDeleteFileDir(nt.getPath());
			}
			noticeService.updateNoticeClose(id, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/admin/notice_list?del=1";
	}

	/**
	 * 超管删除通知和学习回复消息及附件
	 * 
	 * @param model
	 * @return
	 */
	/*
	 * @RequestMapping(value = "/notice_reply_del", method = RequestMethod.GET)
	 * public @ResponseBody String notice_reply_del(Model model,
	 * HttpServletRequest request, @RequestParam Integer id) { NoticeReply n =
	 * noticeService.queryNoticeReplyById(id); if (n != null) { if
	 * (!StringUtils.isBlank(n.getPath())) { String sep =
	 * System.getProperty("file.separator"); String fileDir =
	 * SettingUtils.getCommonSetting("upload.file.path");
	 * 
	 * DataUtil.deleteFile(fileDir + sep + n.getPath()); }
	 * noticeService.deleteNoticeReplyById(id); } return JsonUtil.toJson("ok");
	 * }
	 */

	/**
	 * 保存临时通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_reply", method = RequestMethod.POST)
	public String noticeReply(Model model, HttpServletRequest request,
			@RequestParam String msg_content, @RequestParam Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		NoticeReply nr = new NoticeReply();
		nr.setContent(msg_content);
		nr.setNotice_id(id);
		nr.setUser_id(ud.getId());
		try {
			List<String> paths = DataUtil.uploadFile(request, "file");
			if (!CollectionUtils.isEmpty(paths)) {
				nr.setPath(paths.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		noticeService.insertNoticeReply(nr);
		return "redirect:/admin/notice_create?id=" + id;
	}

	/**
	 * 创建临时通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_create", method = {RequestMethod.GET,RequestMethod.POST})
	public String tmpNoticeCreate(Model model,HttpServletRequest request,
			@RequestParam(required = false) Integer add,
			@RequestParam(required = false) Integer addre,
			@RequestParam(required = false) Integer delay,
			@RequestParam(required = false) Integer verify,
			@RequestParam(required = false) Integer askclose,
			@RequestParam(required = false) Integer changeover,
			@RequestParam(required = false) Integer changefuze,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer tips_id,
			@RequestParam(required = false) Integer see_record) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (id != null && id.intValue() > 0) {
			if (tips_id != null && tips_id.intValue() > 0)
				noticeService.deleteTips(tips_id);

			Notice notice = noticeService.queryTempNoticeDetailById(id);
			List<NoticeReceiver> receivers = noticeService
					.queryNoticeReceiver(notice.getId());
			// 移出李根、黄健、李相蓉
			List<NoticeReceiver> nreceivers = new ArrayList<NoticeReceiver>();
			for (NoticeReceiver nr : receivers) {
				if (nr.getUser_id().intValue() == 2
						|| nr.getUser_id().intValue() == 3) {
					continue;
				}
				nreceivers.add(nr);
			}
			List<NoticeReceiver> allReceivers = noticeService.queryWorkerList(
					null, null);
			List<NoticeReceiver> aallReceivers = new ArrayList<NoticeReceiver>();
			aallReceivers.addAll(allReceivers);
			model.addAttribute("aallReceivers", aallReceivers);
			
			allReceivers.removeAll(nreceivers);
			List<NoticeReceiver> leaders = noticeService.queryLeaderList();
			
			
			// 移出李根、黄健、李相蓉
			List<NoticeReceiver> nleaders = new ArrayList<NoticeReceiver>();
			for (NoticeReceiver nr : leaders) {
				if (nr.getUser_id().intValue() == 2
						|| nr.getUser_id().intValue() == 3) {
					continue;
				}
				nleaders.add(nr);
			}
			List<NoticeReceiver> anleaders = new ArrayList<NoticeReceiver>();
			anleaders.addAll(nleaders);
			model.addAttribute("aleaders", anleaders);
			nleaders.removeAll(nreceivers);
			List<NoticeReply> replies = noticeService
					.queryNoticeReplyByNoticeId(id);
			List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("n_id", id);
			List<NoticeDelay> delays = noticeService
					.queryAllTmpNoticeDelay(param);

			List<EventLevel> els = eventService.queryEventLevel();
			
			model.addAttribute("els", els);

			if(ud.getId()==notice.getFuze()){
				model.addAttribute("publish_id", 1);
			} else{
				model.addAttribute("publish_id", 0);
			}
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("n_id", id);
			PageSupport ps = PageSupport.initPageSupport(request);
			List<NoticeRecord> nrs = noticeService.queryAllNoticeRecord(params,ps);
			Boolean flag=false;
			if(see_record!=null && see_record.intValue()==1){
				model.addAttribute("see_record", see_record);
				flag=true;
			}
			
			model.addAttribute("nrs", nrs);
			model.addAttribute("delays", delays);
			model.addAttribute("pipeLineList", pipeLineList);
			model.addAttribute("leaders", nleaders);
			model.addAttribute("receivers", nreceivers);
			model.addAttribute("allReceivers", allReceivers);
			model.addAttribute("replies", replies);
			model.addAttribute("notice", notice);
			
			if (addre != null && addre.intValue() == 1) {
				model.addAttribute("msg", "指派成功！");
				flag=true;
			} else if (delay != null && delay.intValue() == 1) {
				model.addAttribute("msg", "延期申请已提交！");
				flag=true;
			} else if (verify != null && verify.intValue() == 1) {
				model.addAttribute("msg", "审核成功！");
				flag=true;
			} else if (askclose != null && askclose.intValue() == 1) {
				model.addAttribute("msg", "申请关闭提交成功！");
				flag=true;
			} else if (changeover != null && changeover.intValue() == 1) {
				model.addAttribute("msg", "完成时间修改成功！");
				flag=true;
			} else if (changefuze != null && changefuze.intValue() == 1) {
				model.addAttribute("msg", "负责人修改成功！");
				flag=true;
			}
			if(!flag){
				if(notice.getStatus()==1){
					noticeService.saveRecords(ud.getName()+"查看了闭环临时性工作", ud.getId(), id);
				}
			}
			return "pages/notice/tmp_notice_detail";
		}
		List<NoticeReceiver> receivers = noticeService.queryWorkerList(null,
				null);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		List<NoticeReceiver> leaders = noticeService.queryLeaderList();
		// 移出李根、黄健、李相蓉
		List<NoticeReceiver> nleaders = new ArrayList<NoticeReceiver>();
		for (NoticeReceiver nr : leaders) {
			if (nr.getUser_id().intValue() == 2
					|| nr.getUser_id().intValue() == 3) {
				continue;
			}
			nleaders.add(nr);
		}
		Prompt prompt = baseService.quertPromptByType(15);
		if (prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		List<EventLevel> els = eventService.queryEventLevel();
		
		model.addAttribute("els", els);
		
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("allReceivers", receivers);
		model.addAttribute("leaders", nleaders);
		if (add != null && add.intValue() == -1) {
			model.addAttribute("msg", "保存失败！");
		} else if (add != null && add.intValue() == 1) {
			model.addAttribute("msg", "保存成功！");
		}
		return "pages/notice/tmp_notice_create";
	}

	/**
	 * 创建临时通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_edit", method = RequestMethod.GET)
	public String tmpNoticeEdit(Model model,
			@RequestParam(required = false) Integer add,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer tips_id,
			@RequestParam(required = false) Integer del) {
		if (id != null && id.intValue() > 0) {
			if (tips_id != null && tips_id.intValue() > 0)
				noticeService.deleteTips(tips_id);

			Notice notice = noticeService.queryTempNoticeDetailById(id);
			if (notice != null) {
				notice.setId(id);
				model.addAttribute("notice", notice);
			} else {
				Notice n = new Notice();
				n.setContent("该通知已被删除");
				model.addAttribute("notice", n);
			}
			List<NoticeReceiver> rec = noticeService.queryNoticeReceiver(notice
					.getId());
			// 移出李根、黄健、李相蓉
			List<NoticeReceiver> nrec = new ArrayList<NoticeReceiver>();
			for (NoticeReceiver nr : rec) {
				if (nr.getUser_id().intValue() == 2
						|| nr.getUser_id().intValue() == 3) {
					continue;
				}
				nrec.add(nr);
			}
			List<NoticeReply> replies = noticeService
					.queryNoticeReplyByNoticeId(id);

			model.addAttribute("rec", nrec);
			model.addAttribute("replies", replies);
			// model.addAttribute("notice", notice);
			// return "pages/notice/tmp_notice_detail";

			List<NoticeReceiver> receivers = noticeService.queryWorkerList(
					null, null);
			List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
			List<NoticeReceiver> leaders = noticeService.queryLeaderList();
			// 移出李根、黄健、李相蓉
			List<NoticeReceiver> nleaders = new ArrayList<NoticeReceiver>();
			for (NoticeReceiver nr : leaders) {
				if (nr.getUser_id().intValue() == 2
						|| nr.getUser_id().intValue() == 3) {
					continue;
				}
				nleaders.add(nr);
			}
			Prompt prompt = baseService.quertPromptByType(15);
			if (prompt != null) {
				model.addAttribute("prompt", prompt);
			}
			List<EventLevel> els = eventService.queryEventLevel();
			
			model.addAttribute("els", els);
			
			model.addAttribute("pipeLineList", pipeLineList);
			model.addAttribute("allReceivers", receivers);
			model.addAttribute("leaders", nleaders);
			if (add != null && add.intValue() == -1) {
				model.addAttribute("msg", "修改失败！");
			} else if (add != null && add.intValue() == 1) {
				model.addAttribute("msg", "修改成功！");
			}
			if (del != null) {
				model.addAttribute("msg", "删除成功！");
			}
		}
		return "pages/notice/tmp_notice_edit";
	}

	/**
	 * 删除通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_del", method = RequestMethod.GET)
	public String tmpnoticeDel(Model model, HttpServletRequest request,
			@RequestParam Integer id) {
		// 把要删除的文件放入delete文件夹中
		try {
			List<NoticeReply> np = noticeService.queryNoticeReplyByNoticeId(id);
			if (!CollectionUtils.isEmpty(np)) {
				for (NoticeReply n : np) {
					if (!StringUtils.isBlank(n.getPath())) {
						DataUtil.moveFileToDeleteFileDir(n.getPath());
					}
				}
			}
			Notice nt = noticeService.queryTempNoticeDetailById(id);
			if (!StringUtils.isBlank(nt.getPath())) {
				DataUtil.moveFileToDeleteFileDir(nt.getPath());
			}
			noticeService.updateNoticeClose(id, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/admin/tmp_notice_list?del=1";
	}

	/**
	 * 增加指派人员
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_addre", method = RequestMethod.GET)
	public String addre(Model model, HttpServletRequest request,
			@RequestParam Integer id, @RequestParam(required=false) String users, @RequestParam(required=false) String cancel_users) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		List<Integer> receivers = new ArrayList<Integer>();
		if(!StringUtils.isBlank(users)){
		String[] userArr = users.split(",");
		for (String ids : userArr) {
			receivers.add(Integer.valueOf(ids));
		}
		}
		
		List<Integer> cancel_receivers = new ArrayList<Integer>();
		if(!StringUtils.isBlank(cancel_users)){
		String[] cancel_userArr = cancel_users.split(",");
		for (String ids : cancel_userArr) {
			cancel_receivers.add(Integer.valueOf(ids));
		}
		}
		noticeService.addTempNoticeRecevers(id, receivers,cancel_receivers,ud.getId());
		return "redirect:/admin/tmp_notice_create?addre=1&id=" + id;
	}

	/**
	 * 保存临时通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_save", method = RequestMethod.POST)
	public String tmpNoticeSave(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer id,@RequestParam Integer jibie,
			@RequestParam String title, @RequestParam String content,
			@RequestParam String users,/* @RequestParam String postil,*/
			@RequestParam Integer send, @RequestParam String over_time,
			@RequestParam String fuze_name, @RequestParam Integer fuze) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setType(Notice.TYPE_TEMP);
		//notice.setPostil(postil);
		notice.setJibie(jibie);
		notice.setSend(send);
		notice.setUser_id(ud.getId());
		notice.setFuze(fuze);
		notice.setFuze_name(fuze_name);
		notice.setOver_time(DateFormatter.stringToDate(over_time, "yyyy-MM-dd HH:mm:ss"));
		notice.setId(id);
		try {
			List<String> paths = DataUtil.uploadFile(request, "file");
			if (!CollectionUtils.isEmpty(paths)) {
				notice.setPath(paths.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Integer> receivers = new ArrayList<Integer>();
		// 增加李根、黄健、李相蓉
		//receivers.add(2);
		receivers.add(3);
		//receivers.add(4);
		if(!StringUtils.isBlank(users)){
			String[] userArr = users.split(",");
			for (String ids : userArr) {
				receivers.add(Integer.valueOf(ids));
			}
		} else {
			receivers.clear();
		}
		noticeService.saveTempNotice(notice, receivers,ud.getId());
		if (id != null && id.intValue() > 0) {
			return "redirect:/admin/tmp_notice_edit?add=1&&id=" + id;
		} else {
			return "redirect:/admin/tmp_notice_create?add=1";
		}

	}

	/**
	 * 临时通知查看
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_list", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String tmpNoticeList(HttpServletRequest request, Model model,
			@RequestParam(required = false) Integer del,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String key_w,
			@RequestParam(required = false) Integer uid,
			@RequestParam(required = false) Integer close,
			@RequestParam(required = false) String f_time,
			@RequestParam(required = false) String t_time) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (ud == null) {
			return "pages/login";
		}
		Integer roleId = userService.queryRoleIdByUserId(ud.getId());

		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", Notice.TYPE_TEMP);
		param.put("user_id", ud.getId());
		param.put("roleId", roleId);
		model.addAttribute("role", roleId);
		if(ud.getId()==4){
			param.put("all", 1);
		}
		
		if (!StringUtils.isBlank(key_w)) {
			param.put("key_w", key_w);
			model.addAttribute("key_w", key_w);
		}
		if (uid != null && uid.intValue() > 0) {
			param.put("uid", uid);
			model.addAttribute("uid", uid);
		}
		if (close != null) {
			param.put("status", close);
			model.addAttribute("close", close);
		}
		if (!StringUtils.isBlank(f_time)) {
			param.put("f_time", f_time);
			param.put("t_time", t_time);
			model.addAttribute("f_time", f_time);
			model.addAttribute("t_time", t_time);
		}
		List<Notice> notices = new ArrayList<Notice>();
		if (roleId == 1) {
			notices = noticeService.queryAllNotice(param, ps);
		} else {
			notices = noticeService.queryNotice(param, ps);
		}
		model.addAttribute("notices", notices);
		if (del != null) {
			model.addAttribute("msg", "删除成功！");
		}

		model.addAttribute("authors",
				noticeService.queryNoticeAuthors(Notice.TYPE_TEMP));

		return "pages/notice/tmp_notice_list";
	}

	/**
	 * 临时通知状态更新
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_update_status", method = {
			RequestMethod.POST, RequestMethod.GET })
	public String tmpNoticeUpdateStatus(HttpServletRequest request,
			Model model, @RequestParam Integer id, @RequestParam Integer status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (ud == null) {
			return "pages/login";
		}
		noticeService.updateNoticeStatus(id, status,ud);

		return "redirect:/admin/tmp_notice_list";
	}

	/**
	 * 临时通知请求关闭
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_update_ask", method = {
			RequestMethod.POST, RequestMethod.GET })
	public String tmpNoticeUpdateAsk(HttpServletRequest request, Model model,
			@RequestParam Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (ud == null) {
			return "pages/login";
		}
		noticeService.updateNoticeAsk(id, 1,ud);

		return "redirect:/admin/tmp_notice_create?askclose=1&id=" + id;
	}
	
	/**
	 * 临时通知修改完成时间
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_change_over_time", method = {
			RequestMethod.POST, RequestMethod.GET })
	public String tmpNoticechange_over_time(HttpServletRequest request, Model model,
			@RequestParam Integer id,@RequestParam String over_time) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (ud == null) {
			return "pages/login";
		}
		noticeService.updateNoticeOverTime(id, DateFormatter.stringToDate(over_time, "yyyy-MM-dd HH:mm:ss"),ud);

		return "redirect:/admin/tmp_notice_create?changeover=1&id=" + id;
	}
	
	/**
	 * 临时通知修改责任人
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_change_fuze", method = {
			RequestMethod.POST, RequestMethod.GET })
	public String tmpNoticechange_fuze(HttpServletRequest request, Model model,
			@RequestParam Integer id,@RequestParam Integer fuze_id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (ud == null) {
			return "pages/login";
		}
		noticeService.updateNoticeFuze(id, fuze_id,ud);

		return "redirect:/admin/tmp_notice_create?changefuze=1&id=" + id;
	}

	@RequestMapping(value = "/findWorker", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody List<NoticeReceiver> findWorker(
			HttpServletRequest request, Model model,
			@RequestParam(required = false) Integer pl_id,
			@RequestParam(required = false) Integer section_id) {
		if (pl_id != null && pl_id.intValue() <= 0)
			pl_id = null;
		if (section_id != null && section_id.intValue() <= 0)
			section_id = null;
		List<NoticeReceiver> receivers = noticeService.queryWorkerList(pl_id,
				section_id);
		return receivers;
	}

	@RequestMapping(value = "/findAddWorker", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody List<NoticeReceiver> findAddWorker(
			HttpServletRequest request, Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer pl_id,
			@RequestParam(required = false) Integer section_id) {
		if (pl_id != null && pl_id.intValue() <= 0)
			pl_id = null;
		if (section_id != null && section_id.intValue() <= 0)
			section_id = null;
		List<NoticeReceiver> receivers = noticeService.queryWorkerList(pl_id,
				section_id);
		List<NoticeReceiver> nreceivers = noticeService.queryNoticeReceiver(id);
		receivers.removeAll(nreceivers);
		return receivers;
	}

	/**
	 * 保存临时通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_reply", method = RequestMethod.POST)
	public String tmpNoticeReply(Model model, HttpServletRequest request,
			@RequestParam String msg_content, @RequestParam Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		NoticeReply nr = new NoticeReply();
		nr.setContent(msg_content);
		nr.setNotice_id(id);
		nr.setUser_id(ud.getId());
		try {
			List<String> paths = DataUtil.uploadFile(request, "file");
			if (!CollectionUtils.isEmpty(paths)) {
				nr.setPath(paths.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		noticeService.insertNoticeReply(nr);
		return "redirect:/admin/tmp_notice_create?id=" + id;
	}

	/**
	 * 审核
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_delay_verify", method = RequestMethod.POST)
	public String tmp_notice_delay_verify(Model model,
			@RequestParam(required = false) Integer n_id,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer verify_delay,
			@RequestParam(required = false) String verify_reason) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (ud == null) {
			return "pages/login";
		}
		noticeService.updateTmpNoticeDelayVerify(id, verify_delay,
				verify_reason,ud);

		return "redirect:/admin/tmp_notice_create?verify=1&id=" + n_id;

	}

	/**
	 * 申请延期
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tmp_notice_delay", method = RequestMethod.POST)
	public String tmpNoticeDelay(Model model, HttpServletRequest request,
			@RequestParam String delay_reason, @RequestParam String delay_time,
			@RequestParam Integer id) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (ud == null) {
			return "pages/login";
		}
		NoticeDelay nd = new NoticeDelay();
		nd.setN_id(id);
		nd.setDelay_reason(delay_reason);
		nd.setDelay_time(DateFormatter.stringToDate(delay_time, "yyyy-MM-dd HH:mm:ss"));
		noticeService.saveTmpNoticeDelay(nd,ud);

		return "redirect:/admin/tmp_notice_create?delay=1&id=" + id;
	}

	@RequestMapping(value = "/notice_read")
	public String noticeRead(HttpServletRequest request, Model model) {
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();

		List<Readed> readeds = noticeService.queryNoticeReadList(param, ps);

		model.addAttribute("readeds", readeds);
		return "pages/notice/notice_read_list";
	}

	/**
	 * 临时性工作请示创建
	 * 
	 * @param model
	 * @param add
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/ask_notice_create", method = RequestMethod.GET)
	public String askNoticeCreate(Model model,
			@RequestParam(required = false) Integer add,
			@RequestParam(required = false) Integer id) {
		if (id != null && id.intValue() > 0) {
			Notice notice = noticeService.queryTempNoticeDetailById(id);
			List<NoticeReceiver> receivers = noticeService
					.queryNoticeReceiver(notice.getId());
			// 移出李根、黄健、李相蓉
			List<NoticeReceiver> nreceivers = new ArrayList<NoticeReceiver>();
			for (NoticeReceiver nr : receivers) {
				if (nr.getUser_id().intValue() == 2
						|| nr.getUser_id().intValue() == 3
						|| nr.getUser_id().intValue() == 4) {
					continue;
				}
				nreceivers.add(nr);
			}
			List<NoticeReply> replies = noticeService
					.queryNoticeReplyByNoticeId(id);

			model.addAttribute("receivers", nreceivers);
			model.addAttribute("replies", replies);
			model.addAttribute("notice", notice);
			return "pages/notice/tmp_notice_detail";
		}
		List<NoticeReceiver> receivers = noticeService.queryWorkerList(null,
				null);
		List<BasePipeline> pipeLineList = baseService.queryPipeLine(new HashMap<String,Object>());
		List<NoticeReceiver> leaders = noticeService.queryLeaderList();
		// 移出李根、黄健、李相蓉
		List<NoticeReceiver> nleaders = new ArrayList<NoticeReceiver>();
		for (NoticeReceiver nr : leaders) {
			if (nr.getUser_id().intValue() == 2
					|| nr.getUser_id().intValue() == 3
					|| nr.getUser_id().intValue() == 4) {
				continue;
			}
			nleaders.add(nr);
		}
		Prompt prompt = baseService.quertPromptByType(10);
		if (prompt != null) {
			model.addAttribute("prompt", prompt);
		}
		model.addAttribute("pipeLineList", pipeLineList);
		model.addAttribute("allReceivers", receivers);
		model.addAttribute("leaders", nleaders);
		if (add != null && add.intValue() == -1) {
			model.addAttribute("msg", "保存失败！");
		} else if (add != null && add.intValue() == 1) {
			model.addAttribute("msg", "保存成功！");
		}
		return "pages/notice/ask_notice_create";
	}

	/**
	 * 临时性工作请示保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ask_notice_save", method = RequestMethod.POST)
	public String askNoticeSave(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer id,
			@RequestParam String title, @RequestParam String content,
			@RequestParam String ask_type, @RequestParam Integer send, @RequestParam Integer ask_ca,
			@RequestParam String users,
			@RequestParam(required=false) String filename) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Notice notice = new Notice();
		notice.setType(Notice.TYPE_ASK);
		notice.setTitle(title);
		notice.setSend(send);
		notice.setTitle(title);
		notice.setContent(content);
		notice.setUser_id(ud.getId());
		notice.setAsk_type(ask_type);
		notice.setAsk_ca(ask_ca);
		notice.setId(id);
		
		if (!StringUtils.isBlank(filename)) {
			List<String> fileNames = new ArrayList<String>();

			String[] names = filename.split(";");
			for (String name : names) {
				if (!StringUtils.isBlank(name)) {
					try {
						String fn = DataUtil.moveToFileDir(name);
						fileNames.add(fn);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			notice.setFileNames(fileNames);
		}
		/*try {
			List<String> paths = DataUtil.uploadFile(request, "file");
			if (!CollectionUtils.isEmpty(paths)) {
				notice.setPath(paths.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		List<Integer> receivers = new ArrayList<Integer>();
		// 增加李根、黄健、李相蓉
		//receivers.add(2);
		receivers.add(3);
		receivers.add(4);
		String[] userArr = users.split(",");
		for (String ids : userArr) {
			receivers.add(Integer.valueOf(ids));
		}

		noticeService.saveAskNotice(notice, receivers);
		if (id != null && id.intValue() > 0) {
			return "redirect:/admin/ask_notice_edit?add=1&&id=" + id;
		} else {
			return "redirect:/admin/ask_notice_create?add=1";
		}
	}

	@RequestMapping(value = "/ask_notice_list")
	public String askNoticeList(HttpServletRequest request, Model model,
			@RequestParam(required = false) Integer del,
			@RequestParam(required = false) Integer verify,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String key_w,
			@RequestParam(required = false) Integer uid,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false) Integer verify_status,
			@RequestParam(required = false) String f_time,
			@RequestParam(required = false) String t_time) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Integer roleId = userService.queryRoleIdByUserId(ud.getId());

		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", Notice.TYPE_ASK);
		param.put("user_id", ud.getId());
		if (!StringUtils.isBlank(key_w)) {
			param.put("key_w", key_w);
			model.addAttribute("key_w", key_w);
		}
		if (uid != null && uid.intValue() > 0) {
			param.put("uid", uid);
			model.addAttribute("uid", uid);
		}
		if (type != null && type.intValue() > 0) {
			param.put("ask_ca", 2);
			model.addAttribute("type", type);
		} else {
			param.put("ask_ca", 1);
		}
		if (verify_status != null) {
			param.put("verify_status", verify_status);
			model.addAttribute("verify_status", verify_status);
		}
		if (!StringUtils.isBlank(f_time)) {
			param.put("f_time", f_time);
			param.put("t_time", t_time);
			model.addAttribute("f_time", f_time);
			model.addAttribute("t_time", t_time);
		}
		if (roleId == 2) {
			param.put("roleId", roleId);
			if (verify != null && verify == 1 && roleId == 2) {
				model.addAttribute("verify", 1);
			}
		}
		//
		List<Notice> notices = new ArrayList<Notice>();
		if (roleId == 1) {
			notices = noticeService.queryAllNotice(param, ps);
		} else {
			notices = noticeService.queryNotice(param, ps);
		}
		model.addAttribute("notices", notices);
		if (del != null) {
			model.addAttribute("msg", "删除成功！");
		}
		model.addAttribute("authors",
				noticeService.queryNoticeAuthors(Notice.TYPE_ASK));
		return "pages/notice/ask_notice_list";
	}

	@RequestMapping(value = "/ask_notice_verify")
	public String askNoticeVerify(@RequestParam(required = false) Integer id,
			Model model, @RequestParam(required = false) Integer verify,
			@RequestParam(required = false) Integer tips_id) {
		Notice notice = noticeService.queryAskNotice(id);
		if (tips_id != null && tips_id.intValue() > 0)
			noticeService.deleteTips(tips_id);
		notice.setId(id);
		model.addAttribute("notice", notice);
		List<NoticeAttachement> nas = noticeService.queryNoticeAttachementByNoticeId(id);
		model.addAttribute("nas", nas);
		if (verify != null && verify == 1) {
			model.addAttribute("verify", 1);
		}

		return "pages/notice/ask_notice_verify";
	}

	@RequestMapping(value = "/ask_save_verify", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String askNoticeVerifySave(Model model,
			@RequestParam Integer id,
			@RequestParam(required = false) String verify_desc,
			@RequestParam(required = false) Integer verify_status) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Notice notice = new Notice();
		notice.setId(id);
		notice.setVerifier(ud.getId());
		notice.setVerify_desc(verify_desc);
		notice.setVerify_status(verify_status);
		noticeService.saveVerify(notice);
		return "OK";
	}

	/**
	 * 编辑请示通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ask_notice_edit", method = RequestMethod.GET)
	public String ask_noticeEdit(Model model,
			@RequestParam(required = false) Integer add,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer tips_id) {
		Notice notice = noticeService.queryAskNotice(id);
		if (notice != null) {
			notice.setId(id);
			model.addAttribute("notice", notice);
		} else {
			Notice n = new Notice();
			n.setContent("该通知已被删除");
			model.addAttribute("notice", n);

		}
		List<NoticeReceiver> receivers = noticeService
				.queryNoticeReceiver(notice.getId());
		// 移出李根、黄健、李相蓉
		List<NoticeReceiver> nreceivers = new ArrayList<NoticeReceiver>();
		for (NoticeReceiver nr : receivers) {
			if (nr.getUser_id().intValue() == 2
					|| nr.getUser_id().intValue() == 3
					|| nr.getUser_id().intValue() == 4) {
				continue;
			}
			nreceivers.add(nr);
		}
		model.addAttribute("receivers", nreceivers);

		List<NoticeReceiver> leaders = noticeService.queryLeaderList();
		// 移出李根、黄健、李相蓉
		List<NoticeReceiver> nleaders = new ArrayList<NoticeReceiver>();
		for (NoticeReceiver nr : leaders) {
			if (nr.getUser_id().intValue() == 2
					|| nr.getUser_id().intValue() == 3
					|| nr.getUser_id().intValue() == 4) {
				continue;
			}
			nleaders.add(nr);
		}
		model.addAttribute("leaders", nleaders);
		List<NoticeAttachement> nas = noticeService.queryNoticeAttachementByNoticeId(id);
		model.addAttribute("nas", nas);
		if (add != null) {
			model.addAttribute("msg", "修改成功!");
		}
		return "pages/notice/ask_notice_edit";
	}

	/**
	 * 删除通知
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ask_notice_del", method = RequestMethod.GET)
	public String asknoticeDel(Model model, HttpServletRequest request,
			@RequestParam Integer id) {
		// 把要删除的文件放入delete文件夹中
		try {

			Notice nt = noticeService.queryAskNotice(id);
			if (!StringUtils.isBlank(nt.getPath())) {
				DataUtil.moveFileToDeleteFileDir(nt.getPath());
			}
			noticeService.updateNoticeClose(id, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/admin/ask_notice_list?del=1";
	}

	/**
	 * 编辑请示通知回复
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_reply_edit", method = RequestMethod.GET)
	public String notice_reply_Edit(Model model,
			@RequestParam(required = false) Integer r_id,
			@RequestParam(required = false) Integer type) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		NoticeReply nr = noticeService.queryNoticeReplyById(r_id);
		model.addAttribute("nr", nr);
		model.addAttribute("type", type);
		return "pages/notice/notice_reply_edit";

	}

	/**
	 * 保存请示通知回复
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_reply_save", method = RequestMethod.POST)
	public String notice_reply_save(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer r_id,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false) String msg_content) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		NoticeReply nr = noticeService.queryNoticeReplyById(r_id);
		nr.setContent(msg_content);
		nr.setPath("");
		try {
			List<String> paths = DataUtil.uploadFile(request, "file");
			if (!CollectionUtils.isEmpty(paths)) {
				nr.setPath(paths.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		noticeService.insertNoticeReply(nr);
		if (type != null && type == 0) {
			return "redirect:/admin/notice_edit?add=1&&id=" + nr.getNotice_id();
		} else {
			return "redirect:/admin/tmp_notice_edit?add=1&&id="
					+ nr.getNotice_id();
		}
	}

	/**
	 * 删除通知回复
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notice_reply_del", method = RequestMethod.GET)
	public String noticeReplyDel(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer r_id,
			@RequestParam(required = false) Integer type) {
		// 把要删除的文件放入delete文件夹中
		NoticeReply nr = noticeService.queryNoticeReplyById(r_id);
		try {
			if (!StringUtils.isBlank(nr.getPath())) {
				DataUtil.moveFileToDeleteFileDir(nr.getPath());
			}
			noticeService.deleteNoticeReplyById(r_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type != null && type == 0) {
			return "redirect:/admin/notice_edit?del=1&&id=" + nr.getNotice_id();
		} else {
			return "redirect:/admin/tmp_notice_edit?del=1&&id="
					+ nr.getNotice_id();
		}
	}

	@RequestMapping(value = "/tmp_notice_exp", method = RequestMethod.POST)
	public String tmpNoticeExp(@RequestParam(required = false) Integer del,
			HttpServletRequest request,	HttpServletResponse response,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String key_w,
			@RequestParam(required = false) Integer uid,
			@RequestParam(required = false) Integer close,
			@RequestParam(required = false) String f_time,
			@RequestParam(required = false) String t_time) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (ud == null) {
			return "pages/login";
		}
		Integer roleId = userService.queryRoleIdByUserId(ud.getId());
		if (roleId != 1 && roleId != 2) {
			return null;
		}
		
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称
		String path = fileDir;
		String excelPath = path + sep + "excel.xls";
		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", Notice.TYPE_TEMP);
		param.put("user_id", ud.getId());
		param.put("roleId", roleId);
		if (!StringUtils.isBlank(key_w)) {
			param.put("key_w", key_w);
		}
		if (uid != null && uid.intValue() > 0) {
			param.put("uid", uid);
		}
		if (close != null) {
			param.put("status", close);
		}
		if (!StringUtils.isBlank(f_time)) {
			param.put("f_time", f_time);
			param.put("t_time", t_time);
		}
		List<Notice> notices = new ArrayList<Notice>();
		if (roleId == 1) {
			notices = noticeService.queryAllNotice(param);
		} else {
			notices = noticeService.queryNotice(param);
		}
		try {
			HSSFWorkbook work = new HSSFWorkbook();
			FileOutputStream fileOut = new FileOutputStream(path + sep
					+ "excel.xls");

			/*
			 * pm.getPl_name() + "_" + pm.getPl_section_name() + pm.getC_month()
			 */

			HSSFSheet sheet1 = work.createSheet("sheet1");
			HSSFCell cell;

			// 表头格式
			CellStyle titleStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();

			// 内容加上边框
			dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			// dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			// dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			// dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			// dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle.setWrapText(true);

			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);

			// 表头字体
			Font titlefont = work.createFont();
			// 内容字体
			Font datafont = work.createFont();

			titlefont.setFontHeightInPoints((short) 10);
			titlefont.setFontName("方正仿宋简体");
			titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");
			datafont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			// datafont.setColor(HSSFColor.RED.index);

			titleStyle.setFont(titlefont);
			dataStyle.setFont(datafont);

			// 设置列宽度
			sheet1.setColumnWidth(0, 7 * 256);
			sheet1.setColumnWidth(1, 12 * 256);
			sheet1.setColumnWidth(2, 12 * 256);
			sheet1.setColumnWidth(3, 30 * 256);
			sheet1.setColumnWidth(4, 34	 * 256);
			sheet1.setColumnWidth(5, 12 * 256);

			HSSFRow row;
			
			for(int i = 0; i < notices.size() + 1; i++) {
				row = sheet1.createRow(i);
				for(int x = 0; x < 6; x++) {
					cell = row.createCell(x);
					cell.setCellStyle(titleStyle);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
			}
			row = sheet1.getRow(0);
			cell = row.getCell(0);
			cell.setCellValue("序号");
			cell = row.getCell(1);
			cell.setCellValue("创建人");
			cell = row.getCell(2);
			cell.setCellValue("负责人");
			cell = row.getCell(3);
			cell.setCellValue("参与人");
			cell = row.getCell(4);
			cell.setCellValue("标题");
			cell = row.getCell(5);
			cell.setCellValue("完成情况");
			
			int row_index = 1;
			for (Notice nt : notices) {
				row = sheet1.getRow(row_index);
				
				cell = row.getCell(0);
				cell.setCellValue(row_index+"");
				cell = row.getCell(1);
				cell.setCellValue(nt.getAuthor());
				cell = row.getCell(2);
				cell.setCellValue(nt.getFuze_name());
				List<NoticeReceiver> receivers = noticeService
						.queryNoticeReceiver(nt.getId());
				// 移出李根、黄健、李相蓉
				List<NoticeReceiver> nreceivers = new ArrayList<NoticeReceiver>();
				for (NoticeReceiver nr : receivers) {
					if (nr.getUser_id().intValue() == 2
							|| nr.getUser_id().intValue() == 3
							|| nr.getUser_id().intValue() == 4) {
						continue;
					}
					nreceivers.add(nr);
				}
				String receiversName = "";
				for(NoticeReceiver nr : nreceivers) {
					receiversName += nr.getReceiver() + ";";
				}
				cell = row.getCell(3);
				cell.setCellValue(receiversName);
				cell = row.getCell(4);
				cell.setCellValue(nt.getTitle());
				String nt_status = null;
				if(nt.getAsk()==0 && nt.getStatus()==1 && nt.getIs_delay()==0) {
					nt_status = "正在进行";
				} else if(nt.getAsk()==1 && nt.getStatus()==1) {
					nt_status = "申请关闭";
				} else if(nt.getIs_delay()==1 && nt.getStatus()==1 && nt.getAsk()==0) {
					nt_status = "已延期";
				} else if(nt.getStatus()==0) {
					nt_status = "已关闭";
				}
				cell = row.getCell(5);
				cell.setCellValue(nt_status);
				
				
				row_index++;
			}
			// 将创建好的excel存到指定文件夹下
			work.write(fileOut);
			fileOut.close();
			// 压缩文件夹并下载，下载后删除文件夹
			FileUtils.createZip(response, excelPath, DateFormatter
					.dateToString(new Date(), "yyyy-MM-dd_HH:mm:ss:SSS"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}