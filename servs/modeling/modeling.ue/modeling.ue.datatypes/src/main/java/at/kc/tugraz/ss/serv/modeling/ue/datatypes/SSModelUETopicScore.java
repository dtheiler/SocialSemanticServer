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
 package at.kc.tugraz.ss.serv.modeling.ue.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSEntityA;
import java.util.HashMap;
import java.util.Map;

public class SSModelUETopicScore extends SSEntityA {
  
	public String         topic   = null;
	public Integer        level   = -1;
	public Integer        frequ   = -1;
	
	public SSModelUETopicScore(
			String      topic,
			int         level,
			int         frequ)throws Exception{
    
    super(level);
		
		this.topic      = topic;
		this.level      = level;
		this.frequ      = frequ;
	}
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld = new HashMap<>();
    
    ld.put(SSVarU.topic, SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.level, SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    ld.put(SSVarU.frequ, SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    
    return ld;
  }
   
  /* getters to allow for json enconding */
  public String getTopic(){
    return topic;
  }

  public int getLevel(){
    return level;
  }

  public int getFrequ(){
    return frequ;
  }
}