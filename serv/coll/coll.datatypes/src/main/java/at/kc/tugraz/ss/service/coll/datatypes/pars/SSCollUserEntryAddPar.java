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
 package at.kc.tugraz.ss.service.coll.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "collUserEntryAdd request parameter")
public class SSCollUserEntryAddPar extends SSServPar{
  
  @ApiModelProperty( 
    required = true, 
    value = "collection to add an entity to")
  public SSUri        coll        = null;
  
  @XmlElement
  public void setColl(final String coll) throws Exception{
    this.coll = SSUri.get(coll);
  }
  
  @ApiModelProperty( 
    required = true, 
    value = "title of the collection entry ")
  public SSLabel      label       = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "either null for the creation of new sub-collection, an existing collection or an entity")
  public SSUri        entry       = null;
  
  @XmlElement
  public void setEntry(final String entry) throws Exception{
    this.entry = SSUri.get(entry);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether a new collection should be created instead of adding an existing one")
  public Boolean      addNewColl  = null;
  
  public SSCollUserEntryAddPar(){}
    
  public SSCollUserEntryAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        coll           = (SSUri)       pars.get(SSVarU.coll);
        entry          = (SSUri)       pars.get(SSVarU.entry);
        label          = (SSLabel)     pars.get(SSVarU.label);
        addNewColl     = (Boolean)     pars.get(SSVarU.addNewColl);
      }
      
      if(par.clientJSONObj != null){
        coll  = SSUri.get       (par.clientJSONObj.get(SSVarU.coll).getTextValue());
        label = SSLabel.get     (par.clientJSONObj.get(SSVarU.label).getTextValue());
        
        try{
          addNewColl     = par.clientJSONObj.get(SSVarU.addNewColl).getBooleanValue();
        }catch(Exception error){}
        
        try{
          entry      = SSUri.get       (par.clientJSONObj.get(SSVarU.entry).getTextValue());
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getColl(){
    return SSStrU.removeTrailingSlash(coll);
  }
  
  public String getEntry(){
    return SSStrU.removeTrailingSlash(entry);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
}
