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
import at.tugraz.sss.serv.SSServErrReg;
public class SSTagAddPar extends SSServPar{
  
  public SSUri        entity       = null;
  public SSTagLabel   label        = null;
  public SSSpaceE     space        = null;
  public Long         creationTime = null;
  
  public SSTagAddPar(
    final SSServOpE      op,
    final String       key,
    final SSUri        user,
    final SSUri        entity,
    final SSTagLabel   label,
    final SSSpaceE     space,
    final Long         creationTime) {
  
    super(op, key, user);
    
    this.entity       = entity;
    this.label        = label;
    this.space        = space;
    this.creationTime = creationTime;
  }
  
  public SSTagAddPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entity       = (SSUri)                 pars.get(SSVarU.entity);
        label        = SSTagLabel.get((String) pars.get(SSVarU.label));
        space        = (SSSpaceE)              pars.get(SSVarU.space);
        creationTime = (Long)                  pars.get(SSVarU.creationTime);
      }
      
      if(par.clientJSONObj != null){
        entity     = SSUri.get        (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        label      = SSTagLabel.get   (par.clientJSONObj.get(SSVarU.label).getTextValue());
        space      = SSSpaceE.get     (par.clientJSONObj.get(SSVarU.space).getTextValue());
        
        try{
          creationTime = par.clientJSONObj.get(SSVarU.creationTime).getLongValue();
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getSpace(){
    return SSStrU.toStr(space);
  }
}
