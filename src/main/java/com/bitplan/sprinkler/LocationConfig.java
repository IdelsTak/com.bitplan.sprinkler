/**
 * Copyright (c) 2018 BITPlan GmbH
 *
 * http://www.bitplan.com
 *
 * This file is part of the Opensource project at:
 * https://github.com/BITPlan/com.bitplan.sprinkler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitplan.sprinkler;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openweathermap.weather.Coord;
import org.openweathermap.weather.Location;

import com.bitplan.json.JsonAble;

/**
 * Location Configuration
 * 
 * @author wf
 *
 */
public class LocationConfig implements JsonAble {
  protected static Logger LOGGER = Logger
      .getLogger("com.bitplan.sprinkler");
  
  String name;
  String country;
  String lat;
  String lon;
  private Long id;
  Long dwdid;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public void reinit() {

  }

  /**
   * get the location for this configuration
   * 
   * @return - the location
   */
  public Location getLocation() {
    Location location = null;
    try {
      if (getId() != null && !(getId()==0)) {
        location = Location.byId(getId());
      } else if (name != null && country!=null) {
        location = Location.byName(getFullName());
      }
    } catch (Throwable th) {
      // TODO handle exception
      String msg=String.format("could not find location: %s name: %s",getId()==null?"?":""+getId(),name==null?"?":name);
      LOGGER.log(Level.WARNING, msg, th);
    }
    if (location == null) {
      location = new Location();
      location.setId(getId());
      location.setName(name);
      location.setCountry(country);
    }
    //// @TODO set coordinates from DMS / search by DMS
    // Coord coord = new Coord();
    // location.setCoord(coord);
    return location;
  }
  
  /**
   * get the full name of the location
   * @return - the fullname
   */
  public String getFullName() {
    return name+"/"+country;
  }

  /**
   * configure me from the given location
   * 
   * @param location
   */
  public void fromLocation(Location location) {
    if (location == null)
      return;
    this.setId(location.getId());
    this.name = location.getName();
    this.country = location.getCountry();
    Coord coord = location.getCoord();
    if (coord == null)
      return;
    this.lat = coord.getLatDMS();
    this.lon = coord.getLonDMS();
  }

  @Override
  public void fromMap(Map<String, Object> map) {
    this.name = (String) map.get("name");
    this.country = (String) map.get("country");
    this.lat = (String) map.get("lat");
    this.lon = (String) map.get("long");
    this.setId((Long)map.get("id"));
    this.dwdid = (Long) map.get("dwdid");
  }

}
