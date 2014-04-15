 /**
  * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircle;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.kc.tugraz.ss.service.coll.impl.fct.ue.SSCollUEFct;

public class SSCollEntryDeleteFct{
  
  private final SSDBSQLI     dbSQL;
  private final SSCollSQLFct sqlFct;
  
  public SSCollEntryDeleteFct(final SSDBSQLI dbSQL) throws Exception{
    
    this.dbSQL  = dbSQL;
    this.sqlFct = new SSCollSQLFct(dbSQL);
  }
  
  public Boolean removeColl(final SSCollUserEntryDeletePar par) throws Exception{
    
    switch(SSServCaller.entityMostOpenCircleTypeGet(par.collEntry)){
      
      case priv:{
        
        dbSQL.startTrans(par.shouldCommit);
        
        //TODO dtheiler: remove priv (sub) coll(s) from circle(s)/coll table if not linked anymore to a user in coll clean up timer task thread
        
        sqlFct.removeUserPrivateCollAndUnlinkSubColls(par.user, par.collEntry);
        
        dbSQL.commit(par.shouldCommit);
        
        SSCollUEFct.collUserDeleteColl(par);
        
        break;
      }
      
      default:{
        
        //TODO dtheiler: remove shared/pub (sub) coll(s) from circle(s)/coll table if not linked anymore to a user in coll clean up timer task thread
          
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.unlinkUserCollAndSubColls(par.user, par.coll, par.collEntry);
        
        dbSQL.commit(par.shouldCommit);
        
        SSCollUEFct.collUserUnSubscribeColl(par);
      }
    }
    
    return true;
  }

  public void removeCollEntry(final SSCollUserEntryDeletePar par) throws Exception{
    
    dbSQL.startTrans(par.shouldCommit);
    
    for(SSCircle userEntityCircle : SSServCaller.entityUserEntityCirclesGet(par.user, par.coll)){
      
      switch(userEntityCircle.circleType){
        case priv:
        case pub: break;
        default:{
          SSServCaller.entityUserEntitiesFromCircleRemove(
            par.user,
            userEntityCircle.circleUri,
            par.collEntry,
            false);
        }
      }
    }
    
    sqlFct.removeEntryFromColl(par.coll, par.collEntry);
    
    dbSQL.commit(par.shouldCommit);
  }
}
