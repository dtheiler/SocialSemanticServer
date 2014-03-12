/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.jobs.evernote.impl.helper;

import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.List;

public class SSEvernoteHelper {
  
  public  final SSEvernoteLabelHelper labelHelper;
  public  final SSEvernoteUEHelper    ueHelper;
  public  final SSEvernoteUriHelper   uriHelper;
  
  public SSEvernoteHelper(){
    this.labelHelper = new SSEvernoteLabelHelper ();
    this.ueHelper    = new SSEvernoteUEHelper    ();
    this.uriHelper   = new SSEvernoteUriHelper   ();
  }
  
  public Boolean isSharedNootebook(SSUri notebookUri, SSLabelStr userName, Notebook notebook) {
    return uriHelper.isSharedNotebookUri(userName, notebook, notebookUri);
  }

  public List<String> getSharedNotebookGuids(List<SharedNotebook> sharedNotebooks) {
    
    List<String> sharedNotebookGuids = new ArrayList<String>();
    
    if(sharedNotebooks == null){
      return sharedNotebookGuids;
    }
    
    for(SharedNotebook sharedNotebook : sharedNotebooks){
      sharedNotebookGuids.add(sharedNotebook.getNotebookGuid());
    }
    
    return sharedNotebookGuids;
  }

  public SSLabelStr getUserName(SSEvernoteInfo evernoteInfo) throws Exception{
    return SSLabelStr.get(evernoteInfo.userStore.getUser().getUsername());
  }
}