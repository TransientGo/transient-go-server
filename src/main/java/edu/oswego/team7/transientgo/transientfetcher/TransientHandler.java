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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class TransientHandler extends DefaultHandler{
    
    private Transient transientObject;

    StringBuffer accumulator;
    private String ivorn;
    private String id;
    private double rightAscension;
    private double declination;
    private boolean isDateTime;
    private String dateTime;
    private double magnitude;
    private String imageURL;
    private String lightCurvePageURL;

    public void startDocument() throws SAXException {
        accumulator = new StringBuffer();
        isDateTime = false;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (qName.equals("voe:VOEvent")) {
            ivorn = atts.getValue("ivorn");
        } else if (qName.equals("Param")) {
            switch (atts.getValue("name")) {
                case "ID":
                    id = atts.getValue("value");
                    break;
                case "RA":
                    rightAscension = Double.parseDouble(atts.getValue("value"));
                    break;
                case "Dec":
                    declination = Double.parseDouble(atts.getValue("value"));
                    break;
                case "magnitude":
                    magnitude = Double.parseDouble(atts.getValue("value"));
                    break;
                case "imgmaster":
                    imageURL = atts.getValue("value");
                    break;
                case "lightcurve":
                    lightCurvePageURL = atts.getValue("value");
                default:
                    break;
            }
        } else if (qName.equals("ISOTime")) {
            isDateTime = true;
        }
    }

    public void characters(char[] ch, int start, int length) {
        if (isDateTime) {
            accumulator.append(ch, start, length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("ISOTime")) {
            dateTime = accumulator.toString();
            accumulator.setLength(0);
            isDateTime = false;
        }
    }

    public void endDocument() throws SAXException {
        StringBuilder lightCurveBuffer = new StringBuilder(lightCurvePageURL);
        lightCurveBuffer.insert(lightCurveBuffer.lastIndexOf("/") + 1, "imgs/");
        lightCurveBuffer.replace(lightCurveBuffer.indexOf("p.html"), lightCurveBuffer.length(), ".png");
        String lightCurveURL = lightCurveBuffer.toString();
        transientObject = new Transient(ivorn, id, rightAscension, declination, dateTime, magnitude, imageURL, lightCurveURL);
    }

    public Transient getTransient() {
        return transientObject;
    }
}
