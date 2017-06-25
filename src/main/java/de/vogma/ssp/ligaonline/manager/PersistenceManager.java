package de.vogma.ssp.ligaonline.manager;

import de.vogma.ssp.ligaonline.entitys.Goal;
import de.vogma.ssp.ligaonline.entitys.Match;
import de.vogma.ssp.ligaonline.entitys.Player;
import de.vogma.ssp.ligaonline.entitys.Stadion;
import de.vogma.ssp.ligaonline.entitys.Team;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import manager.ConnectionManager;

public class PersistenceManager {

    private static final String PLAYERSQLSTATEMENT = "INSERT INTO liga.player (p_name,p_verein_id) VALUES (?,?);";
    private static final String STADIONSQLSTATEMENT = "INSERT INTO liga.stadion (stadion_id,s_name,s_gps,s_adress,s_bildURL,s_kapazitaet) VALUES (?,?,?,?,?,?);";
    private static final String TEAMSQLSTATEMENT = "INSERT INTO liga.verein (verein_id,v_name,v_logoURL,stadion_stadion_id) VALUES (?,?,?,?);";
    private static final String MATCHSQLSTATEMENT = "INSERT INTO liga.match (match_id,m_saison,m_stadion,m_stadionID,m_endergebnis,m_halbzeitergebnis,m_punkteHeim,m_punkteGast,m_heimID,m_gastID) VALUES (?,?,?,?,?,?,?,?,?,?);";
    private static final String GOALSQLSTATEMENT = "INSERT INTO liga.goal (goal_id, g_toreHeim, g_toreGast, g_minute, g_schuetze, match_id, player_id) VALUES (?,?,?,?,?,?,?)";

    public static void persistStadions(List<Stadion> stadions) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        int id = 1;
        try (Connection connection = ConnectionManager.getConnection()) {
            for (Stadion stadion : stadions) {
                PreparedStatement preparedStatement = connection.prepareStatement(STADIONSQLSTATEMENT);
                preparedStatement.setInt(1, id++);
                preparedStatement.setString(2, stadion.getStadionName());
                preparedStatement.setString(3, stadion.getStadionGPS());
                preparedStatement.setString(4, stadion.getStadionAddress());
                preparedStatement.setString(5, stadion.getStadionImageURL());
                preparedStatement.setInt(6, stadion.getStadionCapacity());
                preparedStatement.execute();
            }
        }
        System.err.println("Done Stadion");

    }

    public static void persistPlayer(List<Player> playerList) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            for (Player player : playerList) {
                PreparedStatement preparedStatement = connection.prepareStatement(PLAYERSQLSTATEMENT);
                preparedStatement.setString(1, player.getName());
                preparedStatement.setInt(2, player.getTeamID());
                preparedStatement.execute();
            }
        }
        System.err.println("Done Player");

    }

    public static void persistTeams(List<Team> teams) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        try (Connection connection = ConnectionManager.getConnection()) {
            for (Team team : teams) {
                PreparedStatement preparedStatement = connection.prepareStatement(TEAMSQLSTATEMENT);
                preparedStatement.setInt(1, team.getTeamID());
                preparedStatement.setString(2, team.getTeamName());
                preparedStatement.setString(3, team.getTeamIconUrl());
                preparedStatement.setInt(4, team.getStadionID());
                System.err.println(preparedStatement);
                preparedStatement.execute();
            }
        }

    }

    public static void persistMatches(List<Match> matches) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        try (Connection connection = ConnectionManager.getConnection()) {
            for (Match match : matches) {
                PreparedStatement preparedStatement = connection.prepareStatement(MATCHSQLSTATEMENT);
                preparedStatement.setInt(1, match.getMatchID());
                preparedStatement.setInt(2, match.getSaison());
                preparedStatement.setString(3, match.getStadion());
                preparedStatement.setInt(4, match.getStadionID());
                preparedStatement.setString(5, match.getEndergebnis());
                preparedStatement.setString(6, match.getHalbzeitergebnis());
                preparedStatement.setInt(7, match.getPunkteHeim());
                preparedStatement.setInt(8, match.getPunkteGast());
                preparedStatement.setInt(9, match.getHeimID());
                preparedStatement.setInt(10, match.getGastID());
                preparedStatement.execute();
            }
        }
        System.err.println("Done Matches");

    }

    public static void persistGoals(List<Goal> goals) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            for (Goal goal : goals) {
                PreparedStatement preparedStatement = connection.prepareStatement(GOALSQLSTATEMENT);
                //goal_id, g_toreHeim, g_toreGast, g_minute, g_schuetze, match_id, player_id
                preparedStatement.setInt(1, goal.getGoalID());
                preparedStatement.setInt(2, goal.getToreHeim());
                preparedStatement.setInt(3, goal.getToreGast());
                preparedStatement.setInt(4, goal.getMinute());
                //TODO Sch�tze
                preparedStatement.setString(5, goal.getSchuetze());
                preparedStatement.setInt(6, goal.getMatchID());
                //TODO playerID
                preparedStatement.setInt(7, 1);
                preparedStatement.execute();
            }
        }
        System.err.println("Done Goals");

    }
}
