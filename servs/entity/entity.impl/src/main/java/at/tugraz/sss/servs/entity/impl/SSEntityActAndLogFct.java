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
package at.tugraz.sss.servs.entity.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.datatype.par.*;
import java.util.List;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSEntityActAndLogFct {
  
  private final SSActivityServerI activityServ;
  private final SSEvalServerI     evalServ;
  
  public SSEntityActAndLogFct(
    final SSActivityServerI activityServ,
    final SSEvalServerI     evalServ){
    
    this.activityServ = activityServ;
    this.evalServ     = evalServ;
  }
  
//  public void entityUpdate(
//    final SSServPar     servPar,
//    final SSUri         user,
//    final boolean       storeLogs,
//    final SSEntity      entity,
//    final SSLabel       label,
//    final SSTextComment description,
//    final boolean       shouldCommit) throws SSErr{
//    
//    if(
//      !storeLogs ||
//      entity == null){
//      return;
//    }
//    
//    try{
//      
//      if(!SSStrU.equals(entity.label, label)){
//        
//        evalServ.evalLog(
//          new SSEvalLogPar(
//            servPar,
//            user,
//            SSToolContextE.sss,
//            SSEvalLogE.changeLabel,
//            entity.id,
//            SSStrU.toStr(entity.label), //content
//            null, //entities
//            null, //users
//            null, //creationTime
//            shouldCommit));
//      }
//      
//      if(!SSStrU.equals(entity.description, description)){
//        
//        evalServ.evalLog(
//          new SSEvalLogPar(
//            servPar,
//            user,
//            SSToolContextE.sss,
//            SSEvalLogE.changeDescription,
//            entity.id,
//            SSStrU.toStr(entity.description), //content
//            null, //entities
//            null, //users
//            null, //creationTime
//            shouldCommit));
//      }
//      
//    }catch(SSErr error){
//      
//      switch(error.code){
//        case servInvalid: SSLogU.warn(error); break;
//        default:{
//          SSServErrReg.regErrThrow(error);
//          break;
//        }
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
  
  public void entityCopy(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         entity,
    final SSUri         targetEntity,
    final List<SSUri>   forUsers,
    final SSTextComment comment,
    final boolean       shouldCommit) throws SSErr{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.copyEntityForUsers,
          entity,
          forUsers,
          null,
          SSTextComment.asListWithoutNullAndEmpty(comment),
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{
          SSServErrReg.regErrThrow(error);
          break;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.sss,
          SSEvalLogE.entityCopy,
          entity,  //entity
          null, //content,
          SSUri.asListNotNull(targetEntity), //entities
          forUsers, //users
          null, //creationTime
          shouldCommit));
      
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
  
  public void attachEntities(
    final SSServPar servPar,
    final SSUri       user,
    final SSUri       entity,
    final List<SSUri> entities,
    final boolean     shouldCommit) throws SSErr{
    
    if(
      entity == null ||
      entities.isEmpty()){
      return;
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.sss,
          SSEvalLogE.attachEntities,
          entity, //entity
          null, //content
          entities, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
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
  
  public void removeEntities(
    final SSServPar servPar,
    final SSUri       user,
    final SSUri       entity,
    final List<SSUri> entities,
    final boolean     shouldCommit) throws SSErr{
    
    if(
      entity == null ||
      entities.isEmpty()){
      return;
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.sss,
          SSEvalLogE.removeEntities,
          entity, //entity
          null, //content
          entities, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{
          SSServErrReg.regErrThrow(error);
          break;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void createPlaceHolder(
    final SSServPar par, 
    final SSUri     user, 
    final SSUri     placeholder, 
    final boolean   shouldCommit) throws SSErr{
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.createPlaceholder,
          placeholder, //entity
          null, //content
          null, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{
          SSServErrReg.regErrThrow(error);
          break;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}