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
package at.kc.tugraz.ss.serv.serv.caller;

import at.kc.tugraz.socialserver.service.broadcast.datatypes.SSBroadcast;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.enums.SSBroadcastEnum;
import at.kc.tugraz.socialserver.utils.SSFileExtE;
import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.SSActivityContent;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityContentE;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.SSCategoryFrequ;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleRightE;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.SSLocation;
import at.kc.tugraz.ss.like.datatypes.SSLikes;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockHoldRet;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.disc.datatypes.SSDisc;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserEntryAddRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileCanWriteRet;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.sss.flag.datatypes.SSFlag;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSServCaller {

  /* like */
  
  public static SSLikes likesUserGet(
    final SSUri user,
    final SSUri forUser,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,       user);
    opPars.put(SSVarU.entity,     entity);
    opPars.put(SSVarU.forUser,    forUser);
    
    return (SSLikes) SSServA.callServViaServer(new SSServPar(SSMethU.likesUserGet, opPars));
  }
  
  public static SSUri likeUserSet(
    final SSUri   user,
    final SSUri   entity,
    final Integer value) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.entity,    entity);
    opPars.put(SSVarU.value,     value);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.likeUserSet, opPars));
  }
    
  /* friends */
  
  public static List<? extends SSEntity> friendsUserGet(
    final SSUri user) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                    user);
    
    return (List<? extends SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.friendsUserGet, opPars));
  }
  
   /* appStackLayout */
    
  public static SSUri appStackLayoutCreate(
    final SSUri          user,
    final String         uuid,
    final SSUri          app,
    final SSLabel        label,
    final SSTextComment  description) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                    user);
    opPars.put(SSVarU.uuid,                    uuid);
    opPars.put(SSVarU.app,                     app);
    opPars.put(SSVarU.label,                   label);
    opPars.put(SSVarU.description,             description);

    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.appStackLayoutCreate, opPars));
  }
  
  public static List<? extends SSEntity> appStackLayoutsGet(
    final SSUri user) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    
    return (List<? extends SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.appStackLayoutsGet, opPars));
  }
  
  /* app */
  
  public static SSUri appAdd(
    final SSUri         user,
    final SSLabel       label,
    final SSTextComment descriptionShort,
    final SSTextComment descriptionFunctional,
    final SSTextComment descriptionTechnical,
    final SSTextComment descriptionInstall,
    final SSUri         downloadIOS,
    final SSUri         downloadAndroid,
    final SSUri         fork,
    final List<SSUri>   downloads,
    final List<SSUri>   images,
    final List<SSUri>   videos) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                    user);
    opPars.put(SSVarU.label,                   label);
    opPars.put(SSVarU.descriptionShort,        descriptionShort);
    opPars.put(SSVarU.descriptionFunctional,   descriptionFunctional);
    opPars.put(SSVarU.descriptionTechnical,    descriptionTechnical);
    opPars.put(SSVarU.descriptionInstall,      descriptionInstall);
    opPars.put(SSVarU.downloadIOS,             downloadIOS);
    opPars.put(SSVarU.downloadAndroid,         downloadAndroid);
    opPars.put(SSVarU.fork,                    fork);
    opPars.put(SSVarU.downloads,               downloads);
    opPars.put(SSVarU.images,                  images);
    opPars.put(SSVarU.videos,                  videos);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.appAdd, opPars));
  }
  
  public static List<? extends SSEntity> appsGet(
    final SSUri user) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    
    return (List<? extends SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.appsGet, opPars));
  }
   
   /* comment */
  
  public static List<SSUri> commentEntitiesCommentedGet(
    final SSUri user,
    final SSUri forUser) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.forUser,   forUser);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.commentEntitiesCommentedGet, opPars));
  }
  
  public static List<SSTextComment> commentsGet(
    final SSUri user,
    final SSUri forUser,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.forUser,   forUser);
    opPars.put(SSVarU.entity,    entity);
    
    return (List<SSTextComment>) SSServA.callServViaServer(new SSServPar(SSMethU.commentsGet, opPars));
  }
  
  public static List<SSTextComment> commentsUserGet(
    final SSUri user,
    final SSUri forUser,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.forUser,   forUser);
    opPars.put(SSVarU.entity,    entity);
    
    return (List<SSTextComment>) SSServA.callServViaServer(new SSServPar(SSMethU.commentsUserGet, opPars));
  }
  
  /* flag */
  
  public static List<SSFlag> flagsGet(
    final SSUri          user,
    final List<SSUri>    entities,
    final List<String>   types,
    final Long           startTime,
    final Long           endTime) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.entities,          entities);
    opPars.put(SSVarU.types,             types);
    opPars.put(SSVarU.startTime,         startTime);
    opPars.put(SSVarU.endTime,           endTime);
    
    return (List<SSFlag>) SSServA.callServViaServer(new SSServPar(SSMethU.flagsGet, opPars));
  }
  
  public static Boolean flagsUserSet(
    final SSUri          user,
    final List<SSUri>    entities,
    final List<String>   types,
    final Long           endTime,
    final Integer        value) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.entities,          entities);
    opPars.put(SSVarU.types,             types);
    opPars.put(SSVarU.endTime,           endTime);
    opPars.put(SSVarU.value,             value);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.flagsUserSet, opPars)); 
  }
  
  public static List<SSFlag> flagsUserGet(
    final SSUri          user,
    final List<SSUri>    entities,
    final List<String>   types, 
    final Long           startTime,
    final Long           endTime) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.entities,          entities);
    opPars.put(SSVarU.types,             types);
    opPars.put(SSVarU.startTime,         startTime);
    opPars.put(SSVarU.endTime,           endTime);
    
    return (List<SSFlag>) SSServA.callServViaServer(new SSServPar(SSMethU.flagsUserGet, opPars)); 
  }
  
  /* learn ep */
  
  public static SSLearnEpLockHoldRet learnEpLockHold(
    final SSUri       user,
    final SSUri       learnEp,
    final Boolean     withUserRestriction) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                user);
    opPars.put(SSVarU.learnEp,             learnEp);
    opPars.put(SSVarU.withUserRestriction, withUserRestriction);
    
    return (SSLearnEpLockHoldRet) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpLockHold, opPars)); 
  }
  
  public static Boolean learnEpLockRemove(
    final SSUri       user,
    final SSUri       forUser,
    final SSUri       learnEp,
    final Boolean     withUserRestriction,
    final Boolean     shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                user);
    opPars.put(SSVarU.forUser,             forUser);
    opPars.put(SSVarU.learnEp,             learnEp);
    opPars.put(SSVarU.withUserRestriction, withUserRestriction);
    opPars.put(SSVarU.shouldCommit,        shouldCommit);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpLockRemove, opPars)); 
  }
  
  public static Boolean learnEpLockSet(
    final SSUri       user,
    final SSUri       forUser,
    final SSUri       learnEp,
    final Boolean     withUserRestriction,
    final Boolean     shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                user);
    opPars.put(SSVarU.forUser,             forUser);
    opPars.put(SSVarU.learnEp,             learnEp);
    opPars.put(SSVarU.withUserRestriction, withUserRestriction);
    opPars.put(SSVarU.shouldCommit,        shouldCommit);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpLockSet, opPars)); 
  }
  
  public static SSUri learnEpUserCopyForUser(
    final SSUri       user,
    final SSUri       forUser, 
    final SSUri       entity,
    final List<SSUri> entitiesToExclude,
    final Boolean     shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,               user);
    opPars.put(SSVarU.forUser,            forUser);
    opPars.put(SSVarU.entity,             entity);
    opPars.put(SSVarU.entitiesToExclude,  entitiesToExclude);
    opPars.put(SSVarU.shouldCommit,       shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpUserCopyForUser, opPars)); 
  }
  
  public static SSUri learnEpCreate(
    final SSUri         user, 
    final SSLabel       label,
    final SSTextComment description,
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,  shouldCommit);
    opPars.put(SSVarU.user,          user);
    opPars.put(SSVarU.label,         label);
    opPars.put(SSVarU.description,   description);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpCreate, opPars));
  }
  
  public static SSUri learnEpVersionAddCircle(
    final SSUri      user,
    final SSUri      learnEpVersion,
    final SSLabel    label,
    final Float      xLabel,
    final Float      yLabel,
    final Float      xR,
    final Float      yR,
    final Float      xC,
    final Float      yC,
    final Boolean    shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpVersion,    learnEpVersion);
    opPars.put(SSVarU.label,             label);
    opPars.put(SSVarU.xLabel,            xLabel);
    opPars.put(SSVarU.yLabel,            yLabel);
    opPars.put(SSVarU.xR,                xR);
    opPars.put(SSVarU.yR,                yR);
    opPars.put(SSVarU.xC,                xC);
    opPars.put(SSVarU.yC,                yC);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionAddCircle, opPars));
  }
  
  public static SSUri learnEpVersionAddEntity(
    final SSUri   user,
    final SSUri   learnEpVersion,
    final SSUri   entity,
    final Float   x,
    final Float   y,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpVersion,    learnEpVersion);
    opPars.put(SSVarU.entity,            entity);
    opPars.put(SSVarU.x,                 x);
    opPars.put(SSVarU.y,                 y);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionAddEntity, opPars));
  }
  
  public static void learnEpVersionCurrentSet(
    final SSUri   user, 
    final SSUri   learnEpVersion, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpVersion,    learnEpVersion);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionCurrentSet, opPars));
  }
  
  public static SSUri learnEpVersionSetTimelineState(
    final SSUri   user,
    final SSUri   learnEpVersion,
    final Long    startTime,
    final Long    endTime,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpVersion,    learnEpVersion);
    opPars.put(SSVarU.startTime,         startTime);
    opPars.put(SSVarU.endTime,           endTime);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionSetTimelineState, opPars));
  }
  
  public static SSLearnEpTimelineState learnEpVersionGetTimelineState(
    final SSUri   user,
    final SSUri   learnEpVersion) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpVersion,    learnEpVersion);
    
    return (SSLearnEpTimelineState) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionGetTimelineState, opPars));
  }
  
  public static SSUri learnEpVersionCreate(
    final SSUri    user, 
    final SSUri    learnEp, 
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,  shouldCommit);
    opPars.put(SSVarU.user,          user);
    opPars.put(SSVarU.learnEp,       learnEp);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionCreate, opPars));
  }
  
  public static SSLearnEpVersion learnEpVersionCurrentGet(SSUri user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    
    return (SSLearnEpVersion) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionCurrentGet, opPars));
  }
  
  public static Integer uECountGet(
    final SSUri    user,
    final SSUri    forUser,
    final SSUri    entity,
    final SSUEE    type,
    final Long     startTime,
    final Long     endTime) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.forUser,   forUser);
    opPars.put(SSVarU.entity,    entity);
    opPars.put(SSVarU.type,      type);
    opPars.put(SSVarU.startTime, startTime);
    opPars.put(SSVarU.endTime,   endTime);
    
    return (Integer) SSServA.callServViaServer(new SSServPar(SSMethU.uECountGet, opPars));
  }
  
  public static List<SSUE> uEsGet(
    final SSUri        user,
    final SSUri        forUser,
    final SSUri        entity,
    final List<SSUEE>  types,
    final Long         startTime,
    final Long         endTime) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.forUser,   forUser);
    opPars.put(SSVarU.entity,    entity);
    opPars.put(SSVarU.types,      types);
    opPars.put(SSVarU.startTime, startTime);
    opPars.put(SSVarU.endTime,   endTime);
    
    return (List<SSUE>) SSServA.callServViaServer(new SSServPar(SSMethU.uEsGet, opPars));
  }
  
  /* fileSys */
  
  public static void fileSysLocalFormatAudioAndVideoFileNamesInDir() throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    SSServA.callServViaServer(new SSServPar(SSMethU.fileSysLocalFormatAudioAndVideoFileNamesInDir, opPars));
  }
  
  public static void fileSysLocalAddTextToFilesNamesAtBeginInDir() throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    SSServA.callServViaServer(new SSServPar(SSMethU.fileSysLocalAddTextToFilesNamesAtBeginInDir, opPars));
  }
  
  /* evernote */ 
  
  public static Boolean evernoteResourceAdd(
    final SSUri    user,
    final SSUri    note, 
    final SSUri    resource,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,           user);
    opPars.put(SSVarU.note,           note);
    opPars.put(SSVarU.resource,       resource);
    opPars.put(SSVarU.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteResourceAdd, opPars));
  }
  
  public static Boolean evernoteUSNSet(
    final SSUri    user,
    final String   authToken, 
    final Integer  usn,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,           user);
    opPars.put(SSVarU.authToken,      authToken);
    opPars.put(SSVarU.usn,            usn);
    opPars.put(SSVarU.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteUSNSet, opPars));
  }
    
  public static Boolean evernoteNoteAdd(
    final SSUri    user,
    final SSUri    notebook, 
    final SSUri    note,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,           user);
    opPars.put(SSVarU.notebook,       notebook);
    opPars.put(SSVarU.note,           note);
    opPars.put(SSVarU.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNoteAdd, opPars));
  }
    
  public static String evernoteUsersAuthTokenGet(
    final SSUri    user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,           user);
    
    return (String) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteUsersAuthTokenGet, opPars));
  }
  
  public static Boolean evernoteUserAdd(
    final SSUri    user, 
    final String   authToken,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,           user);
    opPars.put(SSVarU.authToken,      authToken);
    opPars.put(SSVarU.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteUserAdd, opPars));
  }
  
  public static Resource evernoteResourceGet(
    final NoteStoreClient noteStore, 
    final String          resourceGUID,
    final Boolean         includeContent) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.noteStore,        noteStore);
    opPars.put(SSVarU.resourceGUID,     resourceGUID);
    opPars.put(SSVarU.includeContent,   includeContent);
    
    return (Resource) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteResourceGet, opPars));
  }
  
  public static List<SharedNotebook> evernoteNotebooksSharedGet(
    final NoteStoreClient noteStore) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();

    opPars.put(SSVarU.noteStore,     noteStore);
      
    return (List<SharedNotebook>) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNotebooksSharedGet, opPars));
  }
  
   public static Notebook evernoteNotebookGet(
    final NoteStoreClient noteStore,
    final String          notebookGUID) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.noteStore,     noteStore);
    opPars.put(SSVarU.notebookGUID,  notebookGUID);
    
    return (Notebook) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNotebookGet, opPars));
  }
  
  public static Note evernoteNoteGet(
    final NoteStoreClient noteStore,
    final String          noteGUID,
    final Boolean         includeContent) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.noteStore,       noteStore);
    opPars.put(SSVarU.noteGUID,        noteGUID);
    opPars.put(SSVarU.includeContent,  includeContent);
    
    return (Note) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNoteGet, opPars));
  }
  
  public static SSEvernoteInfo evernoteNoteStoreGet(
    final SSUri  user, 
    final String authToken) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.authToken, authToken);
    
    return (SSEvernoteInfo) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNoteStoreGet, opPars));
  }
  
   public static List<String> evernoteNoteTagNamesGet(
    final NoteStoreClient noteStore, 
    final String          noteGUID) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.noteStore,      noteStore);
    opPars.put(SSVarU.noteGUID,       noteGUID);
    
    return (List<String>) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNoteTagNamesGet, opPars));
  }
   
  //  public static List<Note> evernoteNotesGet(
//    final NoteStoreClient noteStore, 
//    final String          notebookGuid) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.noteStore,     noteStore);
//    opPars.put(SSVarU.notebookGuid,  notebookGuid);
//    
//    return (List<Note>) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNotesGet, opPars));
//  }
  
//  public static List<Notebook> evernoteNotebooksGet(NoteStoreClient noteStore) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.noteStore, noteStore);
//    
//    return (List<Notebook>) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNotebooksGet, opPars));
//  }
  
  //  public static List<LinkedNotebook> evernoteNotebooksLinkedGet(
//    final NoteStoreClient noteStore) throws Exception{
//    
//    final Map<String, Object>  opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.noteStore,     noteStore);
//    
//    return (List<LinkedNotebook>) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNotebooksLinkedGet, opPars));
//  }
  
//  public static List<Note> evernoteNotesLinkedGet(
//    final NoteStoreClient noteStore, 
//    final LinkedNotebook  linkedNotebook) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.noteStore,      noteStore);
//    opPars.put(SSVarU.linkedNotebook, linkedNotebook);
//    
//    return (List<Note>) SSServA.callServViaServer(new SSServPar(SSMethU.evernoteNotesLinkedGet, opPars));
//  }
  
 
  
  public static void uEAddAtCreationTime(
    final SSUri    user,
    final SSUri    entity,
    final SSUEE    type,
    final String   content,
    final Long     creationTime,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.type,         type);
    opPars.put(SSVarU.content,      content);
    opPars.put(SSVarU.creationTime, creationTime);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.uEAddAtCreationTime, opPars));
  }
  
  public static SSLearnEpVersion learnEpVersionGet(
    final SSUri user, 
    final SSUri learnEpVersion) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpVersion,    learnEpVersion);
    
    return (SSLearnEpVersion) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionGet, opPars));
  }  
  
  public static void learnEpVersionUpdateCircle(
    final SSUri      user,
    final SSUri      learnEpCircle,
    final SSLabel    label,
    final Float      xLabel,
    final Float      yLabel,
    final Float      xR,
    final Float      yR,
    final Float      xC,
    final Float      yC,
    final Boolean    shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpCircle,     learnEpCircle);
    opPars.put(SSVarU.label,             label);
    opPars.put(SSVarU.xLabel,            xLabel);
    opPars.put(SSVarU.yLabel,            yLabel);
    opPars.put(SSVarU.xR,                xR);
    opPars.put(SSVarU.yR,                yR);
    opPars.put(SSVarU.xC,                xC);
    opPars.put(SSVarU.yC,                yC);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionUpdateCircle, opPars));
  }
  
  public static void learnEpVersionUpdateEntity(
    final SSUri   user,
    final SSUri   learnEpEntity,
    final SSUri   entity,
    final Float   x,
    final Float   y,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpEntity,     learnEpEntity);
    opPars.put(SSVarU.entity,            entity);
    opPars.put(SSVarU.x,                 x);
    opPars.put(SSVarU.y,                 y);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionUpdateEntity, opPars));
  }
  
  public static void learnEpVersionRemoveCircle(
    final SSUri   user,
    final SSUri   learnEpCircle,
    final Boolean shouldCommit) throws Exception{
    
    final  Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpCircle,  learnEpCircle);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionRemoveCircle, opPars));
  }
  
  public static void learnEpVersionRemoveEntity(
    final SSUri   user,
    final SSUri   learnEpEntity,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.learnEpEntity,     learnEpEntity);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionRemoveEntity, opPars));
  }
  
  public static List<SSLearnEp> learnEpsGet(
    final SSUri user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    
    return (List<SSLearnEp>) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpsGet, opPars));
  }
  
  public static List<SSLearnEpVersion> learnEpVersionsGet(
    final SSUri user,
    final SSUri learnEp) throws Exception{
    
    final Map<String, Object>     opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,    user);
    opPars.put(SSVarU.learnEp, learnEp);
    
    return (List<SSLearnEpVersion>) SSServA.callServViaServer(new SSServPar(SSMethU.learnEpVersionsGet, opPars));
  }
  
  public static void broadcastUpdate() throws Exception{
    SSServA.callServViaServer(new SSServPar(SSMethU.broadcastUpdate, new HashMap<>()));
  } 
  
  public static Boolean broadcastAdd(
    final SSUri           user,
    final SSUri           entity,
    final SSBroadcastEnum type,
    final Object          content) throws Exception{
    
    final  Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.type,         type);
    opPars.put(SSVarU.content,      content);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.broadcastAdd, opPars));
  }
  
  public static List<SSBroadcast> broadcastsGet(
    final SSUri           user) throws Exception{
    
    final  Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    
    return (List<SSBroadcast>) SSServA.callServViaServer(new SSServPar(SSMethU.broadcastsGet, opPars));
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
  
  /* colls */
    
  public static SSColl collUserWithEntries(
    final SSUri user, 
    final SSUri coll) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    opPars.put(SSVarU.coll, coll);
    
    return (SSColl) SSServA.callServViaServer(new SSServPar(SSMethU.collUserWithEntries, opPars));
  }
  
  public static SSColl collUserRootGet(final SSUri user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    
    return (SSColl) SSServA.callServViaServer(new SSServPar(SSMethU.collUserRootGet, opPars));
  }
  
  public static List<SSColl> collsUserWithEntries(final SSUri user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    
    return (List<SSColl>) SSServA.callServViaServer(new SSServPar(SSMethU.collsUserWithEntries, opPars));
  }
  
  public static Boolean collUserEntryDelete(
    final SSUri        user, 
    final SSUri        entry, 
    final SSUri        parentColl, 
    final Boolean      shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.coll,         parentColl);
    opPars.put(SSVarU.entry,        entry);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.collUserEntryDelete, opPars));
  }
  
  public static SSUri collUserEntryAdd(
    final SSUri        user,
    final SSUri        coll,
    final SSUri        entry,
    final SSLabel      label,
    final Boolean      addNewColl,
    final Boolean      shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    opPars.put(SSVarU.coll,              coll);
    opPars.put(SSVarU.entry,             entry);
    opPars.put(SSVarU.label,             label);
    opPars.put(SSVarU.addNewColl,        addNewColl);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.collUserEntryAdd, opPars));
  }
  
  public static Boolean collUserEntriesAdd(
    final SSUri             user,
    final SSUri             coll,
    final List<SSUri>       entries,
    final List<SSLabel>     labels,
    final Boolean           saveUE,
    final Boolean           shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.saveUE,       saveUE);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.coll,         coll);
    opPars.put(SSVarU.entries,      entries);
    opPars.put(SSVarU.labels,       labels);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.collUserEntriesAdd, opPars));
  }
  
  public static void collUserRootAdd(
    final SSUri   user, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.collUserRootAdd, opPars));
  }
  
  public static List<SSColl> collUserHierarchyGet(
    final SSUri user, 
    final SSUri coll) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.coll,      coll);
    
    return (List<SSColl>) SSServA.callServViaServer(new SSServPar(SSMethU.collUserHierarchyGet, opPars));
  }
  
  public static List<SSTagFrequ> collUserCumulatedTagsGet(
    final SSUri user, 
    final SSUri coll) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.coll,      coll);
    
    return (List<SSTagFrequ>) SSServA.callServViaServer(new SSServPar(SSMethU.collUserCumulatedTagsGet, opPars));
  }
  
  public static List<SSColl> collsUserEntityIsInGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,    entity);
    
    return (List<SSColl>) SSServA.callServViaServer(new SSServPar(SSMethU.collsUserEntityIsInGet, opPars));
  }
  
  public static Boolean collUserEntriesDelete(
    final SSUri       user,
    final SSUri       coll,
    final List<SSUri> entries,
    final Boolean     shouldCommit) throws Exception{
      
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.coll,         coll);
    opPars.put(SSVarU.entries,  entries);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.collUserEntriesDelete, opPars));
  }
  
  public static Boolean collsUserCouldSubscribeGet(
    final SSUri       user) throws Exception{
      
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.collsUserCouldSubscribeGet, opPars));
  }

  /* search */
  
  public static void searchResultPagesCacheClean() throws Exception{
    SSServA.callServViaServer(new SSServPar(SSMethU.searchResultPagesCacheClean, new HashMap<>()));
  }
  
  public static SSSearchRet search(
    final SSUri                user, 
    final Boolean              includeTextualContent,
    final List<String>         wordsToSearchFor,
    final Boolean              includeTags,
    final List<String>         tagsToSearchFor,
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
    
    opPars.put(SSVarU.user,                      user);
    opPars.put(SSVarU.includeTextualContent,     includeTextualContent);
    opPars.put(SSVarU.wordsToSearchFor,          wordsToSearchFor);
    opPars.put(SSVarU.includeTags,               includeTags);
    opPars.put(SSVarU.tagsToSearchFor,           tagsToSearchFor);
    opPars.put(SSVarU.includeLabel,              includeLabel);
    opPars.put(SSVarU.labelsToSearchFor,         labelsToSearchFor);
    opPars.put(SSVarU.includeDescription,        includeDescription);
    opPars.put(SSVarU.descriptionsToSearchFor,   descriptionsToSearchFor);
    opPars.put(SSVarU.typesToSearchOnlyFor,      typesToSearchOnlyFor);
    opPars.put(SSVarU.includeOnlySubEntities,    includeOnlySubEntities);
    opPars.put(SSVarU.entitiesToSearchWithin,    entitiesToSearchWithin);
    opPars.put(SSVarU.extendToParents,           extendToParents);
    opPars.put(SSVarU.includeRecommendedResults, includeRecommendedResults);
    opPars.put(SSVarU.provideEntries,            provideEntries);
    opPars.put(SSVarU.pagesID,                   pagesID);
    opPars.put(SSVarU.pageNumber,                pageNumber);
    opPars.put(SSVarU.minRating,                 minRating);
    opPars.put(SSVarU.maxRating,                 maxRating);
    opPars.put(SSVarU.localSearchOp,             localSearchOp);
    opPars.put(SSVarU.globalSearchOp,            globalSearchOp);
    
    return (SSSearchRet) SSServA.callServViaServer(new SSServPar(SSMethU.search, opPars));
  }
  
  /* solr */

  public static List<String> solrSearch(
    final String       keyword,
    final Integer      maxResults) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.keyword,    keyword);
    opPars.put(SSVarU.maxResults, maxResults);
    
    return (List<String>) SSServA.callServViaServer(new SSServPar(SSMethU.solrSearch, opPars));
  }
  
  public static void solrAddDoc(
    final SSUri   user,
    final String  fileID,
    final String  mimeType,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,    shouldCommit);
    opPars.put(SSVarU.user,            user);
    opPars.put(SSVarU.id,              fileID);
    opPars.put(SSVarU.mimeType,        mimeType);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.solrAddDoc, opPars));
  }
  
  /* disc */

  public static List<SSUri> discEntryURIsGet(
    final SSUri user, 
    final SSUri disc) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,             user);
    opPars.put(SSVarU.disc,             disc);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.discEntryURIsGet, opPars)); 
  }
  
  public static SSDiscUserEntryAddRet discUserEntryAdd(
    final SSUri               user,
    final SSUri               disc,
    final SSUri               entity,
    final SSTextComment       entry,
    final Boolean             addNewDisc,
    final SSEntityE           type,
    final SSLabel             label,
    final SSTextComment       description,
    final List<SSUri>         users,
    final List<SSUri>         circles,
    final List<SSUri>         entities,
    final Boolean             shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.disc,         disc);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.entry,        entry);
    opPars.put(SSVarU.addNewDisc,   addNewDisc);
    opPars.put(SSVarU.type,         type);
    opPars.put(SSVarU.label,        label);
    opPars.put(SSVarU.description,  description);
    opPars.put(SSVarU.users,        users);
    opPars.put(SSVarU.circles,      circles);
    opPars.put(SSVarU.entities,     entities);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    return (SSDiscUserEntryAddRet) SSServA.callServViaServer(new SSServPar(SSMethU.discUserEntryAdd, opPars));
  }
  
  public static List<SSDisc> discsUserAllGet(final SSUri user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    
    return (List<SSDisc>) SSServA.callServViaServer(new SSServPar(SSMethU.discsUserAllGet, opPars));
  }
  
  public static List<SSUri> discUserDiscURIsForTargetGet(
    final SSUri   user,
    final SSUri   entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,    entity);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.discUserDiscURIsForTargetGet, opPars));
  }
  
  public static SSDisc discUserWithEntriesGet(
    final SSUri   user, 
    final SSUri   disc, 
    final Integer maxEntries,
    final Boolean includeComments) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,            user);
    opPars.put(SSVarU.disc,            disc);
    opPars.put(SSVarU.maxEntries,      maxEntries);
    opPars.put(SSVarU.includeComments, includeComments);
    
    return (SSDisc) SSServA.callServViaServer(new SSServPar(SSMethU.discUserWithEntriesGet, opPars));
  }
  
  public static void discUserRemove(
    final SSUri   user,
    final SSUri   entity) throws Exception{
  
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,    entity);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.discUserRemove, opPars));
  }
  
  /* rating */

  public static SSRatingOverall ratingOverallGet(
    final SSUri user, 
    final SSUri entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.entity,    entity);
    
    return (SSRatingOverall) SSServA.callServViaServer(new SSServPar(SSMethU.ratingOverallGet, opPars));
  }
  
  public static void ratingsUserRemove(
    final SSUri   user,
    final SSUri   entity,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,    entity);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.ratingsUserRemove, opPars));
  }
  
  public static Integer ratingUserGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.entity,      entity);
    
    return (Integer) SSServA.callServViaServer(new SSServPar(SSMethU.ratingUserGet, opPars));
  }
  
  /* entity */
  
  public static SSUri entityAdd(
    final SSUri         user, 
    final SSUri         entity, 
    final SSEntityE     type, 
    final SSLabel       label,
    final SSTextComment description, 
    final Long          creationTime,
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,            user);
    opPars.put(SSVarU.entity,          entity);
    opPars.put(SSVarU.type,            type);
    opPars.put(SSVarU.label,           label);
    opPars.put(SSVarU.description,     description);
    opPars.put(SSVarU.creationTime,    creationTime);
    opPars.put(SSVarU.shouldCommit,    shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.entityAdd, opPars));
  }
  
//  public static List<SSEntity> entitiesUserGet(
//    final SSUri       user, 
//    final SSUri       forUser) throws Exception{
//    
//    final Map<String, Object>  opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.user,            user);
//    opPars.put(SSVarU.forUser,         forUser);
//    
//    return (List<SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.entitiesUserGet, opPars));
//  }
  
  public static SSEntity entityUserGet(
    final SSUri       user,
    final SSUri       entity,
    final SSUri       forUser,
    final Boolean     logErr) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.forUser,      forUser);
    opPars.put(SSVarU.logErr,       logErr);
    
    return (SSEntity) SSServA.callServViaServer(new SSServPar(SSMethU.entityUserGet, opPars));
  }
    
  public static void entityUpdate(
    final SSUri               user,
    final SSUri               entity,
    final SSLabel             label,
    final SSTextComment       description,
    final List<SSTextComment> comments,
    final List<SSUri>         downloads,
    final List<SSUri>         screenShots,
    final List<SSUri>         images,
    final List<SSUri>         videos,
    final Boolean             shouldCommit) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.label,        label);
    opPars.put(SSVarU.description,  description);
    opPars.put(SSVarU.comments,     comments);
    opPars.put(SSVarU.downloads,    downloads);
    opPars.put(SSVarU.screenShots,  screenShots);
    opPars.put(SSVarU.images,       images);
    opPars.put(SSVarU.videos,       videos);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.entityUpdate, opPars));
  }
  
  public static void entityLocationsAdd(
    final SSUri   user, 
    final SSUri   entity,
    final Double  latitude, 
    final Double  longitude, 
    final Float   accuracy, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    final List<SSLocation> locations = new ArrayList<>();
    
    locations.add(
      SSLocation.get(
        vocURICreate(), 
        latitude,
        longitude, 
        accuracy));
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.locations,    locations);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.entityLocationsAdd, opPars));
  }
  
  public static List<SSLocation> entityLocationsGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    
    return (List<SSLocation>) SSServA.callServViaServer(new SSServPar(SSMethU.entityLocationsGet, opPars));
  }
  
  public static Boolean entityReadGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,   user);
    opPars.put(SSVarU.entity, entity);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.entityReadGet, opPars));
  }
  
  public static List<SSEntity> entityEntitiesAttachedGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,   user);
    opPars.put(SSVarU.entity, entity);
    
    return (List<SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.entityEntitiesAttachedGet, opPars));
  }
    
  public static List<SSUri> entityUserEntitiesAttach(
    final SSUri       user, 
    final SSUri       entity, 
    final List<SSUri> entities,
    final Boolean     shouldCommit) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarU.user,             user);
    opPars.put(SSVarU.entity,           entity);
    opPars.put(SSVarU.entities,         entities);
    opPars.put(SSVarU.shouldCommit,     shouldCommit);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.entityUserEntitiesAttach, opPars));
  }
  
  public static List<SSUri> entityFilesGet(
    final SSUri user, 
    final SSUri entity) throws Exception{
   
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.entityFilesGet, opPars));
  }
  
  public static List<SSUri> entityDownloadURIsGet(
    final SSUri user, 
    final SSUri entity) throws Exception{
   
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.entityDownloadURIsGet, opPars));
  }
  
  public static List<? extends SSEntity> entityScreenShotsGet(
    final SSUri user, 
    final SSUri entity) throws Exception{
   
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    
    return (List<? extends SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.entityScreenShotsGet, opPars));
  }
  
  public static void entityFileAdd(
    final SSUri   user,
    final SSUri   entity,
    final SSUri   file,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.file,         file);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.entityFileAdd, opPars));
  }
  
  public static List<SSUri> entityThumbsGet(
    final SSUri user, 
    final SSUri entity) throws Exception{
   
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.entityThumbsGet, opPars));
  }
  
  public static void entityThumbAdd(
    final SSUri   user,
    final SSUri   entity,
    final SSUri   thumb,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.thumb,        thumb);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.entityThumbAdd, opPars));
  }
  
  public static List<SSEntity> entitiesForLabelsAndDescriptionsGet(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.requireds, requireds);
    opPars.put(SSVarU.absents,   absents);
    opPars.put(SSVarU.eithers,   eithers);
    
    return (List<SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.entitiesForLabelsAndDescriptionsGet, opPars));
  }
  
  public static List<SSEntity> entitiesForLabelsGet(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.requireds, requireds);
    opPars.put(SSVarU.absents,   absents);
    opPars.put(SSVarU.eithers,   eithers);
    
    return (List<SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.entitiesForLabelsGet, opPars));
  }
  
  public static List<SSEntity> entitiesForDescriptionsGet(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.requireds, requireds);
    opPars.put(SSVarU.absents,   absents);
    opPars.put(SSVarU.eithers,   eithers);
    
    return (List<SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.entitiesForDescriptionsGet, opPars));
  }
  
  public static Boolean entityExists(
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.entity,    entity);
    opPars.put(SSVarU.type,      null);
    opPars.put(SSVarU.label,     null);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.entityExists, opPars));
  }
  
  public static Boolean entityExists(
    final SSEntityE  type,
    final SSLabel    label) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.entity,    null);
    opPars.put(SSVarU.type,      type);
    opPars.put(SSVarU.label,     label);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.entityExists, opPars));
  }
  
  public static SSEntity entityGet(
    final SSEntityE  type,
    final SSLabel    label) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.entity, null);
    opPars.put(SSVarU.type,      type);
    opPars.put(SSVarU.label,     label);
    
    return (SSEntity) SSServA.callServViaServer(new SSServPar(SSMethU.entityGet, opPars));
  }
  
  public static SSEntity entityGet(
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.entity,    entity);
    opPars.put(SSVarU.type,      null);
    opPars.put(SSVarU.label,     null);
    
    return (SSEntity) SSServA.callServViaServer(new SSServPar(SSMethU.entityGet, opPars));
  }
    
  public static List<SSUri> entityUserParentEntitiesGet(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.entity,    entity);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.entityUserParentEntitiesGet, opPars));
  }
  
  public static List<SSUri> entityUserSubEntitiesGet(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,      user);
    opPars.put(SSVarU.entity,    entity);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.entityUserSubEntitiesGet, opPars));
  }
  
  public static SSUri entityUserDirectlyAdjoinedEntitiesRemove(
    final SSUri   user,
    final SSUri   entity           ,
    final Boolean removeUserTags      ,
    final Boolean removeUserRatings   ,
    final Boolean removeFromUserColls ,
    final Boolean removeUserLocations, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                  user);
    opPars.put(SSVarU.entity,                entity);
    opPars.put(SSVarU.removeUserTags,        removeUserTags);
    opPars.put(SSVarU.removeUserRatings,     removeUserRatings);
    opPars.put(SSVarU.removeFromUserColls,   removeFromUserColls);
    opPars.put(SSVarU.removeUserLocations,   removeUserLocations);
    opPars.put(SSVarU.shouldCommit,          shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.entityUserDirectlyAdjoinedEntitiesRemove, opPars));
  }
  
  public static SSEntity entityDescGet(
    final SSUri   user, 
    final SSUri   entity,
    final Boolean getTags,
    final Boolean getOverallRating,
    final Boolean getDiscs,
    final Boolean getUEs,
    final Boolean getThumb,
    final Boolean getFlags,
    final Boolean getCircles) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,             user);
    opPars.put(SSVarU.entity,           entity);
    opPars.put(SSVarU.getTags,          getTags);
    opPars.put(SSVarU.getOverallRating, getOverallRating);
    opPars.put(SSVarU.getDiscs,         getDiscs);
    opPars.put(SSVarU.getUEs,           getUEs);
    opPars.put(SSVarU.getThumb,         getThumb);
    opPars.put(SSVarU.getFlags,         getFlags);
    opPars.put(SSVarU.getCircles,       getCircles);
    
    return (SSEntity) SSServA.callServViaServer(new SSServPar(SSMethU.entityDescGet, opPars));
  }
  
  public static void entityRemove(
    final SSUri   entity, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.entityRemove, opPars));
  }
  
  public static Boolean entityUserCopy(
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   users,
    final List<SSUri>   entitiesToExclude,
    final SSTextComment comment,
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,               user);
    opPars.put(SSVarU.entity,             entity);
    opPars.put(SSVarU.users,              users);
    opPars.put(SSVarU.entitiesToExclude,  entitiesToExclude);
    opPars.put(SSVarU.comment,            comment);
    opPars.put(SSVarU.shouldCommit,       shouldCommit);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.entityUserCopy, opPars));
  }
  
  /* circle */
  
  public static SSEntity circleUserCan(
    final SSUri                    user,
    final SSUri                    entity, 
    final SSCircleRightE           accessRight) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.entity,      entity);
    opPars.put(SSVarU.accessRight, accessRight);
    
    return (SSEntity) SSServA.callServViaServer(new SSServPar(SSMethU.circleUserCan, opPars));
  }
  
  public static SSEntity circleUserCan(
    final SSUri                    user,
    final SSUri                    entity, 
    final SSCircleRightE           accessRight,
    final Boolean                  logErr) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.entity,      entity);
    opPars.put(SSVarU.accessRight, accessRight);
    opPars.put(SSVarU.logErr,      logErr);
    
    return (SSEntity) SSServA.callServViaServer(new SSServPar(SSMethU.circleUserCan, opPars));
  }
  
  public static void entityEntityToPrivCircleAdd(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type,
    final SSLabel       label,
    final SSTextComment description,
    final Long          creationTime,
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarU.user,             user);
    opPars.put(SSVarU.entity,           entity);
    opPars.put(SSVarU.type,             type);
    opPars.put(SSVarU.label,            label);
    opPars.put(SSVarU.description,      description);
    opPars.put(SSVarU.creationTime,     creationTime);
    opPars.put(SSVarU.shouldCommit,     shouldCommit);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.entityEntityToPrivCircleAdd, opPars));
  }
  
  public static void entityEntityToPubCircleAdd(
    final SSUri         user, 
    final SSUri         entity, 
    final SSEntityE     type,
    final SSLabel       label,
    final SSTextComment description, 
    final Long          creationTime, 
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarU.user,             user);
    opPars.put(SSVarU.entity,           entity);
    opPars.put(SSVarU.type,             type);
    opPars.put(SSVarU.label,            label);
    opPars.put(SSVarU.description,      description);
    opPars.put(SSVarU.creationTime,     creationTime);
    opPars.put(SSVarU.shouldCommit,     shouldCommit);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.entityEntityToPubCircleAdd, opPars));
  }
  
  public static SSUri circleEntityShare(
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   users,
    final List<SSUri>   circles,
    final SSTextComment comment,
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,          user);
    opPars.put(SSVarU.entity,        entity);
    opPars.put(SSVarU.users,         users);
    opPars.put(SSVarU.circles,       circles);
    opPars.put(SSVarU.comment,       comment);
    opPars.put(SSVarU.shouldCommit,  shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.circleEntityShare, opPars));
  }
  
  public static SSEntityCircle circleGet(
    final SSUri   user,
    final SSUri   forUser,
    final SSUri   circle,
    final Boolean withSystemCircles,
    final Boolean withUserRestriction) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                user);
    opPars.put(SSVarU.forUser,             forUser);
    opPars.put(SSVarU.circle,              circle);
    opPars.put(SSVarU.withSystemCircles,   withSystemCircles);
    opPars.put(SSVarU.withUserRestriction, withUserRestriction);
    
    return (SSEntityCircle) SSServA.callServViaServer(new SSServPar(SSMethU.circleGet, opPars));
  }
   
  public static List<SSEntityCircle> circlesGet(
    final SSUri   user,
    final SSUri   forUser,
    final SSUri   entity,
    final Boolean withSystemCircles,
    final Boolean withUserRestriction) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                user);
    opPars.put(SSVarU.entity,              entity);
    opPars.put(SSVarU.withSystemCircles,   withSystemCircles);
    opPars.put(SSVarU.withUserRestriction, withUserRestriction);
    
    return (List<SSEntityCircle>) SSServA.callServViaServer(new SSServPar(SSMethU.circlesGet, opPars));
  }
  
  public static SSUri circleCreate(
    final SSUri                     user,
    final List<SSUri>               entities,
    final List<SSUri>               users, 
    final SSLabel                   label, 
    final SSTextComment             description,
    final Boolean                   isSystemCircle,
    final Boolean                   shouldCommit,
    final Boolean                   withUserRestriction,
    final Boolean                   invokeEntityHandlers) throws Exception{
    
    final Map<String, Object>       opPars     = new HashMap<>();
    
    opPars.put(SSVarU.user,                 user);
    opPars.put(SSVarU.entities,             entities);
    opPars.put(SSVarU.users,                users);
    opPars.put(SSVarU.label,                label);
    opPars.put(SSVarU.description,          description);
    opPars.put(SSVarU.isSystemCircle,       isSystemCircle);
    opPars.put(SSVarU.shouldCommit,         shouldCommit);
    opPars.put(SSVarU.withUserRestriction,  withUserRestriction);
    opPars.put(SSVarU.invokeEntityHandlers, invokeEntityHandlers);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.circleCreate, opPars));
  }
  
  public static void circleEntitiesAdd(
    final SSUri       user,
    final SSUri       circle,
    final List<SSUri> entities,
    final Boolean     invokeEntityHandlers,
    final Boolean     withUserRestriction, 
    final Boolean     shouldCommit) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                 user);
    opPars.put(SSVarU.circle,               circle);
    opPars.put(SSVarU.entities,             entities);
    opPars.put(SSVarU.invokeEntityHandlers, invokeEntityHandlers);
    opPars.put(SSVarU.withUserRestriction,  withUserRestriction);
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.circleEntitiesAdd, opPars));
  }
  
  public static SSUri circleUsersAdd(
    final SSUri       user,
    final SSUri       circle,
    final SSUri       userUriToAdd, 
    final Boolean     shouldCommit,
    final Boolean     withUserRestriction) throws Exception{
    
    final Map<String, Object> opPars   = new HashMap<>();
    final List<SSUri>         users = new ArrayList<>();
    
    if(userUriToAdd != null){
      users.add(userUriToAdd);
    }
    
    opPars.put(SSVarU.user,                  user);
    opPars.put(SSVarU.users,                 users);
    opPars.put(SSVarU.circle,                circle);
    opPars.put(SSVarU.shouldCommit,          shouldCommit);    
    opPars.put(SSVarU.withUserRestriction,   withUserRestriction);    
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.circleUsersAdd, opPars));
  }
  
  public static SSUri circleUsersAdd(
    final SSUri       user,
    final SSUri       circle,
    final List<SSUri> userUrisToAdd,
    final Boolean     shouldCommit,
    final Boolean     withUserRestriction) throws Exception{
    
    final Map<String, Object> opPars   = new HashMap<>();
    
    opPars.put(SSVarU.user,                 user);
    opPars.put(SSVarU.users,                userUrisToAdd);
    opPars.put(SSVarU.circle,               circle);
    opPars.put(SSVarU.shouldCommit,         shouldCommit);    
    opPars.put(SSVarU.withUserRestriction,  withUserRestriction);    
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.circleUsersAdd, opPars));
  }
  
  public static SSUri circlePrivURIGet(
    final SSUri user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.circlePrivURIGet, opPars));
  }
  
  public static SSUri circlePubURIGet(
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.circlePubURIGet, opPars));
  }
  
  public static List<SSCircleE> circleTypesGet(
    final SSUri   user, 
    final SSUri   forUser,
    final SSUri   entity,
    final Boolean withUserRestriction) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                user);
    opPars.put(SSVarU.forUser,             forUser);
    opPars.put(SSVarU.entity,              entity);
    opPars.put(SSVarU.withUserRestriction, withUserRestriction);
    
    return (List<SSCircleE>) SSServA.callServViaServer(new SSServPar(SSMethU.circleTypesGet, opPars));
  }
  
  public static SSCircleE circleMostOpenCircleTypeGet(
    final SSUri   user, 
    final SSUri   forUser, 
    final SSUri   entity,
    final Boolean withUserRestriction) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                user);
    opPars.put(SSVarU.forUser,             forUser);
    opPars.put(SSVarU.entity,              entity);
    opPars.put(SSVarU.withUserRestriction, withUserRestriction);
    
    return (SSCircleE) SSServA.callServViaServer(new SSServPar(SSMethU.circleMostOpenCircleTypeGet, opPars));
  }

  public static SSUri circleEntityPublicSet(
    final SSUri   user,
    final SSUri   entity,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.circleEntityPublicSet, opPars));
  }
  
  public static List<SSEntity> circleEntitiesGet(
    final SSUri   user, 
    final SSUri   forUser,
    final Boolean withUserRestriction) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                user);
    opPars.put(SSVarU.forUser,             forUser);
    opPars.put(SSVarU.withUserRestriction, withUserRestriction);
    
    return (List<SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.circleEntitiesGet, opPars));
  }
  
  /* user event */
  
  public static void ueAdd(
    final SSUri     user, 
    final SSUri     entity, 
    final SSUEE  type, 
    final String    content, 
    final Boolean   shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,     entity);
    opPars.put(SSVarU.type,    type);
    opPars.put(SSVarU.content,      content);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.uEAdd, opPars));
  }
  
  public static Boolean uEsRemove(
    final SSUri   user, 
    final SSUri   entity, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.uEsRemove, opPars));
  }
  
  /* user */
  
  public static SSUri userURIGet(
    final SSUri   user,
    final String  email) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,  user);
    opPars.put(SSVarU.email, email);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.userURIGet, opPars));
  }
  
  public static List<SSUser> userAll(
    final Boolean setFriends) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.setFriends,  setFriends);
    
    return (List<SSUser>) SSServA.callServViaServer(new SSServPar(SSMethU.userAll, opPars));
  }
  
  public static List<SSUser> usersGet(
    final List<SSUri> users,
    final Boolean     setFriends) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.users,      users);
    opPars.put(SSVarU.setFriends, setFriends);
    
    return (List<SSUser>) SSServA.callServViaServer(new SSServPar(SSMethU.usersGet, opPars));
  }
  
  public static Boolean userExists(
    final SSUri   user,
    final String  email) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.email,        email);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.userExists, opPars));
  }
  
  public static SSUri userAdd(
    final SSUri   user,
    final SSLabel label,
    final String  email,
    final Boolean isSystemUser,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.label,        label);
    opPars.put(SSVarU.email,        email);
    opPars.put(SSVarU.isSystemUser, isSystemUser);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.userAdd, opPars));
  }
  
  /* modeling user event */

  public static List<SSUri> modelUEEntitiesForMiGet(
    final SSUri    user, 
    final String   mi) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    opPars.put(SSVarU.mi,   mi);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.modelUEEntitiesForMiGet, opPars));
  }
  
  public static void modelUEUpdate() throws Exception{
    SSServA.callServViaServer(new SSServPar(SSMethU.modelUEUpdate, new HashMap<>()));
  }
  
  public static List<String> modelUEMIsForEntityGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.entity,   entity);
    
    return (List<String>) SSServA.callServViaServer(new SSServPar(SSMethU.modelUEMIsForEntityGet, opPars));
  }
  
  /* data export */
  
  public static void dataExportUserRelations(
    final SSUri                     user) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                  user);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.dataExportUserRelations, opPars));
  }
  
  public static void dataExportUserEntityTags(
    final SSUri                     user,
    final Map<String, List<String>> tagsPerEntities,
    final String                    fileName,
    final Boolean                   wasLastLine) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.fileName,              fileName);
    opPars.put(SSVarU.tagsPerEntities,       tagsPerEntities);
    opPars.put(SSVarU.user,                  user);
    opPars.put(SSVarU.wasLastLine,           wasLastLine);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.dataExportUserEntityTags, opPars));
  }
  
  public static void dataExportUserEntityTagTimestamps(
    final SSUri                     user,
    final Map<String, List<String>> tagsPerEntities,
    final Long                      timestampForTag,
    final String                    fileName,
    final Boolean                   wasLastLine) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.fileName,              fileName);
    opPars.put(SSVarU.tagsPerEntities,       tagsPerEntities);
    opPars.put(SSVarU.user,                  user);
    opPars.put(SSVarU.timestamp,             timestampForTag);
    opPars.put(SSVarU.wasLastLine,           wasLastLine);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.dataExportUserEntityTagTimestamps, opPars));
  }
  
  public static void dataExportUserEntityTagCategories(
    final SSUri                     user,
    final Map<String, List<String>> tagsPerEntities,
    final Map<String, List<String>> categoriesPerEntities,
    final String                    fileName,
    final Boolean                   wasLastLine) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.fileName,              fileName);
    opPars.put(SSVarU.tagsPerEntities,       tagsPerEntities);
    opPars.put(SSVarU.categoriesPerEntities, categoriesPerEntities);
    opPars.put(SSVarU.user,                  user);
    opPars.put(SSVarU.wasLastLine,           wasLastLine);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.dataExportUserEntityTagCategories, opPars));
  }
  
  public static void dataExportUserEntityTagCategoryTimestamps(
    final SSUri                     user,
    final Map<String, List<String>> tagsPerEntities,
    final Map<String, List<String>> categoriesPerEntities,
    final Long                      timestampForTag,
    final String                    fileName,
    final Boolean                   wasLastLine) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.fileName,              fileName);
    opPars.put(SSVarU.tagsPerEntities,       tagsPerEntities);
    opPars.put(SSVarU.categoriesPerEntities, categoriesPerEntities);
    opPars.put(SSVarU.user,                  user);
    opPars.put(SSVarU.timestamp,             timestampForTag);
    opPars.put(SSVarU.wasLastLine,           wasLastLine);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.dataExportUserEntityTagCategoryTimestamps, opPars));
  }
  
  /* category */
  
  public static Boolean categoriesPredefinedAdd(
    final SSUri        user,
    final List<String> labels,
    final Boolean      shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.labels,       labels);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.categoriesPredefinedAdd, opPars));
  }
    
  public static List<String> categoriesPredefinedGet(
    final SSUri  user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    
    return (List<String>) SSServA.callServViaServer(new SSServPar(SSMethU.categoriesPredefinedGet, opPars));
  }

  public static List<SSUri> categoryUserEntitiesForCategoriesGet(
    final SSUri        user, 
    final SSUri        forUser,
    final List<String> labels, 
    final SSSpaceE     space,
    final Long         startTime) throws Exception{
    
    final Map<String, Object>   opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.forUser,     forUser);
    opPars.put(SSVarU.labels,      labels);
    opPars.put(SSVarU.space,       space);
    opPars.put(SSVarU.startTime,   startTime);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.categoryUserEntitiesForCategoriesGet, opPars));
  }
  
  public static List<SSCategory> categoriesUserGet(
    final SSUri        user, 
    final SSUri        forUser,
    final List<SSUri>  entities, 
    final List<String> labels, 
    final SSSpaceE     space,
    final Long         startTime) throws Exception{
    
    final Map<String, Object>   opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.forUser,     forUser);
    opPars.put(SSVarU.entities,    entities);
    opPars.put(SSVarU.labels,      labels);
    opPars.put(SSVarU.space,       space);
    opPars.put(SSVarU.startTime,   startTime);
    
    return (List<SSCategory>) SSServA.callServViaServer(new SSServPar(SSMethU.categoriesUserGet, opPars));
  }
  
  public static List<SSCategoryFrequ> categoryUserFrequsGet(
    final SSUri        user, 
    final SSUri        forUser,
    final List<SSUri>  entities, 
    final List<String> labels,
    final SSSpaceE     space,
    final Long         startTime) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.forUser,     forUser);
    opPars.put(SSVarU.entities,    entities);
    opPars.put(SSVarU.labels,      labels);
    opPars.put(SSVarU.space,       space);
    opPars.put(SSVarU.startTime,   startTime);
    
    return (List<SSCategoryFrequ>) SSServA.callServViaServer(new SSServPar(SSMethU.categoryUserFrequsGet, opPars));
  }
  
  public static void categoriesRemove(
    final SSUri         forUser,   
    final SSUri         entity,
    final String        label,
    final SSSpaceE      space,
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.forUser,      forUser);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.label,        label);
    opPars.put(SSVarU.space,        space);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.categoriesRemove, opPars));
  }
  
  public static void categoriesUserRemove(
    final SSUri         user,
    final SSUri         entity,
    final String        label,
    final SSSpaceE      space,
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.label,        label);
    opPars.put(SSVarU.space,        space);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.categoriesUserRemove, opPars));
  }
  
  public static SSUri categoryAdd(
    final SSUri       user,
    final SSUri       entity,
    final String      label,
    final SSSpaceE    space,
    final Long        creationTime, 
    final Boolean     shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.space,        space);
    opPars.put(SSVarU.label,        label);
    opPars.put(SSVarU.creationTime, creationTime);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.categoryAdd, opPars));
  }
  
  public static List<SSUri> categoriesAdd(
    final SSUri            user,
    final SSUri            entity,
    final List<String>     labels,
    final SSSpaceE         space,
    final Long             creationTime,
    final Boolean          shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
          
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.labels,       labels);
    opPars.put(SSVarU.space,        space);
    opPars.put(SSVarU.creationTime, creationTime);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.categoriesAdd, opPars));
  }
  
  /* tag */
 
  public static List<SSUri> tagUserEntitiesForTagsGet(
    final SSUri        user, 
    final SSUri        forUser,
    final List<String> labels, 
    final SSSpaceE     space,
    final Long         startTime) throws Exception{
    
    final Map<String, Object>   opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.forUser,     forUser);
    opPars.put(SSVarU.labels,      labels);
    opPars.put(SSVarU.space,       space);
    opPars.put(SSVarU.startTime,   startTime);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.tagUserEntitiesForTagsGet, opPars));
  }
  
  public static List<SSTag> tagsUserGet(
    final SSUri        user, 
    final SSUri        forUser,
    final List<SSUri>  entities, 
    final List<String> labels, 
    final SSSpaceE     space,
    final Long         startTime) throws Exception{
    
    final Map<String, Object>   opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.forUser,     forUser);
    opPars.put(SSVarU.entities,    entities);
    opPars.put(SSVarU.labels,      labels);
    opPars.put(SSVarU.space,       space);
    opPars.put(SSVarU.startTime,   startTime);
    
    return (List<SSTag>) SSServA.callServViaServer(new SSServPar(SSMethU.tagsUserGet, opPars));
  }
  
  public static List<SSTagFrequ> tagUserFrequsGet(
    final SSUri        user, 
    final SSUri        forUser,
    final List<SSUri>  entities, 
    final List<String> labels,
    final SSSpaceE     space,
    final Long         startTime,
    final Boolean      useUsersEntities) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                 user);
    opPars.put(SSVarU.forUser,              forUser);
    opPars.put(SSVarU.entities,             entities);
    opPars.put(SSVarU.labels,               labels);
    opPars.put(SSVarU.space,                space);
    opPars.put(SSVarU.startTime,            startTime);
    opPars.put(SSVarU.useUsersEntities,     useUsersEntities);
    
    return (List<SSTagFrequ>) SSServA.callServViaServer(new SSServPar(SSMethU.tagUserFrequsGet, opPars));
  }
  
  public static void tagsRemove(
    final SSUri         forUser,   
    final SSUri         entity,
    final String        label,
    final SSSpaceE      space,
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.forUser,      forUser);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.label,        label);
    opPars.put(SSVarU.space,        space);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.tagsRemove, opPars));
  }
  
  public static void tagsUserRemove(
    final SSUri         user,
    final SSUri         entity,
    final String        label,
    final SSSpaceE      space,
    final Boolean       shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.label,        label);
    opPars.put(SSVarU.space,        space);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.tagsUserRemove, opPars));
  }
  
  public static SSUri tagAdd(
    final SSUri       user,
    final SSUri       entity,
    final String      label,
    final SSSpaceE    space,
    final Long        creationTime, 
    final Boolean     shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.space,        space);
    opPars.put(SSVarU.label,        label);
    opPars.put(SSVarU.creationTime, creationTime);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.tagAdd, opPars));
  }
  
  public static List<SSUri> tagsAdd(
    final SSUri            user,
    final SSUri            entity,
    final List<String>     labels,
    final SSSpaceE         space,
    final Long             creationTime,
    final Boolean          shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
          
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.entity,       entity);
    opPars.put(SSVarU.labels,       labels);
    opPars.put(SSVarU.space,        space);
    opPars.put(SSVarU.creationTime, creationTime);
    
    return (List<SSUri>) SSServA.callServViaServer(new SSServPar(SSMethU.tagsAdd, opPars));
  }
  
  /* recomm */
  
  public static Map<String, Double> recommTags(
    final SSUri         user,
    final SSUri         forUser,
    final SSUri         entity,
    final List<String>  categories,
    final Integer       maxTags,
    final Boolean       includeOwn) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,           user);
    opPars.put(SSVarU.forUser,        forUser);
    opPars.put(SSVarU.entity,         entity);
    opPars.put(SSVarU.categories,     categories);
    opPars.put(SSVarU.maxTags,        maxTags);
    opPars.put(SSVarU.includeOwn,     includeOwn);
    
    return (Map<String, Double>) SSServA.callServViaServer(new SSServPar(SSMethU.recommTags, opPars));
  }
  
  public static void recommTagsUpdate() throws Exception {
    SSServA.callServViaServer(new SSServPar(SSMethU.recommTagsUpdate, new HashMap<>()));
  }
  
  public static Map<SSEntity, Double> recommResources(
    final SSUri           user,
    final SSUri           forUser,
    final SSUri           entity,
    final List<String>    categories,
    final Integer         maxResources,
    final List<SSEntityE> typesToRecommOnly, 
    final Boolean         setCircleTypes,
    final Boolean         includeOwn) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,                   user);
    opPars.put(SSVarU.forUser,                forUser);
    opPars.put(SSVarU.entity,                 entity);
    opPars.put(SSVarU.categories,             categories);
    opPars.put(SSVarU.maxResources,           maxResources);
    opPars.put(SSVarU.typesToRecommOnly,      typesToRecommOnly);
    opPars.put(SSVarU.setCircleTypes,         setCircleTypes);
    opPars.put(SSVarU.includeOwn,             includeOwn);
    
    return (Map<SSEntity, Double>) SSServA.callServViaServer(new SSServPar(SSMethU.recommResources, opPars));
  }
  
  public static void recommResourcesUpdate() throws Exception {
    SSServA.callServViaServer(new SSServPar(SSMethU.recommResourcesUpdate, new HashMap<>()));
  }
  
  /* file */
  
  public static String fileThumbBase64Get(
    final SSUri user,
    final SSUri file) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,  user);
    opPars.put(SSVarU.file,  file);
    
    return (String) SSServA.callServViaServer(new SSServPar(SSMethU.fileThumbBase64Get, opPars));
  }
  
  public static SSFileCanWriteRet fileCanWrite(
    final SSUri user,
    final SSUri file) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,  user);
    opPars.put(SSVarU.file,  file);
    
    return (SSFileCanWriteRet) SSServA.callServViaServer(new SSServPar(SSMethU.fileCanWrite, opPars));
  }
  
  public static void fileUpdateWritingMinutes() throws Exception {
    SSServA.callServViaServer(new SSServPar(SSMethU.fileUpdateWritingMinutes, new HashMap<>()));
  }
  
  public static String fileExtGet(
    final SSUri  user,
    final SSUri  file) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,    user);
    opPars.put(SSVarU.file,    file);
    
    return (String) SSServA.callServViaServer(new SSServPar(SSMethU.fileExtGet, opPars));
  }
    
  public static String fileIDFromURI(
    final SSUri user, 
    final SSUri file) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,    user);
    opPars.put(SSVarU.file,    file);
    
    return (String) SSServA.callServViaServer(new SSServPar(SSMethU.fileIDFromURI, opPars));
  }
  
  public static void fileRemoveReaderOrWriter(
    final SSUri   user,
    final SSUri   file,
    final Boolean write,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,  shouldCommit);
    opPars.put(SSVarU.user,          user);
    opPars.put(SSVarU.file,          file);
    opPars.put(SSVarU.write,         write);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.fileRemoveReaderOrWriter, opPars));
  }
  
  /* scaff */
  
  public static List<String> scaffRecommTagsBasedOnUserEntityTag(
    final SSUri         user,
    final SSUri         forUser,
    final SSUri         entity,
    final Integer       maxTags) throws  Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.forUser,   forUser);
    opPars.put(SSVarU.entity,    entity);
    opPars.put(SSVarU.maxTags,      maxTags);
    
    return (List<String>) SSServA.callServViaServer(new SSServPar(SSMethU.scaffRecommTagsBasedOnUserEntityTag, opPars));
  }
  
  public static List<String> scaffRecommTagsBasedOnUserEntityTagTime(
    final SSUri         user,
    final SSUri         forUser,
    final SSUri         entity,
    final Integer       maxTags) throws  Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.forUser,  forUser);
    opPars.put(SSVarU.entity,   entity);
    opPars.put(SSVarU.maxTags,     maxTags);
    
    return (List<String>) SSServA.callServViaServer(new SSServPar(SSMethU.scaffRecommTagsBasedOnUserEntityTagTime, opPars));
  }
  
  public static List<String> scaffRecommTagsBasedOnUserEntityTagCategory(
    final SSUri         user,
    final SSUri         forUser,
    final SSUri         entity,
    final List<String>  categories,
    final Integer       maxTags) throws  Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,       user);
    opPars.put(SSVarU.forUser, forUser);
    opPars.put(SSVarU.entity,  entity);
    opPars.put(SSVarU.categories, categories);
    opPars.put(SSVarU.maxTags,    maxTags);
    
    return (List<String>) SSServA.callServViaServer(new SSServPar(SSMethU.scaffRecommTagsBasedOnUserEntityTagCategory, opPars));
  }
  
  public static List<String> scaffRecommTagsBasedOnUserEntityTagCategoryTime(
    final SSUri         user,
    final SSUri         forUser,
    final SSUri         entity,
    final List<String>  categories,
    final Integer       maxTags) throws  Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,       user);
    opPars.put(SSVarU.forUser, forUser);
    opPars.put(SSVarU.entity,  entity);
    opPars.put(SSVarU.categories, categories);
    opPars.put(SSVarU.maxTags,    maxTags);
    
    return (List<String>) SSServA.callServViaServer(new SSServPar(SSMethU.scaffRecommTagsBasedOnUserEntityTagCategoryTime, opPars));
  }
  
  /* data import */
  
  public static void dataImportAchso(
    final SSUri   user, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,     shouldCommit);
    opPars.put(SSVarU.user,             user);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.dataImportAchso, opPars));
  }
  
  public static void dataImportUserResourceTagFromWikipedia(
    final SSUri   user, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,     shouldCommit);
    opPars.put(SSVarU.user,             user);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.dataImportUserResourceTagFromWikipedia, opPars));
  }
  
  public static Map<String, String> dataImportSSSUsersFromCSVFile( 
    final String fileName) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.fileName, fileName);
    
    return (Map<String, String>) SSServA.callServViaServer(new SSServPar(SSMethU.dataImportSSSUsersFromCSVFile, opPars));
  }
  
  public static void dataImportEvernote(
    final SSUri   user, 
    final String  authToken, 
    final String  authEmail,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.authToken,    authToken);
    opPars.put(SSVarU.authEmail,    authEmail);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.dataImportEvernote, opPars));
  }
  
  public static void dataImportMediaWikiUser(
    final SSUri   user,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,        user);
    opPars.put(SSVarU.shouldCommit, shouldCommit);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.dataImportMediaWikiUser, opPars));
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
    
    opPars.put(SSVarU.user,              user);
    opPars.put(SSVarU.label,             label);
    opPars.put(SSVarU.email,             email);
    opPars.put(SSVarU.password,          password);
    opPars.put(SSVarU.isSystemUser,      isSystemUser);
    opPars.put(SSVarU.updatePassword,    updatePassword);
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.authRegisterUser, opPars));
  }
   
  public static void authUsersFromCSVFileAdd(
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,      shouldCommit);
    
    SSServA.callServViaServer(new SSServPar(SSMethU.authUsersFromCSVFileAdd, opPars));
  }
    
  public static SSUri checkKey(final SSServPar par) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.key, par.key);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.authCheckKey, opPars));
  }
  
  public static SSAuthCheckCredRet authCheckCred(
    final SSUri  user,
    final String key) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, user);
    opPars.put(SSVarU.key, key);
    
    return (SSAuthCheckCredRet) SSServA.callServViaServer(new SSServPar(SSMethU.authCheckCred, opPars));
  }
  
  /* i5Cloud */
  
  public static List<String> i5CloudAchsoSemanticAnnotationsSetGet(
    final List<String> ids) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.ids,   ids);
    
    return (List<String>) SSServA.callServViaServer(new SSServPar(SSMethU.i5CloudAchsoSemanticAnnotationsSetGet, opPars));
  }
    
  public static String i5CloudAchsoVideoInformationGet() throws Exception{
    return (String) SSServA.callServViaServer(new SSServPar(SSMethU.i5CloudAchsoVideoInformationGet, new HashMap<>()));
  }
    
  public static Map<String, String> i5CloudAuth() throws Exception{
    return (Map<String, String>) SSServA.callServViaServer(new SSServPar(SSMethU.i5CloudAuth, new HashMap<>()));
  }
  
  public static Boolean i5CloudFileUpload(
    final String fileName,
    final String containerSpace,
    final String authToken) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.fileName,   fileName);
    opPars.put(SSVarU.space,      containerSpace);
    opPars.put(SSVarU.authToken,  authToken);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.i5CloudFileUpload, opPars));
  }
  
  public static Boolean i5CloudFileDownload(
    final String fileName,
    final String containerSpace,
    final String authToken) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.fileName,   fileName);
    opPars.put(SSVarU.space,      containerSpace);
    opPars.put(SSVarU.authToken,  authToken);
    
    return (Boolean) SSServA.callServViaServer(new SSServPar(SSMethU.i5CloudFileDownload, opPars));
  } 

  /* activity */
  
  public static SSUri activityContentsAdd(
    final SSUri                     user, 
    final SSUri                     activity, 
    final SSActivityContentE        contentType,
    final List<SSActivityContent>   contents,
    final Boolean                   shouldCommit) throws Exception{
   
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarU.user,          user);
    opPars.put(SSVarU.activity,      activity);
    opPars.put(SSVarU.contentType,   contentType);
    opPars.put(SSVarU.contents,      contents);
    opPars.put(SSVarU.shouldCommit,  shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.activityContentsAdd, opPars));
  }
  
  public static SSUri activityContentAdd(
    final SSUri               user, 
    final SSUri               activity, 
    final SSActivityContentE  contentType,
    final SSActivityContent   content,
    final Boolean             shouldCommit) throws Exception{
   
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,          user);
    opPars.put(SSVarU.activity,      activity);
    opPars.put(SSVarU.contentType,   contentType);
    opPars.put(SSVarU.content,       content);
    opPars.put(SSVarU.shouldCommit,  shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.activityContentAdd, opPars));
  }
  
  public static SSUri activityAdd(
    final SSUri               user, 
    final SSActivityE         type, 
    final SSUri               entity, 
    final List<SSUri>         users, 
    final List<SSUri>         entities,
    final List<SSTextComment> comments,
    final Long                creationTime, 
    final Boolean             shouldCommit) throws Exception{
   
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,             user);
    opPars.put(SSVarU.entity,           entity);
    opPars.put(SSVarU.type,             type);
    opPars.put(SSVarU.users,            users);
    opPars.put(SSVarU.entities,         entities);
    opPars.put(SSVarU.comments,         comments);
    opPars.put(SSVarU.creationTime,     creationTime);
    opPars.put(SSVarU.shouldCommit,     shouldCommit);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.activityAdd, opPars));
  }
  
  public static List<SSActivity> activitiesUserGet(
    final SSUri               user, 
    final List<SSActivityE>   types, 
    final List<SSUri>         users, 
    final List<SSUri>         entities,
    final List<SSUri>         circles,
    final Long                startTime,
    final Long                endTime,
    final Boolean             includeOnlyLastActivities) throws Exception{
   
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarU.user,                      user);
    opPars.put(SSVarU.types,                     types);
    opPars.put(SSVarU.users,                     users);
    opPars.put(SSVarU.entities,                  entities);
    opPars.put(SSVarU.circles,                   circles);
    opPars.put(SSVarU.startTime,                 startTime);
    opPars.put(SSVarU.endTime,                   endTime);
    opPars.put(SSVarU.includeOnlyLastActivities, includeOnlyLastActivities);
    
    return (List<SSActivity>) SSServA.callServViaServer(new SSServPar(SSMethU.activitiesUserGet, opPars));
  }
  
  /* video */
  
  public static List<? extends SSEntity> videosUserGet(
    final SSUri user, 
    final SSUri forUser,
    final SSUri forEntity) throws Exception{
   
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user,         user);
    opPars.put(SSVarU.forUser,      forUser);
    opPars.put(SSVarU.forEntity,    forEntity);
    
    return (List<? extends SSEntity>) SSServA.callServViaServer(new SSServPar(SSMethU.videosUserGet, opPars));
  }
  
  public static SSEntity videoUserGet(
    final SSUri   user,
    final SSUri video) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarU.user,             user);
    opPars.put(SSVarU.video,            video);
    
    return (SSEntity) SSServA.callServViaServer(new SSServPar(SSMethU.videoUserGet, opPars));
  }
  
  public static SSUri videoUserAdd(
    final SSUri   user,
    final SSUri   link,
    final SSUri   forEntity,
    final String  uuid,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarU.shouldCommit,     shouldCommit);
    opPars.put(SSVarU.user,             user);
    opPars.put(SSVarU.link,             link);
    opPars.put(SSVarU.uuid,             uuid);
    opPars.put(SSVarU.forEntity,        forEntity);
    
    return (SSUri) SSServA.callServViaServer(new SSServPar(SSMethU.videoUserAdd, opPars));
  }
}



//public static List<SSTag> recommTagsFolkRank(
//    final SSUri         user, 
//    final SSUri         forUser, 
//    final SSUri         entity, 
//    final Integer       maxTags) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.user,           user);
//    opPars.put(SSVarU.forUser,        forUser);
//    opPars.put(SSVarU.entity,      entity);
//    opPars.put(SSVarU.maxTags,        maxTags);
//    
//    return (List<SSTag>) SSServA.callServViaServer(new SSServPar(SSMethU.recommTagsFolkRank, opPars));
//  }
//  
//  public static List<SSTag> recommTagsLDA(
//    final SSUri         user, 
//    final SSUri         forUser, 
//    final SSUri         entity, 
//    final Integer       maxTags) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.user,           user);
//    opPars.put(SSVarU.forUser,        forUser);
//    opPars.put(SSVarU.entity,      entity);
//    opPars.put(SSVarU.maxTags,        maxTags);
//    
//    return (List<SSTag>) SSServA.callServViaServer(new SSServPar(SSMethU.recommTagsLDA, opPars));
//  }
//    
//  public static List<SSTag> recommTagsCollaborativeFilteringOnUserSimilarity(
//    final SSUri         user, 
//    final SSUri         forUser, 
//    final SSUri         entity, 
//    final Integer       maxTags) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.user,           user);
//    opPars.put(SSVarU.forUser,        forUser);
//    opPars.put(SSVarU.entity,      entity);
//    opPars.put(SSVarU.maxTags,        maxTags);
//    
//    return (List<SSTag>) SSServA.callServViaServer(new SSServPar(SSMethU.recommTagsCollaborativeFilteringOnUserSimilarity, opPars));
//  }
//  
//  public static List<SSTag> recommTagsCollaborativeFilteringOnEntitySimilarity(
//    final SSUri         user, 
//    final SSUri         forUser, 
//    final SSUri         entity, 
//    final Integer       maxTags) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.user,           user);
//    opPars.put(SSVarU.forUser,        forUser);
//    opPars.put(SSVarU.entity,      entity);
//    opPars.put(SSVarU.maxTags,        maxTags);
//    
//    return (List<SSTag>) SSServA.callServViaServer(new SSServPar(SSMethU.recommTagsCollaborativeFilteringOnEntitySimilarity, opPars));
//  }
//  
//  public static List<SSTag> recommTagsAdaptedPageRank(
//    final SSUri         user, 
//    final SSUri         forUser, 
//    final SSUri         entity, 
//    final Integer       maxTags) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.user,           user);
//    opPars.put(SSVarU.forUser,        forUser);
//    opPars.put(SSVarU.entity,      entity);
//    opPars.put(SSVarU.maxTags,        maxTags);
//    
//    return (List<SSTag>) SSServA.callServViaServer(new SSServPar(SSMethU.recommTagsAdaptedPageRank, opPars));
//  }

//public static List<SSTag> recommTagsTemporalUsagePatterns(
//    final SSUri         user, 
//    final SSUri         forUser, 
//    final SSUri         entity, 
//    final Integer       maxTags) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.user,           user);
//    opPars.put(SSVarU.forUser,        forUser);
//    opPars.put(SSVarU.entity,      entity);
//    opPars.put(SSVarU.maxTags,        maxTags);
//    
//    return (List<SSTag>) SSServA.callServViaServer(new SSServPar(SSMethU.recommTagsTemporalUsagePatterns, opPars));
//  }