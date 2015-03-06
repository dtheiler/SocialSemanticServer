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
 package at.kc.tugraz.ss.service.disc.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "discUserWithEntriesGet request parameter")
public class SSDiscUserWithEntriesGetPar extends SSServPar{
  
  @ApiModelProperty( 
    required = true, 
    value = "discussion to retrieve")
  public SSUri  disc             = null;
  
  @XmlElement
  public void setDisc(final String disc) throws Exception{
    this.disc = SSUri.get(disc);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = true, 
    value = "max entries to retrieve")
  public int    maxEntries       = 10;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether comments of threads and entries shall be retrieved")
  public Boolean    includeComments       = null;
      
  public SSDiscUserWithEntriesGetPar(){}
    
  public SSDiscUserWithEntriesGetPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        disc            = (SSUri)   pars.get(SSVarU.disc);
        maxEntries      = (Integer) pars.get(SSVarU.maxEntries);
        includeComments = (Boolean) pars.get(SSVarU.includeComments);
      }
      
      if(par.clientJSONObj != null){
        disc         = SSUri.get       (par.clientJSONObj.get(SSVarU.disc).getTextValue());
        maxEntries   = par.clientJSONObj.get(SSVarU.maxEntries).getIntValue();
        
        try{
          includeComments = par.clientJSONObj.get(SSVarU.includeComments).getBooleanValue();
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getDisc(){
    return SSStrU.removeTrailingSlash(disc);
  }
}
