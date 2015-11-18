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
package at.tugraz.sss.servs.entity.datatypes.par;

import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSStrU;

public class SSEntitiesAccessibleGetPar extends SSServPar{
  
  public List<SSEntityE>      types                 = new ArrayList<>();
  public List<SSUri>          authors               = new ArrayList<>();
  public String               pagesID               = null;
  public Integer              pageNumber            = null;
  public SSEntityDescriberPar descPar               = null;
   
  public List<String> getTypes(){
    return SSStrU.toStr(types);
  }
  
  public void setTypes(List<String> types) throws Exception{
    this.types = SSEntityE.get(types);
  }
  
  public List<String> getAuthors(){
    return SSStrU.removeTrailingSlash(authors);
  }

  public void setAuthors(final List<String> authors) throws Exception{
    this.authors = SSUri.get(authors);
  }
  
  public SSEntitiesAccessibleGetPar(){}
  
  public SSEntitiesAccessibleGetPar(
    final SSUri                user, 
    final List<SSEntityE>      types,
    final List<SSUri>          authors,
    final SSEntityDescriberPar descPar,
    final Boolean              withUserRestriction) throws Exception{
    
    super(SSServOpE.entitiesAccessibleGet, null, user);

    SSEntityE.addDistinctWithoutNull(this.types,   types);
    SSUri.addDistinctWithoutNull    (this.authors, authors);
    
    this.descPar              = descPar;
    this.withUserRestriction  = withUserRestriction;
  }
}