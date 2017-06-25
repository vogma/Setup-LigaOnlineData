package de.vogma.ssp.ligaonline;

import de.vogma.ssp.ligaonline.entitys.Goal;
import de.vogma.ssp.ligaonline.entitys.Match;
import de.vogma.ssp.ligaonline.entitys.Player;
import de.vogma.ssp.ligaonline.entitys.Stadion;
import de.vogma.ssp.ligaonline.entitys.Team;
import de.vogma.ssp.ligaonline.manager.ConnectionManager;
import de.vogma.ssp.ligaonline.manager.PersistenceManager;
import de.vogma.ssp.ligaonline.xmlparser.GoalParser;
import de.vogma.ssp.ligaonline.xmlparser.MatchParser;
import de.vogma.ssp.ligaonline.xmlparser.PlayerParser;
import de.vogma.ssp.ligaonline.xmlparser.StadionParser;
import de.vogma.ssp.ligaonline.xmlparser.TeamParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.xml.sax.SAXException;

public class StartSetup {
    
    private static InputStream matchStream;
    private static InputStream teamStream;
    private static InputStream playerStream;
    private static InputStream stadionStream;
    private static InputStream goalStream;
    
    private static List<Match> matches;
    private static List<Team> teams;
    private static List<Player> player;
    private static List<Stadion> stadion;
    private static List<Goal> goals;
    
    private static MatchParser matchParser;
    private static PlayerParser playerParser;
    private static TeamParser teamParser;
    private static StadionParser stadionParser;
    private static GoalParser goalParser;
    
    public static void main(String[] args) {
        try {
            System.out.println("Initialisiere Streams...");
            initializeStreams();
            System.out.println("Initialisiere Parser...");
            initializeParser();
            System.out.println("Parse XML...");
            parseXML();
            System.out.println("Initialisiere Datenbank...");
            runDatabaseSetupScript();
            System.out.println("Persistiere Daten...");
            persistData();
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getLocalizedMessage());
        }
    }
    
    private static void initializeStreams() {
        matchStream = StartSetup.class.getResourceAsStream("/matches.xml");
        teamStream = StartSetup.class.getResourceAsStream("/teams.xml");
        playerStream = StartSetup.class.getResourceAsStream("/teams.xml");
        stadionStream = StartSetup.class.getResourceAsStream("/stadions.xml");
        goalStream = StartSetup.class.getResourceAsStream("/matches.xml");
    }
    
    private static void initializeParser() {
        matchParser = new MatchParser();
        playerParser = new PlayerParser();
        teamParser = new TeamParser();
        stadionParser = new StadionParser();
        goalParser = new GoalParser();
    }
    
    private static void persistData() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        PersistenceManager.persistStadions(stadion);
        PersistenceManager.persistTeams(teams);
        PersistenceManager.persistPlayer(player);
        PersistenceManager.persistMatches(matches);
        PersistenceManager.persistGoals(goals);
    }
    
    private static void parseXML() throws SAXException, IOException, ParserConfigurationException {
        
        matches = matchParser.parseXML(matchStream);
        teams = teamParser.parseXML(teamStream);
        player = playerParser.parseXML(playerStream);
        stadion = stadionParser.parseXML(stadionStream);
        goals = goalParser.parseXML(goalStream);
        
        teams = linkTeamsWithStadion(teams, stadion);
        matches = linkMatchesWithTables(matches, teams, stadion);
    }
    
    private static void testGoals() {
        int c = 0;
        for (Goal goal : goals) {
            label:
            for (Player p : player) {
                if (p.getName().contains(goal.getSchuetze())) {
                    System.out.println("TREFFER " + p.getName() + " : " + goal.getSchuetze());
                }
            }
        }
        System.out.println(c);
    }
    
    private static List<Match> linkMatchesWithTables(List<Match> matches, List<Team> teams, List<Stadion> stadionList) {
        
        for (Match match : matches) {
            for (Stadion stadion : stadionList) {
                if (match.getTeamName1().equals(stadion.getTeamName())) {
                    if ("".equals(match.getStadion()) || match.getStadionID() == 0) {
                        match.setStadion(stadion.getStadionName());
                        match.setStadionID(stadion.getStadionID());
                    }
                    break;
                }
            }
            for (Team team : teams) {
                if (match.getTeamName1().equals(team.getTeamName())) {
                    match.setHeimID(team.getTeamID());
                } else if (match.getTeamName2().equals(team.getTeamName())) {
                    match.setGastID(team.getTeamID());
                }
            }
        }
        
        return matches;
    }
    
    private static List<Team> linkTeamsWithStadion(List<Team> teams, List<Stadion> stadion) {
        for (Team team : teams) {
            for (Stadion stadionItem : stadion) {
                if (team.getTeamName().equals(stadionItem.getTeamName())) {
                    team.setStadionID(stadionItem.getStadionID());
                }
            }
        }
        return teams;
    }
    
    private static void runDatabaseSetupScript() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setLogWriter(null); // Erzeuge keine Logmeldungen -> nur Fehler
            Reader reader = new BufferedReader(new InputStreamReader(StartSetup.class.getResourceAsStream("/database/init.sql")));
            scriptRunner.runScript(reader);
        }
    }
    
}
