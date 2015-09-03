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
package at.tugraz.sss.servs.mail.datatype.ret;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSServRetI;
import java.util.Map;

public class SSMailSendRet extends SSServRetI{

  public Boolean worked = null;

  @Override
  public Map<String, Object> jsonLDDesc(){
    throw new UnsupportedOperationException();
  }
  
  public static SSMailSendRet get(
    final Boolean worked){
    
    return new SSMailSendRet(worked);
  }
  
  private SSMailSendRet(
    final Boolean worked){
    
    super(SSServOpE.mailSend);
    
    this.worked = worked;
  }
}