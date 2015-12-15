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
package at.tugraz.sss.serv;

import com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class SSDBSQLMySQLImpl extends SSServImplDBA implements SSDBSQLI{
  
  private static DataSource   connectionPool           = null;
  public         Connection   connector                = null;
  private        Boolean      gotCon                   = false;
  private        Integer      numberTimesTriedToGetCon = 0;
  
  public SSDBSQLMySQLImpl(final SSConfA conf) throws SSErr {
    
    super(conf);
    
    connectToMYSQL();
  }
  
  @Override
  public Integer getActive() {
    return connectionPool.getActive();
  }
  
  @Override
  public Integer getMaxActive(){
    return connectionPool.getMaxActive();
  }
  
  @Override
  public ResultSet select(final String query) throws SSErr {
    
    try{
      final Statement stmt = connector.createStatement();
      
      return stmt.executeQuery(query);
      
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
      return null;
    }
  }

  @Override
  public ResultSet select(final SSDBSQLSelectPar par) throws SSErr{
    
    try{
      String                                    query   = "SELECT DISTINCT "; //caution do not remove distinct here without checks
      PreparedStatement                         stmt;
      Iterator<Map.Entry<String, List<String>>> iteratorMultiValue;
      Map.Entry<String, List<String>>           entrySet;
      String                                    comparator;
      Integer                                   counter  = 1;
      
      for(String columnName : par.columns){
        query += columnName + SSStrU.comma;
      }
      
      if(par.columns.isEmpty()){
        query += "*";
      }
      
      query = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM ";
      
      for(String tableName : par.tables){
        query += tableName + SSStrU.comma;
      }
      
      if(
        par.orWheres.isEmpty() &&
        par.tableCons.isEmpty()){
        query = SSStrU.removeTrailingString(query, SSStrU.comma);
      }else{
        query = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
      }
      
      for(MultivaluedMap<String, String> where : par.orWheres){
        
        query += "(";
        
        iteratorMultiValue = where.entrySet().iterator();
        
        while(iteratorMultiValue.hasNext()){
          
          entrySet = iteratorMultiValue.next();
          
          for(String value : entrySet.getValue()){
            query += entrySet.getKey() + SSStrU.equal + SSStrU.questionMark + SSStrU.valueBlankORBlank;
          }
        }
        
        query = SSStrU.removeTrailingString(query, SSStrU.valueBlankORBlank) + ")" + SSStrU.blank + par.globalSearchOp + SSStrU.blank;
      }
      
      for(MultivaluedMap<String, String> where : par.andWheres){
        
        query += "(";
        
        iteratorMultiValue = where.entrySet().iterator();
        
        while(iteratorMultiValue.hasNext()){
          
          entrySet = iteratorMultiValue.next();
          
          for(String value : entrySet.getValue()){
            query += entrySet.getKey() + SSStrU.equal + SSStrU.questionMark + SSStrU.valueBlankANDBlank;
          }
        }
        
        query = SSStrU.removeTrailingString(query, SSStrU.valueBlankANDBlank) + ")" + SSStrU.blank + par.globalSearchOp + SSStrU.blank;
      }
      
      if(!par.matches.isEmpty()){
        
        if(
          par.requireds.isEmpty() &&
          par.absents.isEmpty()   &&
          par.eithers.isEmpty()){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        //      SELECT * FROM articles
//    -> WHERE MATCH (title,body)
//    -> AGAINST ('database' IN NATURAL LANGUAGE MODE);
        
        query += " MATCH (";
        
        for(String match : par.matches){
          query += match + SSStrU.comma;
        }
        
        query = SSStrU.removeTrailingString(query, SSStrU.comma) + ") AGAINST ('";
        
        for(String required : par.requireds){
          query += "+" + required + "*" + SSStrU.blank;
        }
        
        for(String absent : par.absents){
          query += "-" + absent + "*" + SSStrU.blank;
        }
        
        for(String either : par.eithers){
          query += either + "*" + SSStrU.blank;
        }
        
        query = SSStrU.removeTrailingString(query, SSStrU.blank) + "' IN BOOLEAN MODE)" + SSStrU.blank;
      }
      
      for(Map.Entry<String, List<MultivaluedMap<String, String>>> wheresNumberic : par.numbericWheres.entrySet()){
        
        comparator = wheresNumberic.getKey();
        
        for(MultivaluedMap<String, String> whereNumeric : wheresNumberic.getValue()){
          
          query += "(";
          
          iteratorMultiValue = whereNumeric.entrySet().iterator();
          
          while(iteratorMultiValue.hasNext()){
            
            entrySet = iteratorMultiValue.next();
            
            for(String value : entrySet.getValue()){
              query += entrySet.getKey() + comparator + SSStrU.questionMark + SSStrU.valueBlankORBlank;
            }
          }
          
          query = SSStrU.removeTrailingString(query, SSStrU.valueBlankORBlank) + ")" + SSStrU.blank + par.globalSearchOp + SSStrU.blank;
        }
      }
      
      query = SSStrU.removeTrailingString(query, SSStrU.blank + par.globalSearchOp + SSStrU.blank);
      
      if(
        (!par.orWheres.isEmpty() || !par.andWheres.isEmpty() || !par.numbericWheres.isEmpty() || !par.matches.isEmpty()) &&
        !par.tableCons.isEmpty()){
        
        query += SSStrU.valueBlankANDBlank;
      }
      
      for(String tableCon : par.tableCons){
        query += tableCon + SSStrU.valueBlankANDBlank;
      }
      
      query = SSStrU.removeTrailingString(query, SSStrU.valueBlankANDBlank);
      
      if(
        par.orderByColumn != null &&
        par.sortType      != null){
        
        query += " ORDER BY " + par.orderByColumn + SSStrU.blank + par.sortType;
      }
      
      if(par.limit != null){
        query += " LIMIT " + par.limit;
      }
      
      stmt = connector.prepareStatement(query);
      
      for(MultivaluedMap<String, String> where : par.orWheres){
        
        iteratorMultiValue = where.entrySet().iterator();
        
        while(iteratorMultiValue.hasNext()){
          
          entrySet = iteratorMultiValue.next();
          
          for(String value : entrySet.getValue()){
            stmt.setObject(counter++, value);
          }
        }
      }
      
      for(MultivaluedMap<String, String> where : par.andWheres){
        
        iteratorMultiValue = where.entrySet().iterator();
        
        while(iteratorMultiValue.hasNext()){
          
          entrySet = iteratorMultiValue.next();
          
          for(String value : entrySet.getValue()){
            stmt.setObject(counter++, value);
          }
        }
      }
      
      for(Map.Entry<String, List<MultivaluedMap<String, String>>> wheresNumberic : par.numbericWheres.entrySet()){
        
        for(MultivaluedMap<String, String> whereNumeric : wheresNumberic.getValue()){
          
          iteratorMultiValue = whereNumeric.entrySet().iterator();
          
          while(iteratorMultiValue.hasNext()){
            
            entrySet = iteratorMultiValue.next();
            
            for(String value : entrySet.getValue()){
              stmt.setObject(counter++, value);
            }
          }
        }
      }
      
      return stmt.executeQuery();
    
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
      return null;
    }
  }
  
  @Override
  public ResultSet select(
    final List<String>        tables,
    final List<String>        columns,
    final Map<String, String> wheres,
    final List<String>        tableCons,
    final String              orderByColumn,
    final String              sortType,
    final Integer             limit) throws SSErr{
    
    try{
      String                              query   = "SELECT DISTINCT "; //caution do not remove distinct here without checks
      PreparedStatement                   stmt;
      Iterator<Map.Entry<String, String>> iterator;
      int                                 counter = 1;
      
      for(String columnName : columns){
        query += columnName + SSStrU.comma;
      }
      
      if(
        columns == null ||
        columns.isEmpty()){
        
        query += "*";
      }
      
      query = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM ";
      
      for(String tableName : tables){
        query += tableName + SSStrU.comma;
      }
      
      query         = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
      
      if(
        wheres != null &&
        !wheres.isEmpty()){
        
        iterator      = wheres.entrySet().iterator();
        
        while(iterator.hasNext()){
          query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
        }
        
        query          = SSStrU.removeTrailingString(query, " AND ");
      }
      
      if(!wheres.isEmpty()){
        query += " AND ";
      }
      
      for(String tableCon : tableCons){
        query += tableCon + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      
      if(
        orderByColumn != null &&
        sortType      != null){
        query         += " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
      }
      
      if(limit != null){
        query += " LIMIT " + limit;
      }
      
      stmt           = connector.prepareStatement(query);
      
      if(
        wheres != null &&
        !wheres.isEmpty()){
        iterator       = wheres.entrySet().iterator();
        
        while(iterator.hasNext()){
          stmt.setObject(counter++, iterator.next().getValue());
        }
      }
      
      return stmt.executeQuery();
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
      return null;
    }
  }
  
  @Override
  public ResultSet select(
    final String              table,
    final List<String>        columns,
    final Map<String, String> wheres,
    final String              orderByColumn,
    final String              sortType,
    final Integer             limit) throws SSErr{
    
    try{
      String                              query   = "SELECT DISTINCT ";
      int                                 counter = 1;
      PreparedStatement                   stmt;
      Iterator<Map.Entry<String, String>> iterator;
      
      for(String certain : columns){
        query += certain + SSStrU.comma + SSStrU.blank;
      }
      
      if(
        columns == null ||
        columns.isEmpty()){
        
        query += "*";
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma + SSStrU.blank) + " FROM " + table;
      
      if(
        wheres != null &&
        !wheres.isEmpty()){
        
        query += " WHERE ";
        
        iterator      = wheres.entrySet().iterator();
        
        while(iterator.hasNext()){
          query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
        }
        
        query          = SSStrU.removeTrailingString(query, " AND ");
      }
      
      if(
        orderByColumn != null &&
        sortType      != null){
        query         += " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
      }
      
      if(limit != null){
        query += " LIMIT " + limit;
      }
      
      stmt           = connector.prepareStatement(query);
      
      if(
        wheres != null &&
        !wheres.isEmpty()){
        
        iterator       = wheres.entrySet().iterator();
        
        while(iterator.hasNext()){
          stmt.setObject(counter++, iterator.next().getValue());
        }
      }
      
      return stmt.executeQuery();
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
      return null;
    }
  }
  
  @Override
  public ResultSet selectLike(
    final List<String>                         tables,
    final List<String>                         columns,
    final List<MultivaluedMap<String, String>> likes,
    final List<String>                         tableCons,
    final String                               orderByColumn,
    final String                               sortType,
    final Integer                              limit) throws SSErr{
    
    try{
      String                                    query   = "SELECT DISTINCT "; //caution do not remove distinct here without checks
      int                                       counter = 1;
      PreparedStatement                         stmt;
      Iterator<Map.Entry<String, List<String>>> iteratorMultiValue;
      Map.Entry<String, List<String>>           entrySet;
      
      for(String columnName : columns){
        query += columnName + SSStrU.comma;
      }
      
      if(
        columns == null ||
        columns.isEmpty()){
        
        query += "*";
      }
      
      query = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM ";
      
      for(String tableName : tables){
        query += tableName + SSStrU.comma;
      }
      
      if(
        likes.isEmpty() &&
        tableCons.isEmpty()){
        query = SSStrU.removeTrailingString(query, SSStrU.comma);
      }else{
        query = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
      }
      
      for(MultivaluedMap<String, String> like : likes){
        
        query += "(";
        
        iteratorMultiValue = like.entrySet().iterator();
        
        while(iteratorMultiValue.hasNext()){
          
          entrySet = iteratorMultiValue.next();
          
          for(String value : entrySet.getValue()){
            query += entrySet.getKey() + " LIKE " + SSStrU.questionMark + " OR ";
          }
        }
        
        query = SSStrU.removeTrailingString(query, " OR ") + ") AND ";
      }
      
      query = SSStrU.removeTrailingString(query, " AND ");
      
      if(
        !likes.isEmpty() &&
        !tableCons.isEmpty()){
        
        query += " AND ";
      }
      
      for(String tableCon : tableCons){
        query += tableCon + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      
      if(
        orderByColumn != null &&
        sortType      != null){
        query         += " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
      }
      
      if(limit != null){
        query += " LIMIT " + limit;
      }
      
      stmt           = connector.prepareStatement(query);
      
      for(MultivaluedMap<String, String> like : likes){
        
        iteratorMultiValue = like.entrySet().iterator();
        
        while(iteratorMultiValue.hasNext()){
          
          entrySet = iteratorMultiValue.next();
          
          for(String value : entrySet.getValue()){
            stmt.setObject(counter++, "%" + value + "%");
          }
        }
      }
      
      return stmt.executeQuery();
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
      return null;
    }
  }
  
  @Override
  public void insertIfNotExists(
    final String              table,
    final Map<String, String> inserts,
    final Map<String, String> uniqueKeys) throws SSErr{
    
    PreparedStatement stmt = null;
    
    try{
      String                              query    = "INSERT INTO " + table + SSStrU.bracketOpen;
      int                                 counter  = 1;
      Iterator<Map.Entry<String, String>> iterator = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + SSStrU.bracketClose + " SELECT * FROM (SELECT ";
      iterator       = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += SSStrU.questionMark + "AS " + iterator.next().getKey() + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + SSStrU.bracketClose + " AS tmp WHERE NOT EXISTS (SELECT ";
      iterator       = uniqueKeys.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM " + table + " WHERE ";
      iterator       = uniqueKeys.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ") + ") LIMIT 1";
      stmt           = connector.prepareStatement(query);
      iterator       = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      iterator = uniqueKeys.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      stmt.executeUpdate();
    }catch(MySQLTransactionRollbackException deadLockError){
      SSServErrReg.regErrThrow(SSErrE.sqlDeadLock, deadLockError);
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
    }finally{
      
      if(stmt != null){
        
        try{
          stmt.close();
        }catch(SQLException sqlError) {
          SSLogU.warn(SSWarnE.sqlCloseStatementFailed, sqlError);
        }
      }
    }
  }
  
  @Override
  public void delete(
    final String table) throws SSErr{
    
    PreparedStatement   stmt  = null;
    
    try{
      
      stmt = connector.prepareStatement("DELETE FROM " + table);
      
      stmt.executeUpdate();
      
    }catch(MySQLTransactionRollbackException deadLockError){
      SSServErrReg.regErrThrow(SSErrE.sqlDeadLock, deadLockError);
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
    }finally{
      
      if(stmt != null){
        
        try{
          stmt.close();
        }catch(SQLException sqlError) {
          SSLogU.warn(SSWarnE.sqlCloseStatementFailed, sqlError);
        }
      }
    }
  }
  
  @Override
  public void updateIgnore(
    final String              table,
    final Map<String, String> wheres,
    final Map<String, String> values) throws SSErr{
    
    PreparedStatement stmt    = null;
    
    try{
      String                              query    = "UPDATE IGNORE " + table + " SET ";
      int                                 counter  = 1;
      Iterator<Map.Entry<String, String>> iterator = values.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
      iterator       = wheres.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = values.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      iterator = wheres.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      stmt.executeUpdate();
      
    }catch(MySQLTransactionRollbackException deadLockError){
      SSServErrReg.regErrThrow(SSErrE.sqlDeadLock, deadLockError);
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
    }finally{
      
      if(stmt != null){
        
        try{
          stmt.close();
        }catch(SQLException sqlError) {
          SSLogU.warn(SSWarnE.sqlCloseStatementFailed, sqlError);
        }
      }
    }
  }
  
  @Override
  public void update(
    final String              table,
    final Map<String, String> wheres,
    final Map<String, String> updates) throws SSErr{
    
    PreparedStatement stmt    = null;
    
    try{
      String                              query   = "UPDATE " + table + " SET ";
      int                                 counter = 1;
      Iterator<Map.Entry<String, String>> iterator = updates.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
      iterator       = wheres.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = updates.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      iterator = wheres.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      stmt.executeUpdate();
      
    }catch(MySQLTransactionRollbackException deadLockError){
      SSServErrReg.regErrThrow(SSErrE.sqlDeadLock, deadLockError);
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
    }finally{
      
      if(stmt != null){
        
        try{
          stmt.close();
        }catch(SQLException sqlError) {
          SSLogU.warn(SSWarnE.sqlCloseStatementFailed, sqlError);
        }
      }
    }
  }
  
  @Override
  public void insert(
    final String              table,
    final Map<String, String> inserts) throws SSErr{
    
    PreparedStatement stmt    = null;
    
    try{
      String                              query    = "INSERT INTO " + table + SSStrU.bracketOpen;
      int                                 counter  = 1;
      Iterator<Map.Entry<String, String>> iterator = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.comma;
      }
      
      query           = SSStrU.removeTrailingString(query, SSStrU.comma) + SSStrU.bracketClose + " VALUES " + SSStrU.bracketOpen;
      iterator        = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += SSStrU.questionMark + SSStrU.comma;
        iterator.next();
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + SSStrU.bracketClose;
      stmt           = connector.prepareStatement(query);
      iterator       = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      stmt.executeUpdate();
    }catch(MySQLTransactionRollbackException deadLockError){
      SSServErrReg.regErrThrow(SSErrE.sqlDeadLock, deadLockError);
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
    }finally{
      
      if(stmt != null){
        
        try{
          stmt.close();
        }catch(SQLException sqlError) {
          SSLogU.warn(SSWarnE.sqlCloseStatementFailed, sqlError);
        }
      }
    }
  }
  
  @Override
  public void delete(
    final String              table,
    final Map<String, String> wheres) throws SSErr{
    
    PreparedStatement stmt    = null;
    
    try{
      String                              query    = "DELETE FROM " + table + " WHERE ";
      int                                 counter  = 1;
      Iterator<Map.Entry<String, String>> iterator = wheres.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = wheres.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      stmt.executeUpdate();
      
    }catch(MySQLTransactionRollbackException deadLockError){
      SSServErrReg.regErrThrow(SSErrE.sqlDeadLock, deadLockError);
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
    }finally{
      
      if(stmt != null){
        
        try{
          stmt.close();
        }catch(SQLException sqlError) {
          SSLogU.warn(SSWarnE.sqlCloseStatementFailed, sqlError);
        }
      }
    }
  }
  
  @Override
  public void deleteIgnore(
    final String              table,
    final Map<String, String> deletes) throws SSErr{
    
    PreparedStatement stmt = null;
    
    try{
      String                              query    = "DELETE IGNORE FROM " + table + " WHERE ";
      Iterator<Map.Entry<String, String>> iterator = deletes.entrySet().iterator();
      int                                 counter  = 1;
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = deletes.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      stmt.executeUpdate();
      
    }catch(MySQLTransactionRollbackException deadLockError){
      SSServErrReg.regErrThrow(SSErrE.sqlDeadLock, deadLockError);
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
    }finally{
      
      if(stmt != null){
        
        try{
          stmt.close();
        }catch(SQLException sqlError) {
          SSLogU.warn(SSWarnE.sqlCloseStatementFailed, sqlError);
        }
      }
    }
  }
  
  @Override
  public void deleteIgnore(
    final String                               table,
    final List<MultivaluedMap<String, String>> wheres) throws SSErr{
    
    PreparedStatement stmt = null;
    
    try{
      String                                    query   = "DELETE IGNORE FROM " + table + " WHERE ";
      int                                       counter = 1;
      Iterator<Map.Entry<String, List<String>>> iteratorMultiValue;
      Map.Entry<String, List<String>>           entrySet;
      
      if(wheres.isEmpty()){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      for(MultivaluedMap<String, String> where : wheres){
        
        query += "(";
        
        iteratorMultiValue = where.entrySet().iterator();
        
        while(iteratorMultiValue.hasNext()){
          
          entrySet = iteratorMultiValue.next();
          
          for(String value : entrySet.getValue()){
            query += entrySet.getKey() + SSStrU.equal + SSStrU.questionMark + " OR ";
          }
        }
        
        query = SSStrU.removeTrailingString(query, " OR ") + ") AND ";
      }
      
      query = SSStrU.removeTrailingString(query, " AND ");
      stmt  = connector.prepareStatement(query);
      
      for(MultivaluedMap<String, String> where : wheres){
        
        iteratorMultiValue = where.entrySet().iterator();
        
        while(iteratorMultiValue.hasNext()){
          
          entrySet = iteratorMultiValue.next();
          
          for(String value : entrySet.getValue()){
            stmt.setObject(counter++, value);
          }
        }
      }
      
      stmt.executeUpdate();
    }catch(MySQLTransactionRollbackException deadLockError){
      SSServErrReg.regErrThrow(SSErrE.sqlDeadLock, deadLockError);
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
    }finally{
      
      if(stmt != null){
        
        try{
          stmt.close();
        }catch(SQLException sqlError) {
          SSLogU.warn(SSWarnE.sqlCloseStatementFailed, sqlError);
        }
      }
    }
  }
  
  @Override
  public Connection getConnection(){
    return connector;
  }
  
  @Override
  protected void finalizeImpl() throws SSErr{
    
    try{
      closeCon();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void startTrans(
    Boolean shouldCommit) throws SSErr{
    
    if(!shouldCommit){
      return;
    }
    
    if(connector == null){
      SSLogU.warn("con null");
      return;
    }
    
    try{
      connector.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
      connector.setAutoCommit(false);
    }catch(SQLException error){
      SSServErrReg.regErrThrow(error);
    }
  }
  @Override
  public void closeCon() throws SSErr{
    
    try{
      if(connector == null){
        return;
      }
      
      if(connector.isClosed()){
        connector = null;
        return;
      }
      
      connector.close();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void closeStmt(final ResultSet resultSet) throws SSErr{
    
    try{
      
      if(
        resultSet                == null ||
        resultSet.isClosed()             ||
        resultSet.getStatement() == null ||
        resultSet.getStatement().isClosed()){
        return;
      }
      
      resultSet.getStatement().close();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean rollBack(final Boolean shouldCommit){
    
    try{
      
      if(
        !shouldCommit             ||
        connector == null         ||
        connector.getAutoCommit() ||
        connector.isClosed()){
        return false;
      }
      
      connector.rollback();
      
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErr(error);
      return null;
    }
  }
  
  @Override
  public void commit(Boolean shouldCommit) throws SSErr{
    
    if(!shouldCommit){
      return;
    }
    
    try{
      connector.commit();
      connector.setAutoCommit(true);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void connectToMYSQL() throws SSErr{
    
    try{
      
      if(SSObjU.isNull(connectionPool)){
        connectToMYSQLConnectionPool();
      }
      
      while(!gotCon && numberTimesTriedToGetCon < 5){
        
        try{
          connector = connectionPool.getConnection();
          connector.setAutoCommit(true);
          
          gotCon = true;
          
        }catch(SQLException sqlError){
          
          SSLogU.warn("no db conn available anymore... going to sleep for 3000 ms", sqlError);
          
          numberTimesTriedToGetCon++;
          
          try{
            Thread.sleep(3000);
          }catch (InterruptedException threadError) {
            throw SSErr.get(SSErrE.mySQLGetConnectionFromPoolFailed, threadError);
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void connectToMYSQLConnectionPool()throws SSErr{
    
    try{
      Class.forName("com.mysql.jdbc.Driver");
      
      //    private static BoneCP     connectionPool   = null;
      //    BoneCPConfig config = new BoneCPConfig();
//    config.setJdbcUrl  ("jdbc:mysql://" + ((SSDBSQLConf)conf).host + SSStrU.colon + ((SSDBSQLConf)conf).port + SSStrU.slash + ((SSDBSQLConf)conf).schema); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
//    config.setUsername (((SSDBSQLConf)conf).username);
//    config.setPassword (((SSDBSQLConf)conf).password);
//    config.setMinConnectionsPerPartition(5);
//    config.setMaxConnectionsPerPartition(10);
//    config.setPartitionCount(4);
//    config.setLogStatementsEnabled(true);
//    config.setPoolStrategy("DEFAULT");
//    config.setAcquireRetryAttempts(1);
//    config.setAcquireRetryDelayInMs(1100);
//    config.setConnectionTimeoutInMs(1300);
//
//    config.setTransactionRecoveryEnabled(false);
//
//    connectionPool = new BoneCP(config);
      
      
      PoolProperties prop = new PoolProperties();
      prop.setUrl             ("jdbc:mysql://" + ((SSDBSQLConf)conf).host + SSStrU.colon + ((SSDBSQLConf)conf).port + SSStrU.slash + ((SSDBSQLConf)conf).schema + "?autoReconnect=true");
      prop.setDriverClassName ("com.mysql.jdbc.Driver");
      prop.setUsername        (((SSDBSQLConf)conf).username);
      prop.setPassword        (((SSDBSQLConf)conf).password);
      prop.setFairQueue(true);
      prop.setTestWhileIdle(true);
      prop.setValidationInterval(30000);
      prop.setMaxActive(100);
      prop.setInitialSize(10);
      prop.setMinIdle(10);
      prop.setMaxWait(10000);
      prop.setRemoveAbandoned(false);
      prop.setMinEvictableIdleTimeMillis(30000);
      prop.setTestOnBorrow(true);
      prop.setValidationQuery("select 1");
      
      connectionPool = new DataSource(prop);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(SSErrE.mySQLConnectionFailed, error);
    }
  }
}

//  @Override
//  public void insertIgnore(String tableName, Map<String, String> parNamesWithValues) throws SSErr {
//
//    String                              query   = "INSERT IGNORE INTO ";
//    int                                 counter = 1;
//    PreparedStatement                   stmt;
//    Iterator<Map.Entry<String, String>> iterator;
//
//    if(
//      SSStrU.isEmpty (tableName) ||
//      SSObjU.isNull  (parNamesWithValues)){
//      SSLogU.logAndThrow(new Exception("table not spec or pars null"));
//    }
//
//    query         += tableName + SSStrU.bracketOpen;
//    iterator       = parNamesWithValues.entrySet().iterator();
//
//    while(iterator.hasNext()){
//      query += iterator.next().getKey() + SSStrU.comma;
//    }
//
//    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
//    query         += SSStrU.bracketClose + " VALUES " + SSStrU.bracketOpen;
//    iterator       = parNamesWithValues.entrySet().iterator();
//
//    while(iterator.hasNext()){
//      query += SSStrU.questionMark + SSStrU.comma;
//      iterator.next();
//    }
//
//    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
//    query         += SSStrU.bracketClose;
//    stmt           = connector.prepareStatement(query);
//    iterator       = parNamesWithValues.entrySet().iterator();
//
//    while(iterator.hasNext()){
//      stmt.setObject(counter++, iterator.next().getValue());
//    }
//
//    stmt.executeUpdate();
//    stmt.close();
//
////    "insert into table (id, name, age) values(1, "A", 19) on duplicate key update name=values(name), age=values(age)";
//  }

//@Override
//  public ResultSet query(String query) throws SSErr{
//
//    Statement   stmt    = connector.createStatement();
//    ResultSet   results = stmt.executeQuery(query);
//
//    return results;
//  }

//    p.setJmxEnabled(false);
//    p.setTestWhileIdle(false);
//    p.setTestOnBorrow(false);
//    p.setValidationQuery("SELECT 1");
//    p.setTestOnReturn(false);
//    p.setValidationInterval(30000);
//    p.setTimeBetweenEvictionRunsMillis(30000);
//    p.setMaxActive(100);
//    p.setInitialSize(10);
//    p.setMaxWait(10000);
//    p.setRemoveAbandonedTimeout(60);
//    p.setMinEvictableIdleTimeMillis(30000);
//    p.setMinIdle(10);
//    p.setLogAbandoned(true);
//    p.setRemoveAbandoned(true);
//    p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");



//  @Override
//  public ResultSet select(
//    final List<String>        tables,
//    final List<String>        columns,
//    final Map<String, String> wheres,
//    final String              tableConnection) throws SSErr{
//
//    String                              query   = "SELECT ";
//    int                                 counter = 1;
//    PreparedStatement                   stmt;
//    Iterator<Map.Entry<String, String>> iterator;
//
//    for(String columnName : columns){
//      query += columnName + SSStrU.comma;
//    }
//
//    query = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM ";
//
//    for(String tableName : tables){
//      query += tableName + SSStrU.comma;
//    }
//
//    query         = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
//    iterator      = wheres.entrySet().iterator();
//
//    while(iterator.hasNext()){
//      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
//    }
//
//    query         += tableConnection;
//    stmt           = connector.prepareStatement(query);
//    iterator       = wheres.entrySet().iterator();
//
//    while(iterator.hasNext()){
//      stmt.setObject(counter++, iterator.next().getValue());
//    }
//
//    return stmt.executeQuery();
//  }

//  @Override
//  public ResultSet select(
//    final String              table,
//    final Map<String, String> wheres) throws SSErr{
//
//    String                              query    = "SELECT * FROM " + table + " WHERE ";
//    Iterator<Map.Entry<String, String>> iterator = wheres.entrySet().iterator();
//    int                                 counter  = 1;
//    PreparedStatement                   stmt;
//
//    while(iterator.hasNext()){
//      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
//    }
//
//    query          = SSStrU.removeTrailingString(query, " AND ");
//    stmt           = connector.prepareStatement(query);
//    iterator       = wheres.entrySet().iterator();
//
//    while(iterator.hasNext()){
//      stmt.setObject(counter++, iterator.next().getValue());
//    }
//
//    return stmt.executeQuery();
//  }

//  @Override
//  public ResultSet select(
//    final String table) throws SSErr{
//    return connector.prepareStatement("SELECT * FROM " + table).executeQuery();
//  }

//  @Override
//  public ResultSet select(
//    String              table,
//    Map<String, String> wheres,
//    String              orderByColumn,
//    String              sortType) throws SSErr{
//
//    String                              query    = "SELECT * FROM " + table + " WHERE ";
//    Iterator<Map.Entry<String, String>> iterator = wheres.entrySet().iterator();
//    int                                 counter  = 1;
//    PreparedStatement                   stmt;
//
//    while(iterator.hasNext()){
//      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
//    }
//
//    query          = SSStrU.removeTrailingString(query, " AND ") + " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
//    stmt           = connector.prepareStatement(query);
//    iterator       = wheres.entrySet().iterator();
//
//    while(iterator.hasNext()){
//      stmt.setObject(counter++, iterator.next().getValue());
//    }
//
//    return stmt.executeQuery();
//  }


//  @Override
//  public ResultSet select(
//    final List<String>        tables,
//    final List<String>        columns,
//    final Map<String, String> wheres,
//    final String              tableCon,
//    final String              orderByColumn,
//    final String              sortType) throws SSErr{
//
//    String                              query   = "SELECT ";
//    int                                 counter = 1;
//    PreparedStatement                   stmt;
//    Iterator<Map.Entry<String, String>> iterator;
//
//    for(String columnName : columns){
//      query += columnName + SSStrU.comma;
//    }
//
//    query = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM ";
//
//    for(String tableName : tables){
//      query += tableName + SSStrU.comma;
//    }
//
//    query         = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
//    iterator      = wheres.entrySet().iterator();
//
//    while(iterator.hasNext()){
//      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
//    }
//
//    query         += tableCon;
//
//    if(
//      orderByColumn != null &&
//      sortType      != null){
//      query         += " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
//    }
//
//    stmt           = connector.prepareStatement(query);
//    iterator       = wheres.entrySet().iterator();
//
//    while(iterator.hasNext()){
//      stmt.setObject(counter++, iterator.next().getValue());
//    }
//
//    return stmt.executeQuery();
//  }

//@Override
//  public ResultSet selectWithNumerics(
//    final List<String>                                           tables,
//    final List<String>                                           columns,
//    final List<MultivaluedMap<String, String>>                   wheres,
//    final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumbericComparision,
//    final List<String>                                           tableCons,
//    final String                                                 orderByColumn,
//    final String                                                 sortType,
//    final Integer                                                limit) throws SSErr{
//
//    String                                    query   = "SELECT DISTINCT "; //caution do not remove distinct here without checks
//    int                                       counter = 1;
//    PreparedStatement                         stmt;
//    Iterator<Map.Entry<String, List<String>>> iteratorMultiValue;
//    Map.Entry<String, List<String>>           entrySet;
//
//    for(String columnName : columns){
//      query += columnName + SSStrU.comma;
//    }
//
//    if(
//      columns == null ||
//      columns.isEmpty()){
//
//      query += "*";
//    }
//
//    query = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM ";
//
//    for(String tableName : tables){
//      query += tableName + SSStrU.comma;
//    }
//
//    query = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
//
//    for(MultivaluedMap<String, String> where : wheres){
//
//      query += "(";
//
//      iteratorMultiValue = where.entrySet().iterator();
//
//      while(iteratorMultiValue.hasNext()){
//
//        entrySet = iteratorMultiValue.next();
//
//        for(String value : entrySet.getValue()){
//          query += entrySet.getKey() + SSStrU.equal + SSStrU.questionMark + " OR ";
//        }
//      }
//
//      query = SSStrU.removeTrailingString(query, " OR ") + ") AND ";
//    }
//
//    for(Map.Entry<String, List<MultivaluedMap<String, String>>> wheresNumberic : wheresNumbericComparision.entrySet()){
//
//      String comparator = wheresNumberic.getKey();
//
//      for(MultivaluedMap<String, String> whereNumeric : wheresNumberic.getValue()){
//
//        query += "(";
//
//        iteratorMultiValue = whereNumeric.entrySet().iterator();
//
//        while(iteratorMultiValue.hasNext()){
//
//          entrySet = iteratorMultiValue.next();
//
//          for(String value : entrySet.getValue()){
//            query += entrySet.getKey() + comparator + SSStrU.questionMark + " OR ";
//          }
//        }
//
//        query = SSStrU.removeTrailingString(query, " OR ") + ") AND ";
//      }
//    }
//
////    if(wheresNumbericComparision.isEmpty()){
////      query = SSStrU.removeTrailingString(query, ") AND ");
////    }else{
//      query = SSStrU.removeTrailingString(query, " AND ");
////    }
//
//    if(
//      !wheres.isEmpty() ||
//      !wheresNumbericComparision.isEmpty()){
//
//      if(!tableCons.isEmpty()){
//        query += " AND ";
//      }
//    }
//
//    for(String tableCon : tableCons){
//      query += tableCon + " AND ";
//    }
//
//    query = SSStrU.removeTrailingString(query, " AND ");
//
//    if(
//      orderByColumn != null &&
//      sortType      != null){
//      query         += " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
//    }
//
//    if(limit != null){
//      query += " LIMIT " + limit;
//    }
//
//    stmt           = connector.prepareStatement(query);
//
//    for(MultivaluedMap<String, String> where : wheres){
//
//      iteratorMultiValue = where.entrySet().iterator();
//
//      while(iteratorMultiValue.hasNext()){
//
//        entrySet = iteratorMultiValue.next();
//
//        for(String value : entrySet.getValue()){
//          stmt.setObject(counter++, value);
//        }
//      }
//    }
//
//    for(Map.Entry<String, List<MultivaluedMap<String, String>>> wheresNumberic : wheresNumbericComparision.entrySet()){
//
//      for(MultivaluedMap<String, String> whereNumeric : wheresNumberic.getValue()){
//
//        iteratorMultiValue = whereNumeric.entrySet().iterator();
//
//        while(iteratorMultiValue.hasNext()){
//
//          entrySet = iteratorMultiValue.next();
//
//          for(String value : entrySet.getValue()){
//            stmt.setObject(counter++, value);
//          }
//        }
//      }
//    }
//
//    return stmt.executeQuery();
//  }