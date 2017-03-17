package cn.hm.oil.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.Event;
import cn.hm.oil.domain.EventAttachement;
import cn.hm.oil.domain.EventExcel;
import cn.hm.oil.domain.EventLevel;
import cn.hm.oil.domain.EventReceiver;
import cn.hm.oil.domain.EventReply;
import cn.hm.oil.domain.EventReplyAttachement;
import cn.hm.oil.domain.EventType;



public interface EventDao {
	
	public List<EventReceiver> queryLeaderList(@Param(value="level") Integer level);

	public List<EventReceiver> queryWorkerList(@Param(value="pl_id")Integer pl_id, @Param(value="pl_section_id") Integer pl_section_id);
	
	/**
	 * 获取一事一案类别
	 * 
	 * @return
	 */
	public List<EventType> queryEventType();
	
	/**
	 * 获取一事一案的所有回复
	 * 
	 * @param event_id
	 * @return
	 */
	public List<EventReply> queryEventReply(Integer event_id);
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
	 */
	public void updateEventStatus(@Param(value="status") Integer status, @Param(value="message") String message, @Param(value="id") Integer id, @Param(value="type_id") Integer type_id, @Param(value="verify_name") String verify_name, @Param(value="verify_level") Integer verify_level , @Param(value="code_no") String code_no);
	
	public void updateEventLevel(@Param(value="id") Integer id, @Param(value="event_level") Integer event_level);
	
	
	public Integer queryEventCodeNo(@Param(value="discover_date") Date discover_date);
	
	
	public EventReplyAttachement queryEventReplyAttachementById(@Param(value="id")Integer id);
	
	public void deleteEventReplyAttachementById(@Param(value="id")Integer id);
	
	/**
	 * 更新一事一案
	 * 
	 * @param event
	 */
	public void updateEvent(Event event);
	
	/**
	 * 添加新的一事一案
	 * 
	 * @param event
	 */
	public void insertEvent(Event event);
	
	/**
	 * 添加一事一安接收人
	 * 
	 * @param user_id
	 * @param event_id
	 */
	public void insertEventReceiver(@Param(value="user_id") Integer user_id, @Param(value="event_id") Integer event_id);
	
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
	 * 通过回复ID删除回复的附件
	 * 
	 * @param id
	 */
	public void deleteEventReplyAttachementByReplyId(@Param(value="id")Integer id);
	
	
	/**
	 * 通过一事一案ID删除一事一案附件
	 * 
	 * @param id
	 */
	public void deleteEventAttachementByEventId(@Param(value="id")Integer id);
	
	/**
	 * 移除一事一案接受者
	 * 
	 * @param user_id
	 */
	public void deleteEventReceiverByUserId(Integer user_id);
	
	/**
	 * 删除该用户的所有回复
	 * 
	 * @param user_id
	 */
	public void deleteEventReplyByUserId(Integer user_id);
	
	/**
	 * 删除指定ID的回复信息
	 * 
	 * @param id
	 */
	public void deleteEventReplyById(Integer id);
	
	public EventReply queryEventReplyById(Integer id);
	
	public void deleteEventReceiverByEventId(@Param(value="event_id") Integer event_id);
	
	public Event queryEventById(Integer id);
	
	public void updateEventClose(Integer id);
	
	public void insertEventAttachement(@Param(value="event_id") Integer event_id, @Param(value="path") String path);
	
	public void insertEventReplyAttachement(@Param(value="id") Integer id, @Param(value="path") String path);
	
	public List<EventAttachement> queryEventAttachementByEventId(@Param(value="event_id") Integer event_id);
	
	public EventAttachement queryEventAttachementById(@Param(value="id") Integer id);
	
	public void deleteEventAttachementById(@Param(value="id") Integer id);
	
	public List<EventLevel> queryEventLevel();
	
	public void updateEventMoonPath(@Param(value="id") Integer id,@Param(value="path") String path);
	
	public void updateEventReply(@Param(value="id") Integer id, @Param(value="content") String content, @Param(value="create_time") Date create_time);

	public void updateEventDelay();
	
	public void insertEventDelay();
	
	public void updateEventDelayById(@Param(value="id") Integer id);
	
	public void updateEventDelayByAll();
	
	public List<Event> queryEventDelay();
	
	public List<EventExcel> queryEventExcelByEventId(Integer id);
	
	public void insertEventExcel(EventExcel ee);
}
