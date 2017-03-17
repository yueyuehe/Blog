package cn.hm.oil.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.Event;
import cn.hm.oil.domain.EventAttachement;
import cn.hm.oil.domain.EventExcel;
import cn.hm.oil.domain.EventLevel;
import cn.hm.oil.domain.EventReceiver;
import cn.hm.oil.domain.EventReply;
import cn.hm.oil.domain.EventType;
import cn.hm.oil.util.PageSupport;

public interface EventService {
	/**
	 * 获取一事一案的所有回复
	 * 
	 * @param event_id
	 * @return
	 */
	public List<EventReply> queryEventReply(Integer event_id);
	
	
	/**
	 * 获取一事一案的等级
	 * 
	 * @param event_id
	 * @return
	 */
	public List<EventLevel> queryEventLevel();
	/**
	 * 获取一事一案的所有接收者
	 * 
	 * @param event_id
	 * @return
	 */
	public List<EventReceiver> queryEventReceiver(Integer event_id);
	
	/**
	 * 审核一事一案
	 * 
	 * @param status
	 * @param message
	 * @param id
	 * @param phmessage 领导批复消息
	 */
	public void updateEventStatus(Integer status, String message, Integer event_id, List<Integer> receiverList, 
			Integer type_id, Integer up_to_level, String phmessage,String verify_name,Integer verify_level);
	
	/**
	 * 添加新的一事一案
	 * 
	 * @param event
	 */
	public void saveEvent(Event event);
	
	public void saveEventExcel(EventExcel ee);
	
	public List<EventExcel> queryEventExcelByEventId(Integer id);
	
	/**
	 * 回复一事一案
	 * 
	 * @param eventReply
	 */
	public void insertEventReply(EventReply eventReply);
	
	/**
	 * 通过ID删除一事一案
	 * 
	 * @param id
	 */
	public void deleteEventById(Integer id);
	
	/**
	 * 删除一事一案
	 * @param id
	 */
	public void deleteEvent(Integer id);
	
	/**
	 * 移除一事一案接受者
	 * 
	 * @param user_id
	 */
	public void deleteEventReceiverByUserId(Integer user_id);
	
	/**
	 * 删除指定ID的回复信息
	 * 
	 * @param id
	 */
	public void deleteEventReplyById(Integer id);
	
	public void deleteEventReplyAttachementById(Integer id);
	
	public List<EventReceiver> queryWorkerList(Integer pl_id, Integer pl_section_id);
	
	public List<EventReceiver> queryLeaderList(Integer level);
	
	/**
	 * 查询一事一案
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<Event> queryEvent(Map<String, Object> param, PageSupport ps);
	
	public Event queryEventById(Integer id);
	
	/**
	 * 获取一事一案类别
	 * 
	 * @return
	 */
	public List<EventType> queryEventType();
	
	public void updateEventClose(Integer id, String msg);
	
	public void updateEventLevel(Integer id, Integer evet_level);
	
	public void deleteTips(Integer id);
	
	public List<EventAttachement> queryEventAttachementByEventId(Integer event_id);
	
	public void deleteEventAttachementById(Integer id);
	
	public void updateEventMoonPath(Integer id,String path);
	
	public EventReply queryEventReplyById(Integer id);
	
	public void updateEventReply(Integer id, String content,List<String> fileNames,Date create_time);
	
	/**
	 * 更新所有在今天未带附件回复的一事一案的标红状态
	 */
	public void updateEventDelay();
	
	/**
	 * 插入一事一案的操作记录
	 */
	public void insertEventDelay();
	
	public void updateEventDelayById(Integer id);
	
	public void updateEventDelayByAll();
	
	public List<Event> queryEventDelay();
}
