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
package at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.activity;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.SSUri;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;

public class SSLearnEpActivityFct{
  
  public static void addCircleToLearnEpVersion(
    final SSUri                        user,
    final SSUri                        learnEpVersion,
    final SSUri                        circle,
    final SSUri                        learnEp,
    final Boolean                      shouldCommit) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.addCircleToLearnEpVersion,
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(circle, learnEp),
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
  }
  
  public static void addEntityToLearnEpVersion(
    final SSUri                        user,
    final SSUri                        learnEpVersion,
    final SSUri                        entity,
    final SSUri                        learnEpEntity,
    final SSUri                        learnEp,
    final Boolean                      shouldCommit) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.addEntityToLearnEpVersion,
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEpEntity, entity, learnEp),
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
  }
  
  public static void removeLearnEpVersionCircle(
    final SSUri                           user,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp, 
    final SSUri                           learnEpCircle,
    final Boolean                         shouldCommit) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.removeLearnEpVersionCircle,
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEpCircle, learnEp),
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
  }
  
  public static void removeLearnEpVersionEntity(
    final SSUri                           user,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEpEntity,
    final SSUri                           entity,
    final SSUri                           learnEp,
    final Boolean                         shouldCommit) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.removeLearnEpVersionEntity,
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEpEntity, entity, learnEp),
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
  }
  
  public static void shareLearnEp(
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToShareWith,
    final Boolean     shouldCommit) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.shareLearnEpWithUser,
          learnEp,
          SSUri.asListWithoutNullAndEmpty(usersToShareWith),
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
  }
  
  public static void copyLearnEp(
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToCopyFor,
    final Boolean     shouldCommit) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.copyLearnEpForUsers,
          learnEp,
          SSUri.asListWithoutNullAndEmpty(usersToCopyFor),
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
  }
  
  public static void changeLearnEpVersionCircleLabel(
    final SSUri                           user,
    final SSLabel                         label,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEpCircle,
    final SSUri                           learnEp,
    final Boolean                         shouldCommit) throws Exception{
    
    try{
      
      if(label == null){
        return;
      }
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.changeLearnEpVersionCircleLabel,
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEpCircle, learnEp),
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
  }

  public static void removeEntityFromLearnEpCircle(
    final SSUri   user, 
    final SSUri   learnEpEntity,
    final SSUri   circle,
    final Boolean shouldCommit) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.removeEntityFromLearnEpCircle, //type
          learnEpEntity, //entity
          null, //users
          SSUri.asListWithoutNullAndEmpty(circle), //entities
          null, //comments
          null, //creationTime
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

  public static void addEntityToLearnEpCircle(
    final SSUri   user, 
    final SSUri   learnEpEntity,
    final SSUri   circle,
    final Boolean shouldCommit) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.addEntityToLearnEpCircle, //type
          learnEpEntity, //entity
          null, //users
          SSUri.asListWithoutNullAndEmpty(circle), //entities
          null, //comments
          null, //creationTime
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
