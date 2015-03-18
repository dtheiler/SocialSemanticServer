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
package at.kc.tugraz.ss.test.serv.coll;

import at.kc.tugraz.ss.serv.coll.conf.SSCollConf;
import at.kc.tugraz.ss.service.coll.service.SSCollServ;

public class SSCollTester {

  public static void run() throws Exception{
    
    SSCollConf collConf = (SSCollConf) SSCollServ.inst.servConf;
    
    if(!collConf.executeOpAtStartUp){
      return;
    }      
    
    switch(collConf.op){
      case collUserHierarchyGet:     new Thread(new SSCollUserHierarchyGetTest(collConf)).start();      break;
      case collUserEntriesAdd:       new Thread(new SSCollUserEntriesAddTest(collConf)).start();        break;
      case collUserCumulatedTagsGet: new Thread(new SSCollUserCummulatedTagsGetTest(collConf)).start(); break;
      case collUserEntriesDelete:    new Thread(new SSCollUserEntriesDeleteTest(collConf)).start();     break;
    }
  }  
}
