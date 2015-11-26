package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;

public class SSEpisodeCreationInfo {

  public SSUri   episodeID    = null;
  public SSLabel episodeLabel = null;
  public Long    timestamp    = null;
  
  public SSEpisodeCreationInfo(
    final SSUri   episodeID, 
    final SSLabel episodeLabel, 
    final Long    timestamp){
    
    this.episodeID    = episodeID;
    this.episodeLabel = episodeLabel;
    this.timestamp    = timestamp;
  }
}