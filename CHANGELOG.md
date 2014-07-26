TODO
----------------
* ICU's BreakIterator performs better in some cases
  * icu = |โหด|หน่อยๆ|, our = |โหด|หน่อ|ยๆ| --- Because we don't allow a single-character word
  * icu = |เพน|ซิลเว|เนีย|, our = |เพ|นซิลเว|เนีย|
  * icu = |เพน|ซิลเว|เนีย|, our = |เพ|นซิลเว|เนีย|
  * icu = |พา|รา|กอน|, our = |พา|ราก|อน|
  * icu = |บ่อยๆ|, our = |บ่อ|ยๆ|
  * icu = |วิทย์ฯ|, our = |วิ|ทย์ฯ|
  * Seeing the source code, I'm pretty sure ICU handles ๆ ฯ specially
* Implement ThaiBreakIterator.next(int n) and ThaiBreakIterator.following(int offset)

[Stemmming]

Some of the examples above can be resolved with stemming. For example:

หน่อยๆๆๆๆๆๆๆๆ --> หน่อย
วิทย์ฯ --> วิทย์

It can cover slangs beautifully as well. Here are some examples:

ยาวววววววววว --> ยาว
มากกกกกกกกกก --> มาก

How do we handle Stemming within our algorithm?


25 July 2014
----------------

* Incorporate RuleBasedBreakIterator
* Change back to use Array. ArrayList seems to be a premature optimization
* Test Framework (e.g. between ThaiBreakIterator and ICU's BreakIterator


14 July 2014
--------------

* ThaiBreakIterator implementation is finished


