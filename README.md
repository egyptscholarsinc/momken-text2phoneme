# Text2Phoneme

The project objective is to convert natural language text into phonemes that can be passed to the MBROLA system.

To download clone the project using the following command:

```
git clone https://github.com/egyptscholarsinc/momken-text2phoneme.git
```

Next, to build the project, simply use maven

```
mvn package
```

Once the jar is created in the target directory, you can execute the project as follows:

```
$ java -jar momken-text2phoneme.jar data/text2phoneme.tsv data/sample.txt out/sampleout.pho
```

where the files are located in the directories that were cloned from the source, namely the `data` directory and the `out` directory.

You can modify the lookup-table entries by modifying the text2phoneme.tsv file. This file contains the preliminary list of character-to-phoneme. This list is expected to undergo major change in the following releases.

