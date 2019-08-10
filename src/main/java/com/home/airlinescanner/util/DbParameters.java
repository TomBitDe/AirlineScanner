package com.home.util;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * Database access and check parameters.
 */
public class DbParameters extends Parameters {
    /**
     * The logger for java logging (not log4j)
     */
    private static final Logger LOG = Logger.getLogger(DbParameters.class.getName());

    // These are the VALID KEYS for DbParameters class
    /**
     * Key to access the driver parameter
     */
    private static final String DRIVER_KEY = "jdbc.driver";
    /**
     * Key to access the connectstring parameter
     */
    private static final String CONNECT_STRING_KEY = "jdbc.connectstring";
    /**
     * Key to access the username parameter
     */
    private static final String USER_NAME_KEY = "jdbc.username";
    /**
     * Key to access the password parameter
     */
    private static final String PASSWORD_KEY = "jdbc.password";
    /**
     * Key to access the check table parameter
     */
    private static final String CHECK_TABLE_KEY = "check.table";
    /**
     * Key to access the db schema parameter
     */
    private static final String SCHEMA = "schema";

    // These are DEFAULT values for the specified KEYS
    /**
     * Default driver value
     */
    private String driver = "oracle.jdbc.driver.OracleDriver";
    /**
     * Default connectstring value
     */
    private String connectString = "jdbc\\:oracle\\:thin\\:@HOST\\:PORT\\:SID";
    /**
     * Default username value
     */
    private String userName = System.getProperty("user.name");
    /**
     * Default password value
     */
    private String password = "<Enter password for " + userName + " here>";
    /**
     * Default check table value
     */
    private String checkTable = "dummy";
    /**
     * Default schema name value
     */
    private String schema = "";

    /**
     * Creates new DbParameters.
     */
    public DbParameters() {
        super("AirportScanner.properties", "Database Parameters");
        getParameters();
    }

    /**
     * Creates new DbParameters.
     *
     * @param propertiesFilename the name of the properties file
     */
    public DbParameters(String propertiesFilename) {
        super(propertiesFilename, "Database Parameters");
        getParameters();
    }

    /**
     * Creates new DbParameters.
     *
     * @param propertiesFilename    the name of the properties file
     * @param propertiesDescription a brief description of the properties
     */
    public DbParameters(String propertiesFilename, String propertiesDescription) {
        super(propertiesFilename, propertiesDescription);
        getParameters();
    }

    @Override
    protected void updateSettingsFromProperties() {
        driver = properties.getProperty(DRIVER_KEY);
        connectString = properties.getProperty(CONNECT_STRING_KEY);
        userName = properties.getProperty(USER_NAME_KEY);
        password = properties.getProperty(PASSWORD_KEY);
        checkTable = properties.getProperty(CHECK_TABLE_KEY);
        schema = properties.getProperty(SCHEMA);
    }

    @Override
    protected void setDefaults(Properties defaults) {
        defaults.put(DRIVER_KEY, driver);
        defaults.put(CONNECT_STRING_KEY, connectString);
        defaults.put(USER_NAME_KEY, userName);
        defaults.put(PASSWORD_KEY, password);
        defaults.put(CHECK_TABLE_KEY, checkTable);
        defaults.put(SCHEMA, schema);
    }

    @Override
    protected void updatePropertiesFromSettings() {
        properties.put(DRIVER_KEY, driver);
        properties.put(CONNECT_STRING_KEY, connectString);
        properties.put(USER_NAME_KEY, userName);
        properties.put(PASSWORD_KEY, password);
        properties.put(CHECK_TABLE_KEY, checkTable);
        properties.put(SCHEMA, schema);
    }

    @Override
    public String toString() {
        return "["
                + driver + ","
                + connectString + ","
                + userName + ","
                + password + ","
                + checkTable + ","
                + schema + "]";
    }

    /**
     * Set the JDBC drivers name.
     *
     * @param driver the drivers name
     */
    public void setDriver(String driver) {
        this.driver = driver;
        saveParameters();
    }

    /**
     * Get the JDBC drivers name.
     *
     * @return the driver name
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Set the JDBC connect string to use.
     *
     * @param connectString the connect string to use
     */
    public void setConnectString(String connectString) {
        this.connectString = connectString;
        saveParameters();
    }

    /**
     * Get the current connect string.
     *
     * @return the connect string used
     */
    public String getConnectString() {
        return connectString;
    }

    /**
     * Set the username to use for the JDBC connect.
     *
     * @param userName the username for the JDBC connect
     */
    public void setUserName(String userName) {
        this.userName = userName;
        saveParameters();
    }

    /**
     * Get the current username for the JDBC connect.
     *
     * @return the used username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the password for the JDBC connect.
     *
     * @param password the password for the JDBC connect
     */
    public void setPassword(String password) {
        this.password = password;
        saveParameters();
    }

    /**
     * Get the current password for the JDBC connect.
     *
     * @return the used password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the name of the database table to use for the check.
     *
     * @param checkTable the database table name
     */
    public void setCheckTable(String checkTable) {
        this.checkTable = checkTable;
        saveParameters();
    }

    /**
     * Get the name of the database table used for checking.
     *
     * @return the database table name
     */
    public String getCheckTable() {
        return checkTable;
    }

    /**
     * Set the dn schema name.
     *
     * @param schema the db schema name
     */
    public void setSchema(String schema) {
        this.schema = schema;
        saveParameters();
    }

    /**
     * Get the db schema name.
     *
     * @return the db schema name
     */
    public String getSchema() {
        return schema;
    }
}
