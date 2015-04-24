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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

import at.tugraz.sss.serv.SSServErrReg;
@XmlRootElement
@ApiModel(value = "activityAdd request parameter")
public class SSActivityAddPar extends SSServPar{

  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "type of the activity to be stored")
  public SSActivityE            type             = null;
  
  @ApiModelProperty(
    required = false,
    value = "targeted entities")
  public SSUri            entity         = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @ApiModelProperty(
    required = false,
    value = "involved users")
  public List<SSUri>            users            = new ArrayList<>();
  
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
  
  @ApiModelProperty(
    required = false,
    value = "involved entities")
  public List<SSUri>            entities         = new ArrayList<>();
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  @ApiModelProperty(
    required = false,
    value = "possible comments attached to this activity")
  public List<SSTextComment>    comments         = new ArrayList<>();
  
  @XmlElement
  public void setComments(final List<String> comments) throws Exception{
    this.comments = SSTextComment.get(comments);
  }
  
  @JsonIgnore
  public Long                   creationTime     = null;
  
  public SSActivityAddPar( ){}
    
  public SSActivityAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        
        type          = (SSActivityE)         pars.get(SSVarNames.type);
        entity        = (SSUri)               pars.get(SSVarNames.entity);
        users         = (List<SSUri>)         pars.get(SSVarNames.users);
        entities      = (List<SSUri>)         pars.get(SSVarNames.entities);
        comments      = (List<SSTextComment>) pars.get(SSVarNames.comments);
        creationTime  = (Long)                pars.get(SSVarNames.creationTime);
      }
      
      if(par.clientJSONObj != null){
        
        type       = SSActivityE.get   (par.clientJSONObj.get(SSVarNames.type).getTextValue());
        
        try{
            entity = SSUri.get(par.clientJSONObj.get(SSVarNames.entity).getTextValue());
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.users)) {
            users.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.entities)) {
            entities.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.comments)) {
            comments.add(SSTextComment.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  
  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
    public List<String> getComments() throws Exception{
    return SSStrU.removeTrailingSlash(comments);
  }
  
  public String getType() throws Exception{
    return SSStrU.toStr(type);
  }
}
