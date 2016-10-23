package arm

import org.junit.Test

class InstructionDecoderTest {
    @Test
    fun name() {
        val thumbDecoder = createDecoder(Thumb::class.java)
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
}