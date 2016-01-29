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
package at.kc.tugraz.ss.service.userevent.datatypes.pars;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.util.*;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.tugraz.sss.serv.reg.SSServErrReg;
import java.sql.*;

public class SSUECountGetPar extends SSServPar{
  
  public SSUri            forUser    = null;
  public SSUri            entity     = null;
  public SSUEE            type       = null;
  public Long             startTime  = null;
  public Long             endTime    = null;
  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser); 
  }
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity); 
  }

  public void setType(final String type) throws SSErr{
    this.type = SSUEE.get(type); 
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }
  
  public SSUECountGetPar(){}
  
  public SSUECountGetPar(
    final SSServPar servPar,
    final SSUri     user,
    final SSUri     forUser, 
    final SSUri     entity,
    final SSUEE     type, 
    final Long      startTime, 
    final Long      endTime, 
    final boolean   withUserRestriction){
    
    super(SSVarNames.userEventCountGet, null, user, servPar.sqlCon);
  
    this.forUser             = forUser;
    this.entity              = entity;
    this.type                = type;
    this.startTime           = startTime;
    this.endTime             = endTime;
    this.withUserRestriction = withUserRestriction;
  }
}