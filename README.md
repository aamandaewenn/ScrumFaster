To run the ScrumFaster, you need to have:

1) javafx-sdk-19 library (version 19) installed on your computer,

2) java jdk (version 19) installed on your computer. To check if you have the right version, type "java -version" in your terminal. If it says that your current version is 19, everything is correct. If not, try deleting all other jdks on your machine and indicate the path to the right version (19) in your system variable "Path".

Once you have those things installed, open the terminal and run the command:

$java --module-path < absolute path to your javafx-sdk-19 library > --add-modules javafx.controls,javafx.fxml,javafx.swing -jar < absolute path to the project .jar file >
