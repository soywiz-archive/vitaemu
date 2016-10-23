package arm

import com.soywiz.util.Stream2

class Memory(val size: Int) {
    private val data = ByteArray(size)

    fun s8(offset: Int, v: Int) {
        this[offset] = v.toByte()
    }

    fun r8(offset: Int): Int {
        return this[offset].toInt() and 0xFF
    }

    operator fun set(offset: Int, v: Byte) {
        data[offset] = v
    }

    operator fun get(offset: Int): Byte {
        return data[offset]
    }

    val stream = object : Stream2() {
        override val length: Long = size.toLong()

        override fun readInternal(position: Long, bytes: ByteArray, offset: Int, count: Int): Int {
            val pos = position.toInt()
            for (n in 0 until count) bytes[offset + n] = this@Memory[pos + n]
            return count
        }

        override fun writeInternal(position: Long, bytes: ByteArray, offset: Int, count: Int) {
            val pos = position.toInt()
            for (n in 0 until count) this@Memory[pos + n] = bytes[offset + n]
        }
    }
}