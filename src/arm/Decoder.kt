package arm


interface Decoder<T> {
    fun execute(pc: Int, code: Int, handler: T): Int
}

fun <T> createSimpleSlowDecoder(clazz: Class<T>): Decoder<T> {
    val iList = arrayListOf<Instruction<T>>()
    for (method in clazz.declaredMethods) {
        val ins = method.getAnnotation(Ins::class.java)
        if (ins != null) {
            iList += Instruction<T>(ins.pattern, method)
        }
    }

    return object : Decoder<T> {
        override fun execute(pc: Int, code: Int, handler: T): Int {
            for (i in iList) {
                if (i.matches(code)) return i.call(pc, code, handler)
            }
            println("Unmatched code %04X".format(code and 0xFFFF))
            return 2
        }
    }
}

fun <T> createDecoder(clazz: Class<T>): Decoder<T> {
    return createSimpleSlowDecoder(clazz)
}
