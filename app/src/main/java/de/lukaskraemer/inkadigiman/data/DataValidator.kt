package de.lukaskraemer.inkadigiman.data

class DataValidator {

    fun isValidHostname(a: String): Boolean {
        val hostnamewithsubfolder =
            "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*./[a-zA-Z0-9/_+-.]".toRegex()
        val hostnamewithoutsubfolders =
            "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])/[a-zA-Z0-9/_+-.]*".toRegex()
        return hostnamewithsubfolder.matches(a) || hostnamewithoutsubfolders.matches(a)
    }

    fun isValidIP(a: String): Boolean {
        val ipwithsubfolder =
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}/[a-zA-Z0-9/_+-.]*".toRegex()
        val ipwithoutsubfolders =
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}".toRegex()
        return ipwithoutsubfolders.matches(a) || ipwithsubfolder.matches(a)
    }

    fun isValidURL(a: String): Boolean {
        return "([hH]{1}[tT]{2}[p]{1}[s]{0,1}://[a-zA-z0-9\\-.+/:?&)]*)".toRegex().matches(a)
    }

    fun isValidEmail(a: String): Boolean {
        return "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$".toRegex().matches(a)
    }


}
