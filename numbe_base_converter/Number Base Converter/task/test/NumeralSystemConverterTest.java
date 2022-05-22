import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.hyperskill.hstest.testing.TestedProgram;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NumeralSystemConverterTest extends StageTest<String> {
    
    @Override
    public List<TestCase<String>> generate () {
        
        return Arrays.asList(
                new TestCase<String>().setDynamicTesting(this::test1),
                new TestCase<String>().setDynamicTesting(this::test2),
                new TestCase<String>().setDynamicTesting(this::test3),
                new TestCase<String>().setTimeLimit(60000).setDynamicTesting(this::test4),
                new TestCase<String>().setTimeLimit(60000).setDynamicTesting(this::test5),
                new TestCase<String>().setTimeLimit(60000).setDynamicTesting(this::test6),
                new TestCase<String>().setTimeLimit(60000).setDynamicTesting(this::test7),
                new TestCase<String>().setTimeLimit(60000).setDynamicTesting(this::test8),
                new TestCase<String>().setTimeLimit(60000).setDynamicTesting(this::test9)
        );
    }

    String getConversionResult(String[] lines) {
        for (String line : lines) {
            if (line.contains(":")) {
                return line.substring(line.indexOf(":") + 1).trim();
            }
        }

        throw new WrongAnswer(
            "Cannot find a \"Conversion result:\" part in the output.");
    }
    
    //test exit command
    CheckResult test1 () {
        
        TestedProgram main = new TestedProgram();
        String output = main.start().toLowerCase();
        
        if (!output.contains("source base") || !output.contains("target base") || !output
                .contains("/exit")) {
            return CheckResult
                    .wrong("Your program should output the message \"Enter two numbers in format:" +
                                   " {source base} {target base} (To quit type /exit)\" when it starts");
        }
        
        main.execute("/exit");
        if (!main.isFinished()) {
            return CheckResult.wrong("Your program should terminate when the user enters " +
                                             "\"/exit\"");
        }
        
        return CheckResult.correct();
    }
    
    //test output format
    CheckResult test2 () {
        
        TestedProgram main = new TestedProgram();
        String output;
        String randomDecimal;
        String actualResult;
        String userResult;
        String lastLine;
        String[] lines;
        
        main.start();
        output = main.execute("10 2").toLowerCase();
        if (!output.contains("base 10") || !output.contains("convert to base 2")) {
            return CheckResult.wrong("Your program should prompt the user for the number to be " +
                                             "converted with the message \"Enter number in base " +
                                             "{user source base} to convert to base {user target base}" +
                                             " (To go back type /back)\" after accepting the " +
                                             "source and target base");
        }
        
        if (!output.contains("/back")) {
            return CheckResult.wrong("Your program should provide the user with an option to go " +
                                             "back to the top-level menu with the message \"Enter number in base " +
                                             "{user source base} to convert to base {user target base} " +
                                             "(To go back type /back)\"");
        }
        
        randomDecimal = Generator.getRandomSourceNumber(10, false);
        actualResult = Converter.decimalToBaseX(randomDecimal, 2);
        
        output = main.execute(randomDecimal).toLowerCase();
        lines = output.split("\n");
        lastLine = lines[lines.length - 1];
        
        if (!lines[0].contains("result:")) {
            return CheckResult.wrong("Your program should print the conversion result in the " +
                                             "format \"Conversion result: CONVERTED_NUMBER\"");
        }
        
        userResult = getConversionResult(lines);

        if (!userResult.equals(actualResult)) {
            return CheckResult.wrong("The conversion result of your program is wrong");
        }
        
        if (main.isFinished()) {
            return CheckResult.wrong("Your program should not terminate until the user enter " +
                                             "\"/exit\" in the top-level menu");
        }
        
        if (lastLine.contains("/exit")) {
            return CheckResult.wrong("Your program should remember the user's source and target " +
                                             "base and should not return to the top-level menu " +
                                             "until the user enters \"/back\"");
        }
        
        if (!lastLine.contains("base 10") || !lastLine.contains("convert to base 2")) {
            return CheckResult.wrong("After each conversion, your program should prompt the user" +
                                             " for a number to be " +
                                             "converted with the message \"Enter number in base " +
                                             "{user source base} to convert to base {user target base}" +
                                             " (To go back type /back)\" until the user enters " +
                                             "\"/back\"");
        }
        
        
        return CheckResult.correct();
    }
    
    //test back command
    CheckResult test3 () {
        
        TestedProgram main = new TestedProgram();
        String output;
        String lastLine;
        String[] lines;
        
        
        main.start();
        main.execute("10 2");
        main.execute(Generator.getRandomSourceNumber(10, false));
        
        output = main.execute("/back").toLowerCase();
        if (!output.contains("/exit")) {
            return CheckResult.wrong("Your program should take the user back to the top-level " +
                                             "menu when they enter \"/back\"");
        }
        
        main.execute("10 8");
        output = main.execute(Generator.getRandomSourceNumber(10, false)).toLowerCase();
        
        lines = output.split("\n");
        lastLine = lines[lines.length - 1];
        if (!lastLine.contains("base 10") || !lastLine.contains("convert to base 8")) {
            return CheckResult.wrong("After each conversion, your program should prompt the user" +
                                             " for a number to be " +
                                             "converted with the message \"Enter number in base " +
                                             "{user source base} to convert to base {user target base}" +
                                             " (To go back type /back)\" until the user enters " +
                                             "\"/back\"");
        }
        
        main.execute("/back");
        main.execute("/exit");
        if (!main.isFinished()) {
            return CheckResult.wrong("Your program should terminate when the user enters " +
                                             "\"/exit\"");
        }
        
        return CheckResult.correct();
    }
    
    //using BigInteger
    CheckResult test4 () {
        
        TestedProgram main = new TestedProgram();
        String output;
        String lastLine;
        String userResult;
        String actualResult;
        String randomBigInteger;
        String[] lines;
        
        main.start();
        
        for (int sourceBase = 2; sourceBase <= 18; sourceBase += 3) {
            
            for (int targetBase = 2; targetBase <= 36; targetBase += 3) {
                
                output = main.execute(sourceBase + " " + targetBase).toLowerCase();
                if (!output.contains("base " + sourceBase) || !output
                        .contains("convert to base " + targetBase)) {
                    return CheckResult
                            .wrong("Your program should prompt the user for the number to be " +
                                           "converted with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base}" +
                                           " (To go back type /back)\" after accepting the " +
                                           "source and target base");
                }
                
                if (!output.contains("/back")) {
                    return CheckResult
                            .wrong("Your program should provide the user with an option to go " +
                                           "back to the top-level menu with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base} " +
                                           "(To go back type /back)\"");
                }
                
                randomBigInteger = Generator.getRandomBigInteger(sourceBase, false);
                actualResult = Converter
                        .convertSourceToTargetBase(randomBigInteger, sourceBase, targetBase
                                , false);
                
                output = main.execute(randomBigInteger).toLowerCase();
                
                lines = output.split("\n");
                lastLine = lines[lines.length - 1];
                
                userResult = getConversionResult(lines);

                if (!userResult.equalsIgnoreCase(actualResult)) {
                    return CheckResult.wrong("The conversion result of your program is wrong");
                }
                
                if (main.isFinished()) {
                    return CheckResult
                            .wrong("Your program should not terminate until the user enter " +
                                           "\"/exit\" in the top-level menu");
                }
                
                if (lastLine.contains("/exit")) {
                    return CheckResult
                            .wrong("Your program should remember the user's source and target " +
                                           "base and should not return to the top-level menu " +
                                           "until the user enters \"/back\"");
                }
                
                main.execute("/back");
            }
            
        }
        
        main.execute("/exit");
        if (!main.isFinished()) {
            return CheckResult.wrong("Your program should terminate when the user enters " +
                                             "\"/exit\"");
        }
        
        return CheckResult.correct();
    }
    
    CheckResult test5 () {
        
        TestedProgram main = new TestedProgram();
        String output;
        String lastLine;
        String userResult;
        String actualResult;
        String randomBigInteger;
        String[] lines;
        
        main.start();
        
        for (int sourceBase = 19; sourceBase <= 36; sourceBase += 3) {
            
            for (int targetBase = 2; targetBase <= 36; targetBase += 3) {
                
                if (sourceBase == targetBase) {
                    continue;
                }
                
                output = main.execute(sourceBase + " " + targetBase).toLowerCase();
                if (!output.contains("base " + sourceBase) || !output
                        .contains("convert to base " + targetBase)) {
                    return CheckResult
                            .wrong("Your program should prompt the user for the number to be " +
                                           "converted with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base}" +
                                           " (To go back type /back)\" after accepting the " +
                                           "source and target base");
                }
                
                if (!output.contains("/back")) {
                    return CheckResult
                            .wrong("Your program should provide the user with an option to go " +
                                           "back to the top-level menu with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base} " +
                                           "(To go back type /back)\"");
                }
                
                randomBigInteger = Generator.getRandomBigInteger(sourceBase, false);
                actualResult = Converter
                        .convertSourceToTargetBase(randomBigInteger, sourceBase, targetBase
                                , false);
                
                output = main.execute(randomBigInteger).toLowerCase();
                
                lines = output.split("\n");
                lastLine = lines[lines.length - 1];

                userResult = getConversionResult(lines);

                if (!userResult.equalsIgnoreCase(actualResult)) {
                    return CheckResult.wrong("The conversion result of your program is wrong");
                }
                
                if (main.isFinished()) {
                    return CheckResult
                            .wrong("Your program should not terminate until the user enter " +
                                           "\"/exit\" in the top-level menu");
                }
                
                if (lastLine.contains("/exit")) {
                    return CheckResult
                            .wrong("Your program should remember the user's source and target " +
                                           "base and should not return to the top-level menu " +
                                           "until the user enters \"/back\"");
                }
                
                main.execute("/back");
            }
            
        }
        
        main.execute("/exit");
        if (!main.isFinished()) {
            return CheckResult.wrong("Your program should terminate when the user enters " +
                                             "\"/exit\"");
        }
        
        return CheckResult.correct();
    }
    
    //test fraction conversion
    CheckResult test6 () {
        
        TestedProgram main = new TestedProgram();
        int fractionalPartLength;
        String output;
        String lastLine;
        String userResult;
        String actualResult;
        String randomFraction;
        String[] lines;
        
        main.start();
        
        for (int sourceBase = 2; sourceBase <= 18; sourceBase += 3) {
            for (int targetBase = 2; targetBase <= 36; targetBase += 3) {
                
                if (sourceBase == targetBase) {
                    continue;
                }
                
                output = main.execute(sourceBase + " " + targetBase).toLowerCase();
                if (!output.contains("base " + sourceBase) || !output
                        .contains("convert to base " + targetBase)) {
                    return CheckResult
                            .wrong("Your program should prompt the user for the number to be " +
                                           "converted with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base}" +
                                           " (To go back type /back)\" after accepting the " +
                                           "source and target base");
                }
                
                if (!output.contains("/back")) {
                    return CheckResult
                            .wrong("Your program should provide the user with an option to go " +
                                           "back to the top-level menu with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base} " +
                                           "(To go back type /back)\"");
                }
                
                randomFraction = Generator.getRandomSourceNumber(sourceBase, true);
                actualResult = Converter.convertSourceToTargetBase(randomFraction,
                                                                   sourceBase,
                                                                   targetBase, true);
                
                output = main.execute(randomFraction).toLowerCase();
                
                lines = output.split("\n");
                lastLine = lines[lines.length - 1];

                userResult = getConversionResult(lines);

                if (!userResult.contains(".")) {
                    return CheckResult.wrong("The conversion result your program outputs does not" +
                                                     " contain the fractional part");
                }

                if (userResult.split("\\.").length < 2) {
                    return CheckResult.wrong("A fractional number expected in the output!");
                }
                
                fractionalPartLength = userResult.split("\\.")[1].length();
                if (fractionalPartLength != 5) {
                    return CheckResult.wrong("The fractional part of your conversion should only " +
                                                     "be 5 digits in length");
                }
                
                if (!actualResult.contains(userResult.substring(0, userResult.indexOf(".") + 2))) {
                    return CheckResult.wrong("The conversion result of your program is wrong.\n" +
                        "Conversion for the number " + randomFraction +
                        " from base " + sourceBase + " to base " + targetBase + "\n" +
                        "Your answer: " + userResult + "\n" +
                        "Correct answer: " + actualResult.substring(0, actualResult.indexOf(".") + 6));
                }
                
                if (main.isFinished()) {
                    return CheckResult
                            .wrong("Your program should not terminate until the user enter " +
                                           "\"/exit\" in the top-level menu");
                }
                
                if (lastLine.contains("/exit")) {
                    return CheckResult
                            .wrong("Your program should remember the user's source and target " +
                                           "base and should not return to the top-level menu " +
                                           "until the user enters \"/back\"");
                }
                
                main.execute("/back");
            }
            
        }
        
        main.execute("/exit");
        if (!main.isFinished()) {
            return CheckResult.wrong("Your program should terminate when the user enters " +
                                             "\"/exit\"");
        }
        
        return CheckResult.correct();
    }
    
    CheckResult test7 () {
        
        TestedProgram main = new TestedProgram();
        int fractionalPartLength;
        String output;
        String lastLine;
        String userResult;
        String actualResult;
        String randomFraction;
        String[] lines;
        
        main.start();
        
        for (int sourceBase = 19; sourceBase <= 36; sourceBase += 3) {
            
            for (int targetBase = 2; targetBase <= 36; targetBase += 3) {
                
                if (sourceBase == targetBase) {
                    continue;
                }
                
                output = main.execute(sourceBase + " " + targetBase).toLowerCase();
                if (!output.contains("base " + sourceBase) || !output
                        .contains("convert to base " + targetBase)) {
                    return CheckResult
                            .wrong("Your program should prompt the user for the number to be " +
                                           "converted with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base}" +
                                           " (To go back type /back)\" after accepting the " +
                                           "source and target base");
                }
                
                if (!output.contains("/back")) {
                    return CheckResult
                            .wrong("Your program should provide the user with an option to go " +
                                           "back to the top-level menu with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base} " +
                                           "(To go back type /back)\"");
                }
                
                randomFraction = Generator.getRandomSourceNumber(sourceBase, true);
                actualResult = Converter.convertSourceToTargetBase(randomFraction,
                                                                   sourceBase,
                                                                   targetBase, true);
                
                output = main.execute(randomFraction).toLowerCase();
                
                lines = output.split("\n");
                lastLine = lines[lines.length - 1];

                userResult = getConversionResult(lines);

                if (!userResult.contains(".")) {
                    return CheckResult.wrong("The conversion result your program outputs does not" +
                                                     " contain the fractional part");
                }

                if (userResult.split("\\.").length < 2) {
                    return CheckResult.wrong("A fractional number expected in the output!");
                }
                
                fractionalPartLength = userResult.split("\\.")[1].length();
                if (fractionalPartLength != 5) {
                    return CheckResult.wrong("The fractional part of your conversion should only " +
                                                     "be 5 digits in length");
                }
                
                if (!actualResult.contains(userResult.substring(0, userResult.indexOf(".") + 2))) {
                    return CheckResult.wrong("The conversion result of your program is wrong");
                }
                
                if (main.isFinished()) {
                    return CheckResult
                            .wrong("Your program should not terminate until the user enter " +
                                           "\"/exit\" in the top-level menu");
                }
                
                if (lastLine.contains("/exit")) {
                    return CheckResult
                            .wrong("Your program should remember the user's source and target " +
                                           "base and should not return to the top-level menu " +
                                           "until the user enters \"/back\"");
                }
                
                main.execute("/back");
            }
            
        }
        
        main.execute("/exit");
        if (!main.isFinished()) {
            return CheckResult.wrong("Your program should terminate when the user enters " +
                                             "\"/exit\"");
        }
        
        return CheckResult.correct();
    }
    
    //test fraction using biginteger
    CheckResult test8 () {
        
        TestedProgram main = new TestedProgram();
        int fractionalPartLength;
        String output;
        String lastLine;
        String userResult;
        String actualResult;
        String randomFraction;
        String[] lines;
        
        main.start();
        
        Converter.setIsBigDec(true);
        
        for (int sourceBase = 2; sourceBase <= 18; sourceBase += 3) {
            
            for (int targetBase = 2; targetBase <= 36; targetBase += 3) {
                
                if (sourceBase == targetBase) {
                    continue;
                }
                
                output = main.execute(sourceBase + " " + targetBase).toLowerCase();
                if (!output.contains("base " + sourceBase) || !output
                        .contains("convert to base " + targetBase)) {
                    return CheckResult
                            .wrong("Your program should prompt the user for the number to be " +
                                           "converted with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base}" +
                                           " (To go back type /back)\" after accepting the " +
                                           "source and target base");
                }
                
                if (!output.contains("/back")) {
                    return CheckResult
                            .wrong("Your program should provide the user with an option to go " +
                                           "back to the top-level menu with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base} " +
                                           "(To go back type /back)\"");
                }
                
                randomFraction = Generator.getRandomBigInteger(sourceBase, true);
                actualResult = Converter.convertSourceToTargetBase(randomFraction,
                                                                   sourceBase,
                                                                   targetBase, true);
                
                output = main.execute(randomFraction).toLowerCase();
                
                lines = output.split("\n");
                lastLine = lines[lines.length - 1];

                userResult = getConversionResult(lines);

                if (!userResult.contains(".")) {
                    return CheckResult.wrong("The conversion result your program outputs does not" +
                                                     " contain the fractional part");
                }

                if (userResult.split("\\.").length < 2) {
                    return CheckResult.wrong("A fractional number expected in the output!");
                }
                
                fractionalPartLength = userResult.split("\\.")[1].length();
                if (fractionalPartLength != 5) {
                    return CheckResult.wrong("The fractional part of your conversion should only " +
                                                     "be 5 digits in length");
                }
                
                boolean correct = CheckConversion.check(userResult, actualResult);
                if (!correct) {
                    return CheckResult.wrong("The conversion result of your program is wrong\n" +
                        "Your answer:\n" + userResult + "\n" +
                        "Correct answer:\n" + actualResult);
                }
                
                if (main.isFinished()) {
                    return CheckResult
                            .wrong("Your program should not terminate until the user enter " +
                                           "\"/exit\" in the top-level menu");
                }
                
                if (lastLine.contains("/exit")) {
                    return CheckResult
                            .wrong("Your program should remember the user's source and target " +
                                           "base and should not return to the top-level menu " +
                                           "until the user enters \"/back\"");
                }
                
                main.execute("/back");
            }
            
        }
        
        main.execute("/exit");
        if (!main.isFinished()) {
            return CheckResult.wrong("Your program should terminate when the user enters " +
                                             "\"/exit\"");
        }
        
        return CheckResult.correct();
    }
    
    CheckResult test9 () {
        
        TestedProgram main = new TestedProgram();
        int fractionalPartLength;
        String output;
        String lastLine;
        String userResult;
        String actualResult;
        String randomFraction;
        String[] lines;
        
        main.start();
        
        for (int sourceBase = 19; sourceBase <= 36; sourceBase += 3) {
            
            for (int targetBase = 2; targetBase <= 36; targetBase += 3) {
                
                if (sourceBase == targetBase) {
                    continue;
                }
                
                output = main.execute(sourceBase + " " + targetBase).toLowerCase();
                if (!output.contains("base " + sourceBase) || !output
                        .contains("convert to base " + targetBase)) {
                    return CheckResult
                            .wrong("Your program should prompt the user for the number to be " +
                                           "converted with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base}" +
                                           " (To go back type /back)\" after accepting the " +
                                           "source and target base");
                }
                
                if (!output.contains("/back")) {
                    return CheckResult
                            .wrong("Your program should provide the user with an option to go " +
                                           "back to the top-level menu with the message \"Enter number in base " +
                                           "{user source base} to convert to base {user target base} " +
                                           "(To go back type /back)\"");
                }
                
                randomFraction = Generator.getRandomBigInteger(sourceBase, true);
                actualResult = Converter.convertSourceToTargetBase(randomFraction,
                                                                   sourceBase,
                                                                   targetBase, true);
                
                output = main.execute(randomFraction).toLowerCase();
                
                lines = output.split("\n");
                lastLine = lines[lines.length - 1];

                userResult = getConversionResult(lines);

                if (!userResult.contains(".")) {
                    return CheckResult.wrong("The conversion result your program outputs does not" +
                                                     " contain the fractional part");
                }

                if (userResult.split("\\.").length < 2) {
                    return CheckResult.wrong("A fractional number expected in the output!");
                }
                
                fractionalPartLength = userResult.split("\\.")[1].length();
                if (fractionalPartLength != 5) {
                    return CheckResult.wrong("The fractional part of your conversion should only " +
                                                     "be 5 digits in length");
                }
                
                boolean correct = CheckConversion.check(userResult, actualResult);
                if (!correct) {
                    return CheckResult.wrong("The conversion result of your program is wrong\n" +
                        "Your answer:\n" + userResult + "\n" +
                        "Correct answer:\n" + actualResult);
                }
                
                if (main.isFinished()) {
                    return CheckResult
                            .wrong("Your program should not terminate until the user enter " +
                                           "\"/exit\" in the top-level menu");
                }
                
                if (lastLine.contains("/exit")) {
                    return CheckResult
                            .wrong("Your program should remember the user's source and target " +
                                           "base and should not return to the top-level menu " +
                                           "until the user enters \"/back\"");
                }
                
                main.execute("/back");
            }
            
        }
        
        main.execute("/exit");
        if (!main.isFinished()) {
            return CheckResult.wrong("Your program should terminate when the user enters " +
                                             "\"/exit\"");
        }
        
        return CheckResult.correct();
    }
    
}

class Generator {
    
    private static String dec;
    
    static String getRandomSourceNumber (int sourceBase, boolean fraction) {
        
        if (fraction) {
            double number = new Random().nextDouble() * 1000;
            dec = String.valueOf(number);
            
            return Converter.fractionToBaseX(dec, sourceBase);
        } else {
            int n = new Random().nextInt(1000);
            
            return Integer.toString(n, sourceBase);
        }
        
    }
    
    static String getDec () {
        
        return dec;
    }
    
    static String getRandomBigInteger (int sourceBase, boolean fraction) {
        
        if (fraction) {
            
            BigDecimal max = new BigDecimal("500000000000000.0");
            BigDecimal randFromDouble = BigDecimal.valueOf(Math.random());
            BigDecimal actualRandomDec = randFromDouble.multiply(max);
            
            actualRandomDec = actualRandomDec.setScale(10, RoundingMode.FLOOR);
            dec = actualRandomDec.toString();
            
            return Converter.fractionToBaseX(dec, sourceBase);
        } else {
            
            BigInteger upperLimit = new BigInteger("500000000000000");
            BigInteger randomNumber;
            do {
                randomNumber = new BigInteger(upperLimit.bitLength(), new Random());
            } while (randomNumber.compareTo(upperLimit) >= 0);
            
            return randomNumber.toString(sourceBase);
        }
        
    }
}

class Converter {
    
    static boolean isBigDec = false;
    
    static void setIsBigDec (boolean bigDec) {
        
        isBigDec = bigDec;
    }
    
    static String decimalToBaseX (String num, int targetBase) {
        
        return new BigInteger(num).toString(targetBase);
    }
    
    
    static String baseXToDecimal (String number, int sourceBase) {
        
        BigInteger integer = new BigInteger(number, sourceBase);
        return integer.toString();
    }
    
    static String convertSourceToTargetBase (String number, int sourceBase, int targetBase,
                                             boolean fraction) {
        
        if (fraction) {
            
            //Step 1- Convert to decimal
            String decimalResult = isBigDec ? Generator.getDec() : baseXToDecimalFraction(number,
                                                                                          number.length(),
                                                                                          sourceBase);
            
            //Step 2- Convert to target base
            String result = fractionToBaseX(decimalResult, targetBase);
            return result;
        } else {
            
            //Step 1 - convert source number to decimal
            String decimal = baseXToDecimal(number, sourceBase);
            
            //Step 2 - convert decimal to target base
            return decimalToBaseX(decimal, targetBase);
        }
        
    }
    
    static String fractionToBaseX (String number, int targetBase) {
        
        String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase();
        
        StringBuilder output = new StringBuilder();
        BigDecimal doubleOfDecInp = new BigDecimal(number);
        
        BigInteger beforeDot = new BigDecimal(number).toBigInteger();
        output.append(beforeDot.toString(targetBase));
        
        BigDecimal bfd = new BigDecimal(beforeDot);
        
        doubleOfDecInp = doubleOfDecInp.subtract(bfd);
        
        output.append(".");
        
        BigInteger subtract = new BigDecimal(String.valueOf(doubleOfDecInp)).toBigInteger();
        BigDecimal bdOfDecInp = new BigDecimal(String.valueOf(doubleOfDecInp));
        BigDecimal bdFractionalPart = bdOfDecInp.subtract(new BigDecimal(subtract));
        double fractionalPart = bdFractionalPart.doubleValue();
        
        if (!isBigDec) {
            
            for (int i = 0; i < targetBase; ++i) {
                
                fractionalPart = fractionalPart * targetBase;
                int digit = (int) fractionalPart;
                char c = digits.charAt(digit);
                
                output.append(c);
                
                fractionalPart = fractionalPart - digit;
                
                if (fractionalPart == 0) {
                    break;
                }
            }
            
        } else {
            
            for (int i = 0; i < number.length(); ++i) {
                
                fractionalPart = fractionalPart * targetBase;
                int digit = (int) fractionalPart;
                char c = digits.charAt(digit);
                
                output.append(c);
                
                fractionalPart = fractionalPart - digit;
                
                if (fractionalPart == 0) {
                    break;
                }
            }
            
        }
        
        while (output.toString().split("\\.")[1].length() < 5) {
            output.append("0");
        }
        
        return output.toString();
    }
    
    static String baseXToDecimalFraction (String number, int len, int sourceBase) {
        
        String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        number = number.toUpperCase();
        
        // Fetch the radix point
        int point = number.indexOf('.');
        
        // Update point if not found
        if (point == -1) {
            point = len;
        }
        
        BigDecimal integralPart = new BigDecimal(0);
        BigDecimal fractionalPart = new BigDecimal(0);
        BigDecimal multiplier = new BigDecimal(1);
        
        // Convert integral part of number to decimal
        // equivalent
        
        BigDecimal sourceBaseBig = new BigDecimal(sourceBase);
        if (number.matches("\\d+.\\d*")) {
            for (int i = point - 1; i >= 0; i--) {
                char c = number.charAt(i);
                
                BigDecimal holder = new BigDecimal(c - '0');
                integralPart = integralPart.add((holder.multiply(multiplier)));
                multiplier = multiplier.multiply(sourceBaseBig);
            }
            
            // Convert fractional part of number to
            // decimal equivalent
            
            multiplier = sourceBaseBig;
            for (int i = point + 1; i < len; i++) {
                
                char c = number.charAt(i);
                
                BigDecimal holder = new BigDecimal((c - '0'));
                fractionalPart = fractionalPart.add((holder).divide(multiplier, 10,
                                                                    RoundingMode.HALF_UP));
                multiplier = multiplier.multiply(new BigDecimal(String.valueOf(sourceBase)));
            }
        } else {
            for (int i = 0; i < point; i++) {
                char c = number.charAt(i);
                int d = digits.indexOf(c);
                integralPart =
                        (sourceBaseBig.multiply(integralPart)).add(new BigDecimal(d));
                
            }
            
            multiplier = sourceBaseBig;
            for (int i = point + 1; i < len; i++) {
                char c = number.charAt(i);
                int d = digits.indexOf(c);
                BigDecimal holder = new BigDecimal(d);
                fractionalPart = fractionalPart.add((holder).divide(multiplier, 10,
                                                                    RoundingMode.HALF_UP));
                multiplier = multiplier.multiply(sourceBaseBig);
            }
            
        }
        
        String result = (integralPart.add(fractionalPart)).toString();
        return result;
    }
    
}

class CheckConversion {
    
    static boolean check (String userResult, String actualResult) {
        
        String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase();
        String[] arrUser = userResult.split("\\.");
        String[] arrActual = actualResult.split("\\.");
        String decimalPartUser = arrUser[1];
        String decimalPartActual = arrActual[1];
        
        if (!arrUser[0].equals(arrActual[0])) {
            return false;
        }
        
        char firstDigit = decimalPartUser.charAt(0);
        char firstDigitActual = decimalPartActual.charAt(0);
        int resultIndex = digits.indexOf(firstDigitActual);
        
        if (firstDigit != firstDigitActual) {
            char maxChar = 'Z';
            char minChar = '0';

            if (firstDigit == maxChar) {
                return digits.indexOf(firstDigit - 1) == firstDigitActual;
            } else if (firstDigit == minChar) {
                return digits.indexOf(firstDigit + 1) == firstDigitActual;
            } else {
                return digits.indexOf(firstDigit) + 1 == resultIndex || digits
                        .indexOf(firstDigit) - 1 == resultIndex;
            }
            
        } else {
            return true;
        }
        
    }
}
