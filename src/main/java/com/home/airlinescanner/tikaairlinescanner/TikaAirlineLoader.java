package com.home.airlinescanner.tikaairlinescanner;

import com.home.util.DbHandler;
import com.home.util.DbParameters;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * The Tika Airline Loader.<p>
 * Scan all URL <code>https://en.wikipedia.org/wiki/List_of_airlines_by_IATA_code:_</code> for airlines and put them in
 * a database table.
 */
public class TikaAirlineLoader {
    private static final Logger LOG = Logger.getLogger(TikaAirlineLoader.class.getName());

    private static final String TEMP_PREFIX = "airlines";
    private static final String TEMP_POSTFIX = ".html";
    private static final String URL_PREFIX = "https://en.wikipedia.org/wiki/List_of_airline_codes_(";
    private static final String URL_POSTFIX = ")";
    private static Connection conn;

    /**
     * Start the Tika Airline Loader.<p>
     * Currently no arguments are evaluated.
     *
     * @param args the arguments
     *
     * @throws Exception     on general exceptions
     * @throws IOException   on any IO exception
     * @throws SAXException  on any SAX exception
     * @throws TikaException on any Tika exception
     */
    public static void main(final String[] args) throws Exception, IOException, SAXException, TikaException {
        File tempFile;
        FileInputStream inputstream;
        ContentHandler handler;
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();
        HtmlParser htmlparser = new HtmlParser();

        DbParameters dp = new DbParameters("AirlineScanner.properties");
        DbHandler.loadDbDriver(dp.getDriver());
        conn = DbHandler.connectToDb(dp);

        // (0-9) Airlines
        tempFile = createTempFile(URL_PREFIX + "0-9" + URL_POSTFIX);

        inputstream = new FileInputStream(tempFile);
        handler = new AirlineTable6ColHandler();

        System.out.println("--- " + "0-9" + " -----------------------------------------------------");
        //Html parser
        htmlparser.parse(inputstream, handler, metadata, pcontext);

        inputstream.close();
        tempFile.delete();

        // All other airlines
        for (char postFix = 'A'; postFix <= 'Z'; ++postFix) {
            tempFile = createTempFile(URL_PREFIX + postFix + URL_POSTFIX);

            inputstream = new FileInputStream(tempFile);
            handler = new AirlineTable6ColHandler();

            System.out.println("--- " + postFix + " -----------------------------------------------------");
            //Html parser
            htmlparser.parse(inputstream, handler, metadata, pcontext);

            inputstream.close();
            tempFile.delete();
        }

        DbHandler.cleanupJdbc();

        LOG.log(Level.INFO, "Unchanged: {0} Inserted: {1} Updated: {2}", new Object[]{AirlineProcessor.getUnchanged(),
                                                                                      AirlineProcessor.getInserted(),
                                                                                      AirlineProcessor.getUpdated()});
    }

    private static File createTempFile(String sUrl) throws IOException {
        File tempFile = File.createTempFile(TEMP_PREFIX, TEMP_POSTFIX);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            URL url = new URL(sUrl);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    bw.write(inputLine);
                }
            }
        }

        return tempFile;
    }

    /**
     * Get the database connection created in here for further processing.
     *
     * @return the connection
     */
    public static Connection getConnection() {
        return conn;
    }
}
