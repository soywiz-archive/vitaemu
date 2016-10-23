package arm

object InstructionDecoder {
    object Conds {
        const val EQ = 0b0000
        const val NE = 0b0001
        const val HS = 0b0010
        const val LO = 0b0011
        const val MI = 0b0100
        const val PL = 0b0101
        const val VS = 0b0110
        const val VC = 0b0111
        const val HI = 0b1000
        const val LS = 0b1001
        const val GE = 0b1010
        const val LT = 0b1011
        const val GT = 0b1100
        const val LE = 0b1101
        const val AL = 0b1110
        const val NV = 0b1111
    }

    val Int.cond: Int get() = (this ushr 28) and 0b1111
    val Int.kind1: Int get() = (this ushr 25) and 0b111
    val Int.opcode: Int get() = (this ushr 21) and 0b1111
    val Int.S: Int get() = (this ushr 20) and 0b1
    val Int.Rd_12: Int get() = (this ushr 12) and 0b1111
    val Int.Rn_16: Int get() = (this ushr 16) and 0b1111
    val Int.Operand2: Int get() = (this ushr 0) and 0b111111111111

    fun decode(i: Int) {
        when (i.kind1) {
            0b001 -> { // Data processing and FSR transfer
                println("001")
                println("opcode: ${i.opcode}")
                println(i.Rd_12)
                println(i.Rn_16)
                when (i.opcode) {
                    0b0000 -> { // AND
                        println("AND")
                    }
                    0b0001 -> { // EOR
                        println("EOR")
                    }
                    0b0010 -> { // SUB
                        println("SUB")
                    }
                    0b0011 -> { // RSB
                        println("RSB")
                    }
                    0b0100 -> { // ADD
                        println("ADD")
                    }
                    0b0101 -> { // ADC
                        println("ADC")
                    }
                    0b0110 -> { // SBC
                        println("SBC")
                    }
                    0b0111 -> { // RSC
                        println("RSC")
                    }
                    0b1000 -> { // TST
                        println("TST")
                    }
                    0b1001 -> { // TEQ
                        println("TEQ")
                    }
                    0b1010 -> { // CMP
                        println("CMP")
                    }
                    0b1011 -> { // CMN
                        println("CMN")
                    }
                    0b1100 -> { // ORR
                        println("ORR")
                    }
                    0b1101 -> { // MOV
                        println("MOV")
                    }
                    0b1110 -> { // BIC
                        println("BIC")
                    }
                    0b1111 -> { // MVN
                        println("MVN")
                    }
                }
            }
            else -> {
                println("XXX")
            }
        }
    }
}