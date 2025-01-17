package it.rattly

import EsitiItem
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.ParseMode
import fuel.httpGet
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.io.asInputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import kotlin.properties.Delegates

var bot by Delegates.notNull<TelegramBot>()
var matId by Delegates.notNull<Int>()
val env = dotenv()
val id = env["USER_ID"]?.toLongOrNull() ?: error("No user id")
val prefix = env["BASE_URL"] ?: error("No base url") // se mi scrivi su tg ti scrivo come trovarlo, sono un paio di passaggi :)
val headers = mapOf(
    "Authorization" to "Basic ${env["KEY"] ?: error("No key")}" // b64encoded username:password
)

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

@OptIn(ExperimentalSerializationApi::class, DelicateCoroutinesApi::class)
suspend fun main() {
    bot = TelegramBot(env["BOT_TOKEN"]).also { GlobalScope.launch { it.handleUpdates() } }
    matId = json.decodeFromStream<JsonElement>(
        "$prefix/app/init?os=ios&os_version=bho&app_version=bho"
            .httpGet(headers = headers)
            .source.asInputStream()
    )
        .jsonObject["auth_session"]
        ?.jsonObject["careers"]
        ?.jsonArray[0]
        ?.jsonObject["registrationId"]
        ?.jsonPrimitive?.int ?: error("unable to parse mat id")

    val announcedEsiti = mutableListOf<String>()
    while (true) {
        esiti().filterNot { announcedEsiti.contains(it.nomeEsame) }.forEach {
            println(it)
            announcedEsiti.add(it.nomeEsame ?: return@forEach)

            message {
                "<b>\uFE0F NUOVO ESITO USCITO \uFE0F</b>\n\n" +
                        " » Esame: <code>${it.nomeEsame}</code>" +
                        "\n » Esito: <b>${if (it.esito?.superatoFlg == 1) "SUPERATO" else "BOCCIATO"}</b>" +
                        "\n » Voto: <b>${it.esito?.voto}</b>"
            }.options { parseMode = ParseMode.HTML }.send(id, bot)
        }

        delay(10 * 60 * 1000)
    }
}

@OptIn(ExperimentalSerializationApi::class)
suspend fun esiti() = json.decodeFromStream<List<EsitiItem>>(
    "$prefix/students/$matId/exams/board".httpGet(headers = headers).source.asInputStream()
)