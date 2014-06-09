package com.example.main.apex;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;

import com.example.constants.WidgetTypes;
import com.example.file.grep.TextSearch;
import com.example.file.traversal.FileStats;

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
				
				findInputFields(rootNode, getFileStats());
				findInputTextAreaFields(rootNode,getFileStats());
			} catch (IOException io) {
				System.out.println(io.getMessage());
			} catch (JDOMException jdomex) {
				System.out.println(jdomex.getMessage());
			}
		}

	}

	private void findInputFields(Element rootNode, FileStats fs) {
		ElementFilter inputTextAreaFilter = new ElementFilter(
				WidgetTypes.widgetTypes.inputTextarea.name());
		IteratorIterable<Element> descendants = rootNode
				.getDescendants(inputTextAreaFilter);
		processElements(descendants,rootNode, fs);
	}

	private void findInputTextAreaFields(Element rootNode, FileStats fs) {
		ElementFilter inputFieldFilter = new ElementFilter(
				WidgetTypes.widgetTypes.inputField.name());
		IteratorIterable<Element> descendants = rootNode
				.getDescendants(inputFieldFilter);
		processElements(descendants,rootNode, fs);
	}
	
	private void processElements(IteratorIterable<Element> descendants, Element rootNode, FileStats fs) {
		while (descendants.hasNext()) {
			Element c = (Element) descendants.next();
			String[] splits = processField(c.getAttribute("value").getValue());
			String controllerName = fs.getControllerLocations().get(rootNode.getAttribute("controller").getValue()+".cls");
			String className = null ;
			try {
				className = TextSearch.searchString(controllerName, splits[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(rootNode.getAttribute("controller").getValue()+":"+className+":"+splits[1]);
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
