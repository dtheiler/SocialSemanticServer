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

import at.tugraz.sss.serv.datatype.par.SSCircleAddEntitiesToCircleOfEntityPar;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.kc.tugraz.ss.service.disc.impl.SSDiscSQLFct;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.reg.SSServErrReg;

public class SSDiscUserEntryAddFct{
  
  private final SSEntityServerI circleServ;
  private final SSEntityServerI entityServ;
  
  public SSDiscUserEntryAddFct(
    final SSEntityServerI circleServ,
    final SSEntityServerI entityServ){
    
    this.circleServ = circleServ;
    this.entityServ = entityServ;
  }
  
  public SSUri addDisc(
    final SSServPar servPar,
    final SSDiscSQLFct  sqlFct,
    final SSUri         userUri, 
    final SSEntityE     discType, 
    final SSLabel       discLabel,
    final SSTextComment description,
    final boolean       withUserRestriction) throws Exception{
    
    try{
      
      final SSUri disc =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            servPar,
            userUri,
            SSConf.vocURICreate(),
            discType, //type,
            discLabel, //label
            description, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(disc == null){
        return null;
      }
      
      sqlFct.createDisc(
        servPar,
        userUri, 
        disc);
      
      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSUri addDiscEntry(
    final SSServPar servPar,
    final SSDiscSQLFct  sqlFct, 
    final SSUri         userUri,
    final SSUri         discUri, 
    final SSTextComment content,
    final boolean       withUserRestriction) throws Exception{
    
    try{
      final SSEntityE discType =
        entityServ.entityGet(
          new SSEntityGetPar(
            servPar,
            null,
            discUri,  //entity
            false, //withUserRestriction
            null)).type; //descPar
      
      SSEntityE discEntryType = null;
      
      switch(discType){
        case disc: discEntryType = SSEntityE.discEntry;   break;
        case qa:   discEntryType = SSEntityE.qaEntry;     break;
        case chat: discEntryType = SSEntityE.chatEntry;   break;
        default: throw new Exception("disc type not valid");
      }
      
      final SSUri discEntry =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            servPar,
            userUri,
            SSConf.vocURICreate(),
            discEntryType, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(discEntry == null){
        return null;
      }
      
      sqlFct.addDiscEntry(
        servPar,
        discEntry,
        discUri,
        content);
      
      circleServ.circleAddEntitiesToCirclesOfEntity(
        new SSCircleAddEntitiesToCircleOfEntityPar(
          servPar,
          userUri,
          discUri,
          SSUri.asListNotNull(discEntry), //entities
          withUserRestriction,
          false, //invokeEntityHandlers,
          false)); //shouldCommit
      
      return discEntry;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
