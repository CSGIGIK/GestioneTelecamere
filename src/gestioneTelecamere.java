import java.net.URL; // Crea indirizzi web tipo "http://192.168.1.100:80" per la telecamera
import java.net.HttpURLConnection; // Il collegamento che manda richieste HTTP alla telecamera
import java.util.Base64; // Trasforma "admin:1234" → "YWRtaW46MTIzNA==" (pacchetto credenziali sicuro)
/*FUNZIONI:
• Prende IP + PORTA + PATH (es: "httpbin.org", 80, "/basic-auth/test/passwd")
• apriConnessione() = BUSSA alla porta
• apriConnessioneauth() = BUSSA + infila password nella busta
• Timeout 3 secondi = "Se non apri veloce me ne vado"
*/


public class gestioneTelecamere {  // Classe per controllare UNA telecamera
    private String path = "";
    private String ip;    // Indirizzo IP della telecamera (es: "192.168.1.100")
    private int porta;    // Porta HTTP della telecamera (es: 80)

    public gestioneTelecamere(String ip, int porta, String percorso) {
        this.ip = ip.trim();    // Pulizia spazi!
        this.porta = porta;
        this.path= percorso;
    }
    //getter
    public String getPath() {
        return  this.path;
    }
    public String getIp() {
        return this.ip;
    }
    public int getPorta() {
        return this.porta;
    }
    // metodi tunnel e Base64
    public HttpURLConnection apriConnessione() throws Exception {
        // 1. costruisco nella variabile string indirizzzo IP o Dominio del server a cui voglio connettermi
        String indirizzo = "http://" + this.ip + ":" + this.porta + this.path; // Esempio: "http://192.168.1.100:80/..."
        // 2. IP+porta → URL TRASFORMO in oggetto URL
        URL url = new URL(indirizzo); //import java.net.URL;← QUESTO!
        // 3. APRO IL COLLEGAMENTO (cavo generico)
        // 4. CAST a HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // import java.net.HttpURLConnection;
        //BUSSIAMO All'indirizzo url POSSIBILI RISPOSTE HTTP:
        // 200=OK  | 401=Password!  | 404=IP sbagliato  | -1=Timeout
        conn.setConnectTimeout(3000); //Ho bussato non apre in 5 secondi ME NE VADO
        conn.setReadTimeout(3000); //Mi ha aperto ma in modo inquietante mi ha fissato senza dire nulla me ne vado
        return conn;
    }

    public HttpURLConnection apriConnessioneauth(String usrnm, String pwd) throws Exception {
        HttpURLConnection conn = apriConnessione();  // Apro connessione grazie a metodo esistente
        String credenziali = usrnm + ":" + pwd; // Costruisco "username:password" (formato Basic Auth standard universale)
        String authBase64 = Base64.getEncoder().encodeToString(credenziali.getBytes());
       /*
                              ↓                    ↓                    ↓
                        1. getEncoder()       2. encodeToString()  3. credenziali.getBytes()
                        "Dammi encoder"       "Codifica → String"   "admin:admin" → byte[11]
       */
        conn.setRequestProperty("Authorization","Basic " + authBase64);
        return conn;
    }

}

