package com.twitter.thaibreakiterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

public class Trie {

  protected Node root = new Node(' ');
  protected Node current = root;

  protected static Node INVALID = new Node(' ');

  public Trie() throws IOException {
    this(null);
  }

  public Trie(Iterable<String> words) throws IOException {
    if (words == null) {
      words = readWords(getClass().getResourceAsStream("/thaidict_latest.txt"));
    }

    buildTrie(words);
  }

  public static Iterable<String> readWords(InputStream in) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    HashSet<String> tmpWords = new HashSet<>();

    String line;
    while ((line = reader.readLine()) != null) {
      tmpWords.add(line.trim());
    }

    return tmpWords;
  }

  protected void buildTrie(Iterable<String> words) {
    for (String s : words) {
      char[] c = s.toCharArray();

      Node node = root;
      for (int i = c.length - 1; i >= 0; i--) {
        node = node.addChild(c[i]);

        if (i == 0) {
          node.isEnd = true;
        }
      }
    }
  }

  public void reset() {
    current = root;
  }

  public boolean isEnd() {
    return current.isEnd;
  }

  public void next(char c) {
    current = current.children.get(c);

    if (current == null) {
      current = INVALID;
    }
  }

  public static class Node {
    public char c;
    public boolean isEnd = false;
    public HashMap<Character, Node> children = new HashMap<>();

    public Node(char c) {
      this.c = c;
    }

    public Node addChild(char n) {
      Node child = children.get(n);

      if (child == null) {
        child = new Node(n);
        children.put(n, child);
      }

      return child;
    }
  }
}
