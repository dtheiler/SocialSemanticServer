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
package at.kc.tugraz.ss.category.api;

import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.SSCategoryFrequ;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import java.util.List;

public interface SSCategoryServerI{
  
  public Boolean                         categoriesPredefinedAdd              (final SSServPar parA) throws Exception;
  public List<String>                    categoriesPredefinedGet              (final SSServPar parA) throws Exception;
  
  public SSUri                           categoryUserEdit                     (final SSServPar parA) throws Exception;
  public List<SSUri>                     categoryUserEntitiesForCategoriesGet (final SSServPar parA) throws Exception;
  public List<SSUri>                     categoriesAdd                        (final SSServPar parA) throws Exception;
  public SSUri                           categoryAdd                          (final SSServPar parA) throws Exception;
  public Boolean                         categoriesRemove                     (final SSServPar parA) throws Exception;
  public Boolean                         categoriesUserRemove                 (final SSServPar parA) throws Exception;
  public List<SSCategory>                categoriesUserGet                    (final SSServPar parA) throws Exception;
  public List<SSCategoryFrequ>           categoryUserFrequsGet                (final SSServPar parA) throws Exception;
}