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
package at.kc.tugraz.ss.serv.dataimport.impl.bitsandpieces;

import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.mail.SSMailServerI;
import at.tugraz.sss.servs.mail.datatype.par.SSMailsReceivePar;
import java.util.List;

public class SSDataImportBitsAndPiecesMailImporter {

  public void handle(
    final SSDataImportBitsAndPiecesPar par) throws Exception{

    try{
     
      if(par.emailInEmail == null){
        return;
      }
      
      final SSMailServerI mail = (SSMailServerI) SSServReg.getServ(SSMailServerI.class);
      
      SSLogU.info("start B&P mail import for " +  par.authEmail);
      
      final List<SSEntity> mails =
        mail.mailsReceive(
          new SSMailsReceivePar(
            SSVocConf.systemUserUri,
            par.emailInEmail,
            par.emailInPassword,
            true,  //withUserRestriction
            par.shouldCommit));
      
      SSLogU.info("end B&P mail import for " +  par.authEmail);
      
    }catch(Exception error){
      SSLogU.warn("B&P mail import failed for " + par.authEmail);
      SSServErrReg.regErrThrow(error);
    }finally{
    }
  }
}