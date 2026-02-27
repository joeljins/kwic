# README 

### To Compile Server:
```bash
javac -d ./server_out server_src/*.java
jar cfe server.jar Controller -C server_out .
```

### To Run Server:
```bash
java -jar server.jar kwic-processing config.properties 
java -jar server.jar keyword-search <keyword> config.properties 
java -jar server.jar index-generation config.properties
java -jar server.jar server config.properties
```

### To Compile Client:
```bash
javac -d ./client_out client_src/*.java
jar cfe client.jar Client -C client_out .
```

### To Run Client:
```bash
java -jar client.jar 
```

### Misc.
- You need to use a .txt file when "Input=TxtInputObj"
- You need to use a .csv file when "Input=CSVInputObj"


### Misc.
- Not mentioned in instructions/rubroc
    - Testing all invalid inputs
    - Case sensitivity for search