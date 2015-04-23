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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.service.user.api.*;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAddPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAllPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserExistsPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.kc.tugraz.ss.service.user.datatypes.ret.SSUserAllRet;
import at.kc.tugraz.ss.service.user.impl.functions.sql.SSUserSQLFct;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;

public class SSUserImpl extends SSServImplWithDBA implements SSUserClientI, SSUserServerI, SSEntityHandlerImplI, SSEntityDescriberI{
  
//  private final SSUserGraphFct       graphFct;
  private final SSUserSQLFct         sqlFct;
  
  public SSUserImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbSQL);
    
//    graphFct = new SSUserGraphFct (this);
    sqlFct   = new SSUserSQLFct   (this);
  }
  
  @Override
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{
    
    try{
      switch(par.entity.type){
        
        case user:{
          
          final SSUser user = sqlFct.getUser(par.entity.id);

          user.friends.addAll(
            SSServCaller.friendsUserGet(
              par.entity.id));
          
          if(par.setCircles){
            
            user.circles.addAll(
              SSServCaller.circlesGet(
                par.user,
                par.user,
                null,
                SSEntityE.asListWithoutNullAndEmpty(),
                false,
                true,
                false));
          }
          
          par.entity = 
            SSUser.get(
              user,
              par.entity);
          
          break;
        }
      }
      
      return par.entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean copyUserEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{

    return null;
  }
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri, 
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception{
  }
  
  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public void shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
  }
  
  @Override
  public void shareUserEntityWithCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE entityType) throws Exception{
  }  
  
  @Override
  public Boolean userExists(final SSServPar parA) throws Exception{
    
    try{
      
      final SSUserExistsPar par = new SSUserExistsPar (parA);
      
      return sqlFct.existsUser(par.email);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri userURIGet(final SSServPar parA) throws Exception{
    
    try{
      final SSUserURIGetPar par = new SSUserURIGetPar (parA);
      
      return sqlFct.getUserURIForEmail(par.email);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
    @Override
  public void userAll(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
//      if(SSAuthEnum.isSame(SSAuthServ.inst().getAuthType(), SSAuthEnum.wikiAuth)){
//        returnObj.object =  new SSAuthWikiDbCon(new SSAuthWikiConf()).getUserList(); //TODO remove new SSAuthWikiConf() --> take it from config
//      }else{
        
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSUserAllRet.get(userAll(parA), parA.op), parA.op);
//      }
  }
  
  @Override
  public List<SSUser> userAll(final SSServPar parA) throws Exception {
    
    try{
      
      final SSUserAllPar par = SSUserAllPar.get(parA);
      
      return SSServCaller.usersGet(
        par.user, 
        SSUri.asListWithoutNullAndEmpty(), 
        par.setFriends);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override 
  public List<SSUser> usersGet(final SSServPar parA) throws Exception{
    
    try{
      final SSUsersGetPar par   = new SSUsersGetPar(parA);
      final List<SSUser>  users = sqlFct.getUsers(par.users);
      
      for(SSUser user : users){
        
        if(par.setFriends){
          user.friends.addAll(SSServCaller.friendsUserGet(user.id));
          user.friend = SSStrU.contains(user.friends, par.user);
        }
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri userAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSUserAddPar  par      = new SSUserAddPar(parA);
      final SSUri         userUri;
      final SSLabel       tmpLabel;
      final String        tmpEmail;
      
      if(par.isSystemUser){
        userUri  = SSVoc.systemUserUri;
        tmpLabel = SSLabel.get(SSVocConf.systemUserLabel);
        tmpEmail = SSVocConf.systemUserEmail; 
      }else{
        
        userUri  = SSServCaller.vocURICreate();
        tmpLabel = par.label;
        tmpEmail = par.email;        
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        SSVoc.systemUserUri,
        userUri,
        SSEntityE.user,
        tmpLabel,
        null,
        null,
        false);
      
      SSServCaller.circleEntitiesAdd(
        SSVoc.systemUserUri, 
        SSServCaller.circlePubURIGet(false), 
        SSUri.asListWithoutNullAndEmpty(userUri), 
        true, 
        false, 
        false);
      
      sqlFct.addUser(userUri, tmpEmail);
      
      dbSQL.commit(par.shouldCommit);
      
      return userUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return userAdd(parA);
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

//@Override
//  public String userNameFromUri(SSServPar parI) throws Exception {
//    
//    SSUserNameFromUriPar par = new SSUserNameFromUriPar (parI);
//    
//    String userUri;
//    
//    if(SSObjU.isNull(par.user)){
//      return null;
//    }
//    
//    userUri = SSStrU.removeTrailingSlash(SSStrU.toStr(par.user));
//    
//    return userUri.substring(userUri.lastIndexOf(SSStrU.slash) + 1, userUri.length());
//  }