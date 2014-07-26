package com.twitter.thaibreakiterator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

public class ComparisonTest {

  protected ThaiBreakIterator thaiLatestBreaker;
  protected ThaiBreakIterator thaiBreaker;
  protected com.ibm.icu.text.BreakIterator icuBreaker;
  protected BreakIterator jdkBreaker;

  @Before
  public void setUp() throws IOException {
    thaiLatestBreaker = new ThaiBreakIterator();

    thaiBreaker = new ThaiBreakIterator(
      null,
      new Trie(Trie.readWords(getClass().getResourceAsStream("/thaidict_icu_52_1.txt")))
    );

    jdkBreaker = BreakIterator.getWordInstance(Locale.forLanguageTag("th"));
    icuBreaker = com.ibm.icu.text.BreakIterator.getWordInstance(Locale.forLanguageTag("th"));
  }

  protected String[] tokenize(BreakIterator breaker, String source) {
    breaker.setText(source);

    ArrayList<String> buffer = new ArrayList<>();

    int start = breaker.first();
    for (int end = breaker.next();
         end != BreakIterator.DONE;
         start = end, end = breaker.next()) {
      buffer.add(source.substring(start, end));
    }

    return buffer.toArray(new String[buffer.size()]);
  }

  protected String[] tokenize(com.ibm.icu.text.BreakIterator breaker, String source) {
    breaker.setText(source);

    ArrayList<String> buffer = new ArrayList<>();

    int start = breaker.first();
    for (int end = breaker.next();
         end != BreakIterator.DONE;
         start = end, end = breaker.next()) {
      buffer.add(source.substring(start, end));
    }

    return buffer.toArray(new String[buffer.size()]);
  }

  protected void compare(String s) {
    System.out.print("Late = ");
    printArray(tokenize(thaiLatestBreaker, s));

    System.out.print("Thai = ");
    printArray(tokenize(thaiBreaker, s));

    System.out.print("ICU  = ");
    printArray(tokenize(icuBreaker, s));

//    System.out.print("JDK  = ");
//    printArray(tokenize(jdkBreaker, s));

  }

  @Test
  public void testThaiSentenceWithAlphaNumeric() {
    String[] texts = new String[] {
      "คนอ่อนแอกว่าก็โดนกดอยู่แบบนี้แหละ",
      "ประเทศไทยมีบริการเทเลเท็กซ์โดยไม่คิดมูลค่าทางช่อง 5 มานานกว่า 4 ปีแล้ว",
      "ข้อสอบอีกประเภทที่อ่านแล้วไม่เข้าใจ คือ ถามว่า “ท่านคิดว่าข้อใดถูกต้องที่สุด” ถ้างั้นมันก็ไม่ควรมีถูกผิดดิ ก็ฉันคิดอย่างนี้นี่หว่า",
      "\"มาร์ค ซัคเคอร์เบิร์ก\"ใกล้รั้งตำแหน่งมหาเศรษฐี ที่ร่ำรวยที่สุดในโลก หลังแซงหน้าสองผู้ก่อตั้งกูเกิล http://bit.ly/1lFJ0Y1",
      "ทุกวันนี้ได้ยินคนด่ากันว่า ไร้จรรยาบรรณ กันบ่อยๆ จนเริ่มสงสัยแล้วว่าจรรยาบรรณของแต่ละคนมันมีมาตรฐานเดียวกันรึเปล่า",
      "เอ้าเร้ว โอกาสที่คุณจะได้เป็นเจ้าของรองเท้าวาเลนติโน่ในราคาเริ่มต้น 22,xxx บาท กำลังจะสิ้นสุดลง ขับรถมาพารากอนด่วน! pic.twitter.com/kMdprIiAWA",
      "ข้อสอบอีกประเภทที่อ่านแล้วไม่เข้าใจ คือ ถามว่า “ท่านคิดว่าข้อใดถูกต้องที่สุด” ถ้างั้นมันก็ไม่ควรมีถูกผิดดิ ก็ฉันคิดอย่างนี้นี่หว่า",
      "หลายคนรู้จัก Bose ในฐานะลำโพงชั้นยอด แต่อาจไม่รู้ว่าผู้ก่อตั้งเป็นชาวอินเดียที่เกิดในเพนซิลเวเนีย อเมริกาชื่อนาย อัมมาร์ โบส",
      "วันนี้ตอนเช้าพาลูกชายสองคนไปปั่นจักรยานภูเขาที่ราบ 11 มาครับ สนุกมากแต่โหดหน่อยๆ สำหรับเด็ก ค่อยๆไปได้",
      "กรมวิทย์ฯ เตือนโรคพิษสุนัขบ้า เป็นแล้วตาย 100%",
      "สามีต่างชาติยิงภรรยา 'กฤตยา ล่ำซำ' ก่อนยิงตัวตายดับคาคอนโดหรูฯ",
      "ดเวย์น จอห์นสัน อาจเป็น Shazam ในหนังซูเปอร์ฮีโร่เรื่องใหม่ของวอร์เนอร์ฯ"
    };

    for (String text: texts) {
      compare(text);
      System.out.println();
    }
  }

  private void printArray(String[] words) {
    for (int i = 0;i < words.length; i++) {
      System.out.print(words[i] + "|");
    }
    System.out.println();
  }
}
