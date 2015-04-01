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
package at.kc.tugraz.ss.circle.datatypes.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import at.tugraz.sss.serv.SSServErrReg;
public class SSCircleEntitiesAddPar extends SSServPar{
  
  public SSUri       circle               = null;
  public List<SSUri> entities             = new ArrayList<>();
  public Boolean     invokeEntityHandlers = true;
  
  public SSCircleEntitiesAddPar(
    final SSServOpE     op,
    final String      key,
    final SSUri       user,
    final SSUri       circle,
    final List<SSUri> entities,
    final Boolean     withUserRestriction,
    final Boolean     invokeEntityHandlers){
    
    super(op, key, user);
    
    this.circle                 = circle;
    this.invokeEntityHandlers   = invokeEntityHandlers;
    
    if(entities != null){
      this.entities.addAll(entities);
    }
  }
  
  public SSCircleEntitiesAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        circle               = (SSUri)         pars.get(SSVarU.circle);
        entities             = (List<SSUri>)   pars.get(SSVarU.entities);
        invokeEntityHandlers = (Boolean)       pars.get(SSVarU.invokeEntityHandlers);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          invokeEntityHandlers = par.clientJSONObj.get(SSVarU.invokeEntityHandlers).getBooleanValue();
        }catch(Exception error){}
        
        try{
          circle         = SSUri.get (par.clientJSONObj.get(SSVarU.circle).getTextValue());
        }catch(Exception error){}
        
        for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entities)) {
          entities.add(SSUri.get(objNode.getTextValue()));
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
   /* json getters */
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
}
