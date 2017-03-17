/**
 * 
 */
package cn.hm.oil.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.SysMenus;
import cn.hm.oil.domain.SysRolesUsers;

/**
 * @author randy
 * 
 */
public interface SysRolesDao {
	
	public List<SysMenus> queryRoleMenus(@Param(value = "role_id") Integer role_id);
	
	public void insertRoleUser(@Param(value = "role_id") Integer role_id, @Param(value = "user_id") Integer user_id);

	public void deleteRoleUser(@Param(value = "role_id") Integer role_id, @Param(value = "user_id") Integer user_id);

	public SysRolesUsers querySysRolesUsers(@Param(value = "user_id") Integer user_id);
}
