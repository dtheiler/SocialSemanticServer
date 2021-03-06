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
package at.tugraz.sss.serv.reg;

import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.entity.api.SSCircleContentRemovedI;
import at.tugraz.sss.serv.entity.api.SSEntityCopiedI;
import at.tugraz.sss.serv.entity.api.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.entity.api.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.entity.api.SSEntitiesSharedWithUsersI;
import at.tugraz.sss.serv.entity.api.SSCopyEntityI;
import at.tugraz.sss.serv.entity.api.SSUserRelationGathererI;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.par.SSCircleContentRemovedPar;
import at.tugraz.sss.serv.datatype.par.SSEntitiesSharedWithUsersPar;
import at.tugraz.sss.serv.datatype.par.SSEntityCopiedPar;
import at.tugraz.sss.serv.datatype.par.SSEntityCopyPar;
import at.tugraz.sss.serv.datatype.par.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.datatype.par.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.datatype.enums.*;
import java.util.*;
import java.util.concurrent.*;

public class SSServReg{
  
  public    static final SSServReg                            inst                                         = new SSServReg();
  protected static final Map<String,        SSServContainerI> servsForClientOps                            = new HashMap<>();
  protected static final Map<Class,         SSServContainerI> servsForServerI                              = new HashMap<>();
  protected static final Map<Class,         SSServContainerI> servsForClientI                              = new HashMap<>();
  protected static final List<SSServContainerI>               servsForGatheringUsersResources              = new ArrayList<>();
  protected static final List<SSServContainerI>               servsForGatheringUserRelations               = new ArrayList<>();
  protected static final List<SSServContainerI>               servsHandlingCircleContentRemoved            = new ArrayList<>();
  protected static final List<SSServContainerI>               servsHandlingEntityCopied                    = new ArrayList<>();
  protected static final List<SSServContainerI>               servsHandlingGetSubEntities                  = new ArrayList<>();
  protected static final List<SSServContainerI>               servsHandlingGetParentEntities               = new ArrayList<>();
  protected static final List<SSServContainerI>               servsHandlingCopyEntity                      = new ArrayList<>();
  protected static final List<SSServContainerI>               servsHandlingDescribeEntity                  = new ArrayList<>();
  protected static final List<SSServContainerI>               servsHandlingAddAffiliatedEntitiesToCircle   = new ArrayList<>();
  protected static final List<SSServContainerI>               servsHandlingPushEntitiesToUsers             = new ArrayList<>();
  protected static final List<SSServContainerI>               servsHandlingEntitiesSharedWithUsers         = new ArrayList<>();
  
  protected static final Map<String, Integer>                         requsLimitsForClientOpsPerUser  = new HashMap<>();
  protected static final Map<String, Map<String, List<SSServImplA>>>  currentRequsForClientOpsPerUser = new HashMap<>();
  protected static final List<ScheduledExecutorService>               schedulers                      = new ArrayList<>();
    
  public static void destroy() throws SSErr{

    try{
      
      SSServImplA impl;
        
      for(SSServContainerI servContainer : servsForServerI.values()){
        
        impl = servContainer.getServImpl();
        
        if(impl != null){
          impl.destroy();
        }
      }
      
      for(SSServContainerI servContainer : servsForClientI.values()){
        
        impl = servContainer.getServImpl();
        
        if(impl != null){
          impl.destroy();
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return;
    }
    
    for(ScheduledExecutorService scheduler : schedulers){
      scheduler.shutdown();
    }
    
    servsForClientOps.clear();
    servsForServerI.clear();
    servsForClientI.clear();
    servsForGatheringUsersResources.clear();
    servsForGatheringUserRelations.clear();
    servsHandlingCircleContentRemoved.clear();
    servsHandlingEntityCopied.clear();
    servsHandlingGetSubEntities.clear();
    servsHandlingGetParentEntities.clear();
    servsHandlingCopyEntity.clear();
    servsHandlingDescribeEntity.clear();
    servsHandlingAddAffiliatedEntitiesToCircle.clear();
    servsHandlingPushEntitiesToUsers.clear();
    servsHandlingEntitiesSharedWithUsers.clear();
    
    requsLimitsForClientOpsPerUser.clear();
    currentRequsForClientOpsPerUser.clear();
  }
  
  public static SSServImplA getClientServ(final Class clientServClass) throws SSErr{
    
    try{
      
      final SSServContainerI serv = servsForClientI.get(clientServClass);
      
      if(serv == null){
        throw SSErr.get(SSErrE.servInvalid);
      }
      
      return serv.getServImpl();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSServContainerI getClientServContainer(
    final String op) throws SSErr{
    
    try{
      
      final SSServContainerI serv = servsForClientOps.get(op);
      
      if(serv == null){
        throw SSErr.get(SSErrE.servInvalid);
      }
      
      return serv;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
      
//      if(!SSServErrReg.containsErr(SSErrE.noClientServiceForOpAvailableOnMachine)){
//        throw error;
//      }
      
//      if(useCloud){
//
//        deployServNode(
//          par,
//          getClientServAvailableOnNodes(par));
//
//        return null;
//      }
  }
      
  public void regClientRequest(
    final SSUri       user, 
    final SSServImplA servImpl,
    final String      op) throws SSErr{
    
    try{
    
      if(!requsLimitsForClientOpsPerUser.containsKey(op)){
        return;
      }
        
      Map<String, List<SSServImplA>> servImplsForUser;
      List<SSServImplA>              servImpls;
      
      synchronized(currentRequsForClientOpsPerUser){
        
        if(!currentRequsForClientOpsPerUser.containsKey(op)){
          
          servImplsForUser = new HashMap<>();
          
          currentRequsForClientOpsPerUser.put(op, servImplsForUser);
        }
        
        if(currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user)) == null){
          currentRequsForClientOpsPerUser.get(op).put(SSStrU.toStr(user), new ArrayList<>());
        }
        
        servImpls = currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user));
        
        if(
          servImpls.size() == requsLimitsForClientOpsPerUser.get(op)){
          throw SSErr.get(SSErrE.maxNumClientConsForOpReached);
        }
        
        servImpls.add(servImpl);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void unregClientRequest(
    final String        op,
    final SSUri         user,
    final SSServImplA   servImpl) throws SSErr{
    
    try{
      if(!requsLimitsForClientOpsPerUser.containsKey(op)){
        return;
      }
      
      synchronized(currentRequsForClientOpsPerUser){
        
        if(
          !currentRequsForClientOpsPerUser.containsKey(op) ||
          currentRequsForClientOpsPerUser.get(op).isEmpty() ||
          currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user)).isEmpty()){
          return;
        }
        
        currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user)).remove(servImpl);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regClientRequestLimit(
    final Class                servImplClientInteraceClass,
    final Map<String, Integer> maxRequsPerOps) throws SSErr{
    
    for(Map.Entry<String, Integer> maxRequestPerOp : maxRequsPerOps.entrySet()){
      
      try{
        servImplClientInteraceClass.getMethod(SSStrU.toStr(maxRequestPerOp.getKey()), SSClientE.class, SSServPar.class);
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
        return;
      }
      
      if(requsLimitsForClientOpsPerUser.containsKey(maxRequestPerOp.getKey())){
        SSServErrReg.regErrThrow(new Exception("client operation already registered for this service"));
        return;
      }
      
      requsLimitsForClientOpsPerUser.put(maxRequestPerOp.getKey(), maxRequestPerOp.getValue());
    }
  }
  
  public void regServForHandlingDescribeEntity(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingDescribeEntity){
        
        if(servsHandlingDescribeEntity.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingDescribeEntity.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingPushEntitiesToUsers(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingPushEntitiesToUsers){
        
        if(servsHandlingPushEntitiesToUsers.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingPushEntitiesToUsers.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingEntitiesSharedWithUsers(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingEntitiesSharedWithUsers){
        
        if(servsHandlingEntitiesSharedWithUsers.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingEntitiesSharedWithUsers.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingAddAffiliatedEntitiesToCircle(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingAddAffiliatedEntitiesToCircle){
        
        if(servsHandlingAddAffiliatedEntitiesToCircle.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingAddAffiliatedEntitiesToCircle.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingCopyEntity(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingCopyEntity){
        
        if(servsHandlingCopyEntity.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingCopyEntity.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingGetParentEntities(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingGetParentEntities){
        
        if(servsHandlingGetParentEntities.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingGetParentEntities.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingGetSubEntities(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingGetSubEntities){
        
        if(servsHandlingGetSubEntities.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingGetSubEntities.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingEntityCopied(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingEntityCopied){
        
        if(servsHandlingEntityCopied.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingEntityCopied.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingCircleContentRemoved(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingCircleContentRemoved){
        
        if(servsHandlingCircleContentRemoved.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingCircleContentRemoved.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForGatheringUserRelations(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForGatheringUserRelations){
        
        if(servsForGatheringUserRelations.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForGatheringUserRelations.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForGatheringUsersResources(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForGatheringUsersResources){
        
        if(servsForGatheringUsersResources.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForGatheringUsersResources.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void regScheduler(final ScheduledExecutorService scheduler) throws SSErr{
    
    try{
      
      if(schedulers.contains(scheduler)){
        return;
      }
      
      schedulers.add(scheduler);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSServContainerI regServ(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return null;
      }
      
      regServOps       (servContainer);
      regServServerI   (servContainer);
      regServClientI   (servContainer);
      
      return servContainer;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static SSServImplA getServ(final Class servServerI) throws SSErr{
    
    SSServContainerI serv;
    
    try{
      
      serv = servsForServerI.get(servServerI);
      
      if(serv == null){
        throw SSErr.get(SSErrE.servInvalid);
      }
      
      return serv.getServImpl();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void regServOps(
    final SSServContainerI servContainer) throws SSErr{
   
    try{
      
      synchronized(servsForClientOps){
        
        for(String op : servContainer.publishClientOps()){
          
          if(servsForClientOps.containsKey(op)){
            throw new Exception("op for client service already registered");
          }
          
          servsForClientOps.put(op, servContainer);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void regServClientI(
    final SSServContainerI servContainer) throws SSErr{
   
    try{
      
      synchronized(servsForClientI){
        
        if(servsForClientI.containsKey(servContainer.servImplClientInteraceClass)){
          throw new Exception("serv server interface already registered");
        }
        
        if(servContainer.servImplClientInteraceClass == null){
          SSLogU.warn("service container has no service client interface", null);
          return;
        }
        
        servsForClientI.put(servContainer.servImplClientInteraceClass, servContainer);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void regServServerI(
    final SSServContainerI servContainer) throws SSErr{
   
    try{
      
      synchronized(servsForServerI){
        
        if(servsForServerI.containsKey(servContainer.servImplServerInteraceClass)){
          throw new Exception("serv server interface already registered");
        }
        
        if(servContainer.servImplServerInteraceClass == null){
          throw SSErr.get(SSErrE.servContainerHasNoServerInterface);
        }
        
        servsForServerI.put(servContainer.servImplServerInteraceClass, servContainer);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSEntity> addAffiliatedEntitiesToCircle(
    final SSServPar         servPar,
    final SSUri             user,
    final SSUri             circle,
    final List<SSEntity>    entities,
    final List<SSUri>       recursiveEntities,
    final boolean           withUserRestriction) throws SSErr{
    
    try{
      final List<SSEntity> addedAffiliatedEntities = new ArrayList<>();
      
      if(
        entities == null || 
        entities.isEmpty()){
        return addedAffiliatedEntities;
      }
      
      final SSAddAffiliatedEntitiesToCirclePar par =
        new SSAddAffiliatedEntitiesToCirclePar(
          user,
          circle,
          entities,
          recursiveEntities,
          withUserRestriction);
      
      for(SSServContainerI serv : servsHandlingAddAffiliatedEntitiesToCircle){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          addedAffiliatedEntities,
          ((SSAddAffiliatedEntitiesToCircleI) serv.getServImpl()).addAffiliatedEntitiesToCircle(servPar, par));
      }
      
      return addedAffiliatedEntities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void circleUsersAdded(
    final SSServPar      servPar,
    final SSUri          user, 
    final SSCircle circle,
    final List<SSUri>    users, 
    final boolean        withUserRestriction) throws SSErr {
    
    try{
      
      if(
        users == null ||
        users.isEmpty()){
        return;
      }
      
      final List<SSEntity>  entitiesToPushToUsers   = new ArrayList<>();
      final List<SSEntity>  addedAffiliatedEntities =
        addAffiliatedEntitiesToCircle(
          servPar,
          user,
          circle.id,
          circle.entities,
          new ArrayList<>(),
          withUserRestriction);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        circle.entities);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        addedAffiliatedEntities);
      
      pushEntitiesToUsers(
        servPar,
        new SSPushEntitiesToUsersPar(
          user,
          entitiesToPushToUsers,
          users,
          withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void pushEntitiesToUsers(
    final SSServPar                servPar,
    final SSPushEntitiesToUsersPar par) throws SSErr{
    
    try{
      
      for(SSServContainerI serv : servsHandlingPushEntitiesToUsers){
        ((SSPushEntitiesToUsersI) serv.getServImpl()).pushEntitiesToUsers(servPar, par);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void entitiesSharedWithUsers(
    final SSServPar                    servPar,
    final SSEntitiesSharedWithUsersPar par) throws SSErr{  
        
    try{
      
      for(SSServContainerI serv : servsHandlingEntitiesSharedWithUsers){
        ((SSEntitiesSharedWithUsersI) serv.getServImpl()).entitiesSharedWithUsers(servPar, par);
      }      
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void entityCopied(
    final SSServPar         servPar,
    final SSEntityCopiedPar entityCopiedPar) throws SSErr{
    
    try{
      
      for(SSServContainerI entityHandler : servsHandlingEntityCopied){
        ((SSEntityCopiedI) entityHandler.getServImpl()).entityCopied(servPar, entityCopiedPar);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void getUsersResources(
    final SSServPar                          servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      for(SSServContainerI serv : servsForGatheringUsersResources){
        ((SSUsersResourcesGathererI) serv.getServImpl()).getUsersResources(servPar, usersEntities);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void getUserRelations(
    final SSServPar                    servPar,
    final List<String>                 allUsers,
    final Map<String, List<SSUri>>     userRelations) throws SSErr{
    
    try{
      for(SSServContainerI serv : servsForGatheringUserRelations){
        ((SSUserRelationGathererI) serv.getServImpl()).getUserRelations(servPar, allUsers, userRelations);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void circleContentRemoved(
    final SSServPar                 servPar,
    final SSCircleContentRemovedPar circleContentRemovedPar) throws SSErr{
    
    try{
      
      for(SSServContainerI serv : servsHandlingCircleContentRemoved){
        ((SSCircleContentRemovedI) serv.getServImpl()).circleContentRemoved(servPar, circleContentRemovedPar);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void copyEntity(
    final SSEntityCopyPar entityCopyPar,
    final SSEntity        entity) throws SSErr{
    
    try{
      
      for(SSServContainerI serv : servsHandlingCopyEntity){
        ((SSCopyEntityI) serv.getServImpl()).copyEntity(entityCopyPar, entity, entityCopyPar);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void circleEntitiesAdded(
    final SSServPar      servPar,
    final SSUri          user, 
    final SSCircle circle,
    final List<SSEntity> entities,
    final boolean        withUserRestriction) throws SSErr{
    
    try{
      
      if(
        entities == null ||
        entities.isEmpty()){
        return;
      }
      
      final List<SSEntity>  entitiesToPushToUsers   = new ArrayList<>();
      final List<SSEntity>  addedAffiliatedEntities =
        addAffiliatedEntitiesToCircle(
          servPar,
          user,
          circle.id,
          entities,
          new ArrayList<>(),
          withUserRestriction);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        entities);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        addedAffiliatedEntities);
      
      pushEntitiesToUsers(
        servPar,
        new SSPushEntitiesToUsersPar(
          user,
          entitiesToPushToUsers,
          SSUri.getDistinctNotNullFromEntities(circle.users),
          withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
   
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSUri                user,
    final SSEntity             entity,
    final SSEntityDescriberPar descPar,
    final boolean              withUserRestriction) throws SSErr{
    
    try{
      
      if(entity == null){
        return null;
      }
      
      if(descPar == null){
        return entity;
      }
        
      descPar.user                = user;
      descPar.withUserRestriction = withUserRestriction;
      
      SSEntity describedEntity = entity;
      
      for(SSServContainerI serv : servsHandlingDescribeEntity){
        describedEntity = ((SSDescribeEntityI) serv.getServImpl()).describeEntity(servPar, describedEntity, descPar);
      }

      return describedEntity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//  public List<SSServContainerI> getServsHandlingAddAffiliatedEntitiesToCircle(){
//    return new ArrayList<>(servsHandlingAddAffiliatedEntitiesToCircle);
//  }
//  
//  public List<SSServContainerI> getServsHandlingEntitiesSharedWithUsers(){
//    return new ArrayList<>(servsHandlingEntitiesSharedWithUsers);
//  }
//    
//  public List<SSServContainerI> getServsHandlingPushEntitiesToUsers(){
//    return new ArrayList<>(servsHandlingPushEntitiesToUsers);
//  }
//  
//  public List<SSServContainerI> getServsHandlingDescribeEntity(){
//    return new ArrayList<>(servsHandlingDescribeEntity);
//  }
//    
//  public List<SSServContainerI> getServsHandlingCopyEntity(){
//    return new ArrayList<>(servsHandlingCopyEntity);
//  }
//  
//  public List<SSServContainerI> getServsHandlingGetParentEntities(){
//    return new ArrayList<>(servsHandlingGetParentEntities);
//  }
//  
//  public List<SSServContainerI> getServsHandlingGetSubEntities(){
//    return new ArrayList<>(servsHandlingGetSubEntities);
//  }
//  
//  public List<SSServContainerI> getServsHandlingEntityCopied(){
//    return new ArrayList<>(servsHandlingEntityCopied);
//  }
//  
//  public List<SSServContainerI> getServsHandlingCircleContentRemoved(){
//    return new ArrayList<>(servsHandlingCircleContentRemoved);
//  }
//    
//  public List<SSServContainerI> getServsGatheringUserRelations(){
//    return new ArrayList<>(servsForGatheringUserRelations);
//  }
//  
//  public List<SSServContainerI> getServsGatheringUsersResources(){
//    return new ArrayList<>(servsForGatheringUsersResources);
//  }

//  public static SSServA servForEntityType(SSEntityEnum entityType) throws SSErr{
//
//    if(SSObjU.isNull (entityType)){
//      SSLogU.logAndThrow(new Exception("entityType null"));
//    }
//
//    if(!servsForEntityManaging.containsKey(entityType.toString())){
//      SSLogU.logAndThrow(new Exception("no serv available for handling entity issues"));
//    }
//
//    return (SSServA) servsForEntityManaging.get(entityType.toString());
//  }

//          if(SSStrU.contains(requestBuffer.toString(), SSRegistryServ.policyFile)){
//            SSVarNames.toStr(op)"policy file serving not supported anymore"); //sScon.writeStringToClient("<?xml version=\"1.0\" ?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + sScon.port + "\"/></cross-domain-policy>");
//          }



//      if(servImpl instanceof SSServImplWithDBA){
//
//        if(((SSServImplWithDBA)servImpl).dbSQL.getActive() > ((SSServImplWithDBA)servImpl).dbSQL.getMaxActive() - 30){
//          SSServErrReg.regErrThrow(SSErr.get(SSErrE.maxNumDBConsReached);
//        }
//      }


//  private void deployServNode(
//    final SSServPar          par,
//    final SSServContainerI   serv) throws SSErr{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarNames.user, par.user);
//    opPars.put(SSVarNames.serv, serv);
//    
//    par.clientCon.writeRetFullToClient((SSServRetI) callServViaServer(new SSServPar(SSVarNames.cloudPublishService, opPars)));
//  }
  
//  private SSServContainerI getClientServAvailableOnNodes(
//    final SSServPar par) throws SSErr{
//    
//    try{
//      final SSServContainerI serv = servs.get(par.op);
//      
//      if(serv == null){
//        throw SSErr.get(SSErrE.noClientServiceForOpAvailableOnNodes);
//      }
//      
//      return serv;
//    }catch(SSErr error){
//      
//      switch(error.code){
//        case noClientServiceForOpAvailableOnNodes: throw error;
//        default: SSServErrReg.regErrThrow(error);
//      }
//      
//      return null;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }