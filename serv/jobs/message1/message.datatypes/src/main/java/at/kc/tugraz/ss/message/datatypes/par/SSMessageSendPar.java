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
package at.kc.tugraz.ss.message.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "messageSend request parameter")
public class SSMessageSendPar extends SSServPar{
  
  @ApiModelProperty( 
    required = true, 
    value = "user to send message to")
  public SSUri         forUser    = null;
  
  @XmlElement
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = true, 
    value = "message to send")
  public SSTextComment       message   = null;
  
  @XmlElement
  public void setDescription(final String message) throws Exception{
    this.message = SSTextComment.get(message);
  }
  
  public SSMessageSendPar(){}
    
  public SSMessageSendPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
	
      if(pars != null){
	    forUser                = (SSUri)           pars.get(SSVarU.forUser);
       message               = (SSTextComment)   pars.get(SSVarU.message);
      }
      
      if(par.clientJSONObj != null){
        this.forUser   = SSUri.get         (par.clientJSONObj.get(SSVarU.forUser).getTextValue());
        this.message   = SSTextComment.get (par.clientJSONObj.get(SSVarU.message).getTextValue());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public String getMessage() throws Exception{
    return SSStrU.toStr(message);
  }
}
