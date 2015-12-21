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
package at.kc.tugraz.ss.service.rating.impl;

import at.tugraz.sss.serv.impl.api.SSEntityServerI;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.service.rating.impl.fct.userraltionsgathering.SSRatingUserRelationGathererFct;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.enums.*;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingOverallGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingSetPar;
import at.kc.tugraz.ss.service.rating.api.*;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.conf.SSConfA;
import at.tugraz.sss.serv.impl.api.SSUserRelationGathererI;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import at.kc.tugraz.ss.service.rating.datatypes.SSRating;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingEntityURIsGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingOverallGetRet;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingGetRet;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingSetRet;
import at.kc.tugraz.ss.service.rating.impl.fct.sql.SSRatingSQLFct;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingsRemovePar;
import at.kc.tugraz.ss.service.rating.impl.fct.activity.SSRatingActivityFct;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.impl.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.impl.api.SSUsersResourcesGathererI;
import java.util.ArrayList;

public class SSRatingImpl
extends SSServImplWithDBA
implements
  SSRatingClientI,
  SSRatingServerI,
  SSDescribeEntityI,
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{
  
  private final SSRatingSQLFct   sql;
  private final SSEntityServerI  entityServ;
  private final SSUserCommons  userCommons;
  
  public SSRatingImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql        = new SSRatingSQLFct   (dbSQL, SSVocConf.systemUserUri);
    this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.userCommons = new SSUserCommons();
  }
  
  @Override
  public void getUsersResources(
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        for(SSUri entity : sql.getEntitiesRatedByUser(userID)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              entity,
              SSEntityE.rating,
              null,
              null));
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
      SSServErrReg.reset();
    }
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      
      final Map<String, List<SSUri>> usersPerEntity = new HashMap<>();
      
      for(String user : allUsers){
        
        for(SSEntity rating :
          sql.getRatingAsss(
            SSUri.asListNotNull(SSUri.get(user)),
            null)){
          
          SSRatingUserRelationGathererFct.addUserForEntity((SSRating) rating, usersPerEntity);
        }
      }
      
      SSRatingUserRelationGathererFct.addUserRelations(userRelations, usersPerEntity);
      
      for(Map.Entry<String, List<SSUri>> usersPerUser : userRelations.entrySet()){
        SSStrU.distinctWithoutNull2(usersPerUser.getValue());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setOverallRating){
        
        entity.overallRating =
          ratingOverallGet(
            new SSRatingOverallGetPar(
              par.user,
              entity.id,
              par.withUserRestriction));
      }
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI ratingSet(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSRatingSetPar par = (SSRatingSetPar) parA.getFromJSON(SSRatingSetPar.class);
      final SSRatingSetRet ret = SSRatingSetRet.get(ratingSet(par));
      
      SSRatingActivityFct.rateEntity(par);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean ratingSet(final SSRatingSetPar par) throws SSErr {
    
    try{
      
      final Boolean userRatedEntityBefore = sql.hasUserRatedEntity(par.user, par.entity);
      
      if(
        !par.allowToRateAgain &&
        userRatedEntityBefore){
        return false;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      par.entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity, //entity,
            null, //type,
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
      
      if(par.entity == null){
        dbSQL.rollBack(par.shouldCommit);
        return false;
      }
      
      SSUri ratingUri = null;
      
      if(!userRatedEntityBefore){
        
        ratingUri =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par.user,
              SSVocConf.vocURICreate(),
              SSEntityE.rating, //type,
              null, //label,
              null, //description,
              null, //creationTime,
              null, //read,
              true, //setPublic
              true, //createIfNotExists
              false, //withUserRestriction
              false)); //shouldCommit)
        
        if(ratingUri == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
        
        sql.rateEntityByUser(
          ratingUri,
          par.user,
          par.entity,
          par.value,
          userRatedEntityBefore);
        
      }else{
        
        sql.rateEntityByUser(
          null,
          par.user,
          par.entity,
          par.value,
          userRatedEntityBefore);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return ratingSet(par);
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
  public SSServRetI ratingGet(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSRatingGetPar par = (SSRatingGetPar) parA.getFromJSON(SSRatingGetPar.class);
      
      return SSRatingGetRet.get(ratingGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Integer ratingGet(final SSRatingGetPar par) throws SSErr {
    
    try{
      
      if(par.withUserRestriction){
        
        final SSEntity entityEntity =
          sql.getEntityTest(
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entityEntity == null){
          return -1;
        }
      }
      
      return sql.getUserRating(par.user, par.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI ratingOverallGet(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSRatingOverallGetPar par = (SSRatingOverallGetPar) parA.getFromJSON(SSRatingOverallGetPar.class);
      
      return SSRatingOverallGetRet.get(ratingOverallGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSRatingOverall ratingOverallGet(final SSRatingOverallGetPar par) throws SSErr {
    
    try{
      
      if(par.withUserRestriction){
        
        final SSEntity entityEntity =
          sql.getEntityTest(
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entityEntity == null){
          return null;
        }
      }
      
      return sql.getOverallRating(par.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> ratingEntityURIsGet(final SSRatingEntityURIsGetPar par) throws SSErr {
    
    try{
      
      if(par.entities.isEmpty()){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        final List<SSUri> entitiesToQuery = new ArrayList<>();
        SSEntity          entityEntity;
        
        for(SSUri entity : par.entities){
          
          entityEntity =
            sql.getEntityTest(
              par.user,
              entity,
              par.withUserRestriction);
          
          if(entityEntity == null){
            continue;
          }
          
          SSUri.addDistinctWithoutNull(entitiesToQuery, entity);
        }
        
        par.entities.clear();
        par.entities.addAll(entitiesToQuery);
      }
      
      final List<SSEntity>         ratings             = sql.getRatingAsss(null, par.entities);
      final List<SSUri>            entityURIs          = new ArrayList<>();
      SSRatingOverall              overallRating;
      
      final SSRatingOverallGetPar overallRatingGetPar =
        new SSRatingOverallGetPar(
          par.user,
          null, //entity
          par.withUserRestriction);
      
      for(SSEntity rating : ratings){
        
        overallRatingGetPar.entity = ((SSRating)rating).entity;
        
        if(SSStrU.contains(entityURIs, overallRatingGetPar.entity)){
          continue;
        }
        
        overallRating = ratingOverallGet(overallRatingGetPar);
        
        if(
          par.minOverallRating != null &&
          par.minOverallRating > overallRating.score){
          continue;
        }
        
        if(
          par.maxOverallRating != null &&
          par.maxOverallRating < overallRating.score){
          continue;
        }
        
        SSUri.addDistinctWithoutNull(entityURIs, overallRatingGetPar.entity);
      }
      
      return entityURIs;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean ratingsRemove(final SSRatingsRemovePar par) throws SSErr{
    
    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        final SSEntity entityEntity =
          sql.getEntityTest(
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entityEntity == null){
          return false;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.deleteRatingAss(par.user, par.entity);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return ratingsRemove(par);
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
  
//  private void saveRatingUserSetUE(SSServPar parA) throws SSErr {
//
//    Map<String, Object> opPars = new HashMap<>();
//    SSRatingUserSetPar par = new SSRatingUserSetPar(parA);
//
//    opPars.put(SSVarU.shouldCommit, true);
//    opPars.put(SSVarU.user,         par.user);
//    opPars.put(SSVarU.resource,     par.resource);
//    opPars.put(SSVarU.eventType,    SSUEEnum.rateEntity);
//    opPars.put(SSVarU.content,      String.valueOf(par.value));
//
//    SSServReg.callServServer(new SSServPar(SSVarNames.uEAdd, opPars));
//  }
}