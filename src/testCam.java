import java.net.HttpURLConnection;
public class testCam {
    public static void main(String[] args) throws Exception {
        gestioneTelecamere cam = new gestioneTelecamere("httpbin.org", 80, "/basic-auth/test/passwd");// puo esplodere
        String indirizzo = "http://" + cam.getIp() + ":" + cam.getPorta() + cam.getPath();
        System.out.println("Indirizzo stringa: " + indirizzo);
        HttpURLConnection connettiamoci = cam.apriConnessione();
        int status = connettiamoci.getResponseCode();
        System.out.println(status);
        switch (status) {
            case 200:
                System.out.println("✅ OK ");
                break;
            case 400:
                System.out.println("❌ BAD REQUEST - URL o parametri sbagliati");
                break;
            case 401:
                connettiamoci.disconnect();
                System.out.println("🔐 AUTH required");
                HttpURLConnection conAuth = cam.apriConnessioneauth("test", "passwd");
                int statusAuth = conAuth.getResponseCode();
                System.out.println("Con auth: " + statusAuth);
                break;
            case 404:
                System.out.println("❌ NOT FOUND");
                break;

            default:
                System.out.println("Status: " + status);
        }
        connettiamoci.disconnect();
    }
}