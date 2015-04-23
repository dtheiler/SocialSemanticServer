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
package at.kc.tugraz.sss.app.impl;

import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.sss.app.api.SSAppServerI;
import at.kc.tugraz.sss.app.api.SSAppClientI;
import at.kc.tugraz.sss.app.datatypes.SSApp;
import at.kc.tugraz.sss.app.datatypes.par.SSAppAddPar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppsGetPar;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppAddRet;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppsGetRet;
import at.kc.tugraz.sss.app.impl.fct.sql.SSAppSQLFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;

public class SSAppImpl extends SSServImplWithDBA implements SSAppClientI, SSAppServerI, SSEntityDescriberI{
  
  private final SSAppSQLFct sqlFct;
  
  public SSAppImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct = new SSAppSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{
    
    try{
      return par.entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void appAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSAppAddRet.get(appAdd(parA), parA.op), parA.op);
  }

  @Override
  public SSUri appAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSAppAddPar par    = new SSAppAddPar(parA);
      final SSUri       appUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPubCircleAdd(
        par.user, 
        appUri, 
        SSEntityE.app, 
        par.label, 
        null, 
        null, 
        false);
      
      sqlFct.createApp(
        appUri,
        par.descriptionShort, 
        par.descriptionFunctional, 
        par.descriptionTechnical, 
        par.descriptionInstall, 
        par.downloadIOS,
        par.downloadAndroid,
        par.fork);
      
      for(SSUri download : par.downloads){
      
        SSServCaller.entityEntityToPubCircleAdd(
          par.user, 
          download, 
          SSEntityE.entity, 
          null, 
          null, 
          null, 
          false);
      }
      
      for(SSUri screenShot : par.screenShots){
      
        SSServCaller.entityEntityToPubCircleAdd(
          par.user, 
          screenShot, 
          SSEntityE.image, 
          null, 
          null, 
          null, 
          false);
      }
      
      SSServCaller.entityUpdate(
        par.user,
        appUri,
        null,
        null,
        SSTextComment.asListWithoutNullAndEmpty(),
        par.downloads,
        par.screenShots,
        SSUri.asListWithoutNullAndEmpty(),
        par.videos,
        false);
      
      dbSQL.commit(par.shouldCommit);
      
      return appUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appAdd(parA);
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
  public void appsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSAppsGetRet.get(appsGet(parA), parA.op), parA.op);
  }
  
  @Override
  public List<SSApp> appsGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSAppsGetPar par  = new SSAppsGetPar(parA);
      final List<SSApp>  apps = new ArrayList<>();
      SSApp              entity;
      
      for(SSApp app : sqlFct.getApps()){

        entity =
          SSApp.get(
            app,
            SSServCaller.entityDescGet(
              par.user,
              app.id,
              true,
              true,
              false,
              false,
              false,
              false,
              false));
        
        entity.downloads.addAll(
          SSServCaller.entityDownloadURIsGet(
            par.user, 
            app.id));
        
        entity.attachedEntities.addAll(
          SSServCaller.entityScreenShotsGet(
            par.user, 
            app.id));
        
        try{
          
          entity.attachedEntities.addAll(
            SSServCaller.videosUserGet(
              par.user,
              null,
              app.id));
          
        }catch(SSErr error){
          
          switch(error.code){
            case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
            default: SSServErrReg.regErrThrow(error);
          }
        }
         
         apps.add(entity);
      }
      
      return apps;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}