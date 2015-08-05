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
package at.kc.tugraz.ss.service.disc.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.api.*;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddFromClientPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryURIsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscURIsForTargetGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsAllGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscURIsForTargetGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscRemoveRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscWithEntriesRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsAllGetRet;
import at.kc.tugraz.ss.service.disc.impl.fct.activity.SSDiscActivityFct;
import at.kc.tugraz.ss.service.disc.impl.fct.misc.SSDiscMiscFct;
import at.kc.tugraz.ss.service.disc.impl.fct.op.SSDiscUserEntryAddFct;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;
import at.tugraz.sss.serv.SSCircleContentAddedI;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSGetParentEntitiesI;
import at.tugraz.sss.serv.SSGetSubEntitiesI;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;

public class SSDiscImpl
  extends SSServImplWithDBA
  implements
  SSDiscClientI,
  SSDiscServerI,
  SSDescribeEntityI,
  SSCircleContentAddedI,
  SSGetSubEntitiesI, 
  SSGetParentEntitiesI,
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{

  private final SSDiscSQLFct     sqlFct;
  private final SSEntityServerI  entityServ;
  private final SSCircleServerI  circleServ;

  public SSDiscImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct     = new SSDiscSQLFct(this);
    this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.circleServ = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
  }

  @Override
  public void getUserRelations(
    final List<String> allUsers,
    final Map<String, List<SSUri>> userRelations) throws Exception{

    List<SSEntity> discUserCircles;
    List<SSDisc> allDiscs;

    for(String user : allUsers){

      final SSUri userUri = SSUri.get(user);

      allDiscs
        = discsAllGet(
          new SSDiscsAllGetPar(
            null,
            null,
            userUri));

      for(SSDisc disc : allDiscs){

        discUserCircles = 
          circleServ.circlesGet(
            new SSCirclesGetPar(
              null,
              null,
              userUri,
              disc.id,
              null, //entityTypesToIncludeOnly
              false, //withUserRestriction
              true, //withSystemCircles
              false)); //invokeEntityHandlers

        for(SSEntity circle : discUserCircles){

          if(userRelations.containsKey(user)){
            userRelations.get(user).addAll(SSUri.getDistinctNotNullFromEntities(circle.users));
          }else{
            userRelations.put(user, SSUri.getDistinctNotNullFromEntities(circle.users));
          }
        }
      }
    }

    for(Map.Entry<String, List<SSUri>> usersPerUser : userRelations.entrySet()){
      SSStrU.distinctWithoutNull2(usersPerUser.getValue());
    }
  }

  @Override
  public void getUsersResources(
    final List<String> allUsers,
    final Map<String, List<SSUri>> usersResources) throws Exception{

    for(String user : allUsers){

      for(SSDisc disc
        : discsAllGet(
          new SSDiscsAllGetPar(
            null,
            null,
            SSUri.get(user)))){

        if(usersResources.containsKey(user)){
          usersResources.get(user).add(disc.id);
        }else{

          final List<SSUri> resourceList = new ArrayList<>();

          resourceList.add(disc.id);

          usersResources.put(user, resourceList);
        }
      }
    }

    for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
      SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
    }
  }

  @Override
  public List<SSUri> getSubEntities(
    final SSUri user,
    final SSUri entity,
    final SSEntityE type) throws Exception{

    if(!SSStrU.equals(type, SSEntityE.disc)
      && !SSStrU.equals(type, SSEntityE.qa)
      && !SSStrU.equals(type, SSEntityE.chat)){
      return null;
    }

    try{
      return sqlFct.getDiscEntryURIs(entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSUri> getParentEntities(
    final SSUri user,
    final SSUri entity,
    final SSEntityE type) throws Exception{
    
    switch(type){
      
      case discEntry:
      case qaEntry:
      case chatEntry: {
        
        try{
          final List<String> userDiscUris = sqlFct.getDiscURIsForUser(user);
          final List<String> discUris = sqlFct.getDiscURIsContainingEntry(entity);
          
          return SSUri.get(SSStrU.retainAll(discUris, userDiscUris));
        }catch(Exception error){
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
    }
    
    return new ArrayList<>();
  }
  
  @Override
  public void circleContentAdded(final SSCircleContentChangedPar par) throws Exception{
    
    try{
      
      final List<SSUri>    discContentURIs = new ArrayList<>();
      final List<SSEntity> entitiesToAdd   = new ArrayList<>();
        
      for(SSEntity entityToAdd : par.entitiesToAdd){
        
        switch(entityToAdd.type){
          case disc:
          case chat:
          case qa:{
            
            if(SSStrU.contains(par.recursiveEntities, entityToAdd)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityToAdd.id);
            }
            
            discContentURIs.clear();
            
            discContentURIs.addAll(SSDiscMiscFct.getDiscContentURIs(sqlFct, entityToAdd.id));
            
            circleServ.circleEntitiesAdd(
              new SSCircleEntitiesAddPar(
                null,
                null,
                par.user,
                par.circle,
                discContentURIs,
                false, //withUserRestriction
                false)); //shouldCommit
            
            for(SSUri circleUser : par.circleUsers){
              
              if(sqlFct.ownsUserDisc(circleUser, entityToAdd.id)){
                continue;
              }
              
              sqlFct.addDisc(entityToAdd.id, circleUser);
            }
            
            for(SSUri userToPushEntityTo : par.usersToPushEntitiesTo){
              
              if(sqlFct.ownsUserDisc(userToPushEntityTo, entityToAdd.id)){
                continue;
              }
              
              sqlFct.addDisc(entityToAdd.id, userToPushEntityTo);
            }
            
            if(!par.usersToPushEntitiesTo.isEmpty()){
              
              circleServ.circleUsersAdd(
                new SSCircleUsersAddPar(
                  null,
                  null,
                  par.user,
                  par.circle,
                  sqlFct.getDiscUserURIs(entityToAdd.id),
                  false,
                  false));
            }
            
            //call circleContentAddAgain for all those entities added from within the disc
            entitiesToAdd.clear();
            
            for(SSUri discContentURI : discContentURIs){
              
              if(SSStrU.contains(par.recursiveEntities, discContentURI)){
                continue;
              }
              
              entitiesToAdd.add(
                entityServ.entityGet(
                  new SSEntityGetPar(
                    null,
                    null,
                    par.user,
                    discContentURI,
                    true,
                    null)));
              
              par.recursiveEntities.add(discContentURI);
            }
            
            if(!entitiesToAdd.isEmpty()){
              
              for(SSServContainerI serv : SSServReg.inst.getServsHandlingCircleContentAdded()){
                
                ((SSCircleContentAddedI) serv.serv()).circleContentAdded(
                  new SSCircleContentChangedPar(
                    SSUri.getDistinctNotNullFromEntities(entitiesToAdd), //recursiveEntitiesToAdd
                    par.user,
                    par.circle,
                    par.isCirclePublic, //isPublicCircle
                    par.usersToAdd,  //usersToAdd
                    entitiesToAdd, //entitiesToAdd,
                    par.usersToPushEntitiesTo,  //usersToPushEntitiesTo
                    par.circleUsers, //circleUsers
                    par.circleEntities)); //circleEntities
              }
            }
            
            break;
          }
        }
      }
      
      if(!par.usersToAdd.isEmpty()){
        
        for(SSEntity circleEntity : par.circleEntities){
          
          switch(circleEntity.type){
            case qa:
            case disc:
            case chat: {
              
              for(SSUri userToAdd : par.usersToAdd){
                
                if(sqlFct.ownsUserDisc(userToAdd, circleEntity.id)){
                  continue;
                }
                
                sqlFct.addDisc(circleEntity.id, userToAdd);
              }
              
              break;
            }
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

      if(par.setDiscs){
        
        final List<SSUri> discURIs =
          discURIsForTargetGet(
            new SSDiscURIsForTargetGetPar(
              null,
              null,
              par.user,
              entity.id,
              par.withUserRestriction));
        
        for(SSUri discURI : discURIs){
          
          SSEntity.addEntitiesDistinctWithoutNull(
            entity.discs,
            discWithEntriesGet(
              new SSDiscWithEntriesGetPar(
                par.user,
                discURI,
                null,
                par.withUserRestriction)));
        }
      }
      
      return entity;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSUri> discEntryURIsGet(final SSDiscEntryURIsGetPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.disc)){
          return new ArrayList<>();
        }
      }
      
      return sqlFct.getDiscEntryURIs(par.disc);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void discEntryAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscEntryAddFromClientPar par = (SSDiscEntryAddFromClientPar) parA.getFromJSON(SSDiscEntryAddFromClientPar.class);
    final SSDiscEntryAddRet           ret = discEntryAdd(par);

    if(par.addNewDisc){
      
      if(
        !par.users.isEmpty() ||
        !par.circles.isEmpty())
        
        entityServ.entityShare(
          new SSEntitySharePar(
            null,
            null,
            par.user,
            ret.disc,
            par.users, //users
            par.circles, //circles
            false, //setPublic,
            null, //comment,
            true, //withUserRestriction,
            true)); //shouldCommit
    }
    
    sSCon.writeRetFullToClient(ret);
    
    SSDiscActivityFct.discEntryAdd(par, ret, true);
  }

  @Override
  public SSDiscEntryAddRet discEntryAdd(final SSDiscEntryAddPar par) throws Exception{

    try{
      SSUri discEntryUri = null;

      if(par.addNewDisc){

        SSDiscUserEntryAddFct.checkWhetherUserCanAddDisc(par);

        if(par.entity != null){
      
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              null,
              null,
              par.user,
              par.entity,
              null, //type,
              null, //label
              null, //description,
              null, //entitiesToAttach,
              null, //creationTime,
              null, //read,
              false, //setPublic
              true, //withUserRestriction
              false)); //shouldCommit)
        }
      }

      if(!par.addNewDisc){
        SSDiscUserEntryAddFct.checkWhetherUserCanAddDiscEntry(par);
      }

      dbSQL.startTrans(par.shouldCommit);

      if(par.addNewDisc){

        par.disc = SSServCaller.vocURICreate();

        SSDiscUserEntryAddFct.addDisc(
          sqlFct,
          par.disc,
          par.user,
          par.entity,
          par.type,
          par.label,
          par.description);

        attachEntities(
          par.user, 
          par.disc, 
          par.entities, 
          par.entityLabels);
      }

      if(par.entry != null){

        discEntryUri = 
          SSDiscUserEntryAddFct.addDiscEntry(
            sqlFct,
            par.user,
            par.disc,
            par.entry);

        attachEntities(
          par.user, 
          discEntryUri, 
          par.entities, 
          par.entityLabels);
      }

      dbSQL.commit(par.shouldCommit);

      return SSDiscEntryAddRet.get(
        par.disc,
        discEntryUri);

    }catch(Exception error){

      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){

        if(dbSQL.rollBack(par.shouldCommit)){

          SSServErrReg.reset();

          return discEntryAdd(par);
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
  public void discsAllGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscsAllGetPar par = (SSDiscsAllGetPar) parA.getFromJSON(SSDiscsAllGetPar.class);

    sSCon.writeRetFullToClient(SSDiscsAllGetRet.get(discsAllGet(par)));
  }

  @Override
  public List<SSDisc> discsAllGet(final SSDiscsAllGetPar par) throws Exception{

    try{
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<SSDisc>         discsWithoutEntries = new ArrayList<>();
      final SSEntityDescriberPar descPar             = new SSEntityDescriberPar(null);
      SSDisc                     disc;

      descPar.setCircleTypes = true;
        
      for(SSUri discUri : sqlFct.getDiscURIs(par.user)){
        
        disc = sqlFct.getDiscWithoutEntries(discUri);
        
        discsWithoutEntries.add(
          SSDisc.get(
            disc,
            entityServ.entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                disc.id,
                par.withUserRestriction, //withUserRestriction,
                descPar))));
      }

      return discsWithoutEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void discWithEntriesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscWithEntriesGetPar par = (SSDiscWithEntriesGetPar) parA.getFromJSON(SSDiscWithEntriesGetPar.class);

    sSCon.writeRetFullToClient(SSDiscWithEntriesRet.get(discWithEntriesGet(par)));
  }

  @Override
  public SSDisc discWithEntriesGet(final SSDiscWithEntriesGetPar par) throws Exception{

    try{
      final SSEntityDescriberPar descPar           = new SSEntityDescriberPar(par.disc);
      final List<SSDiscEntry>    discEntryEntities = new ArrayList<>();
      SSDiscEntry                discEntry;
      
      final SSDisc               disc =
        SSDisc.get(
          sqlFct.getDiscWithEntries(par.disc),
          entityServ.entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              par.disc,
              par.withUserRestriction, //withUserRestriction,
              descPar)));
      
      descPar.recursiveEntity = null;
      descPar.setComments     = par.includeComments;
      descPar.setLikes        = par.setLikes;
      
      for(SSEntity entry : disc.entries){
        
        discEntry =
          SSDiscEntry.get(
            (SSDiscEntry) entry,
            entityServ.entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                ((SSDiscEntry) entry).id,
                par.withUserRestriction, //withUserRestriction,
                descPar)));
        
        discEntryEntities.add(discEntry);
      }

      disc.entries.clear();
      disc.entries.addAll(discEntryEntities);
      
      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void discRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscRemovePar par = (SSDiscRemovePar) parA.getFromJSON(SSDiscRemovePar.class);

    sSCon.writeRetFullToClient(SSDiscRemoveRet.get(discRemove(par)));

//    SSDiscActivityFct.removeDisc(new SSDiscUserRemovePar(parA));
  }

  @Override
  public SSUri discRemove(final SSDiscRemovePar par) throws Exception{

    try{
      SSServCallerU.canUserAllEntity(par.user, par.disc);
      
      dbSQL.startTrans(par.shouldCommit);
      
      switch(circleServ.circleMostOpenCircleTypeGet(
        new SSCircleMostOpenCircleTypeGetPar(
          null,
          null,
          par.user,
          par.disc,
          false))){
        
        case priv:
          sqlFct.deleteDisc(par.disc);
          break;
        case group:
        case pub:
          sqlFct.unlinkDisc(par.user, par.disc);
          break;
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.disc;
    }catch(Exception error){

      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){

        if(dbSQL.rollBack(par.shouldCommit)){

          SSServErrReg.reset();

          return discRemove(par);
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
  public void discURIsForTargetGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscURIsForTargetGetPar par = (SSDiscURIsForTargetGetPar) parA.getFromJSON(SSDiscURIsForTargetGetPar.class);

    sSCon.writeRetFullToClient(SSDiscURIsForTargetGetRet.get(discURIsForTargetGet(par)));
  }

  @Override
  public List<SSUri> discURIsForTargetGet(final SSDiscURIsForTargetGetPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
        SSServCallerU.canUserReadEntity(par.user, par.entity);
      }

      final List<SSUri> discURIs = sqlFct.getDiscURIs(par.user, par.entity);
      
      if(par.withUserRestriction){
        
        return SSUri.getDistinctNotNullFromEntities(
          entityServ.entitiesGet(
            new SSEntitiesGetPar(
              null, 
              null, 
              par.user, 
              discURIs, //entities, 
              null, //types, 
              null, //descPar, 
              par.withUserRestriction))); //withUserRestriction
      }else{
        return discURIs;
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSDisc> discsWithEntriesGet(final SSDiscsWithEntriesGetPar par) throws Exception{

    try{
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<SSDisc> discsWithEntries = new ArrayList<>();

      for(SSDisc disc : 
        discsAllGet(
          new SSDiscsAllGetPar(
            null, 
            null, 
            par.user))){

        discsWithEntries.add(
          discWithEntriesGet(
            new SSDiscWithEntriesGetPar(
              par.user,
              disc.id,
              par.maxEntries,
              par.withUserRestriction)));
      }

      return discsWithEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void attachEntities(
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   entitiesToAttach,
    final List<SSLabel> entityLabels) throws Exception{
    
    if(entitiesToAttach.isEmpty()){
      return;
    }
    
    try{
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          user,
          entity,
          null, //type
          null, //label,
          null, //description,
          entitiesToAttach,  //entitiesToAttach
          null, //creationTime
          null, //read,
          false, //setPublic
          true, //withUserRestriction
          false)); //shouldCommit
      
      if(
        entityLabels.isEmpty() ||
        entitiesToAttach.size() != entityLabels.size()){
        return;
      }
      
      for(Integer counter = 0; counter < entitiesToAttach.size(); counter++){
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            user,
            entitiesToAttach.get(counter), //entity
            null, //type
            entityLabels.get(counter), //label,
            null, //description,
            null, //entitiesToAttach
            null, //creationTime
            null, //read,
            false, //setPublic
            false, //withUserRestriction
            false)); //shouldCommit
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
