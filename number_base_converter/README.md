# Kotlin_Basics
Kotlin Basics by JetBrains Academy


### [Converter from any bases to any bases even numbers with fraction part](https://github.com/VukolRo/Kotlin_Basics/tree/main/number_base_converter)
My very first project on Kotlin programming language which is part of JetBrains Academy Kotlin Basics course.

What I have learned during this project:
- Basic literals
- Values and variables
- Data types and how to work with each of it
- Standard output and input
- Type conversion
- Reading data with a readln
- String basics and templates
- If expression
- Loops and ranges
- Converting from decimal to binary, to Octal, to Hexadecimal and to all other bases
- Converting from any bases to decimal
- MutableList and ImmutableList
- BigInteger and BigDecimal

Program have a two-level menu:
- On the first level, the user sees the following prompt: 
```
Enter two numbers in format: {source base} {target base} (To quit type /exit) >
```
Then, they input two numbers separated by a single space: source base and target base. The user also has the option to use the /exit command to quit the program.

- On the second level, the user sees another prompt:
```
Enter number in base {user source base} to convert to base {user target base} (To go back type /back) > // and inputs the number in the source base.
```
The program outputs the message 
```
Conversion result: > // followed by the number in the target base
```
Then, the program asks for the new number to convert from the previously provided source base to the target base. To change the bases, the user can choose the BACK command and return to the first level menu to make the necessary changes.

### To run the program:
- clone [that](https://github.com/VukolRo/Kotlin_Basics/tree/main/number_base_converter) repository to your computer
- open Main.kt file which has path: numbe_base_converter/Number Base Converter/task/src/converter/Main.kt
- build the project in your IDE
- use terminal to operate with it and follow instruction there
