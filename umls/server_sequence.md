@startuml sequence_server

title KWIC Index System — Server Sequence Diagram

skinparam sequenceArrowThickness 2
skinparam sequenceParticipant {
}
skinparam sequenceLifeLineBorderColor #3B4BC8
skinparam sequenceGroupBackgroundColor #F0F4FF

actor        "Client"        as CLT
participant  "Server"        as SRV
participant  "ClientHandler" as CH
participant  "KeywordSearch" as KWS
participant  "Sentence"      as SEN
participant  "Formatter"     as FMT
participant  "Logger"        as LOG

CLT  ->  SRV : writeUTF(keyword)
[->  SRV : start()
SRV  ->  CH  : new ClientHandler(socket, in, out,\nkeySearch, formatter, logger)
SRV  ->  CH  : thread.start()
CH   ->  LOG : received()
CH   ->  KWS : search(keyword)

alt results found
    KWS  -->  SEN : getWords()
    SEN  -->  KWS : List<String> words
    KWS  -->  CH  : List<Sentence> results
    CH   ->   FMT : boldFormatSentence(sentence, keyword)
    FMT  -->  CH  : highlighted result
    CH   ->   LOG : responded()
    CH   -->  CLT : writeUTF(highlighted result)
else not found
    KWS  -->  CH  : empty results
    CH   -->  CLT : writeUTF("[keyword] not found.")
end

@enduml