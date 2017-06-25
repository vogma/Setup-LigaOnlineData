package de.vogma.ssp.ligaonline.xmlparser;

import de.vogma.ssp.ligaonline.entitys.Team;
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


public class TeamParser {

	private static DocumentBuilderFactory documentBuilderFactory;
	private static DocumentBuilder documentBuilder;
	private static Document document;
	
	public List<Team> parseXML(InputStream teamStream) throws SAXException, IOException, ParserConfigurationException{
		
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		document = documentBuilder.parse(teamStream);

		List<Team> teams = new ArrayList<>();
		NodeList nodeList = document.getElementsByTagName("Team");
		for (int index = 0; index < nodeList.getLength(); index++)
		{
			Team team = new Team();

			Element element = (Element) nodeList.item(index);
			team.setTeamName(element.getElementsByTagName("teamName").item(0).getTextContent());
			team.setTeamID(Integer.parseInt(element.getElementsByTagName("teamID").item(0).getTextContent()));
			team.setTeamIconUrl(element.getElementsByTagName("teamIconURL").item(0).getTextContent());
			teams.add(team);
		}
		return teams;
	}
}
