package com.example.main.apex;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;

import com.example.constants.WidgetTypes;
import com.example.file.grep.TextSearch;
import com.example.file.traversal.FileStats;
import com.example.main.JdomHelper;

public class ApexParserEngine implements ParserEngine{
	private static final Log log = LogFactory.getLog(ApexParserEngine.class);
	private final FileStats fileStats;
	public ApexParserEngine(FileStats fileStats) {
		this.fileStats = fileStats;
	}

	@Override
	public void processVisualForcePage() {
		SAXBuilder builder = new SAXBuilder();
		for (String s : getFileStats().getPageLocations().keySet()) {
			File xmlFile = new File(getFileStats().getPageLocations().get(s));
			try {
				Document document = (Document) builder.build(xmlFile);
				Element rootNode = document.getRootElement();
				List<Element> children = rootNode.getChildren("page");
				
				findInputFields(s,rootNode, getFileStats());
				findInputTextAreaFields(s, rootNode,getFileStats());
			} catch (IOException io) {
				System.out.println(io.getMessage());
			} catch (JDOMException jdomex) {
				System.out.println(jdomex.getMessage());
			}
		}

	}

	private void findInputFields(String pageName, Element rootNode, FileStats fs) {
		ElementFilter inputTextAreaFilter = new ElementFilter(
				WidgetTypes.widgetTypes.inputTextarea.name());
		IteratorIterable<Element> descendants = rootNode
				.getDescendants(inputTextAreaFilter);
		processElements(pageName, descendants,rootNode, fs);
	}

	private void findInputTextAreaFields(String pageName, Element rootNode, FileStats fs) {
		ElementFilter inputFieldFilter = new ElementFilter(
				WidgetTypes.widgetTypes.inputField.name());
		IteratorIterable<Element> descendants = rootNode
				.getDescendants(inputFieldFilter);
		processElements(pageName,descendants,rootNode, fs);
	}
	
	private void processElements(String pageName, IteratorIterable<Element> descendants, Element rootNode, FileStats fs) {
		while (descendants.hasNext()) {
			Element c = (Element) descendants.next();
			String[] splits = processField(c.getAttribute("value").getValue());
			Attribute attribute = rootNode.getAttribute("controller");
			if (attribute!=null && attribute.getValue()!=null) {
				String controllerName = fs.getControllerLocations().get(attribute.getValue()+".cls");
				String className = null ;
				try {
					className = TextSearch.searchString(controllerName, splits[0]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(pageName+":"+rootNode.getAttribute("controller").getValue()+":"+className+":"+splits[1]);
			}
		}
	}
	
	private String[] processField(String value) {
		value = value.replace("{", "").replace("}", "").replace("!", "");
		if(value.indexOf(".")!=-1){
			String[] split = value.split("\\.");
			return split;
		}else{
			
		}
		return null;
	}
	
	@Override
	public void processStandardLayout() {
		
	}

	public FileStats getFileStats() {
		return fileStats;
	}
}
