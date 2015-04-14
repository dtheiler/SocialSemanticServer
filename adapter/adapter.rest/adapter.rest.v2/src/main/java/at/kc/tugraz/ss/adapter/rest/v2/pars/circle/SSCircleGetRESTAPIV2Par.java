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
package at.kc.tugraz.ss.adapter.rest.v2.pars.circle;

import at.tugraz.sss.serv.SSEntityE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "circleGet request parameter")
public class SSCircleGetRESTAPIV2Par{
  
  @ApiModelProperty(
    required = false,
    value = "entity types to include in 'entitites' only")
  public List<SSEntityE>   entityTypesToIncludeOnly             = new ArrayList<>();
  
  @XmlElement
  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly){
    try{ this.entityTypesToIncludeOnly.addAll(SSEntityE.get(entityTypesToIncludeOnly)); }catch(Exception error){}
  }
  
  public SSCircleGetRESTAPIV2Par(){}
}