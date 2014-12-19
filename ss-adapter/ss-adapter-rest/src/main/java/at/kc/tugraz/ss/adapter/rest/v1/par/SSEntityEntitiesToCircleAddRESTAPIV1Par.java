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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityUserEntitiesToCircleAdd request parameter")
public class SSEntityEntitiesToCircleAddRESTAPIV1Par{
  
  @XmlElement 
  @ApiModelProperty( 
    value = "operation to be executed", 
    required = true)
  public        SSMethU              op            = null;
  
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
    value = "circle to add entities to")
  public SSUri       circle  = null;
  
  @XmlElement
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }
  
  @ApiModelProperty(
    required = true,
    value = "entities to add")
  public List<SSUri> entities = new ArrayList<>();
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  public SSEntityEntitiesToCircleAddRESTAPIV1Par(){}
}
