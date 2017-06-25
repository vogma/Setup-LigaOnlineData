package de.vogma.ssp.ligaonline.manager;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataBaseInitiator {

    private static final String TRENNZEICHEN = ";";
    private static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+";
    private static final String DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER";

    private final Connection connection;
    private final boolean autoCommit;
    private String trennzeichen = TRENNZEICHEN;
    private boolean fullLineDelimiter = false;

    public DataBaseInitiator(Connection connection, boolean autoCommit) {
        this.connection = connection;
        this.autoCommit = autoCommit;
    }

    public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
        this.trennzeichen = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    public void startInit(Reader reader) throws IOException, SQLException {
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            try {
                if (originalAutoCommit != autoCommit) {
                    connection.setAutoCommit(autoCommit);
                }
                runScript(connection, reader);
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (IOException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                if (!fullLineDelimiter && trimmedLine.endsWith(getDelimiter())
                        || fullLineDelimiter && trimmedLine.equals(getDelimiter())) {

                    Pattern pattern = Pattern.compile(DELIMITER_LINE_REGEX);
                    Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        setDelimiter(trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim(),
                                fullLineDelimiter);
                        line = lineReader.readLine();
                        if (line == null) {
                            break;
                        }
                        trimmedLine = line.trim();
                    }

                    command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
                    command.append(" ");
                    Statement statement = conn.createStatement();

                    boolean hasResults = false;

                    try {
                        statement.execute(command.toString());
                    } catch (SQLException e) {
                        e.fillInStackTrace();
                    }

                    if (autoCommit && !conn.getAutoCommit()) {
                        conn.commit();
                    }

                    ResultSet rs = statement.getResultSet();
                    if (hasResults && rs != null) {
                        ResultSetMetaData md = rs.getMetaData();
                        int cols = md.getColumnCount();
                        for (int i = 0; i < cols; i++) {
                            String name = md.getColumnLabel(i);
                        }

                        while (rs.next()) {
                            for (int i = 1; i <= cols; i++) {
                                String value = rs.getString(i);
                            }
                        }
                    }

                    command = null;
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (statement != null) {
                            statement.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Ignore to workaround a bug in Jakarta DBCP
                    }
                } else {
                    Pattern pattern = Pattern.compile(DELIMITER_LINE_REGEX);
                    Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        setDelimiter(trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim(),
                                fullLineDelimiter);
                        line = lineReader.readLine();
                        if (line == null) {
                            break;
                        }
                        trimmedLine = line.trim();
                    }
                    command.append(line);
                    command.append(" ");
                }
            }
            if (!autoCommit) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();

            throw e;
        } catch (IOException e) {
            e.fillInStackTrace();

            throw e;
        } finally {
            conn.rollback();

        }
    }

    private String getDelimiter() {
        return trennzeichen;
    }
}
