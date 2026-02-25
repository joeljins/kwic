# README 

### Find Item 3
- hw1: item3_dv8_files\hw1
- hw2: item3_dv8_files\hw2
I included all dv8 files just incase I was missing something

### To Compile:
```bash
javac -d ./out src/*.java
jar cfe hw2.jar Controller -C out .
```

### To Run:
```bash
java -jar hw2.jar kwic-processing config.properties 
java -jar hw2.jar keyword-search <keyword> config.properties 
java -jar hw2.jar index-generation config.properties
```
``

### Misc.
- You need to use a .txt file when "Input=TxtInputObj"
- You need to use a .csv file when "Input=CSVInputObj"