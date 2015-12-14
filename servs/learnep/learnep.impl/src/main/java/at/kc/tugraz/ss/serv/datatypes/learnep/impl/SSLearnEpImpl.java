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
package at.kc.tugraz.ss.serv.datatypes.learnep.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpClientI;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockHoldPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionTimelineStateGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionTimelineStateSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCirclesWithEntriesGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsLockHoldPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockHoldRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCircleAddRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionEntityAddRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionTimelineStateGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCircleRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionEntityRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionTimelineStateSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCircleUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionEntityUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpsLockHoldRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.access.SSLearnEpAccessController;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.activity.SSLearnEpActivityAndLogFct;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.sql.SSLearnEpSQLFct;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.adapter.socket.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSCopyEntityI;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntitiesSharedWithUsersI;
import at.tugraz.sss.serv.SSEntitiesSharedWithUsersPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityContext;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import sss.serv.eval.api.SSEvalServerI;

public class SSLearnEpImpl
extends SSServImplWithDBA
implements
  SSLearnEpClientI,
  SSLearnEpServerI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI,
  SSPushEntitiesToUsersI,
  SSEntitiesSharedWithUsersI,
  SSCopyEntityI,
  SSUsersResourcesGathererI{
  
  private final SSLearnEpSQLFct            sql;
  private final SSLearnEpConf              learnEpConf;
  private final SSEntityServerI            entityServ;
  private final SSCircleServerI            circleServ;
  private final SSLearnEpActivityAndLogFct actAndLogFct;
  
  public SSLearnEpImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.learnEpConf  = (SSLearnEpConf) conf;
    this.sql          = new SSLearnEpSQLFct(dbSQL, SSVocConf.systemUserUri);
    this.entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.circleServ   = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
    
    this.actAndLogFct =
      new SSLearnEpActivityAndLogFct(
        (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class),
        (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class));
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    switch(entity.type){
      
      case learnEp:{
        
        return SSLearnEp.get(
          learnEpGet(
            new SSLearnEpGetPar(
              par.user,
              entity.id,
              par.withUserRestriction,
              false)),
          entity);
      }
      
      default: return entity;
    }
  }
  
  @Override
  public void getUsersResources(
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSLearnEpsGetPar learnEpsGetPar =
        new SSLearnEpsGetPar(
          null, //user
          null, //forUser
          false, //withUserRestriction,
          false); //invokeEntityHandlers)
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        learnEpsGetPar.user    = userID;
        learnEpsGetPar.forUser = userID;
        
        for(SSEntity learnEpEntity : learnEpsGet(learnEpsGetPar)){
          
          for(SSEntity versionEntity : ((SSLearnEp) learnEpEntity).entries){
            
            for(SSEntity entity : ((SSLearnEpVersion) versionEntity).learnEpEntities){
              
              usersEntities.get(user).add(
                new SSEntityContext(
                  ((SSLearnEpEntity) entity).entity.id,
                  SSEntityE.learnEp,
                  null,
                  null));
            }
          }
        }
      }
      
    }catch(Exception error){
      
      SSLogU.err(error);
      SSServErrReg.reset();
    }
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      final List<SSUri>    affiliatedURIs     = new ArrayList<>();
      final List<SSEntity> affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        switch(entityAdded.type){
          
          case learnEp:{
            
            if(SSStrU.contains(par.recursiveEntities, entityAdded)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityAdded.id);
            }
            
            for(SSUri learnEpContentURI : getLearnEpAffiliatedURIs(entityAdded.id)){
              
              if(SSStrU.contains(par.recursiveEntities, learnEpContentURI)){
                continue;
              }
              
              SSUri.addDistinctWithoutNull(
                affiliatedURIs,
                learnEpContentURI);
            }
          }
        }
      }
      
      if(affiliatedURIs.isEmpty()){
        return affiliatedEntities;
      }
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            affiliatedURIs,
            null, //descPar
            par.withUserRestriction)));
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          par.user,
          par.circle,
          affiliatedURIs,
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        SSServCallerU.handleAddAffiliatedEntitiesToCircle(
          par.user,
          par.circle,
          affiliatedEntities, //entities
          par.recursiveEntities,
          par.withUserRestriction));
      
      return affiliatedEntities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void pushEntitiesToUsers(
    final SSPushEntitiesToUsersPar par) throws SSErr {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          
          case learnEp: {
            
            for(SSUri userToPushTo : par.users){
              sql.addLearnEp(entityToPush.id, userToPushTo);
            }
            
            break;
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void entitiesSharedWithUsers(SSEntitiesSharedWithUsersPar par) throws SSErr {
    
    for(SSEntity entityShared : par.circle.entities){
      
      switch(entityShared.type){
        
        case learnEp:{
          
          actAndLogFct.shareLearnEp(
            par.user,
            entityShared.id,
            SSUri.getDistinctNotNullFromEntities(par.circle.users),
            false);
          
          break;
        }
      }
    }
  }
  
  @Override
  public void copyEntity(
    final SSEntity                  entity,
    final SSEntityCopyPar           par) throws SSErr{
    
    try{
      
      if(!SSStrU.equals(entity.type, SSEntityE.learnEp)){
        return;
      }
      
      final List<SSEntity> copiedEntities   = new ArrayList<>();
      final List<SSEntity> entitiesIncluded = new ArrayList<>();
      SSUri                copyVersionUri;
      SSUri                copyLearnEpUri;
      
      final SSLearnEp learnEp =
        learnEpGet(
          new SSLearnEpGetPar(
            par.user,
            entity.id,
            par.withUserRestriction,
            true)); //invokeEntityHandlers
      
      for(SSUri forUser : par.forUsers){
        
        copiedEntities.clear();
        
        copyLearnEpUri =
          learnEpCreate(
            new SSLearnEpCreatePar(
              forUser,
              learnEp.label,
              learnEp.description,
              false,
              false));
        
        for(SSEntity version : learnEp.entries){
          
          copyVersionUri =
            learnEpVersionCreate(
              new SSLearnEpVersionCreatePar(
                forUser,
                copyLearnEpUri,
                false,
                false));
          
          for(SSEntity circle : ((SSLearnEpVersion) version).learnEpCircles){
            
            if(SSStrU.contains(par.entitiesToExclude, circle.id)){
              continue;
            }
            
            entitiesIncluded.add(circle);
            
            learnEpVersionCircleAdd(
              new SSLearnEpVersionCircleAddPar(
                forUser,
                copyVersionUri,
                circle.label,
                ((SSLearnEpCircle) circle).xLabel,
                ((SSLearnEpCircle) circle).yLabel,
                ((SSLearnEpCircle) circle).xR,
                ((SSLearnEpCircle) circle).yR,
                ((SSLearnEpCircle) circle).xC,
                ((SSLearnEpCircle) circle).yC,
                false,
                false));
          }
          
          for(SSEntity learnEpEntity : ((SSLearnEpVersion) version).learnEpEntities){
            
            if(
              SSStrU.contains(par.entitiesToExclude, learnEpEntity.id) ||
              SSStrU.contains(par.entitiesToExclude, ((SSLearnEpEntity) learnEpEntity).entity)){
              continue;
            }
            
            entitiesIncluded.add(((SSLearnEpEntity) learnEpEntity).entity);
            
            learnEpVersionEntityAdd(
              new SSLearnEpVersionEntityAddPar(
                forUser,
                copyVersionUri,
                ((SSLearnEpEntity) learnEpEntity).entity.id,
                ((SSLearnEpEntity) learnEpEntity).x,
                ((SSLearnEpEntity) learnEpEntity).y,
                false,
                false));
            
            copiedEntities.add(((SSLearnEpEntity) learnEpEntity).entity);
          }
          
          if(((SSLearnEpVersion) version).learnEpTimelineState != null){
            
            learnEpVersionTimelineStateSet(
              new SSLearnEpVersionTimelineStateSetPar(
                forUser,
                copyVersionUri,
                ((SSLearnEpVersion) version).learnEpTimelineState.startTime,
                ((SSLearnEpVersion) version).learnEpTimelineState.endTime,
                false,
                false));
          }
        }
        
        SSServCallerU.handleEntityCopied(
          par.user,  //user
          forUser,  //forUser
          learnEp, //entity
          copiedEntities, //entities
          copyLearnEpUri, //targetEntity
          par.withUserRestriction);
        
        SSServCallerU.handleCirclesFromEntityGetEntitiesAdd(
          circleServ,
          entityServ,
          par.user,
          copyLearnEpUri,
          getLearnEpAffiliatedURIs(copyLearnEpUri), //entities
          par.withUserRestriction,
          true); //invokeEntityHandlers
      }
      
      actAndLogFct.copyLearnEp(
        par.user,
        learnEp.id,  //learnEp
        par.forUsers, //usersToCopyFor
        SSUri.getDistinctNotNullFromEntities(entitiesIncluded), //includedEntities
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void learnEpsGet(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpsGetPar par = (SSLearnEpsGetPar) parA.getFromJSON(SSLearnEpsGetPar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpsGetRet.get(learnEpsGet(par)));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSEntity> learnEpsGet(final SSLearnEpsGetPar par) throws SSErr{
    
    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(!SSStrU.equals(par.forUser, par.user)){
          par.forUser = par.user;
        }
      }
      
      final List<SSUri> learnEpURIs   = new ArrayList<>();
      
      if(par.forUser == null){
        learnEpURIs.addAll(sql.getLearnEpURIs());
      }else{
        learnEpURIs.addAll(sql.getLearnEpURIs(par.forUser));
      }
      
      final List<SSEntity>    learnEps      = new ArrayList<>();
      final SSLearnEpGetPar   learnEpGetPar =
        new SSLearnEpGetPar(
          par.user,
          null,
          par.withUserRestriction, //withUserRestriction,
          par.invokeEntityHandlers);
      
      learnEpGetPar.setRead        = par.setRead;
      learnEpGetPar.setCircleTypes = par.setCircleTypes;
      
      for(SSUri learnEpURI : learnEpURIs){
        
        learnEpGetPar.learnEp = learnEpURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          learnEps,
          learnEpGet(learnEpGetPar));
      }
      
      return learnEps;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLearnEp learnEpGet(final SSLearnEpGetPar par) throws SSErr{
    
    try{
      
      SSLearnEp learnEp = sql.getLearnEp(par.learnEp);
      
      if(learnEp == null){
        return null;
      }
      
      SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar                  = new SSEntityDescriberPar(par.learnEp);
        descPar.setRead          = par.setRead;
        descPar.setCircleTypes   = par.setCircleTypes;
      }else{
        descPar = null;
      }
      
      final SSEntity learnEpEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.learnEp,
            par.withUserRestriction,
            descPar));
      
      if(learnEpEntity == null){
        return null;
      }
      
      learnEp =
        SSLearnEp.get(
          learnEp,
          learnEpEntity);
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(learnEp.id);
      }else{
        descPar = null;
      }
      
      final SSEntitiesGetPar entitiesGetPar =
        new SSEntitiesGetPar(
          par.user,
          sql.getLearnEpUserURIs(learnEp.id), //entities
          descPar, //descPar
          par.withUserRestriction);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        learnEp.users,
        entityServ.entitiesGet(entitiesGetPar));
      
      final SSLearnEpVersionsGetPar learnEpVersionsGetPar =
        new SSLearnEpVersionsGetPar(
          par.user,
          learnEp.id, //learnEp
          null, //learnEpVersions,
          par.withUserRestriction, //withUserRestriction
          par.invokeEntityHandlers);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        learnEp.entries,
        learnEpVersionsGet(learnEpVersionsGetPar));
      
      if(!learnEpConf.useEpisodeLocking){
        learnEp.locked       = false;
        learnEp.lockedByUser = false;
      }else{
        
        learnEp.locked       = SSLearnEpAccessController.isLocked (learnEp.id);
        learnEp.lockedByUser = SSLearnEpAccessController.hasLock  (par.user, learnEp.id);
      }
      
      return learnEp;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpVersionsGet(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionsGetPar par = (SSLearnEpVersionsGetPar) parA.getFromJSON(SSLearnEpVersionsGetPar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionsGetRet.get(learnEpVersionsGet(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSEntity> learnEpVersionsGet(final SSLearnEpVersionsGetPar par) throws SSErr{
    
    try{
      
      final SSLearnEpVersionsGetFct fct          = new SSLearnEpVersionsGetFct(entityServ, sql);
      final List<SSEntity>          versions     = new ArrayList<>();
      final SSEntityGetPar          entityGetPar =
        new SSEntityGetPar(
          par.user,
          null, //entity
          par.withUserRestriction, //withUserRestriction
          null); // descPar
      
      SSLearnEpVersion              version;
      SSEntity                      versionEntity;
      SSEntityDescriberPar          descPar;
      
      for(SSUri versionUri : fct.getLearnEpVersionURIsToFill(par)){
        
        version =
          sql.getLearnEpVersion(
            versionUri,
            false, //setCircles
            false, //setEntities
            false); //setTimelineState
        
        if(version == null){
          continue;
        }
        
        if(par.invokeEntityHandlers){
          descPar = new SSEntityDescriberPar(versionUri);
        }else{
          descPar = null;
        }
        
        entityGetPar.entity  = versionUri;
        entityGetPar.descPar = descPar;
        
        versionEntity = entityServ.entityGet(entityGetPar);
        
        if(versionEntity == null){
          continue;
        }
        
        version =
          SSLearnEpVersion.get(
            version,
            versionEntity);
        
        fct.setLearnEpVersionCircles       (version);
        fct.setLearnEpVersionEntities      (par, version);
        fct.setLearnEpVersionTimelineState (version);
        
        versions.add(version);
      }
      
      return versions;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpVersionGet(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionGetPar par = (SSLearnEpVersionGetPar) parA.getFromJSON(SSLearnEpVersionGetPar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionGetRet.get(learnEpVersionGet(par)));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSLearnEpVersion learnEpVersionGet(final SSLearnEpVersionGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> learnEpVersions =
        learnEpVersionsGet(
          new SSLearnEpVersionsGetPar(
            par.user,
            null, //learnEp
            SSUri.asListNotNull(par.learnEpVersion), //learnEpVersions
            par.withUserRestriction,
            par.invokeEntityHandlers));
      
      if(learnEpVersions.isEmpty()){
        return null;
      }
      
      return (SSLearnEpVersion) learnEpVersions.get(0);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpRemove(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpRemovePar par = (SSLearnEpRemovePar) parA.getFromJSON(SSLearnEpRemovePar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpRemoveRet.get(learnEpRemove(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSUri learnEpRemove(final SSLearnEpRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity learnEp =
        sql.getEntityTest(
          par.user,
          par.learnEp,
          par.withUserRestriction);
      
      if(learnEp == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.removeLearnEpForUser(par.user, par.learnEp);
      
      for(SSUri version : sql.getLearnEpVersionURIs(par.learnEp)){
        sql.deleteCurrentEpVersion (par.user, version);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.learnEp;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpRemove(par);
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
  public void learnEpVersionCreate(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionCreatePar par = (SSLearnEpVersionCreatePar) parA.getFromJSON(SSLearnEpVersionCreatePar.class);
      
      SSLearnEpAccessController.checkHasLock(
        learnEpConf,
        par.user,
        par.learnEp);
      
      final SSUri result = learnEpVersionCreate(par);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionCreateRet.get(result));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
//    SSLearnEpActivityFct.createLearnEpVersion(new SSLearnEpVersionCreatePar(parA), result);
  }
  
  @Override
  public SSUri learnEpVersionCreate(final SSLearnEpVersionCreatePar par) throws SSErr{
    
    try{
      
      final SSEntity learnEp =
        sql.getEntityTest(
          par.user,
          par.learnEp,
          par.withUserRestriction);
      
      if(learnEp == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri learnEpVersion =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSServCaller.vocURICreate(),
            SSEntityE.learnEpVersion, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //creatIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(learnEpVersion == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.createLearnEpVersion(
        learnEpVersion,
        par.learnEp);
      
      SSServCallerU.handleCirclesFromEntityGetEntitiesAdd(
        circleServ,
        entityServ,
        par.user,
        par.learnEp,
        SSUri.asListNotNull(learnEpVersion),
        false, //withUserRestriction
        false); //invokeEntityHandlers
      
      dbSQL.commit(par.shouldCommit);
      
      return learnEpVersion;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCreate(par);
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
  public void learnEpVersionCircleAdd(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionCircleAddPar par     = (SSLearnEpVersionCircleAddPar) parA.getFromJSON(SSLearnEpVersionCircleAddPar.class);
      final SSUri                        learnEp = sql.getLearnEpForVersion(par.learnEpVersion);
      
      final SSUri                        newCircle;
      
      SSLearnEpAccessController.checkHasLock(
        learnEpConf,
        par.user,
        learnEp);
      
      final List<SSEntity>                  versionCirclesBefore   = new ArrayList<>();
      final List<SSEntity>                  versionCirclesAfter    = new ArrayList<>();
      final List<SSUri>                     entityURIsBefore       = new ArrayList<>();
      final List<SSUri>                     entityURIsAfter        = new ArrayList<>();
      
      if(
        par.xLabel  != null ||
        par.yLabel  != null ||
        par.xR      != null ||
        par.yR      != null ||
        par.xC      != null ||
        par.yC      != null){
        
        versionCirclesBefore.addAll(
          learnEpVersionCirclesWithEntriesGet(
            new SSLearnEpVersionCirclesWithEntriesGetPar(
              par.user,
              par.learnEpVersion,
              false, //withUserRestriction
              false))); //invokeEntityHandlers
      }
      
      newCircle = learnEpVersionCircleAdd(par);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionCircleAddRet.get(newCircle));
      
      if(newCircle != null){
        
        actAndLogFct.addCircleToLearnEpVersion(
          par.user,
          par.learnEpVersion,
          newCircle,
          learnEp,
          par.shouldCommit);
        
        if(
          par.xLabel  == null &&
          par.yLabel  == null &&
          par.xR      == null &&
          par.yR      == null &&
          par.xC      == null &&
          par.yC      == null){
          
          return;
        }
        
        versionCirclesAfter.addAll(
          learnEpVersionCirclesWithEntriesGet(
            new SSLearnEpVersionCirclesWithEntriesGetPar(
              par.user,
              par.learnEpVersion,
              false, //withUserRestriction
              false))); //invokeEntityHandlers
        
        for(SSEntity circle : versionCirclesBefore){
          
          if(!SSStrU.equals(newCircle, circle)){
            continue;
          }
          
          entityURIsBefore.addAll(SSUri.getDistinctNotNullFromEntities(circle.entries));
        }
        
        for(SSEntity circle : versionCirclesAfter){
          
          if(!SSStrU.equals(newCircle, circle)){
            continue;
          }
          
          entityURIsAfter.addAll(SSUri.getDistinctNotNullFromEntities(circle.entries));
        }
        
        for(SSUri entityURI : entityURIsBefore){
          
          if(!SSStrU.contains(entityURIsAfter, entityURI)){
            
            actAndLogFct.removeEntityFromLearnEpCircle(
              par.user,
              learnEp,
              par.learnEpVersion,
              entityURI,
              sql.getEntity(par.learnEpVersion, entityURI), //entity,
              newCircle,
              par.shouldCommit);
          }
        }
        
        for(SSUri entityURI : entityURIsAfter){
          
          if(!SSStrU.contains(entityURIsBefore, entityURI)){
            
            actAndLogFct.addEntityToLearnEpCircle(
              par.user,
              learnEp,
              par.learnEpVersion,
              entityURI,
              sql.getEntity(par.learnEpVersion, entityURI), //entity,
              newCircle,
              par.shouldCommit);
          }
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSUri learnEpVersionCircleAdd(final SSLearnEpVersionCircleAddPar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpVersion =
        sql.getEntityTest(
          par.user,
          par.learnEpVersion,
          par.withUserRestriction);
      
      if(learnEpVersion == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri circle =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSServCaller.vocURICreate(),
            SSEntityE.learnEpCircle, //type,
            par.label, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(circle == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.addCircleToLearnEpVersion(
        circle,
        par.learnEpVersion,
        par.label,
        par.xLabel,
        par.yLabel,
        par.xR,
        par.yR,
        par.xC,
        par.yC);
      
      SSServCallerU.handleCirclesFromEntityGetEntitiesAdd(
        circleServ,
        entityServ,
        par.user,
        par.learnEpVersion,
        SSUri.asListNotNull(circle),
        false, //withUserRestriction
        false); //invokeEntityHandlers
      
      dbSQL.commit(par.shouldCommit);
      
      return circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCircleAdd(par);
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
  public void learnEpVersionEntityAdd(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionEntityAddPar par     = (SSLearnEpVersionEntityAddPar) parA.getFromJSON(SSLearnEpVersionEntityAddPar.class);
      final SSUri                        learnEp = sql.getLearnEpForVersion(par.learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (
        learnEpConf,
        par.user,
        learnEp);
      
      final List<SSEntity> versionCirclesBefore   = new ArrayList<>();
      
      versionCirclesBefore.addAll(
        learnEpVersionCirclesWithEntriesGet(
          new SSLearnEpVersionCirclesWithEntriesGetPar(
            par.user,
            par.learnEpVersion,
            false, //withUserRestriction
            false))); //invokeEntityHandlers
      
      final SSUri learnEpEntity = learnEpVersionEntityAdd(par);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionEntityAddRet.get(learnEpEntity));
      
      if(learnEpEntity != null){
        
        actAndLogFct.handleEntityAddToOrRemoveFromLearnEpCircle(
          this,
          par.user,
          learnEp,
          par.learnEpVersion,
          learnEpEntity,
          par.entity,
          versionCirclesBefore,
          false, //calledFromRemove
          true, //calledFromAdd
          par.shouldCommit);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSUri learnEpVersionEntityAdd(final SSLearnEpVersionEntityAddPar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpVersion =
        sql.getEntityTest(
          par.user,
          par.learnEpVersion,
          par.withUserRestriction);
      
      if(learnEpVersion == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity,
            null, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(entity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      final SSUri learnEpEntity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSServCaller.vocURICreate(),
            SSEntityE.learnEpEntity, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(learnEpEntity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.addEntityToLearnEpVersion(
        learnEpEntity,
        learnEpVersion.id,
        entity,
        par.x,
        par.y);
      
      //DONE replace this parts with circleContentedChanged:
      //0. first split / refactor circleContentChanged to. circleEntitiesAdded, circleUsersAdded, sharedWithUsers
      //1. dont retrieve entities for learn ep entity here anymore
      //2. extend circlesFromEntityEntitiesAdd to call "circleContentChanged" upon each circle to be touched
      //3. for each entity targeted in "circleContentChanged" call "circleContentChanged" for its affiliated entities
      //4. make sure to avoid recursion on entites via list keeping all entities for "circleContentChanged" was called so far
      //5. call this process whereever "circlesFromEntityEntitiesAdd" is/can be called
      //      entities.addAll(miscFct.getLearnEpEntityAttachedEntities(par.user, par.entity, par.withUserRestriction));
      //      circleServ.circlesFromEntityEntitiesAdd(
//        new SSCirclesFromEntityEntitiesAdd(
//          null,
//          null,
//          par.user,
//          par.learnEpVersion,
//          entities,
//          false));
      
      SSServCallerU.handleCirclesFromEntityGetEntitiesAdd(
        circleServ,
        entityServ,
        par.user,
        par.learnEpVersion,
        SSUri.asListNotNull(learnEpEntity),
        false, //withUserRestriction,
        false); //invokeEntityHandlers);
      
      SSServCallerU.handleCirclesFromEntityGetEntitiesAdd(
        circleServ,
        entityServ,
        par.user,
        par.learnEpVersion,
        SSUri.asListNotNull(entity),
        par.withUserRestriction, //withUserRestriction
        true); //invokeEntityHandlers);
      
      dbSQL.commit(par.shouldCommit);
      
      return learnEpEntity;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionEntityAdd(par);
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
  public void learnEpCreate(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpCreatePar par = (SSLearnEpCreatePar) parA.getFromJSON(SSLearnEpCreatePar.class);
      
      final SSUri learnEp = learnEpCreate(par);
      
      sSCon.writeRetFullToClient(SSLearnEpCreateRet.get(learnEp));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
//    SSLearnEpActivityFct.createLearnEp(new SSLearnEpCreatePar(parA), learnEp);
  }
  
  @Override
  public SSUri learnEpCreate(final SSLearnEpCreatePar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri learnEp =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSServCaller.vocURICreate(),
            SSEntityE.learnEp, //type,
            par.label, //label
            par.description,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(learnEp == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.createLearnEp(learnEp, par.user);
      
      dbSQL.commit(par.shouldCommit);
      
      return learnEp;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpCreate(par);
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
  public void learnEpVersionCircleUpdate(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionCircleUpdatePar par             = (SSLearnEpVersionCircleUpdatePar) parA.getFromJSON(SSLearnEpVersionCircleUpdatePar.class);
      final SSUri                           learnEp         = sql.getLearnEpForCircle       (par.learnEpCircle);
      final SSUri                           learnEpVersion  = sql.getLearnEpVersionForCircle(par.learnEpCircle);
      final SSEntity                        circleEntity    =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.learnEpCircle,
            false,
            null));
      
      SSLearnEpAccessController.checkHasLock(
        learnEpConf,
        par.user,
        learnEp);
      
      final Boolean                         worked;
      
      worked = learnEpVersionCircleUpdate(par);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionCircleUpdateRet.get(worked));
      
      if(!worked){
        return;
      }
      
      actAndLogFct.changeLearnEpVersionCircleLabelAndDescription(
        par.user,
        circleEntity,
        par.label,
        par.description,
        learnEpVersion,
        par.learnEpCircle,
        learnEp,
        par.shouldCommit);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean learnEpVersionCircleUpdate(final SSLearnEpVersionCircleUpdatePar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri circle =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.learnEpCircle,
            SSEntityE.learnEpCircle, //type,
            par.label, //label
            par.description,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            false, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(circle == null){
        dbSQL.rollBack(par.shouldCommit);
        return false;
      }
      
      sql.updateCircle(
        circle,
        par.xLabel,
        par.yLabel,
        par.xR,
        par.yR,
        par.xC,
        par.yC);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCircleUpdate(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public void learnEpVersionEntityUpdate(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionEntityUpdatePar par            = (SSLearnEpVersionEntityUpdatePar) parA.getFromJSON(SSLearnEpVersionEntityUpdatePar.class);
      final SSUri                           learnEp        = sql.getLearnEpForEntity        (par.learnEpEntity);
      final SSUri                           learnEpVersion = sql.getLearnEpVersionForEntity (par.learnEpEntity);
      final SSUri                           entity         = sql.getEntity                  (learnEpVersion, par.learnEpEntity);
      
      SSLearnEpAccessController.checkHasLock(
        learnEpConf,
        par.user,
        learnEp);
      
      final List<SSEntity>                  versionCirclesBefore   = new ArrayList<>();
      final Boolean                         worked;
      
      versionCirclesBefore.addAll(
        learnEpVersionCirclesWithEntriesGet(
          new SSLearnEpVersionCirclesWithEntriesGetPar(
            par.user,
            learnEpVersion,
            false, //withUserRestriction
            false))); //invokeEntityHandlers
      
      worked = learnEpVersionEntityUpdate(par);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionEntityUpdateRet.get(worked));
      
      if(!worked){
        return;
      }
      
      actAndLogFct.handleEntityAddToOrRemoveFromLearnEpCircle(
        this,
        par.user,
        learnEp,
        learnEpVersion,
        par.learnEpEntity,
        entity, //entity
        versionCirclesBefore,
        false, //calledFromRemove
        false, //calledFromAdd
        par.shouldCommit);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean learnEpVersionEntityUpdate(final SSLearnEpVersionEntityUpdatePar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpEntity =
        sql.getEntityTest(
          par.user,
          par.learnEpEntity,
          par.withUserRestriction);
      
      if(learnEpEntity == null){
        return false;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.updateEntity(
        learnEpEntity.id,
        par.x,
        par.y);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionEntityUpdate(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public void learnEpVersionCircleRemove(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionCircleRemovePar par              = (SSLearnEpVersionCircleRemovePar) parA.getFromJSON(SSLearnEpVersionCircleRemovePar.class);
      final SSUri                           learnEp          = sql.getLearnEpForCircle        (par.learnEpCircle);
      final SSUri                           learnEpVersion   = sql.getLearnEpVersionForCircle (par.learnEpCircle);
      final Boolean                         worked;
      
      SSLearnEpAccessController.checkHasLock (
        learnEpConf,
        par.user,
        learnEp);
      
      final List<SSEntity> versionCirclesBefore   = new ArrayList<>();
      
      versionCirclesBefore.addAll(
        learnEpVersionCirclesWithEntriesGet(
          new SSLearnEpVersionCirclesWithEntriesGetPar(
            par.user,
            learnEpVersion,
            false, //withUserRestriction
            false))); //invokeEntityHandlers
      
      worked = learnEpVersionCircleRemove(par);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionCircleRemoveRet.get(worked));
      
      if(worked){
        
        for(SSEntity circle : versionCirclesBefore){
          
          if(!SSStrU.equals(par.learnEpCircle, circle)){
            continue;
          }
          
          actAndLogFct.handleRemoveLearnEpVersionCircleWithEntities(
            par.user,
            learnEpVersion,
            learnEp,
            circle,
            par.shouldCommit);
          
          break;
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean learnEpVersionCircleRemove(final SSLearnEpVersionCircleRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpCircle =
        sql.getEntityTest(
          par.user,
          par.learnEpCircle,
          par.withUserRestriction);
      
      if(learnEpCircle == null){
        return false;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.deleteCircle(learnEpCircle.id);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCircleRemove(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public void learnEpVersionEntityRemove(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionEntityRemovePar par            = (SSLearnEpVersionEntityRemovePar) parA.getFromJSON(SSLearnEpVersionEntityRemovePar.class);
      final SSUri                           learnEpVersion = sql.getLearnEpVersionForEntity       (par.learnEpEntity);
      final SSUri                           learnEp        = sql.getLearnEpForVersion             (learnEpVersion);
      final SSUri                           entity         = sql.getEntity                        (learnEpVersion, par.learnEpEntity);
      final Boolean                         worked;
      
      SSLearnEpAccessController.checkHasLock(
        learnEpConf,
        par.user,
        learnEp);
      
      final List<SSEntity> versionCirclesBefore   = new ArrayList<>();
      
      versionCirclesBefore.addAll(
        learnEpVersionCirclesWithEntriesGet(
          new SSLearnEpVersionCirclesWithEntriesGetPar(
            par.user,
            learnEpVersion,
            false, //withUserRestriction
            false))); //invokeEntityHandlers
      
      worked = learnEpVersionEntityRemove(par);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionEntityRemoveRet.get(worked));
      
      if(worked){
        
        actAndLogFct.handleEntityAddToOrRemoveFromLearnEpCircle(
          this,
          par.user,
          learnEp,
          learnEpVersion,
          par.learnEpEntity,
          entity,
          versionCirclesBefore,
          true, //calledFromRemove
          false, //calledFromAdd
          par.shouldCommit);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean learnEpVersionEntityRemove(final SSLearnEpVersionEntityRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpEntity =
        sql.getEntityTest(
          par.user,
          par.learnEpEntity,
          par.withUserRestriction);
      
      if(learnEpEntity == null){
        return false;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.deleteEntity(learnEpEntity.id);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionEntityRemove(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public void learnEpVersionTimelineStateSet(final SSSocketCon sSCon, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionTimelineStateSetPar par = (SSLearnEpVersionTimelineStateSetPar) parA.getFromJSON(SSLearnEpVersionTimelineStateSetPar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionTimelineStateSetRet.get(learnEpVersionTimelineStateSet(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSUri learnEpVersionTimelineStateSet(final SSLearnEpVersionTimelineStateSetPar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpVerison =
        sql.getEntityTest(
          par.user,
          par.learnEpVersion,
          par.withUserRestriction);
      
      if(learnEpVerison == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri learnEpTimelineStateUri =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSServCaller.vocURICreate(),
            SSEntityE.learnEpTimelineState, //type,
            null, //label
            null,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(learnEpTimelineStateUri == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.setLearnEpVersionTimelineState(
        learnEpTimelineStateUri,
        par.learnEpVersion,
        par.startTime,
        par.endTime);
      
      SSServCallerU.handleCirclesFromEntityGetEntitiesAdd(
        circleServ,
        entityServ,
        par.user,
        par.learnEpVersion,
        SSUri.asListNotNull(learnEpTimelineStateUri),
        false, //withUserRestriction
        false); //invokeEntityHandlers);
      
      dbSQL.commit(par.shouldCommit);
      
      return learnEpTimelineStateUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionTimelineStateSet(par);
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
  public List<SSEntity> learnEpVersionCirclesWithEntriesGet(final SSLearnEpVersionCirclesWithEntriesGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> circles = new ArrayList<>();
      
      final SSLearnEpVersion version =
        learnEpVersionGet(
          new SSLearnEpVersionGetPar(
            par.user,
            par.learnEpVersion,
            false, //withUserRestriction
            false)); //invokeEntityHandlers
      
      if(version == null){
        return circles;
      }
      
      SSLearnEpCircle circle;
      SSLearnEpEntity entity;
      double first, firstDivisor, second, secondDivisor, firstResult, secondResult, result;
      
      for(SSEntity circleEntity : version.learnEpCircles){
        
        circle = (SSLearnEpCircle) circleEntity;
        
        for(SSEntity entityEntity : version.learnEpEntities){
          
          entity = (SSLearnEpEntity) entityEntity;
          
          //(xEntity - xCircle)^2 / rxCircle^2 + same for y <= 1 then the entiy is wihtin the circle
          
          first         = Math.pow(Math.subtractExact(entity.x.longValue() + 25, circle.xC.longValue()), 2);
          firstDivisor  = Math.pow(circle.xR.longValue(), 2);
          second        = Math.pow(Math.subtractExact(entity.y.longValue() + 25, circle.yC.longValue()), 2);
          secondDivisor = Math.pow(circle.yR.longValue(), 2);
          
          if(firstDivisor == 0 || secondDivisor == 0){
            continue;
          }
          
          firstResult  = first / firstDivisor;
          secondResult = second / secondDivisor;
          result       = firstResult + secondResult;
          
          if(result > 1){
            continue;
          }
          
          SSEntity.addEntitiesDistinctWithoutNull(
            circle.entries,
            entity);
        }
      }
      
      SSEntity.addEntitiesDistinctWithoutNull(
        circles,
        version.learnEpCircles);
      
      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpVersionTimelineStateGet(final SSSocketCon sSCon, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionTimelineStateGetPar par = (SSLearnEpVersionTimelineStateGetPar) parA.getFromJSON(SSLearnEpVersionTimelineStateGetPar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionTimelineStateGetRet.get(learnEpVersionTimelineStateGet(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSLearnEpTimelineState learnEpVersionTimelineStateGet(final SSLearnEpVersionTimelineStateGetPar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpVerison =
        sql.getEntityTest(
          par.user,
          par.learnEpVersion,
          par.withUserRestriction);
      
      if(learnEpVerison == null){
        return null;
      }
      
      return sql.getLearnEpVersionTimelineState(learnEpVerison.id);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpVersionCurrentGet(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionCurrentGetPar par = (SSLearnEpVersionCurrentGetPar) parA.getFromJSON(SSLearnEpVersionCurrentGetPar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionCurrentGetRet.get(learnEpVersionCurrentGet(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSLearnEpVersion learnEpVersionCurrentGet(final SSLearnEpVersionCurrentGetPar par) throws SSErr{
    
    try{
      
      return learnEpVersionGet(
        new SSLearnEpVersionGetPar(
          par.user,
          sql.getLearnEpCurrentVersionURI(par.user),
          par.withUserRestriction,
          par.invokeEntityHandlers));
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.learnEpCurrentVersionNotSet)){
        SSServErrReg.regErrThrow(error, false);
      }else{
        SSServErrReg.regErrThrow(error);
      }
      
      return null;
    }
  }
  
  @Override
  public void learnEpVersionCurrentSet(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpVersionCurrentSetPar par = (SSLearnEpVersionCurrentSetPar) parA.getFromJSON(SSLearnEpVersionCurrentSetPar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpVersionCurrentSetRet.get(learnEpVersionCurrentSet(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
//    SSLearnEpActivityFct.setCurrentLearnEpVersion(new SSLearnEpVersionCurrentSetPar(parA));
  }
  
  @Override
  public SSUri learnEpVersionCurrentSet(final SSLearnEpVersionCurrentSetPar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpVerison =
        sql.getEntityTest(
          par.user,
          par.learnEpVersion,
          par.withUserRestriction);
      
      if(learnEpVerison == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.setLearnEpCurrentVersion(par.user, par.learnEpVersion);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.learnEpVersion;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCurrentSet(par);
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
  public void learnEpsLockHold(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpsLockHoldPar par = (SSLearnEpsLockHoldPar) parA.getFromJSON(SSLearnEpsLockHoldPar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpsLockHoldRet.get(learnEpsLockHold(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSLearnEpLockHoldRet> learnEpsLockHold(final SSLearnEpsLockHoldPar par) throws SSErr{
    
    try{
      final List<SSLearnEpLockHoldRet> locks = new ArrayList<>();
      final SSLearnEpLockHoldPar       learnEpLockHoldPar;
      
      if(learnEpConf.useEpisodeLocking){
        
        learnEpLockHoldPar =
          new SSLearnEpLockHoldPar(
            par.user,
            null,
            true);
        
        if(!par.learnEps.isEmpty()){
          
          for(SSUri learnEp : par.learnEps){
            
            learnEpLockHoldPar.learnEp = learnEp;
            
            locks.add(learnEpLockHold(learnEpLockHoldPar));
          }
        }else{
          
          for(SSUri learnEp : sql.getLearnEpURIs(par.user)){
            
            learnEpLockHoldPar.learnEp = learnEp;
            
            locks.add(learnEpLockHold(learnEpLockHoldPar));
          }
        }
        
        return locks;
        
      }else{
        return locks;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLearnEpLockHoldRet learnEpLockHold(final SSLearnEpLockHoldPar par) throws SSErr{
    
    try{
      final SSLearnEpLockHoldRet ret;
      
      if(learnEpConf.useEpisodeLocking){
        
        ret =
          SSLearnEpLockHoldRet.get(
            par.learnEp,
            SSLearnEpAccessController.isLocked(par.learnEp),
            SSLearnEpAccessController.hasLock(
              par.user,
              par.learnEp),
            SSLearnEpAccessController.getRemainingTime(par.learnEp));
        
      }else{
        
        ret =
          SSLearnEpLockHoldRet.get(
            par.learnEp,
            false,
            false,
            0L);
      }
      
      return ret;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpLockSet(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpLockSetPar par = (SSLearnEpLockSetPar) parA.getFromJSON(SSLearnEpLockSetPar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpLockSetRet.get(learnEpLockSet(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean learnEpLockSet(final SSLearnEpLockSetPar par) throws SSErr{
    
    try{
      if(par.withUserRestriction){
        
        final SSEntity learnEp =
          sql.getEntityTest(
            par.user,
            par.learnEp,
            par.withUserRestriction);
        
        if(learnEp == null){
          return false;
        }
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
      }else{
        
        if(par.forUser == null){
          throw SSErr.get(SSErrE.parameterMissing);
        }
      }
      
      Boolean lockResult = false;
      
      if(learnEpConf.useEpisodeLocking){
        
        lockResult = SSLearnEpAccessController.lock(par.forUser, par.learnEp);
      }
      
      return lockResult;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public void learnEpLockRemove(SSSocketCon sSCon, SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSLearnEpLockRemovePar par = (SSLearnEpLockRemovePar) parA.getFromJSON(SSLearnEpLockRemovePar.class);
      
      sSCon.writeRetFullToClient(SSLearnEpLockRemoveRet.get(learnEpLockRemove(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean learnEpLockRemove(final SSLearnEpLockRemovePar par) throws SSErr{
    
    try{
      Boolean unLockResult = false;
      
      if(!learnEpConf.useEpisodeLocking){
        return unLockResult;
      }
      
      if(par.withUserRestriction){
        
        final SSEntity learnEp =
          sql.getEntityTest(
            par.user,
            par.learnEp,
            par.withUserRestriction);
        
        if(learnEp == null){
          return false;
        }
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
        
        if(!SSLearnEpAccessController.hasLock(par.forUser, par.learnEp)){
          return unLockResult;
        }
      }
      
      unLockResult = SSLearnEpAccessController.unLock(par.learnEp);
      
      return unLockResult;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  private List<SSUri> getLearnEpAffiliatedURIs(
    final SSUri           learnEp) throws SSErr{
    
    try{
      
      final List<SSUri>  learnEpContentUris = new ArrayList<>();
      SSLearnEpVersion   learnEpVersion;
      
      learnEpContentUris.add(learnEp);
      
      for(SSUri learnEpVersionUri : sql.getLearnEpVersionURIs(learnEp)){
        
        learnEpContentUris.add(learnEpVersionUri);
        
        learnEpVersion =
          sql.getLearnEpVersion(
            learnEpVersionUri,
            true, //setCircles,
            true, //setEntities,
            true); //setTimeLineState
        
        for(SSEntity circle : learnEpVersion.learnEpCircles){
          learnEpContentUris.add(circle.id);
        }
        
        for(SSEntity learnEpEntity : learnEpVersion.learnEpEntities){
          
          learnEpContentUris.add   (learnEpEntity.id);
          learnEpContentUris.add   (((SSLearnEpEntity) learnEpEntity).entity.id);
          
//          learnEpContentUris.addAll(
//            getLearnEpEntityAttachedEntities(
//              user,
//              ((SSLearnEpEntity) learnEpEntity).entity.id,
//              withUserRestriction));
        }
        
        if(learnEpVersion.learnEpTimelineState != null){
          learnEpContentUris.add(learnEpVersion.learnEpTimelineState.id);
        }
      }
      
      SSStrU.distinctWithoutNull2(learnEpContentUris);
      
      return learnEpContentUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
//  private List<SSUri> getLearnEpEntityAttachedEntities(
//    final SSUri   user,
//    final SSUri   entity,
//    final Boolean withUserRestriction) throws SSErr{
//
//    try{
//      final List<SSEntity> attachedEntities = new ArrayList<>();
//
//      SSEntity.addEntitiesDistinctWithoutNull(
//        attachedEntities,
//        ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).filesGet(
//          new SSEntityFilesGetPar(
//            user,
//            entity,
//            withUserRestriction, //withUserRestcrition
//            false)));   //invokeEntityHandlers
//
//      SSEntity.addEntitiesDistinctWithoutNull(
//        attachedEntities,
//        ((SSImageServerI) SSServReg.getServ(SSImageServerI.class)).imagesGet(
//          new SSImagesGetPar(
//            user,
//            entity, //entity
//            SSImageE.thumb, //imageType
//            withUserRestriction)));
//
//      return SSUri.getDistinctNotNullFromEntities(attachedEntities);
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
}