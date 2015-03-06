/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
* For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
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
package at.kc.tugraz.sss.flag.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import java.util.Map;

public class SSFlag extends SSEntity{
  
  public SSUri   user          = null;
  public SSUri   entity        = null;
  public SSFlagE flagType      = null;
  public Long    endTime       = null;
  public Integer value         = null;
  
  public static SSFlag get(
    final SSUri   id,
    final SSUri   user,
    final SSUri   entity,
    final SSFlagE flagType,
    final Long    endTime,
    final Integer value) throws Exception{
    
    return new SSFlag(id, user, entity, flagType, endTime, value);
  }
  
  protected SSFlag(
    final SSUri   id,
    final SSUri   user,
    final SSUri   entity,
    final SSFlagE flagType,
    final Long    endTime,
    final Integer value) throws Exception{
    
    super(id, SSEntityE.flag);
    
    this.user         = user;
    this.entity       = entity;
    this.flagType     = flagType;
    this.endTime      = endTime;
    this.value        = value;
  }

  @Override
  public Object jsonLDDesc(){
  
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarU.user,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.entity,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.flagType,     SSVarU.sss + SSStrU.colon + SSFlagE.class.getName());
    ld.put(SSVarU.endTime,      SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.value,        SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    
    return ld;
  }
  
  /* json getters */
  public String getUser() throws Exception{
    return SSStrU.removeTrailingSlash(user);
  }
  
  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getFlagType() throws Exception{
    return SSStrU.toStr(flagType);
  }
}