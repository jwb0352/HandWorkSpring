package com.jwb.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jwb.annotation.Controller;
import com.jwb.annotation.Quatifier;
import com.jwb.annotation.RequestMapping;
import com.jwb.annotation.Service;
import com.jwb.controller.SpringmvcController;

public class Dispatcher extends HttpServlet {
	List<String> packageNames = new ArrayList<String>();
	Map<String, Object> instanceMap = new HashMap<String, Object>();
	Map<String, Object> handerMap = new HashMap<String, Object>();
	
	@Override
	public void init() throws ServletException {
		//∂¡≈‰÷√
		
		//…®√Ë∞¸
		scanPackage("com.jwb");
		// µ¿˝ªØ
		try {
			filterAndInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//”≥…‰
		handlerMap();
		//◊¢»Î
		ioc();
	}
	
	private void scanPackage(String packName){
		URL url = this.getClass().getClassLoader().getResource("/" + replaceTo(packName));
		String pathFile = url.getFile();
		File file = new File(pathFile);
		String fileList[] = file.list();
		for (String path : fileList) {
			File eachFile = new File(pathFile+path);
			if(eachFile.isDirectory()){
				scanPackage(packName + "." + eachFile.getName());
			}else{
				packageNames.add(packName + "." + eachFile.getName());
			}
		}
	}
	
	private String replaceTo(String path) {
		return path.replaceAll("\\.", "/");
	}
	
	private void filterAndInstance() throws Exception{
		if(packageNames.size()<=0){
			return;
		}
		for (String className : packageNames) {
			Class<?> cName = Class.forName(className.replace(".class", "").trim());
			if(cName.isAnnotationPresent(Controller.class)){
				Object instance = cName.newInstance();
				Controller controller = cName.getAnnotation(Controller.class);
				String key = controller.value();
				instanceMap.put(key, instance);
			}else if(cName.isAnnotationPresent(Service.class)){
				Object instance = cName.newInstance();
				Service service = cName.getAnnotation(Service.class);
				String key = service.value();
				instanceMap.put(key, instance);
			}else{
				continue;
			}
		}
	}
	
	private void handlerMap(){
		if(instanceMap.size()<=0){
			return;
		}
		for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
			if(entry.getValue().getClass().isAnnotationPresent(Controller.class)){
				Controller controller = entry.getValue().getClass().getAnnotation(Controller.class);
				String ctvalue = controller.value();
				Method[] methods = entry.getValue().getClass().getMethods();
				for (Method method : methods) {
					if(method.isAnnotationPresent(RequestMapping.class)){
						RequestMapping rm = method.getAnnotation(RequestMapping.class);
						String rmvalue = rm.value();
						handerMap.put("/"+ctvalue+"/"+rmvalue + ".do", method);
					}else{
						continue;
					}
				}
			}else{
				continue;
			}
		}
	}
	
	private void ioc(){
		if(instanceMap.size()<=0){
			return;
		}
		for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
			Field fields[] = entry.getValue().getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if(field.isAnnotationPresent(Quatifier.class)){
					Quatifier quatifier = field.getAnnotation(Quatifier.class);
					String value = quatifier.value();
					field.setAccessible(true);
					try {
						field.set(entry.getValue(), instanceMap.get(value));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url = req.getRequestURI();
		String context = req.getContextPath();
		String path = url.replace(context, "");
		Method method = (Method) handerMap.get(path);
		SpringmvcController controller = (SpringmvcController) instanceMap.get(path.split("/")[1]);
		try {
			method.invoke(controller, new Object[]{req,resp,null});
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
