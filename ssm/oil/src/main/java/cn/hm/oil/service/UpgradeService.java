/**
 * 
 */
package cn.hm.oil.service;

import java.util.List;

import cn.hm.oil.domain.Upgrade;





/**
 * @author Administrator
 * 
 */
public interface UpgradeService {
	public Upgrade selectUpgradeByos(String os);

	public void saveUpgrade(Upgrade upgrade);
	
	public List<Upgrade> selectAllUpgrade();
}
