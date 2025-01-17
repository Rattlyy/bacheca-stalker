
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EsitiItem(
    @SerialName("adStuDes")
    val nomeEsame: String? = null,
    @SerialName("dataEsa")
    val dataEsa: String? = null,
    @SerialName("esito")
    val esito: Esito? = null,
    @SerialName("pesoAd")
    val cfu: Int? = null,
) {
    @Serializable
    data class Esito(
        @SerialName("acceptEnabled")
        val acceptEnabled: Int? = null,
        @SerialName("rejectEnabled")
        val rejectEnabled: Int? = null,
        @SerialName("superatoFlg")
        val superatoFlg: Int? = null,
        @SerialName("votoEsa")
        val voto: Int? = null,
    )
}