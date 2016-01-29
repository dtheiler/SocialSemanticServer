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
package at.kc.tugraz.ss.service.coll.impl.fct.op;

import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntityPrivatePar;
import at.tugraz.sss.serv.reg.SSServErrReg;

public class SSCollEntryDeleteFct{
  
  public static boolean removeColl(
    final SSCollSQLFct             sqlFct,
    final SSEntityServerI          circleServ,
    final SSCollUserEntryDeletePar par) throws SSErr{
    
    try{
      
      if(sqlFct.isCollSpecial(par, par.entry)){
        throw new Exception("cant delete special collection");
      }
      
      if(circleServ.circleIsEntityPrivate(new SSCircleIsEntityPrivatePar(par, par.user, par.entry))){
        //TODO dtheiler: remove priv (sub) coll(s) from circle(s)/coll table if not linked anymore to a user in coll clean up timer task thread
          sqlFct.removeCollAndUnlinkSubColls(par, par.user, par.entry);
      }else{
        //TODO dtheiler: remove shared/pub (sub) coll(s) from circle(s)/coll table if not linked anymore to a user in coll clean up timer task thread
        sqlFct.unlinkCollAndSubColls(par, par.user, par.coll, par.entry);
      }

      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }

  public static void removeCollEntry(
    final SSCollSQLFct             sqlFct, 
    final SSCollUserEntryDeletePar par) throws SSErr{
    
    sqlFct.removeCollEntry(par, par.coll, par.entry);
  }
}
