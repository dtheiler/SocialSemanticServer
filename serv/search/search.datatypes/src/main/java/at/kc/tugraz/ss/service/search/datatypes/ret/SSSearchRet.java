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
package at.kc.tugraz.ss.service.search.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSSearchRet extends SSServRetI{
  
  public String         pagesID     = null;
  public Integer        pageNumber  = null;
  public Integer        pageNumbers = null;
  public List<SSEntity> entities    = new ArrayList<>();
  
  public static SSSearchRet get(
    final List<SSEntity> entities,
    final String         pagesID,
    final Integer        pageNumber,
    final Integer        pageNumbers,
    final SSMethU        op){
    
    return new SSSearchRet(entities, pagesID, pageNumber, pageNumbers, op);
  }
  
  private SSSearchRet(
    final List<SSEntity> entities,
    final String         pagesID,
    final Integer        pageNumber,
    final Integer        pageNumbers,
    final SSMethU        op){
    
    super(op);
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    this.pagesID     = pagesID;
    this.pageNumber  = pageNumber;
    this.pageNumbers = pageNumbers;
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld          = new HashMap<>();
    final Map<String, Object> entitiesObj = new HashMap<>();
    
    entitiesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntity.class.getName());
    entitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entities, entitiesObj);
    
    ld.put(SSVarU.pagesID,     SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.pageNumber,  SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    ld.put(SSVarU.pageNumbers, SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    
    return ld;
  }
}
