package arm

import com.soywiz.util.invalidOp
import java.lang.reflect.Method

@Suppress("unused")
annotation class Ins(val pattern: String)

class Instruction<T>(val pattern: String, val method: Method) {
    class Arg(val name: String, val offset: Int, val length: Int) {
        val mask = (1 shl length) - 1
        fun extract(code: Int) = (code ushr offset) and mask
    }

    var size = 0
    var mask = 0; private set
    var value = 0; private set
    val args = arrayListOf<Arg>()

    fun matches(v: Int) = (v and mask) == value

    fun call(pc: Int, code: Int, handler: T): Int {
        val args = args.map { it.extract(code) }.toTypedArray()
        method.invoke(handler, pc, *args)
        return size
    }

    init {
        var pos = 0
        for (chunk in pattern.split(':').reversed()) {
            if (chunk.isEmpty()) continue
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
        if ((pos % 16) != 0) invalidOp("Size must be multiple of 16")
        size = pos / 8
    }

    override fun toString() = "Instruction(%04X:%04X)".format(mask, value)
}