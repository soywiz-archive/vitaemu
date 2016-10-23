package com.soywiz.util

class ELF(
        val s: Stream2,
        val entry: Int,
        val phList: List<ProgramHeader>,
        val shList: List<SectionHeader>
) {
    data class ProgramHeader(
            val s: Stream2,
            val p_type: Int, // type of segment
            val p_offset: Int, // offset in file
            val p_vaddr: Int, // virtual address in memory
            val p_paddr: Int, // reserved
            val p_filesz: Int, // size of segment in file
            val p_memsz: Int, // size of segment in memory
            val p_flags: Int, // segment attributes
            val p_align: Int  // alignment of segment
    ) {
        val fileStream: Stream2 = s.slice(p_offset until p_offset + p_filesz)
        val exec: Boolean get() = ((p_flags ushr 0) and 1) != 0
        val write: Boolean get() = ((p_flags ushr 1) and 1) != 0
        val read: Boolean get() = ((p_flags ushr 2) and 1) != 0

        companion object {
            fun read(s: Stream2) = ProgramHeader(
                    s.slice(),
                    s.readS32_le(), s.readS32_le(), s.readS32_le(), s.readS32_le(),
                    s.readS32_le(), s.readS32_le(), s.readS32_le(), s.readS32_le()
            )
        }
    }

    data class SectionHeader(
            var name: String = "",
            val sh_name: Int, /* section name */
            val sh_type: Int, /* section type */
            val sh_flags: Int, /* section attributes */
            val sh_addr: Int, /* virtual address in memory */
            val sh_offset: Int, /* offset in file */
            val sh_size: Int, /* size of section */
            val sh_link: Int, /* link to other section */
            val sh_info: Int, /* miscellaneous information */
            val sh_addralign: Int, /* address alignment boundary */
            val sh_entsize: Int /* size of entries, if section has table */
    ) {
        fun setNameSH(s: Stream2, strtabSh: SectionHeader) {
            name = s.slice(strtabSh.sh_offset).slice(sh_name).readStringz()
        }

        companion object {
            fun read(s: Stream2) = SectionHeader(
                    "",
                    s.readS32_le(), s.readS32_le(), s.readS32_le(), s.readS32_le(), s.readS32_le(),
                    s.readS32_le(), s.readS32_le(), s.readS32_le(), s.readS32_le(), s.readS32_le()
            )
        }
    }

    companion object {
        fun read(s: Stream2): ELF {
            val magic = s.readS32_le() // Magic
            if (magic != 0x464c457f) invalidOp("Not an elf file")
            val classType = s.readS8() // Class Type [ELFCLASS32 = 1][ELFCLASS64 = 2]
            val dataType = s.readS8() // Data Type [ELFDATA2LSB (i.e. le) = 1][ELFDATA2MSB (i.e. be) = 2]
            if (classType != 1 || dataType != 1) invalidOp("Just supported 32-bit little endian")
            s.readBytes(10)
            val elfType = s.readU16_le()
            val elfMachine = s.readU16_le()
            if (elfMachine != 0x28) invalidOp("Just supported 0x28 as machine")
            val elfVersion = s.readS32_le()
            println("$elfType, $elfMachine, $elfVersion")

            val e_entry = s.readS32_le()       // entry point address
            val e_phoff = s.readS32_le()       // program header offset
            val e_shoff = s.readS32_le()       // section header offset
            val e_flags = s.readS32_le()       // processor-specific flags
            val e_ehsize = s.readS16_le()      // ELF header size
            val e_phentsize = s.readS16_le()   // size of program header entry
            val e_phnum = s.readS16_le()       // number of program header entries
            val e_shentsize = s.readS16_le()   // size of section header entry
            val e_shnum = s.readS16_le()       // number of section header entries
            val e_shstrndx = s.readS16_le()    // section name string table index

            val phList = (0 until e_phnum).map { ProgramHeader.read(s.slice(e_phoff + it * e_phentsize)) }
            val shList = (0 until e_shnum).map { SectionHeader.read(s.slice(e_shoff + it * e_shentsize)) }

            for (sh in shList) sh.setNameSH(s, shList[e_shstrndx])

            //for (ph in phList) println(ph)
            //for (sh in shList) println(sh)

            return ELF(s, e_entry, phList, shList)
        }
    }

    fun writeTo(out: Stream2) {
        for (ph in phList) {
            out.position = ph.p_paddr.toLong()
            println("::" + ph.p_paddr.toLong() + " -> " + ph.fileStream.length)
            out.writeStream(ph.fileStream)
        }
    }
}