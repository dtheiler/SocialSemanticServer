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
package at.kc.tugraz.ss.circle.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.circle.api.SSCircleClientI;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.impl.fct.serv.SSCircleServFct;
import at.kc.tugraz.ss.circle.impl.fct.misc.SSCircleMiscFct;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleTypesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCirclesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreateFromClientPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemoveFromClientPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleIsEntitySharedPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleIsEntityPrivatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleIsEntityPublicPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleTypeChangePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersInvitePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersRemovePar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleTypeChangeRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersInviteRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.tugraz.sss.serv.SSCircleContentRemovedI;
import at.tugraz.sss.serv.SSCircleContentRemovedPar;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSCopyEntityI;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSToolContextE;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;
import sss.servs.entity.sql.SSEntitySQL;

public class SSCircleImpl
extends SSServImplWithDBA
implements
  SSCircleClientI,
  SSCircleServerI,
  SSDescribeEntityI,
  SSCopyEntityI{
  
  private static SSUri             pubCircleUri = null;
  private final  SSEntitySQL       sql;
  private final  SSCircleMiscFct   miscFct;
  private final  SSActivityServerI activityServ;
  private final  SSEvalServerI     evalServ;
  
  public SSCircleImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sql          = new SSEntitySQL     (dbSQL, SSVocConf.systemUserUri);
    this.miscFct      = new SSCircleMiscFct (this, sql);
    this.activityServ = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
    this.evalServ     = (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setCircles){
        
        switch(entity.type){
          
          case user:{
            
            entity.circles.addAll(
              circlesGet(
                new SSCirclesGetPar(
                  par.user, //user
                  entity.id, //forUser
                  null, //entity
                  null, //entityTypesToIncludeOnly
                  true, //setEntities,
                  true, //setUsers,
                  par.withUserRestriction, //withUserRestriction
                  false,  //withSystemCircles
                  false))); //invokeEntityHandlers
            
            break;
          }
          
          default:{
            
            entity.circles.addAll(
              circlesGet(
                new SSCirclesGetPar(
                  par.user, //user
                  null, //forUser
                  entity.id, //entity
                  null, //entityTypesToIncludeOnly
                  true, //setEntities,
                  true, //setUsers,
                  par.withUserRestriction, //withUserRestriction
                  false,  //withSystemCircles
                  false))); //invokeEntityHandlers
            
            break;
          }
        }
      }
      
      if(par.setCircleTypes){
        
        entity.circleTypes.addAll(
          circleTypesGet(
            new SSCircleTypesGetPar(
              par.user,
              entity.id,
              par.withUserRestriction)));
      }
      
      switch(entity.type){
        
        case circle:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          final SSEntityCircle circle =
            circleGet(
              new SSCircleGetPar(
                par.user,
                entity.id,                    //circle
                par.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
                false,  //setTags
                null, //tagSpace
                true, //setEntities
                true, //setUsers
                par.withUserRestriction,  //withUserRestriction
                false));                   //invokeEntityHandlers
          
          return SSEntityCircle.get(circle, entity);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void copyEntity(
    final SSEntity                  entity,
    final SSEntityCopyPar           par) throws SSErr{
    
    try{
      
      switch(entity.type){
        case circle: break;
        default: return;
      }
      
      if(par.withUserRestriction){
        
        if(!sql.isGroupOrPubCircleCircle(entity.id)){
          return;
        }
        
        if(par.targetEntity != null){
          
          if(!sql.isGroupOrPubCircleCircle(par.targetEntity)){
            return;
          }
        }
      }
      
      final SSEntityCircle circle =
        circleGet(
          new SSCircleGetPar(
            par.user,
            entity.id,
            null, //entityTypesToIncludeOnly
            false,  //setTags
            null, //tagSpace
            true, //setEntities
            true, //setUsers,
            par.withUserRestriction,
            true)); //invokeEntityHandlers
      
      if(par.targetEntity == null){
        miscFct.copyCircleToNewCircle(par, circle);
      }else{
        miscFct.copyCircleToExistingCircle(par, circle);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI circleEntitiesRemove(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCircleEntitiesRemoveFromClientPar par    = (SSCircleEntitiesRemoveFromClientPar) parA.getFromJSON(SSCircleEntitiesRemoveFromClientPar.class);
      final List<SSUri>                         result;
      
      if(!sql.isGroupOrPubCircleCircle(par.circle)){
        result = new ArrayList<>();
      }else{
        result = circleEntitiesRemove(par);
      }
      
      if(!result.isEmpty()){
        
        for(SSServContainerI serv : SSServReg.inst.getServsHandlingCircleContentRemoved()){
          
          ((SSCircleContentRemovedI) serv.serv()).circleContentRemoved(
            new SSCircleContentRemovedPar(
              par.user,
              par.circle, //circle
              result, //entities
              par.removeCircleSpecificMetadata, //removeCircleSpecificMetadata
              par.withUserRestriction, //withUserRestriction
              par.shouldCommit)); //shouldCommit
        }
      }
      
      final SSCircleEntitiesRemoveRet ret = SSCircleEntitiesRemoveRet.get(result);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.circleEntitiesRemove,
          par.circle,  //entity
          null, //content,
          par.entities, //entities
          null, //users
          par.shouldCommit));
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> circleEntitiesRemove(final SSCircleEntitiesRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity circle =
        sql.getEntityTest(
          par.user,
          par.circle,
          par.withUserRestriction);
      
      if(circle == null){
        return new ArrayList<>();
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entity : par.entities){
        sql.removeEntityFromCircle(par.circle, entity);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entities;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleEntitiesRemove(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circleUsersRemove(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSCircleUsersRemovePar par    = (SSCircleUsersRemovePar) parA.getFromJSON(SSCircleUsersRemovePar.class);
      final List<SSUri>            result;
      
      if(!sql.isGroupOrPubCircleCircle(par.circle)){
        result = new ArrayList<>();
      }else{
        result = circleUsersRemove(par);
      }
      
      final SSCircleUsersRemoveRet ret = SSCircleUsersRemoveRet.get(result);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.circleUsersRemove,
          par.circle,  //entity
          null, //content,
          null, //entities
          par.users, //users
          par.shouldCommit));
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> circleUsersRemove(final SSCircleUsersRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity circle =
        sql.getEntityTest(
          par.user,
          par.circle,
          par.withUserRestriction);
      
      if(circle == null){
        return new ArrayList<>();
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri user : par.users){
        sql.removeUser(par.circle, user);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.users;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleUsersRemove(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circleCreate(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSCircleCreateFromClientPar par       = (SSCircleCreateFromClientPar) parA.getFromJSON(SSCircleCreateFromClientPar.class);
      final SSUri                       circleURI = circleCreate(par);
      
      if(!par.entities.isEmpty()){
        circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            par.user,
            circleURI,
            par.entities,
            par.withUserRestriction, //withUserRestriction
            par.shouldCommit)); //shouldCommit
      }
      
      if(!par.users.isEmpty()){
        circleUsersAdd(
          new SSCircleUsersAddPar(
            par.user,
            circleURI,
            par.users,
            par.withUserRestriction, //withUserRestriction,
            par.shouldCommit)); //shouldCommit));
      }
      
      if(!par.invitees.isEmpty()){
        circleUsersInvite(
          new SSCircleUsersInvitePar(
            par.user,
            circleURI,
            par.invitees,
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      final SSEntityCircle circle =
        circleGet(
          new SSCircleGetPar(
            par.user,
            circleURI, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //setEntities,
            true, //setUsers,
            par.withUserRestriction, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      SSServCallerU.handleCircleEntitiesAdded(
        par.user,
        circle,
        circle.entities,
        par.withUserRestriction);
      
      final SSCircleCreateRet ret = SSCircleCreateRet.get(circleURI);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.createCircle,
          circleURI,
          par.users, //users,
          par.entities, //entities,
          null,
          null,
          par.shouldCommit));
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.circleCreate,
          circleURI,  //entity
          null, //content,
          par.entities,
          par.users,
          par.shouldCommit));
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleCreate(final SSCircleCreatePar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(par.isSystemCircle){
          throw SSErr.get(SSErrE.notAllowedToCreateCircle);
        }
        
        switch(par.circleType){
          
          case pubCircle:
          case group:{
            break;
          }
          
          default: throw SSErr.get(SSErrE.notAllowedToCreateCircle);
        }
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSUri           circleUri;
      
      dbSQL.startTrans(par.shouldCommit);
      
      circleUri =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSServCaller.vocURICreate(),
            SSEntityE.circle,
            par.label,
            par.description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
      
      if(circleUri == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      miscFct.addCircle(
        circleUri,
        par.circleType,
        par.isSystemCircle,
        par.user); //par.forUser
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleCreate(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circleRemove(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSCircleRemovePar par       = (SSCircleRemovePar) parA.getFromJSON(SSCircleRemovePar.class);
      final SSUri             circleURI;
      
      if(
        sql.isGroupOrPubCircleCircle(par.circle) &&
        sql.isUserAuthor(par.user, par.circle, par.withUserRestriction)){
        circleURI = circleRemove(par);
      }else{
        circleURI = null;
      }
      
      final SSCircleRemoveRet ret = SSCircleRemoveRet.get(circleURI);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.circleRemove,
          par.circle,  //entity
          null, //content,
          null, //entities
          null, //users
          par.shouldCommit));
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleRemove(final SSCircleRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity circle =
        sql.getEntityTest(
          par.user,
          par.circle,
          par.withUserRestriction);
      
      if(circle == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.removeCircle(par.circle);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleRemove(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circleUsersAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSCircleUsersAddPar par = (SSCircleUsersAddPar) parA.getFromJSON(SSCircleUsersAddPar.class);
      final SSUri               circleURI;
      
      if(!sql.isGroupOrPubCircleCircle(par.circle)){
        circleURI = null;
      }else{
        circleURI = circleUsersAdd(par);
      }
      
      final SSEntityCircle circle    =
        circleGet(
          new SSCircleGetPar(
            par.user,
            circleURI, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //setEntities
            true, //setUsers,
            par.withUserRestriction, //withUserRestriction,
            true)); //invokeEntityHandlers))
      
      SSServCallerU.handleCircleUsersAdded(
        par.user,
        circle,
        par.users,
        par.withUserRestriction);
      
      final SSCircleUsersAddRet ret = SSCircleUsersAddRet.get(circleURI);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.addUsersToCircle,
          par.circle,
          par.users,
          null,
          null,
          null,
          par.shouldCommit));
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.circleUsersAdd,
          par.circle,  //entity
          null, //content,
          null, //entities
          par.users, //users
          par.shouldCommit));
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleUsersAdd(final SSCircleUsersAddPar par) throws SSErr{
    
    try{
      
      SSEntity circle =
        sql.getEntityTest(
          null,
          par.circle,
          false); //withUserRestriction
      
      if(circle == null){
        return null;
      }
      
      if(par.withUserRestriction){
        
        circle =
          sql.getEntityTest(
            par.user,
            par.circle,
            true); //withUserRestriction
        
        if(circle == null){
          
          final List<String>   invitedUsers = sql.getInvitedUsers(par.circle);
          final SSUserServerI  userServ     = (SSUserServerI) SSServReg.getServ(SSUserServerI.class);
          final SSUsersGetPar  usersGetPar   =
            new SSUsersGetPar(
              par.user,
              par.users,
              false); //invokeEntityHandlers
          
          for(SSEntity userEntity : userServ.usersGet(usersGetPar)){
            
            if(!SSStrU.contains(invitedUsers, ((SSUser) userEntity).email)){
              return null;
            }
          }
        }
      }
      
      if(!SSServCallerU.areUsersUsers(par.users)){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.users){
        sql.addUserToCircleIfNotExists(par.circle, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleUsersAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circleEntitiesAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCircleEntitiesAddPar par      = (SSCircleEntitiesAddPar) parA.getFromJSON(SSCircleEntitiesAddPar.class);
      final SSUri                  cicleURI;
      
      if(!sql.isGroupOrPubCircleCircle(par.circle)){
        cicleURI = null;
      }else{
        cicleURI = circleEntitiesAdd(par);
      }
      
      final SSEntityCircle circle =
        circleGet(
          new SSCircleGetPar(
            par.user,
            par.circle, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            false, //setEntities
            true, //setUsers,
            par.withUserRestriction, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      SSCircleServFct.addTags(
        par.user,
        par.tags,
        par.entities,
        par.circle);
      
      SSCircleServFct.addCategories(
        par.user,
        par.categories,
        par.entities,
        par.circle);
      
      final List<SSEntity> entities =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            par.entities,
            null, //descPar,
            par.withUserRestriction)); //withUserRestriction,
      
      SSServCallerU.handleCircleEntitiesAdded(
        par.user,
        circle,
        entities,
        par.withUserRestriction);
      
      final SSCircleEntitiesAddRet ret = SSCircleEntitiesAddRet.get(cicleURI);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.addEntitiesToCircle,
          par.circle,
          SSUri.asListNotNull(),
          par.entities,
          null,
          null,
          par.shouldCommit));
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.circleEntitiesAdd,
          par.circle,  //entity
          null, //content,
          par.entities,
          null, //users
          par.shouldCommit));
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.circleEntitiesAddTagsAdd,
          par.circle,  //entity
          SSStrU.toCommaSeparatedStrNotNull(par.tags), //content,
          par.entities,
          null, //users
          par.shouldCommit));
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.circleEntitiesAddCategoriesAdd,
          par.circle,  //entity
          SSStrU.toCommaSeparatedStrNotNull(par.categories), //content,
          par.entities,
          null, //users
          par.shouldCommit));
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleEntitiesAdd(final SSCircleEntitiesAddPar par) throws SSErr{
    
    try{
      
      SSEntity circle =
        sql.getEntityTest(
          par.user,
          par.circle,
          par.withUserRestriction);
      
      if(circle == null){
        return null;
      }
      
      final SSEntityServerI   entityServ      = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntityUpdatePar entityUpdatePar =
        new SSEntityUpdatePar(
          par.user,
          null, // entity
          null, //type,
          null, //label,
          null, //description,
          null, //creationTime,
          null, //read,
          null, //setPublic
          true, //createIfNotExists
          par.withUserRestriction, //withUserRestriction
          false); //shouldCommit
      
      SSUri entityURI;
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entityToAdd : par.entities){
        
        entityUpdatePar.entity = entityToAdd;
        entityURI              = entityServ.entityUpdate(entityUpdatePar);
        
        if(entityURI == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
        
        sql.addEntityToCircleIfNotExists(
          par.circle,
          entityToAdd);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleEntitiesAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSCircleE> circleTypesGet(final SSCircleTypesGetPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(par.user == null){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        return sql.getCircleTypesCommonForUserAndEntity(par.user, par.entity);
      }else{
        return sql.getCircleTypesForEntity(par.entity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean circleIsEntityPrivate(final SSCircleIsEntityPrivatePar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      SSEntity entity =
        sql.getEntityTest(
          par.user,
          par.entity,
          false);
      
      if(entity == null){
        return false;
      }
      
      final List<SSCircleE> circleTypes = sql.getCircleTypesCommonForUserAndEntity(par.user, par.entity);
      
      if(
        circleTypes.size() < 1 ||
        circleTypes.size() > 1){
        return false;
      }
      
      return SSStrU.equals(circleTypes.get(0), SSCircleE.priv);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public Boolean circleIsEntityShared(final SSCircleIsEntitySharedPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.forUser == null){
        return SSStrU.contains(sql.getCircleTypesForEntity(par.entity), SSCircleE.group);
      }else{
        return SSStrU.contains(sql.getCircleTypesCommonForUserAndEntity(par.forUser, par.entity), SSCircleE.group);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public Boolean circleIsEntityPublic(final SSCircleIsEntityPublicPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      SSEntity entity =
        sql.getEntityTest(
          null,
          par.entity,
          false);
      
      if(entity == null){
        return true;
      }
      
      return SSStrU.contains(sql.getCircleTypesForEntity(par.entity), SSCircleE.pub);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI circleGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCircleGetPar par = (SSCircleGetPar) parA.getFromJSON(SSCircleGetPar.class);
      
      return SSCircleGetRet.get(circleGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntityCircle circleGet(final SSCircleGetPar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(
          sql.isSystemCircle(par.circle) &&
          !SSStrU.equals(par.circle, pubCircleUri)){
          
          SSLogU.warn(SSErrE.userNotAllowToAccessSystemCircle);
          return null;
        }
      }
      
      SSEntityCircle       circle;
      SSEntityDescriberPar descPar;
      
      circle =
        sql.getCircle(
          par.circle,
          par.setUsers, //withUsers
          par.setEntities, //withEntities
          true, //withCircleRights
          par.entityTypesToIncludeOnly);
      
      if(circle == null){
        return null;
      }
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.circle);
        
        descPar.setProfilePicture = par.setProfilePicture;
        descPar.setThumb          = par.setThumb;
      }else{
        descPar = null;
      }
      
      final SSEntityServerI entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntity        circleEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.circle,
            par.withUserRestriction, //withUserRestriction,
            descPar)); //descPar
      
      if(circleEntity == null){
        return null;
      }
      
      circle =
        SSEntityCircle.get(
          circle,
          circleEntity);
      
      if(par.invokeEntityHandlers){
        
        descPar = new SSEntityDescriberPar(par.circle);
        
        descPar.setOverallRating = true;
        descPar.setTags          = par.setTags;
        descPar.space            = par.tagSpace;
        
        if(par.tagSpace != null){
          
          switch(par.tagSpace){
            case circleSpace:{
              descPar.circle = par.circle;
              break;
            }
          }
        }
        
      }else{
        descPar = null;
      }
      
      if(!circle.entities.isEmpty()){
        
        final List<SSEntity> circleEntities =
          entityServ.entitiesGet(
            new SSEntitiesGetPar(
              par.user,
              SSUri.getDistinctNotNullFromEntities(circle.entities),
              descPar, //descPar
              par.withUserRestriction)); //withUserRestriction
        
        circle.entities.clear();
        circle.entities.addAll(circleEntities);
      }
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.circle);
      }else{
        descPar = null;
      }
      
      if(!circle.users.isEmpty()){
        
        final List<SSEntity> circleUsers =
          entityServ.entitiesGet(
            new SSEntitiesGetPar(
              par.user,
              SSUri.getDistinctNotNullFromEntities(circle.users),
              descPar, //descPar
              par.withUserRestriction)); //withUserRestriction
        
        circle.users.clear();
        circle.users.addAll(circleUsers);
      }
      
      return circle;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circlesGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCirclesGetPar par = (SSCirclesGetPar) parA.getFromJSON(SSCirclesGetPar.class);
      
      return SSCirclesGetRet.get(circlesGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> circlesGet(final SSCirclesGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity>            circles           = new ArrayList<>();
      final List<SSUri>               circleUris        = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        if(
          par.forUser == null &&
          par.entity  == null){
          
          par.forUser = par.user;
        }
        
        if(par.withSystemCircles){
          SSLogU.warn(SSErrE.userNotAllowToAccessSystemCircle);
          return circles;
        }
        
        if(par.entity != null){
          
          final SSEntity entity =
            sql.getEntityTest(
              par.user,
              par.entity,
              par.withUserRestriction);
          
          if(entity == null){
            return new ArrayList<>();
          }
        }
      }
      
      if(!SSObjU.isNull(par.forUser, par.entity)){
        
        circleUris.addAll(
          sql.getCircleURIsCommonForUserAndEntity(
            par.forUser,
            par.entity,
            par.withSystemCircles));
      }else{
        
        if(
          par.forUser   == null &&
          par.entity    == null){
          
          circleUris.addAll(sql.getCircleURIs(par.withSystemCircles));
        }else{
          
          if(par.forUser != null){
            circleUris.addAll(sql.getCircleURIsForUser(par.forUser, par.withSystemCircles));
          }
          
          if(par.entity != null){
            circleUris.addAll(sql.getCircleURIsForEntity(par.entity, par.withSystemCircles));
          }
        }
      }
      
      final SSCircleGetPar circleGetPar =
        new SSCircleGetPar(
          par.user,
          null, //circle
          par.entityTypesToIncludeOnly,
          par.setTags,  //setTags
          null, //tagSpace
          par.setEntities, //setEntities,
          par.setUsers, //setUsers
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      circleGetPar.setProfilePicture = par.setProfilePicture;
      circleGetPar.setThumb          = par.setThumb;
      
      for(SSUri circleURI : circleUris){
        
        circleGetPar.circle = circleURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          circles,
          circleGet(circleGetPar));
      }
      
      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circlePrivURIGet(final SSCirclePrivURIGetPar par) throws SSErr{
    
    try{
      
      SSUri circleURI;
      
      dbSQL.startTrans(par.shouldCommit);
      
      circleURI = sql.getPrivCircleURI(par.user);
      
      if(circleURI != null){
        return circleURI;
      }
      
      circleURI = SSServCaller.vocURICreate();
      
      sql.addEntityIfNotExists(
        circleURI,
        SSEntityE.circle,
        null, //label
        null, //description
        SSVocConf.systemUserUri, //author
        null);
      
      miscFct.addCircle(
        circleURI,
        SSCircleE.priv,
        true,
        par.user);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleURI;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circlePrivURIGet(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circlePubURIGet(final SSCirclePubURIGetPar par) throws SSErr{
    
    try{
      
      final SSUri tmpPublicCircleURI;
      
      if(pubCircleUri != null){
        return pubCircleUri;
      }
      
      pubCircleUri = sql.getPubCircleURI();
      
      if(pubCircleUri != null){
        return pubCircleUri;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      tmpPublicCircleURI = SSServCaller.vocURICreate();
      
      sql.addEntityIfNotExists(
        tmpPublicCircleURI,
        SSEntityE.circle,
        null, //label,
        null, //description,
        SSVocConf.systemUserUri,
        null);//creationTime);
      
      miscFct.addCircle(
        tmpPublicCircleURI,
        SSCircleE.pub,
        true,
        SSVocConf.systemUserUri);
      
      dbSQL.commit(par.shouldCommit);
      
      pubCircleUri = tmpPublicCircleURI;
      
      return pubCircleUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circlePubURIGet(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circleUsersInvite(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSCircleUsersInvitePar par = (SSCircleUsersInvitePar) parA.getFromJSON(SSCircleUsersInvitePar.class);
      
      return SSCircleUsersInviteRet.get(circleUsersInvite(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleUsersInvite(final SSCircleUsersInvitePar par) throws SSErr{
    
    try{
      
      final SSEntity circle =
        sql.getEntityTest(
          par.user,
          par.circle,
          par.withUserRestriction);
      
      if(circle == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.inviteUsers(par.circle, par.emails);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleUsersInvite(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circleTypeChange(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCircleTypeChangePar par = (SSCircleTypeChangePar) parA.getFromJSON(SSCircleTypeChangePar.class);
      
      return SSCircleTypeChangeRet.get(circleTypeChange(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleTypeChange(final SSCircleTypeChangePar par) throws SSErr {
    
    try{
      
      final SSEntity circle =
        sql.getEntityTest(
          par.user,
          par.circle,
          par.withUserRestriction);
      
      if(circle == null){
        return null;
      }
      
      if(sql.isSystemCircle(par.circle)){
        return null;
      }
      
      if(par.withUserRestriction){
        
        if(
          !sql.isGroupOrPubCircleCircle(par.circle) ||
          !sql.isUserAuthor(par.user, par.circle, par.withUserRestriction)){
          return null;
        }
        
        
        switch(par.type){
          
          case group:
          case pubCircle:{
            break;
          }
          
          default: return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.changeCircleType(par.circle, par.type);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleTypeChange(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//  @Override
//  public void circleCanAccess(final SSCircleCanAccessPar par) throws SSErr{
//
//    try{
//
//      if(par.entityURI == null){
//        throw SSErr.get(SSErrE.parameterMissing);
//      }
//
//      if(!sqlFct.existsEntity(par.entityURI)){
//        return;
//      }
//
//      if(SSStrU.equals(par.user, SSVocConf.systemUserUri)){
//        return;
//      }
//
//      if(
//        !miscFct.canUserForEntityType(
//          par.user,
//          sqlFct.getEntity(par.entityURI))){
//
//        throw new SSErr(SSErrE.userNotAllowedToAccessEntity);
//      }
//
//    }catch(SSErr error){
//      SSServErrReg.regErrThrow(error, false);
//    }
//  }

//  @Override
//  public void circlesFromEntityEntitiesAdd(final SSCirclesFromEntityEntitiesAdd par) throws SSErr{
//
//    try{
//
//      dbSQL.startTrans(par.shouldCommit);
//
//      for(SSEntity entityUserCircle :
//        circlesGet(
//          new SSCirclesGetPar(
//            par.user,
//            par.entity,
//            null, //entityTypesToIncludeOnly
//            false, //withUserRestriction
//            true,  //withSystemCircles
//            false))){ //invokeEntityHandlers
//
//        circleEntitiesAdd(
//          new SSCircleEntitiesAddPar(
//            par.user,
//            entityUserCircle.id,
//            par.entities,  //entities
//            false,  //withUserRestriction
//            false)); //shouldCommit
//      }
//
//      dbSQL.commit(par.shouldCommit);
//
//    }catch(Exception error){
//
//      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
//
//        if(dbSQL.rollBack(par.shouldCommit)){
//
//          SSServErrReg.reset();
//
//          circlesFromEntityEntitiesAdd(par);
//        }else{
//          SSServErrReg.regErrThrow(error);
//        }
//      }
//
//      dbSQL.rollBack(par.shouldCommit);
//      SSServErrReg.regErrThrow(error);
//    }
//  }


//@Override
//  public List<SSEntity> circleEntityUsersGet(final SSCircleEntityUsersGetPar par) throws SSErr{
//
////TODO to be integrated with withUserRestriction
//    try{
//
//      final List<SSUri>                   userUris        = new ArrayList<>();
//      final List<SSUri>                   userCircleUris  = sqlFct.getCircleURIsForUser   (par.user, true);
//
//      for(SSUri circleUri : sqlFct.getCircleURIsForEntity(par.entity, true)){
//
//        switch(sqlFct.getTypeForCircle(circleUri)){
//
//          case priv:{
//
//            if(!SSStrU.contains(userCircleUris, circleUri)){
//              continue;
//            }
//
//            if(!SSStrU.contains(userUris, par.user)){
//              userUris.add(par.user);
//            }
//
//            break;
//          }
//
//          case group:{
//
//            if(!SSStrU.contains(userCircleUris, circleUri)){
//              continue;
//            }
//
//            SSUri.addDistinctWithoutNull(
//              userUris,
//              SSUri.getDistinctNotNullFromEntities(sqlFct.getUsersForCircle(circleUri)));
//
//            break;
//          }
//
//          case pub:{
//
//            SSUri.addDistinctWithoutNull(
//              userUris,
//              SSUri.getDistinctNotNullFromEntities(sqlFct.getUsersForCircle(circleUri)));
//
//            break;
//          }
//        }
//      }
//
//      return ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
//        new SSEntitiesGetPar(
//          null,
//          userUris, //entities
//          null, //types
//          null, //descPar
//          false));  //withUserRestriction
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }