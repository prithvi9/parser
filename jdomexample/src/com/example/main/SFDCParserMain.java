package com.example.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;

import com.example.constants.WidgetTypes;
import com.example.file.grep.TextSearch;
import com.example.file.traversal.FileListingVisitor;
import com.example.file.traversal.FileStats;
import com.example.main.apex.ApexParserEngine;

public class SFDCParserMain {

	private Map<String, String> results = new HashMap<String, String>();
	private static Attribute attribute = null;

	public static void main(String[] args) {
		FileListingVisitor flv = new FileListingVisitor();
		FileStats fs = null;
		try {
			fs = flv.processDirectory("C:\\force.comproject\\DevOrgProject1\\src");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ApexParserEngine ape = new ApexParserEngine(fs);
		ape.processVisualForcePage();
	}

	/*private static void processVisualForcesPages(FileStats fs) {
		SAXBuilder builder = new SAXBuilder();
		for (String s : fs.getPageLocations().keySet()) {
			File xmlFile = new File(fs.getPageLocations().get(s));
			try {
				Document document = (Document) builder.build(xmlFile);
				Element rootNode = document.getRootElement();
				List<Element> children = rootNode.getChildren("page");
				attribute = rootNode.getAttribute("controller");
				
				findInputFields(rootNode, fs);
				findInputTextAreaFields(rootNode,fs);
			} catch (IOException io) {
				System.out.println(io.getMessage());
			} catch (JDOMException jdomex) {
				System.out.println(jdomex.getMessage());
			}
		}

	}

	private static void findInputFields(Element rootNode, FileStats fs) {
		ElementFilter inputTextAreaFilter = new ElementFilter(
				WidgetTypes.widgetTypes.inputTextarea.name());
		IteratorIterable<Element> descendants = rootNode
				.getDescendants(inputTextAreaFilter);
		processElements(descendants, fs);
	}

	private static void findInputTextAreaFields(Element rootNode, FileStats fs) {
		ElementFilter inputFieldFilter = new ElementFilter(
				WidgetTypes.widgetTypes.inputField.name());
		IteratorIterable<Element> descendants = rootNode
				.getDescendants(inputFieldFilter);
		processElements(descendants, fs);
	}

	private static void processElements(IteratorIterable<Element> descendants,  FileStats fs) {
		while (descendants.hasNext()) {
			Element c = (Element) descendants.next();
			String[] splits = processField(c.getAttribute("value").getValue());
			String controllerName = fs.getControllerLocations().get(attribute.getValue()+".cls");
			String className = null ;
			try {
				className = TextSearch.searchString(controllerName, splits[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(attribute.getValue()+":"+className+":"+splits[1]);
		}
	}
	

	private static String[] processField(String value) {
		value = value.replace("{", "").replace("}", "").replace("!", "");
		if(value.indexOf(".")!=-1){
			String[] split = value.split("\\.");
			return split;
		}
		return null;
	}*/
}
