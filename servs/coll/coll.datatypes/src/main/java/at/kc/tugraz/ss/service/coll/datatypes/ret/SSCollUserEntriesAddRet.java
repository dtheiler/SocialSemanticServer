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
import java.util.HashMap;
import java.util.Map;

public class SSCollUserEntriesAddRet extends SSServRetI{

  public boolean worked = false;

  public static SSCollUserEntriesAddRet get(boolean worked, SSServOpE op){
    return new SSCollUserEntriesAddRet(worked, op);
  }
  
  private SSCollUserEntriesAddRet(boolean worked, SSServOpE op) {

    super(op);
    
    this.worked = worked;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld  = new HashMap<>();
    
    ld.put(SSVarU.worked, SSVarU.xsd + SSStrU.valueBoolean);
    
    return ld;
  }
  
  public boolean isWorked() {
    return worked;
  }
}