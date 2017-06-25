package de.vogma.ssp.ligaonline.xmlparser;

import de.vogma.ssp.ligaonline.entitys.Goal;
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

public class GoalParser {

    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    private static Document document;

    public List<Goal> parseXML(InputStream goalStream) throws SAXException, IOException, ParserConfigurationException {
        List<Goal> goals = new ArrayList<>();

        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(goalStream);

        NodeList nodes = document.getElementsByTagName("Matchdata");
        for (int index = 0; index < nodes.getLength(); index++) {

            Element element = (Element) nodes.item(index);

            NodeList goalNodes = element.getElementsByTagName("Goal");
            for (int indexGoal = 0; indexGoal < goalNodes.getLength(); indexGoal++) {
                Goal goal = new Goal();
                Element el = (Element) goalNodes.item(indexGoal);
                goal.setGoalID(Integer.valueOf(el.getElementsByTagName("goalID").item(0).getTextContent()));
                goal.setMatchID(Integer.valueOf(el.getElementsByTagName("goalMachID").item(0).getTextContent()));
                goal.setMinute(Integer.valueOf(el.getElementsByTagName("goalMatchMinute").item(0).getTextContent()));
                goal.setSchuetze(el.getElementsByTagName("goalGetterName").item(0).getTextContent());
                goal.setToreGast(Integer.valueOf(el.getElementsByTagName("goalScoreTeam2").item(0).getTextContent()));
                goal.setToreHeim(Integer.valueOf(el.getElementsByTagName("goalScoreTeam1").item(0).getTextContent()));
                goal.setSchuetze(el.getElementsByTagName("goalGetterName").item(0).getTextContent());

                String valueOfGoalPenalty = el.getElementsByTagName("goalPenalty").item(0).getTextContent();
                if (!"".equals(valueOfGoalPenalty)) {
                    if ("true".equals(valueOfGoalPenalty)) {
                        goal.setGoalPenalty(true);
                    } else if ("false".equals(valueOfGoalPenalty)) {
                        goal.setGoalPenalty(false);
                    }
                }

                goals.add(goal);
            }

        }

        return goals;
    }
}
