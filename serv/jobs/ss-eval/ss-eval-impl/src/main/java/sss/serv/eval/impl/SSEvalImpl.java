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
package sss.serv.eval.impl;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSToolContextE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSToolE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import sss.serv.eval.api.SSEvalClientI;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.conf.SSEvalConf;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;
import sss.serv.eval.impl.fct.sql.SSEvalSQLFct;

public class SSEvalImpl extends SSServImplWithDBA implements SSEvalClientI, SSEvalServerI{

//  private final SSLearnEpGraphFct       graphFct;
  private final SSEvalSQLFct sqlFct;
  private final SSEvalConf   evalConf;

  public SSEvalImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    sqlFct     = new SSEvalSQLFct(this);
    evalConf   = (SSEvalConf) conf;
  }

//  @Override
//  public void learnEpsGet(SSSocketCon sSCon, SSServPar par) throws Exception{
//
//    SSServCaller.checkKey(par);
//
//    sSCon.writeRetFullToClient(SSLearnEpsGetRet.get(learnEpsGet(par), par.op));
//  }

  @Override
  public void evalLog(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEvalLogPar par            = new SSEvalLogPar(parA);
      String             logText        = new String();
      
      if(
        evalConf.tools.isEmpty()              ||
        !evalConf.tools.contains(SSToolE.bnp) ||
        par.type == null){
        return;
      }
      
      //adjusting log type
      switch(par.type){

        case read:{
          
          if(
            par.entity != null &&
            SSStrU.equals(par.entity.type, SSEntityE.message)){
            
            par.type = SSEvalLogE.readMessage;
          }
          break;
        }
      }
        
      //time stamp
      logText += SSDateU.dateAsLong();
      logText += SSStrU.semiColon;
      
      //tool context
      if(par.toolContext != null){
        logText += par.toolContext;
      }else{
        logText += getToolContext(par);
      }
      
      logText += SSStrU.semiColon;
      
      //user
      if(par.forUser != null){
        logText += par.forUser;
      }
      
      logText += SSStrU.semiColon;
      
      // log type
      logText += par.type;
      logText += SSStrU.semiColon;
      
      // entity 
      if(par.entity != null){
        logText += par.entity;
      }
      
      logText += SSStrU.semiColon;
      
      // entity type
      if(par.entity != null){
        logText += par.entity.type;
      }
      
      logText += SSStrU.semiColon;
      
      // entity label
      if(par.entity != null){
        logText += par.entity.label;
      }
      
      logText += SSStrU.semiColon;
      
      // content
      if(par.content != null){
        logText += par.content;
      }
      
      logText += SSStrU.semiColon;
      
      // tag type
      if(
        SSStrU.equals(par.type, SSEvalLogE.addTag) &&
        par.content != null){
          
        if(par.content.startsWith("#")){
          logText += 2;
        }else{
          logText += 1;
        }
      }
      
      logText += SSStrU.semiColon;
      
      // entities' ids
      for(SSEntity entity : par.entities){
        logText += entity.id;
        logText += SSStrU.semiColon;
      }
      
      logText += SSStrU.semiColon;
      
      // entities' labels
      for(SSEntity entity : par.entities){
        logText += entity.label;
        logText += SSStrU.semiColon;
      }
      
      logText += SSStrU.semiColon;
      
      // users' ids
      for(SSEntity entity : par.users){
        logText += entity.id;
        logText += SSStrU.semiColon;
      }
      
      logText += SSStrU.semiColon;
      
      // users' labels
      for(SSEntity entity : par.users){
        logText += entity.label;
        logText += SSStrU.semiColon;
      }
      
      logText += SSStrU.semiColon;
      
      logText = SSStrU.replaceAllLineFeedsWithTextualRepr(logText);
      
      SSLogU.trace(logText, false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
    
  private String getToolContext(final SSEvalLogPar par){
    
    if(par.entity == null){
      return SSStrU.empty;
    }
    
    switch(par.type){
      
      case sendMessage:       return SSToolContextE.notificationTab.toString();
      case readMessage:       return SSToolContextE.notificationTab.toString();
      case changeLabel:       return getToolContextFromChangeLabel(par);
      case changeDescription: return getToolContextFromChangeLabel(par);
      case setImportance:     return SSToolContextE.bitTab.toString();
      case addTag:            return SSToolContextE.bitTab.toString();
      default:                return SSStrU.empty;
    }
  }

  private String getToolContextFromChangeLabel(final SSEvalLogPar par){

    switch(par.entity.type){
      
      case learnEp:
      case learnEpVersion:{
        return SSToolContextE.episodeTab.toString();
      }
      
      case learnEpCircle:{
        return SSToolContextE.organizeArea.toString();
      }
      
      default:{
        return SSToolContextE.bitTab.toString();
      }
    }
  }
}