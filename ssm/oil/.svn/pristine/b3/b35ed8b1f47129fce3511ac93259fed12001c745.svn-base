package cn.hm.oil.dao;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.Invest;
import cn.hm.oil.domain.InvestDetail;

public interface InvestDao {
	public void saveInvest(Invest invest);
	
	public void saveInvestDetail(InvestDetail ind);
	
	public Invest queryInvestById(@Param(value="id") Integer id);
	
	public Integer queryOperate_num(@Param(value = "id") Integer id);
	
	public void updateInvest(@Param(value = "id") Integer id);
	
	public void deleteInvestDetail(@Param(value = "iv_id") Integer iv_id);
}
