/*
 * Copyright (C) 2016 Shakhar Dasgupta <sdasgupt@oswego.edu>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.oswego.team7.transientgo.transientfetcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class TransientFetcher {

    public static Transient getTransient(String ivorn) {
        try {
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            TransientHandler handler = new TransientHandler();
            xmlReader.setContentHandler(handler);
            String encodedIvorn = URLEncoder.encode(ivorn, "UTF-8");
            URL xmlUrl = new URL("http://voeventdb.4pisky.org/apiv1/packet/xml/" + encodedIvorn);
            try (InputStream xmlInputStream = xmlUrl.openStream()) {
                InputSource inputSource = new InputSource(xmlInputStream);
                xmlReader.parse(inputSource);
            }
            return handler.getTransient();
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            return null;
        }
    }
}
