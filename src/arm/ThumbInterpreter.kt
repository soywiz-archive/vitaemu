package arm

import com.soywiz.util.bit

class ThumbInterpreter(val cpu: CPU) : Thumb() {
    val mem = cpu.mem
    override fun nop() = Unit

    fun PUSH(value: Int) {
        mem.writeInt(cpu.SP, value)
        cpu.SP -= 4
    }

    fun POP(): Int {
        val value = mem.readInt(cpu.SP)
        cpu.SP -= 4
        return value
    }

    override fun mov_r(d: Int, m: Int) {
        cpu.R[d] = cpu.R[m]
    }

    override fun mov_i(d: Int, i: Int) {
        cpu.R[d] = i
    }

    override fun movw(d: Int, i: Int) { // mov_i alias?
        cpu.R[d] = i
    }

    override fun movt_w(d: Int, i: Int) {
        cpu.R[d] = (cpu.R[d] and 0xFFFF) or (i shl 16)
    }

    override fun push(r: Int) {
        for (n in 15 downTo 0) if (r.bit(n)) PUSH(cpu.R[r])
    }

    override fun pop(r: Int) {
        for (n in 0 .. 15) if (r.bit(n)) cpu.R[r] = POP()
    }
}