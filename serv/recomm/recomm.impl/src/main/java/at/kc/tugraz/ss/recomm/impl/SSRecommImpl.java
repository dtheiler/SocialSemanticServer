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
package at.kc.tugraz.ss.recomm.impl;

import at.kc.tugraz.socialserver.utils.SSFileExtE;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.recomm.api.SSRecommClientI;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.datatypes.SSRecommUserRealmEngine;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommResourcesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkEntitiesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdatePar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUsersPar;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommResourcesRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommTagsRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUpdateRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUsersRet;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommFct;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommResourcesFct;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommUpdateBulkUploader;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommUserRealmKeeper;
import at.kc.tugraz.ss.recomm.impl.fct.sql.SSRecommSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import engine.Algorithm;
import engine.EntityType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErrE;

public class SSRecommImpl extends SSServImplWithDBA implements SSRecommClientI, SSRecommServerI{
  
  private final SSRecommConf   recommConf;
  private final SSRecommSQLFct sqlFct;
  
  public SSRecommImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbSQL);
    
    recommConf = ((SSRecommConf)conf);
    sqlFct     = new SSRecommSQLFct(this);
    
    SSRecommUserRealmKeeper.setSssRealmAndEngine(recommConf.fileNameForRec);
  }
  
  @Override
  public void recommUsers(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommUsersRet.get(recommUsers(parA), parA.op));
  }
  
  @Override
  public Map<SSEntity, Double> recommUsers(final SSServPar parA) throws Exception{
    
    try{
      final SSRecommUsersPar       par           = new SSRecommUsersPar(parA);
      final Map<SSEntity, Double>  users         = new HashMap<>();
      Integer                      userCounter   = 0;
      
      SSRecommFct.checkPar(par.user, par.forUser, par.realm);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          par.user, 
          par.realm, 
          false, 
          sqlFct);
      
      //Users for resource (Tags): getEntitiesWithLikelihood(null,    entity, null, 10, false, null /* LD: Algorithm.USERTAGCB */, EntityType.USER); // CBtags
      //Users for user:            getEntitiesWithLikelihood(forUser, null,   null, 10, false, null,                               EntityType.USER); // CF
      //Users MostPopular:         getEntitiesWithLikelihood(null,    null,   null, 10, false, null,                               EntityType.USER); // MP
      
      final Map<String, Double> usersWithLikelihood =
        userRealmEngine.engine.getEntitiesWithLikelihood(
          SSStrU.toStr(par.forUser),
          SSStrU.toStr(par.entity),
          par.categories,
          100,
          null, //filterOwn
          recommConf.recommUserAlgorithm, //algo
          EntityType.USER);  //entity type to recommend

      for(Map.Entry<String, Double> userWithLikelihood : usersWithLikelihood.entrySet()){
        
        if(SSStrU.equals(userRealmEngine.realm, SSRecommUserRealmKeeper.getSssRealm())){
          
          users.put(
            SSRecommFct.handleAccess(par.user, SSUri.get(userWithLikelihood.getKey())),
            userWithLikelihood.getValue());
        }else{
          
          users.put(
            SSEntity.get(
              SSUri.get(userWithLikelihood.getKey()),
              SSEntityE.user),
            userWithLikelihood.getValue());
        }
        
        if((++userCounter) >= par.maxUsers){
          break;
        }
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommTags(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommTagsRet.get(recommTags(parA), parA.op));
  }
  
  @Override
  public Map<String, Double> recommTags(final SSServPar parA) throws Exception{
    
    try{
      final SSRecommTagsPar par  = new SSRecommTagsPar(parA);
      final SSEntity        forUserEntity;
      Algorithm             algo = recommConf.recommTagAlgorithm;
      
      SSRecommFct.checkPar(par.user, par.forUser, par.realm);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          par.user, 
          par.realm,
          false,
          sqlFct);
      
      if(
        recommConf.recommTagsGroups != null &&
        !recommConf.recommTagsGroups.isEmpty()){
       
        forUserEntity = SSServCaller.entityGet(par.forUser);
        
        for(String tagRecommGroup : recommConf.recommTagsGroups){
          
          if(SSStrU.equals(SSStrU.split(tagRecommGroup, SSStrU.colon).get(0), forUserEntity.label)){
            algo = Algorithm.valueOf(SSStrU.split(tagRecommGroup, SSStrU.colon).get(1));
            break;
          }
        }
      }
      
      //Tags for user and resource: getEntitiesWithLikelihood(forUser,  entity,  null, 10, false, null, EntityType.TAG);  // BLLac+MPr
      //Tags for user:              getEntitiesWithLikelihood(forUser,  null,    null, 10, false, null, EntityType.TAG);  // BLL
      //Tags for resource:          getEntitiesWithLikelihood(null,     entity,  null, 10, false, null, EntityType.TAG);  // MPr
      //Tags MostPopular:           getEntitiesWithLikelihood(null,     null,    null, 10, false, null, EntityType.TAG);  // MP
      return userRealmEngine.engine.getEntitiesWithLikelihood(
        SSStrU.toStr(par.forUser), 
        SSStrU.toStr(par.entity),
        par.categories,
        par.maxTags,
        !par.includeOwn, //filterOwn
        algo,
        EntityType.TAG);  //entity type to recommend
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommResources(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommResourcesRet.get(recommResources(parA), parA.op));
  }
  
  @Override
  public Map<SSEntity, Double> recommResources(final SSServPar parA) throws Exception{
    
    try{
      final SSRecommResourcesPar   par           = new SSRecommResourcesPar(parA);
      final Map<SSEntity, Double>  entities      = new HashMap<>();
      Integer                      entityCounter = 0;
      SSEntity                     entity;
      
      SSRecommFct.checkPar(par.user, par.forUser, par.realm);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          par.user, 
          par.realm, 
          false,
          sqlFct);
      
      //Resources for user (Tags): getEntitiesWithLikelihood(forUser, null,   null, 10, false, Algorithm.RESOURCETAGCB /* LD */,   EntityType.RESOURCE); // CBtags
      //Resources for user (CF):   getEntitiesWithLikelihood(forUser, null,   null, 10, false, null /* or Algorithm.RESOURCECF */, EntityType.RESOURCE); // CF
      //Resources for resource:    getEntitiesWithLikelihood(null,    entity, null, 10, false, null,                               EntityType.RESOURCE); // CF
      //Resources MostPopular:     getEntitiesWithLikelihood(null,    null,   null, 10, false, null,                               EntityType.RESOURCE); // MP
      
      final Map<String, Double> entitiesWithLikelihood =
        userRealmEngine.engine.getEntitiesWithLikelihood(
          SSStrU.toStr(par.forUser),
          SSStrU.toStr(par.entity),
          par.categories,
          100,
          !par.includeOwn, //filterOwn
          recommConf.recommResourceAlgorithm, //algo
          EntityType.RESOURCE);  //entity type to recommend

      for(Map.Entry<String, Double> entityWithLikelihood : entitiesWithLikelihood.entrySet()){
        
        entity = 
          SSRecommFct.handleAccess(
            par.user, 
            SSUri.get(entityWithLikelihood.getKey()));
        
        if(
          entity == null ||
          !SSRecommResourcesFct.handleType   (par, entity)){
          continue;
        }
        
        SSRecommResourcesFct.addCircleTypes(par, entity);
       
        entities.put(
          entity,
          entityWithLikelihood.getValue());
        
        if((++entityCounter) >= par.maxResources){
          break;
        }
      }
      
      return entities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommLoadUserRealms (final SSServPar parA) throws Exception{
    
    try{
      SSRecommUserRealmKeeper.setUserRealmEngines(sqlFct.getUserRealms());
      
      for(SSRecommUserRealmEngine engine : SSRecommUserRealmKeeper.getUserRealmEngines()){
        engine.engine.loadFile(engine.realm);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void recommUpdateBulk(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSRecommUpdateBulkPar par = new SSRecommUpdateBulkPar(parA);
    
    SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
      par.user, 
      par.realm, 
      true, 
      sqlFct);
    
    new Thread(new SSRecommUpdateBulkUploader(recommConf, sSCon, par)).start();
  }
  
  @Override
  public void recommUpdateBulk(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRecommUpdateBulkPar par = new SSRecommUpdateBulkPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          par.user, 
          par.realm, 
          true, 
          sqlFct);
        
      dbSQL.commit(par.shouldCommit);
      
      SSServCaller.dataExportUserEntityTagCategoryTimestamps(
        par.user,
        true, 
        recommConf.usePrivateTagsToo, 
        true, 
        userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      userRealmEngine.engine.loadFile(userRealmEngine.realm);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          recommUpdateBulk(parA);
        }else{
          SSServErrReg.regErrThrow(error);
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void recommUpdate(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommUpdateRet.get(recommUpdate(parA), parA.op));
  }
  
  @Override
  public Boolean recommUpdate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRecommUpdatePar par = new SSRecommUpdatePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          par.user, 
          par.realm, 
          true,
          sqlFct);
      
      dbSQL.commit(par.shouldCommit);
      
      SSServCaller.dataExportAddTagsCategoriesTimestampsForUserEntity(
        par.user,
        par.forUser,
        par.entity,
        par.tags,
        par.categories,
        userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      userRealmEngine.engine.loadFile(userRealmEngine.realm);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return recommUpdate(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
    @Override
  public void recommUpdateBulkEntities(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommUpdateRet.get(recommUpdateBulkEntities(parA), parA.op));
  }
  
  @Override
  public Boolean recommUpdateBulkEntities(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRecommUpdateBulkEntitiesPar par = new SSRecommUpdateBulkEntitiesPar(parA);

      List<String> tags;
      List<String> categories;
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          par.user, 
          par.realm, 
          true,
          sqlFct);
      
      dbSQL.commit(par.shouldCommit);
      
      if(
        !par.tags.isEmpty() &&
        par.tags.size() != par.entities.size()){
        
        throw new Exception("tag list size differs from entity list size");
      }
      
      if(
        !par.categories.isEmpty() &&
        par.categories.size() != par.entities.size()){
        
        throw new Exception("category list size differs from entity list size");
      }
      
      for(int counter = 0; counter < par.entities.size(); counter++){

        if(!par.tags.isEmpty()){
          tags = par.tags.get(counter);
        }else{
          tags = new ArrayList<>();
        }
        
        if(!par.categories.isEmpty()){
          categories = par.categories.get(counter);
        }else{
          categories = new ArrayList<>();
        }
        
        SSServCaller.dataExportAddTagsCategoriesTimestampsForUserEntity(
          par.user,
          par.forUser,
          par.entities.get(counter),
          tags,
          categories,
          userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      }
      
      userRealmEngine.engine.loadFile(userRealmEngine.realm);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return recommUpdateBulkEntities(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}