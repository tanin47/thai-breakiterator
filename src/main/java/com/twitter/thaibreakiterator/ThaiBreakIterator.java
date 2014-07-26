package com.twitter.thaibreakiterator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.Set;

public class ThaiBreakIterator extends BreakIterator {

  protected RuleBasedBreakIterator baseBreaker;

  // The trie is built on the list of reversed words
  //   It's faster to be used by our dynamic programming approach
  protected Trie trie;

  // The current index of startingIndices
  protected int current = 0;

  // startingIndex of each word, inclusively
  //   startingIndices.get(0) is always 0
  protected int[] startingIndices = new int[0];

  public static int MINUS_INFINITY = -10000;
  public static String ForbiddenEndCharacters = "ไ แ เ ัโใ";
  public static String ForbiddenStartCharacters = "า ำ ์ ื ิ ้ ็  ัํ ี ๊ ึ ุ ู ่ ๋";
  public static Set<Character> ForbiddenEnds;
  public static Set<Character> ForbiddenStarts;

  static {
    ForbiddenEnds = getCharSet(ForbiddenEndCharacters);
    ForbiddenStarts = getCharSet(ForbiddenStartCharacters);
  }

  protected static Set<Character> getCharSet(String s) {
    HashSet<Character> set = new HashSet<>();
    char[] cs = s.toCharArray();

    for (char c : cs) {
      if (c != ' ') {
        set.add(c);
      }
    }

    return set;
  }

  public ThaiBreakIterator() throws IOException, MissingResourceException {
    this(null, null);
  }

  public ThaiBreakIterator(RuleBasedBreakIterator baseBreaker, Trie trie)
      throws IOException, MissingResourceException {
    if (baseBreaker == null) {
      baseBreaker = new RuleBasedBreakIterator(
          new BufferedInputStream(getClass().getResourceAsStream("/sun/text/resources/WordBreakIteratorData_th")));
    }

    if (trie == null) {
      trie = new Trie();
    }

    this.baseBreaker = baseBreaker;
    this.trie = trie;
  }

  @Override
  public int first() {
    tokenize(baseBreaker.first(), baseBreaker.next() - 1);
    return baseBreaker.first();
  }

  @Override
  public int last() {
    startingIndices = new int[0];
    current = 0;
    return baseBreaker.last();
  }

  @Override
  public int next(int n) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int next() {
    if (0 <= current && current < (startingIndices.length - 1)) {
      current++;
    } else {
      current = 0;
      int next = baseBreaker.next();
      int nextNext = baseBreaker.next() - 1;
      baseBreaker.next(-1);

      if (next == getText().getEndIndex()) { return last(); } // the text's past-the-end offset
      if (next == BreakIterator.DONE) { return BreakIterator.DONE; } // Past the past-the-end offset

      tokenize(next, nextNext);
    }

    return startingIndices[current];
  }

  @Override
  public int previous() {
    if (current > 0) {
      current--;
    } else {
      int baseCurrent = baseBreaker.current();
      tokenize(baseBreaker.previous(), baseCurrent);
      current = startingIndices.length - 1;
    }

    return startingIndices[current];
  }

  @Override
  public int following(int offset) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int current() {
    return startingIndices[current];
  }

  @Override
  public CharacterIterator getText() {
    return baseBreaker.getText();
  }

  @Override
  public void setText(CharacterIterator newText) {
    baseBreaker.setText(newText);
  }

  public void tokenize(int start, int end) {
    int size = end - start + 1;

    int[] scores = new int[size];
    int[] startings = new int[size];
    char[] chars = new char[size];

    CharacterIterator text = getText();
    int savedPosition = text.getIndex(); // Need to save position to not to mess with baseBreaker

    // Here is the dynamic programming approach
    // for finding the optimal tokenization that:
    //   (1) matches words in dictionary with many characters as possible
    //   (2) does not violate the forbidden rules (See: isForbidden())
    //
    // Let a0..an be a list of characters
    // _scores[i] = max { for a0 .. aj where j < i
    //                      _score[j-1] + theScoreOf(aj..ai) }
    //
    // if there's a tie, choose the one with maximum i - j
    //
    for(int i = 0; i < size; i++) {
      scores[i] = MINUS_INFINITY;
      startings[i] = -1;
      text.setIndex(start + i);
      trie.reset();

      for(int j = i; j >= 0; j--) {
        int previousScore = j == 0 ? 0 : scores[j - 1];

        chars[j] = text.current();
        trie.next(text.current());
        text.previous();

        int thisScore = previousScore + assign(chars, j, i);
        if (thisScore >= scores[i]) { // prefer longer sequence, thus, >=
          scores[i] = thisScore;
          startings[i] = j;
        }
      }
    }

    buildStartingIndices(startings, start, size);

    text.setIndex(savedPosition);
  }

  protected void buildStartingIndices(int[] startings, int start, int size) {
    int index = startings[size - 1];
    ArrayList<Integer> tmp = new ArrayList<Integer>();

    while (index > 0) {
      tmp.add(index);
      index = startings[index - 1];
    }

    tmp.add(0);

    int indicesSize = tmp.size();
    startingIndices = new int[indicesSize];

    for (int i = indicesSize - 1; i >= 0; i--) {
      startingIndices[indicesSize - i - 1] = tmp.get(i) + start;
    }
  }

  // isForbidden() determines if the substring cannot possibly be a word.
  //   Some patterns in Thai language are just not possible to form a word.
  //   It would be idiotic to allow these patterns.
  protected boolean isForbidden(char[] chars, int start, int end) {
    return (end - start + 1) <= 1
           || chars[start + 1] == '์'
           || ForbiddenEnds.contains(chars[end])
           || ForbiddenStarts.contains(chars[start]);
  }

  protected int assign(char[] chars, int start, int end) {
    if (isForbidden(chars, start, end)) {
      return MINUS_INFINITY;
    } else {
      return trie.isEnd() ? end - start + 1 : 0;
    }
  }
}