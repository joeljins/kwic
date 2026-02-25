@startuml

'--- Main ---
class Main {
    + main(args: String[]): void
}

class Sentence {
    - words: List<String>
    - lineNumber: int
    + Sentence(text: String, lineNumber: int)
    + getWords(): List<String>
    + getLineNumber(): int
    + joinSentence(): String
}

interface InputProcessor {
    + processInput(): List<Sentence>
}

class TextFileInput{
    - String filePath;
    + processInput(): List<Sentence>
    + TextFileInput(filePath: String)
}

InputProcessor <|-- TextFileInput

interface Shift{
    + shiftSentences(sentences: List<Sentence>): List<Sentence>
}

class CircularShift{
    + shiftSentences(sentences: List<Sentence>): List<Sentence>
}

interface Sorting{
    + sort(List<Sentence>): List<Sentence>
}

class AlphabetSort{
    + sort(List<Sentence>): List<Sentence>
}

Sorting <|-- AlphabetSort

class IndexGenerator {
    - keywords: Map<String, Set<Integer>>
    + IndexGenerator(sentences: List<Sentence>, filter: WordFilter)
    + getIndexMap(): Map<String, Set<Integer>>
    + getAllWordCount(): result: List<String[]>
    + getWordLineNums(word: String): Set<Integer>
}

interface WordFilter {
    + accept(word: String): boolean
}

IndexGenerator o-- WordFilter

class KeywordSearch {
    - sentences: List<Sentence>
    - lookUpMap: Map<Integer, Sentence>
    - indexGen: IndexGenerator
    + search(word: String): List<Sentence>
    + getSentenceByLineNum(lineNum: int): Sentence
}

IndexGenerator <-- KeywordSearch

class Formatter {
    + formatSentence(s: Sentence): String
    + formatSentences(sentences: List<Sentence>): String
    + formatIndex(index: List<String[]>): String
    + formatKWIC(sentences: List<Sentence>): String
}

interface Output{
    + write(sentences: List<Sentence>): void
}

class ConsoleOutput{
    + write(sentences: List<Sentence>): void
}

Output <|-- ConsoleOutput

Main --> InputProcessor
Main --> Shift
Main --> Sorting
Main -- IndexGenerator
Main --> KeywordSearch
Main --> Formatter
Main --> Output

InputProcessor ..> Sentence : creates
Shift ..> Sentence : generates shifts
Sorting ..> Sentence : compares
IndexGenerator ..> Sentence : indexes
KeywordSearch ..> Sentence : returns
Formatter ..> Sentence : stringifies
Output ..> Sentence : prints

@enduml

## 5. Design for Future Extensibility

### 1. Input Format Changes (e.g., .csv, .json)
* **Design Decision:** Implementation of the `InputProcessor` interface.

The `Main` class depends on the `InputProcessor` abstraction rather than the `TextFileInput` implementation. To support a new format like CSV, one would simply create a `CSVInputProcessor` class that implements the interface. No changes would be required in the downstream processing logic.

### 2. Index Generation Policy (e.g., Stop Words)
* **Design Decision:** Dependency Injection of the `WordFilter` interface into the `IndexGenerator`.

The `IndexGenerator` aggregates a `WordFilter`. Currently, it may use a default "accept-all" filter. In the future, a `StopWordsFilter` can be passed into the constructor. The generator will use the `accept()` method of the filter without needing to know the underlying logic of which words are being excluded.

### 3. Alphabetizing Policy Changes (e.g., Case-Sensitivity)
* **Design Decision:** Use of the `Sorting` interface.

Sorting logic is isolated from the `Shift` and `Main` classes. By implementing new versions of the `Sorting` interface (e.g., `DescendingSort` or `CaseInsensitiveSort`), the user can toggle sorting behaviors at runtime. This prevents the "hard-coding" of sorting logic within the data processing pipeline.


### 4. Output Method Changes (e.g., File, Web, HTML)
* **Design Decision:** Separation of `Formatter` (data structure) and `Output` (data destination).

By implementing the `Output` interface, we can swap `ConsoleOutput` for `FileOutput` or `WebOutput` seamlessly.