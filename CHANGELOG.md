TODO
----------------
* ICU's BreakIterator performs better in some cases
  * icu = |เพน|ซิลเว|เนีย|, our = |เพ|นซิลเว|เนีย| --- probably related to how we score
  * icu = |พา|รา|กอน|, our = |พา|ราก|อน| --- probably related to how we score
  * icu = |โหด|หน่อยๆ|, our = |โหด|หน่อ|ยๆ| -- can be solved with Stemming
  * icu = |บ่อยๆ|, our = |บ่อ|ยๆ| -- can be solved with Stemming
  * icu = |วิทย์ฯ|, our = |วิ|ทย์ฯ| -- can be solved with Stemming
  * Seeing the source code, I'm pretty sure ICU handles ๆ ฯ specially
* Implement ThaiBreakIterator.next(int n) and ThaiBreakIterator.following(int offset)

### Stemmming

Some of the examples above can be resolved with stemming. For example:

* หน่อยๆๆๆๆๆๆๆๆ --> หน่อย
* วิทย์ฯ --> วิทย์

It can cover slangs beautifully as well. Here are some examples:

* ยาวววววววววว --> ยาว
* มากกกกกกกกกก --> มาก
* กรรรรรม --> กรรม

For efficiency, we should aim to handle stemming into our Trie.

ๆ and ฯ don't advance the state; It stays at the initial state.

ก should go back to itself. But how do we deal with กก? we allow last character to repeat itself in the trie.

Actually, we allow any character to repeat itself. The sentences that might cause problem:

* มากกว่า ---> |มากก|ว่า|

We need to give less score for the need of stemming, but then มากกกกก will be come |มาก|กก|กก|กก|.
This cannot be solved without semantics.


25 July 2014
----------------

* Incorporate RuleBasedBreakIterator
* Change back to use Array. ArrayList seems to be a premature optimization
* Test Framework (e.g. between ThaiBreakIterator and ICU's BreakIterator


14 July 2014
--------------

* ThaiBreakIterator implementation is finished


