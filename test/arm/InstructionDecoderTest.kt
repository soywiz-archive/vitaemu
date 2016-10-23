package arm

import com.soywiz.util.parseHex
import org.junit.Assert
import org.junit.Test

class InstructionDecoderTest {
    val memory = Memory(0x10000)
    val thumbDecoder = createDecoder(Thumb::class.java)

    @Test
    fun name() {
        thumbDecoder.execute(0, 0xBF00, Thumb())
        thumbDecoder.execute(0, 0x4618, Thumb())
        thumbDecoder.execute(0, 0x469E, Thumb())
        thumbDecoder.execute(0, 0x4770, Thumb())
        thumbDecoder.execute(0, 0xB5F8, Thumb())
        thumbDecoder.execute(0, 0xBCF8, Thumb())
        thumbDecoder.execute(0, 0xBC08, Thumb())

        //ThumbDecoder.decode(0x4618) // MOV             R0, R3   ###  18 46  ### 0x4618
        //ThumbDecoder.decode(0x1846) // MOV             R0, R3   ###  18 46  ### 0x4618
        //ThumbDecoder.decode(0xB082) // SUB             SP, SP, #8
        //InstructionDecoder.decode(0x2324F246) // MOVW            R3, #0x6224
    }

    private fun dumpThumbInstructions(count: Int, s: String, start: Int = 0) {
        val thumb = Thumb()
        memory.stream.slice(start).writeBytes(s.parseHex())
        var PC = start
        println("-----------------")
        for (n in 0 until count) PC += thumbDecoder.execute(PC, memory.readInt(PC), thumb)
        println("-----------------")
    }

    private fun decodeThumbInstructions(count: Int, s: String): List<String> {
        val thumb = object : Thumb() {
            val out = arrayListOf<String>()

            override fun log(s: String) {
                out += s
            }
        }
        memory.stream.slice().writeBytes(s.parseHex())
        var PC = 0
        for (n in 0 until count) PC += thumbDecoder.execute(PC, memory.readInt(PC), thumb)
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
                decodeThumbInstructions(6, "F8 B5 00 BF F8 BC 08 BC  9E 46 70 47")
        )
    }

    @Test
    fun name3() {
        Assert.assertEquals(
                listOf(
                    "PUSH {R3, LR}",
                    "MOVS R1, #0",
                    "MOV R4, R0",
                    "BL #9826",
                    "MOVW R3, #35712",
                    "MOVT.W R3, #0",
                    "LDR R0, [R3]",
                    "LDR R3, [R0,#60]"
                ),
                decodeThumbInstructions(8, "08 B5 00 21 04 46 04 F0  60 FE 48 F6 80 33 C0 F2 00 03 18 68 C3 6B 03 B1 18 68 C3 6B")
        )
    }

    @Test
    fun name4() {
        dumpThumbInstructions(8, "00 B5 83 B0 FF F7 CA FF  02 F0 86 F8 48 F6 E8 33     00 22 C0 F2 00 03 69 46  01 20 00 93 01 92 00 F0 ", start = 0x00008114)
    }
}
