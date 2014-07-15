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
  public void testSentences() {
    Assert.assertArrayEquals(
      new String[] { "ประเทศไทย", "มี", "บริการ", "เท", "เล", "เท็กซ์", "โดย", "ไม่", "คิด", "มูลค่า", "ทาง", "ช่อง" },
      tokenize("ประเทศไทยมีบริการเทเลเท็กซ์โดยไม่คิดมูลค่าทางช่อง")
    );

    Assert.assertArrayEquals(
      new String[] { "อิ", "ยาง", "มัด", "ผม", "อิ", "ยาง", "มัด", "ผม", "อิ" },
      tokenize("อิยางมัดผมอิยางมัดผมอิ")
    );

    Assert.assertArrayEquals(
      new String[] { "อิ", "พี่", "ลู่", "อิ", "พี่", "ลู่" },
      tokenize("อิพี่ลู่อิพี่ลู่")
    );
  }
}
