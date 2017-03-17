package cn.hm.oil.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.hm.oil.dao.NoticeDao;
import cn.hm.oil.dao.SysPushDao;
import cn.hm.oil.dao.SysUsersDao;
import cn.hm.oil.dao.TipsDao;
import cn.hm.oil.domain.Notice;
import cn.hm.oil.domain.NoticeAttachement;
import cn.hm.oil.domain.NoticeDelay;
import cn.hm.oil.domain.NoticeRead;
import cn.hm.oil.domain.NoticeReceiver;
import cn.hm.oil.domain.NoticeRecord;
import cn.hm.oil.domain.NoticeReply;
import cn.hm.oil.domain.Readed;
import cn.hm.oil.domain.SysPush;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.Tips;
import cn.hm.oil.service.NoticeService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.AndroidPushMessageSample;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.DateFormatter;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SimpleMailSender;

@Service(value="noticeService")
public class NoticeServiceImpl extends AbstractModuleSuport implements NoticeService {

	@Autowired
	private NoticeDao noticeDao;
	
	@Autowired
	private SysUsersDao sysUsersDao;
	
	@Autowired
	private TipsDao tipsDao;
	
	@Autowired
	private SysPushDao sysPushDao;
	
	@Override
	public void insertPublicNotice(Notice notice) {
		if(notice.getId()!=null){
			Notice n = noticeDao.queryPublicNoticeDetailById(notice.getId());
			if(!StringUtils.isBlank(n.getPath())&&!StringUtils.isBlank(notice.getPath())){
				try {
					DataUtil.moveFileToDeleteFileDir(n.getPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			noticeDao.updatePublicNotice(notice);
		} else{
			noticeDao.insertPublicNotice(notice);
		}
	}

	@Override
	public Notice queryPublicNoticeDetailById(Integer id) {
		return noticeDao.queryPublicNoticeDetailById(id);
	}

	@Override
	public void saveTempNotice(Notice notice, List<Integer> receivers,Integer publish_id) {
		SysUsers su = sysUsersDao.querySysUserById(publish_id);
		if(notice.getId()!=null && notice.getId().intValue() > 0){
			Notice n = noticeDao.queryTempNoticeDetailById(notice.getId());
			if(receivers.size()!=0){
				noticeDao.deleteReservers(notice.getId());
				saveRecord(su.getName()+"清空了督办工作参与人",publish_id, notice.getId());
			}
			
			if(!StringUtils.isBlank(n.getPath())&&!StringUtils.isBlank(notice.getPath())){
				try {
					DataUtil.moveFileToDeleteFileDir(n.getPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			saveRecord(su.getName()+"修改了督办工作:"+n.getTitle(),publish_id, notice.getId());
			if(n.getFuze()!=notice.getFuze()){
				saveTips("您被选为督办工作:"+n.getTitle()+"负责人，请及时处理！", notice.getFuze(), notice.getId(), 2);
				saveRecord(su.getName()+"变更了督办工作负责人为"+notice.getFuze_name(),publish_id, notice.getId());
			}
			noticeDao.updateTempNotice(notice);
		} else {
			noticeDao.insertTempNotice(notice);
			saveTips("您被选为督办工作:"+notice.getTitle()+"负责人，请及时处理！", notice.getFuze(), notice.getId(), 2);
			saveRecord(su.getName()+"创建了督办工作",publish_id, notice.getId());
			saveRecord(su.getName()+"指定了督办工作负责人为"+notice.getFuze_name(),publish_id, notice.getId());
		}
		
		
		if(receivers.size()!=0){
			String re_name="";
			for (Integer user_id : receivers) {
				SysUsers ss = sysUsersDao.querySysUserById(user_id);
				saveTips("您有收到新的督办:"+notice.getTitle()+"通知，请及时处理！", user_id, notice.getId(), 2);
				NoticeReceiver nr = new NoticeReceiver();
				nr.setNotice_id(notice.getId());
				nr.setUser_id(user_id);
				nr.setPublish_id(0);
				noticeDao.insertNoticeReceiver(nr);
				re_name += ss.getName()+" ";
			}
			saveRecord(su.getName()+"指定了督办工作参与人:"+re_name,publish_id, notice.getId());
		}
	}
	
	public void saveAskNotice(Notice notice, List<Integer> receivers){
		if(notice.getId()!=null && notice.getId().intValue() > 0){
			noticeDao.deleteReservers(notice.getId());
			/*Notice n = noticeDao.queryAskNoticeById(notice.getId());
			if(!StringUtils.isBlank(n.getPath())&&!StringUtils.isBlank(notice.getPath())){
				try {
					DataUtil.moveFileToDeleteFileDir(n.getPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			noticeDao.updateAskNotice(notice);
		} else {
			noticeDao.insertAskNotice(notice);
			for (Integer user_id : receivers) {
				saveTips("您有收到新的工作请示:"+notice.getTitle()+"，请及时审核！", user_id, notice.getId(), 4);
				NoticeReceiver nr = new NoticeReceiver();
				nr.setNotice_id(notice.getId());
				nr.setUser_id(user_id);
				noticeDao.insertNoticeReceiver(nr);
			}
		}
		
		if (!CollectionUtils.isEmpty(notice.getFileNames())) {
			for(String fn : notice.getFileNames()) {
				noticeDao.insertNoticeAttachement(notice.getId(), fn);
			}
		}
		
		
	}

	@Override
	public Notice queryTempNoticeDetailById(Integer id) {
		return noticeDao.queryTempNoticeDetailById(id);
	}

	@Override
	public List<NoticeReceiver> queryNoticeReceiver(Integer notice_id) {
		return noticeDao.queryNoticeReceiver(notice_id);
	}

	@Override
	public List<NoticeReceiver> queryWorkerList(Integer pl_id, Integer pl_section_id) {
		return noticeDao.queryWorkerList(pl_id, pl_section_id);
	}

	@Override
	public void insertNoticeReply(NoticeReply noticeReply) {
		if(noticeReply.getId()!=null && noticeReply.getId().intValue() > 0){
			NoticeReply nr = noticeDao.queryNoticeReplyById(noticeReply.getId());
			if(!StringUtils.isBlank(nr.getPath())&&!StringUtils.isBlank(noticeReply.getPath())){
				try {
					DataUtil.moveFileToDeleteFileDir(nr.getPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			noticeDao.updateNoticeReply(noticeReply);
		} else {
			/*Notice e = noticeDao.queryPublicNoticeDetailById(noticeReply.getNotice_id());
			if (e.getUser_id().intValue() != noticeReply.getUser_id().intValue()) {
				saveTips("您发布的督办工作"+e.getTitle()+"有了新回复！", e.getUser_id(), noticeReply.getNotice_id(), e.getType());
			}*/
			noticeDao.insertNoticeReply(noticeReply);
			noticeDao.updateLastReplyTime(noticeReply.getNotice_id());
			SysUsers su = sysUsersDao.querySysUserById(noticeReply.getUser_id());
			saveRecord(su.getName()+"回复了督办工作",su.getId(),noticeReply.getNotice_id());
		}
		
		
	}

	@Override
	public List<Notice> queryNotice(Map<String, Object> param, PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.NoticeDao.queryNotice", "cn.hm.oil.dao.NoticeDao.queryNotice_count", param, ps);
	}

	@Override
	public List<NoticeReply> queryNoticeReplyByNoticeId(Integer notice_id) {
		return noticeDao.queryNoticeReplyByNoticeId(notice_id);
	}

	@Override
	public NoticeRead queryNoticeRead(Integer notice_id, Integer user_id) {
		return noticeDao.queryNoticeRead(notice_id, user_id);
	}

	@Override
	public void insertNoticeRead(NoticeRead noticeRead) {
		noticeDao.insertNoticeRead(noticeRead);
	}

	@Override
	public List<Readed> queryNoticeReadList(Map<String, Object> param,
			PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.NoticeDao.queryNoticeReadList", "cn.hm.oil.dao.NoticeDao.queryNoticeReadList_count", param, ps);
	}

	@Override
	public void updateNoticeStatus(Integer id, Integer status,SysUsers su) {
		noticeDao.updateNoticeStatus(id, status);
		if(status==0){
			saveRecord(su.getName()+"关闭了督办工作",su.getId(),id);
		} else {
			saveRecord(su.getName()+"打开了督办工作",su.getId(),id);
		}
	}
	
	@Override
	public List<NoticeReceiver> queryLeaderList() {
		return noticeDao.queryLeaderList();
	}
	
	@Override
	public Notice queryAskNotice(Integer id) {
		return noticeDao.queryAskNoticeById(id);
	}

	@Override
	public void saveVerify(Notice notice) {
		Notice e = noticeDao.queryPublicNoticeDetailById(notice.getId());
		String con = "";
		if (notice.getVerify_status().intValue() == 1) {
			con = "审核通过！";
		} else {
			con = "未审核通过！";
		}
		saveTips("您提交的工作请示:"+ e.getTitle() + con + ", 请及时处理！", e.getUser_id(), notice.getId(), 1);
		noticeDao.saveVerify(notice);
	}
	
	private void saveTips(String message, Integer user_id, Integer notice_id, int type) {
		Tips tips = new Tips();
		tips.setContent(message);
		tips.setUser_id(user_id);
		if (type == 1)
			tips.setUrl("/admin/notice_create?id=" + notice_id);
		else if (type == 2)
			tips.setUrl("/admin/tmp_notice_create?id=" + notice_id);
		else if (type == 3)
			tips.setUrl("/admin/ask_notice_verify?id=" + notice_id);
		else
			tips.setUrl("/admin/ask_notice_verify?verify=1&id=" + notice_id);
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
	
	private void saveRecord(String message, Integer user_id, Integer notice_id) {
		NoticeRecord nr = new NoticeRecord();
		nr.setContent(message);
		nr.setN_id(notice_id);
		nr.setUser_id(user_id);
		noticeDao.saveRecord(nr);
	}


	@Override
	public void deleteTips(Integer id) {
		tipsDao.deleteTipsByid(id);
	}

	@Override
	public void updateNoticeClose(Integer id, Integer close) {
		noticeDao.updateNoticeClose(id,close);
		
	}

	@Override
	public List<Notice> queryAllNotice(Map<String, Object> param, PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.NoticeDao.queryAllNotice", "cn.hm.oil.dao.NoticeDao.queryAllNotice_count", param, ps);
	}

	@Override
	public NoticeReply queryNoticeReplyById(Integer id) {
		return noticeDao.queryNoticeReplyById(id);
	}

	@Override
	public void deleteNoticeReplyById(Integer id) {
		noticeDao.deleteNoticeReplyById(id);
	}

	@Override
	public List<SysUsers> queryNoticeAuthors(Integer type) {
		return noticeDao.queryNoticeAuthors(type);
	}

	@Override
	public void updateNoticeAsk(Integer id, Integer ask,SysUsers su) {
		noticeDao.updateNoticeAsk(id,ask);
		Notice n = noticeDao.queryTempNoticeDetailById(id);
		saveTips("您发布的督办工作:"+n.getTitle()+"正在申请关闭，请及时处理！", n.getUser_id(), id, 2);
		SysUsers ss = sysUsersDao.querySysUserById(n.getUser_id());
		saveRecord(su.getName()+"向"+ss.getName()+"提交关闭督办工作申请",su.getId(),id);
	}

	@Override
	public void addTempNoticeRecevers(Integer id, List<Integer> receivers,List<Integer> cancel_receivers,Integer publish_id) {
		SysUsers su = sysUsersDao.querySysUserById(publish_id);
		Notice notice = noticeDao.queryTempNoticeDetailById(id);
		if(notice.getFuze()==publish_id){
			publish_id=1;
		} else{
			publish_id=0;
		}
		
		if(!CollectionUtils.isEmpty(cancel_receivers)){
			String ca_name="";
			for (Integer user_id : cancel_receivers) {
				SysUsers ss = sysUsersDao.querySysUserById(user_id);
				noticeDao.deleteCancelReservers(id,user_id);
				ca_name += ss.getName()+" ";
			}
			saveRecord(su.getName()+"取消了督办工作参与人:"+ca_name,su.getId(),id);
		}
		
		
		if(!CollectionUtils.isEmpty(receivers)){
			String re_name="";
			for (Integer user_id : receivers) {
				SysUsers ss = sysUsersDao.querySysUserById(user_id);
				saveTips("您有收到新的督办:"+notice.getTitle()+"通知，请及时处理！", user_id, notice.getId(), 2);
				NoticeReceiver nr = new NoticeReceiver();
				nr.setNotice_id(notice.getId());
				nr.setUser_id(user_id);
				nr.setPublish_id(publish_id);
				noticeDao.insertNoticeReceiver(nr);
				re_name += ss.getName()+" ";
			}
			saveRecord(su.getName()+"增加了督办工作参与人:"+re_name,su.getId(),id);
		}
	}

	@Override
	public void saveTmpNoticeDelay(NoticeDelay nd,SysUsers su) {
		Notice notice = noticeDao.queryTempNoticeDetailById(nd.getN_id());
		saveTips("您发布的督办工作:"+notice.getTitle()+"正在申请延期，请及时处理！", notice.getUser_id(), nd.getN_id(), 2);
		noticeDao.insertTmpNoticeDelay(nd);
		SysUsers ss = sysUsersDao.querySysUserById(notice.getUser_id());
		saveRecord(su.getName()+"向"+ss.getName()+"提交了督办工作延期申请",su.getId(),nd.getN_id());
	}

	@Override
	public List<NoticeDelay> queryAllTmpNoticeDelay(Map<String,Object> param) {
		return noticeDao.queryAllTmpNoticeDelay(param);
	}

	@Override
	public void updateTmpNoticeDelayVerify(Integer id, Integer verify_delay,
			String verify_reason,SysUsers su) {
		Notice notice = noticeDao.queryTempNoticeDetailById(id);
		noticeDao.updateTmpNoticeDelayVerify(id,verify_delay,verify_reason);
		if(verify_delay==1){
			saveRecord(su.getName()+"同意了督办工作:"+notice.getTitle()+"延期申请",su.getId(),id);
		} else{
			saveRecord(su.getName()+"不同意督办工作:"+notice.getTitle()+"延期申请",su.getId(),id);
		}
	}

	@Override
	public void TempNoticeTimerTask() {
		noticeDao.TempNoticeTimerTask();
	}
	
	@Override
	public List<Notice> queryNotice(Map<String,Object> param) {
		return noticeDao.queryNotice(param);
	}

	@Override
	public List<Notice> queryAllNotice(Map<String,Object> param) {
		return noticeDao.queryAllNotice(param);
	}

	@Override
	public void updateNoticeOverTime(Integer id, Date over_time,SysUsers su) {
		noticeDao.updateNoticeOverTime(id,over_time);
		noticeDao.updateNoticeIsDelay(id, over_time);
		saveRecord(su.getName()+"修改了督办工作完成时间为:"+DateFormatter.dateToString(over_time, "yyyy-MM-dd"),su.getId(),id);
	}

	@Override
	public void updateNoticeFuze(Integer id, Integer fuze_id,SysUsers su) {
		Notice notice = noticeDao.queryTempNoticeDetailById(id);
		saveTips("您被选为督办工作:"+notice.getTitle()+"负责人，请及时处理！",fuze_id, id, 2);
		noticeDao.updateNoticeFuze(id,fuze_id);
		SysUsers ss = sysUsersDao.querySysUserById(fuze_id);
		saveRecord(su.getName()+"变更了督办工作负责人为"+ss.getName(),su.getId(),id);
	}
	
	@Override
	public List<Notice> queryTmpByToday() {
		return noticeDao.queryTmpByToday();
	}

	@Override
	public void saveRecords(String content, Integer user_id, Integer n_id) {
		saveRecord(content,user_id,n_id);
	}

	@Override
	public List<NoticeRecord> queryAllNoticeRecord(Map<String, Object> param,
			PageSupport ps) {
		if(ps==null){
			return this.getList("cn.hm.oil.dao.NoticeDao.queryAllNoticeRecord", param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.NoticeDao.queryAllNoticeRecord", "cn.hm.oil.dao.NoticeDao.queryAllNoticeRecord_count", param, ps);
		}
	}

	@Override
	public List<Notice> queryDelayTmpByToday() {
		return noticeDao.queryDelayTmpByToday();
	}

	@Override
	public List<NoticeAttachement> queryNoticeAttachementByNoticeId(Integer id) {
		return noticeDao.queryNoticeAttachementByNoticeId(id);
	}
	
}
