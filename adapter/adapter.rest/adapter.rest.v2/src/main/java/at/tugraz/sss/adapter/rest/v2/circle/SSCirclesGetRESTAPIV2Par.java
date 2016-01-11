/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.adapter.rest.v2.circle;

import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import io.swagger.annotations.*;

import java.util.List;

@ApiModel
public class SSCirclesGetRESTAPIV2Par{
  
  @ApiModelProperty(
    required = false,
    value = "user to retrieve circles for")
  public SSUri   forUser             = null;
  
  
  public void setForUser(final String forUser) throws Exception{
     this.forUser = SSUri.get(forUser);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entity types to include in 'entitites' only")
  public List<SSEntityE>   entityTypesToIncludeOnly             = null;
  
  
  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly) throws Exception{
     this.entityTypesToIncludeOnly = SSEntityE.get(entityTypesToIncludeOnly);
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "whether the profile picture of the circle shall be set")
  public boolean setProfilePicture = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "")
  public boolean setThumb = false;
  
  
  @ApiModelProperty(
    required = false,
    value = "")
  public boolean setTags = false;
    
  
  @ApiModelProperty(
    required = false,
    value = "")
  public boolean invokeEntityHandlers = true;
    
  public SSCirclesGetRESTAPIV2Par(){}
}