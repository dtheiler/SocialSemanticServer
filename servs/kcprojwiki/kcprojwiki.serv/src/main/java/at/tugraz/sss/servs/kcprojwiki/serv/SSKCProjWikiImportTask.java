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
package at.tugraz.sss.servs.kcprojwiki.serv;

import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.impl.api.SSServImplStartA;
import java.util.TimerTask;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.servs.kcprojwiki.api.SSKCProjWikiServerI;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiImportPar;

public class SSKCProjWikiImportTask extends TimerTask {
  
  public SSKCProjWikiImportTask() throws Exception{
    SSServReg.regTimerTask(this);
  }
  
  @Override
  public void run(){
    
    try{
      
      final Thread thread = new Thread(new SSKCProjWikiImportUpdater());
      
      thread.start();
      
      SSServReg.regTimerThread(thread);
      
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }
  }
  
  protected class SSKCProjWikiImportUpdater extends SSServImplStartA implements Runnable{
    
    public SSKCProjWikiImportUpdater() throws Exception{
      super(null);
    }
    
    @Override
    public void run() {
      
      try{
        
        ((SSKCProjWikiServerI) SSServReg.getServ(SSKCProjWikiServerI.class)).kcprojwikiImport(new SSKCProjWikiImportPar(SSConf.systemUserUri));
        
      }catch(Exception error){
        SSServErrReg.regErr(error);
      }finally{
        
        try{
          finalizeImpl();
        }catch(Exception error2){
          SSLogU.err(error2);
        }
      }
    }
    
    @Override
    protected void finalizeImpl() throws Exception{
      destroy();
    }
  }
}