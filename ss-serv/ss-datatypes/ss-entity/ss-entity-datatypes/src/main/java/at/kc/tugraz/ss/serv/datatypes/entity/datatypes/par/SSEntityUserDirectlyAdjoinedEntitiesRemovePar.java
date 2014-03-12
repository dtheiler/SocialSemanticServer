/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSEntityUserDirectlyAdjoinedEntitiesRemovePar extends SSServPar{
  
  public SSUri   entityUri           = null;
  public Boolean removeUserTags      = null;
  public Boolean removeUserRatings   = null;
  public Boolean removeFromUserColls = null;
  public Boolean removeUserLocations = null;
  
  public SSEntityUserDirectlyAdjoinedEntitiesRemovePar(final SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        entityUri            = (SSUri)     pars.get(SSVarU.entityUri);
        removeUserTags       = (Boolean)   pars.get(SSVarU.removeUserTags);
        removeUserRatings    = (Boolean)   pars.get(SSVarU.removeUserRatings);
        removeFromUserColls  = (Boolean)   pars.get(SSVarU.removeFromUserColls);
        removeUserLocations  = (Boolean)   pars.get(SSVarU.removeUserLocations);
      }
      
      if(clientPars != null){
        entityUri     = SSUri.get      (clientPars.get(SSVarU.entityUri));
        
        try{
          removeUserTags   = Boolean.valueOf(clientPars.get(SSVarU.removeUserTags));
        }catch(Exception error){}
        
        try{
          removeUserRatings   = Boolean.valueOf(clientPars.get(SSVarU.removeUserRatings));
        }catch(Exception error){}
        
        try{
          removeFromUserColls   = Boolean.valueOf(clientPars.get(SSVarU.removeFromUserColls));
        }catch(Exception error){}
        
        try{
          removeUserLocations   = Boolean.valueOf(clientPars.get(SSVarU.removeUserLocations));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}