package converter
import java.math.BigDecimal

fun power(num: Int, pow: Int): String {
    var result = num.toBigInteger()
    if (pow == 0) {
        return 1.toString()
    } else {
        for (i in 1 until pow) {
            result *= num.toBigInteger()
        }
    }
    return result.toString()
}

fun atoiBase(number: String, base: Int): String {
    var result = ""
    val abc = "0123456789abcdefghijklmnopqrstuvwxyz"
    val zero = 0.toBigInteger()
    var (num, mod) = number.toBigInteger().divideAndRemainder(base.toBigInteger())
    if (num != zero) result += atoiBase(num.toString(), base)
    result += "" + abc[mod.toString().toInt()]
    return result
}

fun fractionalBase(number: String, base: Int): String {
    var result = ""
    val abc = "0123456789abcdefghijklmnopqrstuvwxyz"
    val zero = 0.toBigDecimal()
    var fractional = number.toBigDecimal()
    var i = 0
    while (fractional != zero && i < 5) {
        fractional *= base.toBigDecimal()
        var (intPart, fractionalPart) = fractional.toString().split('.')
        result += "" + abc[intPart.toInt()]
        fractional = ("0." + fractionalPart).toBigDecimal()
        i++
    }
    return result
}

fun toDecimal(number: String, base: Int): String {
    var result = 0.toBigInteger()
    var power = 0
    val abc = "abcdefghijklmnopqrstuvwxyz"
    val reverseNum = number.reversed()
    for (ch in reverseNum) {
        if (abc.contains(ch)) {
            val baseNum = 10 + abc.indexOf(ch.lowercaseChar())
            result += baseNum.toBigInteger() * power(base, power).toBigInteger()
        } else {
            result += ch.digitToInt().toBigInteger() * power(base, power).toBigInteger()
        }
        power++
    }
    return result.toString()
}

fun fractionalToDecimal(number: String, base: Int): String {
    var result = BigDecimal("0.00000")
    var power = 1
    val one = BigDecimal("1.00000")
    val abc = "abcdefghijklmnopqrstuvwxyz"
    for (ch in number) {
        if (abc.contains(ch)) {
            val baseNum = 10 + abc.indexOf(ch.lowercaseChar())
            result = result + baseNum.toBigDecimal() * one / power(base, power).toBigDecimal()
        } else {
            result += ch.digitToInt().toBigDecimal() * one / power(base, power).toBigDecimal()
        }
        power++
    }
    return result.toString()
}

fun main() {
    var answer = ""
    while (answer != "/exit") {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
        answer = readln()
        if (answer != "/exit") {
            val (srcBase, trgtBase) = answer.split(" ")
            var num = ""
            while (num != "/back") {
                print("Enter number in base $srcBase to convert to base $trgtBase (To go back type /back) ")
                num = readln()
                if (num != "/back") {
                    if (!num.contains('.')) {
                        if (srcBase.toInt() != 10) {
                            num = toDecimal(num, srcBase.toInt())
                        }
                        if (trgtBase.toInt() == 10) {
                            println("Conversion result: $num")
                        } else {
                            println("Conversion result: ${atoiBase(num, trgtBase.toInt())}")
                        }
                    } else {
                        var (intPart, fractionalPart) = num.split('.')
                        if (srcBase.toInt() != 10) {
                            intPart = toDecimal(intPart, srcBase.toInt())
                            fractionalPart = fractionalToDecimal(fractionalPart, srcBase.toInt())
                        }

                        if (trgtBase.toInt() == 10) {
                            println("Conversion result: ${intPart + fractionalPart.substring(2)}")
                        } else {
                            println("Conversion result: ${atoiBase(intPart, trgtBase.toInt())
                                    + "." + fractionalBase(fractionalPart, trgtBase.toInt())}")
                        }
                    }
                }
            }
        }
    }
}