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
package at.kc.tugraz.ss.service.coll.impl.fct.sql;

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSDBSQLI;

import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.datatypes.SSCollEntry;
import at.kc.tugraz.ss.service.coll.impl.fct.misc.SSCollMiscFct;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCollSQLFct extends SSDBSQLFct{

  public SSCollSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public SSUri addColl(
    final SSUri collUri) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.collId, collUri);
      
      dbSQL.insert(SSSQLVarNames.collTable, inserts);
      
      return collUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void addCollRoot(
    final SSUri   collUri, 
    final SSUri   userUri) throws Exception{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();

      //add coll to coll root table
      insert (inserts, SSSQLVarNames.userId, userUri);
      insert (inserts, SSSQLVarNames.collId, collUri);
      
      dbSQL.insert(SSSQLVarNames.collRootTable, inserts);
      
      //add coll to user coll table
      inserts.clear();
      insert(inserts, SSSQLVarNames.userId, userUri);
      insert(inserts, SSSQLVarNames.collId, collUri);
      
      dbSQL.insert(SSSQLVarNames.collUserTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean isCollRoot(
    final SSUri collUri) throws Exception{
    
    ResultSet  resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collRootTable, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  //TODO dtheiler: special coll could be merged with root coll table
  public void addCollSpecial(
    final SSUri   collUri, 
    final SSUri   userUri) throws Exception{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();

      //add coll to special coll table
      insert(inserts, SSSQLVarNames.userId, userUri);
      insert(inserts, SSSQLVarNames.collId, collUri);
      
      dbSQL.insert(SSSQLVarNames.collSpecialTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean isCollSpecial(
    final SSUri collUri) throws Exception{
    
    ResultSet  resultSet               = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collSpecialTable, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
   public SSUri getCollSpecialURI(
     final SSUri userUri) throws Exception{
    
     ResultSet  resultSet = null;
     
     try{
      
       final List<String>        columns = new ArrayList<>();
       final Map<String, String> wheres  = new HashMap<>();
       
       column(columns, SSSQLVarNames.collId);
       
       where(wheres, SSSQLVarNames.userId, userUri);
       
       resultSet = dbSQL.select(SSSQLVarNames.collSpecialTable, columns, wheres, null, null, null);
       
       checkFirstResult(resultSet);
       
       return bindingStrToUri(resultSet, SSSQLVarNames.collId);
       
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Boolean isColl(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collTable, columns, wheres, null, null, null);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addCollEntry(
    final SSUri      collParent,
    final SSUri      collEntry) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      //add coll entry to coll entry pos table
      Integer collEntryCount = getCollEntryCount(collParent);
      
      collEntryCount++;
      
      insert(inserts, SSSQLVarNames.collId,  collParent);
      insert(inserts, SSSQLVarNames.entryId, collEntry);
      insert(inserts, SSSQLVarNames.pos,     collEntryCount);
      
      dbSQL.insert(SSSQLVarNames.collEntryPosTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public Integer getCollEntryCount(
    final SSUri collUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collEntryPosTable, columns, wheres, null, null, null);
      
      resultSet.last();
      
      return resultSet.getRow();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSColl> getCollsPublic() throws Exception{
    
    ResultSet resultSet = null;
    
    try{
    
      final List<String>        tables      = new ArrayList<>();
      final List<String>        columns     = new ArrayList<>();
      final Map<String, String> wheres      = new HashMap<>();
      final List<String>        tableCons   = new ArrayList<>();
      final List<SSColl>        publicColls = new ArrayList<>();
      SSColl                    collObj;
      
      table(tables, SSSQLVarNames.collTable);
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.circleTable);
      table(tables, SSSQLVarNames.circleEntitiesTable);
      column(columns, SSSQLVarNames.id);
      column(columns, SSSQLVarNames.collId);
      column(columns, SSSQLVarNames.author);
      column(columns, SSSQLVarNames.label);
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.circleType);
      column(columns, SSSQLVarNames.circleTable,          SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.circleEntitiesTable,  SSSQLVarNames.circleId);
      where (wheres,  SSSQLVarNames.circleType, SSCircleE.toStr(SSCircleE.pub));
      
      tableCon(tableCons, SSSQLVarNames.entityTable,         SSSQLVarNames.id, SSSQLVarNames.collTable,   SSSQLVarNames.collId);
      tableCon(tableCons, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId, SSSQLVarNames.circleTable, SSSQLVarNames.circleId);
      tableCon(tableCons, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId, SSSQLVarNames.collTable,   SSSQLVarNames.collId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        collObj = 
          SSColl.get(bindingStrToUri   (resultSet, SSSQLVarNames.id),
            bindingStrToLabel (resultSet, SSSQLVarNames.label));
        
        collObj.author = bindingStrToAuthor   (resultSet, SSSQLVarNames.author);
          
        publicColls.add(collObj);
      }
      
      return publicColls;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  } 
  
  public List<String> getCollURIsForUser(
    final SSUri userUri) throws Exception{
     
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
    
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collUserTable, columns, wheres, null, null, null);      
      
      return getStringsFromResult(resultSet, SSSQLVarNames.collId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }  
  
  public Boolean ownsUserColl(
    final SSUri userUri, 
    final SSUri collUri) throws Exception {
    
    ResultSet resultSet = null;
    
    try{

      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      where(wheres, SSSQLVarNames.collId, collUri);
    
      resultSet = dbSQL.select(SSSQLVarNames.collUserTable, columns, wheres, null, null, null);

      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }  
  
  public void addCollToColl(
    final SSUri       user,
    final SSUri       collParent,
    final SSUri       collChild,
    final Boolean     createdCollIsInSharedOrPublicCircle,
    final Boolean     addedCollIsSharedOrPublic) throws Exception{
    
    try{
      
      if(createdCollIsInSharedOrPublicCircle && addedCollIsSharedOrPublic){
        throw new Exception("cannot add to shared coll and add shared/public coll at the same time");
      }
      
      final Map<String, String> inserts = new HashMap<>();
      
      //add relation of coll parent to child coll to hierarchy table
      inserts.clear();
      insert(inserts, SSSQLVarNames.collParentId, collParent);
      insert(inserts, SSSQLVarNames.collChildId,  collChild);
      
      dbSQL.insert(SSSQLVarNames.collHierarchyTable, inserts);
      
      //add coll child to coll parent in coll entry pos table
      final Integer collEntryCount = getCollEntryCount(collParent) + 1;
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.collId,   collParent);
      insert(inserts, SSSQLVarNames.entryId,  collChild);
      insert(inserts, SSSQLVarNames.pos,      collEntryCount);
      
      dbSQL.insert(SSSQLVarNames.collEntryPosTable, inserts);
      
      //add child coll to user coll table
      inserts.clear();
      insert(inserts, SSSQLVarNames.userId,    user);
      insert(inserts, SSSQLVarNames.collId,    collChild);
      
      dbSQL.insert(SSSQLVarNames.collUserTable, inserts);
      
      //add currently added coll to other users as well
      if(createdCollIsInSharedOrPublicCircle){
        
        inserts.clear();
        insert(inserts, SSSQLVarNames.collId, collChild);
        
        for(SSUri coUserUri : getCollUserURIs(collParent)){
          
          if(SSStrU.equals(coUserUri.toString(), user.toString())){
            continue;
          }
          
          insert(inserts, SSSQLVarNames.userId, coUserUri);
          
          dbSQL.insert(SSSQLVarNames.collUserTable, inserts);
        }
      }
      
      //add sub colls of shared / pub coll for this user as well
      if(addedCollIsSharedOrPublic){
        
        final List<String> subCollUris = new ArrayList<>();
        
        SSCollMiscFct.getAllChildCollURIs(this, collChild.toString(), collChild.toString(), subCollUris);
        
        inserts.clear();
        insert(inserts, SSSQLVarNames.userId, user);
        
        for(String subCollUri : subCollUris){
          
          insert(inserts, SSSQLVarNames.collId, subCollUri);
          
          dbSQL.insert(SSSQLVarNames.collUserTable, inserts);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void unlinkCollAndSubColls(
    final SSUri userUri,
    final SSUri parentCollUri,
    final SSUri collUri) throws Exception{
    
    try{
      final Map<String, String> deletes     = new HashMap<>();
      final List<String>        subCollUris = new ArrayList<>();
      
      //remove sub colls of followed coll from user coll table as well
      SSCollMiscFct.getAllChildCollURIs(this, collUri.toString(), collUri.toString(), subCollUris);
      
      deletes.clear();
      delete(deletes, SSSQLVarNames.userId, userUri);
      
      for(String subCollUri : subCollUris){
        
        delete(deletes, SSSQLVarNames.collId, subCollUri);
        
        dbSQL.delete(SSSQLVarNames.collUserTable, deletes);
      }
      
      //remove coll from user coll table
      deletes.clear();
      delete(deletes, SSSQLVarNames.userId,     userUri);
      delete(deletes, SSSQLVarNames.collId,     collUri);
      
      dbSQL.delete(SSSQLVarNames.collUserTable, deletes);
      
      //remove coll from coll hierarchy table
      deletes.clear();
      delete(deletes, SSSQLVarNames.collParentId, parentCollUri);
      delete(deletes, SSSQLVarNames.collChildId,  collUri);
      
      dbSQL.delete(SSSQLVarNames.collHierarchyTable, deletes);
      
      //remove coll from coll entries pos table
      deletes.clear();
      delete(deletes, SSSQLVarNames.collId,   parentCollUri.toString());
      delete(deletes, SSSQLVarNames.entryId,  collUri.toString());
      
      dbSQL.delete(SSSQLVarNames.collEntryPosTable, deletes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<String> getDirectChildCollURIs(
    final String collUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collChildId);
      
      where(wheres, SSSQLVarNames.collParentId, collUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collHierarchyTable, columns, wheres, null, null, null);
      
      return getStringsFromResult(resultSet, SSSQLVarNames.collChildId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<String> getDirectParentCollURIs(
    final String collUri) throws Exception{

    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collParentId);
      
      where(wheres, SSSQLVarNames.collChildId, collUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collHierarchyTable, columns, wheres, null, null, null);
      
      return getStringsFromResult(resultSet, SSSQLVarNames.collParentId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void removeCollAndUnlinkSubColls(
    final SSUri userUri,
    final SSUri collUri) throws Exception{
    
    try{
      
      final Map<String, String> deletes             = new HashMap<>();
      final List<String>        directChildCollURIs = getDirectChildCollURIs(collUri.toString());
      
      //unlink all direct sub colls (and hence their sub colls as well)
      for(String subCollUri : directChildCollURIs){
        unlinkCollAndSubColls(userUri, collUri, SSUri.get(subCollUri));
      }
      
      deletes.clear();
      delete(deletes, SSSQLVarNames.id, collUri);
      dbSQL.delete(SSSQLVarNames.entityTable, deletes);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCollEntry(
    final SSUri collUri,
    final SSUri collEntryUri) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();
      
      //remove coll entry from coll entry pos table
      delete(deletes, SSSQLVarNames.collId,  collUri);
      delete(deletes, SSSQLVarNames.entryId, collEntryUri);
      
      dbSQL.delete(SSSQLVarNames.collEntryPosTable, deletes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void updateCollEntriesPos(
    final SSUri         collUri, 
    final List<SSUri>   collEntries,
    final List<Integer> order) throws Exception{
    
    try{
      final Map<String, String> wheres   = new HashMap<>();
      final Map<String, String> updates  = new HashMap<>();
      Integer                   counter  = 0;
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      while(counter < collEntries.size()){
        
        where  (wheres,  SSSQLVarNames.entryId, collEntries.get(counter));
        update (updates, SSSQLVarNames.pos,     order.get      (counter));
        
        counter++;
        
        dbSQL.update(SSSQLVarNames.collEntryPosTable, wheres, updates);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private SSColl getColl(
    final SSUri           collUri,
    final List<SSCircleE> circleTypes) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        tables     = new ArrayList<>();
      final List<String>        columns    = new ArrayList<>();
      final List<String>        tableCons  = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      final SSColl              collObj;
      
      table    (tables, SSSQLVarNames.collTable);
      table    (tables, SSSQLVarNames.entityTable);
      column   (columns,   SSSQLVarNames.collId);
      column   (columns,   SSSQLVarNames.author);
      column   (columns,   SSSQLVarNames.label);
      where    (wheres,    SSSQLVarNames.collId, collUri);
      tableCon (tableCons, SSSQLVarNames.collTable, SSSQLVarNames.collId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      collObj =
        SSColl.get(collUri,
          bindingStrToLabel (resultSet, SSSQLVarNames.label));
      
      collObj.author = bindingStrToAuthor   (resultSet, SSSQLVarNames.author);
      
      collObj.circleTypes.addAll(circleTypes);
      
      return collObj;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSColl getCollWithEntries(
    final SSUri                 collUri,
    final List<SSCircleE>       circleTypes) throws Exception{

    ResultSet  resultSet = null;
    
    try{
      
      final List<String>        tables    = new ArrayList<>();
      final List<String>        columns   = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final SSColl              coll      = getColl(collUri, circleTypes);
      SSCollEntry               collEntry;

      table    (tables, SSSQLVarNames.collEntryPosTable);
      table    (tables, SSSQLVarNames.entityTable);
      column   (columns,   SSSQLVarNames.entryId);
      column   (columns,   SSSQLVarNames.pos);
      column   (columns,   SSSQLVarNames.label);
      column   (columns,   SSSQLVarNames.type);
      where    (wheres,    SSSQLVarNames.collId,  coll.id);
      tableCon (tableCons, SSSQLVarNames.collEntryPosTable, SSSQLVarNames.entryId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, SSSQLVarNames.pos, "ASC", null);
      
      while(resultSet.next()){
        
        collEntry =
          SSCollEntry.get(bindingStrToUri        (resultSet, SSSQLVarNames.entryId),
            bindingStr             (resultSet, SSSQLVarNames.label),
            new ArrayList<>(),
            bindingStrToInteger    (resultSet, SSSQLVarNames.pos),
            bindingStrToEntityType (resultSet, SSSQLVarNames.type));
        
        coll.entries.add(collEntry);
      }
      
      return coll;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSUri getRootCollURIForUser(
    final SSUri userUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();

      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collRootTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);

      return bindingStrToUri(resultSet, SSSQLVarNames.collId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Boolean containsCollEntry(
    final SSUri collUri, 
    final SSUri collEntryUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId,  collUri);
      where(wheres, SSSQLVarNames.entryId, collEntryUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collEntryPosTable, columns, wheres, null, null, null);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Boolean existsCollRootForUser(
    final SSUri userUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();

      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet  = dbSQL.select(SSSQLVarNames.collRootTable, columns, wheres, null, null, null);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getCollUserURIs(
    final SSUri collUri) throws Exception{

    ResultSet resultSet   = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();

      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collUserTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<String> getCollURIsContainingEntity(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.entryId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.collEntryPosTable, columns, wheres, null, null, null);
      
      return getStringsFromResult(resultSet, SSSQLVarNames.collId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}

//  public SSSpaceEnum getUserCollSpace(SSUri user, SSUri coll) throws Exception{
//    
//    Map<String, String> selectPars    = new HashMap<>();
//    ResultSet           resultSet     = null;
//    SSSpaceEnum         userCollSpace = null;
//    
//    selectPars.put(SSSQLVarU.userId, user.toString());
//    selectPars.put(SSSQLVarU.collId, coll.toString());
//    
//    try{
//      resultSet = dbSQL.dbSQLSelectAllWhere(collUserTable, selectPars);
//      
//      resultSet.first();
//      
//      userCollSpace = SSSpaceEnum.get(resultSet.getString(SSSQLVarU.collSpace));
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.dbSQLCloseStmt(resultSet);
//    }
//    
//    return userCollSpace;
//  }

//private Boolean ownsUserCollByHierarchy(SSUri userUri, SSUri collUri) throws Exception{
//    
//    final List<String> collParents    = new ArrayList<>();
//    final List<String> newCollParents = new ArrayList<>();
//    
//    try{
//      
//      collParents.addAll(getCollDirectParentURIs(collUri));
//      
//      while(true){
//        
//        for(String parentCollUri : collParents){
//          
//          for(String tmpUri : getCollDirectParentURIs(SSUri.get(parentCollUri))){
//          
//            if(!newCollParents.contains(tmpUri)){
//              newCollParents.add(tmpUri);
//            }
//          }
//        }
//        
//        if(newCollParents.isEmpty()){
//          return false;
//        }
//        
//        for(String newCollParentUri : newCollParents){
//          
//          if(ownsUserColl(userUri, SSUri.get(newCollParentUri))){
//            return true;
//          }
//        }
//        
//        collParents.clear();
//        collParents.addAll(newCollParents);
//        newCollParents.clear();
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public SSSpaceEnum getCollSpace(SSUri userUri, SSUri collUri) throws Exception{
//    
//    if(
//      userUri      == null ||
//      collUri      == null){
//      SSServErrReg.regErrThrow(new Exception("parameter(s) null"));
//      return null;
//    }
//        
//    Map<String, String> selectPars  = new HashMap<>();
//    ResultSet           resultSet   = null;
//    SSSpaceEnum         collSpace   = null;
//    
//    selectPars.put(SSSQLVarU.userId, userUri.toString());
//    selectPars.put(SSSQLVarU.collId, collUri.toString());
//    
//    try{
//      
//      resultSet = dbSQL.dbSQLSelectAllWhere(collUserTable, selectPars);
//      
//      if(!resultSet.first()){
//        SSServErrReg.regErrThrow(new Exception("coll-user combination does not exist"));
//      }
//
//      collSpace = bindingStrToSpace(resultSet, SSSQLVarU.collSpace);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.dbSQLCloseStmt(resultSet);
//    }
//    
//    return collSpace;
//  }

// public Boolean isEntityInPrivateUserColl(SSUri user, SSUri entity) throws Exception{
//
//    Map<String, String> selectPars                = new HashMap<>();
//    ResultSet           resultSet                 = null;
//    List<String>        parentCollUris            = new ArrayList<>();
//    Boolean             isEntityInPrivateUserColl;
//    
//    if(isColl(entity)){
//
//      selectPars.put(SSSQLVarU.userId, user.toString());
//      selectPars.put(SSSQLVarU.collId, entity.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(resultSet.first()){
//          isEntityInPrivateUserColl = SSSpaceEnum.isPrivate(bindingStrToSpace(resultSet, SSSQLVarU.collSpace));
//          return isEntityInPrivateUserColl;
//        }else{
//          return false;
//        }
//        
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    selectPars = new HashMap<>();
//    selectPars.put(SSSQLVarU.entryId, entity.toString());
//    
//    try{
//      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
//      
//      while(resultSet.next()){
//        parentCollUris.add(resultSet.getString(SSSQLVarU.collId));
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    if(parentCollUris.isEmpty()){
//      return null;
//    }
//    
//    selectPars = new HashMap<>();
//    
//    for(String parentCollUri : parentCollUris){
//      
//      selectPars.put(SSSQLVarU.collId, parentCollUri.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(
//          resultSet.next() &&
//          SSSpaceEnum.isPrivate(bindingStrToSpace(resultSet, SSSQLVarU.collSpace))){
//          return true;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    return false;
//  }

//public Boolean isEntityInSharedOrFollowedUserColl(final SSUri user, SSUri entity) throws Exception{
//
//    Map<String, String> selectPars;
//    ResultSet           resultSet                            = null;
//    List<String>        parentCollUris                       = new ArrayList<>();
//    Boolean             isEntityInSharedOrFollowedUserColl;
//    
//    if(isColl(entity)){
//
//      selectPars = new HashMap<>();
//      selectPars.put(SSSQLVarU.userId, user.toString());
//      selectPars.put(SSSQLVarU.collId, entity.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(resultSet.first()){
//          isEntityInSharedOrFollowedUserColl = SSSpaceEnum.isSharedOrFollow(bindingStrToSpace(resultSet, SSSQLVarU.collSpace));
//          return isEntityInSharedOrFollowedUserColl;
//        }else{
//          return false;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    selectPars = new HashMap<>();
//    selectPars.put(SSSQLVarU.entryId, entity.toString());
//    
//    try{
//      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
//      
//      while(resultSet.next()){
//        parentCollUris.add(resultSet.getString(SSSQLVarU.collId));
//      }
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    if(parentCollUris.isEmpty()){
//      return null;
//    }
//    
//    selectPars = new HashMap<>();
//    
//    for(String parentCollUri : parentCollUris){
//      
//      selectPars.put(SSSQLVarU.collId, parentCollUri.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(
//          resultSet.next() &&
//          SSSpaceEnum.isSharedOrFollow(bindingStrToSpace(resultSet, SSSQLVarU.collSpace))){
//          return true;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    return false;
//  }

//  public Boolean newIsEntityInPrivateUserColl(final SSUri userUri, final SSUri entityUri) throws Exception{
//    
//    if(SSObjU.isNull(userUri, entityUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        collUris;
//    SSColl                    coll;
//      
//    try{
//      collUris = getCollUrisContainingEntity(entityUri);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//    
//    if(collUris.isEmpty()){
//      return false;
//    }
//    
//    for(String collUri : collUris){
//      
//      try{
//        coll = getUserColl(userUri, SSUri.get(collUri));
//      }catch(Exception error){
//        continue;
//      }
//      
//      if(SSSpaceEnum.isPrivate(coll.space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
//  public Boolean newIsEntityInSharedOrFollowedUserColl(final SSUri userUri, final SSUri entityUri) throws Exception{
//    
//    if(SSObjU.isNull(userUri, entityUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        collUris;
//    final List<String>        userCollUris;
//    SSColl                    coll;
//    
//    try{
//      userCollUris  = getAllUserCollURIs(userUri);
//      
//      if(userCollUris.isEmpty()){
//        return false;
//      }
//      
//      collUris      = getCollUrisContainingEntity(entityUri);
//      
//      if(collUris.isEmpty()){
//        return false;
//      }
//      
//      for(String userCollUri : userCollUris){
//        
//        if(!collUris.contains(userCollUri)){
//          continue;
//        }
//        
//        coll = getUserColl(userUri, SSUri.get(userCollUri));
//        
//        if(SSSpaceEnum.isSharedOrFollow(coll.space)){
//          return true;
//        }
//      }
//      
//      return false;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public void removeColl(
//    final SSUri collUri) throws Exception{
//    
//    if(collUri == null){
//      SSServErrReg.regErrThrow(new Exception("colluri null"));
//      return;
//    }
//    
//    final List<String>        subCollUris = new ArrayList<>();
//    final Map<String, String> deletePars  = new HashMap<>();
//    
//    try{
//      
//      //retrieve all sub coll uris
//      getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
//          
//      //remove all sub colls
//      for(String subCollUri : subCollUris){
//        
//        deletePars.clear();
//        deletePars.put(SSSQLVarU.collId, subCollUri);
//        
//        dbSQL.deleteWhere(collTable, deletePars);
//        
//        deletePars.clear();
//        deletePars.put(SSSQLVarU.entryId, subCollUri);
//        
//        dbSQL.deleteWhere(collEntryPosTable, deletePars);
//      }
//      
//      deletePars.clear();
//      deletePars.put(SSSQLVarU.collId, SSStrU.toStr(collUri));
//
//      dbSQL.deleteWhere(collTable, deletePars);
//      
//      deletePars.clear();
//      deletePars.put(SSSQLVarU.entryId, SSStrU.toStr(collUri));
//
//      dbSQL.deleteWhere(collEntryPosTable, deletePars);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }

//  public Boolean ownsUserASubColl(
//    final SSUri       userUri, 
//    final SSUri       collUri,
//    final SSSpaceEnum space) throws Exception{
//    
//    if(SSObjU.isNull(userUri, collUri, space)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//        
//    final List<String> subCollUris    = new ArrayList<>();
//    
//    getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
//    
//    for(String subCollUri : subCollUris){
//      
//      if(ownsUserColl(userUri, SSUri.get(subCollUri), space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
  //  public Boolean ownsUserASuperColl(
//    final SSUri       userUri, 
//    final SSUri       collUri,
//    final SSSpaceEnum space) throws Exception{
//    
//    if(SSObjU.isNull(userUri, collUri, space)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//        
//    final List<String> superCollUris = new ArrayList<>();
//    
//    getAllParentCollURIs(collUri.toString(), collUri.toString(), superCollUris);
//    
//    for(String parentCollUri : superCollUris){
//      
//      if(ownsUserColl(userUri, SSUri.get(parentCollUri), space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
//  public Boolean ownsUserASuperColl(
//    final SSUri userUri, 
//    final SSUri collUri) throws Exception{
//    
//    if(SSObjU.isNull(userUri, collUri)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//        
//    final List<String> superCollUris = new ArrayList<>();
//    
//    getAllParentCollURIs(collUri.toString(), collUri.toString(), superCollUris);
//    
//    for(String parentCollUri : superCollUris){
//      
//      if(ownsUserColl(userUri, SSUri.get(parentCollUri))){
//        return true;
//      }
//    }
//    
//    return false;
//  }

//  private Boolean ownsUserColl(
//    final SSUri       userUri, 
//    final SSUri       collUri, 
//    final SSSpaceEnum space) throws Exception{
//    
//    
//    if(SSObjU.isNull(userUri, collUri, space)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//    
//    final Map<String, String> whereParNamesWithValues  = new HashMap<>();
//    ResultSet                 resultSet                = null;
//    
//    try{
//      whereParNamesWithValues.put(SSSQLVarU.userId,    userUri.toString());
//      whereParNamesWithValues.put(SSSQLVarU.collId,    collUri.toString());
//      whereParNamesWithValues.put(SSSQLVarU.collSpace, space.toString());
//      
//      resultSet = dbSQL.selectAllWhere(collUserTable, whereParNamesWithValues);
//      
//      return resultSet.first();
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    //select * from entity, circleUser, circleEntity, cirlce where 
//    //id = entityUri, circleUser = userUri, circleType = private and id=entityId, circle.circleId=circlecircleId
//    //select * from entity, circleUser, circleEntity, cirlce where id = entityUri, circleUser = userUri, circleType != private;
//  }
//  public List<SSUri> getAllSharedCollURIs() throws Exception{
//    
//    final List<SSUri>         sharedCollURIs          = new ArrayList<>();
//    final List<String>        columnNames             = new ArrayList<>();
//    final Map<String, String> whereParNamesWithValues = new HashMap<>();
//    ResultSet                 resultSet               = null;
//    
//    //get all colls from user table where space is shared (distinct)
//    columnNames.add             (SSSQLVarU.collId);
//    whereParNamesWithValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
//    
//    try{
//      resultSet = dbSQL.selectCertainDistinctWhere(collUserTable, columnNames, whereParNamesWithValues);
//
//      while(resultSet.next()){
//        sharedCollURIs.add(bindingStrToUri(resultSet, SSSQLVarU.collId));
//      }
//      
//      return sharedCollURIs;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }

//  public void shareCollAndSubColls(
//    final SSUri collUri) throws Exception{
//    
//    if(SSObjU.isNull(collUri)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return;
//    }
//  
//    final Map<String, String> updatePars = new HashMap<>();
//    final Map<String, String> newValues  = new HashMap<>();
//    final List<String>        subCollUris = new ArrayList<>();
//    
//    getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
//    
//    //set space in user coll table for each sub coll to shared
//    for(String subCollUri : subCollUris){
//      
//      updatePars.put(SSSQLVarU.collId,    subCollUri.toString());
//      newValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
//      
//      dbSQL.updateWhere(collUserTable, updatePars, newValues);
//    }
//    
//    //set space in user coll table for coll to shared
//    updatePars.put(SSSQLVarU.collId,    collUri.toString());
//    newValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
//    
//    dbSQL.updateWhere(collUserTable, updatePars, newValues);
//  }


//public SSColl getColl(
//    final SSUri collUri) throws Exception{
//   
//    if(SSObjU.isNull(collUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        tableNames              = new ArrayList<>();
//    final List<String>        columnNames             = new ArrayList<>();
//    final Map<String, String> whereParNamesWithValues = new HashMap<>();
//    SSColl                    coll                    = null;
//    ResultSet                 resultSet               = null;
//    
//    try{
//      tableNames.add              (collTable);
//      tableNames.add              (entityTable);
//      columnNames.add             (SSSQLVarU.collId);
//      columnNames.add             (SSSQLVarU.author);
//      columnNames.add             (SSSQLVarU.label);
//      whereParNamesWithValues.put (SSSQLVarU.collId, collUri.toString());
//      
//      resultSet =
//        dbSQL.selectCertainWhere(
//        tableNames,
//        columnNames,
//        whereParNamesWithValues,
//        SSSQLVarU.collId + SSStrU.equal + SSSQLVarU.id);
//      
//      resultSet.first();
//      
//      coll = 
//        SSColl.get(
//        collUri,
//        null, 
//        bindingStrToUri  (resultSet, SSSQLVarU.author), 
//        bindingStr       (resultSet, SSSQLVarU.label), 
//        null);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return coll;
//  }