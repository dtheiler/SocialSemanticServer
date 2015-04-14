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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "collUserEntriesDelete request parameter")
public class SSCollUserEntriesDeletePar extends SSServPar{
  
  @ApiModelProperty( 
    required = true, 
    value = "collection to delete entries from")
  public SSUri       coll     = null;
  
  @XmlElement
  public void setColl(final String coll) throws Exception{
    this.coll = SSUri.get(coll);
  }
  
  @ApiModelProperty( 
    required = true, 
    value = "items to delete")
  public List<SSUri> entries  = new ArrayList<>();
  
  @XmlElement
  public void setEntries(final List<String> entries) throws Exception{
    this.entries = SSUri.get(entries);
  }
      
  public SSCollUserEntriesDeletePar(){}
  
  public SSCollUserEntriesDeletePar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        coll     = (SSUri)       pars.get(SSVarU.coll);
        entries  = (List<SSUri>) pars.get(SSVarU.entries);
      }
      
      if(par.clientJSONObj != null){
        
        coll    = SSUri.get  (par.clientJSONObj.get(SSVarU.coll).getTextValue());
        
        for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entries)) {
          entries.add(SSUri.get(objNode.getTextValue()));
        }
      }   
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getColl(){
    return SSStrU.removeTrailingSlash(coll);
  }
  
  public List<String> getEntries() throws Exception{
    return SSStrU.removeTrailingSlash(entries);
  }
}