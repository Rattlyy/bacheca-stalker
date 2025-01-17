package it.rattly.commands

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.User
import it.rattly.esiti
import it.rattly.id

@CommandHandler(["/start"])
suspend fun start(user: User, bot: TelegramBot) {
    message { "Comandi disponibili: /esiti" }.send(user, bot)
}

@CommandHandler(["/esiti"])
suspend fun esitiCmd(user: User, bot: TelegramBot) {
    if (user.id != id.toLong()) {
        message { "Unauthorized" }.send(user, bot)
        return
    }

    esiti().forEach {
        message {
            "<b>ESITO</b>\n" +
                    "\n » Esame: <code>${it.nomeEsame}</code>" +
                    "\n » CFU: <code>${it.cfu}</code>" +
                    "\n » Data: <code>${it.dataEsa}</code>" +
                    "\n » Superato?: <code>${if (it.esito?.superatoFlg == 1) "SUPERATO" else "BOCCIATO"}</code>" +
                    "\n\n » Voto: <b>${it.esito?.voto}</b>"
        }.options { parseMode = ParseMode.HTML }.send(user, bot)
    }
}