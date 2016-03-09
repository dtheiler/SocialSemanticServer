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
package at.tugraz.sss.servs.kcprojwiki.impl;

import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportKCProjWikiVorgaengePar;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.servs.kcprojwiki.api.SSKCProjWikiClientI;
import at.tugraz.sss.servs.kcprojwiki.api.SSKCProjWikiServerI;
import at.tugraz.sss.servs.kcprojwiki.conf.SSKCProjWikiConf;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiImportPar;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgang;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SSKCProjWikiImpl
  extends SSServImplWithDBA
  implements
  SSKCProjWikiClientI,
  SSKCProjWikiServerI{
  
  private final SSKCProjWikiImportCommons kcProjWikiImportCommons = new SSKCProjWikiImportCommons();
  
  public SSKCProjWikiImpl(final SSConfA conf){
    
    super(conf, null, null);
  }
  
  @Override
  public void kcprojwikiImport(final SSKCProjWikiImportPar par) throws SSErr{
    
    try{
      
      final SSDataImportServerI                dataImportServ = (SSDataImportServerI) SSServReg.getServ(SSDataImportServerI.class);
      final Map<String, SSKCProjWikiVorgang>   vorgaenge      =
        dataImportServ.dataImportKCProjWikiVorgaenge(
          new SSDataImportKCProjWikiVorgaengePar(
            par,
            par.user,
            ((SSKCProjWikiConf) conf).vorgaengeFilePath));
      
      final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
      final Long       now        = new Date().getTime();
      final String     exportDate = dateFormat.format(now);
      
      SSLogU.info("start vorgaenge update");
      
      kcProjWikiImportCommons.start(((SSKCProjWikiConf) conf));
      
      SSKCProjWikiVorgang vorgang = null;
      
      try{
        
        for(Map.Entry<String, SSKCProjWikiVorgang> vorgangEntry : vorgaenge.entrySet()){
          
          vorgang       = vorgangEntry.getValue();
          vorgang.title = kcProjWikiImportCommons.getVorgangPageTitleByVorgangNumber   (((SSKCProjWikiConf) conf), vorgang.vorgangNumber);
          
          if(vorgang.title == null){
            
            if(!kcProjWikiImportCommons.createVorgang((SSKCProjWikiConf) conf, vorgang)){
              continue;
            }
          }
          
          vorgang.exportDate = exportDate;
          
          if(vorgang.totalResources == 0){
            vorgang.progress = 0F;
          }else{
            vorgang.progress = (vorgang.usedResources / vorgang.totalResources) * 100;
          }
          
          kcProjWikiImportCommons.updateVorgangBasics            (((SSKCProjWikiConf) conf), vorgang);
          kcProjWikiImportCommons.updateVorgangEmployeeResources (((SSKCProjWikiConf) conf), vorgang);
          
          System.out.println("vorgang " + vorgang.title + " updated");
        }
        
      }catch(Exception error){
        SSLogU.warn("import for vorgang (" + vorgang.title + ", " + vorgang.vorgangNumber + ") failed", error);
      }
      
      kcProjWikiImportCommons.end(((SSKCProjWikiConf) conf));
      
      SSLogU.info("end vorgaenge update");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

//      final Map<String, SSKCProjWikiProject> projects =
//        dataImportServ.dataImportKCProjWikiProjects(
//          new SSDataImportKCProjWikiProjectsPar(
//            par.user,
//            projWikiConf.projectsFileName));
//
//      for(Map.Entry<String, SSKCProjWikiProject> project : projects.entrySet()){
//
//        project.getValue().title = importFct.getProjectPageTitleByProjectNumber   (project.getValue().projectNumber); //"20143516"
//
//        importFct.changeVorgangBasics    (project.getValue().title);
//        importFct.changeVorgangResources (project.getValue().title);
//      }