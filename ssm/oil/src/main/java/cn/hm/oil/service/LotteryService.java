package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.Lottery;




public interface LotteryService {
	public void saveLottery(Lottery lottery);
	
	public Lottery queryLottery(Map<String,Object> param);
	
	public List<Lottery> queryLotteryList(Integer status);
	
	public void updateLottery(String phone);
	
	public void clearLotteryList();
}
