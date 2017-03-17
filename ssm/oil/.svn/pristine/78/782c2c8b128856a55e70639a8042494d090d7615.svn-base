/**
 * 
 */
package cn.hm.oil.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.FeedBack;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.SpaceTime;
import cn.hm.oil.domain.SysMenus;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.domain.SysUsersRef;
import cn.hm.oil.domain.UsersLocation;

/**
 * @author Administrator
 * 
 */
public interface SysUsersDao {

	/**
	 * 跟具用户id获取角色id
	 * @param id
	 * @return
	 */
	public Integer queryRoleIdByUserId(Integer id);
	
	/**
	 * 验证username是否存在
	 * 
	 * @param userId
	 * @param username
	 * @return
	 */
	public Integer querySysUserByUsernameID(@Param(value = "id") Integer userId, @Param(value = "username") String username);
	
	/**
	 * 更新用户的密码
	 * 
	 * @param userId
	 * @param password
	 */
	public void updatePassword(@Param(value = "userId") Integer userId, @Param(value = "password") String password);
	
	public void insertLeader(SysUsers sysUsers);
	
	public void updateUser(SysUsers sysUsers);
	
	public void insertMenuFilter(@Param(value = "user_id") Integer user_id, @Param(value = "menu_id") Integer menu_id);
	
	public void deleteMenuFilterByUserId(@Param(value = "user_id") Integer user_id);
	
	public void insertWorker(SysUsers sysUsers);
	
	// 登录操作所需
	public LoginUserInfo queryLoginUserInfoByUsername(@Param(value = "username") String username);

	public void updateLoginInfor(@Param(value = "userId") Integer id, @Param(value = "ipAdrr") String ipAdrr);

	public List<SysMenus> queryUserMenusByUsrId(@Param(value = "userId") Integer user_id);

	public List<Integer> queryMenuFilterByUsrId(@Param(value = "userId") Integer user_id);
	// 登录操作所需END
	
	public Integer queryOne();
	
	public void deleteUserById(@Param(value = "id") Integer id);
	
	public SysUsers querySysUserById(@Param(value = "id") Integer id);
	
	public void insertLoginLog(@Param(value = "user_id") Integer id, @Param(value = "ip") String ip);
	
	public List<SysUsers> querySysUserByLevel(@Param(value = "level") Integer level);
	
	public void saveLocation(UsersLocation lo);
	
	public SpaceTime querySpaceTime();
	
	public void saveSpaceTime(SpaceTime time);
	
	public List<UsersLocation> queryUserLocationByUserId(@Param(value="id")Integer id);
	
	public String queryMailAddress(@Param(value="id") Integer id);
	
	/**
	 * 保存反馈意见
	 * @param fb
	 */
	public void insertFeedBack(FeedBack fb);
	
	
	
	/**
	 * 查看反馈意见详情
	 * @param fb
	 */
	public FeedBack queryFeedBackById(Integer id);
	
	/**
	 * 修改反馈意见状态
	 * @param fb
	 */
	public void updateFeedBackClose(@Param(value="id")Integer id,@Param(value="close")Integer close);
	
	public List<SysUsersRef> querySysUsersRef();
}
