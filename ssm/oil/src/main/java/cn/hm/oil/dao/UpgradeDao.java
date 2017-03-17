/**
 * 
 */
package cn.hm.oil.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.Upgrade;





/**
 * @author Administrator
 * 
 */
public interface UpgradeDao {
	public Upgrade selectUpgradeByos(@Param(value = "os") String os);

	public void insertUpgrade(Upgrade upgrade);

	public void updateUpgrade(Upgrade upgrade);
	
	public List<Upgrade> selectAllUpgrade();
}
