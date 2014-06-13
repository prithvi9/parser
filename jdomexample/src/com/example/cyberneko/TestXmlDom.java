package com.example.cyberneko;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Node;

public class TestXmlDom {
	public static void main(String[] argv) throws Exception {
		DOMParser parser = new DOMParser();
//		SAXParser parser = new SAXParser();
		parser.setFeature("http://xml.org/sax/features/namespaces", false);
		parser.parse("c:\\test\\something.page");
		print(parser.getDocument(), " ");
	}

	public static void print(Node node, String indent) {
		if( node.getClass().getName().equals("org.apache.html.dom.HTMLElementImpl") ){
			System.out.println( indent+node.getNodeName());
		}
			Node child = node.getFirstChild();
			while (child != null) {
				print(child, indent + " ");
				child = child.getNextSibling();
		}
	}
}