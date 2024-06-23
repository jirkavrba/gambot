package dev.vrba.discord.gambot.ui

class Asset(
    filename: String,
) {
    private companion object {
        const val GITHUB_CDN_PREFIX = "https://raw.githubusercontent.com/jirkavrba/gambot/main/assets/roulette"
    }

    val url: String = "$GITHUB_CDN_PREFIX/$filename"
}
