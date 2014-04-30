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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import java.util.HashMap;
import java.util.Map;

public class SSEntity extends SSEntityA{

  public SSUri        uri          = null;
  public SSLabel      label        = null;
  public Long         creationTime = null;
  public SSEntityE    type         = null;
  public SSUri        author       = null;
  
  protected SSEntity(
    final SSUri          uri,
    final SSLabel        label, 
    final Long           creationTime,
    final SSEntityE      type,
    final SSUri          author) throws Exception{
    
    super(uri);
    
    this.uri          = uri;
    this.label        = label;
    this.creationTime = creationTime;
    this.type         = type;
    this.author       = author;
  }
  
  public static SSEntity get(
    final SSUri          uri,
    final SSLabel        label, 
    final Long           creationTime,
    final SSEntityE      type,
    final SSUri          author) throws Exception{
    
    return new SSEntity(uri, label, creationTime, type, author);
  }

  @Override
  public Object jsonLDDesc() {
   
    final Map<String, Object> ld         = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri,            SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,          SSVarU.sss + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.creationTime,   SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.type,           SSVarU.sss + SSStrU.colon + SSEntityE.class.getName());
    ld.put(SSVarU.author,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    
    return ld;
  }
}
