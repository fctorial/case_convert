val rgx_cap = Regex("^([^A-Z]+)?(([A-Z][^A-Z]*)*)$")
val rgx_cap1 = Regex("([A-Z][^A-Z]*)")
val rgx_cabab = Regex("[-_]")

fun parseWord(s: String): List<String> {
    val ws1 = s.split(rgx_cabab)
    if (ws1.size == 1) {
        val res1 = rgx_cap.find(s)?: return listOf(s)
        val res2 = rgx_cap1.findAll(res1.groups[2]?.value?:return ws1).map {
            it.groups[1]!!.value
        }
        val res = ArrayList<String>()
        if (res1.groups[1] != null) {
            res.add(res1.groups[1]!!.value)
        }
        res.addAll(res2)
        return res.filter { it.isNotBlank() }
    } else {
        return ws1.filter { it.isNotBlank() }
    }
}

fun main() {
    val a = parseWord("hereSnakeCase")
}
