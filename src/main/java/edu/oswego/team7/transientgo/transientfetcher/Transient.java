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

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class Transient {
    
    private final String ivorn;
    private final String id;
    private final double rightAscension;
    private final double declination;
    private final String dateTime;
    private final double magnitude;
    private final String imageURL;
    private final String lightCurveURL;

    public Transient(String ivorn, String id, double rightAscension, double declination, String dateTime, double magnitude, String imageURL, String lightCurveURL) {
        this.ivorn = ivorn;
        this.id = id;
        this.rightAscension = rightAscension;
        this.declination = declination;
        this.dateTime = dateTime;
        this.magnitude = magnitude;
        this.imageURL = imageURL;
        this.lightCurveURL = lightCurveURL;
    }

    public String getIvorn() {
        return ivorn;
    }

    public String getId() {
        return id;
    }

    public double getRightAscension() {
        return rightAscension;
    }

    public double getDeclination() {
        return declination;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getLightCurveURL() {
        return lightCurveURL;
    }

}
