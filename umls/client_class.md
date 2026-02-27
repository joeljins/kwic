@startuml class_client

title KWIC Index System — Client Class Diagram

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
class ClientMain {
    + {static} main(args: String[]): void
}

note right of ClientMain
  Instantiates Client with:
  host     = "localhost"
  port     = 8080
  timeout  = 30,000 ms
  Then calls client.run()
end note

' ================================================
' CLIENT
' ================================================
class Client {
    - host: String
    - port: int
    - timeout: int
    + Client(host: String, port: int, timeout: int)
    + run(): void
}

' ================================================
' JAVA STANDARD LIBRARY DEPENDENCIES
' ================================================
class Socket <<java.net>> {
    + setSoTimeout(timeout: int): void
    + getInputStream(): InputStream
    + getOutputStream(): OutputStream
}

class DataInputStream <<java.io>> {
    + readUTF(): String
}

class DataOutputStream <<java.io>> {
    + writeUTF(str: String): void
}


' ================================================
' RELATIONSHIPS
' ================================================
ClientMain  ..>  Client             : creates & calls run() >

Client      ..>  Socket             : opens (host, port) >
Client      ..>  DataInputStream    : reads server messages >
Client      ..>  DataOutputStream   : sends user input >

@enduml