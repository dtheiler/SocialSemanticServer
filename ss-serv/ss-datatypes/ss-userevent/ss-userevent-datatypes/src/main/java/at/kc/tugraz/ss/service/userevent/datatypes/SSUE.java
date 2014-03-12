/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package at.kc.tugraz.ss.service.userevent.datatypes;

import at.kc.tugraz.ss.datatypes.datatypes.SSUEEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSUE extends SSEntityA {

  public         SSUri            uri        = null;
  public         SSUri            user       = null;
  public         SSUri            resource   = null;
  public         SSUEEnum         type       = null;
  public         String           content    = null;
  public         Long             timestamp  = -1L;

  public static SSUE get(
    final SSUri           uri, 
    final SSUri           user,
    final SSUEEnum        type,
    final SSUri           resource,
    final String          content,
    final Long            timestamp){
    
    return new SSUE(uri, user, type, resource, content, timestamp);
  }
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld = new HashMap<String, Object>();

    ld.put(SSVarU.uri,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.user,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.resource,   SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.type,       SSVarU.sss + SSStrU.colon + SSUEEnum.class.getName());
    ld.put(SSVarU.content,    SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.timestamp,  SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    
    return ld;
  }

  /**
   * 0 sorts SSUserEventEnum array ascending according to time
   */
  public static List<SSUE> sort(final List<SSUE> toSortUserEvents) {

    boolean    changedSomething = true;
    SSUE[]     helper           = new SSUE[toSortUserEvents.size()];
    List<SSUE> result           = new ArrayList<SSUE>();
    SSUE       storage;
    
    for (int counter = 0; counter < toSortUserEvents.size(); counter++) {

      helper[counter] = toSortUserEvents.get(counter);
    }

    while (changedSomething) {

      changedSomething = false;

      for (int counter = 0; counter < toSortUserEvents.size(); counter++) {

        if (counter + 1 < toSortUserEvents.size()) {

          if (helper[counter].timestamp > helper[counter + 1].timestamp) {

            storage = helper[counter];

            helper[counter] = helper[counter + 1];
            helper[counter + 1] = storage;

            changedSomething = true;
          }
        }
      }
    }
    
    result.addAll(Arrays.asList(helper));

    return result;
  }
  
	public static boolean isContentCorrect(final SSUE event) {
		return SSStrU.notEquals(event.content, SSStrU.valueNA);
	}
  
  private SSUE(
    SSUri           uri, 
    SSUri           user,
    SSUEEnum        type,
    SSUri           resource,
    String          content,
    Long            timestamp) {

    super(SSStrU.toString(type));
    
    this.uri        = uri;
    this.user       = user;
    this.type       = type;
    this.resource   = resource;
    this.content    = content;
    this.timestamp  = timestamp;
  }

  /*************** getters to allow for json enconding ********************/
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }

  public String getUser() throws Exception{
    return SSUri.toStrWithoutSlash(user);
  }

  public String getType(){
    return SSUEEnum.typeToStr(type);
  }

  public String getResource() throws Exception{
    return SSUri.toStrWithoutSlash(resource);
  }

  public String getContent(){
    return content;
  }

  public Long getTimestamp(){
    return timestamp;
  }
}