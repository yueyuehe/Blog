package cn.hm.oil.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.hm.oil.dao.SysRolesDao;
import cn.hm.oil.dao.SysUsersDao;
import cn.hm.oil.domain.FeedBack;
import cn.hm.oil.domain.SpaceTime;
import cn.hm.oil.domain.SysMenus;
import cn.hm.oil.domain.SysRoles;
import cn.hm.oil.domain.SysRolesUsers;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.SysUsersRef;
import cn.hm.oil.domain.UsersLocation;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.PageSupport;

@Service(value="userService")
public class UserServiceImpl extends AbstractModuleSuport implements UserService {

	@Autowired
	private SysUsersDao sysUsersDao;
	
	@Autowired
	private SysRolesDao sysRolesDao;
	
	@Override
	public Integer querySysUserByUsernameID(Integer userId, String username) {
		return sysUsersDao.querySysUserByUsernameID(userId, username);
	}

	@Override
	public void updatePassword(Integer userId, String password) {
		sysUsersDao.updatePassword(userId, password);
	}

	@Override
	public SysUsers querySysUserByUsername(String username) {
		return sysUsersDao.queryLoginUserInfoByUsername(username);
	}

	@Override
	public List<SysMenus> queryRoleMenus(Integer role_id) {
		return sysRolesDao.queryRoleMenus(role_id);
	}

	@Override
	public List<Integer> queryMenuFilterByUsrId(Integer user_id) {
		return sysUsersDao.queryMenuFilterByUsrId(user_id);
	}

	@Override
	public void saveLeaderUser(SysUsers su, String[] removeMenuIds) {
		if (su.getId() != null && su.getId().intValue() > 0) {
			sysUsersDao.deleteMenuFilterByUserId(su.getId());
			sysUsersDao.updateUser(su);
		} else {
			sysUsersDao.insertLeader(su);
			sysRolesDao.insertRoleUser(SysRoles.LEADER, su.getId());
		}
		if (removeMenuIds != null && removeMenuIds.length > 0)
			for (String rm : removeMenuIds) {
				sysUsersDao.insertMenuFilter(su.getId(), Integer.valueOf(rm));
			}
	}

	@Override
	public void deleteUser(Integer id) {
		sysUsersDao.deleteUserById(id);
	}

	@Override
	public List<SysUsers> queryUserByRole(Map<String, Object> param, PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.SysUsersDao.queryUserByRole", "cn.hm.oil.dao.SysUsersDao.queryUserByRole_count", param, ps);
	}

	@Override
	public SysUsers querySysUserById(Integer id) {
		return sysUsersDao.querySysUserById(id);
	}

	@Override
	public Integer queryRoleIdByUserId(Integer id) {
		return sysUsersDao.queryRoleIdByUserId(id);
	}

	@Override
	public SysRolesUsers querySysRolesUsers(Integer user_id) {
		return sysRolesDao.querySysRolesUsers(user_id);
	}

	@Override
	public void saveUser(SysUsers su) {
		sysUsersDao.updateUser(su);
	}

	@Override
	public List<SysUsers> queryWorkerUser(Map<String, Object> param,
			PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.SysUsersDao.queryUserByRole_worker", "cn.hm.oil.dao.SysUsersDao.queryUserByRole_worker_count", param, ps);
	}
	
	@Override
	public void saveLocation(UsersLocation lo) {
		sysUsersDao.saveLocation(lo);
	}

	@Override
	public SpaceTime querySpaceTime() {
		return sysUsersDao.querySpaceTime();
	}

	@Override
	public void saveSpaceTime(SpaceTime time) {
		sysUsersDao.saveSpaceTime(time);
	}

	@Override
	public List<UsersLocation> queryUserLocationByUserId(Map<String,Object> param,PageSupport ps) {
		if(ps==null){
			String id = param.get("id").toString();
			return sysUsersDao.queryUserLocationByUserId(Integer.valueOf(id));
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.SysUsersDao.queryUserLocationByUserId", "cn.hm.oil.dao.SysUsersDao.queryUserLocationByUserId_count", param, ps);
		}
	}
	
	@Override
	public String queryMailAddress(Integer id) {
		return sysUsersDao.queryMailAddress(id);
	}

	@Override
	public void saveFeedBack(FeedBack fb) {
		sysUsersDao.insertFeedBack(fb);
	}

	@Override
	public List<FeedBack> queryFeedBacks(Map<String, Object> param,PageSupport ps) {
		if(ps==null){
			return this.get("cn.hm.oil.dao.SysUsersDao.queryFeedBacks", param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.SysUsersDao.queryFeedBacks", "cn.hm.oil.dao.SysUsersDao.queryFeedBacks_count", param, ps);
		}
	}

	@Override
	public FeedBack queryFeedBackById(Integer id) {
		return sysUsersDao.queryFeedBackById(id);
	}

	@Override
	public void updateFeedBackClose(Integer id, Integer close) {
		sysUsersDao.updateFeedBackClose(id,close);
	}

	static Map<Integer, Integer> userRefMap;
	
	@Override
	public Map<Integer, Integer> getUsersRef() {
		if (userRefMap != null) {
			return userRefMap;
		} else {
			userRefMap = new HashMap<Integer, Integer>();
			List<SysUsersRef> urList = sysUsersDao.querySysUsersRef();
			if (!CollectionUtils.isEmpty(urList)) {
				for (SysUsersRef ur : urList) {
					userRefMap.put(ur.getUser_id(), ur.getUp_user_id());
				}
			}
		}
		return userRefMap;
	}
}
