package cn.hm.oil.dao;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.Capital;
import cn.hm.oil.domain.CapitalDetail;
import cn.hm.oil.domain.Invest;

public interface CapitalDao {
	public void saveCapital(Capital capital);
	
	public void saveCapitalDetail(CapitalDetail capitalDetail);
	
	public Capital queryCapitalById(@Param(value="id") Integer id);
	
	public Integer queryOperate_num(@Param(value="id") Integer id);
	
	public void updateCapital(@Param(value="id") Integer id);
	
	public void deleteCapitalDetail(@Param(value="ca_id") Integer ca_id);
}
