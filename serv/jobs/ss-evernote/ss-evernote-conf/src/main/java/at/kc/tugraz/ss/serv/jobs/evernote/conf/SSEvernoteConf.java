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
package at.kc.tugraz.ss.serv.jobs.evernote.conf;

import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import java.util.ArrayList;
import java.util.List;

public class SSEvernoteConf extends SSServConfA{
  
  public String       companyName         = "KTI";
  public String       appName             = "EvernoteDataSync";
  public String       appVersion          = "1.0";
  public String       evernoteEnvironment = "sandbox";
  public List<String> authTokens          = new ArrayList<>();
  public List<String> authEmails          = new ArrayList<>();
  public List<String> groups              = new ArrayList<>();
  
  public static SSEvernoteConf copy(final SSEvernoteConf orig){
    
    final SSEvernoteConf copy = (SSEvernoteConf) SSServConfA.copy(orig, new SSEvernoteConf());
    
    copy.companyName             = orig.companyName;
    copy.appName                 = orig.appName;
    copy.appVersion              = orig.appVersion;
    copy.evernoteEnvironment     = orig.evernoteEnvironment;
    
    if(orig.authTokens == null){
      copy.authTokens = null;
    }else{
      copy.authTokens.addAll(orig.authTokens);
    }
    
    if(orig.authEmails == null){
      copy.authEmails = null;
    }else{
      copy.authEmails = orig.authEmails;
    }
    
    if(orig.groups != null){
      copy.groups.addAll(orig.groups);
    }
    
    return copy;
  }
}
