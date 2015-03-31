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
package at.kc.tugraz.ss.adapter.rest.v1.par;

import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "circleCreate request parameter")
public class SSCircleCreateRESTAPIV1Par{
  
  @ApiModelProperty( 
    value = "the user's identifier", 
    required = true)
  public        SSUri                user          = null;
  
  @XmlElement 
  public void setUser(final String user) throws Exception{
    this.user = SSUri.get(user);
  }
  
  @XmlElement 
  @ApiModelProperty( 
    value = "the user's access tocken", 
    required = true)
  public String key                    = null;
  
  @ApiModelProperty(
    required = true,
    value = "circle name")
  public SSLabel                label          = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities to add")
  public List<SSUri>            entities       = new ArrayList<>();
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    try{ this.entities = SSUri.get(entities); } catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "users to add")
  public List<SSUri>            users          = new ArrayList<>();
  
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    try{ this.users = SSUri.get(users); } catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "textual annotation")
  public SSTextComment          description    = null;

  @XmlElement
  public void setDescription(final String description) throws Exception{
    try{ this.description = SSTextComment.get(description); } catch(Exception error){}
  }
  
  public SSCircleCreateRESTAPIV1Par(){}
}
