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
package at.kc.tugraz.ss.serv.lomextractor.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.ArrayList;
import java.util.List;

public class SSLOMCoverage extends SSEntityA{
  
  public String label = null;
  public String lang  = null;
  
  public SSLOMCoverage(
    String label, 
    String lang){
    
    super(label);
    
    this.label = label;
    this.lang  = lang;
  }
  
  public static List<SSLOMCoverage> distinctCoverages(List<SSLOMCoverage> coverages){
    
    List<String>        coverageLabels = new ArrayList<String>();
    List<SSLOMCoverage> result         = new ArrayList<SSLOMCoverage>();
    
    for(SSLOMCoverage coverage : coverages){
      
      if(
        SSStrU.isNotEmpty       (coverage.label) &&
        !coverageLabels.contains (coverage.label)){
        
        coverageLabels.add  (coverage.label);
        result.add          (coverage);
      }
    }
    
    return result;
  }
  
  @Override
  public Object jsonLDDesc(){
    return "dtheiler";
  }
}