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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import java.util.HashMap;
import java.util.Map;

public class SSLearnEpVersionSetTimelineStateRet extends SSServRetI{

  public SSUri learnEpTimelineStateUri = null;

  public static SSLearnEpVersionSetTimelineStateRet get(SSUri learnEpTimelineStateUri, SSMethU op){
    return new SSLearnEpVersionSetTimelineStateRet(learnEpTimelineStateUri, op);
  }
  
  private SSLearnEpVersionSetTimelineStateRet(SSUri learnEpTimelineStateUri, SSMethU op){
    
    super(op);
    this.learnEpTimelineStateUri = learnEpTimelineStateUri;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld = new HashMap<String, Object>();
    
    ld.put(SSVarU.learnEpTimelineStateUri, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public String getLearnEpTimelineStateUri() throws Exception {
    return SSUri.toStrWithoutSlash(learnEpTimelineStateUri);
  }
}
