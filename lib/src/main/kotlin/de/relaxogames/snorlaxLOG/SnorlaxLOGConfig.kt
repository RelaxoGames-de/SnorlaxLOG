class SnorlaxLOGConfig {
    private val url: String
    private val username: String
    private val password: String

    constructor(url: String, username: String, password: String) {
        this.url = url
        this.username = username
        this.password = password
    }

    fun getUrl(): String {
        return url
    }

    fun getUsername(): String {
        return username
    }

    fun getPassword(): String {
        return password
    }
}
