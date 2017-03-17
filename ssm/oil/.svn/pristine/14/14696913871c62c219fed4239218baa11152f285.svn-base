package cn.hm.oil.webservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hm.oil.json.JsonResWrapper;
import cn.hm.oil.json.ResponseStatus;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.SettingUtils;

@Controller
@RequestMapping(value = "/services")
public class WS_Upload {
	/**
	 * 上传文件接口
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper report_unknow_channel(HttpServletRequest request, 
			@RequestParam String fileName) {
		JsonResWrapper response = new JsonResWrapper();
		List<String> paths = null;
		try {
			paths = DataUtil.uploadFile(request, fileName);
			response.setStatus(ResponseStatus.OK);
			response.setData(paths);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(ResponseStatus.FAILED);
		}
		return response;
	}
	
	/**
	 * 上传图片接口
	 */
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper upload_image(HttpServletRequest request, @RequestParam String fileName) {
		JsonResWrapper response = new JsonResWrapper();
		List<String> paths = null;
		try {
			paths = DataUtil.uploadImg(request, fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;
		response.setStatus(ResponseStatus.OK);
		response.setData(paths);
		return response;
	}
	
	/**
	 * 文件上传，用于处理大文件上传
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @param chunk
	 * @param chunks
	 * @return
	 */
	@RequestMapping(value = "/uploadMFile", method = RequestMethod.POST)
	public @ResponseBody JsonResWrapper uploadMFile(Model model, HttpServletRequest request) {
		JsonResWrapper response = new JsonResWrapper();
		try {
			List<String> fileName = DataUtil.uploadMFile(request, "file");
			response.setStatus(ResponseStatus.OK);
			response.setData(fileName);
			return response;
		} catch (Exception e) {
			e.getStackTrace();
			response.setStatus(ResponseStatus.FAILED);
			return null;
		}
			
	}
}
