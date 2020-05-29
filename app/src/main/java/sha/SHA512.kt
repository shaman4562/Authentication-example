package hashKt


fun hash(input: ByteArray): String {
    var bytes = input
    bytes = padArray(bytes)
    val blocks = arrOfBytesToBlocks(bytes)
    val w = computeExpandedMessageBlocks(blocks)
    val buffer = IV.clone()

    for (i in blocks.indices) {
        var a = buffer[0]
        var b = buffer[1]
        var c = buffer[2]
        var d = buffer[3]
        var e = buffer[4]
        var f = buffer[5]
        var g = buffer[6]
        var h = buffer[7]

        for (j in 0..79) {
            val t1 = h + bigSigma1(e) + ch(e, f, g) + K[j] + w[i][j]
            val t2 = bigSigma0(a) + maj(a, b, c)
            h = g
            g = f
            f = e
            e = d + t1
            d = c
            c = b
            b = a
            a = t1 + t2
        }

        buffer[0] += a
        buffer[1] += b
        buffer[2] += c
        buffer[3] += d
        buffer[4] += e
        buffer[5] += f
        buffer[6] += g
        buffer[7] += h
    }
    val result = StringBuilder()
    for (i in 0..7) {
        result.append(String.format("%016x", buffer[i]))
    }
    return result.toString()
}
