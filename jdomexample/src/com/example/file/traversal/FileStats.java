package com.example.file.traversal;
import java.util.HashMap;
import java.util.Map;


public class FileStats {
	private Map<String, String> controllerLocations = new HashMap<String, String>();
	private Map<String, String> pageLocations = new HashMap<String, String>();
	private Map<String, String> laylocations = new HashMap<String, String>();
	public Map<String, String> getControllerLocations() {
		return controllerLocations;
	}
	public void setControllerLocations(Map<String, String> controllerLocations) {
		this.controllerLocations = controllerLocations;
	}
	public Map<String, String> getPageLocations() {
		return pageLocations;
	}
	public void setPageLocations(Map<String, String> pageLocations) {
		this.pageLocations = pageLocations;
	}
	public Map<String, String> getLaylocations() {
		return laylocations;
	}
	public void setLaylocations(Map<String, String> laylocations) {
		this.laylocations = laylocations;
	}
}
