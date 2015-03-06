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
package at.kc.tugraz.ss.circle.api;

import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import java.util.List;

public interface SSCircleServerI{

  public SSUri                           circleCreate                             (final SSServPar parA) throws Exception;
  public SSUri                           circleUsersAdd                           (final SSServPar parA) throws Exception;
  public SSUri                           circleEntitiesAdd                        (final SSServPar parA) throws Exception;
  public SSCircleE                       circleMostOpenCircleTypeGet              (final SSServPar parA) throws Exception;
  public List<SSCircleE>                 circleTypesGet                           (final SSServPar parA) throws Exception;
  public SSEntityCircle                  circleGet                                (final SSServPar parA) throws Exception;
  public List<SSEntityCircle>            circlesGet                               (final SSServPar parA) throws Exception;
  public List<SSEntity>                  circleEntitiesGet                        (final SSServPar parA) throws Exception;
  public List<SSEntity>                  circleEntityUsersGet                     (final SSServPar parA) throws Exception;
  public SSUri                           circlePrivURIGet                         (final SSServPar parA) throws Exception;
  public SSUri                           circlePubURIGet                          (final SSServPar parA) throws Exception;
  public SSEntity                        circleUserCan                            (final SSServPar parA) throws Exception;
  public SSUri                           circleEntityShare                        (final SSServPar parA) throws Exception;
  public SSUri                           circleEntityPublicSet                    (final SSServPar parA) throws Exception;
  public List<SSUri>                     circleEntitiesRemove                     (final SSServPar parA) throws Exception;

  public void                            entityEntityToPrivCircleAdd              (final SSServPar parA) throws Exception;
  public void                            entityEntityToPubCircleAdd               (final SSServPar parA) throws Exception;
}