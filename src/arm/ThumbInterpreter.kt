package arm

class ThumbInterpreter(val cpu: CPU) : Thumb() {
    override fun mov(d: Int, m: Int, D: Int) {
        cpu.R[d or (D shl 3)] = cpu.R[m]
    }

    override fun mov(d: Int, m: Int) {
        cpu.R[d] = cpu.R[m]
    }
}