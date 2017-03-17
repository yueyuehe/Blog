/**
 * 
 */
package cn.hm.oil.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.Notice;
import cn.hm.oil.domain.NoticeAttachement;
import cn.hm.oil.domain.NoticeDelay;
import cn.hm.oil.domain.NoticeRead;
import cn.hm.oil.domain.NoticeReceiver;
import cn.hm.oil.domain.NoticeRecord;
import cn.hm.oil.domain.NoticeReply;
import cn.hm.oil.domain.Readed;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.util.PageSupport;

/**
 * @author Administrator
 * 
 */
public interface NoticeService {

	public void updateNoticeStatus(Integer id, Integer status,SysUsers su);
	
	public void updateNoticeFuze(Integer id, Integer fuze_id,SysUsers su);
	
	public void updateNoticeAsk(Integer id, Integer ask,SysUsers su);
	
	public void updateNoticeOverTime(Integer id, Date over_time,SysUsers su);
	
	public void insertPublicNotice(Notice notice);

	public Notice queryPublicNoticeDetailById(Integer id);

	public void saveTempNotice(Notice notice, List<Integer> receivers,Integer publish_id);
	
	public void saveTmpNoticeDelay(NoticeDelay nd,SysUsers su);
	
	public List<NoticeDelay> queryAllTmpNoticeDelay(Map<String,Object> param);
	
	public void addTempNoticeRecevers(Integer id, List<Integer> receivers,List<Integer> cancel_receivers,Integer publish_id);
	
	public void saveAskNotice(Notice notice, List<Integer> receivers);

	public Notice queryTempNoticeDetailById(Integer id);

	public List<NoticeReceiver> queryNoticeReceiver(Integer notice_id);

	public List<NoticeReceiver> queryWorkerList(Integer pl_id, Integer pl_section_id);

	public void insertNoticeReply(NoticeReply noticeReply);

	public List<Notice> queryNotice(Map<String, Object> param, PageSupport ps);
	
	public List<NoticeRecord> queryAllNoticeRecord(Map<String, Object> param, PageSupport ps);
	
	public List<Notice> queryAllNotice(Map<String, Object> param, PageSupport ps);

	public List<NoticeReply> queryNoticeReplyByNoticeId(Integer notice_id);
	
	public NoticeRead queryNoticeRead(Integer notice_id,Integer user_id);
	
	public void insertNoticeRead(NoticeRead noticeRead);
	
	public List<Readed> queryNoticeReadList(Map<String, Object> param, PageSupport ps);

	/**
	 * 查询技术干部
	 * @return
	 */
	public List<NoticeReceiver> queryLeaderList();
	
	/**
	 * 通过id查找临时请示
	 * @param id
	 * @return
	 */
	public Notice queryAskNotice(Integer id);
	
	public List<NoticeAttachement> queryNoticeAttachementByNoticeId(Integer id);
	
	/**
	 * 更新审核信息
	 * @param notice
	 */
	public void saveVerify(Notice notice);
	
	public void deleteTips(Integer id);
	
	public void saveRecords(String content,Integer user_id,Integer n_id);
	
	public void updateNoticeClose(Integer id,Integer close);
	
	public void updateTmpNoticeDelayVerify(Integer id,Integer verify_delay,String verify_reason,SysUsers su);
	
	public NoticeReply queryNoticeReplyById(Integer id);
	
	public void deleteNoticeReplyById(Integer id);
	
	public List<SysUsers> queryNoticeAuthors(Integer type);
	
	public void TempNoticeTimerTask();
	
	public List<Notice> queryNotice(Map<String,Object> param);
	
	public List<Notice> queryAllNotice(Map<String,Object> param);
	
	public List<Notice> queryTmpByToday();
	
	public List<Notice> queryDelayTmpByToday();
}
