package de.vogma.ssp.ligaonline.xmlparser;

import de.vogma.ssp.ligaonline.entitys.Player;
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


public class PlayerParser {
	
	private static DocumentBuilderFactory documentBuilderFactory;
	private static DocumentBuilder documentBuilder;
	private static Document document;
	
	
public List<Player> parseXML(InputStream playerStream) throws SAXException, IOException, ParserConfigurationException{
	documentBuilderFactory = DocumentBuilderFactory.newInstance();
	documentBuilder = documentBuilderFactory.newDocumentBuilder();
	document = documentBuilder.parse(playerStream);

	int teamID;
	List<Player> playerList = new ArrayList<>();
	NodeList nodeList = document.getElementsByTagName("Team");
	
	for (int index = 0; index < nodeList.getLength(); index++)
	{
		Element element = (Element) nodeList.item(index);
		teamID = Integer.parseInt(element.getElementsByTagName("teamID").item(0).getTextContent());

		NodeList nl = element.getElementsByTagName("player");
		for (int i = 0; i < nl.getLength(); i++)
		{
			Player player = new Player();
			player.setName(nl.item(i).getTextContent());
			player.setTeamID(teamID);
			playerList.add(player);
		}
	}
	return playerList;
}
}
