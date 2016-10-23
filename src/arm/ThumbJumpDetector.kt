package arm

class ThumbJumpDetector : Thumb() {
    var goto = false
    var gotoFixed = false
    var endOfFunction = false
    override val doLog: Boolean = false

    private fun gotoFixed(link: Boolean) {
        goto = true
        gotoFixed = true
        if (!link) endOfFunction = true
    }

    private fun gotoVariable(link: Boolean) {
        goto = true
        gotoFixed = false
        if (!link) endOfFunction = true
    }

    override fun bx(m: Int) {
        gotoVariable(link = false)
    }

    override fun bl(pc: Int, target: Int) {
        gotoFixed(link = true)
    }

    override fun ldr(Rt: Int, Rn: Int, offset: Int) {
        if (Rt == PC) gotoVariable(link = false)
    }

    override fun pop(r: Int) {
        if ((r and (1 shl PC)) != 0) gotoVariable(link = false)
    }
}