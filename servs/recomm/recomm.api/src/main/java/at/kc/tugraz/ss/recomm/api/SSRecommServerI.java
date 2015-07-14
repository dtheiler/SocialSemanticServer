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
package at.kc.tugraz.ss.recomm.api;

import at.kc.tugraz.ss.recomm.datatypes.SSResourceLikelihood;
import at.kc.tugraz.ss.recomm.datatypes.SSUserLikelihood;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommResourcesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUsersPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLikelihood;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServServerI;
import java.util.List;

public interface SSRecommServerI extends SSServServerI{

  public List<SSUserLikelihood>     recommUsers              (final SSRecommUsersPar     par) throws Exception;
  public List<SSTagLikelihood>      recommTags               (final SSRecommTagsPar      par) throws Exception;
  public List<SSResourceLikelihood> recommResources          (final SSRecommResourcesPar par) throws Exception;
  
  
  public Boolean                recommUpdate             (final SSServPar parA) throws Exception;
  public void                   recommUpdateBulk         (final SSRecommUpdateBulkPar par) throws Exception;
  public Boolean                recommUpdateBulkEntities (final SSServPar parA) throws Exception;
  public void                   recommLoadUserRealms     (final SSServPar parA) throws Exception;
}
