package com;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DOMCopy {

	private static final String TAB = "    ";

	static public void main(String[] arg) {
		String a = "a, b, c";
		a = a.replace(" ", "");
		a = a.replace(",", "','");
		System.out.println(a);
	}

	private static void outputCDATASection(CDATASection node, String indent) {
		System.out.println(indent + node.getData());
	}

	private static void outputComment(Comment node, String indent) {
		 System.out.println(indent + "<!-- " + node.getData() + " -->");
	}

	private static void outputElement(Element node, String indent) {
		System.out.print(indent + "<" + node.getTagName());
		NamedNodeMap nm = node.getAttributes();
		for (int i = 0; i < nm.getLength(); i++) {
			Attr attr = (Attr) nm.item(i);
			System.out.print(" " + attr.getName() + "=\"" + attr.getValue()
					+ "\"");
		}
		System.out.println(">");
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			outputloop(list.item(i), indent + TAB);
		}
		System.out.println(indent + "</" + node.getTagName() + ">");
	}

	private static void outputloop(Node node, String indent) {
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE: {
			System.out.println("element node");
			outputElement((Element) node, indent);
			break;
		}
		case Node.TEXT_NODE:
			outputText((Text) node, indent);
			break;
		case Node.CDATA_SECTION_NODE:
			outputCDATASection((CDATASection) node, indent);
			break;
		case Node.COMMENT_NODE:
			outputComment((Comment) node, indent);
			break;
		case Node.PROCESSING_INSTRUCTION_NODE:
			outputProcessingInstructionNode((ProcessingInstruction) node,
					indent);
			break;
		default:
			System.out.println("Unknown node type: " + node.getNodeType());
			break;
		}
	}

	private static void outputProcessingInstructionNode(
			ProcessingInstruction node, String indent) {
		System.out.println(indent + "<?" + node.getTarget() + " "
				+ node.getData() + "?>");
	}

	private static void outputText(Text node, String indent) {

		System.out.println(indent + node.getData());
	}
}

class MyErrorHandler implements ErrorHandler {
	public void error(SAXParseException e) throws SAXException {
		show("Error", e);
		throw e;
	}

	public void fatalError(SAXParseException e) throws SAXException {
		show("Fatal Error", e);
		throw e;
	}

	private void show(String type, SAXParseException e) {
		System.out.println(type + ": " + e.getMessage());
		System.out.println("Line " + e.getLineNumber() + " Column "
				+ e.getColumnNumber());
		System.out.println("System ID: " + e.getSystemId());
	}

	public void warning(SAXParseException e) throws SAXException {
		show("Warning", e);
		throw e;
	}
}
