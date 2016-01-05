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
package at.kc.tugraz.ss.category.ss.category.serv;

import at.kc.tugraz.ss.category.api.SSCategoryClientI;
import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.conf.SSCategoryConf;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedAddPar;
import at.kc.tugraz.ss.category.impl.SSCategoryImpl;
import at.tugraz.sss.conf.SSCoreConf;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import java.util.List;

public class SSCategoryServ extends SSServContainerI{
  
  public static final SSCategoryServ inst = new SSCategoryServ(SSCategoryClientI.class, SSCategoryServerI.class);
  
  protected SSCategoryServ(
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
      
      if(servImpl != null){
        return servImpl;
      }
      
      servImpl = new SSCategoryImpl(conf);
    }
    
    return servImpl;
  }
  
  @Override
  public SSServContainerI regServ() throws Exception{
    
    this.conf = SSCoreConf.instGet().getCategory();
    
    SSServReg.inst.regServ(this);
    
    SSServReg.inst.regServForHandlingDescribeEntity(this);
    SSServReg.inst.regServForHandlingEntityCopied(this);
    SSServReg.inst.regServForHandlingCircleContentRemoved(this);
    SSServReg.inst.regServForGatheringUserRelations(this);
    SSServReg.inst.regServForGatheringUsersResources(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    if(!conf.use){
      return;
    }
    
    if(((SSCategoryConf)conf).initAtStartUp){
      
      ((SSCategoryServerI) this.getServImpl()).categoriesPredefinedAdd(
        new SSCategoriesPredefinedAddPar(
          SSConf.systemUserUri, 
          SSCategoryLabel.asListNotEmpty(SSCategoryLabel.get(((SSCategoryConf) conf).predefinedCategories)), 
          true));
    }
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA,
    final List<Class> configuredServs) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void schedule() throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}