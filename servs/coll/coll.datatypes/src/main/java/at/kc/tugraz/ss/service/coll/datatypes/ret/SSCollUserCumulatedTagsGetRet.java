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
package at.kc.tugraz.ss.service.coll.datatypes.ret;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSJSONLDU;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCollUserCumulatedTagsGetRet extends SSServRetI{

  public final List<SSTagFrequ> tagFrequs = new ArrayList<>();

  public static SSCollUserCumulatedTagsGetRet get(final List<SSTagFrequ> tagFrequs, SSServOpE op){
    return new SSCollUserCumulatedTagsGetRet(tagFrequs, op);
  }
  
  private SSCollUserCumulatedTagsGetRet(final List<SSTagFrequ> tagFrequs, SSServOpE op) {

    super(op);
    
    if(tagFrequs != null){
      this.tagFrequs.addAll(tagFrequs);
    }
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld           = new HashMap<>();
    Map<String, Object> tagFrequsObj = new HashMap<>();
    
    tagFrequsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSTagFrequ.class.getName());
    tagFrequsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.tagFrequs, tagFrequsObj);
    
    return ld;
  }
  
  /* json getters */
  public List<SSTagFrequ> getTagFrequs(){
    return tagFrequs;
  }
}
