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
 package at.tugraz.sss.servs.file.api;

import at.tugraz.sss.servs.file.datatype.ret.SSFileUploadRet;
import at.tugraz.sss.servs.file.datatype.ret.SSFileAddRet;
import at.tugraz.sss.servs.file.datatype.ret.SSFileDownloadRet;
import at.tugraz.sss.servs.file.datatype.par.SSFileDownloadPar;
import at.tugraz.sss.servs.file.datatype.par.SSFilesDeleteNotRegisteredPar;
import at.tugraz.sss.servs.file.datatype.par.SSFileUploadPar;
import at.tugraz.sss.servs.file.datatype.SSFile;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSFileGetPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
import java.util.List;

public interface SSFileServerI extends SSServServerI{

  public SSFileAddRet      fileAdd                  (final SSEntityFileAddPar             par) throws SSErr;
  public SSFile            fileGet                  (final SSFileGetPar                   par) throws SSErr;
  public List<SSEntity>    filesGet                 (final SSEntityFilesGetPar            par) throws SSErr;
  public SSFileDownloadRet fileDownload             (final SSFileDownloadPar              par) throws SSErr;
  public SSFileUploadRet   fileUpload               (final SSFileUploadPar                par) throws SSErr;
  public boolean           filesDeleteNotRegistered (final SSFilesDeleteNotRegisteredPar  par) throws SSErr;
}