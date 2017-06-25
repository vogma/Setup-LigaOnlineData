package de.vogma.ssp.ligaonline.xmlparser;

import de.vogma.ssp.ligaonline.entitys.Stadion;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class StadionParser {

	private static DocumentBuilderFactory documentBuilderFactory;
	private static DocumentBuilder documentBuilder;
	private static Document document;

	public List<Stadion> parseXML(InputStream stadionStream)
			throws SAXException, IOException, ParserConfigurationException {
		
		List<Stadion> stadions = new ArrayList<>();

		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		document = documentBuilder.parse(stadionStream);

		NodeList nodeList = document.getElementsByTagName("Stadion");
		for (int index = 0; index < nodeList.getLength(); index++) {
			Stadion stadion = new Stadion();
			Element element = (Element) nodeList.item(index);
			stadion.setStadionAddress(element.getElementsByTagName("stadionAddress").item(0).getTextContent());
			stadion.setStadionCapacity(
					Integer.parseInt(element.getElementsByTagName("stadionCapacity").item(0).getTextContent()));
			stadion.setStadionGPS(element.getElementsByTagName("stadionGPS").item(0).getTextContent());
			stadion.setStadionImageURL(element.getElementsByTagName("stadionImageURL").item(0).getTextContent());
			stadion.setStadionName(element.getElementsByTagName("stadionName").item(0).getTextContent());
			stadion.setTeamID(Integer.parseInt(element.getElementsByTagName("teamID").item(0).getTextContent()));
			stadion.setTeamName(element.getElementsByTagName("teamName").item(0).getTextContent());
			stadion.setStadionID(Integer.parseInt(element.getElementsByTagName("stadionID").item(0).getTextContent()));
			stadions.add(stadion);
		}
		return stadions;
	}
}
