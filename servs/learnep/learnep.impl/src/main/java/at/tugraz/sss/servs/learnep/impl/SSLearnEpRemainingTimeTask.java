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
package at.tugraz.sss.servs.learnep.impl;

import at.kc.tugraz.ss.serv.datatypes.learnep.api.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockRemovePar;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import java.sql.*;

public class SSLearnEpRemainingTimeTask implements Runnable{
  
  @Override
  public void run() {
    
    Connection sqlCon = null;
    
    try{
      final SSLearnEpServerI learnEpServ = ((SSLearnEpServerI) SSServReg.getServ(SSLearnEpServerI.class));
      final SSServPar        servPar     = new SSServPar(null);
      Long                   remainingTime;
      
      sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      
      servPar.sqlCon = sqlCon;
      
      for(String learnEp : SSLearnEpAccessController.getLockedLearnEps()){
        
        remainingTime = SSLearnEpAccessController.getRemainingTime(SSUri.get(learnEp));
        
        if(remainingTime <= 0){
          
          learnEpServ.learnEpLockRemove(
            new SSLearnEpLockRemovePar(
              servPar,
              SSConf.systemUserUri,
              null,
              SSUri.get(learnEp),
              false,
              true));
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
    }finally{
      
      if(sqlCon != null){
        
        try{
          sqlCon.close();
        }catch (SQLException error) {
          SSLogU.err(error);
        }
      }
    }
  }
}