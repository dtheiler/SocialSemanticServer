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
package at.kc.tugraz.sss.appstacklayout.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "appStackLayoutTileAdd request parameter")
public class SSAppStackLayoutTileAddPar extends SSServPar{
  
  @ApiModelProperty(
    required = false,
    value = "stack")
  public SSUri               stack        = null;
  
  @XmlElement
  public void setStack(final String stack) throws Exception{
    this.stack = SSUri.get(stack);
  }
  
  @ApiModelProperty(
    required = false,
    value = "link to the app the tile conains")
  public SSUri               appLink        = null;
  
  @XmlElement
  public void setAppLink(final String appLink) throws Exception{
    this.appLink = SSUri.get(appLink);
  }
  
  @ApiModelProperty(
    required = false,
    value = "name")
  public SSLabel               label        = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  public SSAppStackLayoutTileAddPar(){}
  
  public SSAppStackLayoutTileAddPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        stack               = (SSUri)         pars.get(SSVarU.stack);
        appLink             = (SSUri)         pars.get(SSVarU.appLink);
        label               = (SSLabel)       pars.get(SSVarU.label);
      }
      
      if(par.clientJSONObj != null){
        
        stack               = SSUri.get(par.clientJSONObj.get(SSVarU.stack).getTextValue());
        
        try{
          appLink =  SSUri.get(par.clientJSONObj.get(SSVarU.appLink).getTextValue());
        }catch(Exception error){}
        
        try{
          label =  SSLabel.get(par.clientJSONObj.get(SSVarU.label).getTextValue());
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  
  public String getStack(){
    return SSStrU.removeTrailingSlash(stack);
  }
    
  public String getAppLink(){
    return SSStrU.removeTrailingSlash(appLink);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
}