package com.twitter.thaitokenizer;

public class OurBreakIterator {
  static long getLong(byte[] buf, int offset) {
    long num = buf[offset]&0xFF;
    for (int i = 1; i < 8; i++) {
      num = num<<8 | (buf[offset+i]&0xFF);
    }
    return num;
  }

  static int getInt(byte[] buf, int offset) {
    int num = buf[offset]&0xFF;
    for (int i = 1; i < 4; i++) {
      num = num<<8 | (buf[offset+i]&0xFF);
    }
    return num;
  }

  static short getShort(byte[] buf, int offset) {
    short num = (short)(buf[offset]&0xFF);
    num = (short)(num<<8 | (buf[offset+1]&0xFF));
    return num;
  }
}

