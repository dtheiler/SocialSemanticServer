package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.SSLabel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSUserInfo {
  
  public SSLabel                                            label                        = null;
  public List<SSImportInfo>                                 importInfos                  = new ArrayList<>();
  public Map<String, SSWorkedOnOwnBitInfo>                  workedOnOwnBitInfos          = new HashMap<>();
  public Map<String, SSWorkedOnOwnEpisodeInfo>              workedOnOwnEpisodeInfos      = new HashMap<>();
  public List<SSEpisodeCreationInfo>                        createdEpisodeInfos          = new ArrayList<>();
  public List<SSEpisodeShareInfo>                           sharedEpisodeInfos           = new ArrayList<>();
  public Map<String, SSWorkedOnReceivedSharedEpisodeInfo>   workedOnReceivedEpisodeInfos = new HashMap<>();
  public Map<String, SSWorkedOnReceivedSharedBitInfo>       workedOnReceivedBitInfos     = new HashMap<>();
}
