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
package at.kc.tugraz.ss.datatypes.datatypes;

import at.kc.tugraz.socialserver.utils.*;
import java.util.*;
import java.util.regex.Pattern;

public class SSTagLabel extends SSEntityA{

  public static String toStr(final SSTagLabel tagLabel){
    return SSStrU.toString(tagLabel);
  }

  public static SSTagLabel get(final String string) throws Exception{
    return new SSTagLabel(string);
  }
  
  public static List<SSTagLabel> getDistinct(
    final List<String> strings) throws Exception {
    
    final List<SSTagLabel> result = new ArrayList<SSTagLabel>();
    
    for(String string : SSStrU.distinct(strings)){
      result.add(get(string));
    }
    
    return result;
  }
  
  public static Boolean contains(
    final List<String> list,
    final SSTagLabel   tagLabel){
    
    if(SSObjU.isNull(tagLabel, list)){
      return false;
    }
    
    return list.contains(tagLabel.toString());
  }
  
  public static void checkTagLabel(
    final String tagLabel) throws Exception {

    if(SSStrU.isEmpty(tagLabel)){
      throw new Exception("Invalid tag (null or empty): " + tagLabel);
    }
    
    final String tmpTagLabel = tagLabel.replaceAll("[/\\*\\?<>]", SSStrU.empty);
    
    if(
      SSStrU.isEmpty  (tmpTagLabel) ||
      !Pattern.matches("^[a-zA-Z0-9_-]*$", tmpTagLabel)){
      throw new Exception("Invalid tag: " + tmpTagLabel);
    }
  }
  
  private SSTagLabel(final String label) throws Exception{
    
    super(label);
    
    checkTagLabel(label);
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