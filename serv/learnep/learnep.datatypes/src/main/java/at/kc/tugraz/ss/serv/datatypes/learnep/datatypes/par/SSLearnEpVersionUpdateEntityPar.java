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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSLearnEpVersionUpdateEntityPar extends SSServPar{
  
  public SSUri    learnEpEntity      = null;
  public SSUri    entity             = null;
  public Float    x                  = null;
  public Float    y                  = null;
  
  public SSLearnEpVersionUpdateEntityPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        learnEpEntity      = (SSUri)    pars.get(SSVarU.learnEpEntity);
        entity             = (SSUri)    pars.get(SSVarU.entity);
        x                  = (Float)    pars.get(SSVarU.x);
        y                  = (Float)    pars.get(SSVarU.y);
      }
      
      if(par.clientJSONObj != null){
        learnEpEntity     = SSUri.get        (par.clientJSONObj.get(SSVarU.learnEpEntity).getTextValue());
        
        try{
          entity            = SSUri.get        (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        }catch(Exception error){}
        
        try{
          x                 = par.clientJSONObj.get(SSVarU.x).getNumberValue().floatValue();
        }catch(Exception error){}
        
        try{
          y                 = par.clientJSONObj.get(SSVarU.y).getNumberValue().floatValue();
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}