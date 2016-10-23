package arm

import com.soywiz.util.invalidOp
import java.lang.reflect.Method

@Suppress("unused")
annotation class Ins(val pattern: String)

class Instruction(val pattern: String, val method: Method) {
    class Arg(val name: String, val offset: Int, val length: Int) {
        val mask = (1 shl length) - 1
        fun extract(code: Int) = (code ushr offset) and mask
    }

    var mask = 0
    var value = 0
    val args = arrayListOf<Arg>()

    fun matches(v: Int) = (v and mask) == value

    init {
        var pos = 0
        for (chunk in pattern.split(':').reversed()) {
            if ((chunk[0] == '0') || (chunk[0] == '1')) {
                for (c in chunk.reversed()) {
                    when (c) {
                        '0' -> {
                        }
                        '1' -> {
                            value = value or (1 shl pos)
                        }
                        else -> invalidOp("Expected 0 or 1")
                    }
                    mask = mask or (1 shl pos)
                    pos++
                }
            } else {
                args += Arg(chunk, pos, chunk.length)
                pos += chunk.length
            }
        }
    }

    override fun toString() = "Instruction(%04X:%04X)".format(mask, value)
}