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
 package at.kc.tugraz.ss.service.disc.datatypes.pars;

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import java.util.ArrayList;
import java.util.List;

public class SSDiscEntryUpdatePar extends SSServPar{
  
  public SSUri               entry                  = null;
  public SSTextComment       content                = null;
  public List<SSUri>         entitiesToRemove       = new ArrayList<>();
  public List<SSUri>         entitiesToAttach       = new ArrayList<>();
  public List<SSLabel>       entityLabels           = new ArrayList<>();

  public String getEntry() {
    return SSStrU.removeTrailingSlash(entry);
  }

  public void setEntry(String entry) throws Exception {
    this.entry = SSUri.get(entry);
  }

  public String getContent() {
    return SSStrU.toStr(content);
  }

  public void setContent(String content) throws Exception {
    this.content = SSTextComment.get(content);
  }

  public List<String> getEntitiesToRemove() {
    return SSStrU.removeTrailingSlash(entitiesToRemove);
  }

  public void setEntitiesToRemove(List<String> entitiesToRemove) throws Exception {
    this.entitiesToRemove = SSUri.get(entitiesToRemove);
  }

  public List<String> getEntitiesToAttach() {
    return SSStrU.removeTrailingSlash(entitiesToAttach);
  }

  public void setEntitiesToAttach(List<String> entitiesToAttach) throws Exception {
    this.entitiesToAttach = SSUri.get(entitiesToAttach);
  }
  
  public SSDiscEntryUpdatePar(){}
  
  public SSDiscEntryUpdatePar(
    final SSUri               user,
    final SSUri               entry, 
    final SSTextComment       content,
    final List<SSUri>         entitiesToRemove,
    final List<SSUri>         entitiesToAttach,
    final List<SSLabel>       entityLabels,
    final Boolean             withUserRestriction,
    final Boolean             shouldCommit){
    
    super(SSServOpE.discEntryUpdate, null, user);
    
    this.entry          = entry;
    this.content        = content;
    
    SSUri.addDistinctWithoutNull   (this.entitiesToRemove, entitiesToRemove);
    SSUri.addDistinctWithoutNull   (this.entitiesToAttach, entitiesToAttach);
    SSLabel.addDistinctWithoutNull (this.entityLabels,     entityLabels);
      
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}