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
package at.kc.tugraz.ss.adapter.rest.v1;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileCanWritePar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileExtGetPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileSetReaderOrWriterPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUserFileWritesPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileCanWriteRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileExtGetRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileGetEditingFilesRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileSetReaderOrWriterRet;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSSocketCon;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
@Api( value = "SSAdapterRESTFile")
public class SSAdapterRESTFile{

  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileExtGet")
  @ApiOperation(
    value = "retrieve a file's extension",
    response = SSFileExtGetRet.class)
  public String fileExtGet(final SSFileExtGetPar input){
    
    try{
      return new SSSocketCon(
        SSRestMainV1.conf.sss.host,
        SSRestMainV1.conf.sss.port).prepRetFullToClient(
          new SSFileExtGetRet(
            SSFileExtE.ext(SSStrU.removeTrailingSlash(input.file)),
            SSServOpE.fileExtGet),
          SSServOpE.fileExtGet);
      
    }catch(Exception error){
      SSLogU.err(error);
      return null;
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileCanWrite")
  @ApiOperation(
    value = "query whether given file can be downloaded with write access",
    response = SSFileCanWriteRet.class)
  public String fileCanWrite(final SSFileCanWritePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.fileCanWrite);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileSetReaderOrWriter")
  @ApiOperation(
    value = "set user being writer or reaader for given file",
    response = SSFileSetReaderOrWriterRet.class)
  public String fileSetReaderOrWriter(final SSFileSetReaderOrWriterPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.fileSetReaderOrWriter);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileUserFileWrites")
  @ApiOperation(
    value = "retrieve files user currently could replace when uploading respective file again as he is writer",
    response = SSFileGetEditingFilesRet.class)
  public String fileUserFileWrites(final SSFileUserFileWritesPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.fileUserFileWrites);
  }
}
