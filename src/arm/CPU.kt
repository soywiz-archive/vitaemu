package arm

class CPU {
    val R = IntArray(16)

    var SP: Int get() = R[13]; set(v) { R[13] = v }
    var LR: Int get() = R[14]; set(v) { R[14] = v }
    var PC: Int get() = R[15]; set(v) { R[15] = v }

    //var R0: Int = 0
    //var R1: Int = 0
    //var R2: Int = 0
    //var R3: Int = 0
    //var R4: Int = 0
    //var R5: Int = 0
    //var R6: Int = 0
    //var R7: Int = 0
    //var R8: Int = 0
    //var R9: Int = 0
    //var R10: Int = 0
    //var R11: Int = 0
    //var R12: Int = 0
    //var SP: Int = 0
    //var LR: Int = 0
    //var PC: Int = 0
}