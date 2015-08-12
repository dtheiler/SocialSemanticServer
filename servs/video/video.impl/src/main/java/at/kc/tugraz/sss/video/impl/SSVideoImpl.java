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
package at.kc.tugraz.sss.video.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.sss.video.api.SSVideoClientI;
import at.kc.tugraz.sss.video.api.SSVideoServerI;
import at.kc.tugraz.sss.video.datatypes.SSVideo;
import at.kc.tugraz.sss.video.datatypes.SSVideoAnnotation;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoAnnotationGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoAnnotationsGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAddFromClientPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAnnotationAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideosUserGetPar;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAnnotationAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideosUserGetRet;
import at.kc.tugraz.sss.video.impl.fct.sql.SSVideoSQLFct;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.servs.location.api.SSLocationServerI;
import at.tugraz.sss.servs.location.datatype.par.SSLocationAddPar;

public class SSVideoImpl 
extends SSServImplWithDBA 
implements 
  SSVideoClientI, 
  SSVideoServerI, 
  SSDescribeEntityI, 
  SSPushEntitiesToUsersI,
  SSAddAffiliatedEntitiesToCircleI{
  
  private final SSVideoSQLFct     sqlFct;
  private final SSEntityServerI   entityServ;
  private final SSCircleServerI   circleServ;
  private final SSLocationServerI locationServ;
  
  public SSVideoImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct       = new SSVideoSQLFct(dbSQL);
    this.entityServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.circleServ   = (SSCircleServerI)   SSServReg.getServ(SSCircleServerI.class);
    this.locationServ = (SSLocationServerI) SSServReg.getServ(SSLocationServerI.class);
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws Exception{
    
    try{
      final List<SSUri>    affiliatedURIs     = new ArrayList<>();
      final List<SSEntity> affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        switch(entityAdded.type){
          case disc:
          case chat:
          case qa:{
            
            if(SSStrU.contains(par.recursiveEntities, entityAdded)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityAdded.id);
            }
            
            affiliatedURIs.clear();
            
            for(SSEntity videoContentEntity :
              videoAnnotationsGet(
                new SSVideoAnnotationsGetPar(
                  par.user,
                  entityAdded.id,
                  par.withUserRestriction))){
            
              if(SSStrU.contains(par.recursiveEntities, videoContentEntity)){
                continue;
              }
              
              SSUri.addDistinctWithoutNull(
                affiliatedURIs,
                videoContentEntity.id);
              
              SSEntity.addEntitiesDistinctWithoutNull(
                affiliatedEntities, 
                videoContentEntity);
            }
            
            circleServ.circleEntitiesAdd(
              new SSCircleEntitiesAddPar(
                null,
                null,
                par.user,
                par.circle,
                affiliatedURIs,
                false, //withUserRestriction
                false)); //shouldCommit
            
            break;
          }
        }
      }
      
      if(affiliatedEntities.isEmpty()){
        return affiliatedEntities;
      }
      
      par.entities.clear();
      par.entities.addAll(affiliatedEntities);
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingAddAffiliatedEntitiesToCircle()){
        ((SSAddAffiliatedEntitiesToCircleI) serv.serv()).addAffiliatedEntitiesToCircle(par);
      }
      
      return affiliatedEntities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
   @Override
  public void pushEntitiesToUsers(
    final SSPushEntitiesToUsersPar par) throws Exception {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          case video: {
            
             for(SSUri userToPushTo : par.users){
              sqlFct.addVideoToUser(userToPushTo, entityToPush.id);
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
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      switch(entity.type){
        
        case video:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSVideo.get(
            videoGet(
              new SSVideoUserGetPar(
                null,
                null,
                par.user,
                entity.id,
                par.withUserRestriction,
                false)), //invokeEntityHandlers
            entity);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void videoAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSVideoUserAddFromClientPar par      = (SSVideoUserAddFromClientPar) parA.getFromJSON(SSVideoUserAddFromClientPar.class);
    final SSUri                       videoURI = videoAdd(par);
    
    if(
      par.latitude  != null &&
      par.longitude != null){
      
      locationServ.locationAdd(
        new SSLocationAddPar(
          null,
          null,
          par.user,
          videoURI,
          par.latitude,
          par.longitude,
          par.accuracy,
          par.withUserRestriction, //withUserRestriction,
          par.shouldCommit)); //shouldCommit
    }
    
    sSCon.writeRetFullToClient(SSVideoUserAddRet.get(videoURI));
  }

  @Override
  public SSUri videoAdd(final SSVideoUserAddPar par) throws Exception{
    
    try{
      
      final SSUri             videoUri;
      
      if(par.uuid != null){
        videoUri = SSServCaller.vocURICreateFromId(par.uuid);
      }else{
        
        if(par.link != null){
          videoUri = par.link;
        }else{
          videoUri = SSServCaller.vocURICreate();
        }
      }
      
//      if(par.link != null){
//        videoUri = par.link;
//      }else{
//
//        if(par.uuid != null){
//          videoUri = SSServCaller.vocURICreateFromId(par.uuid);
//        }else{
//          videoUri = SSServCaller.vocURICreate();
//        }
//      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          videoUri,
          SSEntityE.video, //type,
          par.label, //label
          par.description,//description,
          null, //entitiesToAttach,
          par.creationTime, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      if(par.forEntity != null){
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            par.forEntity,
            null, //type,
            null, //label
            null,//description,
            null, //entitiesToAttach,
            par.creationTime, //creationTime,
            null, //read,
            false, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      }      
      
      if(par.link != null){
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            par.link,
            null, //type,
            null, //label
            null,//description,
            null, //entitiesToAttach,
            par.creationTime, //creationTime,
            null, //read,
            false, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      }
      
      sqlFct.addVideo(
        videoUri,
        par.genre,
        par.forEntity,
        par.link);
      
      sqlFct.addVideoToUser(
        par.user,
        videoUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return videoUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return videoAdd(par);
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
  public void videoAnnotationAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSVideoUserAnnotationAddPar par = (SSVideoUserAnnotationAddPar) parA.getFromJSON(SSVideoUserAnnotationAddPar.class);
    
    sSCon.writeRetFullToClient(SSVideoUserAnnotationAddRet.get(videoAnnotationAdd(par)));
  }
  
  @Override
  public SSUri videoAnnotationAdd(final SSVideoUserAnnotationAddPar par) throws Exception{
  
    try{
      final SSUri annotationUri = SSServCaller.vocURICreate();

      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.video,
          null, //type,
          null, //label
          null,//description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          annotationUri,
          SSEntityE.videoAnnotation, //type,
          par.label, //label
          par.description,//description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.createAnnotation(
        par.video, 
        annotationUri, 
        par.x,
        par.y, 
        par.timePoint);
    
      dbSQL.commit(par.shouldCommit);
      
      return annotationUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return videoAnnotationAdd(par);
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
  public SSVideo videoGet(final SSVideoUserGetPar par) throws Exception{
    
    try{
      final SSVideo                video;
      final SSEntityDescriberPar   descPar;
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.video)){
          return null;
        }
      }
      
      if(par.invokeEntityHandlers){
        descPar              = new SSEntityDescriberPar(par.video);
        descPar.setLocations = true;
      }else{
        descPar = null;
      }
      
      video = 
        SSVideo.get(
          sqlFct.getVideo(par.user, par.video), 
          entityServ.entityGet(
            new SSEntityGetPar(
              null, 
              null, 
              par.user,
              par.video,
              par.withUserRestriction, 
              descPar))); //descPar
      
      SSEntity.addEntitiesDistinctWithoutNull(
        video.annotations,
        videoAnnotationsGet(
          new SSVideoAnnotationsGetPar(
            par.user,
            video.id,
            par.withUserRestriction)));
        
      return video;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSVideoAnnotation videoAnnotationGet(final SSVideoAnnotationGetPar par) throws Exception{
    
    try{
     
      if(par.withUserRestriction){
      
        if(!SSServCallerU.canUserRead(par.user, par.annotation)){
          return null;
        }
      }
      
      return sqlFct.getAnnotation(par.annotation);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> videoAnnotationsGet(final SSVideoAnnotationsGetPar par) throws Exception{
    
    try{
     
      if(par.withUserRestriction){
      
        if(!SSServCallerU.canUserRead(par.user, par.video)){
          return new ArrayList<>();
        }
      }
      
      final List<SSEntity> annotations    = new ArrayList<>();
      final List<SSUri>    annotationURIs = sqlFct.getAnnotations(par.video);
      
      for(SSUri annotation : annotationURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          annotations,
          videoAnnotationGet(
            new SSVideoAnnotationGetPar(
              par.user, 
              annotation, 
              par.withUserRestriction)));
        
      }
      
      return annotations;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void videosGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSVideosUserGetPar par = (SSVideosUserGetPar) parA.getFromJSON(SSVideosUserGetPar.class);
    
    sSCon.writeRetFullToClient(SSVideosUserGetRet.get(videosGet(par)));
  }
  
  @Override
  public List<SSEntity> videosGet(final SSVideosUserGetPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
      
        if(par.forEntity != null){
          
          if(!SSServCallerU.canUserRead(par.user, par.forEntity)){
            return new ArrayList<>();
          }
        }
      }
      
      final List<SSEntity> videos    = new ArrayList<>();
      final List<SSUri>    videoURIs = sqlFct.getVideoURIs(par.user, par.forEntity);
      
      for(SSUri videoURI : videoURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          videos,
          videoGet(
            new SSVideoUserGetPar(
              null,
              null,
              par.user,
              videoURI,
              par.withUserRestriction,
              par.invokeEntityHandlers)));
      }
      
      return videos;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}