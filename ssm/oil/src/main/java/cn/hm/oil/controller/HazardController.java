package cn.hm.oil.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.domain.Hazard;
import cn.hm.oil.service.HazardService;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;

@Controller
public class HazardController {

	@Autowired
	private HazardService excelService;

	/**
	 * 文件上传
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @param chunk
	 * @param chunks
	 * @param pw
	 */
	@RequestMapping(value = "/admin/uploadHazFile", method = RequestMethod.POST)
	public void uploadFile(Model model, HttpServletRequest request,
			@RequestParam String name,
			@RequestParam(required = false) Integer chunk,
			@RequestParam(required = false) Integer chunks, PrintWriter pw) {
		try {
			DataUtil.uploadFileForHaz(request, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.write(name);
		pw.flush();
	}

	@RequestMapping(value = "/admin/list_hazard")
	public String list_excel(Model model,
			@RequestParam(required = false) String error) {
		return "pages/base/list_hazard";
	}

	/**
	 * 将文件信息储存进数据库
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/import_hazard", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String import_user(Model model,
			HttpServletRequest request, @RequestParam String name) {
		Hazard hazard = new Hazard();
		String[] filename = name.split(";");
		for (String fn : filename) {
			if (!StringUtils.isBlank(fn)) {
				String[] filestr = fn.split("@");
				String filetitle = filestr[2].substring(0,filestr[2].lastIndexOf("."));
				String ext = filestr[2].substring(filestr[2].lastIndexOf(".")+1);
				hazard.setTitle(filetitle);
				hazard.setFiletype(ext);
				hazard.setFilesize(DataUtil.FormateSize(Integer
						.parseInt(filestr[1])));
				hazard.setFilepath(filestr[0] + "." + ext);

				excelService.saveHazard(hazard);
			}
		}

		model.addAttribute("msg", "数据导入成功！");
		return "success";
	}

	/**
	 * 获取文件列表
	 * 
	 */
	@RequestMapping(value = "/list_hazard", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String list_user(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer delete,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false) String searchName,
			@RequestParam(required = false) String start_time,
			@RequestParam(required = false) String end_time) {
		if (delete != null && delete.intValue() > 0) {
			model.addAttribute("msg", "删除成功！");
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		if (!StringUtils.isBlank(searchName)) {
			param.put("title", searchName);
			model.addAttribute("searchName", searchName);
		}
		if (!StringUtils.isBlank(start_time)) {
			param.put("start_time", start_time);
			param.put("end_time", end_time);
			model.addAttribute("start_time", start_time);
			model.addAttribute("end_time", end_time);
			System.out.println("------------------" + start_time);
		}
		List<Hazard> hazard = excelService.queryHazard(param, ps);
		model.addAttribute("excel", hazard);

		model.addAttribute("type", type);
		return "pages/base/list_hazard";
	}

	/**
	 * 删除文件
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del_hazard", method = RequestMethod.GET)
	public String del_excel(Model model, @RequestParam Integer[] id) {
		// List<Integer> ids = Arrays.asList(id);
		// 进行删除
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path"+"/hazard");
		for (Integer did : id) {
			Hazard excel = excelService.queryHazardByid(did);

			DataUtil.deleteFile(fileDir + sep + excel.getFilepath());
			excelService.deleteHazardByid(did);
		}
		return "redirect:/list_hazard?delete=1";
	}

	/**
	 * 文件下载
	 * 
	 * @param model
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/export_hazard", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void download(Model model, HttpServletResponse response,
			@RequestParam(required = false) Integer id) {
		Hazard sexcel = excelService.queryHazardByid(id);
		// 文件位置
		String filePath = "/home/oil/upload/hazard/";
		String FileName = sexcel.getTitle();
		// System.out.println(FileName);
		String path = sexcel.getFilepath();
		String[] fp = path.split("\\.");
		DataUtil.downLoad(filePath + path, response, FileName + "." + fp[1]);
	}

}
