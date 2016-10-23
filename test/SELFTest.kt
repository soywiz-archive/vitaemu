import com.soywiz.vitaorganizer.ext.getResourceStream2

class SELFTest {
    @org.junit.Test
    fun name() {
        SELF.read(getResourceStream2("hello_world/eboot.bin")!!)
    }
}