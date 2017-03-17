/**
 * 
 */
package cn.hm.oil.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.hm.oil.dao.EventDao;
import cn.hm.oil.dao.SysPushDao;
import cn.hm.oil.dao.SysUsersDao;
import cn.hm.oil.dao.TipsDao;
import cn.hm.oil.domain.Event;
import cn.hm.oil.domain.EventAttachement;
import cn.hm.oil.domain.EventExcel;
import cn.hm.oil.domain.EventLevel;
import cn.hm.oil.domain.EventReceiver;
import cn.hm.oil.domain.EventReply;
import cn.hm.oil.domain.EventReplyAttachement;
import cn.hm.oil.domain.EventType;
import cn.hm.oil.domain.SysPush;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.Tips;
import cn.hm.oil.service.EventService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.AndroidPushMessageSample;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SimpleMailSender;

/**
 * @author Administrator
 *
 */
@Service(value="eventService")
public class EventServiceImpl extends AbstractModuleSuport implements EventService {

	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private TipsDao tipsDao;
	
	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private SysPushDao sysPushDao;

	@Override
	public List<EventReply> queryEventReply(Integer event_id) {
		return eventDao.queryEventReply(event_id);
	}

	@Override
	public List<EventReceiver> queryEventReceiver(Integer event_id) {
		return eventDao.queryEventReceiver(event_id);
	}

	@Override
	public void updateEventStatus(Integer status, String message, Integer event_id, 
			List<Integer> receiverList, Integer type_id, Integer up_to_level, String phmessage,String verify_name,Integer verify_level) {
		Event e = eventDao.queryEventById(event_id);
		if (status != null) {
			
			String content;
			if (status.intValue() == 1) {
				content = "您提交的一事一案"+e.getPl_name()+e.getPl_section_name()+e.getPl_spec_name()+e.getKeyword()+"已审核通过！";
			} else {
				content = "您提交的一事一案"+e.getPl_name()+e.getPl_section_name()+e.getPl_spec_name()+e.getKeyword()+"未审核通过！";
			}
			saveTips(content, e.getCreated_by(), event_id);
			Integer code = eventDao.queryEventCodeNo(e.getDiscover_date());
			code++;
			DecimalFormat df = new DecimalFormat("000");
			String code_no= df.format(code);
/*			if (status == 1) {
				if (0!=code) {
					//String[] ar = code.split("-");
					code++;
					switch (verify_level) {
					case 1:
						code_no = "G-"+code;
						break;
					case 2:
						code_no = "Z-"+code;
						break;
					case 3:
						code_no = "S-"+code;
						break;
					default:
						code_no = "0";
					}
				} else {
					switch (verify_level) {
					case 1:
						code_no = "G-1";
						break;
					case 2:
						code_no = "Z-1";
						break;
					case 3:
						code_no = "S-1";
						break;
					default:
						code_no = "0";
					}
				}
			}*/
			String date = DateUtil.formatDate(e.getDiscover_date(), "yyyy-MM-dd");
			code_no = date + "-" + code_no;
			eventDao.updateEventStatus(status, message, event_id, type_id,verify_name,verify_level,code_no);
		}
		if (!CollectionUtils.isEmpty(receiverList)) {
			//List<EventReceiver> receiverHistory = eventDao.queryEventReceiver(event_id);
			//if (!CollectionUtils.isEmpty(receiverHistory)) {
				for (Integer user_id : receiverList) {
					/*boolean exists = false;
					for (EventReceiver er : receiverHistory) {
						if (er.getUser_id().intValue() == user_id.intValue()) {
							exists = true;
						}
					}
					
					if (!exists) {*/
						saveTips("您有收到一事一案"+e.getPl_name()+e.getPl_section_name()+e.getPl_spec_name()+e.getKeyword()+"消息，请及时处理！", user_id, event_id);
					//}
				}
			//}
			eventDao.deleteEventReceiverByEventId(event_id);
			for (Integer user_id : receiverList) {
				eventDao.insertEventReceiver(user_id, event_id);
			}
		}
		if (up_to_level != null && up_to_level.intValue() == 1) {
			SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<SysUsers> sus = sysUsersDao.querySysUserByLevel(ud.getLevel() - 1);
			for (SysUsers s : sus) {
				eventDao.insertEventReceiver(s.getId(), event_id);
				saveTips("您有收到一事一案"+e.getPl_name()+e.getPl_section_name()+e.getPl_spec_name()+e.getKeyword()+"需要批复，请及时处理！", s.getId(), event_id);
			}
		}
		
		if (!StringUtils.isBlank(phmessage)) {
			SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			EventReply er = new EventReply();
			er.setLeader(1);
			er.setContent(phmessage);
			er.setEvent_id(event_id);
			er.setUser_id(ud.getId());
			eventDao.insertEventReply(er);
		}
	}

	@Override
	public void saveEvent(Event event) {
		try {
		if (event.getId() != null && event.getId().intValue() > 0) {
			Event et = eventDao.queryEventById(event.getId());
			if(!StringUtils.isBlank(event.getNotice_file())){
				if(!StringUtils.isBlank(et.getNotice_file())){
						DataUtil.moveFileToDeleteFileDir(et.getNotice_file());
				}
			}
			if(!StringUtils.isBlank(event.getWarn_file())){
				if(!StringUtils.isBlank(et.getWarn_file())){
						DataUtil.moveFileToDeleteFileDir(et.getWarn_file());
				}
			}
			if(!StringUtils.isBlank(event.getMoon_file())){
				if(!StringUtils.isBlank(et.getMoon_file())){
						DataUtil.moveFileToDeleteFileDir(et.getMoon_file());
				}
			}
			if(!StringUtils.isBlank(event.getScene_file())){
				if(!StringUtils.isBlank(et.getScene_file())){
						DataUtil.moveFileToDeleteFileDir(et.getScene_file());
				}
			}
			eventDao.updateEvent(event);
		} else {
			eventDao.insertEvent(event);
			
		}
		Event e = eventDao.queryEventById(event.getId());
		saveTips("您有收到一事一案"+e.getPl_name()+e.getPl_section_name()+e.getPl_spec_name()+e.getKeyword()+"需要审核，请及时处理！", 6, "/admin/event/verify_detail?id=" + event.getId());
		if (!CollectionUtils.isEmpty(event.getFileNames())) {
			for(String fn : event.getFileNames()) {
				eventDao.insertEventAttachement(event.getId(), fn);
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void insertEventReply(EventReply eventReply) {
		Event e = eventDao.queryEventById(eventReply.getEvent_id());
		//saveTips("您提交的一事一案有了新回复！", e.getCreated_by(), eventReply.getEvent_id());
		eventDao.insertEventReply(eventReply);
		if (!CollectionUtils.isEmpty(eventReply.getFileNames())) {
			for(String fn : eventReply.getFileNames()) {
				eventDao.insertEventReplyAttachement(eventReply.getId(), fn);
			}
		}
	}

	@Override
	public void deleteEventById(Integer id) {
		eventDao.deleteEventById(id);
	}

	@Override
	public void deleteEventReceiverByUserId(Integer user_id) {
		eventDao.deleteEventReceiverByUserId(user_id);
		eventDao.deleteEventReplyByUserId(user_id);
	}

	@Override
	public void deleteEventReplyById(Integer id) {
		EventReply er = eventDao.queryEventReplyById(id);
		if(er!=null){
				if(!CollectionUtils.isEmpty(er.getEras())){
					for(EventReplyAttachement p : er.getEras()){
						if(!StringUtils.isBlank(p.getPath())){
							try {
								DataUtil.moveFileToDeleteFileDir(p.getPath());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
		}
		eventDao.deleteEventReplyById(id);
	}

	@Override
	public List<EventReceiver> queryWorkerList(Integer pl_id, Integer pl_section_id) {
		return eventDao.queryWorkerList(pl_id, pl_section_id);
	}

	@Override
	public List<Event> queryEvent(Map<String, Object> param, PageSupport ps) {
		if(ps==null){
			return this.getList("cn.hm.oil.dao.EventDao.queryEvent", param);
		} else {
			return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.EventDao.queryEvent", param, ps);
		}
	}

	@Override
	public Event queryEventById(Integer id) {
		return eventDao.queryEventById(id);
	}

	@Override
	public List<EventType> queryEventType() {
		return eventDao.queryEventType();
	}

	@Override
	public List<EventReceiver> queryLeaderList(Integer level) {
		return eventDao.queryLeaderList(level);
	}
	
	private void saveTips(String message, Integer user_id, String url) {
		Tips tips = new Tips();
		tips.setContent(message);
		tips.setUser_id(user_id);
		tips.setUrl(url);
		
		tipsDao.saveTips(tips);
	}
	
	private void saveTips(String message, Integer user_id, Integer event_id) {
		Tips tips = new Tips();
		tips.setContent(message);
		tips.setUser_id(user_id);
		tips.setUrl("/admin/event/detail?id=" + event_id);
		//此处需要查询用户通道id
		tipsDao.saveTips(tips);
		List<String> address = new ArrayList<String>();
		String mail = sysUsersDao.queryMailAddress(user_id);
		if(!StringUtils.isBlank(mail)){
			address.add(mail);
			SimpleMailSender.sendMail(address,"您有新的消息，请及时处理",message);
		}
		SysPush push = sysPushDao.queryPush(tips.getUser_id());
		if(push != null) {
			AndroidPushMessageSample.pushNotice(push.getUserId(), push.getChannelId(), "新消息", tips);
		}
	}

	@Override
	public void updateEventClose(Integer id, String msg) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!StringUtils.isBlank(msg)) {
			EventReply er = new EventReply();
			er.setLeader(1);
			er.setContent(msg);
			er.setEvent_id(id);
			er.setUser_id(ud.getId());
			eventDao.insertEventReply(er);
		}
		eventDao.updateEventClose(id);
		Event e = eventDao.queryEventById(id);
		saveTips("您有收到一事一案"+e.getPl_name()+e.getPl_section_name()+e.getPl_spec_name()+e.getKeyword()+"已处理完毕，已被领导关闭！", ud.getId(), id);
	}

	@Override
	public void deleteTips(Integer id) {
		tipsDao.deleteTipsByid(id);
	}

	@Override
	public List<EventAttachement> queryEventAttachementByEventId(Integer event_id) {
		return eventDao.queryEventAttachementByEventId(event_id);
	}

	@Override
	public void deleteEventAttachementById(Integer id) {
		EventAttachement et = eventDao.queryEventAttachementById(id);
		try {
			DataUtil.moveFileToDeleteFileDir(et.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eventDao.deleteEventAttachementById(id);
		
	}

	@Override
	public void deleteEvent(Integer id) {
		Event e = eventDao.queryEventById(id);
		try {
		if(!StringUtils.isBlank(e.getWarn_file())){
			DataUtil.moveFileToDeleteFileDir(e.getWarn_file());
		}
		if(!StringUtils.isBlank(e.getNotice_file())){
			DataUtil.moveFileToDeleteFileDir(e.getNotice_file());
		}
		List<EventAttachement> ea = eventDao.queryEventAttachementByEventId(id);
		if(!CollectionUtils.isEmpty(ea)){
			for(EventAttachement a : ea){
				if(!StringUtils.isBlank(a.getPath())){
					DataUtil.moveFileToDeleteFileDir(a.getPath());
				}
			}
		}
		List<EventReply> er = eventDao.queryEventReply(id);
		if(!CollectionUtils.isEmpty(er)){
			for(EventReply r : er){
				if(!CollectionUtils.isEmpty(r.getEras())){
					for(EventReplyAttachement p : r.getEras()){
						if(!StringUtils.isBlank(p.getPath())){
							DataUtil.moveFileToDeleteFileDir(p.getPath());
						}
					}
					//eventDao.deleteEventReplyAttachementByReplyId(r.getId());
					
				}
				//eventDao.deleteEventReplyById(r.getId());
			}
		}
		//eventDao.deleteEventReceiverByEventId(id);
		//eventDao.deleteEventAttachementByEventId(id);
		
		eventDao.deleteEventById(id);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public List<EventLevel> queryEventLevel() {
		return eventDao.queryEventLevel();
	}

	@Override
	public void updateEventMoonPath(Integer id, String path) {
		Event e = eventDao.queryEventById(id);
		if(!StringUtils.isBlank(e.getMoon_file())){
			try {
				DataUtil.moveFileToDeleteFileDir(e.getMoon_file());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		eventDao.updateEventMoonPath(id,path);
	}

	@Override
	public EventReply queryEventReplyById(Integer id) {
		return eventDao.queryEventReplyById(id);
	}

	@Override
	public void updateEventReply(Integer id, String content,List<String> fileNames,Date create_time) {
		if(!CollectionUtils.isEmpty(fileNames)){
			for(String f : fileNames ){
				if(!StringUtils.isBlank(f)){
					eventDao.insertEventReplyAttachement(id, f);
				}
			}
		}
		eventDao.updateEventReply(id, content,create_time);
	}

	@Override
	public void deleteEventReplyAttachementById(Integer id) {
		EventReplyAttachement era = eventDao.queryEventReplyAttachementById(id);
		if(!StringUtils.isBlank(era.getPath())){
			try {
				DataUtil.moveFileToDeleteFileDir(era.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		eventDao.deleteEventReplyAttachementById(id);
	}

	@Override
	public void updateEventLevel(Integer id, Integer event_level) {
		eventDao.updateEventLevel(id,event_level);
	}
	
	@Override
	public void updateEventDelay() {
		eventDao.updateEventDelay();
	}
	
	@Override
	public void insertEventDelay() {
		eventDao.insertEventDelay();
	}
	
	@Override
	public void updateEventDelayById(Integer id) {
		eventDao.updateEventDelayById(id);
	}
	
	@Override
	public void updateEventDelayByAll() {
		eventDao.updateEventDelayByAll();
	}

	@Override
	public List<Event> queryEventDelay() {
		return eventDao.queryEventDelay();
	}

	@Override
	public void saveEventExcel(EventExcel ee) {
		eventDao.insertEventExcel(ee);
	}

	@Override
	public List<EventExcel> queryEventExcelByEventId(Integer id) {
		return eventDao.queryEventExcelByEventId(id);
	}
}
