/**
 * 
 */
package cn.hm.oil.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.hm.oil.dao.SysUsersDao;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.SysMenus;

/**
 * @author randy
 *
 */
public class DefaultUserDetailsService implements UserDetailsService {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private SysUsersDao sysUsersDao;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginUserInfo su = sysUsersDao.queryLoginUserInfoByUsername(username);
		try {
			if (su != null) {
				List<SysMenus> menus = sysUsersDao.queryUserMenusByUsrId(su.getId());
				List<Integer> filterMenus = sysUsersDao.queryMenuFilterByUsrId(su.getId());
				Map<Integer, SysMenus> map = new LinkedHashMap<Integer, SysMenus>();
				Map<Integer, Integer> ot = new HashMap<Integer, Integer>();
				for (SysMenus sm : menus) {
					if (filterMenus.contains(sm.getId()))
						continue;
					
					if (sm.getParent_id().intValue() == 0) {//一级菜单
						map.put(sm.getId(), sm);
						continue;
					}
					
					SysMenus psm = map.get(sm.getParent_id());//获取一级菜单
					if (psm != null) {
						psm.addSubMenus(sm);//设置二级菜单
						ot.put(sm.getId(), sm.getParent_id());
					} else {//三级菜单
						int id = ot.get(sm.getParent_id());//获取一级菜单ID
						for (SysMenus tm : map.get(id).getSubMenus()) {
							if (tm.getId().intValue() == sm.getParent_id().intValue()) {
								tm.addSubMenus(sm);
								break;
							}
						}
					}
				}
				
				List<SysMenus> newMenus = new ArrayList<SysMenus>();
				Iterator<SysMenus> is = map.values().iterator();
				while(is.hasNext()) {
					newMenus.add(is.next());
				}
				su.setMenus(newMenus);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return su;
	}
}
