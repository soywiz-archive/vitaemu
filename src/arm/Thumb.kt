package arm

@Suppress("unused")
open class Thumb {
    @Ins("1011111100000000")
    open fun nop() {
        println("nop")
    }

    @Ins("01000110:D:mmmm:ddd") // MOV (register) T1
    open fun mov(d: Int, m: Int, D: Int) {
        println("mov R$d, R$m")
    }

    @Ins("0000000000:D:mmm:ddd") // MOV (register) T2
    open fun mov(d: Int, m: Int) {
        println("mov R$d, R$m")
    }

    @Ins("010001110:mmmm:000") // Encoding T1 All versions of the Thumb ISA. BX<c> <Rm>
    open fun bx(m: Int) {
        println("bx R$m")
    }

    @Ins("1011010:M:rrrrrrrr")
    open fun push(r: Int, M: Int) {
        println("push %02X".format(r))
    }

    @Ins("1011110:P:rrrrrrrr")
    open fun pop(r: Int, P: Int) {
        println("pop %02X".format(r))
    }
}
