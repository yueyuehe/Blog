/**
 * 
 */
package cn.hm.oil.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.Notice;
import cn.hm.oil.domain.NoticeAttachement;
import cn.hm.oil.domain.NoticeDelay;
import cn.hm.oil.domain.NoticeRead;
import cn.hm.oil.domain.NoticeReceiver;
import cn.hm.oil.domain.NoticeRecord;
import cn.hm.oil.domain.NoticeReply;
import cn.hm.oil.domain.SysUsers;

/**
 * @author Administrator
 * 
 */
public interface NoticeDao {
	
	public void updateNoticeStatus(@Param(value="id") Integer id, @Param(value="status") Integer status);
	
	public void updateNoticeFuze(@Param(value="id") Integer id, @Param(value="fuze_id") Integer fuze_id);
	
	public void updateNoticeAsk(@Param(value="id") Integer id, @Param(value="ask") Integer ask);
	
	public void updateNoticeOverTime(@Param(value="id") Integer id, @Param(value="over_time") Date over_time);
	
	/**
	 * 判断修改后的时间是否延期，如果不延期就改变延期状态
	 * @param id
	 * @param over_time
	 */
	public void updateNoticeIsDelay(@Param(value="id") Integer id, @Param(value="over_time") Date over_time);
	
	public void insertPublicNotice(Notice notice);

	public Notice queryPublicNoticeDetailById(Integer id);

	public void insertTempNotice(Notice notice);
	
	public void insertAskNotice(Notice notice);

	public void insertNoticeReceiver(NoticeReceiver noticeReceiver);
	
	public void insertNoticeAttachement(@Param(value="n_id") Integer n_id, @Param(value="path") String path);

	public Notice queryTempNoticeDetailById(Integer id);

	public List<NoticeReceiver> queryNoticeReceiver(Integer notice_id);

	public List<NoticeReceiver> queryWorkerList(@Param(value="pl_id")Integer pl_id, @Param(value="pl_section_id") Integer pl_section_id);
	
	public void insertNoticeReply(NoticeReply noticeReply);
	
	public void updateLastReplyTime(Integer id);

	public List<NoticeReply> queryNoticeReplyByNoticeId(Integer notice_id);
	
	//public void deleteNoticeReplyById(Integer id);
	
	public NoticeRead queryNoticeRead(@Param(value="notice_id") Integer notice_id, @Param(value="user_id")Integer user_id);

	public void insertNoticeRead(NoticeRead noticeRead);
	
	public List<NoticeReceiver> queryLeaderList();
	
	public Notice queryAskNoticeById(@Param(value = "id") Integer id);
	
	public void saveVerify(Notice notice);
	
	public void saveRecord(NoticeRecord nr);
	
	public void updateNoticeClose(@Param(value = "id") Integer id,@Param(value = "close") Integer close);
	
	public void updateTmpNoticeDelayVerify(@Param(value = "id")Integer id, @Param(value = "verify_delay")Integer verify_delay,@Param(value = "verify_reason")String verify_reason);
	
	public void updatePublicNotice(Notice notice);
	
	public void deleteReservers(@Param(value = "id") Integer id);
	
	public void deleteCancelReservers(@Param(value = "notice_id") Integer id,@Param(value = "user_id") Integer user_id);
	
	public void updateAskNotice(Notice notice);
	
	public void updateTempNotice(Notice notice);
	
	public NoticeReply queryNoticeReplyById(@Param(value = "id") Integer id);
	
	public void updateNoticeReply(NoticeReply noticeReply);
	
	public void deleteNoticeReplyById(@Param(value = "id") Integer id);
	
	public List<SysUsers> queryNoticeAuthors(@Param(value = "type") Integer type);
	
	public void insertTmpNoticeDelay(NoticeDelay nd);
	
	public List<NoticeDelay> queryAllTmpNoticeDelay(Map<String,Object> param);
	
	public void TempNoticeTimerTask();
	
	public List<Notice> queryNotice(Map<String,Object> param);
	
	public List<Notice> queryAllNotice(Map<String,Object> param); 
	
	public List<Notice> queryTmpByToday();
	
	public List<Notice> queryDelayTmpByToday();
	
	public List<NoticeAttachement> queryNoticeAttachementByNoticeId(Integer id);
}
