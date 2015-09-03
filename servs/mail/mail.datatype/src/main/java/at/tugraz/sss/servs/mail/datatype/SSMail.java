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
package at.tugraz.sss.servs.mail.datatype;

import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSUri;

public class SSMail extends SSEntity{
  
  public String subject = null;
  public String content = null;

  @Override
  public Object jsonLDDesc(){
    throw new UnsupportedOperationException();
  }
  
  public static SSMail get(
    final SSMail     mail, 
    final SSEntity   entity) throws Exception{
    
    return new SSMail(mail, entity);
  }
   
  public static SSMail get(
    final SSUri  id,
    final String subject, 
    final String content) throws Exception{
    
    return new SSMail(id, subject, content);
  }
  
  protected SSMail(
    final SSMail   mail,
    final SSEntity entity) throws Exception{
    
    super(mail, entity);
  }
  
  protected SSMail(
    final SSUri  id,
    final String subject, 
    final String content) throws Exception{
    
    super(id, SSEntityE.mail);
    
    this.subject = subject;
    this.content = content;
  }
}