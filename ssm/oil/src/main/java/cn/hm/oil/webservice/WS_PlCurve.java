package cn.hm.oil.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.LoginUserInfo;
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.service.BaseService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.Constants;
import cn.hm.oil.util.DrawPic;
import cn.hm.oil.util.JsonUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

/**
 * 管道保护电位曲线图
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/services/base")
public class WS_PlCurve {
	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;
	
	/**
	 * 管道保护电位曲线图 列表
	 * @param model
	 * @param request
	 * @param pl
	 * @param section
	 * @param spec
	 * @param c_month
	 * @return
	 */
	@RequestMapping(value = "/pl_curve/list", method = { RequestMethod.GET,RequestMethod.POST })
	public @ResponseBody JsonResWrapper plCurve_list(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer pl, @RequestParam(required = false) Integer section,
			@RequestParam(required = false) Integer spec, @RequestParam(required = false) String c_month,
			@RequestParam(required=false) Integer status) {
		JsonResWrapper response = new JsonResWrapper();
		Map<String, Object> param = new HashMap<String, Object>();
		if (pl != null && pl.intValue() > 0) {
			param.put("pl_id", pl);
		}
		if (section != null && section.intValue() > 0) {
			param.put("pl_section_id", section);
		}
		if (spec != null && spec.intValue() > 0) {
			param.put("pl_spec_id", spec);
		}

		if (!StringUtils.isBlank(c_month)) {
			param.put("c_month", Integer.valueOf(c_month.replace("-", "")));
		}

		if(status != null) {
			param.put("status", status);
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		List<PotentialCurve> pcList = baseService
				.queryPotentialCurve(param, ps);
		response.setStatus(ResponseStatus.OK);
		response.setData(pcList);
		response.setPageOffset(ps.getPageOffset());
		response.setPageSize(ps.getPageSize());
		response.setTotalRecord(ps.getTotalRecord());
		return response;
	}
	
	/**
	 * 管道保护电位曲线图保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pl_curve/save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper plCurve_save(HttpServletRequest request, @RequestParam Integer pl, @RequestParam Integer section, @RequestParam Integer spec,
			@RequestParam String unit, @RequestParam String c_month, @RequestParam String[] no, @RequestParam Float[] p_early,
			@RequestParam Float[] p_end, @RequestParam String[] measurer, @RequestParam String[] remark,
			@RequestParam(required = false) Integer id) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		PotentialCurve pc = new PotentialCurve();
		pc.setC_month(Integer.valueOf(c_month.replace("-", "")));
		pc.setPl_id(pl);
		pc.setPl_section_id(section);
		pc.setPl_spec_id(spec);
		pc.setUnit(unit);
		pc.setCreater(ud.getId());
		if(id != null && id.intValue() > 0) {
			pc.setId(id);
		}
		
		List<PotentialCurveDetail> pcdList = new ArrayList<PotentialCurveDetail>();
		int i = 0;
		for (String n : no) {
			PotentialCurveDetail pcd = new PotentialCurveDetail();
			if (!StringUtils.isBlank(n)) {
				pcd.setNo(n);
				pcd.setMeasurer(measurer[i]);
				pcd.setP_early(p_early[i]);
				pcd.setP_end(p_end[i]);
				pcd.setRemark(remark[i]);

				pcdList.add(pcd);
			}
			i++;
		}

		List<Double> p_earlyList = new ArrayList<Double>();
		List<Double> p_endList = new ArrayList<Double>();

		// 获取点电位数据
		for (PotentialCurveDetail pcd : pcdList) {
			p_earlyList.add((double) pcd.getP_early());
			p_endList.add((double) pcd.getP_end());
		}

		// 生成电位图
		CategoryDataset dataset = DrawPic.createDataset(p_earlyList, p_endList);
		JFreeChart chart = DrawPic.createChart(dataset);
		String chartPath = DrawPic.saveAsFile(pc.getPl_id() + "-" + pc.getPl_section_id() + "-" + pc.getPl_spec_id() + "-" + pc.getC_month(), chart);

		String status = "0";
		if(chartPath == null) {
			response.setStatus(ResponseStatus.FAILED);
			response.setMessage("生成曲线图失败");
		} else {
			pc.setChartPath(chartPath);
			if (pcdList.size() > 0) {
				baseService.savePlCurve(pc, pcdList);
				response.setStatus(ResponseStatus.OK);
				response.setMessage("保存成功");
			} else {
				response.setStatus(ResponseStatus.FAILED_PARAM_LOST);
				response.setMessage("参数不全");
			}
		}
		
		return response;
	}
	
	/**
	 * 管道保护电位曲线图详细
	 * @param pc_id
	 * @return
	 */
	@RequestMapping(value="/pl_curve/show")
	public @ResponseBody JsonResWrapper plCurveShow(@RequestParam Integer pc_id) {
		JsonResWrapper response = new JsonResWrapper();
		PotentialCurve pc = baseService.queryPotentialCurveById(pc_id);
		List<PotentialCurveDetail> pcdList = pc.getDetailList();
		List<Double> p_earlyList = new ArrayList<Double>();
		List<Double> p_endList = new ArrayList<Double>();

		// 获取点电位数据
		for (PotentialCurveDetail pcd : pcdList) {
			p_earlyList.add(pcd.getP_early().doubleValue());
			p_endList.add(pcd.getP_end().doubleValue());
		}
		CategoryDataset dataset = DrawPic.createDataset(p_earlyList, p_endList);
		JFreeChart chart = DrawPic.createChart(dataset);
		String b = DrawPic.saveAsFile(pc.getPl_name()+pc.getPl_section_name()+pc.getPl_spec_name(), chart);
		String imgurl = SettingUtils.getCommonSetting("base.image.url");
		pc.setChartPath(imgurl + pc.getChartPath());
		response.setStatus(ResponseStatus.OK);
		response.setData(pc);
		return response;
	}
	
	/**
	 * 管道保护电位曲线图 审核
	 * @param request
	 * @param id
	 * @param status
	 * @param verify_desc
	 * @return
	 */
	@RequestMapping(value="/pl_curve/verify_save", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper plCurve_verify_save(HttpServletRequest request,
			@RequestParam Integer id, @RequestParam Integer status,
			@RequestParam String verify_desc) {
		JsonResWrapper response = new JsonResWrapper();
		SysUsers ud = (SysUsers) request.getSession().getAttribute(Constants.SESSION_APP_LOGIN_USER);
		if(ud == null) {
			response.setStatus(ResponseStatus.FAILED_NOT_EXISTS);
			response.setMessage("请先登录");
			return response;
		}
		baseService.updatePotentialCurveVerifyStatus(id, ud.getId(), status, verify_desc);
		response.setStatus(ResponseStatus.OK);
		response.setMessage("审核成功");
		return response;
	}

}
