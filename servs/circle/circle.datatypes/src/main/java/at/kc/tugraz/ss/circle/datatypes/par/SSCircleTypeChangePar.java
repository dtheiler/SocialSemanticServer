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
package at.kc.tugraz.ss.circle.datatypes.par;

import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;

public class SSCircleTypeChangePar extends SSServPar{
  
  public SSUri     circle   = null;
  public SSCircleE type     = null;

  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }
  
  public String getType() throws Exception{
    return SSStrU.toStr(type);
  }
  
  public void setType(final String type) throws Exception{
    this.type = SSCircleE.get(type);
  }

  public SSCircleTypeChangePar(){}
  
  public SSCircleTypeChangePar(
    final SSUri          user,
    final SSUri          circle,
    final SSCircleE      type,
    final Boolean        withUserRestriction,
    final Boolean        shouldCommit) throws Exception{
    
    super(SSServOpE.circleTypeChange, null, user);
    
    this.circle                 = circle;
    this.type                   = type;
    this.withUserRestriction    = withUserRestriction;
    this.shouldCommit           = shouldCommit;
  }
}