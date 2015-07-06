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

import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.kc.tugraz.ss.circle.api.SSCircleClientI;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.impl.fct.activity.SSCircleActivityFct;
import at.kc.tugraz.ss.circle.impl.fct.misc.SSCircleMiscFct;
import at.kc.tugraz.ss.circle.impl.fct.sql.SSCircleSQLFct;
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
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCirclesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCanAccessPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityUsersGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityUsersGetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityShareRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSTextComment;

public class SSCircleImpl 
extends SSServImplWithDBA 
implements 
  SSCircleClientI, 
  SSCircleServerI, 
  SSEntityDescriberI{
  
  private final SSCircleSQLFct sqlFct;

  public SSCircleImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSCircleSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      switch(entity.type){
        case circle:{
          
          final SSEntityCircle circle =
            circleGet(
              new SSCircleGetPar(
                null,
                null,
                par.user,
                entity.id,
                par.forUser,
                SSEntityE.asListWithoutNullAndEmpty(),
                false,
                false,
                false));
          
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
  public void circleEntitiesRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
     final SSCircleEntitiesRemovePar par = (SSCircleEntitiesRemovePar) parA.getFromJSON(SSCircleEntitiesRemovePar.class);
     
    sSCon.writeRetFullToClient(SSCircleEntitiesRemoveRet.get(circleEntitiesRemove(par)));
  }
  
  @Override
  public List<SSUri> circleEntitiesRemove(final SSCircleEntitiesRemovePar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle(sqlFct, par.user, par.circle);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entity : par.entities){
        sqlFct.removeEntity(par.circle, entity);
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
  public void circleCreate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleCreatePar par = (SSCircleCreatePar) parA.getFromJSON(SSCircleCreatePar.class);
    
    final SSUri result = circleCreate(par);
    
    sSCon.writeRetFullToClient(SSCircleCreateRet.get(result));
    
    SSCircleActivityFct.createCircle(par, result);
  }
  
  @Override
  public SSUri circleCreate(final SSCircleCreatePar par) throws Exception{
    
    try{
      
      final SSUri circleUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null, 
          null, 
          par.user, 
          circleUri,
          null, //uriAlternative
          SSEntityE.circle, 
          par.label, 
          par.description, 
          SSTextComment.asListWithoutNullAndEmpty(), //comments, 
          null, //downloads, 
          null, //screenShots, 
          null, //images, 
          null, //videos, 
          null, //entitiesToAttach, 
          null, //creationTime, 
          null, //read,
          false, //setPublic 
          false, //withUserRestriction, 
          false)); //shouldCommit))
      
      sqlFct.addCircle(
        circleUri, 
        SSCircleE.group, 
        par.isSystemCircle);
      
      sqlFct.addUserToCircleIfNotExists(
        circleUri, 
        par.user);
      
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
  public void circleUsersAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCircleUsersAddPar par = (SSCircleUsersAddPar) parA.getFromJSON(SSCircleUsersAddPar.class);
      
    final SSUri result = circleUsersAdd(par);
    
    sSCon.writeRetFullToClient(SSCircleUsersAddRet.get(result));
    
    SSCircleActivityFct.addUsersToCircle(par);
  }
  
  private static void checkWhetherUsersAreUsers(final List<SSUri> users) throws Exception{
    
    try{
      
      final List<SSEntity> entities =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
            null,
            users,    //entities
            null,     //forUser
            SSEntityE.asListWithoutNullAndEmpty(), //types
            false,   //invokeEntityHandlers
            null,    //descPar
            false,  //withUserRestriction
            true)); //logErr
      
      for(SSEntity entity : entities){
        
        switch(entity.type){
          
          case user: continue;
          default:   throw new SSErr(SSErrE.providedUserIsNotRegistered);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSUri circleUsersAdd(final SSCircleUsersAddPar par) throws Exception{
    
    try{

      if(par.withUserRestriction){
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle(sqlFct, par.user, par.circle);
      }
      
      checkWhetherUsersAreUsers(par.users);
        
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.users){
        sqlFct.addUserToCircleIfNotExists(par.circle, userUri);
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
  public void circleEntitiesAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCircleEntitiesAddPar par = (SSCircleEntitiesAddPar) parA.getFromJSON(SSCircleEntitiesAddPar.class);
    
    sSCon.writeRetFullToClient(SSCircleEntitiesAddRet.get(circleEntitiesAdd(par)));
    
    SSCircleActivityFct.addEntitiesToCircle(par);
  }
  
  @Override
  public SSUri circleEntitiesAdd(final SSCircleEntitiesAddPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle(
          sqlFct,   
          par.user, 
          par.circle);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entity : par.entities){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null, 
            null, 
            par.user, 
            entity, 
            null, //uriAlternative, 
            null, //type, 
            null, //label, 
            null, //description, 
            null, //comments, 
            null, //downloads, 
            null, //screenShots, 
            null, //images, 
            null, //videos, 
            null, //entitiesToAttach,
            null, //creationTime, 
            null, //read, 
            false, //setPublic
            par.withUserRestriction, //withUserRestriction 
            false)); //shouldCommit
        
        sqlFct.addEntityToCircleIfNotExists(
          par.circle, 
          entity);
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
  public SSCircleE circleMostOpenCircleTypeGet(final SSCircleMostOpenCircleTypeGetPar par) throws Exception{
    
    try{
      
      SSCircleE                              mostOpenCircleType = SSCircleE.priv;
      
      for(SSCircleE circleType :
        circleTypesGet(
          new SSCircleTypesGetPar(
            null,
            null,
            par.user,
            par.forUser,
            par.entity,
            par.withUserRestriction))){
        
        switch(circleType){
          
          case pub:  return SSCircleE.pub;
          case priv: continue;
          default:   mostOpenCircleType = SSCircleE.group;
        }
      }
      
      return mostOpenCircleType;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSCircleE> circleTypesGet(final SSCircleTypesGetPar par) throws Exception{
    
    try{
      
      if(par.entity == null){
        throw new Exception("entity to retrieve circle types for is null");
      }
      
      if(
        par.withUserRestriction &&
        par.forUser != null &&
        !SSStrU.equals(par.forUser, par.user)){
        throw new Exception("user cannot retrieve circle types for other user");
      }
      
      if(par.forUser == null){
        return sqlFct.getCircleTypesForEntity(par.entity);
      }else{
        return sqlFct.getCircleTypesCommonForUserAndEntity(par.forUser, par.entity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleGetPar par = (SSCircleGetPar) parA.getFromJSON(SSCircleGetPar.class);
    
    sSCon.writeRetFullToClient(SSCircleGetRet.get(circleGet(par)));
  }
  
  @Override
  public SSEntityCircle circleGet(final SSCircleGetPar par) throws Exception{
    
    try{
      final List<SSEntity>  entities = new ArrayList<>();
      final List<SSEntity>  users    = new ArrayList<>();
      final SSEntityCircle  circle;
      
      if(par.withUserRestriction){
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
        
        if(
          par.withSystemCircles ||
          sqlFct.isSystemCircle(par.circle)){
          
          throw new Exception("user cannot access system circle");
        }
        
        SSServCallerU.canUserReadEntity(par.forUser, par.circle);
      }
      
      circle = 
        sqlFct.getCircle(
          par.circle, 
          true, 
          true, 
          true, 
          par.entityTypesToIncludeOnly);
      
      if(par.invokeEntityHandlers){
          
        entities.addAll(
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
            new SSEntitiesGetPar(
              null,
              null,
              par.user,
              SSUri.getFromEntitites(circle.entities),
              null, //forUser,
              SSEntityE.asListWithoutNullAndEmpty(), //types,
              true, //invokeEntityHandlers,
              new SSEntityDescriberPar(
                false, //setTags
                true, //setOverallRating,
                false, //setDiscs,
                false, //setUEs,
                false, //setThumb,
                false, //setFlags,
                false), //setCircles //descPar,
              false, //withUserRestriction
              false))); //logErr
        
        circle.entities.clear();
        circle.entities.addAll(entities);
        
        users.addAll(
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
            new SSEntitiesGetPar(
              null,
              null,
              par.user,
              SSUri.getFromEntitites(circle.users),
              null, //forUser,
              SSEntityE.asListWithoutNullAndEmpty(), //types,
              false, //invokeEntityHandlers,
              null, //descPar
              false, //withUserRestriction
              false))); //logErr
        
        circle.users.clear();
        circle.users.addAll(users);
      }
      
      return circle;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circlesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

   SSServCallerU.checkKey(parA);

   final SSCirclesGetPar par = (SSCirclesGetPar) parA.getFromJSON(SSCirclesGetPar.class);
   
   sSCon.writeRetFullToClient(SSCirclesGetRet.get(circlesGet(par)));
  }
  
  @Override
  public List<SSEntityCircle> circlesGet(final SSCirclesGetPar par) throws Exception{
    
    try{
      
      final List<SSEntityCircle>            circles           = new ArrayList<>();
      final List<SSUri>                     circleUris        = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
        
        if(par.withSystemCircles){
          throw new Exception("user cannot access system circles");
        }
        
        if(par.entity != null){
          SSServCallerU.canUserReadEntity(par.forUser, par.entity);
        }
      }
      
      if(!SSObjU.isNull(par.forUser, par.entity)){
        
        for(SSEntityCircle circle : 
          sqlFct.getCirclesCommonForUserAndEntity(
            par.forUser,
            par.entity,
            par.withSystemCircles)){
          
          circleUris.add(circle.id);
        }
        
      }else{
        
        if(
          par.forUser == null &&
          par.entity  == null){
          
          circleUris.addAll(sqlFct.getCircleURIs(par.withSystemCircles));
        }else{
          
          if(par.forUser != null){
            circleUris.addAll(sqlFct.getCircleURIsForUser(par.forUser, par.withSystemCircles));
          }
          
          if(par.entity != null){
            circleUris.addAll(sqlFct.getCircleURIsForEntity(par.entity, par.withSystemCircles));
          }
        }
      }
      
      if(par.withUserRestriction){
        
        for(SSUri circleUri : circleUris){
          
          try{
            SSServCallerU.canUserReadEntity(par.forUser, circleUri);
          }catch(Exception error){
            SSServErrReg.reset();
            continue;
          }
          
          circles.add(
            circleGet(
              new SSCircleGetPar(
                null, 
                null,
                par.forUser,
                circleUri, 
                null,
                par.entityTypesToIncludeOnly,
                true, 
                par.withSystemCircles,
                par.invokeEntityHandlers)));
        }
      }else{
        
        for(SSUri circleUri : circleUris){
          
          circles.add(
            circleGet(
              new SSCircleGetPar(
                null,
                null,
                par.user,
                circleUri,
                null,
                par.entityTypesToIncludeOnly,
                false,
                par.withSystemCircles,
                par.invokeEntityHandlers)));
        }
      }

      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circlePrivURIGet(final SSCirclePrivURIGetPar par) throws Exception{
    
    try{
      
      final SSUri  privCircleUri;
      
      dbSQL.startTrans(par.shouldCommit);
      
      privCircleUri = SSCircleMiscFct.addOrGetPrivCircleURI(sqlFct, par.user);
      
      dbSQL.commit(par.shouldCommit);
      
      return privCircleUri;
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
  public SSUri circlePubURIGet(final SSCirclePubURIGetPar par) throws Exception{
    
    try{
      
      final SSUri circleUri;
      
      dbSQL.startTrans(par.shouldCommit);
      
      circleUri = SSCircleMiscFct.addOrGetPubCircleURI(sqlFct);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
      
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
  public SSEntity circleCanAccess(final SSCircleCanAccessPar par) throws Exception{
   
    try{

      if(
        par.entity    != null &&
        par.entityURI != null){
        throw new Exception("either set entity or entityURI");
      }
      
      if(
        par.entity    == null &&
        par.entityURI == null){
        throw new Exception("entity nor entityURI set");
      }
      
      if(par.entityURI != null){
      
        par.entity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              null,
              par.entityURI,
              null,  //forUser
              null, //label
              null, //type
              false, //withUserRestriction
              false, //invokeEntityHandlers
              null, //descPar
              true)); //logErr
      
        if(par.entity == null){
          return null;
        }
      }
      
      SSCircleMiscFct.checkWhetherUserCanForEntityType(
        sqlFct,
        par.user,
        par.entity,
        par.accessRight,
        par.logErr);
      
      return par.entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, par.logErr);
      return null;
    }
  }
  
  @Override
  public void circleEntityUsersGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleEntityUsersGetPar par = (SSCircleEntityUsersGetPar) parA.getFromJSON(SSCircleEntityUsersGetPar.class);
    
    sSCon.writeRetFullToClient(SSCircleEntityUsersGetRet.get(circleEntityUsersGet(par)));
  }
  
  @Override
  public List<SSEntity> circleEntityUsersGet(final SSCircleEntityUsersGetPar par) throws Exception{
    
//TODO to be integrated with withUserRestriction
    try{
      final List<SSUri>                   userUris        = new ArrayList<>();
      final List<SSUri>                   userCircleUris  = sqlFct.getCircleURIsForUser   (par.user, true);
      
      for(SSUri circleUri : sqlFct.getCircleURIsForEntity(par.entity, true)){
        
        switch(sqlFct.getTypeForCircle(circleUri)){
          
          case priv:{
            
            if(!SSStrU.contains(userCircleUris, circleUri)){
              continue;
            }
            
            if(!SSStrU.contains(userUris, par.user)){
              userUris.add(par.user);
            }
            
            break;
          }
          
          case pub:{
            
            for(SSEntity user : sqlFct.getUsersForCircle(circleUri)){
              
              if(!SSStrU.contains(userUris, user.id)){
                userUris.add(user.id);
              }
            }
            
            break;
          }
          
          case group:{
            
            if(!SSStrU.contains(userCircleUris, circleUri)){
              continue;
            }
            
            for(SSEntity user : sqlFct.getUsersForCircle(circleUri)){
              
              if(!SSStrU.contains(userUris, user.id)){
                userUris.add(user.id);
              }
            }
            
            break;
          }
        }
      }
      
      return ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
        new SSEntitiesGetPar(
          null,
          null,
          null,
          userUris, //entities
          null,     //forUser
          SSEntityE.asListWithoutNullAndEmpty(), //types
          false,   //invokeEntityHandlers
          null, //descPar
          false,  //withUserRestriction
          true));  //logErr
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleEntityShare(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCircleEntityShareRet.get(circleEntityShare(parA), parA.op));
  }
  
  @Override  
  public SSUri circleEntityShare(final SSServPar parA) throws Exception{
    
    try{
      final SSCircleEntitySharePar par      = new SSCircleEntitySharePar(parA);
      final List<SSEntity>         entities = new ArrayList<>();
      
      if(
        par.users.isEmpty() &&
        par.circles.isEmpty()){
        return par.entity;
      }
      
      if(par.withUserRestriction){
        
        if(SSObjU.isNull(par.user)){
          throw new Exception("pars null");
        }
        
        SSServCallerU.canUserEditEntity(par.user, par.entity);
      }
      
      if(SSStrU.contains(par.users, par.user)){
        throw new Exception("user cannot share with himself");
      }
      
      entities.add(
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            par.entity,
            null, //forUser,
            null, //label,
            null, //type,
            false, //withUserRestriction,
            false, //invokeEntityHandlers,
            null, //descPar,
            true))); //logErr));
      
      if(!par.users.isEmpty()){
        
        checkWhetherUsersAreUsers  (par.users);
        
        dbSQL.startTrans(par.shouldCommit);
        
        final SSUri           circleUri =
          circleCreate(
            new SSCircleCreatePar(
              null,
              null,
              par.user, //user
              null, //label
              null, //description
              true,  //isSystemCircle
              false, //withUserRestriction
              false)); //shouldCommit
        
        circleUsersAdd(
          new SSCircleUsersAddPar(
            null,
            null,
            par.user,
            circleUri, //circle
            par.users, //users
            false, //withUserRestriction
            false)); //shouldCommit
        
        circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null,
            null,
            par.user,
            circleUri, //circle
            SSUri.asListWithoutNullAndEmpty(par.entity),  //entities
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit
        
        for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
          
          ((SSEntityHandlerImplI) serv.serv()).circleContentChanged(
            par.user,
            circleUri,
            false, //isPublicCircle
            SSUri.asListWithoutNullAndEmpty(),  //usersToAdd
            entities, //entitiesToAdd,
            par.users,  //usersToPushEntitiesTo
            SSUri.asListWithoutNullAndEmpty(), //circleUsers
            new ArrayList<>()); //circleEntities
        }
        
        SSCircleActivityFct.shareEntityWithUsers(par, circleUri);
      }
        
      if(!par.circles.isEmpty()){

        dbSQL.startTrans(par.shouldCommit);
        
        for(SSUri circleURI : par.circles){
          
          circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              null, 
              null, 
              par.user,
              circleURI,  //circle
              SSUri.asListWithoutNullAndEmpty(par.entity),  //entities
              true, //withUserRestriction
              false)); //shouldCommit
          
          final SSEntityCircle circle;
          
          circle =
            circleGet(
              new SSCircleGetPar(
                null,
                null,
                par.user,
                circleURI, //circle
                null, //forUser
                SSEntityE.asListWithoutNullAndEmpty(), //entityTypesToIncludeOnly
                false, //withUserRestriction
                true, //withSystemCircles
                false)); //invokeEntityHandlers
          
          switch(circle.circleType){
            case pub:{
              if(par.withUserRestriction){
                throw new SSErr(SSErrE.cannotShareWithPublicCircle);
              }
              break;
            }
          }
          
          for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
            
            ((SSEntityHandlerImplI) serv.serv()).circleContentChanged(
              par.user,
              circleURI,
              false,    //isPublicCircle
              SSUri.asListWithoutNullAndEmpty(),         //usersToAdd
              entities, //entitiesToAdd,
              SSUri.asListWithoutNullAndEmpty(),  //usersToPushEntitiesTo
              SSUri.getFromEntitites(circle.users), //circleUsers
              new ArrayList<>()); //circleEntities
          }
        }
        
        SSCircleActivityFct.shareEntityWithCircles(par);
      }
     
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleEntityShare(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}