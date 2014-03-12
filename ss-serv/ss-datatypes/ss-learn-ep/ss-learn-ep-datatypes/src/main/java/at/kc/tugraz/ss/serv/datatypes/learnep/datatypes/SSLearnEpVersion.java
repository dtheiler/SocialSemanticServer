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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLearnEpVersion extends SSEntityA {

  public SSUri                 learnEpVersionUri          = null;
  public SSUri                 learnEpUri                 = null;
  public String                timestamp                  = null;
  public List<SSLearnEpEntity> entities                   = new ArrayList<SSLearnEpEntity>();
  public List<SSLearnEpCircle> circles                    = new ArrayList<SSLearnEpCircle>();

  public static SSLearnEpVersion get(SSUri learnEpVersionUri, SSUri learnEpUri, String timestamp, List<SSLearnEpEntity> entities, List<SSLearnEpCircle> circles){
    return new SSLearnEpVersion(learnEpVersionUri, learnEpUri, timestamp, entities, circles);
  }
  
  private SSLearnEpVersion(SSUri learnEpVersionUri, SSUri learnEpUri, String timestamp, List<SSLearnEpEntity> entities, List<SSLearnEpCircle> circles){
    
    super(learnEpVersionUri);
    
    this.learnEpVersionUri   = learnEpVersionUri;
    this.learnEpUri          = learnEpUri;
    this.timestamp           = timestamp;
    
    if(SSObjU.isNotNull(entities)){
      this.entities = entities;
    }
    
    if(SSObjU.isNotNull(circles)){
      this.circles = circles;
    }
  }
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld         = new HashMap<String, Object>();
    
    ld.put(SSVarU.learnEpVersionUri,  SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.learnEpUri,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.timestamp,          SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    
    Map<String, Object> entitiesObj = new HashMap<String, Object>();
    
    entitiesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSLearnEpEntity.class.getName());
    entitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entities, entitiesObj);
    
    Map<String, Object> circlesObj = new HashMap<String, Object>();
    
    circlesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSLearnEpCircle.class.getName());
    circlesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circles, circlesObj);
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public String getLearnEpVersionUri() throws Exception {
    return SSUri.toStrWithoutSlash(learnEpVersionUri);
  }

  public String getLearnEpUri() throws Exception {
    return SSUri.toStrWithoutSlash(learnEpUri);
  }

  public String getTimestamp() {
    return timestamp;
  }
  
  public List<SSLearnEpEntity> getEntities(){
    return entities;
  }
  
  public List<SSLearnEpCircle> getCircles(){
    return circles;
  }
}
