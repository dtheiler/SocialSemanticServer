package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.datatypes.SSEvalLogE;

public class SSWorkedOnReceivedSharedEpisodeInfo {
  
  public SSUri                  episodeID        = null;
  public Integer                totalActionsDone = 0;
  public List<SSEvalLogE>       actions          = new ArrayList<>();
  public List<SSEvalActionInfo> actionDetails    = new ArrayList<>();
}
