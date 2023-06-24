# WordCount Project

This project demonstrates a word counting application using Apache Hadoop MapReduce. It also includes additional features such as grammar check, plagiarism detection, and word translation.

## Requirements

- Java Development Kit (JDK) 8 or later
- Apache Hadoop 2.x or later
- Google Cloud Translate API credentials (if using the translation feature)

## Usage

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/wordcount-project.git

Set up Apache Hadoop and ensure it is running correctly.

Update the project configuration:

    Open the WordCount.java file and provide the necessary values for the following constants:
        GRAMMAR_PATTERN: The regular expression pattern for grammar check.
        PLAGIARISM_THRESHOLD: The threshold value for plagiarism detection.
        GOOGLE_TRANSLATE_API_KEY: Your Google Cloud Translate API key (if using translation).

Build the project:

bash

javac -classpath $(hadoop classpath) -d wordcount-project/classes/ wordcount-project/src/*.java

Package the project:

bash

jar -cvf wordcount-project.jar -C wordcount-project/classes/ .

Run the application:

bash

    hadoop jar wordcount-project.jar WordCount input_directory output_directory

    Replace input_directory with the path to the input directory containing the text files to be processed, and output_directory with the desired output directory.

    View the results:

    The output files will be generated in the specified output directory. You can examine the word count and other analysis results.

Additional Information

    The Translator class provides translation functionality using the Google Cloud Translate API. If you wish to use this feature, make sure to set up the API credentials and update the GOOGLE_TRANSLATE_API_KEY constant.

    For more information on Apache Hadoop and MapReduce, refer to the official Apache Hadoop documentation.

    