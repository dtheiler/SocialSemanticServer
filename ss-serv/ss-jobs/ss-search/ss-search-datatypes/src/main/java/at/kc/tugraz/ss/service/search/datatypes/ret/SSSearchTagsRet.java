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
package at.kc.tugraz.ss.service.search.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSSearchTagsRet extends SSServRetI{

  public List<SSSearchResult> searchResults = new ArrayList<SSSearchResult>();

  public static SSSearchTagsRet get(List<SSSearchResult> searchResults, SSMethU op){
    return new SSSearchTagsRet(searchResults, op);
  }
  
  private SSSearchTagsRet(List<SSSearchResult> searchResults, SSMethU op){
    
    super(op);
    
    this.searchResults = searchResults;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld               = new HashMap<String, Object>();
    Map<String, Object> searchResultsObj = new HashMap<String, Object>();
    
    searchResultsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSSearchResult.class.getName());
    searchResultsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.searchResults, searchResultsObj);
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public List<SSSearchResult> getSearchResults() {
    return searchResults;
  }
}