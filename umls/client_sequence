@startuml sequence_client

title KWIC Index System — Client Sequence Diagram

skinparam sequenceArrowThickness 2
skinparam sequenceParticipant {
}
skinparam sequenceLifeLineBorderColor #3B4BC8
skinparam sequenceGroupBackgroundColor #F0F4FF

actor        User
participant  "ClientMain"  as CM
participant  "Client"      as CLI
participant  "Server"      as SRV

User -> CM  : main()
CM   -> CLI : new Client("localhost", 8080, 30000)
CM   -> CLI : run()
User -> CLI : enter keyword
CLI  -> SRV : writeUTF(keyword)

alt Server responds
    SRV --> CLI : readUTF(response)
    CLI --> User : println(response)
else No response within 30s
    CLI --> User : "The KWIC server is not responding."
end

@enduml
