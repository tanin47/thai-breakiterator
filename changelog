TODO
----------------
* ICU's BreakIterator performs better in some cases
  * icu = |โหด|หน่อยๆ|, our = |โหด|หน่อ|ยๆ|
  * icu = |เพน|ซิลเว|เนีย|, our = |เพ|นซิลเว|เนีย|
  * icu = |เพน|ซิลเว|เนีย|, our = |เพ|นซิลเว|เนีย|
  * icu = |พา|รา|กอน|, our = |พา|ราก|อน|
  * icu = |บ่อยๆ|, our = |บ่อ|ยๆ|
  * icu = |วิทย์ฯ|, our = |วิ|ทย์ฯ|
  * Seeing the source code, I'm pretty sure ICU handles ๆ ฯ specially
* Implement ThaiBreakIterator.next(int n) and ThaiBreakIterator.following(int offset)


25 July 2014
----------------

Done:
* Incorporate RuleBasedBreakIterator
* Change back to use Array. ArrayList seems to be a premature optimization
* Test Framework (e.g. between ThaiBreakIterator and ICU's BreakIterator


14 July 2014
--------------

* ThaiBreakIterator implementation is finished


