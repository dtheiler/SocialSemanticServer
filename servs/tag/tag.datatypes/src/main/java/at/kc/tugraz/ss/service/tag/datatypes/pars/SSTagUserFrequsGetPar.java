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
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import at.tugraz.sss.serv.SSServErrReg;
public class SSTagUserFrequsGetPar extends SSServPar{

  public SSUri              forUser              = null;
  public List<SSUri>        entities             = new ArrayList<>();
  public List<SSTagLabel>   labels               = new ArrayList<>();
  public SSSpaceE           space                = null;
  public Long               startTime            = null;
  public Boolean            useUsersEntities     = false;
  
  public SSTagUserFrequsGetPar(
    final SSServOpE          op,
    final String           key, 
    final SSUri            user, 
    final SSUri            forUser, 
    final List<SSUri>      entities, 
    final List<SSTagLabel> labels, 
    final SSSpaceE         space, 
    final Long             startTime,
    final Boolean          useUsersEntities){
    
    super(op, key, user);
    
    this.forUser = forUser;
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    if(labels != null){
      this.labels.addAll(labels);
    }
    
    this.space                = space;
    this.startTime            = startTime;
    this.useUsersEntities     = useUsersEntities;
  }
    
  public SSTagUserFrequsGetPar(final SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        forUser             = (SSUri)                        pars.get(SSVarU.forUser);
        entities            = (List<SSUri>)                  pars.get(SSVarU.entities);
        labels              = SSTagLabel.get((List<String>)  pars.get(SSVarU.labels));
        space               = (SSSpaceE)                     pars.get(SSVarU.space);
        startTime           = (Long)                         pars.get(SSVarU.startTime);
        useUsersEntities    = (Boolean)                      pars.get(SSVarU.useUsersEntities);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          forUser   = SSUri.get (par.clientJSONObj.get(SSVarU.forUser).getTextValue());
        }catch(Exception error){}
          
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entities)) {
            entities.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.labels)) {
            labels.add(SSTagLabel.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
         
        try{
          space      = SSSpaceE.get  (par.clientJSONObj.get(SSVarU.space).getTextValue());
        }catch(Exception error){}
        
        try{
          startTime      = par.clientJSONObj.get(SSVarU.startTime).getLongValue();
        }catch(Exception error){}
        
        try{
          useUsersEntities      = par.clientJSONObj.get(SSVarU.useUsersEntities).getBooleanValue();
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }

  public List<String> getLabels() throws Exception{
    return SSStrU.toStr(labels);
  }

  public String getSpace(){
    return SSStrU.toStr(space);
  }
}