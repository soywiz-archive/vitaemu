import arm.Memory
import com.soywiz.util.ELF
import com.soywiz.vitaorganizer.ext.getResourceStream2
import org.junit.Test
import java.io.File

class ELFTest {
    @Test
    fun name() {
        val memory = Memory(0x80000)
        val elf = ELF.read(SELF.read(getResourceStream2("hello_world/eboot.bin")!!).elf)
        elf.writeTo(memory.stream)

        File("dump.bin").writeBytes(memory.stream.slice().readAll())
    }
}