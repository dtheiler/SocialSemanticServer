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
package at.kc.tugraz.ss.service.user.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.user.api.*;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAddPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserEntityUsersGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserExistsPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIsGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.kc.tugraz.ss.service.user.datatypes.ret.SSUserEntityUsersGetRet;
import at.kc.tugraz.ss.service.user.datatypes.ret.SSUsersGetRet;
import at.kc.tugraz.ss.service.user.impl.functions.sql.SSUserSQLFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;

public class SSUserImpl 
extends SSServImplWithDBA 
implements 
  SSUserClientI, 
  SSUserServerI, 
  SSDescribeEntityI{
  
  private final SSUserSQLFct      sql;
  private final SSEntityServerI   entityServ;
  private final SSCircleServerI   circleServ;
  
  public SSUserImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sql          = new SSUserSQLFct(dbSQL, SSVocConf.systemUserUri);
    this.entityServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.circleServ   = (SSCircleServerI)   SSServReg.getServ(SSCircleServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(!SSStrU.equals(entity.author, SSVocConf.systemUserUri)){
        
        if(entity.author != null){
          
          final List<SSEntity> authors =
            usersGet(
              new SSUsersGetPar(
                par.user,
                SSUri.asListNotNull(entity.author.id),
                false)); //invokeEntityHandlers))
          
          if(!authors.isEmpty()){
            entity.author = authors.get(0);
          }
        }
      }
      
      switch(entity.type){
        
        case user:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          final List<SSEntity> users =
            usersGet(
              new SSUsersGetPar(
                par.user,
                SSUri.asListNotNull(entity.id),
                false)); //invokeEntityHandlers))
          
          if(!users.isEmpty()){
            
            return SSUser.get(
              (SSUser) users.get(0),
              entity);
          }else{
            return entity;
          }
        }
        
        default: return entity;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean userExists(final SSUserExistsPar par) throws Exception{
    
    try{
      return sql.existsUser(par.email);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri userURIGet(final SSUserURIGetPar par) throws Exception{
    
    try{
      return sql.getUserURIForEmail(par.email);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  
  @Override
  public List<SSUri> userURIsGet(final SSUserURIsGetPar par) throws Exception{
    
    try{
      
      final List<SSUri> uris = new ArrayList<>();
      
      for(String email : par.emails){
        uris.add(sql.getUserURIForEmail(email));
      }

      return uris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void usersGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
//      if(SSAuthEnum.isSame(SSAuthServ.inst().getAuthType(), SSAuthEnum.wikiAuth)){
//        returnObj.object =  new SSAuthWikiDbCon(new SSAuthWikiConf()).getUserList(); //TODO remove new SSAuthWikiConf() --> take it from config
//      }else{
        
    SSServCallerU.checkKey(parA);

    final SSUsersGetPar par = (SSUsersGetPar) parA.getFromJSON(SSUsersGetPar.class);
    
    sSCon.writeRetFullToClient(new SSUsersGetRet(usersGet(par)));
//      }
  }
  
  @Override 
  public List<SSEntity> usersGet(final SSUsersGetPar par) throws Exception{
    
    try{
      
      final List<SSUri>    userURIs   = sql.getUserURIs(par.users);
      final List<SSEntity> users      = new ArrayList<>();
      SSUser               userToGet;
      SSEntity             userEntityToGet;
      SSEntityDescriberPar descPar; 
      
      for(SSUri userURI : userURIs){
        
        userToGet = sql.getUser(userURI);
        
        if(userToGet == null){
          continue;
        }
      
        if(par.invokeEntityHandlers){
          descPar = new SSEntityDescriberPar(userToGet.id);
          
          descPar.setProfilePicture = par.setProfilePicture;
          descPar.setFriends        = par.setFriends;
          descPar.setThumb          = par.setThumb;
          descPar.setMessages       = par.setMessages;
          descPar.setActivities     = par.setActivities;
          descPar.setCircles        = par.setCircles;
          descPar.setDiscs          = par.setDiscs;
          descPar.setColls          = par.setColls;
          descPar.setTags           = par.setTags;
          
        }else{
          descPar = null;
        }

        userEntityToGet =
          entityServ.entityGet(
            new SSEntityGetPar(
              par.user,
              userToGet.id,
              par.withUserRestriction,
              descPar));
      
        if(userEntityToGet == null){
          continue;
        }
      
        userToGet =
          SSUser.get(
            userToGet,
            userEntityToGet);
        
        if(
          par.invokeEntityHandlers &&
          par.setFriends){
          
          userToGet.friend = SSStrU.contains(userToGet.friends, par.user);
        }
        
        users.add(userToGet);
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri userAdd(final SSUserAddPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        throw new SSErr(SSErrE.userCannotAddUser);
      }
      
      final SSLabel       tmpLabel;
      final String        tmpEmail;
      SSUri               publicCircleURI;
      SSUri               userUri;
      
      if(par.isSystemUser){
        userUri  = SSVocConf.systemUserUri;
        tmpLabel = SSLabel.get(SSVocConf.systemUserLabel);
        tmpEmail = SSVocConf.systemUserEmail; 
      }else{
        userUri  = SSServCaller.vocURICreate();
        tmpLabel = par.label;
        tmpEmail = par.email;        
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      userUri =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            SSVocConf.systemUserUri,
            userUri,
            SSEntityE.user, //type,
            tmpLabel, //label
            null,//description,
            null, //creationTime,
            null, //read,
            true, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(userUri == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      publicCircleURI = 
        circleServ.circlePubURIGet(
          new SSCirclePubURIGetPar(
            par.user,
            false));
      
      publicCircleURI =
        circleServ.circleUsersAdd(
          new SSCircleUsersAddPar(
            SSVocConf.systemUserUri,
            publicCircleURI, //circle
SSUri.asListNotNull(userUri), //users
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit
      
      if(publicCircleURI == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.addUser(userUri, tmpEmail);
      
      dbSQL.commit(par.shouldCommit);
      
      return userUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return userAdd(par);
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
  public void userEntityUsersGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSUserEntityUsersGetPar par = (SSUserEntityUsersGetPar) parA.getFromJSON(SSUserEntityUsersGetPar.class);
    
    sSCon.writeRetFullToClient(SSUserEntityUsersGetRet.get(userEntityUsersGet(par)));
  }
  
  @Override
  public List<SSEntity> userEntityUsersGet(final SSUserEntityUsersGetPar par) throws Exception{
    
    try{
      
      final List<SSEntity> users = new ArrayList<>();
      
      if(!sql.existsEntity(par.entity)){
        return users;
      }
      
      SSEntity entity;
        
      if(par.withUserRestriction){
        
        entity = 
          sql.getEntityTest(
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entity == null){
          return users;
        }
      }

      for(SSEntity user : 
        usersGet(
          new SSUsersGetPar(
            par.user,
            null, //users
            false))){ //invokeEntityHandlers
       
        entity = 
          sql.getEntityTest(
            user.id, 
            par.entity, 
            par.withUserRestriction);
        
        if(entity == null){
          continue;
        }
        
        users.add(user);
      }

      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
  
//  private void setUserThumb(
//    final SSUri          callingUser,
//    final SSUser         user,
//    final SSImageServerI imageServ, 
//    final Boolean        withUserRestriction) throws Exception{
//    
//    try{
//      
//      for(SSEntity thumb :
//        imageServ.imagesGet(
//          new SSImagesGetPar(
//            callingUser,
//            user.id,
//            SSImageE.thumb,
//            withUserRestriction))){
//        
//        user.thumb = thumb;
//        break;
//      }
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
