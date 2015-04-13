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
package at.kc.tugraz.ss.service.tag.datatypes.pars;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSServErrReg;

public class SSTagsUserRemovePar extends SSServPar{
  
  public SSUri        entity     = null;
  public SSTagLabel   label      = null;
  public SSSpaceE     space      = null;

  public void setEntity(final String entity){
    try{ this.entity = SSUri.get(entity); }catch(Exception error){}
  }

  public void setLabel(final String label){
    try{ this.label = SSTagLabel.get(label); }catch(Exception error){}
  }

  public void setSpace(final String space){
    try{ this.space =  SSSpaceE.get(space); }catch(Exception error){} 
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getSpace(){
    return SSStrU.toStr(space);
  }
  
  public SSTagsUserRemovePar(){}
  
  public SSTagsUserRemovePar(
    final SSServOpE    op,
    final String       key,
    final SSUri        user,
    final SSUri        entity,
    final SSTagLabel   label,
    final SSSpaceE     space,
    final Boolean      shouldCommit){
  
    super(op, key, user);
  
    this.entity       = entity;
    this.label        = label;
    this.space        = space;
    this.shouldCommit = shouldCommit;
  }
  
  public static SSTagsUserRemovePar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSTagsUserRemovePar) par.getFromJSON(SSTagsUserRemovePar.class);
      }
      
      return new SSTagsUserRemovePar(
        par.op,
        par.key,
        par.user,
        (SSUri)                 par.pars.get(SSVarU.entity),
        (SSTagLabel)            par.pars.get(SSVarU.label),
        (SSSpaceE)              par.pars.get(SSVarU.space),
        (Boolean)               par.pars.get(SSVarU.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}