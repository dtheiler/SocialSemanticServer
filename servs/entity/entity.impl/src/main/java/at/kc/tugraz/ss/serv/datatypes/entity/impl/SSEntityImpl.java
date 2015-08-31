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
package at.kc.tugraz.ss.serv.datatypes.entity.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivURIGetPar;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityShareRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsAndDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAttachEntitiesPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDownloadURIsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDownloadsAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserParentEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSubEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntitiesGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityCopyRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityActivityFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityUserRelationsGatherFct;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSCopyEntityI;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSGetParentEntitiesI;
import at.tugraz.sss.serv.SSGetSubEntitiesI;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSToolContextE;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSEntityImpl 
extends SSServImplWithDBA
implements 
  SSEntityClientI, 
  SSEntityServerI,
  SSUserRelationGathererI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI,
  SSUsersResourcesGathererI{
  
  private final SSEntitySQLFct    sqlFct;
  private final SSActivityServerI activityServ;
  private final SSEvalServerI     evalServ;
  private final SSCircleServerI   circleServ;
  
  public SSEntityImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct          = new SSEntitySQLFct(this);
    this.activityServ    = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
    this.evalServ        = (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class);
    this.circleServ      = (SSCircleServerI)   SSServReg.getServ(SSCircleServerI.class);
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws Exception{
    
    try{
      
      final List<SSUri>    affiliatedURIs     = new ArrayList<>();
      final List<SSEntity> affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        for(SSUri attachedEntity : sqlFct.getAttachedEntities(entityAdded.id)){
          
          if(SSStrU.contains(par.recursiveEntities, attachedEntity)){
            continue;
          }
          
          SSUri.addDistinctWithoutNull(
            affiliatedURIs,
            attachedEntity);
        }
      }
      
      if(affiliatedURIs.isEmpty()){
        return affiliatedEntities;
      }
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        entitiesGet(
          new SSEntitiesGetPar(
            par.user, 
            affiliatedURIs, 
            null, 
            null, 
            par.withUserRestriction)));
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          par.user,
          par.circle, //circle
          affiliatedURIs, //entities
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
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{

    if(par.setAttachedEntities){
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entity.attachedEntities,
        entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            sqlFct.getAttachedEntities(entity.id), //entities
            null, //types,
            null, //descPar,
            par.withUserRestriction)));
    }
    
    if(par.setRead){
      entity.read = sqlFct.getEntityRead (par.user, entity.id);
    }
    
    return entity;
  }
  
  @Override
  public void getUsersResources(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> usersResources) throws Exception{
    
    final List<SSEntityE> types = new ArrayList<>();
    
    types.add(SSEntityE.file);
    types.add(SSEntityE.entity);
    
    for(String user : allUsers){
      
      for(SSUri entity : sqlFct.getEntityURIs(SSUri.get(user), types)){
        
        if(usersResources.containsKey(user)){
          usersResources.get(user).add(entity);
        }else{
          
          final List<SSUri> resourceList = new ArrayList<>();
          
          resourceList.add(entity);
          
          usersResources.put(user, resourceList);
        }
      }
    }
    
    for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
      SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
    }
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    SSEntityUserRelationsGatherFct.getUserRelations(
      sqlFct, 
      allUsers, 
      userRelations);
  }
  
  @Override
  public void entityCopy(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSEntityCopyPar par = (SSEntityCopyPar) parA.getFromJSON(SSEntityCopyPar.class);
    
    sSCon.writeRetFullToClient(SSEntityCopyRet.get(entityCopy(par)));
    
    activityServ.activityAdd(
      new SSActivityAddPar(
        par.user,
        SSActivityE.copyEntityForUsers,
        par.entity,
        par.forUsers,
        null,
        SSTextComment.asListWithoutNullAndEmpty  (par.comment),
        null,
        par.shouldCommit));
    
    evalServ.evalLog(
      new SSEvalLogPar(
        par.user,
        SSToolContextE.sss,
        SSEvalLogE.entityCopy,
        par.entity,  //entity
        null, //content,
        SSUri.asListWithoutNullAndEmpty(par.targetEntity), //entities
        par.forUsers, //users
        par.shouldCommit));
  }
  
  @Override
  public Boolean entityCopy(final SSEntityCopyPar par) throws Exception{
    
    try{   
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSEntity entity =
        entityGet(
          new SSEntityGetPar(
            par.user,
            par.entity,
            par.withUserRestriction,
            null));
      
      if(entity == null){
        return false;
      }
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingCopyEntity()){
        ((SSCopyEntityI) serv.serv()).copyEntity(entity, par);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityCopy(par);
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
  public void entitiesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSEntitiesGetPar par = (SSEntitiesGetPar) parA.getFromJSON(SSEntitiesGetPar.class);
    
    sSCon.writeRetFullToClient(SSEntitiesGetRet.get(entitiesGet(par)));
  }
  
  @Override
  public List<SSEntity> entitiesGet(final SSEntitiesGetPar par) throws Exception{
    
    //TODO to be handled via entity handler like service overarching call; now its done with the help of access restrictions (i.e., circles)
    try{
      
      final List<SSEntity> entities = new ArrayList<>();
      
      if(
        par.user == null &&
        par.entities.isEmpty() &&
        par.types.isEmpty()){ //no information on what to query given
        
        return entities;
      }
        
      if(!par.entities.isEmpty()){
        
        SSStrU.distinctWithoutNull2(par.entities);
        
        for(SSUri entity : par.entities){
          
          SSEntity.addEntitiesDistinctWithoutNull(
            entities,
            entityGet(
              new SSEntityGetPar(
                par.user,
                entity,
                par.withUserRestriction,
                par.descPar)));
        }
        
        return entities;
      }
      
      if(!par.types.isEmpty()){
        
        for(SSEntity entity : sqlFct.getAccessibleEntityURIs(par.user, true, par.types)){
          
//      for(SSUri circle : sqlFct.getCircleURIsForUser(par.forUser, par.withSystemCircles)){
          
//        for(SSEntity entity : sqlFct.getEntitiesForCircle(circle, par.types)){
          
          SSEntity.addEntitiesDistinctWithoutNull(
            entities,
            entityGet(
              new SSEntityGetPar(
                par.user,
                entity.id,
                par.withUserRestriction, //withUserRestriction
                par.descPar))); //descPar
        }
      }
      
      return entities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSEntityGetPar par = (SSEntityGetPar) parA.getFromJSON(SSEntityGetPar.class);
    
    sSCon.writeRetFullToClient(SSEntityGetRet.get(entityGet(par)));
  }
  
  @Override
  public SSEntity entityGet(final SSEntityGetPar par) throws Exception{
    
    try{
      
      if(par.entity == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      SSEntity entity = sqlFct.getEntity(par.entity);
      
      if(entity == null){
        return null;
      }
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, entity.id)){
          return null;
        }
      }
        
      if(par.descPar == null){
        entity.author = sqlFct.getEntity(entity.author.id);
      }
      
      
      if(par.descPar != null){
      
        par.descPar.user                = par.user;
        par.descPar.withUserRestriction = par.withUserRestriction;
        
        for(SSServContainerI serv : SSServReg.inst.getServsHandlingDescribeEntity()){
          entity = ((SSDescribeEntityI) serv.serv()).describeEntity(entity, par.descPar);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntity entityFromTypeAndLabelGet(final SSEntityFromTypeAndLabelGetPar par) throws Exception{
    
    try{
      
      if(
        par.type == null ||
        par.label == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
        
      final SSEntity entity = sqlFct.getEntity(par.label, par.type);
      
      if(entity == null){
        return null;
      }
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, entity.id)){
          return null;
        }
      }
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityUpdate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSEntityUpdatePar par = (SSEntityUpdatePar) parA.getFromJSON(SSEntityUpdatePar.class);
    final SSUri             entityURI;
    Boolean                 isPlaceholderAdd = false;
    
    if(
      par.entity == null &&
      par.type   == null){
      throw new SSErr(SSErrE.parameterMissing);
    }
        
    if(par.entity == null){
      
      switch(par.type){
        case placeholder:{
          
          par.entity = SSServCaller.vocURICreate();
          
          isPlaceholderAdd = true;
          break;
        }
        
        default: throw new SSErr(SSErrE.entityTypeNotSupported);
      }
    }
    
    entityURI = entityUpdate(par);
      
    sSCon.writeRetFullToClient(SSEntityUpdateRet.get(entityURI));
    
    if(isPlaceholderAdd){
    
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
        new SSUEAddPar(
          par.user,
          par.entity,
          SSUEE.bnpPlaceholderAdd,
          null, //content
          par.creationTime,
          par.withUserRestriction,
          par.shouldCommit)); //shouldCommit
    }
  }
  
  @Override
  public SSUri entityUpdate(final SSEntityUpdatePar par) throws Exception{
    
    SSEntity entityToAttach;
    
    try{
      
      if(par.entity == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.entity)){
          throw new SSErr(SSErrE.userNotAllowedToAccessEntity);
        }  
      }
      
      final SSEntity entity = sqlFct.getEntity(par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(entity == null){
        
        if(par.type == null){
          par.type = SSEntityE.entity;
        }
      }
      
      sqlFct.addEntityIfNotExists(
        par.entity,
        par.type,
        par.label,
        par.description,
        par.user,
        par.creationTime);
      
      if(entity == null){
        
        final SSUri privateCircleURI =
          circleServ.circlePrivURIGet(
            new SSCirclePrivURIGetPar(
              par.user,
              false)); //shouldCommit
        
        sqlFct.addEntityToCircleIfNotExists(
          privateCircleURI,
          par.entity);
      }
      
//      for(SSUri entityURIToAttach : par.entitiesToAttach){
//        
//        entityToAttach = sqlFct.getEntity(entityURIToAttach);
//        
//        if(entityToAttach == null){
//          
//          sqlFct.addEntityIfNotExists(
//            entityURIToAttach,
//            SSEntityE.entity,
//            null,
//            null,
//            par.user,
//            null);
//          
//        }else{
//          
//          if(par.withUserRestriction){
//            if(!SSServCallerU.canUserRead(par.user, entityURIToAttach)){
//              continue;
//            }
//          }
//        }
//        
//        sqlFct.attachEntity(par.entity, entityURIToAttach);
//      }
      
      if(par.read != null){
        
        sqlFct.setEntityRead(
          par.user, 
          par.entity,
          par.read);
      }
      
      if(
        par.setPublic != null &&
        par.setPublic){
          
        entityShare(
          new SSEntitySharePar(
            par.user,
            par.entity, //entity,
            null, //users,
            null, //circles,
            true, //setPublic,
            null, //comment,
            par.withUserRestriction,
            false)); //shouldCommit))
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityUpdate(par);
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
  public List<SSEntity> entitiesForLabelsAndDescriptionsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForLabelsAndDescriptionsGetPar  par = new SSEntitiesForLabelsAndDescriptionsGetPar(parA);
      
      //TODO a lot of improvement and error handling here
      
      if(!par.requireds.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.requireds, par.requireds, SSSearchOpE.and);
      }
      
      if(!par.eithers.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.eithers, par.eithers, SSSearchOpE.or);
      }
      
      if(!par.absents.isEmpty()){
        throw new UnsupportedOperationException("absents not suppported yet");
      }
      
      return new ArrayList<>();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesForLabelsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForLabelsGetPar  par      = new SSEntitiesForLabelsGetPar(parA);

      //TODO a lot of improvment and error handling here
      
      if(!par.requireds.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.requireds, new ArrayList<>(), SSSearchOpE.and);
        //TODO could also use public List<SSEntity> getEntitiesForLabelsAndDescriptions(final List<String> requireds,final List<String> absents,final List<String> eithers);
        //here, although it depends on the innodb_ft_min_token_size setting for InnoDB MYSQL tables then, and stopwords and so on
        //see http://dba.stackexchange.com/questions/51144/mysql-match-against-boolean-mode and http://dev.mysql.com/doc/refman/5.6/en/fulltext-stopwords.html
        //please consider that the actual version doesnt exploit MYSQL indexes on labels and descriptions
      }
      
      if(!par.eithers.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.eithers, new ArrayList<>(), SSSearchOpE.or);
      }
      
      if(!par.absents.isEmpty()){
        throw new UnsupportedOperationException("absents not suppported yet");
      }
      
      return new ArrayList<>();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesForDescriptionsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForDescriptionsGetPar  par = new SSEntitiesForDescriptionsGetPar(parA);
      
      //TODO a lot of improvement and error handling here
      
      if(!par.requireds.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(new ArrayList<>(), par.requireds, SSSearchOpE.and);
      }
      
      if(!par.eithers.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(new ArrayList<>(), par.eithers, SSSearchOpE.or);
      }
      
      if(!par.absents.isEmpty()){
        throw new UnsupportedOperationException("absents not suppported yet");
      }
      
      return new ArrayList<>();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityRemove(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityRemovePar par = new SSEntityRemovePar(parA);
      
      sqlFct.deleteEntityIfExists(par.entity);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityRemove(parA);
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
  
  @Override
  public List<SSUri> entityUserParentEntitiesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserParentEntitiesGetPar par = new SSEntityUserParentEntitiesGetPar(parA);

      if(!SSServCallerU.canUserRead(par.user, par.entity)){
        return new ArrayList<>();
      }
      
      final SSEntityE    entityType   = sqlFct.getEntity(par.entity).type;
      final List<SSUri>  entities     = new ArrayList<>();
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingGetParentEntities()){
        entities.addAll(((SSGetParentEntitiesI) serv.serv()).getParentEntities(par.user, par.entity, entityType));
      }
      
      SSStrU.distinctWithoutNull2(entities);
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> entityUserSubEntitiesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserSubEntitiesGetPar par = new SSEntityUserSubEntitiesGetPar(parA);
      final List<SSUri>                   subEntities = new ArrayList<>();
      final SSEntityE                     entityType;
      
      if(!SSServCallerU.canUserRead(par.user, par.entity)){
        return new ArrayList<>();
      }
      
      entityType = sqlFct.getEntity(par.entity).type;
      
      switch(entityType){
        
        case entity: {
          return new ArrayList<>();
        }
        
        default: {
          
          for(SSServContainerI serv : SSServReg.inst.getServsHandlingGetSubEntities()){
            
            subEntities.addAll(
              ((SSGetSubEntitiesI) serv.serv()).getSubEntities(
                par.user, 
                par.entity, 
                entityType));
          }
          
          return subEntities;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityEntitiesAttach(final SSEntityAttachEntitiesPar par) throws Exception{
    
    try{

      if(par.withUserRestriction){
        
        if(
          !SSServCallerU.canUserRead (par.user, par.entity) ||
          !SSServCallerU.canUserRead (par.user, par.entities)){
          return null;
        }
      }
      
      for(SSUri entity : par.entities){
        
        entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            entity,
            SSEntityE.entity,
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic,
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
      }
      
      sqlFct.attachEntities(par.entity, par.entities);
      
      SSServCallerU.handleCirclesFromEntityGetEntitiesAdd(
        par.user,
        par.entity,
        par.entities, //entities
        par.withUserRestriction);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityEntitiesAttach(par);
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
  public SSUri entityDownloadsAdd(final SSEntityDownloadsAddPar par) throws Exception{
    
    try{

      if(par.withUserRestriction){
        
        if(
          !SSServCallerU.canUserRead (par.user, par.entity) ||
          !SSServCallerU.canUserRead (par.user, par.downloads)){
          return null;
        }
      }
      
      sqlFct.addDownloads(par.entity, par.downloads);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityDownloadsAdd(par);
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
  public List<SSUri> entityDownloadsGet(final SSEntityDownloadURIsGetPar par) throws Exception{
    
    try{
      
      final List<SSUri> downloads = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead (par.user, par.entity)){
          return downloads;
        }
      }
      
      for(SSUri download : sqlFct.getDownloads(par.entity)){
        
        if(!SSServCallerU.canUserRead (par.user, download)){
          continue;
        }
        
        downloads.add(download);
      }
      
      return downloads;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
//  @Override
//  public SSUri entityAttachmentsRemove(final SSEntityAttatchmentsRemovePar par) throws Exception{
//    
//    try{
//
////      if(par.withUserRestriction){
////        
////        if(
////          !SSServCallerU.canUserRead(par.user, par.entity) ||
////          !SSServCallerU.canUserRead (par.user, par.attachments)){
////          return null;
////        }
////      }
////      
////      sqlFct.removeAttachments(par.entity, par.attachments);
//      
//      return par.entity;
//    }catch(Exception error){
//      
//      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
//        
//        if(dbSQL.rollBack(par.shouldCommit)){
//          
//          SSServErrReg.reset();
//          
//          return entityAttachmentsRemove(par);
//        }else{
//          SSServErrReg.regErrThrow(error);
//          return null;
//        }
//      }
//      
//      dbSQL.rollBack(par.shouldCommit);
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
  
  @Override
  public void entityShare(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSEntitySharePar par = (SSEntitySharePar) parA.getFromJSON(SSEntitySharePar.class);
    
    final SSUri entityURI = entityShare(par);
      
    sSCon.writeRetFullToClient(SSEntityShareRet.get(entityURI));
    
    if(!par.users.isEmpty()){
      SSEntityActivityFct.shareEntityWithUsers(par);
    }
    
    if(!par.circles.isEmpty()){
      SSEntityActivityFct.shareEntityWithCircles(par);
    }
    
    if(par.setPublic){
      SSEntityActivityFct.setEntityPublic(par);
    }
  }
  
  @Override  
  public SSUri entityShare(final SSEntitySharePar par) throws Exception{
    
    try{
      
      if(
        par.users.isEmpty()   &&
        par.circles.isEmpty() &&
        !par.setPublic){
        
        return par.entity;
      }
      
      if(par.withUserRestriction){
        
        if(par.user == null){
          throw new SSErr(SSErrE.parameterMissing);
        }
        
        if(par.setPublic){
          
          if(!SSServCallerU.canUserAll(par.user, par.entity)){
            return null;
          }
        }else{
          if(!SSServCallerU.canUserRead(par.user, par.entity)){
            return null;
          }
        }
      }
      
      final SSEntity entity =
        entityGet(
          new SSEntityGetPar(
            par.user,
            par.entity,
            par.withUserRestriction,
            null)); //descPar
      
      if(entity == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(!par.users.isEmpty()){
        
        new SSEntityShareWithUsers(circleServ).handle(
          par.user, 
          entity,
          par.users, 
          par.withUserRestriction);
      }
        
      if(!par.circles.isEmpty()){
        
        new SSEntityShareWithCircles(circleServ).handle(
          par.user,
          entity,
          par.circles,
          par.withUserRestriction);
      }
      
      if(par.setPublic){
        
        new SSEntitySetPublic(circleServ).handle(
          par.user,
          entity,
          par.withUserRestriction);
      }
     
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityShare(par);
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

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.coll.toString());
//   
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//      return SSEntityEnum.coll.toString();
//    }

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.disc.toString());
//   
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//      return SSEntityEnum.disc.toString();
//    }
    
//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.file.toString());
//    
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//      return SSEntityEnum.file.toString();
//    }

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.user.toString());
//    
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//
//			List<User> allUsers = ModelGC.SOCKET.getAllUsers();
//			
//			for(User user : allUsers){
//				
//				if(GM.isSame(uri, user.getId())){
//					
//					return SSEntityEnum.user;
//				}
//			}
//
//      return SSEntityEnum.user.toString();
//    }

//    return SSEntityEnum.entity.toString();