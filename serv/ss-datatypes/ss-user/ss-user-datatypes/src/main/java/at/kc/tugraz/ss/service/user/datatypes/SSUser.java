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
package at.kc.tugraz.ss.service.user.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import java.util.HashMap;
import java.util.Map;

public class SSUser extends SSEntity{

  public String      email   = null;
  
  public static SSUser get(
    final SSUser         user,
    final SSEntity       entity) throws Exception{
    
    return new SSUser(user, entity);
  }
  
  protected SSUser(
    final SSUser         user,
    final SSEntity       entity) throws Exception{
    
    super(entity);
    
    this.email      = user.email;
  }
  
  public static SSUser get(
    final SSUri    id,
    final SSLabel  label,
    final String   email) throws Exception{
    
    return new SSUser(id, label, email);
  }
  
  protected SSUser(
    final SSUri   id,
    final SSLabel label,
    final String  email) throws Exception{
    
    super(id, SSEntityE.user, label);
    
    this.email = email;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld         = (Map<String, Object>) super.jsonLDDesc();
    final Map<String, Object> friendsObj = new HashMap<>();
    
    ld.put(SSVarU.email,   SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    
    return ld;
  }
}