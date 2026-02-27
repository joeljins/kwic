@startuml component_diagram
title KWIC Index System — Component Diagram\n(Client: White-Box View | Server: Black-Box View)
skinparam componentStyle rectangle
' =====================================================
' CLIENT COMPONENT — White-Box View
' =====================================================
package "KWIC-Client Component\n<<white-box>>" as ClientPkg {
    class ClientMain {
        + {static} main(args: String[]): void
    }
    class Client {
        - host: String
        - port: int
        - timeout: int
        + Client(host: String, port: int, timeout: int)
        + run(): void
    }
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
    ClientMain  ..>  Client             : creates & calls run() >
    Client      ..>  Socket             : opens (host, port) >
    Client      ..>  DataInputStream    : reads server messages >
    Client      ..>  DataOutputStream   : sends user input >
}
' =====================================================
' SERVER COMPONENT — Black-Box View
' =====================================================
package "KWIC-Server Component\n<<black-box>>" as ServerPkg {
}

' =====================================================
' TCP Interface
' =====================================================
interface "TCP :8080" as TCP
Client      --> TCP        : sends query >
TCP         --> ServerPkg : received by Server >
ServerPkg   --> TCP        : sends response >
TCP         --> Client     : received by Client >
@enduml
