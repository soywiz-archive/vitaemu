package arm

@Suppress("unused")
open class Thumb {
    private fun reg(v: Int) = when (v) {
        13 -> "SP"
        14 -> "LR"
        15 -> "PC"
        else -> "R$v"
    }

    open fun println(s: String) {
        System.out.println(s)
    }

    private fun sext(x: Int, b: Int): Int {
        val m = 1 shl (b - 1); // mask can be pre-computed if b is fixed
        return ((x and ((1 shl b) - 1)) xor m) - m;
    }

    @Ins("1011111100000000")
    open fun nop() {
        println("NOP")
    }

    @Ins("01000110:D:mmmm:ddd") // MOV (register) T1
    open fun mov(d: Int, m: Int, D: Int) {
        val DD = d or (D shl 3)
        println("MOV ${reg(DD)}, ${reg(m)}")
    }

    @Ins("0000000000:mmm:ddd") // MOV (register) T2
    open fun mov(d: Int, m: Int) {
        println("MOV ${reg(d)}, ${reg(m)}")
    }

    @Ins("00100:ddd:iiiiiiii")
    open fun mov_i(i: Int, d: Int) {
        println("MOVS ${reg(d)}, #$i")
    }

    // 1 1 1 1 0 i 1 0 0 1 0 0 imm4 || 0 imm3 Rd imm8
    // 0 imm3 Rd imm8 || 1 1 1 1 0 i 1 0 0 1 0 0 imm4
    @Ins("0:xxx:xxxx:iiiiiiii::11110:i:100100:iiii")
    open fun movw_t3(imm4:Int, imm1: Int, imm8: Int, rd: Int, imm3: Int) {
        //d = UInt(Rd); setflags = FALSE; imm32 = ZeroExtend(imm4:i:imm3:imm8, 32);
        //if d IN {13,15} then UNPREDICTABLE;
        var out = 0
        out = out or (imm8 shl 0)
        out = out or (imm3 shl 8)
        out = out or (imm1 shl 11)
        out = out or (imm4 shl 12)
        println("MOVW ${reg(rd)}, #$out")
    }

    // 1 1 1 1 0 i 1 0 1 1 0 0 imm4 || 0 imm3 Rd imm8
    // 0 imm3 Rd imm8 || 1 1 1 1 0 i 1 0 1 1 0 0 imm4
    @Ins("0:xxx:xxxx:iiiiiiii::11110:i:101100:iiii")
    open fun movt_w(imm4:Int, imm1: Int, imm8: Int, rd: Int, imm3: Int) {
        var out = 0
        out = out or (imm8 shl 0)
        out = out or (imm3 shl 8)
        out = out or (imm1 shl 11)
        out = out or (imm4 shl 12)
        println("MOVT.W ${reg(rd)}, #$out")
    }

    @Ins("010001110:mmmm:000") // Encoding T1 All versions of the Thumb ISA. BX<c> <Rm>
    open fun bx(m: Int) {
        println("BX ${reg(m)}")
    }

    private fun getRegList(r: Int) = (0 until 16).filter { ((r ushr it) and 1) != 0 }
    private fun getRegList(r: Int, M: Int, P: Int) = getRegList(r or (M shl 14) or (P shl 15))

    @Ins("1011010:M:rrrrrrrr")
    open fun push(r: Int, M: Int) {
        println("PUSH {%s}".format(getRegList(r, M, 0).map { reg(it) }.joinToString(", ")))
    }

    @Ins("1011110:P:rrrrrrrr")
    open fun pop(r: Int, P: Int) {
        println("POP {%s}".format(getRegList(r, 0, P).map { reg(it) }.joinToString(", ")))
    }


    // 1 1 1 1 0 S imm10 || 1 1 J1 1 J2 imm11
    // 1 1 J1 1 J2 imm11 || 1 1 1 1 0 S imm10

    @Ins("11:j:1:J:IIIIIIIIIII::11110:S:iiiiiiiiii")
    open fun bl(imm10: Int, S: Int, imm11: Int, J2: Int, J1: Int) {
        // I1 = NOT(J1 EOR S); I2 = NOT(J2 EOR S); imm32 = SignExtend(S:I1:I2:imm10:imm11:'0', 32);
        val I1 = (J1 xor S).inv() and 1
        val I2 = (J2 xor S).inv() and 1
        var out = 0
        out = out or (S shl 23)
        out = out or (I1 shl 22)
        out = out or (I2 shl 21)
        out = out or (imm10 shl 11)
        out = out or (imm11 shl 0)
        val imm = sext(out, 24) + 2
        println("BL #$imm")
    }

    // LDR (immediate) Encoding T1
    // 0 1 1 0 1 imm5 Rn Rt
    @Ins("01101:iiiii:nnn:ttt")
    open fun ldr_t1(Rt: Int, Rn: Int, imm5: Int) {
        if (imm5 == 0) {
            println("LDR ${reg(Rt)}, [${reg(Rn)}]")
        } else {
            println("LDR ${reg(Rt)}, [${reg(Rn)},#${imm5 * 4}]")
        }
    }
}
