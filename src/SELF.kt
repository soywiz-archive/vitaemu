import com.soywiz.util.Stream2
import com.soywiz.util.invalidOp

/**
 * http://www.vitadevwiki.com/index.php?title=SELF_File_Format
 */
class SELF(val elf: Stream2) {
    companion object {
        fun read(s: Stream2): SELF {
            val magic = s.readS32_le()                 // 53434500 = SCE\0
            if (magic != 0x00454353) invalidOp("Not a SELF file (not SCE\\x00 magic)")
            val version = s.readS32_le()               // header version
            if (version != 3) invalidOp("Just supported SELF version 3")
            val sdk_type = s.readS16_le()              //
            val header_type = s.readS16_le()           // 1 self, 2 rvk, 3 pkg, 4 spp
            val metadata_offset = s.readS32_le()       // metadata offset
            val header_len = s.readS64_le()            // self header length
            val elf_filesize = s.readS64_le()          // ELF file length
            val self_filesize = s.readS64_le()         // SELF file length
            val unknown = s.readS64_le()               // UNKNOWN
            val self_offset = s.readS64_le()           // SELF offset
            val appinfo_offset = s.readS64_le()        // app info offset
            val elf_offset = s.readS64_le()            // ELF #1 offset
            val phdr_offset = s.readS64_le()           // program header offset
            val shdr_offset = s.readS64_le()           // section header offset
            val section_info_offset = s.readS64_le()   // section info offset
            val sceversion_offset = s.readS64_le()     // version offset
            val controlinfo_offset = s.readS64_le()    // control info offset
            val controlinfo_size = s.readS64_le()      // control info size
            val padding = s.readS64_le()
            //return SELF(s.slice(elf_offset until elf_offset + elf_filesize))
            return SELF(s.slice(header_len))
        }
    }
}