/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import java.util.ArrayList;
import java.util.List;

public class SSEntityUserCircleCreatePar extends SSServPar{

  public List<SSUri>               entityUris  = new ArrayList<SSUri>();
  public List<SSUri>               userUris    = new ArrayList<SSUri>();
  public SSCircleE       circleType  = null;
  public SSLabel                label       = null;
  
  public SSEntityUserCircleCreatePar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        circleType       = (SSCircleE)       pars.get(SSVarU.circleType);
        label            = (SSLabel)                pars.get(SSVarU.label);
        entityUris       = (List<SSUri>)               pars.get(SSVarU.entityUris);
        userUris         = (List<SSUri>)               pars.get(SSVarU.userUris);
      }
      
      if(clientPars != null){
        circleType       = SSCircleE.get       (clientPars.get(SSVarU.circleType));
        label            = SSLabel.get                (clientPars.get(SSVarU.label));
        
        try{
          entityUris       = SSUri.get (SSStrU.splitDistinct(clientPars.get(SSVarU.entityUris), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          userUris         = SSUri.get (SSStrU.splitDistinct(clientPars.get(SSVarU.userUris), SSStrU.comma));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
