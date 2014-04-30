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

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserEntryAddPar;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.api.*;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUrisUserForTargetGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsUserAllGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserWithEntriesRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsUserAllGetRet;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;
import at.kc.tugraz.ss.service.disc.impl.fct.ue.SSDiscUEFct;
import java.util.*;

public class SSDiscImpl extends SSServImplWithDBA implements SSDiscClientI, SSDiscServerI, SSEntityHandlerImplI {

  protected final List<SSEntityE> supportedEntityTypes;
//  private final SSDiscGraphFct graphFct;
  private final SSDiscSQLFct sqlFct;

  public SSDiscImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception {

    super(conf, dbGraph, dbSQL);

//    graphFct = new SSDiscGraphFct (this);
    sqlFct = new SSDiscSQLFct(this);
    
    supportedEntityTypes = new ArrayList<SSEntityE>();
    supportedEntityTypes.add(SSEntityE.disc);
    supportedEntityTypes.add(SSEntityE.discEntry);
  }

  public List<SSEntityE> getSupportedEntityTypes() throws Exception{
    return supportedEntityTypes;
  }
   
  /* SSEntityHandlerImplI */
  
  @Override
  public List<SSUri> searchWithKeywordWithin(
    final SSUri         userUri,
    final SSUri         entityUri,
    final String        keyword,
    final SSEntityE     entityType) throws Exception{

    return null;
  }

  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE   entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public Boolean shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE   entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE entityType) throws Exception{
    
    return false;
  }
    
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityE                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par) throws Exception{
    
  }

  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityE        entityType,
    final SSUri            userUri,
    final SSUri            entityUri,
    final SSLabel          label,
    final Long             creationTime,
    final List<String>     tags,
    final SSEntityA        overallRating,
    final List<SSUri>      discUris,
    final SSUri            author) throws Exception {

    if (SSEntityE.equals(entityType, SSEntityE.disc)) {

      return SSDiscDesc.get(
        entityUri,
        label,
        creationTime,
        tags,
        overallRating,
        author, 
        discUris);
    }

    if(SSEntityE.equals(entityType, SSEntityE.discEntry)) {

      return SSDiscEntryDesc.get(
        entityUri,
        label,
        creationTime,
        author, 
        overallRating, 
        tags, 
        discUris);
    }

    return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
  }

  /* SSDiscClientI */
  
  @Override
  public void discUserEntryAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception {

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(discUserEntryAdd(parA));
  }

  @Override
  public void discsUserAllGet(final SSSocketCon sSCon, final SSServPar par) throws Exception {

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSDiscsUserAllGetRet.get(discsUserAllGet(par), par.op));
  }

  @Override
  public void discUserWithEntriesGet(final SSSocketCon sSCon, final SSServPar par) throws Exception {

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSDiscUserWithEntriesRet.get(discUserWithEntriesGet(par), par.op));
  }

  /* SSDiscServerI */
  @Override
  public List<SSUri> discUrisUserForTargetGet(final SSServPar parA) throws Exception {

    final SSDiscUrisUserForTargetGetPar par = new SSDiscUrisUserForTargetGetPar(parA);

    try {
      return sqlFct.getDiscUrisForTarget(par.entityUri);
    } catch (Exception error) {
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri discUserRemove(final SSServPar parA) throws Exception {

    final SSDiscUserRemovePar par = new SSDiscUserRemovePar(parA);

    try {
      final SSUri author = SSServCaller.entityAuthorGet(par.user, par.discUri);

      if (!SSUri.equals(author, par.user)) {
        throw new Exception("user is not author of disc");
      }

      dbSQL.startTrans(par.shouldCommit);

      sqlFct.deleteDisc(par.discUri);

      dbSQL.commit(par.shouldCommit);
      
      return par.discUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return discUserRemove(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSDiscUserEntryAddRet discUserEntryAdd(final SSServPar parA) throws Exception {

    final SSDiscUserEntryAddPar par           = new SSDiscUserEntryAddPar(parA);
    SSUri                       discEntryUri  = null;
    SSLabel                  discLabel     = null;
    SSUri                       discUri;

    try{

      discUri = par.disc;
      
      if(
        !par.addNewDisc &&
        par.content == null){
        throw new Exception("pars not valid");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.addNewDisc){
        
        discUri = sqlFct.createDiscUri();
        
        SSServCaller.entityAdd(
          par.user,
          par.target,
          SSLabel.get(par.target.toString()),
          SSEntityE.entity,
          false);
        
        discLabel = SSServCaller.entityLabelGet(par.target);
        
        SSServCaller.entityAdd(
          par.user,
          discUri,
          discLabel,
          SSEntityE.disc,
          false);
      }

      if (par.content != null) {

        discEntryUri = sqlFct.createDiscEntryUri();

        SSServCaller.entityAdd(
          par.user,
          discEntryUri,
          SSLabel.get(discEntryUri.toString()),
          SSEntityE.discEntry,
          false);
      }

      if(par.addNewDisc){
        sqlFct.createDisc(discUri, par.user, par.target, discLabel);
      }

      if(par.content != null){
        sqlFct.addDiscEntry(discEntryUri, par.user, discUri, par.content);
      }

//      SSServCaller.broadCastUpdate(par.user, disc, SSBroadcastEnum.suDiscssion, true);    
      if(par.addNewDisc){
        SSDiscUEFct.discCreate    (par, discUri);
      } else {
        SSDiscUEFct.discEntryAdd  (par, discEntryUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return SSDiscUserEntryAddRet.get(discUri, discEntryUri, par.op);
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return discUserEntryAdd(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSDisc> discsUserAllGet(final SSServPar parA) throws Exception{

    final SSDiscsUserAllGetPar par                 = new SSDiscsUserAllGetPar(parA);
    final List<SSDisc>         discsWithoutEntries = new ArrayList<SSDisc>();

    
    try{

      for(SSUri discUri : sqlFct.getAllDiscUris()){
        discsWithoutEntries.add(sqlFct.getDiscWithoutEntries(discUri));
      }

      return discsWithoutEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSDisc discUserWithEntriesGet(final SSServPar parA) throws Exception{

    final SSDiscUserWithEntriesGetPar par = new SSDiscUserWithEntriesGetPar(parA);

    try{
      return sqlFct.getDiscWithEntries(par.disc);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSDisc> discsUserWithEntriesGet(final SSServPar parA) throws Exception{

    final SSDiscsWithEntriesGetPar par = new SSDiscsWithEntriesGetPar(parA);
    final List<SSDisc> discsWithEntries = new ArrayList<SSDisc>();

    try{

      for(SSDisc disc : SSServCaller.discsUserAllGet(par.user)){

        discsWithEntries.add(
          SSServCaller.discUserWithEntriesGet(
            par.user,
            disc.uri,
            par.maxDiscEntries));
      }

      return discsWithEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
