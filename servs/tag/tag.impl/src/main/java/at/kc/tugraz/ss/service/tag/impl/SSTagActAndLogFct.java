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
package at.kc.tugraz.ss.service.tag.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.serv.SSUri;
import java.util.List;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSTagActAndLogFct {
  
  private final SSActivityServerI activityServ;
  private final SSEvalServerI     evalServ;
  
  public SSTagActAndLogFct(
    final SSActivityServerI activityServ,
    final SSEvalServerI     evalServ){
    
    this.activityServ = activityServ;
    this.evalServ     = evalServ;
  }
  
  public void tagsAdd(
    final SSUri            user,
    final List<SSUri>      entities,
    final List<SSUri>      tagURIs,
    final List<SSTagLabel> labels,
    final Boolean          shouldCommit) throws Exception{
    
    if(entities.isEmpty()){
      return;
    }
    
    try{
      
      for(SSUri entity : entities){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            user,
            SSActivityE.tagEntity,
            entity,
            null,
            tagURIs,
            null,
            null,
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    if(labels.isEmpty()){
      return;
    }
    
    try{
      
      for(SSUri entity : entities){
        
        for(SSTagLabel label : labels){
          
          evalServ.evalLog(
            new SSEvalLogPar(
              user,
              SSToolContextE.sss,
              SSEvalLogE.tagAdd,
              entity,  //entity
              SSStrU.toStr(label), //content,
              null, //entities
              null, //users
              shouldCommit));
        }
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
  
  public void tagsRemove(
    final SSUri            user,
    final SSUri            entity,
    final SSTagLabel       label,
    final Boolean          shouldCommit) throws Exception{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.removeTags,
          entity,
          null,
          null,
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      evalServ.evalLog(
        new SSEvalLogPar(
          user,
          SSToolContextE.sss,
          SSEvalLogE.tagsRemove,
          entity,  //entity
          SSStrU.toStr(label), //content,
          null, //entities
          null, //users
          shouldCommit));
      
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