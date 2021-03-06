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
package at.tugraz.sss.servs.livingdocument.api;

import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.servs.livingdocument.datatype.SSLivingDocument;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocAddPar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocGetPar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocRemovePar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocUpdatePar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocsGetPar;
import java.util.List;

public interface SSLivingDocServerI {
  
  public SSUri              livingDocAdd    (final SSLivingDocAddPar      par) throws SSErr;
  public SSUri              livingDocUpdate (final SSLivingDocUpdatePar   par) throws SSErr;
  public SSLivingDocument   livingDocGet    (final SSLivingDocGetPar      par) throws SSErr;
  public List<SSEntity>     livingDocsGet   (final SSLivingDocsGetPar     par) throws SSErr;
  public SSUri              livingDocRemove (final SSLivingDocRemovePar   par) throws SSErr;
}
