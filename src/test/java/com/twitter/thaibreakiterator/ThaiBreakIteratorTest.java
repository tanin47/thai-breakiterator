package com.twitter.thaibreakiterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;

public class ThaiBreakIteratorTest {

  protected ThaiBreakIterator breaker;

  @Before
  public void setUp() throws IOException {
    breaker = new ThaiBreakIterator();
  }

  protected String[] tokenize(String source) {
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

  @Test
  public void testThaiSentence() {
    Assert.assertArrayEquals(
      new String[] { "อิ", "ยาง", "มัด", "ผม", "อิ", "ยาง", "มัด", "ผม", "อิ" },
      tokenize("อิยางมัดผมอิยางมัดผมอิ")
    );

    Assert.assertArrayEquals(
      new String[] { "อิ", "พี่", "ลู่", "อิ", "พี่", "ลู่" },
      tokenize("อิพี่ลู่อิพี่ลู่")
    );
  }

  @Test
  public void testThaiSentenceWithAlphaNumeric() {
    Assert.assertArrayEquals(
      new String[] { "ประเทศไทย", "มี", "บริการ", "เท", "เล", "เท็กซ์", "โดย", "ไม่", "คิด", "มูลค่า", "ทาง", "ช่อง", " ", "5", " ", "มา", "นาน", "กว่า", " ", "4", " ", "ปี", "แล้ว" },
      tokenize("ประเทศไทยมีบริการเทเลเท็กซ์โดยไม่คิดมูลค่าทางช่อง 5 มานานกว่า 4 ปีแล้ว")
    );

    Assert.assertArrayEquals(
      new String[] { "จอง", "จู", "ยอน", " ", "นางเอก", "ของ", "อู", "บิน", "ใน", "เรื่อง", " ", "Twenty", " ", "เรื่อง", "นี้", "รอ", "ดู", "เป็น", "พิเศษ", " ", "บอก", "เลย", " ", "อิอิ" },
      tokenize("จองจูยอน นางเอกของอูบินในเรื่อง Twenty เรื่องนี้รอดูเป็นพิเศษ บอกเลย อิอิ")
    );
  }
}
