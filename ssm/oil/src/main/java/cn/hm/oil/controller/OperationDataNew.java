package cn.hm.oil.controller;

import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.hm.oil.domain.Sexcel;
import cn.hm.oil.domain.SysRolesUsers;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.ExcelService;
import cn.hm.oil.service.UserService;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SettingUtils;


@Controller

@RequestMapping(value = "/admin/new/excel")
public class OperationDataNew {
	
	@Autowired
	private ExcelService excelService;
	
	@Autowired
	private UserService userService;
	
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
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(Model model, HttpServletRequest request,
			@RequestParam String name, @RequestParam(required=false) Integer chunk, @RequestParam(required=false) Integer chunks,
			PrintWriter pw) {
		try {
			DataUtil.uploadFileForOpe(request, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.write(name);
		pw.flush();
	}

	@RequestMapping(value = "/list_excel")
	public String list_excel(Model model, @RequestParam(required = false) String error) {
		return "pages/base/list_excel_new";
	}
	
	/**
	 * 将文件信息储存进数据库
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/import", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String import_user(Model model,
			HttpServletRequest request, @RequestParam String name,
			@RequestParam Integer parent) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Sexcel excel = new Sexcel();
		String[] filename = name.split(";");
		for (String fn : filename) {
			if (!StringUtils.isBlank(fn)) {
				String[] filestr = fn.split("@");
				String filetitle = filestr[2].substring(0,filestr[2].lastIndexOf("."));
				String ext = filestr[2].substring(filestr[2].lastIndexOf(".")+1);
				excel.setCreater(ud.getId());
				excel.setTitle(filetitle);
				excel.setFiletype(ext);
				excel.setFilesize(DataUtil.FormateSize(Integer
						.parseInt(filestr[1])));
				excel.setFilepath(filestr[0] + "." + ext);
				excel.setParent(parent);
				excel.setIsdir(0);
				excelService.saveExcel(excel);
			}
		}
		model.addAttribute("msg", "数据导入成功！");
		return "success";
	}

	/**
	 * 获取文件列表
	 * 
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String list_user(Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer delete,
			@RequestParam(required = false) Integer status,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false) Integer parent,
			@RequestParam(required = false) Integer back,
			@RequestParam(required = false) String searchName,
			@RequestParam(required = false) String start_time,
			@RequestParam(required = false) String end_time) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		//区分用户类型，确定操作权限
		SysRolesUsers sru = userService.querySysRolesUsers(ud.getId());
		model.addAttribute("verify", sru.getRole_id());
		
		if (delete != null && delete.intValue() > 0) {
			model.addAttribute("msg", "删除成功！");
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		
		if (!StringUtils.isBlank(searchName)) {
			param.put("title", searchName);
			model.addAttribute("searchName", searchName);
		}
		if(parent != null && parent.intValue() > 0) {
			if(back!=null && back.intValue()==1){
				Sexcel s = excelService.queryExcelByid(parent);
				parent = s.getParent();
			}
			param.put("parent", parent);
			model.addAttribute("parent", parent);
			
		} else {
			param.put("parent", 0);
		}
		//添加判断查看列表查询
		if(sru.getRole_id() != 1) {
			param.put("removeDelete", "removeDelete");
		}
		if (!StringUtils.isBlank(start_time)) {
			param.put("start_time", start_time);
			param.put("end_time", end_time);
			model.addAttribute("start_time", start_time);
			model.addAttribute("end_time", end_time);
			System.out.println("------------------" + start_time);
		}
		List<Sexcel> sexcel = excelService.queryExcel(param, ps);
		model.addAttribute("excel", sexcel);
		
		model.addAttribute("type", type);
		if(status!=null && status==5){
			model.addAttribute("msg", "创建成功!");
		}
		return "pages/base/list_excel_new";
	}
	
	/**
	 * 删除文件
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del_excel", method = RequestMethod.GET)
	public String del_excel(Model model, @RequestParam Integer[] id) {
		// List<Integer> ids = Arrays.asList(id);
		// 进行删除
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
		for (Integer did : id) {
			Sexcel excel = excelService.queryExcelByid(did);

			DataUtil.deleteFile(fileDir + sep + excel.getFilepath());
			//System.out.println(ud.getId());
			excelService.deleteExcelByid(ud.getId(),did);
		}
		return "redirect:/admin/new/excel/list?delete=1";
	}
	/**
	 * 文件下载
	 * @param model
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/export_excel", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void download(Model model, HttpServletResponse response,
			@RequestParam(required = false) Integer id) {
		Sexcel sexcel = excelService.queryExcelByid(id);
		//文件位置
		String filePath = "/home/oil/upload/temp/";
		String FileName = sexcel.getTitle();
		// System.out.println(FileName);
		String path = sexcel.getFilepath();
		String[] fp = path.split("\\.");
		DataUtil.downLoad(filePath + path, response, FileName + "." + fp[1]);
	}

	@RequestMapping(value="/create_dir",method = RequestMethod.GET)
	public String create_dir(){
		return "pages/base/create_dir";
	}

	@RequestMapping(value = "/save_dir", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String save_dir(Model model,RedirectAttributes redirect,
			HttpServletRequest request,
			@RequestParam(required = false) String filetitle,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false) Integer parent) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Sexcel excel = new Sexcel();
		//String[] filename = name.split(";");
				excel.setCreater(ud.getId());
				excel.setTitle(filetitle);
				excel.setFiletype("文件夹");
				excel.setFilesize("0");
				excel.setFilepath("/");
				excel.setParent(parent);
				excel.setIsdir(1);
				excelService.saveExcel(excel);
				redirect.addFlashAttribute("type", type);
				redirect.addFlashAttribute("parent", parent);
		//return "redirect:/list?status=5";
			return "ok";
	}
	
	@RequestMapping(value = "/check_dir", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String check_dir(Model model,@RequestParam Integer parent,
			@RequestParam String filetitle){
		Sexcel s = excelService.queryExcelCheck(parent, filetitle.trim());
		if(s==null){
			return "ok";
		}
		return "exist";
	}
	
}
