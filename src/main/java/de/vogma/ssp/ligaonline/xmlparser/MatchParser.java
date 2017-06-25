package de.vogma.ssp.ligaonline.xmlparser;

import de.vogma.ssp.ligaonline.entitys.Match;
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

public class MatchParser {

    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    private static Document document;

    public List<Match> parseXML(InputStream matchStream)
            throws SAXException, IOException, ParserConfigurationException {

        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(matchStream);

        List<Match> matches = new ArrayList<>();

        NodeList nodes = document.getElementsByTagName("Matchdata");
        for (int index = 0; index < nodes.getLength(); index++) {

            Match match = new Match();
            Element element = (Element) nodes.item(index);

            int matchID = Integer.parseInt(element.getElementsByTagName("matchID").item(0).getTextContent());
            match.setMatchID(matchID);

            int spielTag = Integer.parseInt(element.getElementsByTagName("groupOrderID").item(0).getTextContent());
            match.setSpieltag(spielTag);
            
            int saison = Integer.parseInt(element.getElementsByTagName("leagueSaison").item(0).getTextContent());
            match.setSaison(saison);

            try {
                match.setZuschauer(
                        Integer.parseInt(element.getElementsByTagName("NumberOfViewers").item(0).getTextContent()));
            } catch (Exception ex) {
                // Log error
            }
            String matchDateTime = element.getElementsByTagName("matchDateTime").item(0).getTextContent();
            matchDateTime = matchDateTime.replace('T', ' ');
            match.setDatum(matchDateTime);

            //LocalDate localDateMatchTime = LocalDate.parse(matchDateTime);
            // System.err.println(localDateMatchTime.getYear()+" "+localDateMatchTime.getMonth());
            try {
                match.setZuschauer(Integer.parseInt(element.getElementsByTagName("NumberOfViewers").item(0).getTextContent()));
            }catch(NumberFormatException nfe){
                match.setZuschauer(0);
            }

            match.setPunkteHeim(Integer.parseInt(element.getElementsByTagName("pointsTeam1").item(0).getTextContent()));
            match.setPunkteGast(Integer.parseInt(element.getElementsByTagName("pointsTeam2").item(0).getTextContent()));
            match.setTeamName1(element.getElementsByTagName("nameTeam1").item(0).getTextContent());
            match.setTeamName2(element.getElementsByTagName("nameTeam2").item(0).getTextContent());
            NodeList matchResults = element.getElementsByTagName("matchResult");

            for (int i = 0; i < matchResults.getLength(); i++) {
                Element el = (Element) matchResults.item(i);
                if (el.getElementsByTagName("resultName").item(0).getTextContent().equals("Endergebnis")) {
                    String pointsTeam1 = el.getElementsByTagName("pointsTeam1").item(0).getTextContent();
                    String pointsTeam2 = el.getElementsByTagName("pointsTeam2").item(0).getTextContent();
                    String endergebnis = pointsTeam1 + ":" + pointsTeam2;
                    match.setEndergebnis(endergebnis);
                }
                if (el.getElementsByTagName("resultName").item(0).getTextContent().equals("Halbzeit")) {
                    String pointsTeam1 = el.getElementsByTagName("pointsTeam1").item(0).getTextContent();
                    String pointsTeam2 = el.getElementsByTagName("pointsTeam2").item(0).getTextContent();
                    String halbzeitergebnis = pointsTeam1 + ":" + pointsTeam2;
                    match.setHalbzeitergebnis(halbzeitergebnis);
                }
            }
            matches.add(match);
        }
        return matches;
    }
}
