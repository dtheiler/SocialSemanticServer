package at.tugraz.sss.serv;

import static at.tugraz.sss.serv.SSServRegI.servs;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class SSServContainerI{
 
  public        SSConfA                                         conf                        = null;
  public        final Class                                     servImplClientInteraceClass;
  public        final Class                                     servImplServerInteraceClass;
  protected     Exception                                       servImplCreationError           = null;
  
  private final ThreadLocal<SSServImplA> servImplsByServByThread = new ThreadLocal<SSServImplA>(){
    
    @Override protected SSServImplA initialValue() {
      
      try{
        return createServImplForThread();
      }catch (Exception error){
        SSLogU.err(error);
        servImplCreationError = error;
        return null;
      }
    }
  };
  
  protected SSServContainerI(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    this.servImplClientInteraceClass = servImplClientInteraceClass;
    this.servImplServerInteraceClass = servImplServerInteraceClass;
  }
  
  protected abstract SSServImplA      createServImplForThread   () throws Exception;
  public    abstract SSCoreConfA      getConfForCloudDeployment (final SSCoreConfA coreConfA, final List<Class> configuredServs) throws Exception;
  public    abstract void             initServ                  () throws Exception;
  public    abstract void             schedule                  () throws Exception;
  
  protected SSCoreConfA getConfForCloudDeployment(
    final Class       servI,
    final SSCoreConfA coreConf,
    final List<Class> configuredServs) throws Exception{
    
    if(configuredServs.contains(servI)){
      return coreConf;
    }
    
    for(SSServContainerI serv : servs.values()){
      
      if(servI.isInstance(serv)){
        
        configuredServs.add(servI);
        
        return serv.getConfForCloudDeployment(coreConf, configuredServs);
      }
    }
    
    throw new Exception("service not registered");
  }
  
  public List<SSServOpE> publishClientOps() throws Exception{
    
    final List<SSServOpE> clientOps = new ArrayList<>();
    
    if(servImplClientInteraceClass == null){
      return clientOps;
    }
    
    final Method[]      methods   = servImplClientInteraceClass.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSServOpE.get(method.getName()));
    }
    
    return clientOps;
  }
  
  public List<SSServOpE> publishServerOps() throws Exception{
    
    final List<SSServOpE> serverOps = new ArrayList<>();
    
    if(servImplServerInteraceClass == null){
      return serverOps;
    }
    
    final Method[]      methods   = servImplServerInteraceClass.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSServOpE.get(method.getName()));
    }
    
    return serverOps;
  }
  
  public SSServImplA serv() throws Exception{
    
    try{
      
      if(!conf.use){
        return null;
      }
      
      final SSServImplA servTmp = servImplsByServByThread.get();
      
      if(servImplCreationError != null){
        throw servImplCreationError;
      }
      
      SSServImplStartA.regServImplUsedByThread (servTmp);
      
      return servTmp;
    }catch(Exception error){
      SSLogU.err(error);
      throw error;
    }
  }
  
  public SSServContainerI regServ(final SSConfA conf) throws Exception{
    this.conf = conf;
    
    return this;
  }
}