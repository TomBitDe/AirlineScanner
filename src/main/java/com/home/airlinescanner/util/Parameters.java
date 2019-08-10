package com.home.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General methods to access parameters in a property file.
 */
public abstract class Parameters {
    /**
     * The logger for java logging (not log4j)
     */
    private static final Logger LOG = Logger.getLogger(Parameters.class.getName());
    /**
     * The defaults marker
     */
    private static final String DEFAULTS = "DEFAULTS";

    /**
     * The property file name to use
     */
    private final String propertiesFilename;
    /**
     * A brief description of the properties
     */
    private final String propertiesDescription;
    /**
     * The properties origin like DEFAULTS or file
     */
    private String propertiesOrigin;

    /**
     * The properties
     */
    protected Properties properties = null;

    /**
     * Create new parameters.
     *
     * @param propertiesFilename    the properties file to load the parameters from
     * @param propertiesDescription a brief description of the properties
     */
    protected Parameters(String propertiesFilename, String propertiesDescription) {
        LOG.log(Level.FINE, "Parameters from [{0}]", propertiesFilename);

        this.propertiesFilename = propertiesFilename;
        this.propertiesDescription = propertiesDescription;
        this.propertiesOrigin = DEFAULTS;
    }

    /**
     * Set default values for the properties.
     *
     * @param defaults the defaults as Properties
     */
    abstract protected void setDefaults(Properties defaults);

    /**
     * Update the properties from the current settings.
     */
    abstract protected void updatePropertiesFromSettings();

    /**
     * Update the settings from the properties.
     */
    abstract protected void updateSettingsFromProperties();

    /**
     * Retrieve the parameters origin.
     *
     * @return the origin like DEFAULTS or filename
     */
    protected String getParamOrigin() {
        return propertiesOrigin;
    }

    /**
     * Retrieve the needed parameters.<p>
     * In case of any error the defauls are given.
     */
    protected void getParameters() {
        Properties defaults = new Properties();
        FileInputStream in = null;
        String folder = getConfigFolder();
        String filesep = System.getProperty("file.separator");

        setDefaults(defaults);

        properties = new Properties(defaults);

        try {
            in = new FileInputStream(folder
                    + filesep
                    + propertiesFilename);
            properties.load(in);
            this.propertiesOrigin = folder + filesep + propertiesFilename;
            LOG.log(Level.INFO, "Parameters loaded from [{0}]", propertiesFilename);
        }
        catch (java.io.FileNotFoundException e) {
            in = null;
            LOG.log(Level.WARNING, "Can''t find properties file {0}{1}{2}. Using defaults.", new Object[]{folder, filesep, propertiesFilename});
        }
        catch (java.io.IOException e) {
            LOG.log(Level.WARNING, "Can''t read properties file. {0}{1}{2}. Using defaults.", new Object[]{folder, filesep, propertiesFilename});
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (java.io.IOException e) {
                }
            }
        }

        updateSettingsFromProperties();
    }

    /**
     * Save the parameters in a property file.
     */
    protected void saveParameters() {

        updatePropertiesFromSettings();

        LOG.log(Level.FINE, "Just set properties: {0}", propertiesDescription);
        LOG.fine(toString());

        FileOutputStream out = null;

        try {
            String folder = getConfigFolder();
            String filesep = System.getProperty("file.separator");
            out = new FileOutputStream(folder
                    + filesep
                    + propertiesFilename);
            properties.store(out, propertiesDescription);
        }
        catch (java.io.IOException e) {
            LOG.warning("Can't save properties. "
                    + "Oh well, it's not a big deal.");
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (java.io.IOException e) {
                }
            }
        }
    }

    /**
     * Get the path of the current configuration folder.
     *
     * @return the folders path
     */
    protected String getConfigFolder() {
        String folder = System.getProperty("ConfigFolder");

        if (folder == null) {
            folder = System.getProperty("user.dir");
        }
        else {
            String tmp = folder.trim();

            if (tmp.equals("")) {
                folder = System.getProperty("user.dir");
            }
            else {
                // folder is acceptable
            }
        }
        LOG.log(Level.INFO, "Parameter config folder is [{0}]", folder);

        return folder;
    }
}
