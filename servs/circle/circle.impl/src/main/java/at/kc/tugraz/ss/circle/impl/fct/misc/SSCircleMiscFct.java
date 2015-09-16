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
package at.kc.tugraz.ss.circle.impl.fct.misc;

import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.impl.SSCircleImpl;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.circle.impl.fct.sql.SSCircleSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSCircleRightE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSEntityCopiedI;
import at.tugraz.sss.serv.SSEntityCopiedPar;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.util.SSServCallerU;

public class SSCircleMiscFct{
  
  private final SSCircleSQLFct sqlFct;
  private final SSCircleImpl   serv;
  
  public SSCircleMiscFct(
    final SSCircleImpl   serv,
    final SSCircleSQLFct sqlFct){
    
    this.serv   = serv;
    this.sqlFct = sqlFct;
  }
  
  public static List<SSCircleRightE> getCircleRights(
    final SSCircleE circleType) throws Exception{
    
    try{
      
      final List<SSCircleRightE> circleRights = new ArrayList<>();
      
      if(SSObjU.isNull(circleType)){
        throw new Exception("pars null");
      }
      
      switch(circleType){
        case priv: circleRights.add(SSCircleRightE.all);  break;
        case pub:  circleRights.add(SSCircleRightE.read); break;
        default:   circleRights.add(SSCircleRightE.read); circleRights.add(SSCircleRightE.edit);
      }
      
      return circleRights;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void checkWhetherCircleIsGroupCircle(
    final SSCircleE circleType) throws Exception{
    
    try{
      
      if(!SSCircleE.isGroupCircle(circleType)){
        throw new Exception("circle is no group circle");
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean doesUserHaveRightInAnyCircleOfEntity(
    final SSUri          user,
    final SSUri          entity,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      final List<SSCircleE> circleTypes = sqlFct.getCircleTypesCommonForUserAndEntity(user, entity);
      
      for(SSCircleE circleType : circleTypes){
        
        if(doesCircleOfTypeHaveRight(circleType, accessRight)){
          return true;
        }
      }
      
      return false;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private Boolean doesCircleOfTypeHaveRight(
    final SSCircleE      circleType,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      switch(circleType){
        case priv: return true;
        case pub:{
          
          if(SSCircleRightE.equals(accessRight, SSCircleRightE.read)){
            return true;
          }
          
          break;
        }
        
        default:{
          
          if(
            SSCircleRightE.equals(accessRight, SSCircleRightE.read) ||
            SSCircleRightE.equals(accessRight, SSCircleRightE.edit)){
            return true;
          }
          
          break;
        }
      }
      
      return false;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean isUserInCircle(
    final SSUri          user,
    final SSUri          circle) throws Exception{
    
    try{
      final List<SSEntity> usersForCircle = sqlFct.getUsersForCircle(circle);
      
      return SSStrU.contains(usersForCircle, user);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean hasCircleOfTypeRight(
    final SSUri          circle,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      return doesCircleOfTypeHaveRight(sqlFct.getTypeForCircle(circle), accessRight);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean canUserForEntityType(
    final SSUri          user,
    final SSEntity       entity,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      switch(entity.type){
        case entity: return true; //TODO dtheiler: break down general entity types so that checks on e.g. videos will be present
        case circle: {
          
          if(
            isUserInCircle       (user,      entity.id) &&
            hasCircleOfTypeRight (entity.id, accessRight)){
            return true;
          }
          
          return false;
        }
        
        default:{
          return doesUserHaveRightInAnyCircleOfEntity(user, entity.id, accessRight);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void addCircle(
    final SSUri          circleUri,
    final SSCircleE      circleType,
    final Boolean        isSystemCircle,
    final SSUri          userToAdd) throws Exception{
    
    try{
      sqlFct.addCircle(
        circleUri,
        circleType,
        isSystemCircle);
      
      sqlFct.addUserToCircleIfNotExists(
        circleUri,
        userToAdd);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void copyCircleToNewCircle(
    final SSEntityCopyPar par,
    final SSEntityCircle  circle) throws Exception{
    
    try{
      
      SSLabel              label;
      SSUri                copyCircleURI;
      SSEntityCircle       newCircle;
      String               labelStr;
      
      for(SSUri forUser : par.forUsers){
        
        if(par.label != null){
          label = par.label;
        }else{
          label = circle.label;
        }
        
        if(par.appendUserNameToLabel){
          
          labelStr =
            SSStrU.toStr(label) +
            SSStrU.underline    +
            SSStrU.toStr(
              ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
                new SSEntityGetPar(
                  par.user, //user
                  forUser, //entity
                  par.withUserRestriction,  //withUserRestriction
                  null)).label); //descPar
          
          label = SSLabel.get(labelStr);
        }
        
        copyCircleURI =
          serv.circleCreate(
            new SSCircleCreatePar(
              forUser,
              circle.circleType,
              label,
              circle.description,
              circle.isSystemCircle,
              false, //withUserRestriction
              false)); //shouldCommit
        
        if(par.includeEntities){
          
          serv.circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              forUser,
              copyCircleURI,
              SSUri.getDistinctNotNullFromEntities(circle.entities),
              false, //withUserRestriction
              false)); //shouldCommit
          
          SSServCallerU.handleEntityCopied(
            par.user, 
            forUser, 
            circle, 
            circle.entities, 
            copyCircleURI, 
            par.withUserRestriction);
        }
        
        if(par.includeUsers){
          
          serv.circleUsersAdd(
            new SSCircleUsersAddPar(
              forUser,
              copyCircleURI,
              SSUri.getDistinctNotNullFromEntities(circle.users),
              false, //withUserRestriction,
              false)); //shouldCommit
          
          SSServCallerU.handleEntityCopied(
            par.user, 
            forUser, 
            circle, 
            circle.users, 
            copyCircleURI, 
            par.withUserRestriction);
        }
        
        if(par.includeOriginUser){
          
          serv.circleUsersAdd(
            new SSCircleUsersAddPar(
              forUser,
              copyCircleURI,
              SSUri.asListWithoutNullAndEmpty(par.user),
              false, //withUserRestriction,
              false)); //shouldCommit
          
          final List<SSEntity> originUsers = new ArrayList<>();
          
          originUsers.add(
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                par.user, //user
                par.user, //entity
                par.withUserRestriction,  //withUserRestriction
                null))); //descPar
          
          SSServCallerU.handleEntityCopied(
            par.user, 
            forUser, 
            circle, 
            originUsers, 
            copyCircleURI, 
            par.withUserRestriction);
        }
        
        newCircle =
          serv.circleGet(
            new SSCircleGetPar(
              forUser,
              copyCircleURI,
              null, //entityTypesToIncludeOnly
              false,  //setTags
              null, //tagSpace
              par.withUserRestriction,
              false)); //invokeEntityHandlers
        
        SSServCallerU.handleCircleEntitiesAdded(
          par.user,
          newCircle,
          newCircle.entities,
          par.withUserRestriction);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void copyCircleToExistingCircle(
    final SSEntityCopyPar           par,
    final SSEntity                  circle) throws Exception{
    
    try{
      
      SSEntityCopiedPar    entityCopiedPar;
      SSEntityCircle       targetCircle =
        serv.circleGet(
          new SSCircleGetPar(
            par.user,
            par.targetEntity,
            null, //entityTypesToIncludeOnly
            false,  //setTags
            null, //tagSpace
            par.withUserRestriction,
            false)); //invokeEntityHandlers
      
      if(par.includeEntities){
        
        serv.circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            par.user,
            targetCircle.id,
            SSUri.getDistinctNotNullFromEntities(circle.entities),
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit
        
        entityCopiedPar =
          new SSEntityCopiedPar(
            par.user,
            null, //forUser
            circle, //entity
            circle.entities, //entities
            targetCircle.id, //targetEntity
            par.withUserRestriction);
        
        entityCopiedPar.includeMetadataSpecificToEntityAndItsEntities = true;
        
        for(SSServContainerI entityHandler : SSServReg.inst.getServsHandlingEntityCopied()){
          ((SSEntityCopiedI) entityHandler.serv()).entityCopied(entityCopiedPar);
        }
      }
      
      if(par.includeUsers){
        
        serv.circleUsersAdd(
          new SSCircleUsersAddPar(
            par.user,
            targetCircle.id,
            SSUri.getDistinctNotNullFromEntities(circle.users),
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
        
        entityCopiedPar =
          new SSEntityCopiedPar(
            par.user,
            null, //forUser,
            circle, //entity
            circle.users, //entities
            targetCircle.id,  //targetEntity
            par.withUserRestriction);
        
        for(SSServContainerI entityHandler : SSServReg.inst.getServsHandlingEntityCopied()){
          ((SSEntityCopiedI) entityHandler.serv()).entityCopied(entityCopiedPar);
        }
      }
      
      if(par.includeOriginUser){
        
        serv.circleUsersAdd(
          new SSCircleUsersAddPar(
            par.user,
            par.targetEntity, //circle
            SSUri.asListWithoutNullAndEmpty(par.user), //users
            false, //withUserRestriction,
            false)); //shouldCommit
        
        final List<SSEntity> originUsers = new ArrayList<>();
        
        originUsers.add(
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              par.user, //user
              par.user, //entity
              par.withUserRestriction,  //withUserRestriction
              null))); //descPar
        
        entityCopiedPar =
          new SSEntityCopiedPar(
            par.user, //user
            null, //targetUser
            circle, //entity
            originUsers, //entities
            par.targetEntity, //targetEntity
            par.withUserRestriction);
        
        for(SSServContainerI entityHandler : SSServReg.inst.getServsHandlingEntityCopied()){
          ((SSEntityCopiedI) entityHandler.serv()).entityCopied(entityCopiedPar);
        }
      }
      
      targetCircle =
        serv.circleGet(
          new SSCircleGetPar(
            par.user,
            par.targetEntity,
            null, //entityTypesToIncludeOnly
            false,  //setTags
            null, //tagSpace
            par.withUserRestriction,
            false)); //invokeEntityHandlers
      
      SSServCallerU.handleCircleEntitiesAdded(
        par.user,
        targetCircle,
        targetCircle.entities,
        par.withUserRestriction);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
