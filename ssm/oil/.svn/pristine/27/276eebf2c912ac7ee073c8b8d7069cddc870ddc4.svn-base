/**
 * 
 */
package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.FeedBack;
import cn.hm.oil.domain.SpaceTime;
import cn.hm.oil.domain.SysMenus;
import cn.hm.oil.domain.SysRolesUsers;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.UsersLocation;
import cn.hm.oil.util.PageSupport;

/**
 * @author Administrator
 *
 */
public interface UserService {
	
	public List<SysUsers> queryUserByRole(Map<String, Object> param, PageSupport ps);
	
	public List<SysUsers> queryWorkerUser(Map<String, Object> param, PageSupport ps);
	
	public void saveLeaderUser(SysUsers su, String [] removeMenuIds);
	
	public void saveUser(SysUsers su);
	
	public void deleteUser(Integer id);
	
	public List<Integer> queryMenuFilterByUsrId(Integer user_id);
	
	public SysUsers querySysUserById(Integer id);
	/**
	 * 查询用户的角色id
	 * @param id
	 * @return
	 */
 	public Integer queryRoleIdByUserId(Integer id);
	
	/**
	 * 查询角色下的所有菜单
	 * 
	 * @param role_id
	 * @return
	 */
	public List<SysMenus> queryRoleMenus(Integer role_id);
	
	/**
	 * 验证username是否存在
	 * 
	 * @param userId
	 * @param username
	 * @return
	 */
	public Integer querySysUserByUsernameID(Integer userId, String username);
	
	/**
	 * 更新用户的密码
	 * 
	 * @param userId
	 * @param password
	 */
	public void updatePassword(Integer userId, String password);
	
	/**
	 * 根据登录名查询用户信息
	 * 
	 * @param username
	 * @return
	 */
	public SysUsers querySysUserByUsername(String username);
	
	/**
	 * 查找用户类型
	 * @param user_id
	 * @return
	 */
	public SysRolesUsers querySysRolesUsers(Integer user_id);
	
	/**
	 * 保存地理信息
	 * @param lo
	 */
	public void saveLocation(UsersLocation lo);
	
	public SpaceTime querySpaceTime();
	
	public void saveSpaceTime(SpaceTime time);
	
	/**
	 * 保存反馈意见
	 * @param fb
	 */
	public void saveFeedBack(FeedBack fb);
	
	/**
	 * 查看反馈意见
	 * @param fb
	 */
	public List<FeedBack> queryFeedBacks(Map<String,Object> param,PageSupport ps);
	
	/**
	 * 查看反馈意见详情
	 * @param fb
	 */
	public FeedBack queryFeedBackById(Integer id);
	
	/**
	 * 修改反馈意见状态
	 * @param fb
	 */
	public void updateFeedBackClose(Integer id,Integer close);
	
	public List<UsersLocation> queryUserLocationByUserId(Map<String,Object> param,PageSupport ps);
	
	/**
	 * 查询用户的邮箱地址
	 * @param id
	 * @return
	 */
	public String queryMailAddress(Integer id);
	
	public Map<Integer, Integer> getUsersRef();
}
