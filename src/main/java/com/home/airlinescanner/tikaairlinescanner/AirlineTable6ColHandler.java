package com.home.airlinescanner.tikaairlinescanner;

import org.xml.sax.SAXException;

/**
 * Handle a data table with 6 columns of content.<p>
 * Columns are iata code, icao code, airline name and airline location, timezone information, timezone change
 * information. Currently the two timezone informations are not used in further processing.<br>
 * Call the <code>AirlineProcessor</code> if valid data are found.
 */
public class AirlineTable6ColHandler extends AirlineTableColHandler {
    int idx = 0;
    String iata;
    String icao;
    String name;
    String callSign;
    String country;
    String comments;

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (col) {
            String val = new String(ch, start, length);

            switch (idx) {
                case 0:
                    iata = val.replaceAll("\n", "").replaceAll("[^a-zA-Z0-9]", "");
                    break;
                case 1:
                    icao = val.replaceAll("\n", "");
                    break;
                case 2:
                    name = val.replaceAll("\n", "");
                    break;
                case 3:
                    callSign = val.replaceAll("\n", "");
                    break;
                case 4:
                    country = val.replaceAll("\n", "");
                    break;
                case 5:
                    comments = val.replaceAll("\n", "");
                    break;
                default:
                    throw new IndexOutOfBoundsException("idx is > 5");
            }

            col = false;
            ++idx;
            if (idx == 6) {
                System.out.println("IATA=[" + iata + "] ICAO=[" + icao + "] name=[" + name + "] callSign=[" + callSign + "] country=[" + country + "] comments=[" + comments + ']');
                // Only the following is valid
                if (!iata.trim().isEmpty()) {
                    if (iata.trim().length() == 2) {
                        AirlineProcessor.processAirline(iata, name);
                    }
                }
                else if (!icao.trim().isEmpty()) {
                    if (icao.trim().length() == 3) {
                        AirlineProcessor.processAirline(icao, name);
                    }
                }
                idx = 0;
            }
        }
    }
}
