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
package at.kc.tugraz.socialserver.service.broadcast.datatypes;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.enums.SSBroadcastEnum;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import java.util.HashMap;
import java.util.Map;

public class SSBroadcast implements SSJSONLDPropI{

  public SSUri             entity    = null;
  public SSBroadcastEnum   type      = null;
  public SSUri             user      = null;
  public Object            content   = null;
  public Long              timestamp = -1L;

  public static SSBroadcast get(
    final SSBroadcastEnum    type, 
    final SSUri              entity, 
    final SSUri              user,
    final Object             content) {
    
    return new SSBroadcast(type, entity, user, content);
  }

  private SSBroadcast(
    final SSBroadcastEnum    type, 
    final SSUri              entity, 
    final SSUri              user,
    final Object             content){
    
    this.type      = type;
    this.entity    = entity;
    this.user      = user;
    this.timestamp = System.currentTimeMillis();
    this.content   = content;
  }
  
  public SSBroadcast(){}
  
  @Override
  public Object jsonLDDesc() {
    
    Map<String, Object> ld = new HashMap<>();
    
    ld.put(SSVarU.entity,      SSVarU.sss  + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.type,        SSVarU.sss  + SSStrU.colon + SSBroadcastEnum.class.getName());
    ld.put(SSVarU.user,        SSVarU.sss  + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.timestamp,   SSVarU.xsd  + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.content,     SSVarU.xsd  + SSStrU.colon + SSStrU.valueBoolean);
    
    return ld;
  }
}
