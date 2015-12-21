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
package at.kc.tugraz.ss.serv.datatypes.entity.serv;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.tugraz.sss.serv.datatype.par.SSCirclePubURIGetPar;
import at.tugraz.sss.serv.conf.SSCoreConfA;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.impl.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.servs.entity.impl.SSEntityImpl;
import java.util.List;

public class SSEntityServ extends SSServContainerI{
  
  public static final SSEntityServ inst = new SSEntityServ(SSEntityClientI.class, SSEntityServerI.class);
  
  protected SSEntityServ(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  protected SSServImplA createServImplForThread() throws SSErr{
    return new SSEntityImpl(conf);
  }
  
  @Override
  public SSServContainerI regServ() throws Exception{
    
    this.conf = SSCoreConf.instGet().getEntity();
    
    SSServReg.inst.regServ(this);
    
    SSServReg.inst.regServForGatheringUsersResources(this);
    SSServReg.inst.regServForHandlingAddAffiliatedEntitiesToCircle(this);
    SSServReg.inst.regServForHandlingDescribeEntity(this);
    SSServReg.inst.regServForHandlingCopyEntity(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
     if(!conf.use){
      return;
    }
    
    ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).circlePubURIGet(
      new SSCirclePubURIGetPar(
        SSVocConf.systemUserUri, 
        true));
  }
  
  @Override
  public void schedule() throws Exception{
    
    if(!conf.use){
      return;
    }
    
    SSDateU.scheduleAtFixedRate(
      new SSEntitiesAccessibleGetCleanUpTask(),
      SSDateU.getDatePlusMinutes(5),
      5 * SSDateU.minuteInMilliSeconds);
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA,
    final List<Class> configuredServs) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}