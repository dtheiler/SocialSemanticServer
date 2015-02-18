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
package at.kc.tugraz.ss.service.disc.impl.fct.op;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserEntryAddPar;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;

public class SSDiscUserEntryAddFct{
  
  public static void addDisc(
    final SSDiscSQLFct  sqlFct,
    final SSUri         discUri,
    final SSUri         userUri, 
    final SSUri         targetUri,
    final SSEntityE     discType, 
    final SSLabel       discLabel,
    final SSTextComment description) throws Exception{
    
    try{
      
      final SSUri         tmpTargetUri;
      
      if(targetUri == null){
        tmpTargetUri = discUri;
      }else{
        tmpTargetUri = targetUri;
      }
      
      SSServCaller.entityEntityToPrivCircleAdd(
        userUri,
        discUri,
        discType,
        discLabel,
        description,
        null,
        false);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        userUri,
        tmpTargetUri,
        SSEntityE.entity,
        null,
        description,
        null,
        false);
      
      sqlFct.createDisc(
        userUri, 
        discUri, 
        tmpTargetUri);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static SSUri addDiscEntry(
    final SSDiscSQLFct  sqlFct, 
    final SSUri         userUri,
    final SSUri         discUri, 
    final SSTextComment content) throws Exception{
    
    try{
      final SSUri     discEntryUri  = SSServCaller.vocURICreate();
      final SSEntityE discType      = SSServCaller.entityGet    (discUri).type;
      SSEntityE       discEntryType = null;
      
      switch(discType){
        case disc: discEntryType = SSEntityE.discEntry;   break;
        case qa:   discEntryType = SSEntityE.qaEntry;     break;
        case chat: discEntryType = SSEntityE.chatEntry;   break;
        default: throw new Exception("disc type not valid");
      }
      
      SSServCaller.entityEntityToPrivCircleAdd(
        userUri,
        discEntryUri,
        discEntryType,
        SSLabel.get(discEntryUri),
        null,
        null,
        false);
            
      for(SSEntityCircle entityUserCircle : SSServCaller.circlesGet(userUri, null, discUri, true, false, false)){
        
        SSServCaller.circleEntitiesAdd(
          userUri,
          entityUserCircle.id, 
          SSUri.asListWithoutNullAndEmpty(discEntryUri),
          false, 
          false, 
          false);
      }
      
      sqlFct.addDiscEntry(
        discEntryUri, 
        discUri, 
        content);
      
      return discEntryUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void checkWhetherUserCanAddDisc(
    final SSDiscUserEntryAddPar par) throws Exception{
    
    try{
      
      if(SSObjU.isNull(par.label, par.type)){
        throw new Exception("label, disc type null");
      }
      
      switch(par.type){
        case disc:
        case qa:
        case chat: break;
        default: throw new Exception("disc type not valid");
      }
      
      if(!SSObjU.isNull(par.entity)){
        SSServCallerU.canUserReadEntity(par.user, par.entity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void checkWhetherUserCanAddDiscEntry(
    final SSDiscUserEntryAddPar par) throws Exception{
    
    try{
     
      if(SSObjU.isNull(par.entry)){
        throw new Exception("content missing");
      }
      
      SSServCallerU.canUserEditEntity(par.user, par.disc);
       
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
