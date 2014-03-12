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
 package at.kc.tugraz.ss.service.tag.api;

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import java.util.List;

public interface SSTagServerI {
 
  public Boolean                         tagsAdd                      (final SSServPar parA) throws Exception;
  public Boolean                         tagsAddAtCreationTime        (final SSServPar parA) throws Exception;
  public Boolean                         tagAdd                       (final SSServPar parA) throws Exception;
  public Boolean                         tagAddAtCreationTime         (final SSServPar parA) throws Exception;
  public Boolean                         tagsRemove                   (final SSServPar parA) throws Exception;
  public Boolean                         tagsUserRemove               (final SSServPar parA) throws Exception;
  public List<SSTag>                     tagsUserGet                  (final SSServPar parA) throws Exception;
  public List<SSTagFrequ>                tagUserFrequsGet             (final SSServPar parA) throws Exception;
  public List<SSUri>                     tagUserEntitiesForTagGet     (final SSServPar parA) throws Exception;
}