SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_05\bin"
SET PATH=%JAVA_HOME%;%PATH%
SET CLASSPATH=%JAVA_HOME%;
cd D:\Sexto Semestre\Compiladores\Compiler\src\parser
java -jar C:\java-cup-11b.jar -parser analisis_sintactico -symbols Simbolos A_Sintactico_1.cup
pause
