/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package sss.serv.eval.datatypes;

import at.tugraz.sss.serv.SSJSONLDPropI;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import java.util.ArrayList;
import java.util.List;

public enum SSEvalLogE implements SSJSONLDPropI{
  
  //knowbrain
  circleCreate,
  circleEntitiesAdd,
  circleEntitiesAddTagsAdd,
  circleEntitiesAddCategoriesAdd,
  circleUsersAdd,
  circleEntitiesRemove,
  circleRemove,
  circleUsersRemove,
  entityCopy,
  fileUpload,
  fileDowload,
  categoryAdd,
  tagsRemove,
  categoriesRemove,
  recommTagsQuery,
  recommTagsResult,
  
  //bits and pieces
  addNotebook, //server
  addNote, //server
  addResource, //server
  copyLearnEpForUser, //server
  shareLearnEpWithUser,  //server
  removeLearnEpVersionCircle, //server
  removeLearnEpVersionEntity, //server
  addEntityToLearnEpVersion, //server
  addCircleToLearnEpVersion, //server
  addEntityToLearnEpCircle, //server
  removeEntityFromLearnEpCircle, //server
  removeLearnEpVersionCircleWithEntitites, //server
  changeLabel,  //client | server
  changeDescription, //client | server
  addTag, //client | server
  clickBit,  //client
  clickTag,  //client
  clickLabelRecommendation,   //client
  clickTagRecommendation, //client
  clickJumpToDateButton,   //client
  clickAffectButton, //client
  clickHelpButton,  //client
  searchWithKeyword,//client
  readMessage, //client
  sendMessage, //client
  setImportance, //client
  removeTag, //client
  setFilter, //client
  removeFilter, //client
  executeJumpToDateButton, //client
  requestEditButton,  //client
  releaseEditButton, //client
  
  addDiscEntry, //server
  discussEntity, //server
  attachEntities, //server
  removeEntities, //server
  
  //knowbrain && bits and pieces
  tagAdd //server
  
  ;
  
  @Override
  public Object jsonLDDesc() {
    return SSVarNames.xsd + SSStrU.colon + SSStrU.valueString;
  }
  
  public static SSEvalLogE get(final String space) throws Exception{
    
    try{
      
      if(space == null){
        return null;
      }
      
      return SSEvalLogE.valueOf(space);
    }catch(Exception error){
      throw new Exception("tool context nvalid");
    }
  }
  
  public static List<SSEvalLogE> get(final List<String> strings) throws Exception{

    final List<SSEvalLogE> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
}