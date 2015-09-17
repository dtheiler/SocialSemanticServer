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
package at.kc.tugraz.ss.recomm.impl.fct.misc;

import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.datatypes.SSRecommUserRealmEngine;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkPar;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUpdateBulkRet;
import at.kc.tugraz.ss.recomm.impl.fct.sql.SSRecommSQLFct;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplStartA;
import engine.EntityRecommenderEngine;
import java.io.FileOutputStream;
import java.io.IOException;

public class SSRecommUpdateBulkUploader extends SSServImplStartA{
  
  private final SSRecommUpdateBulkPar       par;
  private       SSRecommSQLFct              sqlFct;
  private       FileOutputStream            fileOutputStream  = null;
  private       byte[]                      fileChunk         = null;
  private       String                      dataCSVPath       = null;
  
  public SSRecommUpdateBulkUploader(
    final SSRecommConf           recommConf,
    final SSRecommUpdateBulkPar  par) throws Exception{
    
    super(recommConf);
    
    this.par               = par;
    this.dataCSVPath       = SSFileU.dirWorkingDataCsv();
  }
  
  @Override
  public void run(){
    
    try{
      
      dbSQL  = (SSDBSQLI) SSDBSQL.inst.serv();
      sqlFct = new SSRecommSQLFct(dbSQL);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine =
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          (SSRecommConf)conf,
          par.user,
          par.realm,
          true, //checkForUpdate
          new EntityRecommenderEngine(), //engine
          sqlFct,
          true);
      
      this.fileOutputStream = SSFileU.openOrCreateFileWithPathForWrite (dataCSVPath + userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      sendAnswer();
      
      while(true){
        
        fileChunk = par.sSCon.readFileChunkFromClient();
        
        if(fileChunk.length != 0){
          
          fileOutputStream.write        (fileChunk);
          fileOutputStream.flush        ();
          continue;
        }
        
        fileOutputStream.close();
        
        sendAnswer();
        
        dbSQL.commit(par.shouldCommit);
        return;
      }
    }catch(Exception error1){
      
      SSServErrReg.regErr(error1);
      
      try{
        par.sSCon.writeErrorFullToClient(SSServErrReg.getServiceImplErrors(), par.op);
      }catch(Exception error2){
        SSServErrReg.regErr(error2);
      }
    }finally{
      
      if(fileOutputStream != null){
        
        try{
          fileOutputStream.close();
        }catch(IOException ex){
          SSLogU.err(ex);
        }
      }
      
      try{
        finalizeImpl();
      }catch(Exception error3){
        SSLogU.err(error3);
      }
    }
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread(true);
  }
  
  private void sendAnswer() throws Exception{
    par.sSCon.writeRetFullToClient(SSRecommUpdateBulkRet.get(true));
  }
}