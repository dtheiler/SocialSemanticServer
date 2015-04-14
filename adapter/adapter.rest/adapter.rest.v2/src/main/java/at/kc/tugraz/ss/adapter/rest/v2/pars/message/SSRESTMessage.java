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
package at.kc.tugraz.ss.adapter.rest.v2.pars.message;

import at.kc.tugraz.ss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.message.datatypes.par.SSMessageSendPar;
import at.kc.tugraz.ss.message.datatypes.par.SSMessagesGetPar;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessageSendRet;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessagesGetRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarU;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/messages")
@Api( value = "/messages")
public class SSRESTMessage{
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "retrieve messages for the user",
    response = SSMessagesGetRet.class)
  public Response messagesGetPOST(
    @Context 
      final HttpHeaders headers, 
    
    final SSMessagesGetRESTAPIV2Par input){
    
    final SSMessagesGetPar par;
    
    try{
      
      par =
        new SSMessagesGetPar(
          SSServOpE.messagesGet,
          null,
          null,  
          input.includeRead,
          input.startTime);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/users/{forUser}")
  @ApiOperation(
    value = "send a message to a user",
    response = SSMessageSendRet.class)
  public Response messageSend(
    @Context 
      final HttpHeaders headers, 
    
    @PathParam(SSVarU.forUser) 
      final String forUser,
    
    final SSMessageSendRESTAPIV2Par input){
    
    final SSMessageSendPar par;
    
    try{
      
      par =
        new SSMessageSendPar(
          SSServOpE.messageSend,
          null,
          null,
          SSUri.get(forUser, SSVocConf.sssUri),
          input.message,
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}