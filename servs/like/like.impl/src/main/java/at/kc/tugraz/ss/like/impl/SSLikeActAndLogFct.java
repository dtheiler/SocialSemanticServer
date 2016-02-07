/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.like.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.util.SSLogU;
import at.kc.tugraz.ss.like.datatypes.par.*;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSLikeActAndLogFct{
  
  private final SSActivityServerI activityServ;
  private final SSEvalServerI     evalServ;
  
  public SSLikeActAndLogFct(
    final SSActivityServerI activityServ,
    final SSEvalServerI     evalServ){
    
    this.activityServ = activityServ;
    this.evalServ     = evalServ;
  }
  
  public void setLike(
    final SSLikeUserSetPar par,
    final boolean          shouldCommit) throws SSErr{
    
    try{
      
      if(!par.storeLogs){
        return;
      }
      
      if(par.value == 1){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.likeEntity,
            par.entity, //entity
            null, //content
            null, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
      if(par.value == -1){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.dislikeEntity,
            par.entity, //entity
            null, //content
            null, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default: {
          SSServErrReg.regErrThrow(error);
          break;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

//try{
//
//      if(
//        label != null &&
//        !SSStrU.equals(circleEntity.label, label)){
//
//        evalServ.evalLog(
//          new SSEvalLogPar(
//            user,
//            SSToolContextE.organizeArea,
//            SSEvalLogE.changeLabel,
//            learnEpCircle,
//            SSStrU.toStr(label), //content
//            SSUri.asListWithoutNullAndEmpty(learnEp), //entities
//            null, //users
//            shouldCommit));
//      }
//
//    }catch(SSErr error){
//
//      switch(error.code){
//        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
//        default: SSServErrReg.regErrThrow(error);
//      }
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//
//    try{
//
//      if(
//        description != null &&
//        !SSStrU.equals(circleEntity.description, description)){
//
//        evalServ.evalLog(
//          new SSEvalLogPar(
//            user,
//            SSToolContextE.organizeArea,
//            SSEvalLogE.changeDescription,
//            learnEpCircle,
//            SSStrU.toStr(description), //content
//            SSUri.asListWithoutNullAndEmpty(learnEp), //entities
//            null, //users
//            shouldCommit));
//      }
//
//    }catch(SSErr error){
//
//      switch(error.code){
//        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
//        default: SSServErrReg.regErrThrow(error);
//      }
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }