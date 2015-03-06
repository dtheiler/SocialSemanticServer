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
package at.kc.tugraz.sss.appstacklayout.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.sss.appstacklayout.datatypes.SSAppStackLayout;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSAppStackLayoutSQLFct extends SSDBSQLFct{

  public SSAppStackLayoutSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public List<SSAppStackLayout> getAppStackLayouts() throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSAppStackLayout> appStackLayouts = new ArrayList<>();
      final List<String>           columns         = new ArrayList<>();
      final Map<String, String>    wheres          = new HashMap<>();
      
      column(columns, SSSQLVarU.stackId);
      column(columns, SSSQLVarU.app);
        
      resultSet = dbSQL.select(appStackLayoutTable, columns, wheres, null, null, null);
      
      while(resultSet.next()){
        
        appStackLayouts.add(
          SSAppStackLayout.get(
            bindingStrToUri         (resultSet, SSSQLVarU.stackId), 
            bindingStrToUri         (resultSet, SSSQLVarU.app),
            new ArrayList<>()));
      }
      
      return appStackLayouts;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void createAppStackLayout(
    final SSUri         stack,
    final SSUri         app) throws Exception{
    
     try{
      final Map<String, String> inserts    = new HashMap<>();
      
      if(app != null){
        insert    (inserts,    SSSQLVarU.app,     app);
      }
      
      insert    (inserts,    SSSQLVarU.stackId,     stack);
      
      dbSQL.insert(appStackLayoutTable, inserts);
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }

  public void deleteStack(
    final SSUri stack) throws Exception{
    
    try{
      final Map<String, String> deletes    = new HashMap<>();
      
      delete(deletes, SSSQLVarU.stackId, stack);
      
      dbSQL.delete(appStackLayoutTable, deletes);
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }

  public void updateAppStackLayout(
    final SSUri stack, 
    final SSUri app) throws Exception{
    
    try{
      final Map<String, String> updates    = new HashMap<>();
      final Map<String, String> wheres     = new HashMap<>();
      
      where(wheres, SSSQLVarU.stackId, stack);
      
      if(app != null){
        update(updates,    SSSQLVarU.app,     app);
      }
      
      dbSQL.updateIgnore(appStackLayoutTable, wheres, updates);
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
}