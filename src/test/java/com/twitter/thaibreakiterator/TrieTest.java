package com.twitter.thaibreakiterator;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.Arrays;

public class TrieTest {
  @Test
  public void testEndOfWord() throws IOException {
    Iterable<String> words = Arrays.asList(
      "การกระทำ",
      "การใช่ทำ",
      "ไม่ใช่ทำ"
    );

    Trie trie = new Trie(words);

    for (String w : words) {
      char[] cs = w.toCharArray();
      trie.reset();

      for (int i = cs.length - 1; i >= 0; i--) {
        trie.next(cs[i]);

        Assert.assertTrue(trie.current.c == cs[i]);
        Assert.assertTrue(trie.isEnd() == (i == 0));
      }
    }
  }

  @Test
  public void testSubWord() throws IOException {
    Iterable<String> words = Arrays.asList(
        "การกระทำ",
        "ทำ"
    );

    Trie trie = new Trie(words);

    for (String w : words) {
      char[] cs = w.toCharArray();
      trie.reset();

      for (int i = cs.length - 1; i >= 0; i--) {
        trie.next(cs[i]);
      }

      Assert.assertTrue(trie.isEnd());
    }
  }

  @Test
  public void testInvalidQuery() throws IOException {
    Iterable<String> words = Arrays.asList(
        "การกระทำ",
        "ทำ"
    );

    Trie trie = new Trie(words);

    char[] cs = "เราไม่รู้".toCharArray();

    trie.reset();
    trie.next('ำ');
    trie.next('ท');
    Assert.assertTrue(trie.isEnd());

    for (int i = cs.length - 1; i >= 0; i--) {
      trie.next(cs[i]);
      Assert.assertTrue(trie.current == Trie.INVALID);
      Assert.assertFalse(trie.isEnd());
    }

    // from start
    trie.reset();
    for (int i = cs.length - 1; i >= 0; i--) {
      trie.next(cs[i]);
      Assert.assertTrue(trie.current == Trie.INVALID);
      Assert.assertFalse(trie.isEnd());
    }
  }
}
