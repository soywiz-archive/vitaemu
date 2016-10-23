package arm

import com.soywiz.util.parseHex
import org.junit.Assert
import org.junit.Test

class InstructionDecoderTest {
    val memory = Memory(0x100)
    val thumbDecoder = createDecoder(Thumb::class.java)

    @Test
    fun name() {
        thumbDecoder.execute(0xBF00, Thumb())
        thumbDecoder.execute(0x4618, Thumb())
        thumbDecoder.execute(0x469E, Thumb())
        thumbDecoder.execute(0x4770, Thumb())
        thumbDecoder.execute(0xB5F8, Thumb())
        thumbDecoder.execute(0xBCF8, Thumb())
        thumbDecoder.execute(0xBC08, Thumb())

        //ThumbDecoder.decode(0x4618) // MOV             R0, R3   ###  18 46  ### 0x4618
        //ThumbDecoder.decode(0x1846) // MOV             R0, R3   ###  18 46  ### 0x4618
        //ThumbDecoder.decode(0xB082) // SUB             SP, SP, #8
        //InstructionDecoder.decode(0x2324F246) // MOVW            R3, #0x6224
    }

    private fun logInstructions(count: Int, s: String): List<String> {
        val thumb = ThumbLog()
        memory.stream.slice().writeBytes(s.parseHex())
        var PC = 0
        for (n in 0 until count) PC += thumbDecoder.execute(memory.readInt(PC), thumb)
        return thumb.out
    }

    @Test
    fun name2() {
        Assert.assertEquals(
                listOf(
                        "PUSH {R3, R4, R5, R6, R7, LR}",
                        "NOP",
                        "POP {R3, R4, R5, R6, R7}",
                        "POP {R3}",
                        "MOV LR, R3",
                        "BX LR"
                ),
                logInstructions(6, "F8 B5 00 BF F8 BC 08 BC  9E 46 70 47")
        )
    }

    @Test
    fun name3() {
        val thumb = Thumb()
        memory.stream.slice().writeBytes("08 B5 00 21 04 46 04 F0  60 FE 48 F6 80 33 C0 F2 00 03 18 68 C3 6B 03 B1".parseHex())

        var PC = 0
        println("---------- [3]")
        for (n in 0 until 6) {
            PC += thumbDecoder.execute(memory.readInt(PC), thumb)
        }
    }

    class ThumbLog : Thumb() {
        val out = arrayListOf<String>()

        override fun println(s: String) {
            out += s
        }
    }
}
