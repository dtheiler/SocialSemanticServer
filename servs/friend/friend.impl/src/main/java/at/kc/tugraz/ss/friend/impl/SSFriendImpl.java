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
package at.kc.tugraz.ss.friend.impl;

import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.friend.api.SSFriendClientI;
import at.kc.tugraz.ss.friend.api.SSFriendServerI;
import at.kc.tugraz.ss.friend.datatypes.SSFriend;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendAddPar;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendGetPar;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendsGetPar;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendAddRet;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendsGetRet;
import at.kc.tugraz.ss.friend.impl.fct.sql.SSFriendSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;

public class SSFriendImpl
extends
  SSServImplWithDBA
implements
  SSFriendClientI,
  SSFriendServerI,
  SSDescribeEntityI{
  
  private final SSFriendSQLFct  sqlFct;
  private final SSEntityServerI entityServ;
  
  public SSFriendImpl(final SSConfA conf) throws SSErr{
    super(conf, (SSDBSQLI) SSDBSQL.inst.getServImpl(), (SSDBNoSQLI) SSDBNoSQL.inst.getServImpl());
    
    this.sqlFct     = new SSFriendSQLFct(dbSQL);
    this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(!par.setFriends){
        return entity;
      }
      
      switch(entity.type){
        
        case user: {
          
          final SSUser userEntity = SSUser.get(entity);
          
          userEntity.friends.addAll(
            friendsGet(
              new SSFriendsGetPar(
                par.user)));
          
          return userEntity;
        }
        
        default: return entity;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSFriend friendGet(final SSFriendGetPar par) throws SSErr{
    
    try{
      
      if(par.friend == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      return SSFriend.get(
        sqlFct.getFriend(par.friend),
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user, 
            par.friend, 
            par.withUserRestriction, //withUserRestriction, 
            null))); //descPar));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI friendsGet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{

      SSServCallerU.checkKey(parA);
    
    final SSFriendsGetPar par = (SSFriendsGetPar) parA.getFromJSON(SSFriendsGetPar.class);
    
    return SSFriendsGetRet.get(friendsGet(par));
    
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> friendsGet(final SSFriendsGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> result = new ArrayList<>();
      
      for(SSUri friend : sqlFct.getFriends(par.user)){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          result,
          friendGet(
            new SSFriendGetPar(
              par.user, 
              friend)));
      }
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI friendAdd(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSFriendAddPar par = (SSFriendAddPar) parA.getFromJSON(SSFriendAddPar.class);
      
      return SSFriendAddRet.get(friendAdd(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri friendAdd(final SSFriendAddPar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
            
      sqlFct.addFriend(par.user, par.friend);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.friend;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return friendAdd(par);
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