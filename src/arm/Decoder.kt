package arm


interface Decoder<T> {
    fun execute(code: Int, handler: T): Int
}

fun <T> createSimpleSlowDecoder(clazz: Class<T>): Decoder<T> {
    val iList = arrayListOf<Instruction>()
    for (method in clazz.declaredMethods) {
        val ins = method.getAnnotation(Ins::class.java)
        if (ins != null) {
            iList += Instruction(ins.pattern, method)
        }
    }

    return object : Decoder<T> {
        override fun execute(code: Int, handler: T): Int {
            for (i in iList) {
                if (i.matches(code)) {
                    //println(i)
                    //println(handler)
                    val args = i.args.map { it.extract(code) }.toTypedArray()
                    i.method.invoke(handler, *args)
                    return i.size
                }
            }
            println("Unmatched code %04X".format(code))
            return 2
        }
    }
}

fun <T> createDecoder(clazz: Class<T>): Decoder<T> {
    return createSimpleSlowDecoder(clazz)
}
