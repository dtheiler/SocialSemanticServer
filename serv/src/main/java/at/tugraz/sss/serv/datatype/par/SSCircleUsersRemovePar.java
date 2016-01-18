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
package at.tugraz.sss.serv.datatype.par;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.par.SSServPar;import at.tugraz.sss.serv.util.*;
 import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSCircleUsersRemovePar extends SSServPar{
  
  public SSUri                 circle                       = null;
  public List<SSUri>           users                        = new ArrayList<>();

  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }

  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users); 
  }
  
  public SSCircleUsersRemovePar(){}
  
  public SSCircleUsersRemovePar(
    final SSServPar servPar,
    final SSUri          user,
    final SSUri          circle,
    final List<SSUri>    users,
    final boolean        withUserRestriction,
    final boolean        shouldCommit) throws Exception{
    
    super(SSVarNames.circleUsersRemove, null, user, servPar.sqlCon);
    
    this.circle  = circle;
    
    SSUri.addDistinctWithoutNull(this.users, users);
    
    this.withUserRestriction          = withUserRestriction;
    this.shouldCommit                 = shouldCommit;
  }
}