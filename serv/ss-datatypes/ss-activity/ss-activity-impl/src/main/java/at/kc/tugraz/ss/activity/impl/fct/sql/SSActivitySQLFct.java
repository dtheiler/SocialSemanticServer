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
package at.kc.tugraz.ss.activity.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.SSActivityContent;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityContentE;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSActivitySQLFct extends SSDBSQLFct{

  public SSActivitySQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public void addActivityContent(
    final SSUri               user,
    final SSUri               activity,
    final SSActivityContentE  contentType,
    final SSActivityContent   content) throws Exception{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.activityId,     activity);
      insert(inserts, SSSQLVarU.contentType,    contentType);
      insert(inserts, SSSQLVarU.content,        content);
      
      dbSQL.insert(activityContentsTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addActivity(
    final SSUri               author,
    final SSUri               activity, 
    final SSActivityE         type, 
    final SSUri               entity, 
    final List<SSUri>         users, 
    final List<SSUri>         entityUris, 
    final List<SSTextComment> textComments) throws Exception{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.activityId,     activity);
      insert(inserts, SSSQLVarU.activityType,   type);
      
      if(entity != null){
        insert(inserts, SSSQLVarU.entityId,   entity);
      }else{
        insert(inserts, SSSQLVarU.entityId,   SSStrU.empty);
      }
      
      if(!textComments.isEmpty()){
        insert(inserts, SSSQLVarU.textComment,   textComments.get(0));
      }else{
        insert(inserts, SSSQLVarU.textComment,   SSStrU.empty);
      }
      
      dbSQL.insert(activityTable, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarU.activityId,     activity);
      insert(inserts, SSSQLVarU.userId,         author);
      
      dbSQL.insert(activityUsersTable, inserts);
        
      for(SSUri user : users){

        inserts.clear();
        insert(inserts, SSSQLVarU.activityId,     activity);
        insert(inserts, SSSQLVarU.userId,         user);
        
        dbSQL.insert(activityUsersTable, inserts);
      }
      
      for(SSUri entityUri : entityUris){

        inserts.clear();
        insert(inserts, SSSQLVarU.activityId,     activity);
        insert(inserts, SSSQLVarU.entityId,       entityUri);
        
        dbSQL.insert(activityEntitiesTable, inserts);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSActivity> getActivities(
    final List<SSUri>       users,
    final List<SSUri>       entities,
    final List<SSActivityE> types,
    final Long              startTime,
    final Long              endTime) throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSActivity>                     activities     = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      Long                                       timestamp;
      SSActivity                                 activityObj;
      SSEntity                                   activityEntity;

      table    (tables,    activityTable);
      table    (tables,    entityTable);
      
      column   (columns,   entityTable,   SSSQLVarU.id);
      column   (columns,   activityTable, SSSQLVarU.activityType);
      column   (columns,   activityTable, SSSQLVarU.entityId);
      column   (columns,   entityTable,   SSSQLVarU.creationTime);
      column   (columns,   entityTable,   SSSQLVarU.author);
      column   (columns,   activityTable, SSSQLVarU.textComment);

      tableCon (tableCons, activityTable, SSSQLVarU.activityId, entityTable, SSSQLVarU.id);
      
      if(
        users != null &&
        !users.isEmpty()){
        
        table    (tables,    activityUsersTable);
        tableCon (tableCons, activityUsersTable,    SSSQLVarU.activityId, entityTable, SSSQLVarU.id);
        
        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        for(SSUri user : users){
          where(whereUsers, activityUsersTable, SSSQLVarU.userId, user);
        }
        
        wheres.add(whereUsers);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){

        table    (tables,    activityEntitiesTable);
        tableCon (tableCons, activityEntitiesTable, SSSQLVarU.activityId, entityTable, SSSQLVarU.id);
      
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, activityEntitiesTable, SSSQLVarU.entityId, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSActivityE type : types){
          where(whereTypes, activityTable, SSSQLVarU.activityType, type);
        }
        
        wheres.add(whereTypes);
      }
      
      if(!wheres.isEmpty()){
        resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      }else{
        resultSet = dbSQL.select(tables, columns, tableCons);
      }
      
      while(resultSet.next()){
        
        timestamp = bindingStrToLong (resultSet, SSSQLVarU.creationTime);
        
        if(
          startTime != null &&
          startTime != 0    &&
          timestamp <= startTime){
          continue;
        }
        
        if(
          endTime != null &&
          endTime != 0    &&
          timestamp >= endTime){
          continue;
        }
        
        if(bindingStrToUri(resultSet, SSSQLVarU.entityId) != null){
          activityEntity =
            SSEntity.get(
              bindingStrToUri(resultSet, SSSQLVarU.entityId),
              SSEntityE.entity);
        }else{
          activityEntity = null;
        }
        
        activityObj = 
          SSActivity.get(
            bindingStrToUri  (resultSet, SSSQLVarU.id), 
            SSActivityE.get(bindingStr(resultSet, SSSQLVarU.activityType)),
            activityEntity);

        activityObj.creationTime   = timestamp;
        activityObj.author         = bindingStrToUri        (resultSet, SSSQLVarU.author);

        activityObj.comments.addAll(
          SSTextComment.asListWithoutNullAndEmpty(
            SSTextComment.get(bindingStr(resultSet, SSSQLVarU.textComment))));
        
        activities.add(activityObj);
      }
      
      return activities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getActivityUsers(final SSUri activity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
    
      final List<String>              tables         = new ArrayList<>();
      final List<String>              columns        = new ArrayList<>();
      final Map<String, String>       wheres         = new HashMap<>();
      final List<String>              tableCons      = new ArrayList<>();
      
      table    (tables,    activityTable);
      table    (tables,    activityUsersTable);
      column   (columns,   SSSQLVarU.userId);
      where    (wheres,    activityTable,        SSSQLVarU.activityId, activity);
      tableCon (tableCons, activityTable,        SSSQLVarU.activityId, activityUsersTable, SSSQLVarU.activityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      return getURIsFromResult(resultSet, SSSQLVarU.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getActivityEntities(final SSUri activity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
    
      final List<String>              tables         = new ArrayList<>();
      final List<String>              columns        = new ArrayList<>();
      final Map<String, String>       wheres         = new HashMap<>();
      final List<String>              tableCons      = new ArrayList<>();
      
      table    (tables,    activityTable);
      table    (tables,    activityEntitiesTable);
      column   (columns,   activityEntitiesTable,  SSSQLVarU.entityId);
      where    (wheres,    activityTable,          SSSQLVarU.activityId, activity);
      tableCon (tableCons, activityTable,          SSSQLVarU.activityId, activityEntitiesTable, SSSQLVarU.activityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      return getURIsFromResult(resultSet, SSSQLVarU.entityId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
