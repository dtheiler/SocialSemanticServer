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
package at.kc.tugraz.ss.serv.dataimport.impl;

import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSLabel;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportClientI;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportAchsoPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportUserResourceTagFromWikipediaPar;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernotePar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportMediaWikiUserPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportSSSUsersFromCSVFilePar;
import at.kc.tugraz.ss.serv.dataimport.impl.evernote.SSDataImportEvernoteHandler;
import at.kc.tugraz.ss.serv.dataimport.impl.fct.op.SSDataImportAchsoFct;
import at.kc.tugraz.ss.serv.dataimport.impl.fct.reader.SSDataImportReaderFct;
import at.kc.tugraz.ss.serv.dataimport.impl.fct.sql.SSDataImportSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.SSi5CloudAchsoVideo;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;

public class SSDataImportImpl extends SSServImplWithDBA implements SSDataImportClientI, SSDataImportServerI{
  
  private static final ReentrantReadWriteLock  currentlyRunEvernoteImportsLock  = new ReentrantReadWriteLock();
  private static final List<String>            currentlyRunEvernoteImports      = new ArrayList<>();
    
  private final SSDataImportSQLFct          sqlFct;
  private final SSDataImportEvernoteHandler dataImpEvernoteHelper;
  
  
  public SSDataImportImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct                = new SSDataImportSQLFct         (dbSQL);
    this.dataImpEvernoteHelper = new SSDataImportEvernoteHandler (dbSQL); 
  }
  
  @Override
  public Map<String, String> dataImportSSSUsersFromCSVFile(final SSServPar parA) throws Exception{
    
    final SSDataImportSSSUsersFromCSVFilePar par = new SSDataImportSSSUsersFromCSVFilePar(parA);
    final List<String[]>                     lines;
    
    try{
      lines = SSDataImportReaderFct.readAllFromCSV(SSFileU.dirWorking(), par.fileName);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
      
    final Map<String, String> passwordPerUser = new HashMap<>();
    
    try{
      
      for(String[] line : lines){

        try{
          passwordPerUser.put(line[0].trim(), line[1].trim());
        }catch(Exception error){
        }
      }
      
      return passwordPerUser;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean dataImportEvernote(final SSServPar parA) throws Exception{
    
    String authToken = null;
    
    try{
      
      final SSDataImportEvernotePar par = new SSDataImportEvernotePar(parA);
     
      authToken = par.authToken;
        
      if(!addCurrentlyRunEvernotImport(authToken)){
        return false;
      }
      
      SSLogU.info("start data import for evernote account " + authToken);                
      
      dbSQL.startTrans(par.shouldCommit);
      
      dataImpEvernoteHelper.setBasicEvernoteInfo  (par);
      
      dataImpEvernoteHelper.handleLinkedNotebooks ();
      dataImpEvernoteHelper.setSharedNotebooks    ();
      dataImpEvernoteHelper.handleNotebooks       ();
      dataImpEvernoteHelper.handleNotes           ();
      dataImpEvernoteHelper.handleResources       ();
      
      dataImpEvernoteHelper.setUSN();
      
      dbSQL.commit(par.shouldCommit);
      
      SSLogU.info("end data import for evernote account " + authToken);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
       
          SSServErrReg.reset();
          
          removeCurrentlyRunEvernoteImport(authToken);
          
          return dataImportEvernote(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      removeCurrentlyRunEvernoteImport(authToken);
    }
  }
  
  private Boolean addCurrentlyRunEvernotImport(final String authToken){
    
    if(!currentlyRunEvernoteImportsLock.isWriteLocked()){
      currentlyRunEvernoteImportsLock.writeLock().lock();
    }
    
    if(currentlyRunEvernoteImports.contains(authToken)){
      SSLogU.warn("import currently runs for " + authToken);
      return false;
    }else{
      currentlyRunEvernoteImports.add(authToken);
      
      currentlyRunEvernoteImportsLock.writeLock().unlock();
      return true;
    }
  }
  
  private void removeCurrentlyRunEvernoteImport(final String authToken){
    
    if(!currentlyRunEvernoteImportsLock.isWriteLocked()){
      currentlyRunEvernoteImportsLock.writeLock().lock();
    }
    
    if(authToken != null){
      currentlyRunEvernoteImports.remove(authToken);
    }
    
    currentlyRunEvernoteImportsLock.writeLock().unlock();
  }
  
  @Override
  public void dataImportMediaWikiUser(final SSServPar parA) throws Exception{

    final SSDataImportMediaWikiUserPar par         = new SSDataImportMediaWikiUserPar(parA);
    final List<String[]>               lines;
    
    try{
      lines = SSDataImportReaderFct.readAllFromCSV(SSFileU.dirWorkingDataCsv(), ((SSDataImportConf)conf).fileName);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return;
    }
    
    String firstName;
    String familyName;
    String password;
    String userName;
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(String[] line : lines){

        try{
          firstName  = line[0].trim();
          familyName = line[1].trim();
          password   = line[2].trim();
          userName   = firstName + familyName;
          
          userName = userName.replaceAll("[^a-zA-Z0-9]+", SSStrU.empty); 
          
          System.out.println(userName);
          
        }catch(Exception error){
          continue;
        }

        sqlFct.addUserWithGroup(userName, password);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      
      }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          dataImportMediaWikiUser(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  //TODO dtheiler: add transactions here
  @Override
  public void dataImportAchso(final SSServPar parA) throws Exception {
    
    try{
      final SSDataImportAchsoPar      par          = new SSDataImportAchsoPar(parA);
      final List<SSi5CloudAchsoVideo> videoObjs    = 
        SSDataImportAchsoFct.getVideoObjs(
          par.user, 
          SSServCaller.i5CloudAchsoVideoInformationGet());
      
      SSUri authorUri;

      for(SSi5CloudAchsoVideo video : videoObjs){
        
        authorUri =
          SSServCaller.authRegisterUser(
            par.user,
            video.authorLabel,
            video.authorLabel + SSStrU.at + SSVocConf.systemEmailPostFix,
            "1234",
            false,
            false, 
            true);
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            authorUri,
            video.id,
            null, //type,
            video.label, //label
            null, //description,
            null, //entitiesToAttach,
            video.creationTime, //creationTime,
            null, //read,
            false, //setPublic
            true, //withUserRestriction
            true)); //shouldCommit)
                
        ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsAdd(
          new SSTagsAddPar(
            null,
            null,
            authorUri,
            SSTagLabel.get(video.keywords), //labels,
            video.id, //entity
            SSSpaceE.sharedSpace, //space
            null, //circles
            video.creationTime,  //creationTime
            true, //withUserRestriction
            true)); //shouldCommit
        
        final List<String> categoryLabels = new ArrayList<>();
        
        for(String annotation : video.annotations){
          
          try{
            SSCategoryLabel.get(annotation);
          }catch(Exception error){
            continue;            
          }
          
          categoryLabels.add(annotation);
        }

        ((SSCategoryServerI) SSServReg.getServ(SSCategoryServerI.class)).categoriesAdd(
          new SSCategoriesAddPar(
            null,
            null,
            authorUri,
            SSCategoryLabel.asListWithoutNullAndEmpty(SSCategoryLabel.get(categoryLabels)),
            video.id,
            SSSpaceE.sharedSpace,
            video.creationTime,
            false,
            true));
      }
      
      System.out.println();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean dataImportUserResourceTagFromWikipedia(final SSServPar parA) throws Exception {
    
    final SSDataImportUserResourceTagFromWikipediaPar par           = new SSDataImportUserResourceTagFromWikipediaPar(parA);
    int                                               counter       = 1;
    int                                               tagCounter    = 0;
    BufferedReader                                    lineReader    = null;
    List<String>                                      tagList;
    FileInputStream                                   dataImportFileIn;
    String                                            line;
    List<String>                                      lineSplit;
    String                                            userLabel;
    SSUri                                             resource;
    SSUri                                             user;
    String                                            tags;
    String                                            categories;
    Long                                              timestamp;
    
    try{
      ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsRemove(
        new SSTagsRemovePar(
          null,
          null,
          parA.user,
          null,
          null,
          null,
          SSSpaceE.sharedSpace,
          false,
          par.shouldCommit));
      
      dataImportFileIn = SSFileU.openFileForRead   (SSFileU.dirWorkingDataCsv() + ((SSDataImportConf)conf).fileName);
      lineReader       = new BufferedReader        (new InputStreamReader(dataImportFileIn));
      line             = lineReader.readLine();
      
      while(line != null){
        
        line       = SSStrU.removeDoubleQuotes(line); //        line       = SSStrU.replaceAll(line, SSStrU.dot,     SSStrU.empty);
        line       = SSStrU.replaceAll        (line, SSStrU.percent, SSStrU.empty);
        lineSplit  = SSStrU.split             (line, SSStrU.semiColon);
        
        if(
          lineSplit == null ||
          lineSplit.size() < 5){
          
          line = lineReader.readLine();
          continue;
        }
        
        categories = lineSplit.get(4);
        
//        if(!categories.contains("health")){
//          line = lineReader.readLine();
//          continue;
//        }
        
        try{
          resource  = SSUri.get(lineSplit.get(1));
        }catch(Exception error){
          line = lineReader.readLine();
          continue;
        }
        
        userLabel   = lineSplit.get   (0);
        timestamp   = Long.parseLong  (lineSplit.get(2)) * 1000;
        tags        = lineSplit.get   (3);
        
        user = 
          SSServCaller.authRegisterUser(
            par.user, 
            SSLabel.get(userLabel), 
            userLabel + SSStrU.at + SSVocConf.systemEmailPostFix,
            "1234",
            false,
            false,
            false);
        
        tagList     = SSStrU.splitDistinctWithoutEmptyAndNull(tags, SSStrU.comma);
        tagCounter += tagList.size();

        ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsAdd(
          new SSTagsAddPar(
            null,
            null,
            user,
            SSTagLabel.get(tagList), //labels
            resource, //entity
            SSSpaceE.sharedSpace, //space
            null, //circles
            timestamp, //creationTime
            true, //withUserRestriction
            par.shouldCommit)); //shouldCommit

        SSLogU.info("line " + counter++ + " " + tagCounter + " time : " + new Date().getTime() + " user: " + user.toString() + " tags: " + tags);

        line = lineReader.readLine();
      }

      return true;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return dataImportUserResourceTagFromWikipedia(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(lineReader != null){
        lineReader.close();
      }
    }
  }
}

//  private Boolean evernoteNoteUploadPNGToWebDav(String pngFilePath, String pngFileID){
//    
//    FileInputStream in = null;
//    
//    try{
//      SSFileU.openFileForRead(pngFilePath);
//    
//      SardineFactory.begin("wduza", "w!123a").put("http://kedemo.know-center.tugraz.at:80/webdav/knowBrainWeb/" + pngFileID, in);
//      
//      return true;
//      
//    }catch(Exception error){
//      SSLogU.logError(error, "couldnt upload note png to webdav");
//      return false;
//    }finally{
//      
//      if(in != null){
//        
//        try {
//          in.close();
//        } catch (IOException error2) {
//          SSLogU.logError(error2, "couldnt close note png input stream for webdav");
//          return false;
//        }
//      }
//    }
//  }


//    try{
//      
//      String inputFile = SSFileU.dirWorkingTmp() + htmlFileName;
//        String url = new File(inputFile).toURI().toURL().toString();
//        String outputFile = SSFileU.dirWorkingTmp() + pdfFileName;
//        noteOutputStream = new FileOutputStream(outputFile);
//        
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocument(url);
//        renderer.layout();
//        renderer.createPDF(noteOutputStream);
//      
//    } catch (Exception error1) {
//      SSLogU.logError(error1, "couldnt write to pdf");
//    }finally{
//      
//      if(noteOutputStream != null){
//        
//        try {
//          noteOutputStream.close();
//        } catch (IOException error) {
//          SSLogU.logError(error, "couldnt close pdf writer");
//        }
//      }
//    }