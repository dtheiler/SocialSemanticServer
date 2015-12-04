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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.List;

public class SSErrForClient extends Exception{

  public final String    id;
  public final String    className;
  public final String    message;
  public final Long      threadWhereThrown;
  public final String    classWhereThrown;
  public final String    methodWhereThrown;
  public final Integer   lineWhereThrown;
  public final Exception exception;
  
  public static SSErrForClient get(final Exception originalError) throws SSErr{
    return new SSErrForClient(originalError);
  }
  
  private SSErrForClient(final Exception originalError) throws SSErr{
    
    try{
      
      if(
        originalError            == null ||
        originalError.getClass() == null){
        throw new Exception();
      }
      
      if(originalError instanceof SSErr){
        this.id        = ((SSErr) originalError).code.toString();
      }else{
        this.id        = null;
      }
      
      this.exception = originalError;
      this.className = originalError.getClass().getName();
      this.message   = originalError.getMessage();
      
      threadWhereThrown  = Thread.currentThread().getId();
      classWhereThrown   = Thread.currentThread().getStackTrace()[4].getClassName();
      methodWhereThrown  = Thread.currentThread().getStackTrace()[4].getMethodName();
      lineWhereThrown    = Thread.currentThread().getStackTrace()[4].getLineNumber();
    
    }catch(Exception error){
      throw new SSErr(SSErrE.clientErrorCreationFailed);
    }
  }
  
  public static List<String> linesWhereThrown(List<SSErrForClient> errors) {
    
    List<String> linesWhereThrown = new ArrayList<>();
    
    if(errors == null){
      return linesWhereThrown;
    }
    
    for(SSErrForClient error: errors){
      linesWhereThrown.add(SSStrU.toStr(error.lineWhereThrown));
    }
    
    return linesWhereThrown;
  }
  
  public static List<String> methodsWhereThrown(List<SSErrForClient> errors) {
    
    List<String> methodsWhereThrown = new ArrayList<>();
    
    if(errors == null){
      return methodsWhereThrown;
    }
    
    for(SSErrForClient error: errors){
      methodsWhereThrown.add(SSStrU.toStr(error.methodWhereThrown));
    }
    
    return methodsWhereThrown;
  }
  
  public static List<String> classesWhereThrown(List<SSErrForClient> errors) {
    
    List<String> classesWhereThrown = new ArrayList<>();
    
    if(errors == null){
      return classesWhereThrown;
    }
    
    for(SSErrForClient error: errors){
      classesWhereThrown.add(SSStrU.toStr(error.classWhereThrown));
    }
    
    return classesWhereThrown;
  }
  
  public static List<String> threadsWhereThrown(List<SSErrForClient> errors) {
    
    List<String> threadsWhereThrown = new ArrayList<>();
    
    if(errors == null){
      return threadsWhereThrown;
    }
    
    for(SSErrForClient error: errors){
      threadsWhereThrown.add(SSStrU.toStr(error.threadWhereThrown));
    }
    
    return threadsWhereThrown;
  }
  
  public static List<String> messages(List<SSErrForClient> errors) {
    
    List<String> messages = new ArrayList<>();
    
    if(errors == null){
      return messages;
    }
    
    for(SSErrForClient error: errors){
      messages.add(error.message);
    }
    
    return messages;
  }
  
  public static List<String> classNames(List<SSErrForClient> errors) {
    
    List<String> classNames = new ArrayList<>();
    
    if(errors == null){
      return classNames;
    }
    
    for(SSErrForClient error : errors){
      classNames.add(error.className);
    }
    
    return classNames;
  }
}
