package com.home.airlinescanner.tikaairlinescanner;

import com.home.util.DbParameters;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

/**
 * Airline data processor.
 */
public class AirlineProcessor {
    private static final Logger LOG = Logger.getLogger(AirlineProcessor.class.getName());
    private static final String UPDTUSER = "Scanner";
    private static final DbParameters DB_PARAMS = new DbParameters("AirlineScanner.properties");
    private static int unchanged = 0;
    private static int inserted = 0;
    private static int updated = 0;

    /**
     * Insert or update the database using the given airline values.
     *
     * @param code     the iata code
     * @param icao     the icao code
     * @param name     the airlines name
     * @param location the airlines location
     */
    public static void processAirline(String code, String name) {
        // Can happen
        if (code.trim().isEmpty()) {
            throw new IllegalArgumentException("CODE is EMPTY");
        }

        String schema = (DB_PARAMS.getSchema().trim().isEmpty() ? "" : (DB_PARAMS.getSchema() + "."));
        String table = DB_PARAMS.getCheckTable();
        String selectAirline = "SELECT DESCR, VERSION from " + schema + table + " WHERE CODE = ?";

        // Valid parameters to process
        try {
            PreparedStatement preparedStatement = TikaAirlineLoader.getConnection().prepareStatement(selectAirline,
                                                                                                     ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                                                     ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, code);
            ResultSet rs = preparedStatement.executeQuery();

            // Check if the result set is empty
            if (!rs.isBeforeFirst()) {
                // Insert a new airline
                String insertAirline = "INSERT INTO " + schema + table
                        + " (CODE, DESCR, CREATED, CREAUSER, UPDATED, UPDTUSER, VERSION)"
                        + " VALUES (?,?,?,?,?,?,?)";
                preparedStatement = TikaAirlineLoader.getConnection().prepareStatement(insertAirline);
                preparedStatement.setString(1, code);
                preparedStatement.setString(2, name);
                preparedStatement.setTimestamp(3, getCurrentTimeStamp());
                preparedStatement.setString(4, UPDTUSER);
                preparedStatement.setTimestamp(5, getCurrentTimeStamp());
                preparedStatement.setString(6, UPDTUSER);
                preparedStatement.setInt(7, 0);

                preparedStatement.executeUpdate();

                ++inserted;
            }
            else {
                // Update the current row in case values are empty
                while (rs.next()) {
                    String descr = rs.getString("DESCR");
                    int version = rs.getInt("VERSION");

                    boolean updateNeeded = false;

                    if (descr.isEmpty()) {
                        descr = name;
                        updateNeeded = true;
                    }

                    if (updateNeeded) {
                        ++version;
                        String updateAirline = "UPDATE " + schema + table + " SET DESCR = ?, UPDATED = ?, UPDTUSER = ?, VERSION = ? WHERE CODE = ?";
                        preparedStatement = TikaAirlineLoader.getConnection().prepareStatement(updateAirline);
                        preparedStatement.setString(1, descr);
                        preparedStatement.setTimestamp(2, getCurrentTimeStamp());
                        preparedStatement.setString(3, UPDTUSER);
                        preparedStatement.setInt(4, version);
                        preparedStatement.setString(5, code);

                        preparedStatement.executeUpdate();

                        ++updated;
                    }
                    else {
                        ++unchanged;
                    }
                }
            }
        }
        catch (SQLException ex) {
            LOG.severe(ex.getMessage());
        }
    }

    /**
     * Create the current Timestamp value.
     *
     * @return the current Timestamp value
     */
    private static Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }

    /**
     * Get the number of inserted rows.
     *
     * @return the number of inserts
     */
    public static int getInserted() {
        return inserted;
    }

    /**
     * Get the number of updated rows.
     *
     * @return the number of updates
     */
    public static int getUpdated() {
        return updated;
    }

    /**
     * Get the number of unchanged rows.
     *
     * @return the number of unchanged rows
     */
    public static int getUnchanged() {
        return unchanged;
    }
}
