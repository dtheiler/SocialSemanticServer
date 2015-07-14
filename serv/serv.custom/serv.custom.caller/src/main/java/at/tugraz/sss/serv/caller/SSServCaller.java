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
package at.tugraz.sss.serv.caller;

import at.kc.tugraz.socialserver.service.broadcast.datatypes.SSBroadcast;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.enums.SSBroadcastEnum;
import at.kc.tugraz.ss.like.datatypes.SSLikes;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchLabel;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSIDU;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarNames;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.eval.datatypes.SSEvalLogE;

public class SSServCaller {

  /* eval */
  
  public static void evalLog(
    final SSUri          user,
    final SSToolContextE toolContext,
    final SSUri          forUser,
    final SSEvalLogE     type,
    final SSUri          entity,
    final String         content,
    final List<SSUri>    entities,
    final List<SSUri>    users) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,            user);
    opPars.put(SSVarNames.toolContext,     toolContext);
    opPars.put(SSVarNames.forUser,         forUser);
    opPars.put(SSVarNames.type,            type);
    opPars.put(SSVarNames.entity,          entity);
    opPars.put(SSVarNames.content,         content);
    opPars.put(SSVarNames.entities,        entities);
    opPars.put(SSVarNames.users,           users);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evalLog, opPars));
  }
  
  /* like */
  
  public static SSLikes likesUserGet(
    final SSUri user,
    final SSUri forUser,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,       user);
    opPars.put(SSVarNames.entity,     entity);
    opPars.put(SSVarNames.forUser,    forUser);
    
    return (SSLikes) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.likesUserGet, opPars));
  }
  
  public static SSUri likeUserSet(
    final SSUri   user,
    final SSUri   entity,
    final Integer value) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.entity,    entity);
    opPars.put(SSVarNames.value,     value);
    
    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.likeUserSet, opPars));
  }
    
  /* evernote */ 
  
  public static Resource evernoteResourceByHashGet(
    final SSUri           user,
    final NoteStoreClient noteStore,
    final String          noteGUID, 
    final String          resourceHash) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.noteStore,      noteStore);
    opPars.put(SSVarNames.noteGUID,       noteGUID);
    opPars.put(SSVarNames.resourceHash,   resourceHash);
    
    return (Resource) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteResourceByHashGet, opPars));
  }
    
  public static Boolean evernoteResourceAdd(
    final SSUri    user,
    final SSUri    note, 
    final SSUri    resource,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.note,           note);
    opPars.put(SSVarNames.resource,       resource);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteResourceAdd, opPars));
  }
  
  public static Boolean evernoteUSNSet(
    final SSUri    user,
    final String   authToken, 
    final Integer  usn,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.authToken,      authToken);
    opPars.put(SSVarNames.usn,            usn);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteUSNSet, opPars));
  }
    
  public static Boolean evernoteNoteAdd(
    final SSUri    user,
    final SSUri    notebook, 
    final SSUri    note,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.notebook,       notebook);
    opPars.put(SSVarNames.note,           note);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteAdd, opPars));
  }
    
  public static String evernoteUsersAuthTokenGet(
    final SSUri    user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    
    return (String) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteUsersAuthTokenGet, opPars));
  }
  
  public static Boolean evernoteUserAdd(
    final SSUri    user, 
    final String   authToken,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.authToken,      authToken);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteUserAdd, opPars));
  }
  
  public static Resource evernoteResourceGet(
    final NoteStoreClient noteStore, 
    final String          resourceGUID,
    final Boolean         includeContent) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,        noteStore);
    opPars.put(SSVarNames.resourceGUID,     resourceGUID);
    opPars.put(SSVarNames.includeContent,   includeContent);
    
    return (Resource) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteResourceGet, opPars));
  }
  
  public static List<SharedNotebook> evernoteNotebooksSharedGet(
    final NoteStoreClient noteStore) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();

    opPars.put(SSVarNames.noteStore,     noteStore);
      
    return (List<SharedNotebook>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNotebooksSharedGet, opPars));
  }
  
   public static Notebook evernoteNotebookGet(
    final NoteStoreClient noteStore,
    final String          notebookGUID) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,     noteStore);
    opPars.put(SSVarNames.notebookGUID,  notebookGUID);
    
    return (Notebook) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNotebookGet, opPars));
  }
  
  public static Note evernoteNoteGet(
    final NoteStoreClient noteStore,
    final String          noteGUID,
    final Boolean         includeContent) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,       noteStore);
    opPars.put(SSVarNames.noteGUID,        noteGUID);
    opPars.put(SSVarNames.includeContent,  includeContent);
    
    return (Note) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteGet, opPars));
  }
  
  public static SSEvernoteInfo evernoteNoteStoreGet(
    final SSUri  user, 
    final String authToken) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.authToken, authToken);
    
    return (SSEvernoteInfo) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteStoreGet, opPars));
  }
  
   public static List<String> evernoteNoteTagNamesGet(
    final NoteStoreClient noteStore, 
    final String          noteGUID) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,      noteStore);
    opPars.put(SSVarNames.noteGUID,       noteGUID);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteTagNamesGet, opPars));
  }
   
  public static Boolean broadcastAdd(
    final SSUri           user,
    final SSUri           entity,
    final SSBroadcastEnum type,
    final Object          content) throws Exception{
    
    final  Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.entity,       entity);
    opPars.put(SSVarNames.type,         type);
    opPars.put(SSVarNames.content,      content);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.broadcastAdd, opPars));
  }
  
  public static void broadcastUpdate() throws Exception{
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.broadcastUpdate, new HashMap<>()));
  } 
  
  public static List<SSBroadcast> broadcastsGet(
    final SSUri           user) throws Exception{
    
    final  Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    
    return (List<SSBroadcast>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.broadcastsGet, opPars));
  }

  private static SSUri vocURIPrefixGet() throws Exception{
    return (SSUri) SSUri.get(SSVocConf.sssUri);
  }
  
  public static SSUri vocURICreate() throws Exception{
    return SSUri.get(vocURIPrefixGet() + SSIDU.uniqueID());
  }
  
  public static SSUri vocURICreateFromId(final String id) throws Exception{
    return SSUri.get(vocURIPrefixGet() + id);
  }
  
  public static SSUri vocURICreate(final SSFileExtE fileExt) throws Exception{
    return SSUri.get(vocURIPrefixGet() + SSIDU.uniqueID() + SSStrU.dot + fileExt.toString());
  }
  
  
  /* search */
  
  public static void searchResultPagesCacheClean() throws Exception{
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.searchResultPagesCacheClean, new HashMap<>()));
  }
  
  public static SSSearchRet search(
    final SSUri                user, 
    final Boolean              includeTextualContent,
    final List<String>         wordsToSearchFor,
    final Boolean              includeTags,
    final List<String>         tagsToSearchFor,
    final Boolean              includeAuthors,
    final List<SSUri>          authorsToSearchFor,
    final Boolean              includeLabel,
    final List<String>         labelsToSearchFor,
    final Boolean              includeDescription,
    final List<String>         descriptionsToSearchFor,
    final List<SSEntityE>      typesToSearchOnlyFor,
    final Boolean              includeOnlySubEntities,
    final List<SSUri>          entitiesToSearchWithin,
    final Boolean              extendToParents, 
    final Boolean              includeRecommendedResults,
    final Boolean              provideEntries,
    final String               pagesID,
    final Integer              pageNumber,
    final Integer              minRating,
    final Integer              maxRating,
    final SSSearchOpE          localSearchOp,
    final SSSearchOpE          globalSearchOp) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,                      user);
    opPars.put(SSVarNames.includeTextualContent,     includeTextualContent);
    opPars.put(SSVarNames.wordsToSearchFor,          wordsToSearchFor);
    opPars.put(SSVarNames.includeTags,               includeTags);
    opPars.put(SSVarNames.tagsToSearchFor,           tagsToSearchFor);
    opPars.put(SSVarNames.includeAuthors,            includeAuthors);
    opPars.put(SSVarNames.authorsToSearchFor,        authorsToSearchFor);
    opPars.put(SSVarNames.includeLabel,              includeLabel);
    opPars.put(SSVarNames.labelsToSearchFor,         SSSearchLabel.get(labelsToSearchFor));
    opPars.put(SSVarNames.includeDescription,        includeDescription);
    opPars.put(SSVarNames.descriptionsToSearchFor,   SSSearchLabel.get(descriptionsToSearchFor));
    opPars.put(SSVarNames.typesToSearchOnlyFor,      typesToSearchOnlyFor);
    opPars.put(SSVarNames.includeOnlySubEntities,    includeOnlySubEntities);
    opPars.put(SSVarNames.entitiesToSearchWithin,    entitiesToSearchWithin);
    opPars.put(SSVarNames.extendToParents,           extendToParents);
    opPars.put(SSVarNames.includeRecommendedResults, includeRecommendedResults);
    opPars.put(SSVarNames.provideEntries,            provideEntries);
    opPars.put(SSVarNames.pagesID,                   pagesID);
    opPars.put(SSVarNames.pageNumber,                pageNumber);
    opPars.put(SSVarNames.minRating,                 minRating);
    opPars.put(SSVarNames.maxRating,                 maxRating);
    opPars.put(SSVarNames.localSearchOp,             localSearchOp);
    opPars.put(SSVarNames.globalSearchOp,            globalSearchOp);
    
    return (SSSearchRet) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.search, opPars));
  }
  
  /* solr */

  public static List<String> solrSearch(
    final String       keyword,
    final Integer      maxResults) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.keyword,    keyword);
    opPars.put(SSVarNames.maxResults, maxResults);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.solrSearch, opPars));
  }
  
  public static void solrAddDoc(
    final SSUri        user,
    final String       fileID,
    final SSMimeTypeE  mimeType,
    final Boolean      shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,    shouldCommit);
    opPars.put(SSVarNames.user,            user);
    opPars.put(SSVarNames.id,              fileID);
    opPars.put(SSVarNames.mimeType,        mimeType);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.solrAddDoc, opPars));
  }
  
  /* entity */
  
  public static List<SSEntity> entitiesForLabelsAndDescriptionsGet(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.requireds, requireds);
    opPars.put(SSVarNames.absents,   absents);
    opPars.put(SSVarNames.eithers,   eithers);
    
    return (List<SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entitiesForLabelsAndDescriptionsGet, opPars));
  }
  
  public static List<SSEntity> entitiesForLabelsGet(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.requireds, requireds);
    opPars.put(SSVarNames.absents,   absents);
    opPars.put(SSVarNames.eithers,   eithers);
    
    return (List<SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entitiesForLabelsGet, opPars));
  }
  
  public static List<SSEntity> entitiesForDescriptionsGet(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.requireds, requireds);
    opPars.put(SSVarNames.absents,   absents);
    opPars.put(SSVarNames.eithers,   eithers);
    
    return (List<SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entitiesForDescriptionsGet, opPars));
  }
  
  public static List<SSUri> entityUserParentEntitiesGet(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.entity,    entity);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityUserParentEntitiesGet, opPars));
  }
  
  public static List<SSUri> entityUserSubEntitiesGet(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.entity,    entity);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityUserSubEntitiesGet, opPars));
  }
  
  public static void entityRemove(
    final SSUri   entity, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.entity,       entity);
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityRemove, opPars));
  }
  
  /* modeling user event */

  public static List<SSUri> modelUEEntitiesForMiGet(
    final SSUri    user, 
    final String   mi) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user, user);
    opPars.put(SSVarNames.mi,   mi);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.modelUEEntitiesForMiGet, opPars));
  }
  
  public static void modelUEUpdate() throws Exception{
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.modelUEUpdate, new HashMap<>()));
  }
  
  public static List<String> modelUEMIsForEntityGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,        user);
    opPars.put(SSVarNames.entity,   entity);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.modelUEMIsForEntityGet, opPars));
  }
  
  /* data export */
  
  public static void dataExportUserRelations(
    final SSUri                     user) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,                  user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataExportUserRelations, opPars));
  }
  
  public static void dataExportUsersEntitiesTagsCategoriesTimestampsFile(
    final SSUri                     user,
    final List<SSUri>               users,
    final Boolean                   exportTags,
    final Boolean                   usePrivateTagsToo,
    final Boolean                   exportCategories,
    final String                    fileName) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName,              fileName);
    opPars.put(SSVarNames.users,                 users);
    opPars.put(SSVarNames.exportTags,            exportTags);
    opPars.put(SSVarNames.usePrivateTagsToo,     usePrivateTagsToo);
    opPars.put(SSVarNames.exportCategories,      exportCategories);
    opPars.put(SSVarNames.user,                  user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataExportUsersEntitiesTagsCategoriesTimestampsFile, opPars));
  }
  
  public static void dataExportUserEntityTagsCategoriesTimestampsLine(
    final SSUri        user,
    final SSUri        forUser,
    final SSUri        entity,
    final List<String> tags,
    final List<String> categories,
    final String       fileName) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,          user);
    opPars.put(SSVarNames.forUser,       forUser);
    opPars.put(SSVarNames.entity,        entity);
    opPars.put(SSVarNames.tags,          tags);
    opPars.put(SSVarNames.categories,    categories);
    opPars.put(SSVarNames.fileName,      fileName);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataExportUserEntityTagsCategoriesTimestampsLine, opPars));
  }
  
  /* data import */
  
  public static void dataImportAchso(
    final SSUri   user, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,     shouldCommit);
    opPars.put(SSVarNames.user,             user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportAchso, opPars));
  }
  
  public static void dataImportUserResourceTagFromWikipedia(
    final SSUri   user, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,     shouldCommit);
    opPars.put(SSVarNames.user,             user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportUserResourceTagFromWikipedia, opPars));
  }
  
  public static Map<String, String> dataImportSSSUsersFromCSVFile( 
    final String fileName) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName, fileName);
    
    return (Map<String, String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportSSSUsersFromCSVFile, opPars));
  }
  
  public static void dataImportEvernote(
    final SSUri   user, 
    final String  authToken, 
    final String  authEmail,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.authToken,    authToken);
    opPars.put(SSVarNames.authEmail,    authEmail);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportEvernote, opPars));
  }
  
  public static void dataImportMediaWikiUser(
    final SSUri   user,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,        user);
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportMediaWikiUser, opPars));
  }
  
  /* auth */
  
  public static SSUri authRegisterUser(
    final SSUri   user,
    final SSLabel label,
    final String  email, 
    final String  password,
    final Boolean isSystemUser,
    final Boolean updatePassword,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,              user);
    opPars.put(SSVarNames.label,             label);
    opPars.put(SSVarNames.email,             email);
    opPars.put(SSVarNames.password,          password);
    opPars.put(SSVarNames.isSystemUser,      isSystemUser);
    opPars.put(SSVarNames.updatePassword,    updatePassword);
    opPars.put(SSVarNames.shouldCommit,      shouldCommit);
    
    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.authRegisterUser, opPars));
  }
   
  public static void authUsersFromCSVFileAdd(
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,      shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.authUsersFromCSVFileAdd, opPars));
  }
    
  public static SSUri checkKey(final SSServPar par) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.key, par.key);
    
    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.authCheckKey, opPars));
  }
  
  public static SSAuthCheckCredRet authCheckCred(
    final SSUri  user,
    final String key) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user, user);
    opPars.put(SSVarNames.key, key);
    
    return (SSAuthCheckCredRet) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.authCheckCred, opPars));
  }
  
  /* i5Cloud */
  
  public static List<String> i5CloudAchsoSemanticAnnotationsSetGet(
    final List<String> ids) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.ids,   ids);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudAchsoSemanticAnnotationsSetGet, opPars));
  }
    
  public static String i5CloudAchsoVideoInformationGet() throws Exception{
    return (String) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudAchsoVideoInformationGet, new HashMap<>()));
  }
    
  public static Map<String, String> i5CloudAuth() throws Exception{
    return (Map<String, String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudAuth, new HashMap<>()));
  }
  
  public static Boolean i5CloudFileUpload(
    final String fileName,
    final String containerSpace,
    final String authToken) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName,   fileName);
    opPars.put(SSVarNames.space,      containerSpace);
    opPars.put(SSVarNames.authToken,  authToken);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudFileUpload, opPars));
  }
  
  public static Boolean i5CloudFileDownload(
    final String fileName,
    final String containerSpace,
    final String authToken) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName,   fileName);
    opPars.put(SSVarNames.space,      containerSpace);
    opPars.put(SSVarNames.authToken,  authToken);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudFileDownload, opPars));
  } 

  public static void recommLoadUserRealms(
    final SSUri user) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarNames.user,             user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.recommLoadUserRealms, opPars));
  }
}