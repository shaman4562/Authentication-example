package hashKt

import java.math.BigInteger
import kotlin.experimental.and

fun ch(x: Long, y: Long, z: Long): Long {
    return x and y xor (x.inv() and z)
}

fun maj(x: Long, y: Long, z: Long): Long {
    return x and y xor (x and z) xor (y and z)
}

fun rotate(x: Long, l: Int): Long {
    return x ushr l or (x shl java.lang.Long.SIZE - l)
}

fun bigSigma0(x: Long): Long {
    return rotate(x, 28) xor rotate(x, 34) xor rotate(x, 39)
}

fun bigSigma1(x: Long): Long {
    return rotate(x, 14) xor rotate(x, 18) xor rotate(x, 41)
}

fun smallSigma0(x: Long): Long {
    return rotate(x, 1) xor rotate(x, 8) xor (x ushr 7)
}

fun smallSigma1(x: Long): Long {
    return rotate(x, 19) xor rotate(x, 61) xor (x ushr 6)
}

fun padArray(input: ByteArray): ByteArray {
    var size: Int = input.size + 17

    while (size % 128 != 0) size++

    val out = ByteArray(size)

    for (i in input.indices) {
        out[i] = input[i]
    }

    out[input.size] = 0x80.toByte()
    val lenInBytes = BigInteger.valueOf((input.size * 8).toLong()).toByteArray()
    for (i in lenInBytes.size downTo 1) {
        out[size - i] = lenInBytes[lenInBytes.size - i]
    }
    return out
}

fun byteByIndexToLong(input: ByteArray, index: Int): Long {
    var value: Long = 0
    for (i in 0..7) {
        value = (value shl 8) + (input[index + i] and 0xff.toByte())
    }
    return value
}

fun arrOfBytesToBlocks(input: ByteArray): Array<LongArray> {

    // Block = 1024 bits = 128 bytes = 16 longs
    val numberOfLongs = 16
    val numberOfBlocks = input.size / 128
    val blocks = Array(numberOfBlocks) { LongArray(numberOfLongs) }

    for (i in 0 until numberOfBlocks) {
        for (j in 0 until numberOfLongs) {
            blocks[i][j] = byteByIndexToLong(input, i * 128 + j * 8)
        }
    }
    return blocks
}

fun computeExpandedMessageBlocks(m: Array<LongArray>): Array<LongArray> {
    val w = Array(m.size) { LongArray(80) }

    for (i in m.indices) {
        for (j in 0..15) {
            w[i][j] = m[i][j]
        }
        for (j in 16..79) {
            w[i][j] = smallSigma1(w[i][j - 2]) + w[i][j - 7] + smallSigma0(w[i][j - 15]) + w[i][j - 16]
        }
    }
    return w
}
