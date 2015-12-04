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
 package at.kc.tugraz.ss.service.coll.service;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSCoreConfA;
import at.kc.tugraz.ss.service.coll.impl.*;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServImplA;
import at.kc.tugraz.ss.service.coll.api.SSCollClientI;
import at.kc.tugraz.ss.service.coll.api.SSCollServerI;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServContainerI;
import java.util.List;

public class SSCollServ extends SSServContainerI{
  
 public static final SSCollServ inst = new SSCollServ(SSCollClientI.class, SSCollServerI.class);
  
 protected SSCollServ(
    final Class servImplClientInteraceClass, 
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws SSErr{
    return new SSCollImpl(conf);
  }

  @Override
  public SSServContainerI regServ() throws Exception{
    
    this.conf = SSCoreConf.instGet().getColl();
    
    SSServReg.inst.regServ(this);

    SSServReg.inst.regServForHandlingDescribeEntity(this);
    SSServReg.inst.regServForHandlingGetParentEntities(this);
    SSServReg.inst.regServForHandlingGetSubEntities(this);
    SSServReg.inst.regServForHandlingPushEntitiesToUsers(this);
    SSServReg.inst.regServForHandlingAddAffiliatedEntitiesToCircle(this);
    SSServReg.inst.regServForGatheringUserRelations (this);
    SSServReg.inst.regServForGatheringUsersResources (this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA, 
    final List<Class> configuredServs) throws Exception{
   
    //TODO dtheiler: check whether to deploy service calls itself here once in getConfForCloudDeployment
    final SSCoreConf coreConf = (SSCoreConf) getConfForCloudDeployment(SSTagServerI.class /* TODO wrong class here */, coreConfA, configuredServs);
//    final SSCollConf collConf = coreConf.getColl();
//    
//    collConf.use                = true;
//    collConf.executeOpAtStartUp = false;
////    collConf.op                 = null;
//    
    return coreConf;
  }

  @Override
  public void schedule() throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}