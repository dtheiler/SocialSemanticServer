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
package at.kc.tugraz.ss.serv.job.recomm.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSLinkU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSRecommFct{

  public static Boolean exportEntityTagTimestampCategoryCombinationsForAllUsers(
    final String fileName) throws Exception {
    
    Boolean                   somethingExported = false;
    Map<String, List<String>> tagsPerEntities;
    
    for(SSUri user : SSServCaller.getAllUsers()){
      
      tagsPerEntities = SSRecommFct.getTagsOfUserPerEntities(user, SSSpaceEnum.sharedSpace);
      
      if(tagsPerEntities.isEmpty()){
        continue;
      }
      
      //TODO dtheiler: provide various sortings by timestamps
      //TODO dtheiler: allow more than one file per algo
      //TODO dtheiler: for categories try out all tags of a resource as long as we have no category information
      SSServCaller.dataExportUserResourceTimestampTagsCategories(
        user,
        tagsPerEntities,
        SSRecommFct.getCategoriesPerEntities                  (tagsPerEntities.size()),
        SSRecommFct.getTimestampInMillisecOfAUserTagForEntity (user, tagsPerEntities.entrySet().iterator().next().getKey(), SSSpaceEnum.sharedSpace),
        fileName,
        false);
      
      somethingExported = true;
    }
    
    if(!somethingExported){
      return false;
    }
    
    SSServCaller.dataExportUserResourceTimestampTagsCategories(
      null,
      new HashMap<String, List<String>>(),
      new HashMap<String, List<String>>(),
      null,
      fileName,
      true);
    
    return true;
  }
  
  private static Map<String, List<String>> getTagsOfUserPerEntities(
    final SSUri       userUri, 
    final SSSpaceEnum space) throws Exception{
    
    return SSTag.getTagLabelsPerEntities(
      SSServCaller.tagsUserGet(
        userUri, 
        null, 
        null, 
        space));
  }

  private static Map<String, List<String>> getCategoriesPerEntities(
    final Integer numberOfEntities){
    
    final Map<String, List<String>> categoriesPerEntities = new HashMap<String, List<String>>();
    
    for(Integer counter = 0; counter < numberOfEntities; counter++){
      categoriesPerEntities.put(SSLinkU.dummyUri + SSStrU.underline + counter.toString(), new ArrayList<String>());
    }
    
    return categoriesPerEntities;
  }

  private static Long getTimestampInMillisecOfAUserTagForEntity(
    final SSUri       userUri, 
    final String      entityUri, 
    final SSSpaceEnum space) throws Exception{
    
    final List<SSTag> tags = 
      SSServCaller.tagsUserGet(
        userUri,
        SSUri.get(entityUri),
        null,
        space);
    
    if(tags.isEmpty()){
      return 0L;
    }
    
    return SSServCaller.entityCreationTimeGet(tags.get(0).uri) / 1000;
  }
}