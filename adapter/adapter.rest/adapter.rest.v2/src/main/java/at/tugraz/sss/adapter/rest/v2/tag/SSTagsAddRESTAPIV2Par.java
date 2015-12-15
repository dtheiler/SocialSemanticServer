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
package at.tugraz.sss.adapter.rest.v2.tag;

import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSUri;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "tags add request parameter")
public class SSTagsAddRESTAPIV2Par{
  
  @ApiModelProperty(
    required = true,
    value = "tag labels")
  public List<SSTagLabel>     labels        = null;
  
  @XmlElement
  public void setLabels(final List<String> labels) throws Exception{
    this.labels = SSTagLabel.get(labels);
  }
  
  @ApiModelProperty(
    required = true,
    value = "access restriction for the tag (i.e. privateSpace, sharedSpace)")
  public SSSpaceE     space        = null;
  
  @XmlElement
  public void setSpace(final String space) throws Exception{
    this.space = SSSpaceE.get(space);
  }
  
  @ApiModelProperty(
    required = true,
    value = "circle, if space is circleSpace; restricts the tag to be visible to users in circle")
  public SSUri     circle        = null;
  
  @XmlElement
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle, SSVocConf.sssUri);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "timestamp for the tag assignment to be created at in milliseconds")
  public Long         creationTime = null;
  
  public SSTagsAddRESTAPIV2Par(){}
}
