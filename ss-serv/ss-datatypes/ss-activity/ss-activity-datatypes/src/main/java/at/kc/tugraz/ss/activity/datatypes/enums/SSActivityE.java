package at.kc.tugraz.ss.activity.datatypes.enums;

import at.kc.tugraz.socialserver.utils.SSStrU;

public enum SSActivityE{
  share;

  public static String toStr(final SSActivityE type){
    return SSStrU.toString(type);
  }
}