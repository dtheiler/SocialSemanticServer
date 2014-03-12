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
package at.kc.tugraz.ss.log.conf;

import at.kc.tugraz.ss.serv.serv.api.SSServConfA;

public class SSLogConf extends SSServConfA{
  
  public String logLevelRoot = null;
  public String logLevel     = null;
  
//  @Override
//  public String getLogLevelRoot() throws Exception{
//    
//    if(logLevelRoot == null){
//      SSLogU.logAndThrow(new Exception("no root logger found"));
//    }
//    
//    return logLevelRoot;
////    return Logger.getRootLogger().getLevel().toString();
//  }
//  
//  @Override
//  public void setLogLevelRoot(
//    final String level) throws Exception {
//    
//    Logger root = Logger.getRootLogger();
//    
//    if(root == null) {
//      SSLogU.logAndThrow(new Exception("no root logger found"));
//    }
//    
//    setLevel(root, SSLogLevelEnum.valueOf(level));
//      
//    logLevelRoot = level;
//  }
//  
//  @Override
//  public String getLogLevel(
//    String logger) throws Exception{
//    
//    Logger log = Logger.getLogger(logger);
//    
//    if (log == null){
//      SSLogU.logAndThrow(new Exception("no logger found"));
//    }
//    
//    return logLevel;
//    //return log.getLevel().toString();
//  }
//  
//  @Override
//  public void setLogLevel(
//    String logger, 
//    String level) throws Exception {
//    
//    Logger log = Logger.getLogger(logger);
//    
//    if (log == null) {
//      SSLogU.logAndThrow(new Exception("no logger found"));
//    }
//    
//    setLevel(log, SSLogLevelEnum.valueOf(level));
//    
//    logLevel = level;
//  }
//  
//  private synchronized void setLevel(
//    Logger         logger, 
//    SSLogLevelEnum level) {
//    
//    switch(level){
//      
//      case debug:
//        logger.setLevel(Level.DEBUG);
//        break;
//        
//      case error:
//        logger.setLevel(Level.ERROR);
//        break;
//        
//      case fatal:
//        logger.setLevel(Level.FATAL);
//        break;
//        
//      case info:
//        logger.setLevel(Level.INFO);
//        break;
//        
//      case warn:
//        logger.setLevel(Level.WARN);
//        break;
//        
//      default:
//        logger.setLevel(Level.INFO);
//    }
//  }
}