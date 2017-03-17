package cn.hm.oil.service;

import java.util.List;

import cn.hm.oil.domain.SysPush;

public interface PushService {
	public void savePush(SysPush sysPush);
	
	public SysPush queryPush(Integer user_id);
	
	public void updatePush(SysPush sysPush);
}
