/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
 package at.kc.tugraz.ss.service.userevent.impl;

import at.kc.tugraz.socialserver.utils.SSLinkU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.user.api.SSUserGlobals;
import at.kc.tugraz.ss.service.userevent.api.*;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEDesc;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddAtCreationTimePar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEAddRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEsGetRet;
import at.kc.tugraz.ss.service.userevent.impl.fct.misc.SSUEMiscFct;
import at.kc.tugraz.ss.service.userevent.impl.fct.sql.SSUESQLFct;
import java.lang.reflect.Method;
import java.util.*;

public class SSUEImpl extends SSServImplWithDBA implements SSUEClientI, SSUEServerI, SSEntityHandlerImplI{
  
//  private final SSUEGraphFct graphFct;
  private final SSUESQLFct   sqlFct;
  private final SSUEMiscFct  fct;
  
  public SSUEImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct = new SSUEGraphFct(this);
    this.sqlFct   = new SSUESQLFct  (this);
    this.fct      = new SSUEMiscFct ();
  }
  
  /****** SSEntityHandlerImplI ******/
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par,
    final Boolean                                       shouldCommit) throws Exception{
  }
  
  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityEnum    entityType, 
    final SSUri           userUri, 
    final SSUri           entityUri, 
    final SSLabelStr      label,
    final Long            creationTime,
    final List<SSTag>     tags, 
    final SSRatingOverall overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception{
    
    if(!SSEntityEnum.equals(entityType, SSEntityEnum.userEvent)){
      return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
    }
    
    return SSUEDesc.get(
        entityUri,
        label,
        creationTime,
        author);
  }
  
  /****** SSServRegisterableImplI ******/
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
      
    Method[] methods = SSUEClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSUEServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSUEClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSUEServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  /****** SSUserEventClientI ******/
  
  @Override
  public void uEGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSUEGetRet.get(uEGet(par), par.op));
  }
    
  @Override
  public void uEsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSUEsGetRet.get(uEsGet(par), par.op));
  }
  
  @Override
  public void uEAdd(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSUEAddRet.get(uEAdd(par), par.op));
  }
  
  /****** SSUserEventServerI ******/
  /********************************/
  @Override
  public boolean uEAddAtCreationTime(SSServPar parI) throws Exception{
    
    SSUEAddAtCreationTimePar par   = new SSUEAddAtCreationTimePar(parI);
    SSUri                    ueUri;
    
    try{
      ueUri = SSUEMiscFct.createUEUri();
      
      SSServCaller.addEntity(
        SSUri.get(SSUserGlobals.systemUserURI),
        par.user,
        SSLabelStr.get(par.user.toString()),
        SSEntityEnum.user);
      
      SSServCaller.addEntityAtCreationTime(
        par.user,
        ueUri,
        SSLabelStr.get(ueUri.toString()),
        par.creationTime,
        SSEntityEnum.userEvent);
      
      SSServCaller.addEntity(
        par.user,
        par.resource,
        SSLabelStr.get(par.resource.toString()),
        SSEntityEnum.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addUE(ueUri, par.user, par.resource, par.eventType, par.content);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return true;
  }
  
  @Override
  public boolean uEAdd(final SSServPar parA) throws Exception{
    
    final SSUEAddPar par   = new SSUEAddPar(parA);
    SSUri            ueUri;
    
    try{
      if(par.resource == null){
        par.resource = SSUri.get(SSLinkU.dummyUri);
      }
      
      if(SSStrU.isEmpty(par.content)){
        par.content = SSStrU.empty;
      }
      
      ueUri = fct.createUEUri();
      
      SSServCaller.addUser(
        par.user,
        null,
        par.shouldCommit);
      
      SSServCaller.addEntity(
        par.user,
        ueUri,        
        SSLabelStr.get(ueUri.toString()),        
        SSEntityEnum.userEvent);

      SSServCaller.addEntity(
        par.user,
        par.resource,
        SSLabelStr.get(par.resource.toString()), 
        SSEntityEnum.entity);

      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addUE(ueUri, par.user, par.resource, par.eventType, par.content);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }

    return true;
  }
  
  @Override
  public SSUE uEGet(SSServPar parI) throws Exception {
    
    SSUEGetPar par     = new SSUEGetPar(parI);
    SSUE       result  = null;
    
    try{
      result = sqlFct.getUE(par.ueUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return result;
  }

  //TODO dtheiler: restrict user event retrival for calling user
  @Override
  public List<SSUE> uEsGet(final SSServPar parA) throws Exception {
    
    final SSUEsGetPar par = new SSUEsGetPar(parA);
    
    try{
      
      return sqlFct.getUEs(
        par.forUser,
        par.resource,
        par.eventType,
        par.startTime,
        par.endTime);
      
//      return SSUEMiscFct.filterUEs(uEs, par.startTime, par.endTime);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}