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
package at.kc.tugraz.ss.activity.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "activitiesUserGet request parameter")
public class SSActivitiesUserGetPar extends SSServPar{

  @XmlElement
  @ApiModelProperty(required = false, value = "types of activities to be queried (optional)" )
  public List<SSActivityE>      types            = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( required = false, value = "users which have been involved in activities (optional)" )
  public List<SSUri>            users            = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( required = false, value = "entities which have been involved in activities as targets (e.g. the target for a discussion) (optional)" )
  public List<SSUri>            entities         = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( required = false, value = "groups for which activities shall be retrieved (optional)" )
  public List<SSUri>            circles          = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty(required = false, value = "time frame start (optional)" )
  public Long                   startTime        = null; 
  
  @XmlElement
  @ApiModelProperty(required = false, value = "time frame end (optional)" )
  public Long                   endTime          = null;

  public SSActivitiesUserGetPar(){}
  
  public SSActivitiesUserGetPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        
        types           = (List<SSActivityE>)   pars.get(SSVarU.types);
        users           = (List<SSUri>)         pars.get(SSVarU.users);
        entities        = (List<SSUri>)         pars.get(SSVarU.entities);
        circles         = (List<SSUri>)         pars.get(SSVarU.circles);
        startTime       = (Long)                pars.get(SSVarU.startTime);
        endTime         = (Long)                pars.get(SSVarU.endTime);
      }
      
      if(clientPars != null){
        
        try{ types      = SSActivityE.get (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.types),    SSStrU.comma));    }catch(Exception error){}
        try{ users      = SSUri.get       (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.users),    SSStrU.comma));    }catch(Exception error){}
        try{ entities   = SSUri.get       (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.entities), SSStrU.comma));    }catch(Exception error){}
        try{ circles    = SSUri.get       (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.circles), SSStrU.comma));     }catch(Exception error){}
        try{ startTime  = Long.valueOf    (clientPars.get(SSVarU.startTime));                                                          }catch(Exception error){}
        try{ endTime    = Long.valueOf    (clientPars.get(SSVarU.endTime));                                                            }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public List<String> getCircles() throws Exception{
    return SSStrU.removeTrailingSlash(circles);
  }
   
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getTypes() throws Exception{
    return SSStrU.toStr(types);
  }
}