@startuml class_server

title KWIC Index System — Server Class Diagram

skinparam classAttributeIconSize 0
skinparam class {
}
skinparam note {
    BackgroundColor #FFFDE7
    BorderColor #F9A825
}
skinparam stereotypeCBackgroundColor #A5B4FC
skinparam stereotypeIBackgroundColor #C7D2FE

' ================================================
' ENTRY POINT
' ================================================
class Controller <<Context>> {
    + {static} main(args: String[]): void
}

note right of Controller
  When args[0] == "server":
  Creates Logger, then Server,
  then calls server.start()
end note

' ================================================
' SERVER & NETWORKING
' ================================================
class Server {
    - serverSocket: ServerSocket
    - keySearch: KeywordSearch
    - formatter: Formatter
    - logger: Logger
    + Server(port: int, keySearch: KeywordSearch,\n  formatter: Formatter, logger: Logger)
    + start(): void
}

note right of Server
  <b>start() loop:</b>
  Blocks on serverSocket.accept().
  On new connection: wraps socket in
  DataInputStream / DataOutputStream,
  creates ClientHandler thread, starts it.
end note

class ClientHandler {
    - clientSocket: Socket
    - dataIn: DataInputStream
    - dataOut: DataOutputStream
    - keySearch: KeywordSearch
    - formatter: Formatter
    - logger: Logger
    + ClientHandler(clientSocket: Socket,\n  dataIn: DataInputStream,\n  dataOut: DataOutputStream,\n  keySearch: KeywordSearch,\n  formatter: Formatter,\n  logger: Logger)
    + run(): void
}

note bottom of ClientHandler
  <b>run() flow:</b>
  1. dataOut.writeUTF(prompt)
  2. Loop while true:
     a. logger.received()
     b. keyword = dataIn.readUTF()
     c. if keyword == "Exit": break
     d. results = keySearch.search(keyword)
     e. dataOut.writeUTF(formatted results)
     f. logger.responded()
  3. Close socket
end note

class Logger {
    - filePath: String
    - numRequests: AtomicInteger
    - successfulResponses: AtomicInteger
    + Logger(filePath: String)
    + received(): void
    + responded(): void
    - writeLogFile(): void
}

note right of Logger
  Thread-safe via AtomicInteger.
  writeLogFile() is synchronized.
  Overwrites log.txt on every call.
  Resets on new Server start.
end note

' ================================================
' CONFIGURATION
' ================================================
class OptionReader <<utility>> {
    - {static} userOptions: HashMap<String, String>
    - {static} kwicObjLoader: KWICObjectLoader
    + {static} readOptions(filePath: String): void
    + {static} getString(key: String): String
    + {static} getObjectFromKey(key: String): Object
    + {static} getObjectFromStr(objStr: String): Object
}

class KWICObjectLoader {
    + loadObject(className: String): Object
}

note right of KWICObjectLoader
  Uses reflection to instantiate
  Input / Output classes by name
  from .properties config file.
end note

' ================================================
' DATA MODEL
' ================================================
class Sentence {
    - words: List<String>
    - lineNumber: int
    + Sentence(words: List<String>, lineNumber: int)
    + getWords(): List<String>
    + getLineNumber(): int
    + joinSentence(): String
}

' ================================================
' STRATEGY: INPUT
' ================================================
interface Input <<Strategy>> {
    + processInput(): List<Sentence>
    + setFilePath(path: String): void
}

class TextInput <<ConcreteStrategy>> {
    - filePath: String
    + processInput(): List<Sentence>
    + setFilePath(path: String): void
}

class CsvInput <<ConcreteStrategy>> {
    - filePath: String
    + processInput(): List<Sentence>
    + setFilePath(path: String): void
}

Input <|.. TextInput
Input <|.. CsvInput

note "<b>Strategy Pattern (Input)</b>\nEncapsulates different file\nparsing logic (Text vs CSV)." as N_Input
Input .. N_Input

' ================================================
' STRATEGY: SHIFTING
' ================================================
interface Shift <<Strategy>> {
    + shiftSentences(sentences: List<Sentence>): List<Sentence>
}

class CircularShift <<ConcreteStrategy>> {
    + shiftSentences(sentences: List<Sentence>): List<Sentence>
}

Shift <|.. CircularShift

note "<b>Strategy Pattern (Shifting)</b>\nAllows switching shifting\nalgorithms." as N_Shift
Shift .. N_Shift

' ================================================
' STRATEGY: SORTING
' ================================================
interface Sorting <<Strategy>> {
    + sortSentences(sentences: List<Sentence>, isAscending: boolean): void
    + sortIndex(keymap: Map<String, Set<Integer>>, isAscending: boolean): Map<String, Set<Integer>>
}

class AlphabetSort <<ConcreteStrategy>> {
    + sortSentences(sentences: List<Sentence>, isAscending: boolean): void
    + sort(sentences: List<Sentence>): void
}

Sorting <|.. AlphabetSort

note "<b>Strategy Pattern (Sorting)</b>\nAllows switching sorting\nalgorithms." as N_Sort
Sorting .. N_Sort

' ================================================
' STRATEGY: WORD FILTER
' ================================================
interface WordFilter <<Strategy>> {
    + accept(word: String): boolean
}

class StopWordsFilter <<ConcreteStrategy>> {
    - trivialWords: HashSet<String>
    + StopWordsFilter(trivialWords: HashSet<String>)
    + accept(word: String): boolean
}

WordFilter <|.. StopWordsFilter

note "<b>Strategy Pattern (WordFilter)</b>\nAllows plugging in different\nword filtering policies." as N_Filter
WordFilter .. N_Filter

' ================================================
' INDEXING & SEARCH
' ================================================
class IndexGenerator <<Context>> {
    - keywords: Map<String, Set<Integer>>
    + IndexGenerator(sentences: List<Sentence>, filter: WordFilter)
    + getIndexMap(): Map<String, Set<Integer>>
    + setIndexMap(map: Map<String, Set<Integer>>): void
    + getAllWordCount(): List<String[]>
    + getWordLineNums(word: String): Set<Integer>
}

class KeywordSearch {
    - sentences: List<Sentence>
    - lookUpMap: Map<Integer, Sentence>
    - indexGen: IndexGenerator
    + KeywordSearch(sentences: List<Sentence>, indexGen: IndexGenerator)
    + search(word: String): List<Sentence>
    + getSentenceByLineNum(lineNum: int): Sentence
}

IndexGenerator o-- WordFilter       : uses >
KeywordSearch  --> IndexGenerator   : queries >

' ================================================
' FORMATTING & OUTPUT
' ================================================
class Formatter {
    + formatSentence(s: Sentence): String
    + formatSentences(sentences: List<Sentence>): String
    + formatIndex(index: List<String[]>): String
    + formatKWIC(sentences: List<Sentence>): String
    + boldFormatSentence(s: Sentence, keyword: String): String
}

interface Output <<Strategy>> {
    + write(content: String): void
    + setFilePath(filePath: String): void
}

class ConsoleOutput <<ConcreteStrategy>> {
    + write(content: String): void
    + setFilePath(filePath: String): void
}

class TextOutput <<ConcreteStrategy>> {
    + write(content: String): void
    + setFilePath(filePath: String): void
}

Output <|.. ConsoleOutput
Output <|.. TextOutput

note "<b>Strategy Pattern (Output)</b>\nDecouples logic from destination\n(Console vs File)." as N_Output
Output .. N_Output

' ================================================
' RELATIONSHIPS
' ================================================

' Controller orchestration
Controller      -->  OptionReader    : readOptions() / getString() >
Controller      -->  Input           : processInput() >
Controller      -->  Shift           : shiftSentences() >
Controller      -->  Sorting         : sortSentences() / sortIndex() >
Controller      -->  IndexGenerator  : getAllWordCount() >
Controller      -->  KeywordSearch   : search(keyword) >
Controller      -->  Formatter       : formatKWIC() / formatIndex() >
Controller      -->  Output          : write() >
Controller      -->  Logger          : creates >
Controller      -->  Server          : creates & starts >

' Config loading
OptionReader    -->  KWICObjectLoader : loadObject(className) >

' Server internals
Server          "1" --> "1"    Logger        : has >
Server          "1" --> "1"    KeywordSearch : has >
Server          "1" --> "1"    Formatter     : has >
Server               ..>       ClientHandler : <<creates>> spawns Thread >

' ClientHandler usage
ClientHandler   "many" --> "1" KeywordSearch : search() >
ClientHandler   "many" --> "1" Formatter     : boldFormatSentence() >
ClientHandler   "many" --> "1" Logger        : received() / responded() >

Thread <|-- ClientHandler

' Sentence usage
Input           ..>  Sentence : creates >
Shift           ..>  Sentence : generates shifts >
Sorting         ..>  Sentence : compares >
IndexGenerator  ..>  Sentence : indexes >
KeywordSearch   ..>  Sentence : returns >
Formatter       ..>  Sentence : stringifies >

@enduml
