package com.example.main.apex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.constants.WidgetTypes.widgetTypes;
import com.example.file.grep.TextSearch;
import com.example.file.traversal.FileStats;

public class ApexParserEngine implements ParserEngine {
	private static final Log log = LogFactory.getLog(ApexParserEngine.class);
	private final FileStats fileStats;

	public ApexParserEngine(FileStats fileStats) {
		this.fileStats = fileStats;
	}

	@Override
	public Map<String, Map<String, List<String>>> processVisualForcePage() {
		Map<String, Map<String, List<String>>> results = new HashMap<String, Map<String, List<String>>>();
		SAXBuilder builder = new SAXBuilder();
		for (String s : getFileStats().getPageLocations().keySet()) {
			File xmlFile = new File(getFileStats().getPageLocations().get(s));
			try {
				Document document = (Document) builder.build(xmlFile);
				Element rootNode = document.getRootElement();
				processElements(s, rootNode, getFileStats(), results);
			} catch (IOException io) {
				// log.error(io.getMessage(), io);
			} catch (JDOMException jdomex) {
				// log.error(jdomex.getMessage(),jdomex);
			}
		}
		return results;
	}

	private void processElements(String pageName, Element rootNode,
			FileStats fs, Map<String, Map<String, List<String>>> results) {
		Attribute controllerNameInVfPage = null;

		if (rootNode.getAttribute("controller") != null) {
			log.info("found Controller for " + pageName);
			controllerNameInVfPage = rootNode.getAttribute("controller");
		} else if (rootNode.getAttribute("standardController") != null
				&& rootNode.getAttribute("extensions") != null) {
			log.info("found Standard controller, Extensions for " + pageName);
			controllerNameInVfPage = rootNode.getAttribute("extensions");
		} else if (rootNode.getAttribute("standardController") != null) {
			log.info("found Standard controller for " + pageName);
			controllerNameInVfPage = rootNode
					.getAttribute("standardController");
		}
		for (WidgetTypes.widgetTypes type : widgetTypes.values()) {
			ElementFilter filter = new ElementFilter(type.name());
			IteratorIterable<Element> descendants = rootNode
					.getDescendants(filter);
			if (controllerNameInVfPage != null
					&& controllerNameInVfPage.getValue() != null) {
				String controllerClassName = fs.getControllerLocations().get(
						controllerNameInVfPage.getValue() + ".cls");
				while (descendants.hasNext()) {
					Element c = (Element) descendants.next();
					if (type.equals(widgetTypes.repeat)) {
						log.info(pageName + ":"
								+ controllerNameInVfPage.getValue());
					} else if (type.equals(widgetTypes.inputField)
							|| type.equals(widgetTypes.inputTextarea)) {
						String[] splits = processField(c.getAttribute("value")
								.getValue());
						if (controllerClassName != null) {
							String className = null;
							try {
								className = TextSearch.searchString(
										controllerClassName, splits[0]);
							} catch (IOException e) {
								log.error(e.getMessage(), e);
							}
							catalogResults(pageName, controllerClassName,
									className, splits[1], results);
							log.info(pageName + ":"
									+ controllerNameInVfPage.getValue() + ":"
									+ className + ":" + splits[1]);
						} else {
							catalogResults(pageName,
									controllerNameInVfPage.getValue(),
									splits[1], null, results);
							log.info(pageName + ":"
									+ controllerNameInVfPage.getValue() + ":"
									+ splits[0] + ":" + splits[1]);

						}
					}
				}
			}
		}
	}

	private void catalogResults(String pageName, String controllerName,
			String fieldName, String s,
			Map<String, Map<String, List<String>>> results) {
		if (!results.containsKey(pageName)) {

			Map<String, List<String>> classDetails = new HashMap<String, List<String>>();
			List<String> subfields = new ArrayList<String>();
			if (s != null) {
				subfields.add(fieldName + "^" + s);
			} else {
				subfields.add(fieldName);
			}
			classDetails.put(controllerName, subfields);
			results.put(pageName, classDetails);

		} else {
			Map<String, List<String>> map = results.get(pageName);
			if (!map.containsKey(controllerName)) {
				Map<String, List<String>> classDetails = new HashMap<String, List<String>>();
				List<String> subfields = new ArrayList<String>();
				if (s != null) {
					subfields.add(fieldName + "^" + s);
				} else {
					subfields.add(fieldName);
				}
				classDetails.put(controllerName, subfields);
				results.put(pageName, classDetails);
			} else {
				List<String> list = map.get(controllerName);
				if (s != null) {
					list.add(fieldName + "^" + s);
				} else {
					list.add(fieldName);
				}
			}
		}
	}

	private String getValueAttribute(Element e) {
		if (e != null) {
			return e.getAttribute("value").getValue();
		} else {
			return null;
		}
	}

	private String[] processField(String value) {
		value = value.replace("{", "").replace("}", "").replace("!", "");
		if (value.indexOf(".") != -1) {
			String[] split = value.split("\\.");
			return split;
		} else {

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
