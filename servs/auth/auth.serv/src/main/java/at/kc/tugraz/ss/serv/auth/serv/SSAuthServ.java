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
package at.kc.tugraz.ss.serv.auth.serv;

import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.serv.auth.api.SSAuthClientI;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.tugraz.sss.servs.auth.impl.SSAuthImpl;
import at.tugraz.sss.servs.auth.datatype.par.SSAuthRegisterUserPar;
import at.tugraz.sss.servs.auth.datatype.par.SSAuthUsersFromCSVFileAddPar;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.util.*;
import java.sql.*;
import java.util.List;

public class SSAuthServ extends SSServContainerI{
  
  public static final SSAuthServ inst = new SSAuthServ(SSAuthClientI.class, SSAuthServerI.class);

  protected SSAuthServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  public SSServImplA getServImpl() throws SSErr{
    
    if(!conf.use){
      throw SSErr.get(SSErrE.servNotRunning);
    }
    
    if(servImpl != null){
      return servImpl;
    }
    
    synchronized(this){
      servImpl = new SSAuthImpl((SSAuthConf) conf);
    }
    
    return servImpl;
  }
  
  @Override
  public SSServContainerI regServ(final SSConfA conf) throws SSErr{
    
    this.conf = conf;
    
    SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws SSErr{
    
    if(!conf.use){
      return;
    }
    
    Connection sqlCon = null;
    
    try{
      
      if(conf.initAtStartUp){
        
        final SSAuthImpl authServ = (SSAuthImpl) inst.getServImpl();
        final SSServPar  servPar  = new SSServPar(null);
        
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
        
        servPar.sqlCon = sqlCon;
        
        authServ.authRegisterUser(
          new SSAuthRegisterUserPar(
            servPar,
            SSConf.systemUserEmail,
            ((SSAuthConf)conf).systemUserPassword,
            SSLabel.get(SSConf.systemUserLabel),
            true,
            true,
            false,
            true));
        
        switch(((SSAuthConf)conf).authType){
          case csvFileAuth:{
            authServ.authUsersFromCSVFileAdd(
              new SSAuthUsersFromCSVFileAddPar(
                servPar,
                SSConf.systemUserUri));
            break;
          } 
        }
      }
    }finally{
      
      if(sqlCon != null){
        try {
          sqlCon.close();
        } catch (SQLException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA, 
    final List<Class> configuredServs) throws SSErr{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void schedule() throws SSErr{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}