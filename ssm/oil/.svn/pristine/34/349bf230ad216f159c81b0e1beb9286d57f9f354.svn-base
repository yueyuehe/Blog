/**
 * 
 */
package cn.hm.oil.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.SysPushDao;
import cn.hm.oil.dao.TipsDao;
import cn.hm.oil.domain.CheckLog;
import cn.hm.oil.domain.SysPush;
import cn.hm.oil.domain.Tips;
import cn.hm.oil.service.TipsService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.AndroidPushMessageSample;
import cn.hm.oil.util.PageSupport;

/**
 * @author Administrator
 *
 */
@Service(value="tipsService")
public class TipsServiceImpl extends AbstractModuleSuport implements TipsService {

	@Autowired
	private TipsDao tipsDao;
	@Autowired
	private SysPushDao sysPushDao;

	@Override
	public void saveTips(Tips tips) {
		tipsDao.saveTips(tips);
		SysPush push = sysPushDao.queryPush(tips.getUser_id());
		if(push != null) {
			AndroidPushMessageSample.pushNotice(push.getUserId(), push.getChannelId(), "新消息", tips);
		}
		//此处需要查询用户通道id
		//AndroidPushMessageSample.pushNotice("995379719876257548", 3870620768511225045L, "新消息", tips);
	}

	@Override
	public void deleteTipsByid(Integer id) {
		tipsDao.deleteTipsByid(id);
	}

	@Override
	public List<Tips> queryTips(Map<String, Object> param, PageSupport ps) {
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.TipsDao.queryTips", param, ps);
	}
	
	@Override
	public void saveCheckLog(CheckLog cl) {
		if(cl.getId() != null && cl.getId().intValue() > 0){
			tipsDao.updateCheckLog(cl);
		} else{
			tipsDao.insertCheckLog(cl);
		}
	}

	@Override
	public List<CheckLog> queryCheckList(Map<String, Object> param,
			PageSupport ps) {
		if(ps == null){
			return this.getList("cn.hm.oil.dao.TipsDao.queryCheckList", param);
		} else {
			return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.TipsDao.queryCheckList", param, ps);
		}
	}

	@Override
	public CheckLog queryCheckById(Integer id) {
		return tipsDao.queryCheckById(id);
	}

	@Override
	public void deleteCheckLogByid(Integer id) {
		tipsDao.deleteCheckLogByid(id);
	}
	
}
