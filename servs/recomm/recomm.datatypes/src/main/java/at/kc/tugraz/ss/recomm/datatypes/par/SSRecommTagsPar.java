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
package at.kc.tugraz.ss.recomm.datatypes.par;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSRecommTagsPar extends SSServPar{
  
  public String         realm                = null;
  public SSUri          forUser              = null;
  public SSUri          entity               = null;
  public List<String>   categories           = new ArrayList<>();
  public Integer        maxTags              = 10;
  public boolean        includeOwn           = true;
  public boolean        ignoreAccessRights   = false;
  
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }
  
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public SSRecommTagsPar(){}
  
  public SSRecommTagsPar(
    final SSServPar servPar,
    final SSUri         user,
    final String        realm,
    final SSUri         forUser, 
    final SSUri         entity, 
    final List<String>  categories, 
    final Integer       maxTags, 
    final boolean       includeOwn, 
    final boolean       ignoreAccessRights,
    final boolean       withUserRestriction){
    
    super(SSVarNames.recommTags, null, user, servPar.sqlCon);
    
    this.realm   = realm;
    this.forUser = forUser;
    this.entity  = entity;
    
    if(categories != null){
      this.categories.addAll(categories);
    }
    this.maxTags              = maxTags;
    this.includeOwn           = includeOwn;
    this.ignoreAccessRights   = ignoreAccessRights;
    this.withUserRestriction  = withUserRestriction;
  }
}