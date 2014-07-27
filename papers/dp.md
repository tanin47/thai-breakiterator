Practical Thai Tokenizer with dynamic programming
===================================================

Abstract
--------------
- A dynamic-programming approach to Thai tokenization presented is simpler and easier to customize in comparison to ICU's Thai tokenization, which uses a custom state machine.
- There are several rules that can improve Thai tokenization. For example, certain characters (e.g. vowels, tone-marks) cannot start or end a word. การันตี cannot appear as the second character in a word.
- In addition, Thai word stemming is also introduced to improve the quality.
- Our approach performs as fast as the ICU's Thai tokenizer, while our quality is much better where the sentence are difficult (e.g. with words outside dictionary or slangs).

Introduction
--------------

Problems
--------------

### Certain characters are forbidden to form a word in certain positions

### Thai word stemming

Our approach
--------------


Comparison
--------------

Future work
--------------
- There are no probabilistic models involved at all.
- In many cases, a probabilistic model facilitates the ambiguity. For example, มากกกกกก
- A probabilistic model can help guess word boundary as well. For example, if it looks like a word...
- Incorporating probabilistic models while preventing too many results is crucial to future work.

Conclusion
--------------

Our dynamic-programming approach is easier to understand, better in quality, and more extensible.
