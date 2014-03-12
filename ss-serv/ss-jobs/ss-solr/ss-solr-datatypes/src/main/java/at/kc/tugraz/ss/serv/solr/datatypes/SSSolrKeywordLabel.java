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
package at.kc.tugraz.ss.serv.solr.datatypes;

import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SSSolrKeywordLabel extends SSEntityA{

  public static String toStr(final SSSolrKeywordLabel keywordLabel){
    return SSStrU.toString(keywordLabel);
  }

  public static SSSolrKeywordLabel get(final String string) throws Exception{
    return new SSSolrKeywordLabel(string);
  }
  
  public static List<SSSolrKeywordLabel> getDistinct(
    final List<String> strings) throws Exception {
    
    final List<SSSolrKeywordLabel> result = new ArrayList<SSSolrKeywordLabel>();
    
    for(String string : SSStrU.distinct(strings)){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static Boolean contains(
    final List<String>         list,
    final SSSolrKeywordLabel   keywordLabel){
    
    if(SSObjU.isNull(keywordLabel, list)){
      return false;
    }
    
    return list.contains(keywordLabel.toString());
  }
  
  public static void checkKeywordLabel(
    final String tag) throws Exception {

    if(SSStrU.isEmpty(tag)){
      throw new Exception("Invalid tag (null or empty): " + tag);
    }
    
    final String tmpTag = tag.replaceAll("[/\\*\\?<>]", SSStrU.empty);
    
    if(
      SSStrU.isEmpty  (tmpTag) ||
      !Pattern.matches("^[a-zA-Z0-9_-]*$", tmpTag)){
      throw new Exception("Invalid tag: " + tmpTag);
    }
  }
  
  private SSSolrKeywordLabel(final String label) throws Exception{
    
    super(label);
    
    checkKeywordLabel(label);
  }
  
  @Override
  public Object jsonLDDesc() {
    return SSVarU.xsd + SSStrU.colon + SSStrU.valueString;
  }
}

//public static Collection<String> toString(
//    SSTagString[] tagStrings){
//    
//    List<String> result = new ArrayList<String>();
//    
//    for (SSTagString tagString : tagStrings){
//      result.add(tagString.toString());
//    }
//    
//    return result;
//  }


//  public static SSTagLabel[] toTagStringArray(Collection<SSTagLabel> toConvert) {
//    return (SSTagLabel[]) toConvert.toArray(new SSTagLabel[toConvert.size()]);
//  } 