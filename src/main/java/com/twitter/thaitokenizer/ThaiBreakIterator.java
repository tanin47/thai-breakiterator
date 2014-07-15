package com.twitter.thaitokenizer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.Set;

public class ThaiBreakIterator extends BreakIterator {

  protected RuleBasedBreakIterator ruleBasedBreakIterator;

  // The trie is built on the list of reversed words
  //   It's faster to be used by our dynamic programming approach
  protected Trie trie;

  protected CharacterIterator currentText;

  // The size of the text
  protected int size;

  // The current index of startingIndices
  protected int current;

  // startingIndex of each word, inclusively
  //   startingIndices.get(0) is always 0
  protected ArrayList<Integer> startingIndices = new ArrayList<>();

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

  public ThaiBreakIterator(RuleBasedBreakIterator ruleBasedBreakIterator, Trie trie)
      throws IOException, MissingResourceException {
    if (ruleBasedBreakIterator == null) {
      ruleBasedBreakIterator = new RuleBasedBreakIterator(
          new BufferedInputStream(getClass().getResourceAsStream("/sun/text/resources/WordBreakIteratorData_th")));
    }

    if (trie == null) {
      trie = new Trie();
    }

    this.ruleBasedBreakIterator = ruleBasedBreakIterator;
    this.trie = trie;
  }

  @Override
  public int first() {
    current = 0;
    return 0;
  }

  @Override
  public int last() {
    current = startingIndices.size() - 1;
    return startingIndices.get(current);
  }

  @Override
  public int next(int n) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int next() {
    if (current == startingIndices.size() - 1) {
      return BreakIterator.DONE;
    } else {
      current++;
      return startingIndices.get(current);
    }
  }

  @Override
  public int previous() {
    if (current == 0) {
      return BreakIterator.DONE;
    } else {
      current--;
      return startingIndices.get(current);
    }
  }

  @Override
  public int following(int offset) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int current() {
    return startingIndices.get(current);
  }

  @Override
  public CharacterIterator getText() {
    return currentText;
  }

  @Override
  public void setText(CharacterIterator newText) {
    currentText = newText;
    tokenize();
  }

  // Avoid re-instantiate these variables everytime tokenize() is invoked.
  private ArrayList<Integer> _scores = new ArrayList<>();
  private ArrayList<Integer> _startings = new ArrayList<>();
  private ArrayList<Character> _characters = new ArrayList<>();

  public void tokenize() {
    size = currentText.getEndIndex();

    // Here is the dynamic programming approach
    // for finding the optimal tokenization that:
    //   (1) matches words in dictionary with best effort
    //   (2) does not break the forbidden rules (See: isForbidden())
    //
    // Let a0..an be a list of characters
    // _scores[i] = max { for a0 .. aj where j < i
    //                      _score[j-1] + theScoreOf(aj..ai) }
    //
    // if there's a tie, choose the one with maximum i - j
    //
    for(int i = 0; i < size; i++) {
      _scores.set(i, Integer.MIN_VALUE);
      _startings.set(i, -1);
      currentText.setIndex(i);
      trie.reset();

      for(int j = i; j >= 0; j--) {
        int previousScore = j == 0 ? 0 : _scores.get(j - 1);

        _characters.set(j, currentText.current());
        currentText.previous();

        int thisScore = previousScore + assign(j, i);
        if (thisScore >= _scores.get(i)) { // prefer longer sequence, thus, >=
          _scores.set(i, thisScore);
          _startings.set(i, j);
        }
      }
    }

    buildStartingIndices();
  }

  protected void buildStartingIndices() {
    int index = size;
    startingIndices.clear();

    while (index > 0) {
      startingIndices.add(index);
      index = _startings.get(index - 1);
    }

    startingIndices.add(0);

    reverse(startingIndices);
  }

  protected static void reverse(ArrayList<Integer> list) {
    int n = list.size();
    int half = n / 2;
    for (int i = 0; i < half; i++) {
      int tailIndex = n - i - 1;
      int temp = list.get(i);
      list.set(i, list.get(tailIndex));
      list.set(tailIndex, temp);
    }
  }

  // isForbidden() determines if the substring cannot possibly be a word.
  //   Some patterns in Thai language are just not possible to form a word.
  //   It would be idiotic to allow these patterns.
  protected boolean isForbidden(int start, int end) {
    return (end - start + 1) <= 1
           || _characters.get(start + 1) == '์'
           || ForbiddenEnds.contains(_characters.get(end))
           || ForbiddenStarts.contains(_characters.get(start));
  }

  protected int assign(int start, int end) {
    if (isForbidden(start, end)) {
      return MINUS_INFINITY;
    } else {
      trie.next(_characters.get(start));
      return trie.isEnd() ? end - start + 1 : 0;
    }
  }
}