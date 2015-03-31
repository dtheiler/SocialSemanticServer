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
package at.kc.tugraz.ss.service.disc.impl.fct.activity;

import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;

import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserEntryAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserEntryAddRet;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServErrReg;

public class SSDiscActivityFct{
  
  public static void discEntryAdd(
    final SSDiscUserEntryAddPar par,
    final SSDiscUserEntryAddRet ret) throws Exception{
    
    try{
      
      if(par.addNewDisc){
        
        SSServCaller.activityAdd(
          par.user,
          SSActivityE.discussEntity,
          par.entity,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(ret.disc),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          true);
        
        if(par.entry != null){
          
          SSServCaller.activityAdd(
            par.user,
            SSActivityE.addDiscEntry,
            par.entity,
            SSUri.asListWithoutNullAndEmpty(),
            SSUri.asListWithoutNullAndEmpty(ret.entry),
            SSTextComment.asListWithoutNullAndEmpty(par.entry),
            null,
            true);
        }
      }else{
        
        SSServCaller.activityAdd(
          par.user,
          SSActivityE.addDiscEntry,
          ret.disc,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(ret.entry),
          SSTextComment.asListWithoutNullAndEmpty(par.entry),
          null,
          true);
      }
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}