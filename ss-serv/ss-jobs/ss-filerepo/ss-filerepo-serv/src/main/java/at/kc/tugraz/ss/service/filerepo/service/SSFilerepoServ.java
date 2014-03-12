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
 package at.kc.tugraz.ss.service.filerepo.service;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import at.kc.tugraz.ss.service.filerepo.impl.SSFilerepoImpl;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import at.kc.tugraz.ss.service.filerepo.datatypes.SSFileRepoFileAccessProperty;
import at.kc.tugraz.ss.service.filerepo.service.task.SSFileRepoWritingMinutesUpdateTask;
import java.util.HashMap;
import java.util.Map;

public class SSFilerepoServ extends SSServA{
  
  public  static final SSFilerepoServ                            inst            = new SSFilerepoServ();
  private static final Map<String, SSFileRepoFileAccessProperty> fileAccessProps = new HashMap<String, SSFileRepoFileAccessProperty>();
  
  private SSFilerepoServ(){}
  
  @Override
  protected SSServImplA createServImplForThread() throws Exception{
    return new SSFilerepoImpl((SSFileRepoConf)servConf, fileAccessProps);
  }
  
  public void schedule() throws Exception{
    
    if(servConf.use){
      
      SSDateU.scheduleAtFixedRate(
        new SSFileRepoWritingMinutesUpdateTask(),
        SSDateU.getDateForNextMinute(),
        SSDateU.minuteInMilliSeconds);
    }
  }

  @Override
  protected void initServSpecificStuff() throws Exception{
    regServForManagingEntities(SSEntityEnum.file);
  }
}