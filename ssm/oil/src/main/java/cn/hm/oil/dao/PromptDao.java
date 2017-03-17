package cn.hm.oil.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.PromptType;

public interface PromptDao {
	public List<PromptType> queryPromptType();
	
	public Prompt quertPromptByType(@Param(value="type_id") Integer type_id);
	
	public void insertPrompt(Prompt prompt);
	
	public void updatePrompt(Prompt prompt);
}
