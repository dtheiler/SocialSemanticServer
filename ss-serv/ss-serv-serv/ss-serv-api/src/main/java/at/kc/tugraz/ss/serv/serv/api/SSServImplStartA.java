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
package at.kc.tugraz.ss.serv.serv.api;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public abstract class SSServImplStartA extends SSServImplA implements Runnable{

  private static final ThreadLocal<List<SSServImplA>> servImplsUsedByThread = new ThreadLocal<List<SSServImplA>>(){
    
    @Override protected List<SSServImplA> initialValue() {
      
      try{
        return new ArrayList<SSServImplA>();
      }catch (Exception error){
        SSServErrReg.regErr(error);
        return null;
      }
    }
  };
  
  public SSServImplStartA(final SSServConfA conf){
    super(conf);
  }
  
  public static void regServImplUsedByThread(final SSServImplA servImpl){
    
    List<SSServImplA> servImplUsedList = servImplsUsedByThread.get();
    
    if(servImplUsedList.contains(servImpl)){
      return;
    }
    
    servImplUsedList.add(servImpl);
  }
  
  protected void finalizeThread(){
    
    List<SSServImplA> usedServs = new ArrayList<SSServImplA>();
    
    try{
      servImplsUsedByThread.get().remove(this);

      usedServs.addAll(servImplsUsedByThread.get());

      for(SSServImplA servImpl : usedServs){
        servImpl.finalizeImpl();
      }
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }finally{
      SSServErrReg.logServImplErrors();
    }
  }
  
  @Override
  public void handleClientOp(
    final Class       servImplClientInteraceClass, 
    final SSSocketCon sSCon, 
    final SSServPar   par) throws Exception{
    
    throw new UnsupportedOperationException(SSStrU.empty);
  }
  
  @Override
  public Object handleServerOp(
    final Class     servImplServerInteraceClass, 
    final SSServPar par) throws Exception{
    
    throw new UnsupportedOperationException(SSStrU.empty);
  }

  @Override
  public List<SSMethU> publishClientOps(final Class clientInterfaceClass) throws Exception{
    return new ArrayList<SSMethU>();
  }

  @Override
  public List<SSMethU> publishServerOps(final Class serverInterfaceClass) throws Exception{
    return new ArrayList<SSMethU>();
  }
}
