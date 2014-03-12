/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
 package at.kc.tugraz.ss.service.filerepo.datatypes.rets;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import java.util.HashMap;
import java.util.Map;

public class SSFileReplaceRet extends SSServRetI{
  
  public  SSUri         uri;
  public  SSUri         user;
  public  String        status;

  public SSFileReplaceRet(
    SSUri            uri, 
    SSUri            user, 
    String           status, 
    SSMethU           op){
    
    super(op);
    
    this.uri           = uri;
    this.user          = user;
    this.status        = status;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld           = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri,     SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.user,    SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.status,  SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }

  public String getUser() throws Exception{
    return SSUri.toStrWithoutSlash(user);
  }

  public String getStatus(){
    return status;
  }
}