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
package at.kc.tugraz.ss.service.tag.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.tag.api.*;
import at.kc.tugraz.ss.service.tag.datatypes.*;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagEntitiesForTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsRemoveRet;
import at.kc.tugraz.ss.service.tag.impl.fct.userrelationgatherer.SSTagUserRelationGathererFct;
import at.tugraz.sss.serv.SSCircleContentRemovedI;
import at.tugraz.sss.serv.SSCircleContentRemovedPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSEntityCopiedI;
import at.tugraz.sss.serv.SSEntityCopiedPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSSearchOpE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.common.impl.tagcategory.SSTagAndCategoryCommonMisc;
import at.tugraz.sss.servs.common.impl.tagcategory.SSTagAndCategoryCommonSQL;
import sss.serv.eval.api.SSEvalServerI;

public class SSTagImpl
extends SSServImplWithDBA
implements
  SSTagClientI,
  SSTagServerI,
  SSDescribeEntityI,
  SSEntityCopiedI,
  SSUserRelationGathererI,
  SSCircleContentRemovedI,
  SSUsersResourcesGathererI{
  
  private final SSTagActAndLogFct          actAndLogFct;
  private final SSTagAndCategoryCommonSQL  sql;
  private final SSTagAndCategoryCommonMisc commonMiscFct;
  private final SSEntityServerI            entityServ;
  
  public SSTagImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sql           = new SSTagAndCategoryCommonSQL (dbSQL, SSVocConf.systemUserUri, SSEntityE.tag);
    this.commonMiscFct = new SSTagAndCategoryCommonMisc(dbSQL, SSEntityE.tag);

    this.actAndLogFct = 
      new SSTagActAndLogFct(
        (SSActivityServerI) SSServReg.getServ (SSActivityServerI.class),
        (SSEvalServerI)     SSServReg.getServ (SSEvalServerI.class));
    
    this.entityServ = (SSEntityServerI)   SSServReg.getServ (SSEntityServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setTags){
        
        entity.tags.addAll(
          tagsGet(
            new SSTagsGetPar(
              par.user,
              null, //forUser
SSUri.asListNotNull(entity.id), //entities
              null, //labels
              null, //labelSearchOp, 
              SSSpaceE.asListWithoutNull(par.space), //spaces
SSUri.asListNotNull(par.circle), //circles
              null, //startTime
              par.withUserRestriction))); //withUserRestriction
      }
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    try{
      
      final Map<String, List<SSUri>> usersPerTag    = new HashMap<>();
      final Map<String, List<SSUri>> usersPerEntity = new HashMap<>();
      List<SSTag>                    tags;
      
      for(String user : allUsers){
        
        final SSUri userUri = SSUri.get(user);
        
        for(SSEntity tagEntity :
          tagsGet(
            new SSTagsGetPar(
              userUri,
              userUri, //forUser
              null, //entities
              null, //labels,
              null, //labelSearchOp, 
              null, //spaces
              null, //circles
              null, //startTime,
              false))){ //withUserRestriction){
          
          SSTagUserRelationGathererFct.addUserForTag     (tagEntity, usersPerTag);
          SSTagUserRelationGathererFct.addUserForEntity  (tagEntity, usersPerEntity);
        }
      }
      
      SSTagUserRelationGathererFct.addUserRelations(userRelations, usersPerTag);
      SSTagUserRelationGathererFct.addUserRelations(userRelations, usersPerEntity);
      
      for(Map.Entry<String, List<SSUri>> usersPerUser : userRelations.entrySet()){
        SSStrU.distinctWithoutNull2(usersPerUser.getValue());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void getUsersResources(
    final List<String>             allUsers,
    final Map<String, List<SSUri>> usersResources) throws Exception{
    
    try{
      SSUri userUri;
      
      for(String user : allUsers){
        
        userUri = SSUri.get(user);
        
        for(SSEntity tagEntity :
          tagsGet(
            new SSTagsGetPar(
              userUri,
              userUri, //forUser
              null, //entities
              null, //labels
              null, //labelSearchOp, 
              null, //spaces
              null, //circles
              null, //startTime,
              false))){ //withUserRestriction
          
          if(usersResources.containsKey(user)){
            usersResources.get(user).add(((SSTag)tagEntity).entity);
          }else{
            
            final List<SSUri> resourceList = new ArrayList<>();
            
            resourceList.add(((SSTag)tagEntity).entity);
            
            usersResources.put(user, resourceList);
          }
        }
      }
      
      for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
        SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void circleContentRemoved(
    final SSCircleContentRemovedPar par) throws Exception {
    
    if(!par.removeCircleSpecificMetadata){
      return;
    }
    
    for(SSUri entity : par.entities){
      
      tagsRemove(
        new SSTagsRemovePar(
          par.user,
          null, //forUser
          entity, 
          null, //label, 
          SSSpaceE.circleSpace, //space, 
          par.circle, 
          par.withUserRestriction, 
          false));
    }
  }
  
  @Override
  public void entityCopied(final SSEntityCopiedPar par) throws Exception{
    
    try{
      
      if(!par.includeMetadataSpecificToEntityAndItsEntities){
        return;
      }
      
      switch(par.entity.type){
        
        case circle:{
          
          for(SSEntity tag :
            tagsGet(
              new SSTagsGetPar(
                par.user,
                null, //forUser
                SSUri.getDistinctNotNullFromEntities(par.entities), //entities
                null, //labels
                null, //labelSearchOp, 
                SSSpaceE.asListWithoutNull(SSSpaceE.circleSpace), //spaces
                SSUri.getDistinctNotNullFromEntities(par.entity), //circles
                null, //startTime,
                par.withUserRestriction))){
            
            if(par.targetUser != null){
              
              tagAdd(
                new SSTagAddPar(
                  par.targetUser,  //user
                  ((SSTag)tag).entity, //entity
                  ((SSTag)tag).tagLabel, //label
                  ((SSTag)tag).space, //space
                  par.targetEntity, //circle
                  tag.creationTime, //creationTime
                  par.withUserRestriction, //withUserRestriction
                  false)); //shouldCommmit
            }else{
              
              tagAdd(
                new SSTagAddPar(
                  ((SSTag)tag).user, //user
                  ((SSTag)tag).entity, //entity
                  ((SSTag)tag).tagLabel, //label
                  ((SSTag)tag).space, //space
                  par.targetEntity, //circle
                  tag.creationTime, //creationTime
                  par.withUserRestriction, //withUserRestriction
                  false)); //shouldCommmit
            }
          }
          break;
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void tagsAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagsAddPar par     = (SSTagsAddPar) parA.getFromJSON(SSTagsAddPar.class);
    final List<SSUri>  tagURIs = tagsAdd(par);
    
    sSCon.writeRetFullToClient(SSTagsAddRet.get(tagURIs));
    
    actAndLogFct.tagsAdd(
      par.user,
      par.entities, 
      tagURIs, 
      par.labels, 
      par.shouldCommit);
  }
  
  @Override
  public List<SSUri> tagsAdd(final SSTagsAddPar par) throws Exception {
    
    try{
      
      final List<SSUri> tags = new ArrayList<>();
      
      for(SSTagLabel tagLabel : par.labels) {
        
        for(SSUri entity : par.entities){
        
          SSUri.addDistinctWithoutNull(
            tags,
            tagAdd(
              new SSTagAddPar(
                par.user,
                entity,
                tagLabel,
                par.space,
                par.circle,
                par.creationTime,
                par.withUserRestriction,
                par.shouldCommit)));
        }
      }
      
      SSStrU.distinctWithoutNull2(tags);
      
      return tags;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return tagsAdd(par);
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
  public void tagAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagAddPar par    = (SSTagAddPar) parA.getFromJSON(SSTagAddPar.class);
    final SSUri       tagURI = tagAdd(par);
    
    sSCon.writeRetFullToClient(SSTagAddRet.get(tagURI));
    
    if(tagURI != null){
    
      actAndLogFct.tagsAdd(
        par.user, 
        SSUri.asListNotNull(par.entity), 
        SSUri.asListNotNull(tagURI), 
        SSTagLabel.asListWithoutNullAndEmpty(par.label), 
        par.shouldCommit);
    }
  }
  
  @Override
  public SSUri tagAdd(final SSTagAddPar par) throws Exception {
    
    try{
      
      final SSUri       tagUri;
      final SSEntity    tagEntity = 
        entityServ.entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            par.user,
            SSLabel.get(SSStrU.toStr(par.label)), //label,
            SSEntityE.tag, //type,
            par.withUserRestriction)); //withUserRestriction
      
      if(SSObjU.isNull(par.circle)){
        
        par.circle =
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePubURIGet(
            new SSCirclePubURIGetPar(
              null, 
              false));
      }else{
        par.space = SSSpaceE.circleSpace;
      }
      
      if(par.space == null){
        par.space = SSSpaceE.sharedSpace;
      }

      switch(par.space){
        
        case circleSpace:{
          
          final SSEntity circle = 
            sql.getEntityTest(
              par.user, 
              par.circle, 
              par.withUserRestriction);
          
          if(circle == null){
            return null;
          }
          
          break;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      par.entity =
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
      
      if(par.entity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      if(tagEntity == null){
       
        tagUri =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par.user,
              SSServCaller.vocURICreate(),
              SSEntityE.tag, //type,
              SSLabel.get(SSStrU.toStr(par.label)), //label
              null, //description,
              par.creationTime, //creationTime,
              null, //read,
              true, //setPublic
              true, //createIfNotExists
              par.withUserRestriction, //withUserRestriction
              false)); //shouldCommit)
        
        if(tagUri == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
      }else{
        tagUri = tagEntity.id;
      }
      
      sql.addMetadataAssIfNotExists1(
        tagUri,
        par.user,
        par.entity,
        par.space,
        par.circle,
        par.creationTime);
      
      dbSQL.commit(par.shouldCommit);
      
      return tagUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return tagAdd(par);
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
  public void tagsRemove(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagsRemovePar par    = (SSTagsRemovePar) parA.getFromJSON(SSTagsRemovePar.class);
    final Boolean         worked = tagsRemove(par);
    
    sSCon.writeRetFullToClient(SSTagsRemoveRet.get(worked));
    
    if(worked){
      
      actAndLogFct.tagsRemove(
        par.user, 
        par.entity, 
        par.label, 
        par.shouldCommit);
    }
  }
  
  @Override
  public Boolean tagsRemove(final SSTagsRemovePar par) throws Exception {
    
    try{
      
      if(SSObjU.isNull(par.user)){
         throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.entity != null){
      
        final SSEntity entity = 
          sql.getEntityTest(
            par.user, 
            par.entity, 
            par.withUserRestriction);

        if(entity == null){
          return false;
        }
      }
      
      if(par.withUserRestriction){
        
        if(SSObjU.isNull(par.entity)){
          throw new SSErr(SSErrE.parameterMissing);
        }
        
        if(par.circle != null){
          
          final SSEntity circle = 
            sql.getEntityTest(
              par.user, 
              par.circle, 
              par.withUserRestriction);
          
          if(circle == null){
            return false;
          }
        }
        
        if(!SSStrU.equals(par.space, SSSpaceE.circleSpace)){
          par.circle = null;
        }
      }
      
      SSUri tagUri = null;
      
      if(par.label != null){
        
        final SSEntity tagEntity =
          entityServ.entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              par.user,
              SSLabel.get(SSStrU.toStr(par.label)), //label,
              SSEntityE.tag, //type,
              par.withUserRestriction)); //withUserRestriction
        
        if(tagEntity == null){
          return true;
        }else{
          tagUri = tagEntity.id;
        }
      }
      
      if(!par.withUserRestriction){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss(
          par.forUser,
          par.entity,
          tagUri,
          par.space,
          par.circle);
        
        dbSQL.commit(par.shouldCommit);
        
        return true;
      }
      
      if(
        par.space  == null &&
        par.entity == null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss(par.user, null, tagUri, SSSpaceE.privateSpace, par.circle);
        sql.removeMetadataAsss(par.user, null, tagUri, SSSpaceE.sharedSpace,  par.circle);
        sql.removeMetadataAsss(par.user, null, tagUri, SSSpaceE.circleSpace,  par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space  != null &&
        par.entity == null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss(par.user, null, tagUri, par.space, par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space  == null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss (par.user, par.entity, tagUri, SSSpaceE.privateSpace, par.circle);
        sql.removeMetadataAsss (null,     par.entity, tagUri, SSSpaceE.sharedSpace,  par.circle);
        sql.removeMetadataAsss (null,     par.entity, tagUri, SSSpaceE.circleSpace,  par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space  != null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss(null, par.entity, tagUri, par.space, par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return tagsRemove(par);
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
  public void tagEntitiesForTagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSTagEntitiesForTagsGetPar par = (SSTagEntitiesForTagsGetPar) parA.getFromJSON(SSTagEntitiesForTagsGetPar.class);
    
    sSCon.writeRetFullToClient(SSTagEntitiesForTagsGetRet.get(tagEntitiesForTagsGet(par)));
  }
  
  @Override
  public List<SSUri> tagEntitiesForTagsGet(final SSTagEntitiesForTagsGetPar par) throws Exception{
    
    try{

      final List<SSEntity> tagAsss =
        tagsGet(
          new SSTagsGetPar(
            par.user,
            par.forUser,
            par.entities,
            par.labels,
            par.labelSearchOp,
            par.spaces,
            par.circles,
            par.startTime,
            par.withUserRestriction));
      
      return commonMiscFct.getEntitiesFromMetadataDistinctNotNull(tagAsss);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void tagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagsGetPar par = (SSTagsGetPar) parA.getFromJSON(SSTagsGetPar.class);
    
    sSCon.writeRetFullToClient(SSTagsGetRet.get(tagsGet(par)));
  }
  
  @Override
  public List<SSEntity> tagsGet(final SSTagsGetPar par) throws Exception {
    
    try{
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
       
        if(
          par.forUser != null &&
          !SSStrU.equals(par.user, par.forUser)){
          throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
        }
      }
      
      final List<SSEntity> tags = 
        commonMiscFct.getMetadata(
          par.user,
          par.forUser,
          par.entities,
          SSStrU.toStr(par.labels),
          par.labelSearchOp,
          par.spaces,
          par.circles,
          par.startTime);
      
      return commonMiscFct.filterMetadataByEntitiesUserCanAccess(
        tags, 
        par.withUserRestriction, 
        par.user, 
        par.forUser);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void tagFrequsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagFrequsGetPar par = (SSTagFrequsGetPar) parA.getFromJSON(SSTagFrequsGetPar.class);
    
    sSCon.writeRetFullToClient(SSTagFrequsGetRet.get(tagFrequsGet(par)));
  }
  
  @Override
  public List<SSTagFrequ> tagFrequsGet(final SSTagFrequsGetPar par) throws Exception {
    
    try{
      
      if(
        !par.entities.isEmpty() &&
        par.useUsersEntities){
        throw new Exception("entities and useForUsersEntities set at the same time");
      }
      
      if(par.useUsersEntities){
        
        //TODO change: hack from bits and pieces
        final List<SSEntityE> types =
          SSEntityE.asListNotNull(
            SSEntityE.entity,
            SSEntityE.file,
            SSEntityE.evernoteResource,
            SSEntityE.evernoteNote,
            SSEntityE.evernoteNotebook,
            SSEntityE.placeholder);
        
        final SSEntitiesGetPar entitiesGetPar = 
          new SSEntitiesGetPar(
            par.user,
            null, //entities
            null, //descPar
            par.withUserRestriction);
        
        entitiesGetPar.types.addAll(types);
          
        par.entities.addAll(
          SSUri.getDistinctNotNullFromEntities(
            entityServ.entitiesGet(entitiesGetPar))); //withUserRestriction
      }
      
      final List<SSTagFrequ> tagFrequs = new ArrayList<>();
      
      for(SSEntityA tagFrequ :
        commonMiscFct.getMetadataFrequsFromMetadata(
          tagsGet(
            new SSTagsGetPar(
              par.user,
              par.forUser,
              par.entities,
              par.labels,
              SSSearchOpE.or, //labelSearchOp
              par.spaces,
              par.circles,
              par.startTime,
              par.withUserRestriction)))){
        
        tagFrequs.add((SSTagFrequ) tagFrequ);
      }
      
      return tagFrequs;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}


//@Override
//  public SSUri tagEdit(final SSTagEditPar par) throws Exception {
//    
//    try{
//      
//      SSUri                  newTagUri = null;
//      
//      if(par.user == null){
//        throw new Exception("user null");
//      }
//      
//      final SSUri    tagUri;
//      final SSEntity tagEntity = 
//        entityServ.entityGet(
//          new SSEntityGetPar(
//            null,
//            null,
//            par.user,
//            null, //entity
//            null, //forUser,
//            SSLabel.get(SSStrU.toStr(par.label)), //label,
//            SSEntityE.tag, //type,
//            false, //withUserRestriction
//            false, //invokeEntityHandlers
//            null,  //descPar
//            true)); //logErr
//      
//      if(tagEntity != null){
//        tagUri = tagEntity.id;
//      }else{
//        tagUri = SSServCaller.vocURICreate();
//      }
//      
//      final List<SSTag> tags =
//        tagsGet(
//          new SSTagsGetPar(
//            null,
//            null,
//            par.user,
//            par.user,
//            SSUri.asListWithoutNullAndEmpty(par.entity),
//            SSTagLabel.asListWithoutNullAndEmpty(par.tag),
//            null,
//            null));
//      
//      for(SSTag tag : tags){
//        
//        tagsRemove(
//          new SSTagsRemovePar(
//            null,
//            null,
//            par.user,
//            par.user,
//            tag.entity,
//            SSTagLabel.get(SSStrU.toStr(tag.label)),
//            tag.space,
//            false,
//            false));
//        
//        newTagUri =
//          tagAdd(
//            new SSTagAddPar(
//              null,
//              null,
//              par.user,
//              tag.entity,
//              par.label,
//              tag.space,
//              null,
//              false));
//      }
//      
//      if(newTagUri == null){
//        return tagUri;
//      }else{
//        return newTagUri;
//      }
//      
//    }catch(Exception error){
//      
//      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
//        
//        if(dbSQL.rollBack(par.shouldCommit)){
//          
//          SSServErrReg.reset();
//          
//          return tagEdit(par);
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

//@Override
//  public List<SSUri> tagEntitiesForTagsGet(final SSTagEntitiesForTagsGetPar par) throws Exception{
//    
//    //TODO dtheiler: use start time for this call as well
//    try{
//
//      final List<SSUri> entityURIs = new ArrayList<>();
//      
//      if(par.user == null){
//        throw new SSErr(SSErrE.parameterMissing);
//      }
//      
//      if(par.withUserRestriction){
//       
//        if(
//          par.forUser != null &&
//          !SSStrU.equals(par.user, par.forUser)){
//          throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
//        }
//      }
//
//      if(par.spaces.isEmpty()){
//        
//        SSUri.addDistinctWithoutNull(
//          entityURIs,
//          commonMiscFct.getEntitiesForMetadataIfSpaceNotSet(
//            par.user, 
//            par.forUser, 
//            SSStrU.toStr(par.labels)));
//      }else{
//        
//        for(SSSpaceE space : par.spaces){
//          
//          switch(space){
//            
//            case privateSpace:{
//              
//              SSUri.addDistinctWithoutNull(
//                entityURIs,
//                commonMiscFct.getEntitiesForMetadataIfSpaceSet(
//                  par.user,
//                  SSStrU.toStr(par.labels),
//                  space,
//                  par.user));
//              
//              break;
//            }
//            
//            case sharedSpace:{
//              
//              SSUri.addDistinctWithoutNull(
//                entityURIs,
//                commonMiscFct.getEntitiesForMetadataIfSpaceSet(
//                  par.user,
//                  SSStrU.toStr(par.labels),
//                  space,
//                  par.forUser));
//              break;
//            }
//          }
//        }
//      } 
//          
//      return commonMiscFct.filterEntitiesUserCanAccess(
//        entityURIs, 
//        par.withUserRestriction, 
//        par.user, 
//        par.forUser);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }