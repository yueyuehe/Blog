package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.HighConsequence;
import cn.hm.oil.domain.HighConsequenceDetail;

public interface BaseHighConsequenceDao {
	public void insertHighConsequence(HighConsequence highConsequence);
	
	public void updateHighConsequence(HighConsequence highConsequence);
	
	public void deleteHighConsequenceDetailByHcId(@Param(value="hc_id") Integer hc_id);
	
	public void insertHighConsequenceDetail(HighConsequenceDetail highConsequenceDetail);

	public List<HighConsequence> queryHighConsequence(Map<String, Object> param);
	
	public HighConsequence queryHighConsequenceById(@Param(value="id") Integer id);
	
	public void updateHighConsequenceVerifyStatus(@Param(value="id") Integer id,@Param(value="verifier") Integer verifier,@Param(value="status") Integer status,@Param(value="verify_desc") String verify_desc);
}
