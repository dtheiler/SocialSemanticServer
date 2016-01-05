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
package at.tugraz.sss.adapter.rest.v2.category;

import at.kc.tugraz.ss.category.api.*;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryFrequsGetPar;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesPredefinedGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryAddRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryFrequsGetRet;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/categories")
@Api( value = "/categories")
public class SSRESTCategory{
  
  @PostConstruct
  public void createRESTResource(){
    
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/predefined")
  @ApiOperation(
    value = "retrieve predefined categories",
    response = SSCategoriesPredefinedGetRet.class)
  public Response categoriesPredefinedGet(
    @Context
    final HttpHeaders headers){
    
    final SSCategoriesPredefinedGetPar par;
    
    try{
      
      par =
        new SSCategoriesPredefinedGetPar(
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSCategoryClientI categoryServ = (SSCategoryClientI) SSServReg.getClientServ(SSCategoryClientI.class);
      
      return Response.status(200).entity(categoryServ.categoriesPredefinedGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "add a category for an entity in given space",
    response = SSCategoryAddRet.class)
  public Response categoryAdd(
    @Context
    final HttpHeaders headers,
    
    final SSCategoryAddRESTAPIV2Par input){
    
    final SSCategoryAddPar par;
    
    try{
      par =
        new SSCategoryAddPar(
          null,
          input.entity, //entity
          input.label, //label
          input.space, //space
          input.circle, //circle
          input.creationTime,  //creationTime
          true, //withUserRestriction
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSCategoryClientI categoryServ = (SSCategoryClientI) SSServReg.getClientServ(SSCategoryClientI.class);
      
      return Response.status(200).entity(categoryServ.categoryAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/frequs")
  @ApiOperation(
    value = "retrieve category frequencies",
    response = SSCategoryFrequsGetRet.class)
  public Response categoryFrequsGet(
    @Context
    final HttpHeaders headers){
    
    final SSCategoryFrequsGetPar par;
    
    try{
      par =
        new SSCategoryFrequsGetPar(
          null, //user
          null, //forUser
          null, //entities
          null, //labels
          null, //spaces
          null, //circles
          null, //startTime
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSCategoryClientI categoryServ = (SSCategoryClientI) SSServReg.getClientServ(SSCategoryClientI.class);
      
      return Response.status(200).entity(categoryServ.categoryFrequsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/filtered/frequs")
  @ApiOperation(
    value = "retrieve category frequencies",
    response = SSCategoryFrequsGetRet.class)
  public Response categoryFrequsGetFiltered(
    @Context
    final HttpHeaders headers,
    
    final SSCategoryFrequsGetRESTAPIV2Par input){
    
    final SSCategoryFrequsGetPar par;
    
    try{
      par =
        new SSCategoryFrequsGetPar(
          null,
          input.forUser, //forUser
          input.entities, //entities
          input.labels, //labels
          SSSpaceE.asListWithoutNull(input.space), //spaces
          input.circles, //circles
          input.startTime, //startTime
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSCategoryClientI categoryServ = (SSCategoryClientI) SSServReg.getClientServ(SSCategoryClientI.class);
      
      return Response.status(200).entity(categoryServ.categoryFrequsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
}