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
 package at.kc.tugraz.ss.service.search.datatypes;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import static at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA.contains;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.service.rating.datatypes.*;
import java.util.*;

public class SSSearchResult extends SSEntityA{

  public  SSUri                   uri                             = null;
  public  SSSpaceE                space                           = null;
  public  String                  label                           = null;
  public  SSEntityE               type                            = null;

  public SSSearchResult(
    SSUri       uri,
    SSSpaceE    space) throws Exception{
    
    super(uri);
    
    this.uri    = uri;
    this.space  = space;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri,           SSVarU.sss + SSStrU.colon  + SSUri.class.getName());
    ld.put(SSVarU.space,         SSVarU.sss + SSStrU.colon  + SSSpaceE.class.getName());
    ld.put(SSVarU.label,         SSVarU.xsd + SSStrU.colon  + SSStrU.valueString);
    ld.put(SSVarU.type,          SSVarU.sss + SSStrU.colon  + SSEntityE.class.getName());
    ld.put(SSVarU.userRating,    SSVarU.xsd + SSStrU.colon  + SSStrU.valueInteger);
    ld.put(SSVarU.overallRating, SSVarU.sss + SSStrU.colon  + SSRatingOverall.class.getName());
    
    return ld;
  }

  /* getters to allow for json enconding */
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }

  public String getSpace(){
    return SSSpaceE.toStr(space);
  }

  public String getLabel(){
    return label;
  }

  public String getType(){
    return SSEntityE.toStr(type);
  }
  
  public static void addDistinct(
    final List<SSSearchResult> entities,
    final List<SSSearchResult> toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSSearchResult entity : toAddEntities){

      if(!contains(entities, entity)){
        entities.add(entity);
      }
    }
  }
}