package cn.hm.oil.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.SysPushDao;
import cn.hm.oil.domain.SysPush;
import cn.hm.oil.service.PushService;

@Service(value="pushService")
public class PushServiceImpl implements PushService {
	@Autowired
	private SysPushDao sysPushDao;
	
	@Override
	public void savePush(SysPush sysPush) {
		// TODO Auto-generated method stub
		if(sysPush.getId() == null) {
			sysPushDao.savePush(sysPush);
		} else {
			sysPushDao.updatePush(sysPush);
		}
	}

	@Override
	public SysPush queryPush(Integer user_id) {
		// TODO Auto-generated method stub
		return sysPushDao.queryPush(user_id);
	}

	@Override
	public void updatePush(SysPush sysPush) {
		// TODO Auto-generated method stub
		sysPushDao.updatePush(sysPush);
	}

}
