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
package at.kc.tugraz.ss.serv.datatypes.location.serv;

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.serv.SSDBGraph;
import at.kc.tugraz.ss.serv.db.serv.SSDBSQL;
import at.kc.tugraz.ss.serv.datatypes.location.impl.SSLocationImpl;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;

public class SSLocationServ extends SSServA{
  
  public static final SSServA inst = new SSLocationServ();
  
  private SSLocationServ(){}
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSLocationImpl(servConf, (SSDBGraphI) SSDBGraph.inst.serv(), (SSDBSQLI) SSDBSQL.inst.serv());
  }

  @Override
  protected void initServSpecificStuff() throws Exception{
    regServForManagingEntities(SSEntityEnum.location);
  }
}