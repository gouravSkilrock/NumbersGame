package com.skilrock.lms.common.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlReader {
	static Log logger = LogFactory.getLog(XmlReader.class);

	public static void main(String argv[]) {
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("D:/struts.xml"));
			doc.getDocumentElement().normalize();
			logger.debug("Root element of the doc is "
					+ doc.getDocumentElement().getNodeName());

			NodeList listOfIncludes = doc.getElementsByTagName("include");
			int totalInclude = listOfIncludes.getLength();
			logger.debug("Total no of include : " + totalInclude);

			for (int s = 0; s < listOfIncludes.getLength(); s++) {

				Node node = listOfIncludes.item(s);

				logger.debug(node.getNodeName());

				NamedNodeMap map = node.getAttributes();

				Node n = map.getNamedItem("file");

				logger.debug(n.getNodeName() + ":::" + n.getNodeValue());

				if (n.getNodeValue().equals(
						"com/skilrock/lms/retailer/retailer.xml")) {
					logger.debug("got the file ");
					logger.debug("s is  " + s);
					node.getParentNode().removeChild(node);

					Transformer transformer = TransformerFactory.newInstance()
							.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");

					StreamResult result = new StreamResult(new StringWriter());
					DOMSource source = new DOMSource(doc);
					transformer.transform(source, result);

					String xmlString = result.getWriter().toString().trim();
					logger.debug("gtgg" + xmlString);

					FileWriter f = new FileWriter(new File("D:/struts1.xml"));
					f.write(xmlString);
					f.flush();

					logger.debug("reader is " + f);
					break;

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
