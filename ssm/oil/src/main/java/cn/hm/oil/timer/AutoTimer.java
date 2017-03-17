package cn.hm.oil.timer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import cn.hm.oil.domain.CheckLog;
import cn.hm.oil.domain.Event;
import cn.hm.oil.domain.Notice;
import cn.hm.oil.domain.NoticeReceiver;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.EventService;
import cn.hm.oil.service.NewBaseService;
import cn.hm.oil.service.NoticeService;
import cn.hm.oil.service.TipsService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.SettingUtils;
import cn.hm.oil.util.SimpleMailSender;

public class AutoTimer {

	protected Logger log = Logger.getLogger(AutoTimer.class);
	public static int i = 0;

	@Autowired
	private NoticeService noticeService; 
	
	@Autowired
	private TipsService tipsService; 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private BaseService baseService;
	
	@Autowired
	private NewBaseService newBaseService;
	
	public AutoTimer() {
	}

	/**
	 * 定时提交基础资料去审核
	 */
	public void audit(){
		//baseService.updateBaseDataAudit();
		newBaseService.updateAutoSubmitEveryWeek();
	}
	
	public void run() {
		//进行比较，发送短信
		List<Notice> notices = noticeService.queryTmpByToday();
		if (!CollectionUtils.isEmpty(notices)) {
			for (Notice nt : notices) {
				List<String> address = new ArrayList<String>();
				String userMail = userService.queryMailAddress(nt.getUser_id());
				String fuzeMail = userService.queryMailAddress(nt.getFuze());
				if(!StringUtils.isBlank(fuzeMail)){
					address.add(fuzeMail);
				}
				if(!StringUtils.isBlank(userMail)){
					address.add(userMail);
				}
				List<NoticeReceiver> nrs =  noticeService.queryNoticeReceiver(nt.getId());
				if(!CollectionUtils.isEmpty(nrs)){
					for(NoticeReceiver nr : nrs){
						if(!StringUtils.isBlank(nr.getMail())){
							address.add(nr.getMail());
						}
					}
				}
				
				if(!CollectionUtils.isEmpty(address)){
					SimpleMailSender.sendMail(address,"您有督办工作即将过期，请及时处理","您有督办工作即将过期，请及时处理，名称:"+ nt.getTitle());
				}
			}
		}
		
		//更新数据库
		noticeService.TempNoticeTimerTask();
		
		List<Notice> nts = noticeService.queryDelayTmpByToday();
		if(!CollectionUtils.isEmpty(nts)){
			for (Notice nt : nts) {
				CheckLog cl = new CheckLog();
				cl.setType(2);
				cl.setO_id(nt.getId());
				cl.setContent(nt.getTitle());
				cl.setUser_name(nt.getFuze_name());
				cl.setReason("未按期完成");
				cl.setHref(SettingUtils.getCommonSetting("base.url") + "/admin/tmp_notice_create?id="+nt.getId());
				tipsService.saveCheckLog(cl);
			}
		}
	}
	
	public void check(){
		eventService.updateEventDelayByAll();
		//eventService.insertEventDelay();
		eventService.updateEventDelay();
	 	List<Event> evs = eventService.queryEventDelay();
	 	if(!CollectionUtils.isEmpty(evs)){
	 		for(Event e:evs){
	 			CheckLog cl = new CheckLog();
	 			cl.setType(1);
	 			cl.setO_id(e.getId());
	 			cl.setContent(e.getPl_name()+e.getPl_section_name()+e.getPl_spec_name()+e.getKeyword());
	 			cl.setUser_name(e.getCreater());
	 			cl.setReason("未按期完成");
	 			cl.setHref(SettingUtils.getCommonSetting("base.url") + "/admin/event/detail_show?id="+e.getId());
	 			tipsService.saveCheckLog(cl);
	 		}
	 	}
	}
}
