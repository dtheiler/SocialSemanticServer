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
package at.tugraz.sss.servs.livingdocument.impl;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.service.disc.api.SSDiscServerI;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscTargetsAddPar;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.livingdocument.api.SSLivingDocClientI;
import at.tugraz.sss.servs.livingdocument.api.SSLivingDocServerI;
import at.tugraz.sss.servs.livingdocument.conf.SSLivingDocConf;
import at.tugraz.sss.servs.livingdocument.datatype.SSLivingDocument;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocAddPar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocGetPar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocRemovePar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocUpdatePar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocsGetPar;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocAddRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocGetRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocRemoveRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocUpdateRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocsGetRet;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;

public class SSLivingDocImpl 
extends SSServImplWithDBA
implements
  SSLivingDocClientI,
  SSLivingDocServerI,
  SSDescribeEntityI,
  SSPushEntitiesToUsersI{
  
  private final SSLivingDocSQLFct  sqlFct;
  private final SSLivingDocConf    livingDocConf;
  
  public SSLivingDocImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.livingDocConf  = (SSLivingDocConf) conf;
    this.sqlFct         = new SSLivingDocSQLFct(this);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    switch(entity.type){
      
      case livingDoc:{
        
        return SSLivingDocument.get(
          livingDocGet(
            new SSLivingDocGetPar(
              par.user,
              entity.id,
              par.withUserRestriction,
              false)),
          entity);
      }
      
      default: return entity;
    }
  }
  
  @Override
  public void pushEntitiesToUsers(
    final SSPushEntitiesToUsersPar par) throws Exception {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          
          case livingDoc: {
            
            for(SSUri userToPushTo : par.users){
              sqlFct.addLivingDoc(entityToPush.id, userToPushTo);
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
  public void livingDocAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSLivingDocAddPar par          = (SSLivingDocAddPar) parA.getFromJSON(SSLivingDocAddPar.class);
    final SSUri             livingDocURI = livingDocAdd(par);
    
    if(livingDocURI != null){
      
      if(par.discussion != null){
        
        final SSDiscServerI discServ = (SSDiscServerI) SSServReg.getServ(SSDiscServerI.class);
        final List<SSUri>   targets  = new ArrayList<>();
        
        targets.add(livingDocURI);
        
        discServ.discTargetsAdd(
          new SSDiscTargetsAddPar(
            par.user, 
            par.discussion, 
            targets, 
            par.withUserRestriction, 
            par.shouldCommit));
      }
    }
    
    sSCon.writeRetFullToClient(SSLivingDocAddRet.get(livingDocURI));
  }
  
  @Override
  public SSUri livingDocAdd(final SSLivingDocAddPar par) throws Exception{
    
    try{
      
      if(par.uri == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final SSEntityServerI  entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
//      final SSUri            livingDocURI = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          par.user,
          par.uri,
          SSEntityE.livingDoc, //type,
          par.label, //label
          par.description,//description,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.createLivingDoc(par.uri, par.user);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.uri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return livingDocAdd(par);
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
  public void livingDocUpdate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSLivingDocUpdatePar par          = (SSLivingDocUpdatePar) parA.getFromJSON(SSLivingDocUpdatePar.class);
    final SSUri                livingDocURI = livingDocUpdate(par);
    
    if(livingDocURI != null){
      
      if(par.discussion != null){
        
        final SSDiscServerI discServ = (SSDiscServerI) SSServReg.getServ(SSDiscServerI.class);
        final List<SSUri>   targets  = new ArrayList<>();
        
        targets.add(livingDocURI);
        
        discServ.discTargetsAdd(
          new SSDiscTargetsAddPar(
            par.user, 
            par.discussion, 
            targets, 
            par.withUserRestriction, 
            par.shouldCommit));
      }
    }
    
    sSCon.writeRetFullToClient(SSLivingDocUpdateRet.get(livingDocURI));
  }
  
  @Override
  public SSUri livingDocUpdate(final SSLivingDocUpdatePar par) throws Exception{
    
    try{
      
      final SSEntityServerI  entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          par.user,
          par.livingDoc,
          SSEntityE.livingDoc, //type,
          par.label, //label
          par.description,//description,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      dbSQL.commit(par.shouldCommit);
      
      return par.livingDoc;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return livingDocUpdate(par);
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
  public void livingDocRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSLivingDocRemovePar par = (SSLivingDocRemovePar) parA.getFromJSON(SSLivingDocRemovePar.class);
    
    sSCon.writeRetFullToClient(SSLivingDocRemoveRet.get(livingDocRemove(par)));
  }
  
  @Override
  public SSUri livingDocRemove(final SSLivingDocRemovePar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.livingDoc)){
          return null;
        }
      }
      
      final SSEntityServerI  entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeLivingDoc(par.livingDoc);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.livingDoc;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return livingDocRemove(par);
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
  public void livingDocGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSLivingDocGetPar par = (SSLivingDocGetPar) parA.getFromJSON(SSLivingDocGetPar.class);
    
    sSCon.writeRetFullToClient(SSLivingDocGetRet.get(livingDocGet(par)));
  }
  
  @Override
  public SSLivingDocument livingDocGet(final SSLivingDocGetPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
       
        if(!SSServCallerU.canUserRead(par.user, par.livingDoc)){
          return null;
        }
      }

      final SSEntityServerI  entityServ       = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSLivingDocument livingDoc;
      SSEntityDescriberPar   descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.livingDoc);
      }else{
        descPar = null;
      }
        
      livingDoc =
        SSLivingDocument.get(
          sqlFct.getLivingDoc(par.livingDoc),
          entityServ.entityGet(
            new SSEntityGetPar(
              par.user,
              par.livingDoc,
              par.withUserRestriction,
              descPar)));
      
      if(par.setUsers){
        
        descPar = new SSEntityDescriberPar(livingDoc.id);
        
        SSEntity.addEntitiesDistinctWithoutNull(
          livingDoc.users,
          entityServ.entitiesGet(
            new SSEntitiesGetPar(
              par.user,
              sqlFct.getLivingDocUserURIs(livingDoc.id), //entities
              null, //types
              descPar, //descpar
              par.withUserRestriction)));
      }
      
      return livingDoc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void livingDocsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSLivingDocsGetPar par = (SSLivingDocsGetPar) parA.getFromJSON(SSLivingDocsGetPar.class);
    
    sSCon.writeRetFullToClient(SSLivingDocsGetRet.get(livingDocsGet(par)));
  }
  
  @Override
  public List<SSEntity> livingDocsGet(final SSLivingDocsGetPar par) throws Exception{

    try{
      
      final List<SSEntity>   docs        = new ArrayList<>();
      SSLivingDocGetPar      livingDocGetPar;
      
      for(SSUri livingDocUri : sqlFct.getLivingDocURIsForUser(par.user)){
      
        livingDocGetPar =
          new SSLivingDocGetPar(
            par.user,
            livingDocUri,
            par.withUserRestriction,
            par.invokeEntityHandlers);
        
        livingDocGetPar.setUsers = par.setUsers;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          docs,
          livingDocGet(livingDocGetPar));
      }
      
      return docs;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}