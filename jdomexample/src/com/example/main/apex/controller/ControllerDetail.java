package com.example.main.apex.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerDetail {

	private String controllerName = null;
	private Map<String, List<String>> classDetails = new HashMap<String, List<String>>(); 
	
	public ControllerDetail() {
	}
	
	public String getControllerName() {
		return controllerName;
	}
	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}
	public Map<String, List<String>> getClassDetails() {
		return classDetails;
	}
	public void setClassDetails(Map<String, List<String>> classDetails) {
		this.classDetails = classDetails;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this.controllerName == null && obj !=null){
			return false;
		}else if (this.controllerName != null && obj ==null){
			return false;
		}else if(this.controllerName == null && obj ==null){
			return true;
		}
		return this.controllerName.equals(((ControllerDetail)obj).getControllerName());
	}
}
